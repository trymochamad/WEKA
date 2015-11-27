/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.main;

import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import weka.data.Attribute;
import weka.data.DataStore;

/**
 *
 * @author visat
 */
public class MainFrame extends javax.swing.JFrame {
    private static final String TRAINING_FULL = "Full Training";
    private static final String TRAINING_N_FOLD = "N-Fold Training";

    private static final String ALGO_KNN = "kNN";
    private static final String ALGO_NAIVE_BAYES = "Naive Bayes";
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelFileName = new javax.swing.JLabel();
        textFileName = new javax.swing.JTextField();
        buttonFileName = new javax.swing.JButton();
        comboAlgorithm = new javax.swing.JComboBox<>();
        labelAlgorithm = new javax.swing.JLabel();
        spinnerAlgorithm = new javax.swing.JSpinner();
        labelTraining = new javax.swing.JLabel();
        comboTraining = new javax.swing.JComboBox<>();
        spinnerTraining = new javax.swing.JSpinner();
        buttonRun = new javax.swing.JButton();
        paneResult = new javax.swing.JScrollPane();
        textResult = new javax.swing.JTextArea();
        labelClass = new javax.swing.JLabel();
        comboClass = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WEKA-WEKA");
        setName("mainFrame"); // NOI18N

        labelFileName.setText("File name:");

        textFileName.setEditable(false);

        buttonFileName.setText("...");
        buttonFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFileNameActionPerformed(evt);
            }
        });

        comboAlgorithm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ALGO_KNN, ALGO_NAIVE_BAYES }));
        comboAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboAlgorithmActionPerformed(evt);
            }
        });

        labelAlgorithm.setText("Algorithm:");

        spinnerAlgorithm.setModel(new javax.swing.SpinnerNumberModel(1, 1, 1, 1));

        labelTraining.setText("Training Schema:");

        comboTraining.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { TRAINING_N_FOLD, TRAINING_FULL }));
        comboTraining.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTrainingActionPerformed(evt);
            }
        });

        spinnerTraining.setModel(new javax.swing.SpinnerNumberModel(10, 1, 100, 1));

        buttonRun.setText("Run");
        buttonRun.setEnabled(false);
        buttonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunActionPerformed(evt);
            }
        });

        textResult.setEditable(false);
        textResult.setColumns(20);
        textResult.setRows(5);
        paneResult.setViewportView(textResult);

        labelClass.setText("Choose class:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboClass, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paneResult, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(textFileName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFileName)
                            .addComponent(buttonRun, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTraining)
                            .addComponent(labelClass)
                            .addComponent(labelAlgorithm))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comboTraining, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboAlgorithm, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerAlgorithm)
                            .addComponent(spinnerTraining))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonFileName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAlgorithm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTraining)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTraining, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerTraining, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelClass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonRun)
                .addGap(7, 7, 7)
                .addComponent(paneResult)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFileNameActionPerformed
        final JFileChooser fileDialog = new JFileChooser();
        int returnVal = fileDialog.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fileDialog.getSelectedFile().getPath();
            textFileName.setText(fileName);
            comboClass.removeAllItems();
            DataStore ds = new DataStore();
            ds.readArff(fileName);
            ds.read();
            for (Attribute attr: ds.getArffAttributes())
                comboClass.addItem(attr.getName());            
            if (ds.getElementSize() > 0)
                spinnerAlgorithm.setModel(new SpinnerNumberModel(1, 1, ds.getElementSize(), 1));
            comboClass.setSelectedIndex(0);
            buttonRun.setEnabled(true);
        }
    }//GEN-LAST:event_buttonFileNameActionPerformed

    private void comboAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboAlgorithmActionPerformed
        String algorithm = (String)comboAlgorithm.getSelectedItem();
        if (algorithm.equalsIgnoreCase("kNN"))
            spinnerAlgorithm.setVisible(true);
        else
            spinnerAlgorithm.setVisible(false);
    }//GEN-LAST:event_comboAlgorithmActionPerformed

    private void comboTrainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTrainingActionPerformed
        String training = (String)comboTraining.getSelectedItem();
        if (training.equalsIgnoreCase("N-Fold Training"))
            spinnerTraining.setVisible(true);
        else
            spinnerTraining.setVisible(false);
    }//GEN-LAST:event_comboTrainingActionPerformed

    private void buttonRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunActionPerformed
        int algorithm = 1;
        switch ((String)comboAlgorithm.getSelectedItem()) {
            case ALGO_KNN:
                algorithm = 1;
                break;
            case ALGO_NAIVE_BAYES:
                algorithm = 2;
                break;
        }
        int training = 1;
        switch ((String)comboTraining.getSelectedItem()) {
            case TRAINING_FULL:
                training = 1;
                break;
            case TRAINING_N_FOLD:
                training = 2;
                break;
        }
        textResult.setText(Main.doAlgorithm(
                algorithm, training, textFileName.getText(),
                (int)spinnerAlgorithm.getValue(), (int)spinnerTraining.getValue(),
                comboClass.getSelectedIndex()));
    }//GEN-LAST:event_buttonRunActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonFileName;
    private javax.swing.JButton buttonRun;
    private javax.swing.JComboBox<String> comboAlgorithm;
    private javax.swing.JComboBox<String> comboClass;
    private javax.swing.JComboBox<String> comboTraining;
    private javax.swing.JLabel labelAlgorithm;
    private javax.swing.JLabel labelClass;
    private javax.swing.JLabel labelFileName;
    private javax.swing.JLabel labelTraining;
    private javax.swing.JScrollPane paneResult;
    private javax.swing.JSpinner spinnerAlgorithm;
    private javax.swing.JSpinner spinnerTraining;
    private javax.swing.JTextField textFileName;
    private javax.swing.JTextArea textResult;
    // End of variables declaration//GEN-END:variables
}
