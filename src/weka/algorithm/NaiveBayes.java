/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weka.weka.DataStore;
import weka.object.TriGram;

/**
 *
 * @author Asus
 */
public class NaiveBayes {
  
  private HashMap<TriGram, Integer> model = new HashMap<>();
  private Map<String, Integer> classes = new HashMap<>();
    
  public void makeModel(DataStore ds) {    
    for ( int i = 0; i < ds.getElementSize(); i++ ) {
      for ( int j = 0; j < ds.getAttributeSize(i); j++ ) {
        TriGram key = new TriGram(Integer.toString(j), ds.getAttribute(i, j), ds.getClass(i));
        
        if ( j == ds.getAttributeSize(i) - 1 ) {
          String theClass = ds.getClass(i);
          classes.put(
            theClass,
            (!classes.containsKey(theClass)) ? 1 : (int)classes.get(theClass) + 1            
          );
        } else {        
          model.put(
            key,
            (!model.containsKey(key)) ? 1 : (int)model.get(key) + 1
          );
        }
        
      }
    }        
  }
    
}
