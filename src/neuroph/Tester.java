package neuroph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

public class Tester {
	private final static int N = 1;
	private final static double E = 0.001;
	private final static int I = 1000;
	
	public static void main(String[] args) {
		List<Integer> vals= new ArrayList<Integer>();
		vals.add(64);
		vals.add(43);
		vals.add(1);
		analize(vals);
		
		
		
	}
	
	private static void calculate(double[][] results, List<Integer> ints, int i, int j){
		double meanAccuracy = 0;
		
		NeuralNetwork<BackPropagation> network = new MultiLayerPerceptron(ints, TransferFunctionType.SIGMOID);
		MomentumBackpropagation rule = new MomentumBackpropagation();
		network.setLearningRule(rule);
		rule.setMaxError(E);
		rule.setMaxIterations(I);//This thing here :(
		rule.setMomentum(j*10.0/100.0);//*/
		rule.setLearningRate(i*10.0/100.0);
		
		for(int k = 0; k < N; k++){
			meanAccuracy += Network.train(network, "test");
		}
		meanAccuracy /= N;
		
		results[i-1][j-1] = meanAccuracy;
	}
	
	private static void analize(List<Integer> ints){
		double[][] results = new double[10][10];
		for(int i = 1; i <= 10; i++){
			for(int j = 1; j <= 10; j++){
				calculate(results, ints, i, j);
				updateLog(results, ints);
			}
		}
	}

	private static void updateLog(double[][] results, List<Integer> ints) {
		String content = "Neural Network(";
		for(int i=0;i<ints.size() - 1;i++){
			content += ints.get(i) + ",";
		} 
		content += ints.get(ints.size()-1) + ")\n";
		
		content += "Trainings by cell: " + N + "\n";
		content += "Max error: " + E + "\n";
		content += "Max Iterations: " + I + "\n";
		
		
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				content += String.format("%1$,.2f ", results[i][j]);
			}
			content += "\n";
		}
		
		File log = new File("foo.log");
		FileWriter fooWriter;
		try {
			fooWriter = new FileWriter(log, false);
			fooWriter.write(content);
			fooWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // true to append
		                                                     // false to overwrite.
		
		
	}
}
