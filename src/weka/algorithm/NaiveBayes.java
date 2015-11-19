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
public class NaiveBayes {

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

    public static void main(String[] args) {        
        DataStore dataStore = new DataStore("src/weka/algorithm/weather.data.arff");
        System.out.println("Naive Bayes algorithm");
        
        // full training
        NaiveBayes naiveBayesFull = new NaiveBayes(dataStore);        
        int correctFull = 0;
        for (int i = 0; i < dataStore.getElementSize(); ++i) {
            String prediction = naiveBayesFull.predict(dataStore.getAttributes(i));
            if (dataStore.getClass(i).equals(prediction))
                ++correctFull;
        }
        System.out.println("full training: " + correctFull + "/" + dataStore.getElementSize() + " correct");
        
        // 10-fold cross-validation
        final int k = 10;
        final int partitionSize = dataStore.getElementSize() >= k ? dataStore.getElementSize()/k : 1;        
        NaiveBayes naiveBayesFold = new NaiveBayes();
        int maxCorrectFold = Integer.MIN_VALUE;
        for (int fold = 0; fold < k; ++fold) {
            // get partition for training and test
            DataStore partitionTest = new DataStore(), partitionTraining = new DataStore();
            int l = fold * partitionSize,
                r = l + partitionSize - 1;
            for (int i = 0; i < dataStore.getElementSize(); ++i)
                if (i >= l && i <= r)
                    partitionTest.add(dataStore.getAttributes(i), dataStore.getClass(i));
                else
                    partitionTraining.add(dataStore.getAttributes(i), dataStore.getClass(i));
            NaiveBayes naiveBayesTraining = new NaiveBayes(partitionTraining);            
            
            // validation
            int correct = 0;
            for (int i = 0; i < partitionTest.getElementSize(); ++i) {
                if (partitionTest.getClass(i).equals(
                naiveBayesTraining.predict(partitionTest.getAttributes(i))))
                    ++correct;
            }            
            if (maxCorrectFold < correct) {
                maxCorrectFold = correct;
                naiveBayesFold = naiveBayesTraining;
            }
        }        
        int correctFold = 0;
        for (int i = 0; i < dataStore.getElementSize(); ++i) {
            String prediction = naiveBayesFold.predict(dataStore.getAttributes(i));
            if (dataStore.getClass(i).equals(prediction))
                ++correctFold;
        }
        System.out.println(String.valueOf(k) + "-fold cross-validation: " + correctFold + "/" + dataStore.getElementSize() + " correct");
    }
}
