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
public class DiGram {
    private final String theNumber, theClass;

    public DiGram(String theNumber, String theClass) {
      this.theNumber = theNumber.toLowerCase();
      this.theClass = theClass.toLowerCase();
    }

    public String getNumber() {
      return theNumber;
    }
    
    public String getTheClass() {
      return theClass;
    }

    @Override
    public int hashCode() {
        return theNumber.hashCode() ^ theClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return (obj instanceof DiGram) 
        && ((DiGram) obj).theNumber.equals(theNumber)
        && ((DiGram) obj).theClass.equals(theClass);
    }
}
