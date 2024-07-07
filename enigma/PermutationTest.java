package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author HAHAH
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkDerangement() {
        Alphabet selfmap = new Alphabet("A");
        perm = new Permutation("(A)", selfmap);
        assertTrue(perm.derangement());
        Alphabet notselfmap = new Alphabet("AB");
        perm = new Permutation("(AB)", notselfmap);
        assertFalse((perm.derangement()));
        perm = new Permutation(NAVALA.get("I"), new Alphabet());
        assertTrue(perm.derangement());
        perm = new Permutation(NAVALA.get("C"), new Alphabet());
        assertFalse(perm.derangement());
    }

    @Test
    public void test1() {
        Alphabet all = new Alphabet();
        perm = new Permutation(NAVALA.get("I"), all);
        assertEquals(26, perm.size());
        assertEquals('E', perm.permute('A'));
        assertEquals('U', perm.permute('R'));
        assertEquals('A', perm.permute('U'));
        assertEquals('S', perm.permute('S'));
        assertEquals('S', perm.invert('S'));
        assertEquals('A', perm.invert('E'));
        assertEquals('U', perm.invert('A'));
        assertEquals('Z', perm.permute('J'));
        assertEquals('J', perm.permute('Z'));
        assertEquals('J', perm.invert('Z'));
        assertEquals('Z', perm.invert('J'));

        assertEquals(all.toInt('E'), perm.permute(all.toInt('A')));
        assertEquals(all.toInt('U'), perm.permute(all.toInt('R')));
        assertEquals(all.toInt('A'), perm.permute(all.toInt('U')));
        assertEquals(all.toInt('S'), perm.permute(all.toInt('S')));
        assertEquals(all.toInt('S'), perm.invert(all.toInt('S')));
        assertEquals(all.toInt('A'), perm.invert(all.toInt('E')));
        assertEquals(all.toInt('U'), perm.invert(all.toInt('A')));
        assertEquals(all.toInt('Z'), perm.permute(all.toInt('J')));
        assertEquals(all.toInt('J'), perm.permute(all.toInt('Z')));
        assertEquals(all.toInt('J'), perm.invert(all.toInt('Z')));
        assertEquals(all.toInt('Z'), perm.invert(all.toInt('J')));
    }

    @Test(expected = EnigmaException.class)
    public void test() {
        alpha = "ASDASDASD";
        Alphabet alphab = new Alphabet(alpha);
        perm = new Permutation("(ASD)", alphab);
    }

    @Test(expected = EnigmaException.class)
    public void testnotinAlphabet() {
        alpha = "ASD";
        Alphabet alphab = new Alphabet(alpha);
        perm = new Permutation("(ASD)", alphab);
        perm.invert('F');
    }

    @Test
    public void testnegative() {
        alpha = "ASD";
        Alphabet alphab = new Alphabet(alpha);
        perm = new Permutation("(ASD)", alphab);
        assertEquals(0, perm.permute(-1));
        assertEquals(1, perm.invert(-1));
    }

    @Test
    public void testnocycle() {
        alpha = "ASDB";
        Alphabet alphab = new Alphabet(alpha);
        perm = new Permutation("(ASD)", alphab);
        assertEquals('B', perm.permute('B'));
        assertEquals('B', perm.invert('B'));
    }
}
