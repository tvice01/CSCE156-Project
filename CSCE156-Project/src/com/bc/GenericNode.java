package com.bc;

/*
 * Date: 11/12/2020
 * CSCE 156, Assignment 3
 * @authors Treyvor Vice, Ann Le
 * This class stores generic nodes for the linked list in GenericList. It contains the basic methods needed
 * to create, access, and maintain the nodes of a linked list
 */

public class GenericNode <T> {
	
	private GenericNode<T> next;
	private T item;
	
	public GenericNode(T item) {
		this.item = item;
		this.next = null;
	}
	
	public T getItem() {
        return item;
    }
	
	public GenericNode<?> getNext() {
	// Grabs the next node in the list
		return next;
	}

    public void setNext(GenericNode<T> next) {
        this.next = next;
    }
    
    public boolean hasNext() {
    // Checks if the node is the last in the list
    	return this.next!=null;
    }
}
