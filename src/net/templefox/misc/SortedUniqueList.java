package net.templefox.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedUniqueList<T extends Comparable<T>> extends ArrayList<T> {
	public SortedUniqueList() {
		super();
	}
	public SortedUniqueList(List<T> list) {
		super(list);
		Collections.sort(list);
	}
	
	@Override
	public boolean add(T e) {
		int index = Collections.binarySearch(this, e);
		if (index>=0) {
			remove(index);
			add(index, e);
		}else {
			add(-index-1,e);
		}
		return true;
	}
}
