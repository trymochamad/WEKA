/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import weka.algorithm.*;
import weka.data.*;

/**
 *
 * @author visat
 */
public class Main {
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

    public static void doAlgorithm(int algorithm, int scheme, String fileName, int knn, int kfold, int theClass) {
        Algorithm algo = null;
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
                    nb.writeModel(dataStore);
                    break;
                default:
                    return;
            }
            int correctFull = 0;
            for (int i = 0; i < dataStore.getElementSize(); ++i) {
                String prediction = algo.predict(dataStore.getAttributes(i));
                if (dataStore.getClass(i).equals(prediction))
                    ++correctFull;
            }
            System.out.println(algo.getAlgorithmName() + " full training: " + correctFull + "/" + dataStore.getElementSize() + " correct (" + Double.toString((correctFull*100)/dataStore.getElementSize()) + "%)");
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
                    default:
                        return;
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
              nb.writeModel(dataStore);
            }
            
            int correctFold = 0;
            for (int i = 0; i < dataStore.getElementSize(); ++i) {
                String prediction = algoFold.predict(dataStore.getAttributes(i));
                if (dataStore.getClass(i).equals(prediction))
                    ++correctFold;
            }
            System.out.println(algoFold.getAlgorithmName() + " " + String.valueOf(kfold) + "-fold cross-validation: " + correctFold + "/" + dataStore.getElementSize() + " correct (" + Double.toString((correctFold*100)/dataStore.getElementSize()) + "%)");
        }
    }
}
