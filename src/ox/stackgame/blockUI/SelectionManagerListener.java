package ox.stackgame.blockUI;

import java.util.Collection;

import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * Receives information about a change in the set of selected objects.
 * 
 * @param <E> 			the type of selected objects
 */
public interface SelectionManagerListener {
	/**
	 * Notifies the listener that the specified objects have been
	 * added to the selection.
	 * 
	 * @param objects				the selected objects
	 */
	void objectsSelected(Collection<? extends Instruction> objects);
	/**
	 * Notifies the listener that the specified objects have been
	 * removed from the selection.
	 * 
	 * @param objects				the unselected objects
	 */
	void objectsUnselected(Collection<? extends Instruction> objects);
	/**
	 * Notifies the listener that all objects have been unselected.
	 */
	void selectionCleared();


}
