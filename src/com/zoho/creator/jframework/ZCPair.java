// $Id$

package com.zoho.creator.jframework;

public class ZCPair<A, B> {

	private final A one;
	private final B two;

	ZCPair(A one, B two) {
        this.one = one;
        this.two = two;
    }
	
    public A getOne() {
        return one;
    }
	
    public B getTwo() {
        return two;
    }
    
    public String toString() {
    	return " [" + one.toString() + " : " + two.toString() + "] ";
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 53 * result + ((one == null) ? 0 : one.hashCode());
        result = 53 * result + ((two == null) ? 0 : two.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")//No I18N
    @Override
    public boolean equals(Object toCheck) {
        if (this == toCheck) {
        	return true;
        }
        
        if (toCheck == null || getClass() != toCheck.getClass()) {
        	return false;
        }
        
        ZCPair<A, B> toCheckPair = (ZCPair<A, B>) toCheck;
        
        if ((one == null && toCheckPair.getOne() != null) ||  
        		(!one.equals(toCheckPair.getOne()))) {
            return false;
        }
        
        if ((two == null && toCheckPair.getTwo() != null) || 
        		(!two.equals(toCheckPair.getTwo()))) {
            return false;
        }
        return true;
    }
    
}
