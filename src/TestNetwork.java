import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;



public class TestNetwork {
	public static void main(String[] args){
	
		ArrayList<Integer> layers = new ArrayList<Integer>();
		layers.add(3);
		layers.add(2);
		layers.add(1);
		NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(layers, TransferFunctionType.SIGMOID);
		MomentumBackpropagation bp = new MomentumBackpropagation();
		bp.setMomentum(0.3);//*/
		bp.setMaxError(0.0001);
		bp.setMaxIterations(1000);//This thing here :(
		//BackPropagation bp = new BackPropagation();
		bp.setLearningRate(0.1);
		neuralNetwork.setLearningRule(bp);
		
		generateTrainingSet();
		DataSet trainingSet = generateTrainingSet();
		
		DataSet testingSet = generateTestingSet();
		
		
		LearningThread trainer = new LearningThread();
		trainer.trainUntilAccurate(neuralNetwork, trainingSet, 0.98);

		
		double[] rgb = readRGB();
		
		neuralNetwork.setInput(rgb);
		neuralNetwork.calculate();
		double[] networkOutput = neuralNetwork.getOutput();
		
		System.out.println("Result: " + networkOutput[0]);
		
		
	}

	private static double[] readRGB() {
		System.out.println("Input a color(RGB), with each value between 0..1. Result: 1 if R+G+B > 1.5, 0 otherwise. ");
		
		double[] rgb = new double[3];
		Scanner in = new Scanner(System.in);
		System.out.print("Red  : ");
		rgb[0] = in.nextDouble();
		System.out.print("Green: ");
		rgb[1] = in.nextDouble();
		System.out.print("Blue : ");
		rgb[2] = in.nextDouble();

		return rgb;
	}

	private static DataSet generateTrainingSet() {
		Random r = new Random();
		
		DataSet set = new DataSet(3, 1);
		
		for(int i = 0; i < 500; i++){
			double red = r.nextDouble();
			double blue = r.nextDouble();
			double green = r.nextDouble();
			double res = 0;
			if (red + blue + green >= 1.5){
				res = 1;
			}
			
			
			//Uncomment to generate nulls
			double MC = r.nextDouble();
			if (MC < 0.01)
				red = ArrfReader.NULLVAL;
			else if(MC < 0.02)
				blue = ArrfReader.NULLVAL;
			else if(MC < 0.03)
				green = ArrfReader.NULLVAL;
			else if(MC < 0.04)
				res = ArrfReader.NULLVAL;
			
			set.addRow(new DataSetRow(new double[]{red, blue, green}, new double[]{res}));
		}
		return set;
	}
	
	private static DataSet generateTestingSet() {
		Random r = new Random();
		
		DataSet set = new DataSet(3, 1);
		
		for(int i = 0; i < 50; i++){
			double red = r.nextDouble();
			double blue = r.nextDouble();
			double green = r.nextDouble();
			double res = 0;
			if (red + blue + green >= 1.5){
				res = 1;
			}
			
			/*double MC = r.nextDouble();
			if (MC < 0.01)
				red = ArrfReader.NULLVAL;
			else if(MC < 0.02)
				blue = ArrfReader.NULLVAL;
			else if(MC < 0.03)
				green = ArrfReader.NULLVAL;
			else if(MC < 0.04)
				res = ArrfReader.NULLVAL;*/
			
			set.addRow(new DataSetRow(new double[]{red, blue, green}, new double[]{res}));
		}
		return set;
	}
}
