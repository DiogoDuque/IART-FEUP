package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import neuroph.Network;

public class NetworkPanel extends JPanel {
	private static final String[] datasets = {"test2", "test", "1year", "2year", "3year", "4year", "5year"};
	
	private Network network;
	private CalculationPanel calcPanel;
	
	private JButton trainNetworkBtn;
	private JComboBox<String> datasetComboBox;
	private JLabel datasetComboBoxLbl;
	private JLabel accuracyTextLbl;
	private JLabel accuracyLbl;
	private JLabel stateLabel;
	
	private JSpinner maxError;
	private JCheckBox normalize;
	
	public NetworkPanel(final Network network, final CalculationPanel calcPanel){
		super();
		
		this.network = network;
		this.calcPanel = calcPanel;
		
		this.setLayout(null);
		
		trainNetworkBtn = new JButton("Train");
		trainNetworkBtn.setBounds(500, 11, 90, 51);
		trainNetworkBtn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent ae){		    	   
		    	   SwingWorker<Double, Object> swingWorker = new SwingWorker<Double, Object>() {
		               @Override
		               protected Double doInBackground() throws Exception {
		            	   String file = (String) datasetComboBox.getSelectedItem();
				    	   double accuracy = Network.train(network.getNetwork(), file);

		                   return accuracy;
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
		
		stateLabel = new JLabel("Network not trained!");
		stateLabel.setBounds(600, 11, 250, 51);
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
		maxError.setModel(new SpinnerNumberModel(0.0001, 0.0001, 1.0, 0.0001));
		maxError.setBounds(400, 11, 90, 20);
		maxError.setEditor(new JSpinner.NumberEditor(maxError, "0.0000"));
		this.add(maxError);

		JLabel maxErrorLbl = new JLabel("Max Error:");
		maxErrorLbl.setBounds(300, 14, 90, 14);
		maxErrorLbl.setLabelFor(maxError);
		this.add(maxErrorLbl);

		normalize = new JCheckBox("Normalize");
		normalize.setBounds(300, 42, 180, 20);
		normalize.setSelected(true);
		this.add(normalize);

	}

	private void reset() {
		network.setReady(false);
		trainNetworkBtn.setEnabled(false);
		calcPanel.getCalculateButton().setEnabled(false);
		accuracyLbl.setVisible(false);
		accuracyTextLbl.setVisible(false);
		stateLabel.setText("Learning...");

		network.getRule().setMaxError((Double)maxError.getValue());
		
	} 
	
	private void result(double accuracy) {
		network.setReady(true);
		trainNetworkBtn.setEnabled(true);
		calcPanel.getCalculateButton().setEnabled(true);
		accuracyLbl.setText(String.format( "%.2f", accuracy*100 ) + " %");
		accuracyLbl.setVisible(true);
		accuracyTextLbl.setVisible(true);
		stateLabel.setText("Ready!");
		
	} 
	
	
}
