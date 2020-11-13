package com.bc;

import java.util.Comparator;
import java.util.Iterator;

/*
 * Date: 11/12/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class stores a generic linked list that uses the GenericNode class for the nodes. It contains the basic methods
 * that are needed to create, access, and maintain a linked list, and a method to sort the list as you add items (addItemSorted)
 */

public class GenericList<T extends Comparator <T>> implements Iterable<T> {
	
	private GenericNode<T> head;
	public int size;
	
	public GenericList() {
		this.head = null;
		this.size = 0;
	}
	
	public int getSize() {
	// Returns the size of the list
		return this.size;
	}
	
	@SuppressWarnings("unchecked")
	public void addItemSorted(T item) {
	// Adds the given object to the list, with its position based on the object type's comparator (sorted highest to lowest)
		
		GenericNode<T> curr = this.head;
		GenericNode<T> next = null;
		
		if (this.size==0 ||  item.compare(item, curr.getItem()) >= 0) {
			// If the list is currently empty or the given item is >= to that of the current 
			// head of the list, add this item to the start
			this.addToStart(item);
			return;
		}
		
		while (item.compare(item, curr.getItem()) < 0) {
						
			if (!curr.hasNext()) {
				// If curr doesn't have a next node linked, just add the item to the end of the list
				this.addToEnd(item);
				return;
			}
			else {
				next = (GenericNode<T>)curr.getNext();
			}
			
			if (item.compare(item, next.getItem()) >= 0) {
				// If the given item is >= to the next node's item, insert a new node with the 
				// given item and alter the curr and next nodes accordingly
				GenericNode<T> newNode = new GenericNode<>(item);
				curr.setNext(newNode);
				newNode.setNext(next);
				this.size++;
				return;
			}
			else {
				// Otherwise, set curr to the next node and continue through the loop
				curr = (GenericNode<T>)curr.getNext();
			}
		}
	}
	
	private void addToStart(T item) {
	// Adds the given object to the beginning of the list
		GenericNode<T> temp = new GenericNode<>(item);
		temp.setNext(this.head);
		this.head = temp;
		this.size++;
	}
	
	private void addToEnd(T item) {
	// Adds the given object to the end of the list
		if (this.size==0) {
			// If the list is currently empty, just add the object to the start
			this.addToStart(item);
			return;
		}
		
		GenericNode<T> curr = getGenericNode(this.size-1);
		GenericNode<T> newEnd = new GenericNode<>(item);
		curr.setNext(newEnd);
		
		this.size++;
	}
	
	@SuppressWarnings("unchecked")
	public void remove(int position) {
	// Removes the object at the given position in the list
		if (this.head==null) {
			throw new IllegalArgumentException("the list is empty");
		}
		if (position<0 || position>=this.size) {
			throw new IndexOutOfBoundsException("position must be in range 0 - " + this.size);
		}
		if (position==0) {
			this.head = (GenericNode<T>)this.head.getNext();
			this.size--;
			return;
		}
		
		GenericNode<T> prev = this.getGenericNode(position-1);
		GenericNode<T> curr = (GenericNode<T>)prev.getNext();
		
		prev.setNext((GenericNode<T>)curr.getNext());
		
		this.size--;
	}

	public void clear() {
	// Clears the contents of the list, making it an empty list
		while (this.size>0) {
			this.remove(0);
		}
		this.size = 0;
	}
	
	@SuppressWarnings("unchecked")
	private GenericNode<T> getGenericNode(int position) {
	// Returns a GenericNode corresponding to the given position
		if (position<0 || position>=this.size) {
			throw new IndexOutOfBoundsException("position not valid!");
		}
		
		GenericNode<T> curr = this.head;
		
		for (int i=0; i<position; i++) {
			curr = (GenericNode<T>)curr.getNext();
		}
		
		return curr;
	}
	
	@SuppressWarnings("unchecked")
	public T getItem(int position) {
	// Returns the element stored at the given position
		if (position<0 || position>=this.size) {
			throw new IndexOutOfBoundsException("position must be in range 0 - " + this.size);
		}
		
		T item = null;
		GenericNode<T> curr = this.head;
		int count = 0;
		
		while (curr!=null) {
			if (count == position) {
				item = curr.getItem();
				break;
			}
			else {
				curr = (GenericNode<T>)curr.getNext();
				count++;
			}
		}
		
		return item;
	}
	
	// The following 2 functions implement the iterator for this linked list
	@Override
	public Iterator<T> iterator() {
		return new LinkedListIterator();
	}
	
	private class LinkedListIterator implements Iterator<T> {
		private GenericNode<T> curr = head;
		
		@SuppressWarnings("unchecked")
		public T next() {
			if (!hasNext()) {
				throw new IllegalArgumentException();
			}
			T item = (T)curr.getItem();
			curr = (GenericNode<T>)curr.getNext();
			return item;
		}

		public boolean hasNext() {
			return curr != null;
		}
	}
	
}
