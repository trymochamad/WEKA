
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import weka.algorithm.*;
import weka.data.*;
import weka.object.DiGram;

/**
 *
 * @author visat
 */
public class Main {
    private static HashMap<DiGram, Integer> confusionMatrix = new HashMap<>();

    private static final Scanner sc = new Scanner(System.in);

    /*public static void main(String[] args) {
        System.out.println("WEKA-WEKA");
        boolean keepRunning = true;
        boolean wrongChoice = false;
        while (true) {
            if (wrongChoice) {
                System.out.println("Masukkan anda salah");
                wrongChoice = false;
            }

            int idxAlgorithm = getAlgorithmIndex();
            wrongChoice = idxAlgorithm < 0 || idxAlgorithm > 2;
            if (wrongChoice) continue;            
            int knn = 0;
            if (idxAlgorithm == 0) break;
            else if (idxAlgorithm == 1) {
                knn = getKNN();
                wrongChoice = knn < 1;
                if (wrongChoice) continue;
            }

            int idxScheme = getSchemeIndex();
            wrongChoice = idxScheme < 1 || idxScheme > 3;
            if (wrongChoice) continue;

            String fileName = getFileName();
            wrongChoice = fileName == null;
            if (wrongChoice) continue;

            doAlgorithm(idxAlgorithm, idxScheme, fileName, knn, 10);
        }
    }*/

    private static int getAlgorithmIndex() {
        int idx = -1;
        System.out.println("\nPilihan algoritma:");
        System.out.println("1. kNN");
        System.out.println("2. Naive Bayes");
        System.out.println("0. Keluar");
        System.out.print("Masukkan pilihan: ");
        if (sc.hasNextInt())
            idx = sc.nextInt();
        return idx;
    }

    private static int getSchemeIndex() {
        int idx = -1;
        System.out.println("1. Full training");
        System.out.println("2. 10-fold cross-validation");
        System.out.print("Masukkan skema: ");
        if (sc.hasNextInt())
            idx = sc.nextInt();
        return idx;
    }

    private static String getFileName() {
        String fileName = null;
        System.out.print("Masukkan nama file: ");
        if (sc.hasNext())
            fileName = sc.next();
        return fileName;
    }
    
    private static int getKNN() {
        int k = -1;
        System.out.print("Masukkan nilai k: ");
        if (sc.hasNextInt())
            k = sc.nextInt();
        return k;
    }
    
    private static int getClassIndex(DataStore dataStore) {
        int k = -1;
        int i = 0;
        for ( Attribute attribute: dataStore.getArffAttributes() ) {
          System.out.println(i + ". " + attribute.getName());
          ++i;
        }
        System.out.print("Pilih kelas atribut: ");
        if (sc.hasNextInt())
            k = sc.nextInt();
        
        return k;      
    }
    
    public static List<String> readFile(String fileName) {
        List<String> attr = new ArrayList<>();
      
        DataStore dataStore = new DataStore();
        dataStore.readArff(fileName);
        
        for ( Attribute attribute: dataStore.getArffAttributes() ) {
          attr.add(attribute.getName());
        }
        
        return attr;
    }
    
