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
public class kNN implements Algorithm {

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

    public String getAlgorithmName() {
        return "kNN";
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

    @Override
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
}
