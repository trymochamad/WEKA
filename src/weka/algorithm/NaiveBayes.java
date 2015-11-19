/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import weka.data.DataStore;
import weka.object.TriGram;

/**
 *
 * @author Asus
 */
public class NaiveBayes implements Algorithm {

    private final Map<TriGram, Integer> model = new HashMap<>();
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
            for ( int j = 0; j < ds.getAttributeSize(); j++ ) {
                TriGram key = new TriGram(Integer.toString(j), ds.getAttribute(i, j), theClass);
                model.put(
                    key,
                    (!model.containsKey(key)) ? 1 : (int)model.get(key) + 1
                );
            }
        }
        for (Map.Entry<String, Integer> classEntry: classes.entrySet())
            classDenumerators.put(classEntry.getKey(),
                BigInteger.valueOf(ds.getElementSize()).multiply(
                BigInteger.valueOf(classEntry.getValue()).pow(ds.getAttributeSize())));
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
