/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author visat
 */
public class DataStore {
    private List<List<String>> attributeList = new ArrayList<>();
    private List<String> classList = new ArrayList<>();
    private Set<Integer> testDataIndices = new HashSet<>();

    public DataStore() {

    }

    public DataStore(String fileName) {
        read(fileName);
    }

    public void add(List<String> attributes, String _class) {
        if (attributeList.isEmpty() || attributeList.get(0).size() == attributes.size()) {
            attributeList.add(attributes);
            classList.add(_class);
            testDataIndices.clear();
        }
    }

    public void clear() {
        attributeList.clear();
        classList.clear();
        testDataIndices.clear();
    }

    public void read(String fileName) {
        clear();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> list = Arrays.asList(line.trim().split("[,]"));
                if (list.size() <= 1 || (!attributeList.isEmpty() && list.size()-1 != attributeList.get(0).size()))
                    throw new Exception("Error: wrong file input format");
                else if (list.isEmpty())
                    continue;
                List<String> attributes = new ArrayList<>();
                for (int i = 0; i < list.size(); ++i) {
                    String s = list.get(i).trim();
                    if (i == list.size()-1)
                        classList.add(s);
                    else
                        attributes.add(s);
                }
                attributeList.add(attributes);
            }
        }
        catch (Exception e) {
            clear();
            System.err.println(e.getMessage());
        }
    }

    private void populateTestDataIndices() {
        while (testDataIndices.size() < (int)(0.1*attributeList.size())) {
            int i;
            do {
                i = ThreadLocalRandom.current().nextInt(0, attributeList.size());
            } while (testDataIndices.contains(i));
            testDataIndices.add(i);
        }
    }

    public int getElementSize() {
        return attributeList.size();
    }

    public int getAttributeSize() {
        return attributeList.isEmpty() ? 0 : attributeList.get(0).size();
    }

    public String getAttribute(int elemIndex, int attrIndex) {
        if (elemIndex >= 0 && elemIndex < attributeList.size() &&
            attrIndex >= 0 && attrIndex < attributeList.get(elemIndex).size())
            return attributeList.get(elemIndex).get(attrIndex);
        else
            return null;
    }
    
    public List<String> getAttributes(int elemIndex) {
        if (elemIndex >= 0 && elemIndex < attributeList.size())
            return attributeList.get(elemIndex);
        else 
            return null;
    }

    public String getClass(int elemIndex) {
        if (elemIndex >= 0 && elemIndex < classList.size())
            return classList.get(elemIndex);
        else
            return null;
    }

    public DataStore getTrainingData() {
        DataStore trainingData = new DataStore();
        populateTestDataIndices();
        for (int i = 0; i < attributeList.size(); ++i)
            if (!testDataIndices.contains(i))
                trainingData.add(attributeList.get(i), classList.get(i));
        return trainingData;
    }

    public DataStore getTestData() {
        DataStore testData = new DataStore();
        populateTestDataIndices();
        for (Integer i: testDataIndices)
            testData.add(attributeList.get(i), classList.get(i));
        return testData;
    }
}
