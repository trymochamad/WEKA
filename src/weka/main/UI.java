/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.main;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Asus
 */
public class UI {
  private JFrame mainFrame;
  private JPanel controlPanel;
  private JPanel openFilePanel;
  private JPanel algorithmPanel;
  private JPanel trainingPanel;
  private JPanel resultPanel;
   
  int algorithm;
  int scheme;
  String path;
  int k;
  int kfold;
  int theClass;
  
  public UI() {
    prepare();
  }
  
  public static void main(String[] args){
     UI ui = new UI();   
  }
  
  private void prepare(){
    mainFrame = new JFrame("WEKA-WEKA");
    mainFrame.setSize(400,400);
    mainFrame.setLayout(new GridLayout(5, 1));
    mainFrame.addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent windowEvent){
        System.exit(0);
       }        
    });    
    
    openFilePanel = new JPanel();
    openFilePanel.setLayout(new FlowLayout());
    algorithmPanel = new JPanel();
    algorithmPanel.setLayout(new FlowLayout());
    trainingPanel = new JPanel();
    trainingPanel.setLayout(new FlowLayout());
    resultPanel = new JPanel();
    resultPanel.setLayout(new FlowLayout());
      
    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    showOpenFileButton(openFilePanel);
    showAlgorithmChooser(algorithmPanel);
    showTrainingType(trainingPanel);;
    showResultArea(resultPanel);

    JButton run = new JButton("Run");

    mainFrame.add(openFilePanel);
    mainFrame.add(algorithmPanel);
    mainFrame.add(trainingPanel);
    mainFrame.add(run);
    mainFrame.add(resultPanel);
    
    run.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {     
          Main.doAlgorithm(algorithm, scheme, path, k, kfold, theClass);        
       }
    });   
        
    mainFrame.setVisible(true);  
 }
  
  private void showOpenFileButton(final JPanel JP){
    final JFileChooser  fileDialog = new JFileChooser();
    final JTextField filePath = new JTextField("", 20); 
    JButton showFileDialogButton = new JButton("Open File");
    
    showFileDialogButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(ActionEvent e) {
          int returnVal = fileDialog.showOpenDialog(mainFrame);
          if (returnVal == JFileChooser.APPROVE_OPTION) {
             java.io.File file = fileDialog.getSelectedFile();
             filePath.setText(file.getPath());
             path = file.getPath();
             showClassChooser(JP);
          }
       }
    });

    JP.add(showFileDialogButton);
    JP.add(filePath);   
    mainFrame.setVisible(true);  
  }
  
  private void showClassChooser(JPanel JP) {
    final DefaultComboBoxModel className = new DefaultComboBoxModel();
    
    for ( String attr: Main.readFile(path) ) {
      className.addElement(attr);      
    }
    
    final JComboBox classCombo = new JComboBox(className);    
    classCombo.setSelectedIndex(classCombo.getItemCount()-1);

    JScrollPane algorithmList = new JScrollPane(classCombo);    

    theClass = classCombo.getItemCount()-1;
    classCombo.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) { 
         theClass = classCombo.getSelectedIndex();
       }
    });
    
    JP.add(algorithmList);  
    mainFrame.setVisible(true);
  }
  
  private void showAlgorithmChooser(final JPanel JP) {
    final DefaultComboBoxModel algorithmName = new DefaultComboBoxModel();
    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    
    algorithmName.addElement("KNN");
    algorithmName.addElement("Naive Bayes");

    final JComboBox algorithmCombo = new JComboBox(algorithmName);    
    algorithmCombo.setSelectedIndex(1);
    final JFormattedTextField kNN = new JFormattedTextField(numberFormat);
    kNN.setValue(new Integer(2));
    kNN.setColumns(4);

    JScrollPane algorithmList = new JScrollPane(algorithmCombo);    

    algorithm = 2;
    k = 2;
    algorithmCombo.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) { 
          if (algorithmCombo.getSelectedIndex() == 0) {                     
            JP.add(kNN);
          } else {
            JP.remove(kNN);
          }
          algorithm = algorithmCombo.getSelectedIndex() + 1;
          JP.revalidate(); 
          JP.repaint();
       }
    });
    
    kNN.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        change();
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
        change();
      }
      @Override
      public void insertUpdate(DocumentEvent e) {
        change();
      }

      public void change() {
        try {
         k = Integer.parseInt(kNN.getText());
        } catch (NumberFormatException e) {
          System.out.println(e);
        }
      }
    });
    
    JP.add(algorithmList);  
    mainFrame.setVisible(true);
  }
  
  private void showTrainingType(JPanel JP){
    final JRadioButton fullTrain = new JRadioButton("Full Training", true);
    final JRadioButton foldTrain = new JRadioButton("N-Fold Training");
    
    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    final JFormattedTextField fold = new JFormattedTextField(numberFormat);
    fold.setValue(new Integer(10));
    fold.setColumns(4);

    ButtonGroup trainingGroup = new ButtonGroup();
    trainingGroup.add(fullTrain);
    trainingGroup.add(foldTrain);
    
    scheme = 1;
    kfold = 10;
    fullTrain.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {         
        if (e.getStateChange()==1) { 
          scheme = 1;
        } else {
          scheme = 2;
        }
      }           
    });
    
    fold.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        change();
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
        change();
      }
      @Override
      public void insertUpdate(DocumentEvent e) {
        change();
      }

      public void change() {
        try {
         kfold = Integer.parseInt(fold.getText());
        } catch (NumberFormatException e) {
          System.out.println(e);
        }
      }
    });

    JP.add(fullTrain);
    JP.add(foldTrain);   
    JP.add(fold);

    mainFrame.setVisible(true);  
  }
  
  private void showResultArea(JPanel JP) {
    final JTextArea resultTextArea = new JTextArea("",5,20);
    resultTextArea.setEditable(false);
    
    JP.add(resultTextArea);
    mainFrame.setVisible(true); 
  }
}
