import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import weka.filters.unsupervised.attribute.Normalize;

import java.util.*;

/**
 * Created by Diogo on 22-03-2017.
 */
public class Network {
	public static void main(String[] args){
		NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 64, 43, 1);
		MomentumBackpropagation bp = new MomentumBackpropagation();
		bp.setMomentum(0.8);//*/
		bp.setMaxError(0.01);
		bp.setMaxIterations(1000);//This thing here :(
		bp.setLearningRate(0.1);
		neuralNetwork.setLearningRule(bp);
		
		train(neuralNetwork, "test");

		
		//Check
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println("Entry? ");
			int i = in.nextInt();
			double[] input = createDataSet("1year", neuralNetwork.getInputsCount(), neuralNetwork.getOutputsCount()).get(i).getInput();
			System.out.println("Result: ");
			ask(neuralNetwork, input);
		}
		
		
	}
 
	
    private static void ask(NeuralNetwork<BackPropagation> neuralNetwork, double[] input) {
    	neuralNetwork.setInput(input);
		neuralNetwork.calculate();
		double[] networkOutput = neuralNetwork.getOutput();
		
		System.out.println("Result: " + networkOutput[0]);
		
	}


	private static void train(NeuralNetwork<BackPropagation> neuralNetwork, String filename) {
    	DataSet set = createDataSet(filename, neuralNetwork.getInputsCount(), neuralNetwork.getOutputsCount());
    	normalize(set);
    	
		LearningThread trainer = new LearningThread();
		trainer.trainUntilAccurate(neuralNetwork, set, 0.9);

		
    	
	}


    private static void normalize(DataSet set) {
		// TODO Auto-generated method stub
		
	}


	private static DataSet createDataSet(String filename, int inputs_number, int outputs_number) {
    	ArrfReader reader = new ArrfReader("./dataset/"+ filename + ".arff");
    	ArrayList<ArrayList<Double>> setData = reader.getFullDataSet();
    	DataSet set = new DataSet(inputs_number, outputs_number);
    	
    	for(ArrayList<Double> rowData : setData){
    		ArrayList<Double> input = new ArrayList<Double> (rowData.subList(0, inputs_number));
    		ArrayList<Double> output = new ArrayList<Double> (rowData.subList(
    				inputs_number,
    				inputs_number + outputs_number));
    
    		DataSetRow row = new DataSetRow(input, output);
			set.addRow(row);
		}
		return set;
	}
}
