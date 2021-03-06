/**
 * 
 */
package ox.stackgame.ui;

/**
 * Visit a Mode for some purpose (after activation or deactivation). Within the
 * mode, visit(this) is called.
 * 
 * @author danfox
 * 
 */
public interface ModeVisitor {
    public void visit(ChallengeMode m);
    public void visit(RunMode m);
    public void visit(FreeDesignMode m);
}
