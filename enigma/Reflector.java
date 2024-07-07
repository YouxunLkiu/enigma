package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Youxun LIu
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _perm = perm;
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }
    /** Return my permutation. */
    Permutation permutation() {
        return _perm;
    }

    @Override
    public String toString() {
        return "Refector " + _name;
    }

    /** filed for name. */
    private String _name;
    /** file for perm. */
    private Permutation _perm;
}
