package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import neuralNetwork.Network;

public class NetworkPanel extends JPanel {
	private static final String[] datasets = {"test", "1year", "2year", "3year", "4year", "5year"};
	
	private Network network;
	private CalculationPanel calcPanel;
	
	private JButton trainNetworkBtn;
	private JComboBox<String> datasetComboBox;
	private JLabel datasetComboBoxLbl;
	private JLabel accuracyTextLbl;
	private JLabel accuracyLbl;
	private JLabel stateLabel;
	
	private JSpinner maxError;
	private JSpinner maxIterations;
	private JSpinner learningRate;
	private JSpinner momentum;
	
	public NetworkPanel(final Network network, final CalculationPanel calcPanel){
		super();
		
		this.network = network;
		this.calcPanel = calcPanel;
		
		this.setLayout(null);
		
		trainNetworkBtn = new JButton("Train");
		trainNetworkBtn.setBounds(600, 11, 90, 51);
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
		stateLabel.setBounds(700, 11, 140, 51);
		this.add(stateLabel);
		
		datasetComboBox = new JComboBox<String>(datasets);
		datasetComboBox.setBounds(130, 11, 60, 20);
		datasetComboBox.setSelectedIndex(0);
		this.add(datasetComboBox);
		
		datasetComboBoxLbl = new JLabel("Select dataset:");
		datasetComboBoxLbl.setLabelFor(datasetComboBox);
		datasetComboBoxLbl.setBounds(24, 11, 120, 20);
		this.add(datasetComboBoxLbl);
		
		accuracyTextLbl = new JLabel("Current accuracy:");
		accuracyTextLbl.setBounds(24, 42, 120, 20);
		this.add(accuracyTextLbl);
		
		accuracyLbl = new JLabel("0%");
		accuracyLbl.setBounds(130, 42, 60, 20);
		this.add(accuracyLbl);
		
		
		maxError = new JSpinner();
		maxError.setModel(new SpinnerNumberModel(0.0001, 0.0001, 1.0, 0.0001));
		maxError.setBounds(300, 11, 90, 20);
		maxError.setEditor(new JSpinner.NumberEditor(maxError, "0.0000"));
		this.add(maxError);
		
		JLabel maxErrorLbl = new JLabel("Max Error:");
		maxErrorLbl.setBounds(200, 14, 90, 14);
		maxErrorLbl.setLabelFor(maxError);
		this.add(maxErrorLbl);
		
		maxIterations = new JSpinner();
		maxIterations.setModel(new SpinnerNumberModel(1000, 0,10000, 1));
		maxIterations.setBounds(300, 42, 90, 20);
		this.add(maxIterations);
		
		JLabel maxIterationsLbl = new JLabel("Max Iterations:");
		maxIterationsLbl.setBounds(200, 45, 90, 14);
		maxIterationsLbl.setLabelFor(maxIterations);
		this.add(maxIterationsLbl);
		
		
		learningRate = new JSpinner();
		learningRate.setModel(new SpinnerNumberModel(0.3,  0, 1, 0.01));
		learningRate.setBounds(500, 11, 90, 20);
		learningRate.setEditor(new JSpinner.NumberEditor(learningRate, "0.00"));
		this.add(learningRate);
		
		JLabel learningRateLbl = new JLabel("Learning Rate:");
		learningRateLbl.setBounds(400, 14, 90, 14);
		learningRateLbl.setLabelFor(learningRate);
		this.add(learningRateLbl);
		
		momentum = new JSpinner();
		momentum.setModel(new SpinnerNumberModel(0.8, 0, 1, 0.01));
		momentum.setBounds(500, 42, 90, 20);
		momentum.setEditor(new JSpinner.NumberEditor(momentum, "0.00"));
		this.add(momentum);
		
		JLabel momentumLbl = new JLabel("Momentum:");
		momentumLbl.setBounds(400, 45, 90, 14);
		momentumLbl.setLabelFor(momentum);
		this.add(momentumLbl);
		
	}

	private void reset() {
		network.setReady(false);
		trainNetworkBtn.setEnabled(false);
		calcPanel.getCalculateButton().setEnabled(false);
		accuracyLbl.setVisible(false);
		accuracyTextLbl.setVisible(false);
		stateLabel.setText("Learning...");
		
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
