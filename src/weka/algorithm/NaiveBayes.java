/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weka.weka.DataStore;

/**
 *
 * @author Asus
 */
public class NaiveBayes {
  
  private HashMap<List<String>, Integer> model = new HashMap<>();
    
  public void makeModel(DataStore ds) { 
    
    for ( int i = 0; i < ds.getElementSize(); i++ ) {
      for ( int j = 0; j < ds.getAttributeSize(i); j++ ) {
        List<String> key = new ArrayList<>();
        key.add(Integer.toString(j));
        key.add(ds.getAttribute(i, j));
        key.add(ds.getClass(i));
        
        model.put(
          key,
          (model.get(key) == null) ? 1 : (int)model.get(key) + 1);
      }
    }        

  }
    
}
