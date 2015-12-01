/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import static java.lang.String.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weka.data.Attribute;
import weka.data.DataStore;
import weka.object.TriGram;
import weka.object.DiGram;

/**
 *
 * @author Asus
 */
public class NaiveBayes implements Algorithm {

    private final Map<TriGram, Integer> model = new HashMap<>();
    private final HashMap<DiGram, ArrayList<Integer>> nmodel = new HashMap<DiGram, ArrayList<Integer>>();
    private final Map<String, Integer> classes = new HashMap<>();
    private final Map<String, BigInteger> classDenumerators = new HashMap<>();

    public NaiveBayes() {

    }

    public NaiveBayes(DataStore ds) {
        makeModel(ds);
    }

    public void makeModel(DataStore ds) {
        model.clear();
        classes.clear();
        classDenumerators.clear();
        for ( int i = 0; i < ds.getElementSize(); i++ ) {
            String theClass = ds.getClass(i);
            classes.put(
                theClass,
                (!classes.containsKey(theClass)) ? 1 : (int)classes.get(theClass) + 1
            );
            for ( int j = 0; j < ds.getArffAttributes().size()-1; j++ ) {
                 List<Attribute> attrs = ds.getArffAttributes();                 
                 Attribute attr = attrs.get(j);
                 if ( attr.getType() == attr.NOMINAL ) {
                  System.out.println("fery ambis" + ds.getAttribute(i,j));
                  TriGram key = new TriGram(Integer.toString(j), ds.getAttribute(i, j), theClass);
                  model.put(
                      key,
                      (!model.containsKey(key)) ? 1 : (int)model.get(key) + 1
                  );
                 }
                  else if ( attr.getType() == attr.NUMERIC ) {
                  DiGram nkey = new DiGram(Integer.toString(j), theClass);
                  System.out.println(nmodel.get(nkey));
                  ArrayList<Integer> ltemp = new ArrayList<Integer>();
                 
                  ltemp.add(Integer.parseInt(ds.getAttribute(i,j)));
                  
                  nmodel.put(
                    nkey,
                    ltemp
                  );
                
              }
            }
        }
        for (Map.Entry<String, Integer> classEntry: classes.entrySet())
            classDenumerators.put(classEntry.getKey(),
                BigInteger.valueOf(ds.getElementSize()).multiply(
                BigInteger.valueOf(classEntry.getValue()).pow(ds.getAttributeSize())));
    }
    
    public String getModel(DataStore ds) {
      String formatting = "%-20s%s%n";
      StringBuilder builder = new StringBuilder();
      
      List<String> classList = new ArrayList<>();
      String initMsg = new String();
      
      for ( String value: ds.getArffAttributes().get(ds.getClassIndex()).getValues()) {
        initMsg = initMsg + "\t" + value + "(" + classes.get(value) + "/" + ds.getElementSize() + ")";
        classList.add(value);
      }
      
      builder.append(String.format(formatting, "Attributes\t", initMsg));
      
      builder.append("========================================================\n");
      
      int i = 0; int j = 0;
      for ( Attribute attribute: ds.getArffAttributes() ) {
        while(j < ds.getArffAttributes().size()-1){
          if ( i != ds.getClassIndex()) {
          builder.append(attribute.getName()+"\n");
            System.out.println("lol" + attribute.getValues());
          for ( String value: attribute.getValues() ) {
            String msg = new String();
            for ( String theClass: classList ) {
              Integer frequency = model.get(new TriGram(Integer.toString(j), value, theClass));
              msg = msg + "\t \t" + ((frequency != null) ? frequency : "0") + "/" + classes.get(theClass);
            }
            builder.append(String.format(formatting, "   " + value + "     ", msg));
          }
          ++j;
        }
        }
        ++i;
      }
      builder.append("\n");

      return builder.toString();
    }
    
    @Override
    public String getAlgorithmName() {
        return "Naive Bayes";
    }

    @Override
    public String predict(List<String> attributes) {
        BigDecimal maxProbability = BigDecimal.ZERO;
        String prediction = null;
        for (Map.Entry<String, Integer> classKey: classes.entrySet()) {
            BigInteger numerator = BigInteger.valueOf(classKey.getValue());
            BigInteger denumerator = classDenumerators.get(classKey.getKey());
            if (denumerator.compareTo(BigInteger.ZERO) == 0)
                continue;
            for (int i = 0; i < attributes.size(); ++i) {
                TriGram key = new TriGram(Integer.toString(i), attributes.get(i), classKey.getKey());
                Integer val = model.get(key);
                if (val == null) {
                    numerator = BigInteger.ZERO;
                    break;
                }                    
                else
                    numerator = numerator.multiply(BigInteger.valueOf(val));
            }
            BigDecimal probability = new BigDecimal(numerator).divide(new BigDecimal(denumerator), MathContext.DECIMAL128);
            if (probability.compareTo(maxProbability) >= 0) {
                maxProbability = probability;
                prediction = classKey.getKey();
            }
        }        
        return prediction;
    }
}
