package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import neuralNetwork.Network;

public class CalculationPanel extends JPanel {
	Network network;
	ManualInputPanel manual;
	AutomaticInputPanel auto;
	private JLabel resultLabel;
	private JButton calculateButton;
	private JRadioButton autoButton;
	private JRadioButton manualButton;
	
	
	
	
	/**
	 * Create the panel.
	 */
	public CalculationPanel(final Network network, final ManualInputPanel manual, final AutomaticInputPanel auto) {
		super();
		this.setLayout(null);
		
		this.network = network;
		this.manual = manual;
		this.auto = auto;
		
		manualButton = new JRadioButton("Use Manual Input");
		manualButton.setBounds(6, 7, 141, 23);
		this.add(manualButton);
		
		autoButton = new JRadioButton("Use Automatic Input");
		autoButton.setBounds(6, 33, 141, 23);
		autoButton.setSelected(true);
		this.add(autoButton);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(manualButton);
	    group.add(autoButton);
	       
	 
		
		calculateButton = new JButton("Calculate");
		calculateButton.setEnabled(false);
		calculateButton.setBounds(153, 7, 89, 49);
		calculateButton.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent ae){
		    	   double[] data;
		    	   if(autoButton.isSelected())
		    		   data = auto.getData();
		    	   else
		    		   data = manual.getData();
		    	   
		    	   if(data == null){
		    		   resultLabel.setText("Error");
		    		   return;
		    	   }
		    	   
		    	   double result = Network.ask(network.getNetwork(), data);
		    	   
		    	   resultLabel.setText("The neural network says: " + result);
		       }
		       
		       
		});
		this.add(calculateButton);
		
		resultLabel = new JLabel("");
		resultLabel.setBounds(252, 11, 592, 45);
		this.add(resultLabel);
	}




	public JButton getCalculateButton() {
		return calculateButton;
	}
	
	

}