package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import neuroph.Network;

import java.awt.Color;

import javax.swing.border.LineBorder;



public class NetworkGui extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NetworkGui frame = new NetworkGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NetworkGui() {
		setTitle("Polish Companies Bankruptcy Calculator");

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 880, 756);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel manualInput = new ManualInputPanel();
		manualInput.setBorder(new LineBorder(new Color(0, 0, 0)));
		manualInput.setBounds(10, 96, 854, 461);
		contentPane.add(manualInput);
		
		JPanel automaticInput = new AutomaticInputPanel();
		automaticInput.setBounds(10, 568, 854, 74);
		automaticInput.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(automaticInput);
		
		JPanel calculationPanel = new CalculationPanel((ManualInputPanel)manualInput, (AutomaticInputPanel)automaticInput);
		calculationPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		calculationPanel.setBounds(10, 653, 854, 65);
		contentPane.add(calculationPanel);
		
		JPanel panel = new NetworkPanel((CalculationPanel)calculationPanel );
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 854, 74);
		contentPane.add(panel);
		
	}
}
