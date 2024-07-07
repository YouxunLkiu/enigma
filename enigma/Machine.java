package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Youxun Liu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (!(pawls < numRotors) || !(pawls > 0)) {
            throw new EnigmaException("Mal input on pawls stupid");
        }
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allrotors = allRotors;
        _rotorlist = new ArrayList<>();
        _usedrotormap = new HashMap<>();
        _allrotormap = new HashMap<>();
        for (Rotor rot: _allrotors) {
            _allrotormap.put(rot.name(), rot);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotorlist.clear();
        HashMap<String, Rotor> temp = new HashMap<>();
        for (Rotor rot: _allrotors) {
            temp.put(rot.name(), rot);
        }
        if (rotors.length !=  _numRotors) {
            throw new EnigmaException("Wrong String input");
        }
        if (!temp.get(rotors[0]).reflecting()) {
            throw new EnigmaException("First one is not reflector");
        }
        for (int i = 0; i < _numRotors; i++) {
            _usedrotormap.put(rotors[i], temp.get(rotors[i]));
        }
        for (String name: rotors) {
            _rotorlist.add(_usedrotormap.get(name));
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int index;
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Incorrect setting input");
        }
        for (index = 0; index < _numRotors - 1; index++) {
            _rotorlist.get(index + 1).set(setting.charAt(index));
        }
    }
    /** set my ringsetting for rotor with SETTING.  */
    void setRing(String setting) {
        int index;
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Incorrect setting input");
        }
        for (index = 0; index < _numRotors - 1; index++) {
            _rotorlist.get(index + 1).ringset(setting.charAt(index));
        }
    }
    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (_rotorlist.size() == 0) {
            throw new EnigmaException("There ain't no rotor in this btich. "
                    + "You haven't called insertrotor");
        }
        boolean[] canmove = new boolean[_numRotors];
        for (int i = 0; i < _numRotors; i += 1) {
            if ((i == _numRotors - 1) || (_rotorlist.get(i).rotates()
                    && _rotorlist.get(i + 1).atNotch())) {
                canmove[i] = true;
            } else if (i != 0) {
                canmove[i] = canmove[i - 1];
            }
        }
        for (int i = 0; i < _numRotors; i++) {
            if (canmove[i]) {
                _rotorlist.get(i).advance();
            }
        }
        c = _plugboard.permute(c);
        for (int forward = _numRotors - 1; forward != 0; forward--) {
            c = _rotorlist.get(forward).convertForward(c);
        }
        for (int back = 0; back < _numRotors; back++) {
            c = _rotorlist.get(back).convertBackward(c);
        }
        return _plugboard.invert(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String rt = "";
        for (int i = 0; i < msg.length(); i++) {
            rt = rt + _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return rt;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** referancing the arraylist rotor machine.
     * @return _rotorlist
     * */
    public ArrayList<Rotor> getRotorlist() {
        return _rotorlist;
    }

    /** all rotor map calling.
     * @return _allrotormap
     * */
    public HashMap<String, Rotor> getAllrotormap() {
        return _allrotormap;
    }

    /** the number of rotors. */
    private int _numRotors;

    /** The number of pawls. */
    private int _pawls;

    /** the collections of rotors.
     *
     */
    private Collection<Rotor> _allrotors;
    /** the plug board.
     *
     */
    private Permutation _plugboard;
    /** the storing of rotors in an array list.
     *
     */
    private ArrayList<Rotor> _rotorlist;
    /** Hashmap storing of rotors.
     *
     */
    private HashMap<String, Rotor> _usedrotormap;
    /** All rotor map.
     *
     */
    private HashMap<String, Rotor> _allrotormap;
}

