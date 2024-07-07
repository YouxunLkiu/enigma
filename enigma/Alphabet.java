package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Youxun LIu
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        char check;
        for (int i = 0; i < chars.length(); i++) {
            check = chars.charAt(i);
            for (int j = i + 1; j < chars.length(); j++) {
                if (check == chars.charAt(j)) {
                    throw new EnigmaException("Dt");
                }
            }
        }
        _chars = chars;
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.contains(String.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int rt = 0;
        for (int i = 0; i < _chars.length(); i++) {
            if (_chars.charAt(i) == ch) {
                rt = i;
                break;
            }
        }
        return rt;
    }

    /**
     * setting for strings in this class.
     */
    private String _chars;
}
