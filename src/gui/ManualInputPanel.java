package gui;

import java.awt.Font;
import java.util.Arrays;
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
			double result = Double.valueOf(field.getText());
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
		addField("Current Liabilities");
		
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
		double TA = get("Total Assets");
		double CA = get("Current Assets");
		double FA = get("Fixed Assets");
		double STS = get("Short-term Securities");
		double WC = get("Working Capital");
		double SC = get("Share Capital");
		double CC = get("Constant Capital");
		double C = get("Cash");
		double TL = get("Total Liabilities");
		double STL = get("Short-term Liabilities");
		double LTL = get("Long-term Liabilities");
		double CL = get("Current Liabilities");
		double NP = get("Net Profit");
		double GP = get("Gross Profit");
		double GP3 = get("Gross Profit (in 3 years)");
		double POA = get("Profit on Operating Activities");
		double PS = get("Profit on Sales");
		double S = get("Sales");
		double TS = get("Total Sales");
		double LYS = get("Last Year Sales");
		double R = get("Receivables");
		double RR = get("Rotation Receivables");
		double RE = get("Retained Earnings");
		double EBIT = get("EBIT");
		double TC = get("Total Costs");
		double CPS = get("Cost of Products Sold");
		double EI = get("Extraordinary Items");
		double FE = get("Financial Expenses");
		double OE = get("Operating Expenses");
		double InV = get("Inventory");
		double ITD = get("Inventory Turnover in Days");
		double BVE = get("Book Value of Equity");
		double E = get("Equity");
		double InT = get("Interest");
		double D = get("Depreciation");
		
		
		double[] data = new double[64];
		Arrays.fill(data, ArrfReader.NULLVAL);

		if (NP != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[0] = NP / TA;
		
		if (TL != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[1] = TL / TA;
		
		if (WC != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[2] = WC / TA;
		
		if (CA != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[3] = CA / STL;
		
		if (C != ArrfReader.NULLVAL || STS != ArrfReader.NULLVAL || R != ArrfReader.NULLVAL ||
				STL != ArrfReader.NULLVAL || OE != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL)
			data[4] = (C + STS + R - STL) / (OE - D) * 365;
		
		if (RE != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[5] = RE / TA;
		
		if (EBIT != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[6] = EBIT / TA;
		
		if (BVE != ArrfReader.NULLVAL || TL != ArrfReader.NULLVAL)
			data[7] = BVE / TL;
		
		if (S != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[8] = S / TS;
		
		if (E != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[9] = E / TA;
		
		if (GP != ArrfReader.NULLVAL || EI != ArrfReader.NULLVAL ||
				FE != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[10] = (GP + EI + FE) / TA;
		
		if (GP != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[11] = GP / STL;

		if (GP != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL ||
				S != ArrfReader.NULLVAL)
			data[12] = (GP + D) / S;
		
		if (GP != ArrfReader.NULLVAL || InT != ArrfReader.NULLVAL ||
				TA != ArrfReader.NULLVAL)
			data[13] = (GP + InT) / TA;
		
		if (TL != ArrfReader.NULLVAL || GP != ArrfReader.NULLVAL ||
				D != ArrfReader.NULLVAL)
			data[14] = (TL * 365) / (GP + D);
		
		if (TL != ArrfReader.NULLVAL || GP != ArrfReader.NULLVAL ||
				D != ArrfReader.NULLVAL)
			data[15] = (GP + D) / TL;
		
		if (TA != ArrfReader.NULLVAL || TL != ArrfReader.NULLVAL)
			data[16] = TA / TL;
		
		if (GP != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[17] = GP / TA;
		
		if (GP != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[18] = GP / S;
		
		if (InV != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[19] = InV * 365 / S;
		
		if (S != ArrfReader.NULLVAL || LYS != ArrfReader.NULLVAL)
			data[20] = S / LYS;
		
		if (POA != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[21] = POA / TA;
		
		if (NP != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[22] = NP / S;
		
		if (GP3 != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[23] = GP3 / TA;
		
		if (E != ArrfReader.NULLVAL || SC != ArrfReader.NULLVAL ||
				TA != ArrfReader.NULLVAL)
			data[24] = (E - SC) / TA;
		
		if (NP != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL ||
				TL != ArrfReader.NULLVAL)
			data[25] = (NP + D) / TL;
		
		if (POA != ArrfReader.NULLVAL || FE != ArrfReader.NULLVAL)
			data[26] = POA / FE;
		
		if (WC != ArrfReader.NULLVAL || FA != ArrfReader.NULLVAL)
			data[27] = WC / FA;
		
		if (TA != ArrfReader.NULLVAL)
			data[28] = Math.log(TA);
		
		if (TL != ArrfReader.NULLVAL || C != ArrfReader.NULLVAL ||
				S != ArrfReader.NULLVAL)
			data[29] = (TL - C) / S;
		
		if (GP != ArrfReader.NULLVAL || InT != ArrfReader.NULLVAL ||
				S != ArrfReader.NULLVAL)
			data[30] = (GP + InT) / S;
		
		if (CL != ArrfReader.NULLVAL || CPS != ArrfReader.NULLVAL)
			data[31] = CL / CPS;
		
		if (OE != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[32] = OE / STL;
		
		if (OE != ArrfReader.NULLVAL || TL != ArrfReader.NULLVAL)
			data[33] = OE / TL;
		
		if (PS != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[34] = PS / TA;
		
		if (TS != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[35] = TS / TA;
		
		if (CA != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL ||
				LTL != ArrfReader.NULLVAL)
			data[36] = (CA - InV) / LTL;
		
		if (CC != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[37] = CC / TA;
		
		if (PS != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[38] = PS / S;
		
		if (CA != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL ||
				R != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[39] = (CA - InV - R) / STL;
		
		if (TL != ArrfReader.NULLVAL || POA != ArrfReader.NULLVAL ||
				D != ArrfReader.NULLVAL)
			data[40] = TL /((POA + D) * (12/365));
		
		if (POA != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[41] = POA / S;
		
		if (RR != ArrfReader.NULLVAL || ITD != ArrfReader.NULLVAL)
			data[42] = RR + ITD;
		
		if (R != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL)
			data[43] = (R * 365)/S;
		
		if (NP != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL)
			data[44] = NP / InV;
			
		if (CA != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL ||
				STL != ArrfReader.NULLVAL)
			data[45] = (CA - InV) / STL;
		
		if (InV != ArrfReader.NULLVAL || CPS != ArrfReader.NULLVAL)
			data[46] = (InV * 365) / CPS;
		
		if (POA != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL ||
				TA != ArrfReader.NULLVAL)
			data[47] = (POA - D) / TA;
		
		if (POA != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL ||
				S != ArrfReader.NULLVAL)
			data[48] = (POA - D) / S;
		
		if (CA != ArrfReader.NULLVAL || TL != ArrfReader.NULLVAL)
			data[49] = CA / TL;
		
		if (STL != ArrfReader.NULLVAL || TA != ArrfReader.NULLVAL)
			data[50] = STL / TA;
		
		if (STL != ArrfReader.NULLVAL || CPS != ArrfReader.NULLVAL)
			data[51] = (STL * 365) / CPS;
		
		if (E != ArrfReader.NULLVAL || FA != ArrfReader.NULLVAL)
			data[52] = E / FA;
		
		if (CC != ArrfReader.NULLVAL || FA != ArrfReader.NULLVAL)
			data[53] = CC / FA;
		
		if (WC != ArrfReader.NULLVAL)
			data[54] = WC;
		
		if (S != ArrfReader.NULLVAL || CPS != ArrfReader.NULLVAL)
			data[55] = (S - CPS) / S;

		if (CA != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL || S != ArrfReader.NULLVAL ||
				STL != ArrfReader.NULLVAL || GP != ArrfReader.NULLVAL || D != ArrfReader.NULLVAL)
			data[56] = (CA - InV - STL) / (S - GP - D) * 365;
		
		if (TC != ArrfReader.NULLVAL || TS != ArrfReader.NULLVAL)
			data[57] = TC / TS;
		
		if (LTL != ArrfReader.NULLVAL || E != ArrfReader.NULLVAL)
			data[58] = LTL / E;	
		
		if (S != ArrfReader.NULLVAL || InV != ArrfReader.NULLVAL)
			data[59] = S / InV;
		
		if (S != ArrfReader.NULLVAL || R != ArrfReader.NULLVAL)
			data[60] = S / R;
		
		if (S != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[61] = STL *365 / S;
		
		if (S != ArrfReader.NULLVAL || STL != ArrfReader.NULLVAL)
			data[62] = S / STL;
		
		if (S != ArrfReader.NULLVAL || FA != ArrfReader.NULLVAL)
			data[63] = S / FA;
		
		return data;
		
		//TODO 37 INVENTORIES INSTEAD OF INVENTORY
	}

}
