package net.templefox.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedUniqueList<T extends Comparable<T>> extends ArrayList<T> {
	Comparator<T> comparator = null;
	public SortedUniqueList() {
		super();
	}
	
	public SortedUniqueList(Comparator<T> comparator){
		this();
		this.comparator = comparator;
	}

	public SortedUniqueList(List<T> list) {
		super(list);
		Collections.sort(list);
	}
	
	public SortedUniqueList(List<T> list,Comparator<T> comparator) {
		this(list);
		this.comparator = comparator;
		if(comparator!=null){
			Collections.sort(list,comparator);
		}
		else {
			Collections.sort(list);
		}
	}
	
	@Override
	public boolean add(T e) {
		int index = 0;
		if(comparator!=null){
			index = Collections.binarySearch(this,e,comparator);
		}
		else {
			index = Collections.binarySearch(this,e);
		}
		if (index>=0) {
			remove(index);
			add(index, e);
		}else {
			add(-index-1,e);
		}
		return true;
	}
}
