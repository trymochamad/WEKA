/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
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

    private Map<TriGram, Integer> model = new HashMap<>();
    private Map<String, Integer> classes = new HashMap<>();
    private int elementSize = 0, attributeSize = 0;

    public NaiveBayes() {

    }

    public NaiveBayes(DataStore ds) {
        makeModel(ds);
    }

    public void makeModel(DataStore ds) {
        model.clear();
        classes.clear();
        elementSize = ds.getElementSize();
        attributeSize = ds.getAttributeSize();
        for ( int i = 0; i < elementSize; i++ ) {
            String theClass = ds.getClass(i);
            classes.put(
                theClass,
                (!classes.containsKey(theClass)) ? 1 : (int)classes.get(theClass) + 1
            );
            for ( int j = 0; j < attributeSize; j++ ) {
                TriGram key = new TriGram(Integer.toString(j), ds.getAttribute(i, j), theClass);
                model.put(
                    key,
                    (!model.containsKey(key)) ? 1 : (int)model.get(key) + 1
                );
            }
        }
    }

    public String predict(List<String> attributes) {
        List<BigDecimal> probabilities = new ArrayList<>();
        List<String> predictions = new ArrayList<>();
        for (Map.Entry<String, Integer> classKey: classes.entrySet()) {
            BigInteger numerator = BigInteger.valueOf(classKey.getValue());
            BigInteger denumerator =
                BigInteger.valueOf(elementSize).multiply(
                BigInteger.valueOf(classKey.getValue()).pow(attributeSize));
            if (denumerator.compareTo(BigInteger.ZERO) == 0)
                continue;
            for (int i = 0; i < attributes.size(); ++i) {
                TriGram key = new TriGram(Integer.toString(i), attributes.get(i), classKey.getKey());
                Integer val = model.get(key);
                if (val == null)
                    numerator = BigInteger.ZERO;
                else
                    numerator = numerator.multiply(BigInteger.valueOf(val));
            }
            BigDecimal probability = new BigDecimal(numerator).divide(new BigDecimal(denumerator), MathContext.DECIMAL128);
            probabilities.add(probability);
            predictions.add(classKey.getKey());
        }
        BigDecimal maxProbability = BigDecimal.ZERO;
        String prediction = null;
        for (int i = 0; i < probabilities.size(); ++i) {
            if (probabilities.get(i).compareTo(maxProbability) == 1) {
                maxProbability = probabilities.get(i);
                prediction = predictions.get(i);
            }
        }
        return prediction;
    }

    public static void main(String[] args) {
        DataStore dataStore = new DataStore("src/weka/algorithm/car.data.txt");
        NaiveBayes naiveBayes = new NaiveBayes(dataStore.getTrainingData());
        DataStore testData = dataStore.getTestData();
        int correct = 0;
        for (int i = 0; i < testData.getElementSize(); ++i) {
            String prediction = naiveBayes.predict(testData.getAttributes(i));
            if (testData.getClass(i).equals(prediction))
                ++correct;
        }
        System.out.println("Result: " + correct + "/" + testData.getElementSize() + " correct");
    }
}
