package neuralNetwork;
import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;
import java.util.Scanner;

public class Network {
	NeuralNetwork<BackPropagation> network;
	MomentumBackpropagation rule;
	boolean ready = false;
	public static Normalizer normalizer;
	
	
	public Network() {
		super();
		this.network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 64, 43, 1);
		this.rule = new MomentumBackpropagation();
		
		network.setLearningRule(rule);
		
		rule.setMomentum(0.8);//*/
		rule.setMaxError(0.0001);
		rule.setMaxIterations(1000);//This thing here :(
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
		NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 64, 43, 1);
		MomentumBackpropagation bp = new MomentumBackpropagation();
		bp.setMomentum(0.8);//*/
		bp.setMaxError(0.0001);
		bp.setMaxIterations(1000);//This thing here :(
		bp.setLearningRate(0.3);
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
		trainer.trainUntilAccurate(neuralNetwork, set, 0.8);
	}


    private static void normalize(DataSet set) {

        normalizer = new MinMaxNormalizer(set, -1, 1);
        normalizer.normalizeDataSet();
		
	}


	public static DataSet createDataSet(String filename, int inputs_number, int outputs_number) {
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
