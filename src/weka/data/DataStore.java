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
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author visat
 */
public class DataStore {
    private int classIndex = -1;
  
    private List<List<String>> attributeList = new ArrayList<>();
    private List<String> classList = new ArrayList<>();
    private Set<Integer> testDataIndices = new HashSet<>();

    private List<String> data = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();
    
    public DataStore() {

    }

    public DataStore(String fileName) {
        readArff(fileName);
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
    
    public void readArff(String fileName) {
      clear();
      try {
        
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
        
          line = line.replaceAll(",\\s+", ",");
          List<String> header = Arrays.asList(line.trim().split("[ ]"));
          String arff_header = header.get(0);

          switch(arff_header) {
            case Attribute.ARFF_RELATION:
              /* Ignore HA HA */
              break;
            case Attribute.ARFF_ATTRIBUTE:
              String arff_attr_name = header.get(1);

              switch(header.get(2)) {
                case Attribute.ARFF_ATTRIBUTE_NUMERIC:
                  attributes.add(new Attribute(arff_attr_name, Attribute.NUMERIC));
                  break;
                default:
                  /* Anggap Nominal */
                  attributes.add(new Attribute(arff_attr_name, Attribute.NOMINAL, header.get(2)));
                  break;
              }

              break;
            case Attribute.ARFF_DATA:              
              saveData(fileReader, bufferedReader, line);
              break;
            default:
              break;                
          }
          
        }
        
      }
      catch (Exception e) {
          clear();
          System.err.println(e.getMessage());
      }
      
    }
    
    public void saveData(FileReader fileReader, BufferedReader bufferedReader, String line) {
      try {
        while ((line = bufferedReader.readLine()) != null) {
          data.add(line);
        } 
        classIndex = (Arrays.asList(data.get(0).trim().split("[,]"))).size() - 1;
      }
      catch (Exception e) {
          clear();
          System.err.println(e.getMessage());
      }
    }
    
    public void read() {     
      for ( String line: data ) {
        try {
          List<String> list = Arrays.asList(line.trim().split("[,]"));
          if (list.size() <= 1 || (!attributeList.isEmpty() && list.size()-1 != attributeList.get(0).size()))
            throw new Exception("Error: wrong file input format");
          else if (list.isEmpty())
            continue;
          List<String> attributes = new ArrayList<>();
          for (int i = 0; i < list.size(); ++i) {
            String s = list.get(i).trim();
            if (i == classIndex)
              classList.add(s);
            else
              attributes.add(s);
          }
          attributeList.add(attributes);
        } catch (Exception ex) {
          Logger.getLogger(DataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public List<Attribute> getArffAttributes() {
      return attributes;
    }
    
    public void setClassIndex(int c) {
      classIndex = c;
    }
}
