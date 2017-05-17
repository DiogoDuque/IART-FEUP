package neuralNetwork;
import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.Normalizer;

import weka.filters.unsupervised.attribute.Normalize;

import java.util.*;

/**
 * Created by Diogo on 22-03-2017.
 */
public class Network {
	NeuralNetwork<BackPropagation> network;
	MomentumBackpropagation rule;
	boolean ready = false;
	
	
	
	public Network() {
		super();
		this.network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 64, 43, 1);
		this.rule = new MomentumBackpropagation();
		network.setLearningRule(rule);
		
		rule.setMomentum(0.8);//*/
		rule.setMaxError(0.01);
		rule.setMaxIterations(100);//This thing here :(
		rule.setLearningRate(0.3);
	}
	

	public NeuralNetwork<BackPropagation> getNetwork() {
		return network;
	}

	public MomentumBackpropagation getRule() {
		return rule;
	}


	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}



	public static void main(String[] args){
		NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 63, 43, 1);
		ResilientPropagation bp = new ResilientPropagation();
		//bp.setMomentum(0.8);//*/
		bp.setMaxError(0.00000000000001);
		bp.setMaxIterations(1);//This thing here :(
		bp.setLearningRate(0.2);
		neuralNetwork.setLearningRule(bp);
		
		trainUntilAccurate(neuralNetwork, "test2");

		
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
 
	
	public static double ask(NeuralNetwork<BackPropagation> neuralNetwork, double[] input) {
    	neuralNetwork.setInput(input);
		neuralNetwork.calculate();
		double[] networkOutput = neuralNetwork.getOutput();
		
		System.out.println("Result: " + networkOutput[0]);
		return networkOutput[0];
		
	}


	public static double train(NeuralNetwork<BackPropagation> neuralNetwork, String filename) {
    	DataSet set = createDataSet(filename, neuralNetwork.getInputsCount(), neuralNetwork.getOutputsCount());
    	normalize(set);
    	
		LearningThread trainer = new LearningThread();
		return trainer.train(neuralNetwork, set);
	}
	
	public static void trainUntilAccurate(NeuralNetwork<BackPropagation> neuralNetwork, String filename) {
    	DataSet set = createDataSet(filename, neuralNetwork.getInputsCount(), neuralNetwork.getOutputsCount());
    	normalize(set);
    	
		LearningThread trainer = new LearningThread();
		trainer.trainUntilAccurate(neuralNetwork, set, 0.99);
	}


    private static void normalize(DataSet set) {
		Normalizer mmn= new MaxMinNormalizerMV();
		mmn.normalize(set);
		
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
