/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.data;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Asus
 */
public class Attribute {
  public final static int NOMINAL = 0;
  public final static int NUMERIC = 1;
  public final static int STRING = 2;
  public final static int DATE = 3;
  
  public final static String ARFF_RELATION = "@relation";
  public final static String ARFF_ATTRIBUTE = "@attribute";
  public final static String ARFF_DATA = "@data";
 
  public final static String ARFF_ATTRIBUTE_NUMERIC = "numeric";
  
  /* Nama Attribute */
  private String name;
  
  /* Attribute Type */
  private int type;
  
  /* Untuk data berupa nominal */
  private List<String> values;
  
  /* Constructor */
  Attribute(String name, int type, String input) {
    this.name = name;
    this.type = type;
    input = input.replaceAll("[{}]","");
    values = Arrays.asList(input.split("[,]"));
  }
  
  Attribute(String name, int type) {
    this.name = name;
    this.type = type;
  }
  
  /* Getter */
  public String getName() {
    return name;
  }
  public int getType() {
    return type;
  }
  
}
