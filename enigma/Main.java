package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Youxun Liu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine amachine = readConfig();
        while (_input.hasNextLine()) {
            String linefrominput = _input.nextLine();
            if (linefrominput.contains("*")) {
                setUp(amachine, linefrominput);
            } else if (linefrominput.isEmpty()) {
                _output.println();
            } else {
                String nospace = linefrominput.replaceAll("\\s+",
                        "");
                String converted = amachine.convert(nospace);
                printMessageLine(converted);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong input, no mumrotor");
            }
            int numrt = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("WRonginput, no pawls");
            }
            int pawls = _config.nextInt();
            Collection<Rotor> rotors = new ArrayList<>();
            while (_config.hasNext()) {
                if (rotors.add(readRotor())) {
                    continue;
                } else {
                    throw new EnigmaException("ROTOR IS aready added stpuid");
                }
            }
            return new Machine(_alphabet, numrt, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            if (name.equals(")") || name.equals("(")) {
                throw new EnigmaException("name is wrong stupid");
            }
            String typeApawls = _config.next();
            String cycles = "";
            while (_config.hasNext("\\(.+\\)")) {
                cycles = cycles + _config.next();
            }
            Permutation permutation = new Permutation(cycles, _alphabet);
            if (typeApawls.length() == 1) {
                if (typeApawls.equals("N")) {
                    return new FixedRotor(name, permutation);
                } else if (typeApawls.equals("R")) {
                    return new Reflector(name, permutation);
                } else if (typeApawls.equals("M")) {
                    return new MovingRotor(name, permutation, "");
                } else {
                    throw new NoSuchElementException();
                }
            } else if (typeApawls.charAt(0) == 'M') {
                String pawls = typeApawls.substring(1);
                return new MovingRotor(name, permutation, pawls);
            } else {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner setups = new Scanner(settings);
        String[] rotorset = new String[M.numRotors()];
        String begin = setups.next();
        if (!begin.equals("*")) {
            throw new EnigmaException("You are fucked");
        }
        String reflname = setups.next();
        if (!M.getAllrotormap().containsKey(reflname)) {
            throw new EnigmaException("Reflector does not exist");
        }
        rotorset[0] = reflname;
        int mradded = 0;
        for (int i = 1; i < M.numRotors(); i++) {
            String name = setups.next();
            if (!M.getAllrotormap().containsKey(name)) {
                throw new EnigmaException("Rotor name does not exist "
                        + "in all rotors");
            } else if (M.getAllrotormap().get(name).reflecting()) {
                throw new EnigmaException("Double reflector");
            } else if (M.getAllrotormap().get(name).rotates()) {
                mradded += 1;
            }
            for (String names: rotorset) {
                if (names == null) {
                    break;
                }
                if (name.equals(names)) {
                    throw new EnigmaException("Rotor is added twice");
                }
            }
            rotorset[i] = name;
        }
        if (mradded != M.numPawls()) {
            throw new EnigmaException("pawl number not equals to"
                    + " the number of MR added");
        }
        M.insertRotors(rotorset);
        String rotorsetting = "";
        String ringsetting = "";
        String permcycle = "";
        if (!setups.hasNext() || setups.hasNext("\\(")) {
            throw new EnigmaException("There is not initial setting");
        }
        while (setups.hasNext()) {
            String nex = setups.next();
            if (!nex.contains("(")) {
                rotorsetting = nex;
            } else if (nex.contains("(")) {
                permcycle = permcycle + nex;
            }
        }
        Permutation plugboard = new Permutation(permcycle, _alphabet);
        M.setPlugboard(plugboard);
        M.setRotors(rotorsetting);
        if (setups.hasNext()) {
            throw new EnigmaException("Error setting wrong length");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int head = 0;
        String out = "";
        while (head < msg.length()) {
            if (head + 5 <= msg.length()) {
                out += msg.substring(head, head + 5) + " ";
                head += 5;
            } else {
                out += msg.substring(head);
                head += 5;
            }
        }
        _output.println(out);
    }
    /** rt setting while check SETTING.
     * @return String

     * */
    String settingerror(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("Error in setting"
                        + " not in alphabet");
            }
        }
        return setting;
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}

