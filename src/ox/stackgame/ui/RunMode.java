/**
 * 
 */
package ox.stackgame.ui;

/**
 * @author danfox
 */
public class RunMode extends Mode {

    public void accept(ModeVisitor v) {
        v.visit(this);
    }

}
