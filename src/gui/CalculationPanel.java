package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import encog.MyNetwork;
import neuroph.Network;
import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;

public class CalculationPanel extends JPanel {
	ManualInputPanel manual;
	AutomaticInputPanel auto;
	private JLabel resultLabel;
	private JButton calculateButton;
	private JRadioButton autoButton;
	private JRadioButton manualButton;

	MyNetwork myNetwork;

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


		    	   double[] normalizedData = myNetwork.getNormalizer().normalizeInputArray(data);

                   System.out.println("Input normalized array: ");
                   System.out.println(Arrays.toString(normalizedData));


                   double result = myNetwork.ask(new BasicMLData(normalizedData));
		    	   
		    	   resultLabel.setText("The neural network says: " + result);
		       }
		       
		       
		});
		this.add(calculateButton);
		
		resultLabel = new JLabel("");
		resultLabel.setBounds(550, 11, 600, 45);
		this.add(resultLabel);
	}

	public void setMyNetwork(MyNetwork myNetwork) {
		this.myNetwork = myNetwork;
	}

	public JButton getCalculateButton() {
		return calculateButton;
	}
	
	

}
