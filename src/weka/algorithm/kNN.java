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
            similiarity[i] = -1;
            similiarityclass[i] = "";
        }
    }
    
    public void insertMin(int sesuatu, String sesuatukelas){
        int min = similiarity[0];
        int idxmin = 0;
        for (int i=0; i<k; i++) {
            if (similiarity[i]<=min && min!=-1) {
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

        for (int i=0; i<k; i++) {
            similiarity[i] = -1;
            similiarityclass[i]="";
        }

        return maxEntry.getKey();
    }

    public static void main(String args[]){
        DataStore dataStore = new DataStore("weka/algorithm/car.data.txt");
        System.out.println("kNN algorithm");

        // full training
        final int k = 3;
        kNN knnFull = new kNN(k,dataStore);        
        int correctFull = 0;
        for (int i = 0; i < dataStore.getElementSize(); ++i) {
            String prediction = knnFull.predict(dataStore.getAttributes(i));
            if (dataStore.getClass(i).equals(prediction))
                ++correctFull;
        }
        System.out.println("full training: " + correctFull + "/" + dataStore.getElementSize() + " correct");
 
        // 10-fold cross-validation
        final int x = 10;
        final int partitionSize = dataStore.getElementSize() >= x ? dataStore.getElementSize()/x : 1;        
        kNN knnFold = new kNN();
        int maxCorrectFold = Integer.MIN_VALUE;
        for (int fold = 0; fold < x; ++fold) {
            // get partition for training and test
            DataStore partitionTest = new DataStore(), partitionTraining = new DataStore();
            int l = fold * partitionSize,
                r = l + partitionSize - 1;
            for (int i = 0; i < dataStore.getElementSize(); ++i)
                if (i >= l && i <= r)
                    partitionTest.add(dataStore.getAttributes(i), dataStore.getClass(i));
                else
                    partitionTraining.add(dataStore.getAttributes(i), dataStore.getClass(i));
            kNN knnTraining = new kNN(k, partitionTraining);            
            
            // validation
            int correct = 0;
            for (int i = 0; i < partitionTest.getElementSize(); ++i) {
                if (partitionTest.getClass(i).equals(knnTraining.predict(partitionTest.getAttributes(i))))
                    ++correct;
            }            
            if (maxCorrectFold < correct) {
                maxCorrectFold = correct;
                knnFold = knnTraining;
            }
        }        
        int correctFold = 0;
        for (int i = 0; i < dataStore.getElementSize(); ++i) {
            String prediction = knnFold.predict(dataStore.getAttributes(i));
            if (dataStore.getClass(i).equals(prediction))
                ++correctFold;
        }
        System.out.println(String.valueOf(x) + "-fold cross-validation: " + correctFold + "/" + dataStore.getElementSize() + " correct");
    }

}
