package io.cubyz.util;

import java.lang.reflect.Array;
import java.util.Comparator;

// A faster list implementation. Velocity is reached by sacrificing bound checks, by keeping some additional memory(When removing elements they are not necessarily cleared from the array) and through direct data access.

public class FastList<T> {

	public T[] array;
	public int size = 0;
	private static final int arrayIncrease = 20; // this allow to use less array re-allocations

	@SuppressWarnings("unchecked")
	public FastList(int initialCapacity, Class<T> type) {
		array = (T[])Array.newInstance(type, initialCapacity);
	}

	public FastList(Class<T> type) {
		this(10, type);
	}

	@SuppressWarnings("unchecked")
	public void increaseSize(int increment) {
		T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + increment);
		System.arraycopy(array, 0, newArray, 0, array.length);
		array = newArray;
	}

	@SuppressWarnings("unchecked")
	public void trimToSize() {
		T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), size);
		System.arraycopy(array, 0, newArray, 0, size);
		array = newArray;
	}
	
	public void set(int index, T obj) {
		array[index] = obj;
	}
	
	public void add(T obj) {
		if (size == array.length)
			increaseSize(arrayIncrease);
		array[size] = obj;
		size++;
	}
	
	public void remove(int index) {
		System.arraycopy(array, index, array, index-1, array.length-index-1);
		size--;
	}
	
	public void remove(T t) {
		for(int i = size-1; i >= 0; i--) {
			if(array[i] == t)
				remove(i); // Don't break here in case of multiple occurrence.
		}
	}
	
	public boolean contains(T t) {
		for(int i = size-1; i >= 0; i--) {
			if(array[i] == t)
				return true;
		}
		return false;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void sort(Comparator<T> comp) {
		/*
		// TODO: use more efficient than bubble sort
		for (int i = size-1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (comp.compare((T) array[j], (T) array[j + 1]) > 0) {
					Object temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
		*/
		if (size > 0) {
			sort(comp, 0, size-1);
		}
	}
	
	private void sort(Comparator<T> comp, int l, int r) {
		int i = l, j = r;
		
		T x = array[(l+r)/2];
		while (true) {
			while (comp.compare((T) array[i], x) < 0) {
				i++;
			}
			while (comp.compare(x, (T) array[j]) < 0) {
				j--;
			}
			if (i <= j) {
				T temp = array[i];
				array[i] = array[j];
				array[j] = temp;
				i++;
				j--;
			}
			if (i > j) {
				break;
			}
		}
		if (l < j) {
			sort(comp, l, j);
		}
		if (i < r) {
			sort(comp, i, r);
		}
	}
	
	/**
	 * Sets the size to 0, meaning {@link RenderList#trimToSize()} should be called in order to free memory.
	 */
	public void clear() {
		size = 0;
	}

}