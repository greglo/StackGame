/**
 * 
 */
package ox.stackgame.ui;

import ox.stackgame.challenge.AbstractChallenge;

/**
 * Allows the user to create a program to solve a specific challenge (limited
 * instruction set, well defined success conditions, specified max stack size).
 * 
 * At the moment, it stores the current challenge.
 * 
 * @author danfox
 * @author rgossiaux
 * 
 */
public class ChallengeMode extends DesignMode {
    protected AbstractChallenge challenge;

    public void accept(ModeVisitor v) {
        v.visit(this);
    }

    public ChallengeMode() {
        challenge = null;
    }

    public ChallengeMode(AbstractChallenge challenge) {
        this.challenge = challenge;
    }

    public AbstractChallenge getChallenge() {
        return challenge;
    }

}
