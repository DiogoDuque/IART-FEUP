package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
		textField.getText();
		//TODO
		return null;
	}

}
