package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Youxun Liu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = _cycles + '(' + cycle + ')';
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw new EnigmaException("Character Not included");
        }
        char rt = p;
        if (_cycles.length() == 0) {
            return rt;
        }
        char store = _cycles.charAt(0);
        for (int i = 0; i < _cycles.length(); i++) {
            char check = _cycles.charAt(i);
            if (check == '(') {
                store = _cycles.charAt(i + 1);
            } else if (check == rt && _cycles.charAt(i + 1) != ')') {
                rt = _cycles.charAt(i + 1);
                break;
            } else if (check == rt && _cycles.charAt(i + 1) == ')') {
                rt = store;
                break;
            }
        }
        return rt;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Character Not included");
        }
        char rt = c;
        if (_cycles.length() == 0) {
            return rt;
        }
        boolean incycle = false;
        char store = _cycles.charAt(0);
        for (int i = 0; i < _cycles.length(); i++) {
            char check = _cycles.charAt(i);
            if (check == c && _cycles.charAt(i - 1) == '(') {
                incycle = true;
            } else if (check == ')' && incycle) {
                rt = _cycles.charAt(i - 1);
                break;
            }
            if (check == c && _cycles.charAt(i - 1) != '(') {
                rt = _cycles.charAt(i - 1);
                break;
            }
        }
        return rt;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int i = 0;
        while (i < _cycles.length() - 2) {
            if (_cycles.charAt(i) == '(' && _cycles.charAt(i + 2) == ')') {
                return true;
            }
            i += 1;
        }
        return false;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** filed for cycles. */
    private String _cycles;
}
