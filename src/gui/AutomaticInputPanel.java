package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import static reader.ArrfReader.NULLVAL;

public class AutomaticInputPanel extends JPanel {
	private JTextField textField;
	/**
	 * Create the panel.
	 */
	public AutomaticInputPanel() {
		super();
		this.setLayout(null);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setBounds(10, 36, 834, 20);
		this.add(textField);
		
		JLabel inputLabel = new JLabel("Input Data:");
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inputLabel.setBounds(10, 11, 834, 20);
		inputLabel.setLabelFor(textField);
		this.add(inputLabel);
	}
	
	public double[] getData(){
		String value = textField.getText();
		if (value.equals(""))
			return null;
		
		String[] values = value.split(",");
		if (values.length != 64) {
            System.err.println("Wrong number of input. Must be 64 instead of: " + values.length);
            return null;
        }
		
		double[] data = new double[64];
		for(int i = 0; i < 64; i++){
			try
			{
				if(values[i].equalsIgnoreCase("?")) {
					data[i] = NULLVAL;	// so it allows people to declare missing values on the automatic input field
				}
				else {
					data[i] = Double.valueOf(values[i]);
				}
			}
			catch(NumberFormatException e)
			{
				System.err.println(values[i]);
				return null;
			}
		}
		
		return data;
	}

}
