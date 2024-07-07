package enigma;

import org.junit.Test;
import ucb.junit.textui;
import static enigma.TestUtils.*;
import static org.junit.Assert.assertEquals;
import java.util.Collection;
import java.util.ArrayList;

/** The suite of all JUnit tests for the enigma package.
 *  @author Youxun Liu
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(PermutationTest.class,
                MovingRotorTest.class));
    }

    @Test
    public void alphabetTests() {
        Alphabet simple = new Alphabet("ABCD");
        assertEquals("Wrong length", 26, UPPER.size());
        assertEquals(65, UPPER.toChar(0));
        assertEquals(0, UPPER.toInt((char) 65));
        assertEquals(2, UPPER.toInt((char) 67));
        assertEquals('Z', UPPER.toChar(25));
        assertEquals('X', UPPER.toChar(23));
        assertEquals(4, simple.size());
        assertEquals(65, simple.toChar(0));
        assertEquals(0, simple.toInt((char) 65));
        assertEquals(2, simple.toInt((char) 67));
        assertEquals(true, simple.contains('A'));
        assertEquals(false, simple.contains('E'));
    }

    @Test
    public void fixedRotorTests() {
        Alphabet simple = new Alphabet("ABCD");
        FixedRotor easy = new FixedRotor("Easy", new Permutation("", simple));
        easy.set(1);
        assertEquals(1, easy.setting());
        easy.advance();
        assertEquals(1, easy.setting());
    }


    /** Following tests test the machine class of the enigma
     */
    @Test
    public void simplemachineTest() {
        Alphabet simple = new Alphabet("ABCD");
        Permutation plugboard = new Permutation("(AD)(CB)", simple);
        Permutation first = new Permutation("(AC)(BD)", simple);
        Rotor one = new MovingRotor("One", first, "ABCD");
        Permutation second = new Permutation("(AB)(CD)", simple);
        Rotor two = new MovingRotor("Two", second, "C");
        Permutation third = new Permutation("(ADCB)", simple);
        Rotor three = new MovingRotor("Three", third, "C");
        Permutation ref = new Permutation("(AD)(CB)", simple);
        Rotor refl = new Reflector("Ref", ref);
        Collection<Rotor> set = new ArrayList<>();
        set.add(refl);
        set.add(three);
        set.add(two);
        set.add(one);
        Machine simp = new Machine(simple, 4, 3, set);
        simp.setPlugboard(plugboard);
        String[] names = {"Ref", "Three", "Two", "One"};
        simp.insertRotors(names);
        assertEquals(simp.numRotors(), simp.getRotorlist().size());
        assertEquals(simp.getRotorlist().get(0), refl);
        assertEquals(simp.getRotorlist().get(1), three);
        assertEquals(simp.getRotorlist().get(3), one);
        assertEquals(2, simp.convert(3));
        assertEquals(1, simp.convert(0));
        simp.setRotors("AAA");
        assertEquals(1, simp.convert(0));
        simp.setRotors("AAA");
        assertEquals("BAD", simp.convert("ABC"));
        simp.setRotors("AAA");
        assertEquals("ABC", simp.convert("BAD"));

    }

    @Test(expected = EnigmaException.class)
    public void erroredinser() {
        Alphabet simple = new Alphabet("ABCD");
        Permutation plugboard = new Permutation("(AD)(CB)", simple);
        Permutation first = new Permutation("(AC)(BD)", simple);
        Rotor one = new MovingRotor("One", first, "ABCD");
        Permutation second = new Permutation("(AB)(CD)", simple);
        Rotor two = new MovingRotor("Two", second, "C");
        Permutation third = new Permutation("(ADCB)", simple);
        Rotor three = new MovingRotor("Three", third, "C");
        Permutation ref = new Permutation("(AD)(CB)", simple);
        Rotor refl = new Reflector("Ref", ref);
        Collection<Rotor> set = new ArrayList<>();
        set.add(refl);
        set.add(three);
        set.add(two);
        set.add(one);
        Machine simp = new Machine(simple, 4, 3, set);
        simp.setPlugboard(plugboard);
        String[] names = {"Ref", "Three", "Three", "One"};
        simp.insertRotors(names);
    }

    @Test
    public void complexmachinetest() {
        Alphabet complex = new Alphabet();
        Permutation I = new Permutation(NAVALA.get("I"), complex);
        Permutation B = new Permutation(NAVALA.get("B"), complex);
        Permutation C = new Permutation(NAVALA.get("C"), complex);
        Rotor one = new MovingRotor("I", I, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Rotor ref = new Reflector("ref", C);
        Collection<Rotor> set = new ArrayList<>();
        set.add(ref);
        set.add(one);
        Machine comp = new Machine(complex, 2, 1, set);
        comp.setPlugboard(B);
        String[] names = {"ref", "I"};
        comp.insertRotors(names);
        assertEquals(complex.toInt('T'), comp.convert(complex.toInt('X')));

    }
    @Test
    public void integrationtest() {
        Alphabet reg = new Alphabet();
        Collection<Rotor> set = new ArrayList<>();
        set.add(new Reflector("B",
                new Permutation(NAVALA.get("B"), reg)));
        set.add(new FixedRotor("Beta",
                new Permutation(NAVALA.get("Beta"), reg)));
        set.add(new MovingRotor("I",
                new Permutation(NAVALA.get("I"), reg), "Q"));
        set.add(new MovingRotor("II",
                new Permutation(NAVALA.get("II"), reg), "E"));
        set.add(new MovingRotor("III",
                new Permutation(NAVALA.get("III"), reg), "V"));
        Machine enigma = new Machine(reg, 5, 3, set);
        String[] rotors = {"B", "Beta", "I", "II", "III"};
        enigma.insertRotors(rotors);
        assertEquals(enigma.numRotors(), enigma.getRotorlist().size());
        enigma.setRotors("AAAA");
        enigma.setPlugboard(new Permutation("", reg));
        assertEquals("ILBDAAMTAZ", enigma.convert("HELLOWORLD"));
        enigma.setPlugboard(new Permutation("(AQ) (EP)", reg));
        enigma.setRotors("AAAA");
        String encode = enigma.convert("HELLOWORLD");
        enigma.setRotors("AAAA");
        assertEquals("HELLOWORLD", enigma.convert(encode));
    }
    /** Following tests test the main class of the enigma
     *
     */

    @Test
    public void mainTests() {
        String[] args = {"default.conf", "trivial.in", "trivial.out"};
        Main amain = new Main(args);
    }

}

