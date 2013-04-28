package ox.stackgame.blockUI;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ox.stackgame.stackmachine.instructions.Instruction;

/**
 * This class manages the selection of a set of cave elements. It implements the
 * Observer pattern and can therefore be used as a model in a
 * Model-View-Controller framework.
 * 
 * @param <E>	determines the type of objects that are being selected
 */
public class SelectionManager {

	protected final List<SelectionManagerListener> listeners;
	protected final List<Instruction> selection;
//	protected Cave currentCave;
//	protected int currentCave;
//	protected boolean currentWithShadow;
	protected boolean active;
	
	public SelectionManager() {
		listeners = new ArrayList<SelectionManagerListener>();
		selection = new ArrayList<Instruction>();
		active = false;
	}
//	public Cave getCurrentCave() {
//		return currentCave;
//	}
//	public void setCurrentCave(Cave newCurrentCave) {
	//	currentCave = newCurrentCave;
/*		if (currentCave.getNumberOfCaves() > 0)
			currentCave = 0;
		else
			currentCave = -1;
		selection.clear();*/
//		for (SelectionManagerListener l : listeners)
//			l.currentCaveChanged(currentCave);
//	}
/*	public int getCurrentCave() {
		return currentCave;
	}*/
/*	public void setCurrentCave(int newCurrentCave) {
		if (currentCave != null && newCurrentCave < currentCave.getNumberOfCaves())
			currentCave = newCurrentCave;
		else
			currentCave = -1;
		selection.clear();
		for (SelectionManagerListener l : listeners)
			l.currentCaveChanged(currentCave);
	}*/
	public boolean isSelectionEmpty() {
		return selection.isEmpty();
	}
	public Iterator<Instruction> getSelection() {
		return selection.iterator();
	}
	public boolean isSelected(Instruction object) {
		return selection.contains(object);
	}
	public void addListener(SelectionManagerListener l) {
		listeners.add(l);
	}
	public void removeListener(SelectionManagerListener l) {
		listeners.remove(l);
	}
	public void selectObject(Instruction e) {
		if (selection.add(e))
			fireObjectsSelected(Collections.singleton(e));
	}
	public void selectObjects(Collection<? extends Instruction> objects) {
		selection.addAll(objects);
		fireObjectsSelected(objects);
	}
	protected void fireObjectsSelected(Collection<? extends Instruction> objects) {
		for (SelectionManagerListener l : listeners)
			l.objectsSelected(objects);
	}
	public void unselectObject(Instruction object) {
		if (selection.remove(object))
			fireObjectsUnselected(Collections.singleton(object));
	}
	public void unselectObjects(Collection<? extends Instruction> objects) {
		selection.removeAll(objects);
		fireObjectsUnselected(objects);
	}
	protected void fireObjectsUnselected(Collection<? extends Instruction> objects) {
		for (SelectionManagerListener l : listeners)
			l.objectsUnselected(objects);
	}
	
	public void toggleObjectSelection(Instruction object) {
		if (isSelected(object))
			unselectObject(object);
		else
			selectObject(object);
	}
	public void clear() {
		selection.clear();
		for (SelectionManagerListener l : listeners)
			l.selectionCleared();
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public boolean getActive(){
		return active;
	}

}
