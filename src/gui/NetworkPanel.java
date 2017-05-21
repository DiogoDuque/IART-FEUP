package gui;

import encog.Main;
import encog.MyNetwork;
import reader.ArrfReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.*;


public class NetworkPanel extends JPanel {
	private static final String[] datasets = {"test2", "test", "1year", "2year", "3year", "4year", "5year"};

	private CalculationPanel calcPanel;

	private JButton trainNetworkBtn;
	private JComboBox<String> datasetComboBox;
	private JLabel datasetComboBoxLbl;
	private JLabel accuracyTextLbl;
	private JLabel accuracyLbl;
	private JLabel stateLabel;
	
	private JSpinner maxError;
	private JSpinner maxIterations;
	private JCheckBox normalize;
	private JCheckBox useMean;
	
	public NetworkPanel(final CalculationPanel calcPanel){
		super();

		this.calcPanel = calcPanel;
		
		this.setLayout(null);
		
		trainNetworkBtn = new JButton("<html>Create<br/>& Train</html>");
		trainNetworkBtn.setBounds(650, 11, 90, 51);
		trainNetworkBtn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent ae){		    	   
		    	   SwingWorker<Double, Object> swingWorker = new SwingWorker<Double, Object>() {
		               @Override
		               protected Double doInBackground() throws Exception {
		            	   String file = (String) datasetComboBox.getSelectedItem();
                           ArrfReader reader = new ArrfReader("dataset/" + file + ".arff", useMean.isSelected());
                           MyNetwork myNetwork = Main.run(reader, normalize.isSelected(), (double) maxError.getValue(), (int) maxIterations.getValue());
                           calcPanel.setMyNetwork(myNetwork);

		                   return Main.currentAccuracy;
		               }

		               @Override
		               protected void done() {
		                  try {
							result(get());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
		               }

		 

		           };

		           reset();
		           swingWorker.execute();
		       }
		       
		       
		});
		this.add(trainNetworkBtn);
		
		stateLabel = new JLabel("<html>Network not<br> trained!</html>");
		stateLabel.setBounds(750, 11, 250, 51);
		this.add(stateLabel);
		
		datasetComboBox = new JComboBox<String>(datasets);
		datasetComboBox.setBounds(190, 11, 100, 20);
		datasetComboBox.setSelectedIndex(0);
		this.add(datasetComboBox);
		
		datasetComboBoxLbl = new JLabel("Select dataset:");
		datasetComboBoxLbl.setLabelFor(datasetComboBox);
		datasetComboBoxLbl.setBounds(24, 11, 180, 20);
		this.add(datasetComboBoxLbl);
		
		accuracyTextLbl = new JLabel("Current accuracy:");
		accuracyTextLbl.setBounds(24, 42, 180, 20);
		this.add(accuracyTextLbl);
		
		accuracyLbl = new JLabel("0%");
		accuracyLbl.setBounds(190, 42, 100, 20);
		this.add(accuracyLbl);

		maxError = new JSpinner();
		maxError.setModel(new SpinnerNumberModel(0.15, 0.0001, 1.0, 0.01));
		maxError.setBounds(400, 11, 90, 20);
		maxError.setEditor(new JSpinner.NumberEditor(maxError, "0.00"));
		this.add(maxError);

		JLabel maxErrorLbl = new JLabel("Max Error:");
		maxErrorLbl.setBounds(300, 14, 90, 14);
		maxErrorLbl.setLabelFor(maxError);
		this.add(maxErrorLbl);

		maxIterations = new JSpinner();
		maxIterations.setModel(new SpinnerNumberModel(5000, 0, 100000000, 100));
		maxIterations.setBounds(400, 42, 90, 20);
		maxIterations.setEditor(new JSpinner.NumberEditor(maxIterations, "0"));
		this.add(maxIterations);

		JLabel maxIterationsLbl = new JLabel("Max Iter:");
		maxIterationsLbl.setBounds(300, 42, 90, 14);
		maxIterationsLbl.setLabelFor(maxError);
		this.add(maxIterationsLbl);

		normalize = new JCheckBox("Normalize");
		normalize.setBounds(500, 11, 100, 20);
		normalize.setSelected(true);
		this.add(normalize);

        useMean = new JCheckBox("Use mean");
        useMean.setBounds(500, 42, 100, 20);
        useMean.setSelected(true);
        this.add(useMean);

	}

	private void reset() {

		trainNetworkBtn.setEnabled(false);
		calcPanel.getCalculateButton().setEnabled(false);
		accuracyLbl.setVisible(false);
		accuracyTextLbl.setVisible(false);
		stateLabel.setText("Learning...");
		
	} 
	
	private void result(double accuracy) {
		trainNetworkBtn.setEnabled(true);
		calcPanel.getCalculateButton().setEnabled(true);
		accuracyLbl.setText(String.format( "%.2f", accuracy*100 ) + " %");
		accuracyLbl.setVisible(true);
		accuracyTextLbl.setVisible(true);
		stateLabel.setText("Ready!");
		
	} 
	
	
}
