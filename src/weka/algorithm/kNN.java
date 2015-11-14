/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.List;
import weka.data.DataStore;

/**
 *
 * @author KEVIN
 */
public class kNN {
    private DataStore ds;
    private int similiarity[];
    private String similiarityclass[];
    public kNN(){
        
    }
    
    public kNN(int k, DataStore _ds){
        ds = _ds;
       similiarity = new int[k];
       similiarityclass = new String[k];
       for (int i=0; i<k; i++){
           similiarity[i] = 100;
           similiarityclass[i] = "";
       }
    }
    
    
public void insertMin(int sesuatu, String sesuatukelas){
   int i =0;
   boolean belom = true;
   while (i < similiarity.length && belom){
       if(similiarity[i] > sesuatu){
           similiarity[i] = sesuatu;
           similiarityclass[i] = sesuatukelas;
           belom = false;
       }
   }
}

public String predict(List<String> attributes){
    int min = 1000;
    String classpredict = " ";
    for (int i = 0; i < ds.getElementSize(); i++){
       int counter = 0;
       List<String> temp = ds.getAttributes(i);
       for (int j = 0; j < temp.size() ; j++ ){
           if (temp.get(j).equals(ds.getAttributes(i).get(j)))
               counter++;
       }
        if (counter < min){
        min = counter;
        classpredict = ds.getClass(i);
    }   
   }
    
    System.out.println(classpredict);
    return classpredict;
}

    public static void main(String args[]){
        DataStore dataStore = new DataStore("src/weka/algorithm/car.data.txt");
        kNN knn = new kNN(3,dataStore);
        DataStore testData = dataStore.getTestData();
        int correct = 0;
        for (int i = 0; i < testData.getElementSize(); ++i) {
            String prediction = knn.predict(testData.getAttributes(i));
            if (testData.getClass(i).equals(prediction))
                ++correct;
        }
        System.out.println("Result: " + correct + "/" + testData.getElementSize() + " correct");
}

}
