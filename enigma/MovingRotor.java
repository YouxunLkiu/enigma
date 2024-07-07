package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Youxun Liu
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _perm = perm;
        _notches = notches;
        _name = name;
        _setting = 0;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        if (rotates()) {
            _setting += 1;
        }
    }
    /** true if the thing is atnotch.*/
    @Override
    boolean atNotch() {
        boolean rt = false;
        int notch = permutation().wrap(_setting);
        for (int i = 0; i < _notches.length(); i++) {
            if (_perm.alphabet().toInt(_notches.charAt(i)) == notch) {
                rt = true;
                break;
            }
        }
        return rt;
    }
    /** give the setting. */
    @Override
    int setting() {
        return _setting;
    }
    @Override
    /** setting up at posn.*/
    void set(int posn) {
        _setting = posn;
    }
    @Override
    /** setting up at char.*/
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }
    /** changing the stringe method.*/
    @Override
    public String toString() {
        return "MovingRotor " + _name;
    }
    /** field for perm.*/
    private Permutation _perm;
    /** field for name.*/
    private String _name;
    /** field for notches.*/
    private String _notches;
    /** field for setting.*/
    private int _setting;

}
