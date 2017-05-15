package gui;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import neuralNetwork.ArrfReader;

public class ManualInputPanel extends JPanel {
	private final static int INITIAL_X = 10;
	private final static int INITIAL_Y = 11;
	private final static int WIDTH = 90;
	private final static int WIDTH_LBL = 170;
	private final static int HEIGHT = 20;
	private final static int GAP = 10;
	
	private int x;
	private int y;
	
	Map<String, JTextField> values;

	/**
	 * Create the panel.
	 */
	public ManualInputPanel() {
		this.setLayout(null);
		values = new HashMap<String, JTextField>();
		x = INITIAL_X;
		y = INITIAL_Y;
		
		addFields();
	}

	private double get(String name){
		JTextField field = values.get(name);
		if (field == null)
			return ArrfReader.NULLVAL;
		
		try
		{
			double result = Double.parseDouble(field.getText());
		  	return result;
		}
		catch(NumberFormatException e)
		{
			return ArrfReader.NULLVAL;
		}
		
	}
	
	
	private void addFields() {

		//4
		addTitle("Assets");
		addField("Total Assets");
		addField("Current Assets");
		addField("Fixed Assets");
		addField("Short-term Securities");
		
		addTitle("Capital");
		addField("Working Capital");
		addField("Share Capital");
		addField("Constant Capital");
		addField("Cash");
		
		//
		addTitle("Liabilities");
		addField("Total Liabilities");
		addField("Short-term Liabilities");
		addField("Long-term Liabilities");
		
		newLine();
		
		//
		addTitle("Profit");
		addField("Net Profit");
		addField("Gross Profit");
		addField("Gross Profit (in 3 years)");
		addField("Profit on Operating Activities");
		addField("Profit on Sales");

		addTitle("Income");
		addField("Sales");
		addField("Total Sales");
		addField("Last Year Sales");
		addField("Receivables");
		addField("Rotation Receivables");
		addField("Retained Earnings");
		addField("EBIT");

		newLine();
		
		addTitle("Expenses");
		addField("Total Costs");
		addField("Cost of Products Sold");
		addField("Extraordinary Items");
		addField("Financial Expenses");
		addField("Operating Expenses");
	
		
		addTitle("Inventory");
		addField("Inventory");
		addField("Inventory Turnover in Days");
	
		addTitle("Other");
		addField("Book Value of Equity");
		addField("Equity");
		addField("Interest");
		addField("Depreciation");
	
		
		
	}

	private void addTitle(String string) {
		JLabel label = new JLabel(string);
		label.setBounds(x, y, WIDTH_LBL, HEIGHT + GAP / 2);
		
		Font font = label.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, 20);
		label.setFont(boldFont);
		
		this.add(label);
		
		y += HEIGHT + GAP;
	}


	public void addField(String name){
		
		if(values.get(name) != null)
		try {
			throw new Exception();
		} catch (Exception e) {
			System.out.println(name);
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JTextField field = new JTextField();
		field.setBounds(x + GAP + WIDTH_LBL, y, WIDTH, HEIGHT);
		
		JLabel label = new JLabel(name + " :");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(x, y, WIDTH_LBL, HEIGHT);
		label.setLabelFor(field);
		
		y += HEIGHT + GAP;
		
		
		this.add(field);
		this.add(label);
		
		values.put(name, field);
	}
		
	public void newLine(){
		y = INITIAL_Y;
		x += WIDTH_LBL + GAP + WIDTH + GAP;
	}

	public double[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
