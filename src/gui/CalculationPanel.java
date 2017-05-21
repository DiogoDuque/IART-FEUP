package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import neuroph.Network;

public class CalculationPanel extends JPanel {
	ManualInputPanel manual;
	AutomaticInputPanel auto;
	private JLabel resultLabel;
	private JButton calculateButton;
	private JRadioButton autoButton;
	private JRadioButton manualButton;
	
	
	
	
	/**
	 * Create the panel.
	 */
	public CalculationPanel(final ManualInputPanel manual, final AutomaticInputPanel auto) {
		super();
		this.setLayout(null);

		this.manual = manual;
		this.auto = auto;
		
		manualButton = new JRadioButton("Use Manual Input");
		manualButton.setBounds(6, 7, 200, 23);
		this.add(manualButton);
		
		autoButton = new JRadioButton("Use Automatic Input");
		autoButton.setBounds(6, 33, 200, 23);
		autoButton.setSelected(true);
		this.add(autoButton);
		
		ButtonGroup group = new ButtonGroup();
	    group.add(manualButton);
	    group.add(autoButton);
	       
	 
		
		calculateButton = new JButton("Calculate");
		calculateButton.setEnabled(false);
		calculateButton.setBounds(220, 7, 300, 49);
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

                   System.out.println("Input raw array: ");
                   System.out.println(Arrays.toString(data));
		    	   double[] normalizedData = Network.normalizer.normalizeInputArray(data);
                   System.out.println("Input normalized array: ");
                   System.out.println(Arrays.toString(normalizedData));

                   // TODO double result = Network.ask(network.getNetwork(), normalizedData);
		    	   
		    	   // TODO resultLabel.setText("The neural network says: " + result);
		       }
		       
		       
		});
		this.add(calculateButton);
		
		resultLabel = new JLabel("");
		resultLabel.setBounds(330, 11, 600, 45);
		this.add(resultLabel);
	}




	public JButton getCalculateButton() {
		return calculateButton;
	}
	
	

}
