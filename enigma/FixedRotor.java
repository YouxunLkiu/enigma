package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Youxun Liu
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _setting = 0;
        _perm = perm;
    }

    /** returns the setting.
     */
    int setting() {
        return _setting;
    }


    @Override
    void set(int posn) {
        _setting = posn;
    }
    @Override
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** returning th permutaiton of this rotor.
     *
     */
    Permutation permutation() {
        return _perm;
    }

    /** overriding the tostring of a fixrotor.
     */
    @Override
    public String toString() {
        return "FixedRotor " + _name;
    }

    /** private filed for the rotor name.
     */
    private String _name;
    /** private field for the permutation.
     */
    private Permutation _perm;

    /** private field for the setting.
     */
    private int _setting;
}
