package gui;

import gcom.Debug;
import java.util.Vector;
import javax.swing.AbstractListModel;

public class MockListModel<T> extends AbstractListModel {
	private Vector<T> model;

	public MockListModel(Vector<T> model) {
		this.model = model;
	}

	@Override
	public int getSize() {
		return model.size();
	}

	@Override
	public T getElementAt(int arg0) {
		return model.elementAt(arg0);
	}

	public void add(T obj) {
		Debug.log(this, Debug.TRACE, "Added message to queue");
		model.add(obj);
		fireContentsChanged(this, 0, model.size()-1);
	}

	public void moveUp(int index) {
		T tmp = model.remove(index);
		model.insertElementAt(tmp, index-1);
		fireContentsChanged(this, index, index-1);
	}

	public void moveDown(int index) {
		T tmp = model.remove(index);
		model.insertElementAt(tmp, index+1);
		fireContentsChanged(this, index, index+1);
	}

	public T drop(int index) {
		T tmp = model.remove(index);
		fireContentsChanged(this, index, index);
		return tmp;
	}

	public void update() {
		fireContentsChanged(this, 0, model.size());
	}
}