    public static String doAlgorithm(int algorithm, int scheme, String fileName, int knn, int kfold, int theClass) {      
        StringBuilder builder = new StringBuilder();
        Algorithm algo = null;
        confusionMatrix.clear();
        DataStore dataStore = new DataStore();
        dataStore.readArff(fileName);
        //dataStore.setClassIndex(getClassIndex(dataStore));  
        dataStore.setClassIndex(theClass);
        dataStore.read();
                
        // full training
        if (scheme == 1) {   
            switch (algorithm) {
                case 1:
                    algo = new kNN(knn, dataStore);
                    break;
                case 2:
                    algo = new NaiveBayes(dataStore);
                    
                    /* Write Model */
                    NaiveBayes nb = new NaiveBayes(dataStore);
                    builder.append(nb.getModel(dataStore));
                    break;                
            }
            int correctFull = 0;
            for (int i = 0; i < dataStore.getElementSize(); ++i) {
                String prediction = algo.predict(dataStore.getAttributes(i));
                DiGram key = new DiGram(prediction, dataStore.getClass(i));
                confusionMatrix.put(
                  key,
                  (!confusionMatrix.containsKey(key)) ? 1 : (int)confusionMatrix.get(key) + 1
                );
                if (dataStore.getClass(i).equals(prediction))
                    ++correctFull;
            }
            builder.append(algo.getAlgorithmName() + " full training: " + correctFull + "/" + dataStore.getElementSize() + " correct (" + Double.toString((correctFull*100)/dataStore.getElementSize()) + "%)");
        }
        // k-fold cross-validation
        else if (scheme == 2) {
            Algorithm algoFold = null;
            final int partitionSize = dataStore.getElementSize() >= kfold ? dataStore.getElementSize()/kfold : 1;
            int maxCorrectFold = Integer.MIN_VALUE;
            for (int fold = 0; fold < kfold; ++fold) {
                // get partition for training and test
                DataStore partitionTest = new DataStore(), partitionTraining = new DataStore();
                int l = fold * partitionSize,
                    r = l + partitionSize - 1;
                for (int i = 0; i < dataStore.getElementSize(); ++i)
                    if (i >= l && i <= r)
                        partitionTest.add(dataStore.getAttributes(i), dataStore.getClass(i));
                    else
                        partitionTraining.add(dataStore.getAttributes(i), dataStore.getClass(i));
                Algorithm algoTraining = null;
                switch (algorithm) {
                    case 1:
                        algoTraining = new kNN(knn, partitionTraining);
                        break;
                    case 2:
                        algoTraining = new NaiveBayes(partitionTraining);
                        break;                    
                }
                // validation
                int correct = 0;
                for (int i = 0; i < partitionTest.getElementSize(); ++i) {
                    if (partitionTest.getClass(i).equals(
                    algoTraining.predict(partitionTest.getAttributes(i))))
                        ++correct;
                }
                if (maxCorrectFold < correct) {
                    maxCorrectFold = correct;
                    algoFold = algoTraining;
                }
            }
            
            if (algorithm == 2) {    
              /* Write Model for Naive Bayes*/
              NaiveBayes nb = new NaiveBayes(dataStore);
              builder.append(nb.getModel(dataStore));
            }
            
            int correctFold = 0;
            for (int i = 0; i < dataStore.getElementSize(); ++i) {
                String prediction = algoFold.predict(dataStore.getAttributes(i));
                DiGram key = new DiGram(prediction, dataStore.getClass(i));
                confusionMatrix.put(
                  key,
                  (!confusionMatrix.containsKey(key)) ? 1 : (int)confusionMatrix.get(key) + 1
                );
                if (dataStore.getClass(i).equals(prediction))
                    ++correctFold;
            }
            builder.append(algoFold.getAlgorithmName() + " " + String.valueOf(kfold) + "-fold cross-validation: " + correctFold + "/" + dataStore.getElementSize() + " correct (" + Double.toString((correctFold*100)/dataStore.getElementSize()) + "%)");
        }
        builder.append(showConfusionMatrix(dataStore));
        return builder.toString();
    }
    
    public static String predictClass(int algorithm, String fileName, int knn, int theClass, List<String> attributes) {
        Algorithm algo = null;
        confusionMatrix.clear();
        DataStore dataStore = new DataStore();
        dataStore.readArff(fileName);
        dataStore.setClassIndex(theClass);
        dataStore.read();
                
        if (algorithm == 1)
            algo = new kNN(knn, dataStore);
        else
            algo = new NaiveBayes(dataStore);
        String prediction = "Class prediction: " + algo.predict(attributes);
        return prediction;
    }
    
    public static String showConfusionMatrix(DataStore ds) {
      StringBuilder builder = new StringBuilder();
      builder.append("\n");
      builder.append("\n==== Confusion Matrix ====\n");
           
      List<String> listOfClass = (ds.getArffAttributes().get(ds.getClassIndex())).getValues();
      
      builder.append("\t(predicted)\t\n");
      for ( String predictedClass: listOfClass) {
        builder.append("\t" + predictedClass);
      }
      builder.append("\n");
            
      for ( String actualClass: listOfClass) {
        builder.append(actualClass + "\t");
        
        for ( String predictedClass: listOfClass) {
          DiGram key = new DiGram(predictedClass, actualClass);
          builder.append( ((confusionMatrix.get(key) == null) ? 0 : confusionMatrix.get(key)) + "\t");
        }
        
        builder.append("\n");
      }
      
      return builder.toString();
    }
}
