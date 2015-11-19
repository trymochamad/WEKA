/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.algorithm;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import weka.data.DataStore;
import weka.data.Attribute;
/**
 *
 * @author KEVIN
 */
public class kNN implements Algorithm {

    private DataStore ds;   // training data
    private double similiarity[];
    private String similiarityclass[];
    private String similiarityatr[][];
    private int atr[];
    private int k;

    public kNN(){
        
    }
    
    public kNN(int k, DataStore _ds){
        this.k = k;
        ds = _ds;
        atr = new int[ds.getArffAttributes().size()];
        similiarity = new double[k];
        similiarityclass = new String[k];
        similiarityatr  = new String[k][ds.getArffAttributes().size()];
        for (int i=0; i<k; i++){
            similiarity[i] = -1;
            for (int j=0; j<ds.getArffAttributes().size(); j++)
                similiarityatr[i][j] = "";
        }
        for (int x=0; x< ds.getArffAttributes().size(); x++){
            atr[x]= ((ds.getArffAttributes()).get(x)).getType();
        }
            
    }

    public boolean isContainNumeric() {       
        for (int x=0; x< ds.getArffAttributes().size(); x++){
            atr[x]= ((ds.getArffAttributes()).get(x)).getType();
            if (atr[x] == 1) 
                return true;             
        }
        return false;    
    }

    public String getAlgorithmName() {
        return "kNN";
    }
    
    public void insertMin(int sesuatu, List<String> attributes, String kelas){
        double min = similiarity[0];
        int idxmin = 0;
        for (int i=0; i<k; i++) {
            if (similiarity[i]<=min && min!=-1) {
                min = similiarity[i];
                idxmin = i;
            }
        }
        if (sesuatu>min) {
            similiarity[idxmin] = sesuatu;
            similiarityclass[idxmin] = kelas;
            attributes.toArray(similiarityatr[idxmin]);
        }
    }

    public String predictNumeric(List<String> attributes){
        double sum = 0;
        for(int j=0; j < k; j++) {
            for (int i = 0; i < attributes.size(); i++){
                if(atr[i]== 1){                    
                    sum += Math.pow((Double.parseDouble(attributes.get(i)) - Double.parseDouble(similiarityatr[j][i])),2);
                }
            }
            similiarity[j] = Math.sqrt(sum);
        }
        double min = similiarity[0];
        int idxmin = 0;
        for (int i=0; i<k; i++) {
            if (similiarity[i] < min) {
                min = similiarity[i];
                idxmin = i;
            }
        }
        return similiarityclass[idxmin];
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
            insertMin(counter, ds.getAttributes(i), ds.getClass(i));
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
        int counter = 0;
        for (Map.Entry<String, Integer> entry : classes.entrySet()){
            if (maxEntry == null || entry.getValue() >= (maxEntry.getValue())) {
                maxEntry = entry;
                counter++;
            }
        }
        if (counter>1 && isContainNumeric())
            return predictNumeric(attributes);
        else {
            for (int i=0; i<k; i++) {
                similiarity[i] = -1;
                similiarityclass[i] = "";
                for (int j=0; j<ds.getArffAttributes().size(); j++)
                    similiarityatr[i][j] = "";
            }
            return maxEntry.getKey();
        }
    }
}
