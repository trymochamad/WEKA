/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import weka.data.DataStore;

/**
 *
 * @author KEVIN
 */
public class kNN {
    private DataStore ds;   // training data
    private int similiarity[];
    private String similiarityclass[];
    private int k;

    public kNN(){
        
    }
    
    public kNN(int k, DataStore _ds){
        this.k = k;
        ds = _ds;
        similiarity = new int[k];
        similiarityclass = new String[k];
        for (int i=0; i<k; i++){
            similiarity[i] = 0;
            similiarityclass[i] = "";
        }
    }
    
    public void insertMin(int sesuatu, String sesuatukelas){
        int min = 0;
        int idxmin = 0;
        for (int i=0; i<k; i++) {
            if (similiarity[i]<=min) {
                min = similiarity[i];
                idxmin = i;
            }
        }
        if (sesuatu>min) {
            similiarity[idxmin] = sesuatu;
            similiarityclass[idxmin] = sesuatukelas;
        }
    }

    public String predict(List<String> attributes){
        for (int i = 0; i < ds.getElementSize(); i++){
            int counter = 0;    // jumlah atribut yang sama
            List<String> temp = ds.getAttributes(i);
            for (int j = 0; j < temp.size() ; j++ ){
                if (temp.get(j).equals(attributes.get(j)))
                    counter++;
            }
            insertMin(counter, ds.getClass(i));
        }

        // return modus dari similiarity class
        Map<String, Integer> classes = new HashMap<>();
        for (int i=0; i<k; i++) {
            classes.put(
                similiarityclass[i],
                (!classes.containsKey(similiarityclass[i])) ? 1 : (int)classes.get(similiarityclass[i]) + 1
            );
        }

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : classes.entrySet()){
            if (maxEntry == null || entry.getValue() > (maxEntry.getValue()))
                maxEntry = entry;
        }

        return maxEntry.getKey();
    }

    public static void main(String args[]){
        DataStore dataStore = new DataStore("weka/algorithm/car.data.txt");
        kNN knn = new kNN(3,dataStore);
        DataStore testData = dataStore;
        int correct = 0;
        
        for (int i = 0; i < testData.getElementSize(); ++i) {
            String prediction = knn.predict(testData.getAttributes(i));
            System.out.println(prediction);
            if (testData.getClass(i).equals(prediction))
                ++correct;
        }

        System.out.println("Result: " + correct + "/" + testData.getElementSize() + " correct");   
    }

}
