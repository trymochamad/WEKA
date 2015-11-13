/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.object;

/**
 *
 * @author Asus
 */
public class TriGram {
    private final String theNumber, theAttribute, theClass;

    public TriGram(String theNumber, String theAttribute, String theClass) {
      this.theNumber = theNumber.toLowerCase();
      this.theAttribute = theAttribute.toLowerCase();
      this.theClass = theClass.toLowerCase();
    }

    public String getNumber() {
      return theNumber;
    }

    public String getAttribute() {
      return theAttribute;
    }
    
    public String getTheClass() {
      return theClass;
    }

    @Override
    public int hashCode() {
        return theNumber.hashCode() ^ theAttribute.hashCode() ^ theClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return (obj instanceof TriGram) 
        && ((TriGram) obj).theNumber.equals(theNumber)
        && ((TriGram) obj).theAttribute.equals(theAttribute)
        && ((TriGram) obj).theClass.equals(theClass);
    }
}
