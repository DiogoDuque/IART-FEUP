package neuroph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import reader.ArrfReader;

public class LearningThread{
	private NeuralNetwork<BackPropagation> neuralNetwork;

	private Map<String, DataSet> trainingSets;
	private Map<String, DataSet> testingSets;

	private DataSet set;

	private double accuracy = 0;

	private double lastTime;

	private int testingSetSize = 10;



	public LearningThread() {
		super();
	}


	
	public double train(NeuralNetwork<BackPropagation> neuralNetwork, DataSet set) {
		this.neuralNetwork = neuralNetwork;
		this.set = set;
		
		//Save Time
		lastTime = System.currentTimeMillis();

		//Learn and test
		prepare();
		learn();
		accuracy = test();

		//Get elapsed time
		double delta = System.currentTimeMillis() - lastTime;
		printStatus(accuracy, delta);
		return accuracy;
	}
	
	
	public void trainUntilAccurate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet set, double desired_accuracy) {
		this.neuralNetwork = neuralNetwork;
		this.set = set;
		
		
		while(accuracy < desired_accuracy){
			//Save Time
			lastTime = System.currentTimeMillis();

			//Learn and test
			prepare();
			learn();
			accuracy = test();

			//Randomize if horrible accuracy
			if(accuracy < 0.5)
				neuralNetwork.randomizeWeights(new Random());

			//Get elapsed time
			double delta = System.currentTimeMillis() - lastTime;
			printStatus(accuracy, delta);

		}
	}

	private void prepare(){
		set.shuffle();
		DataSet[] sets = set.createTrainingAndTestSubsets(100 - testingSetSize, testingSetSize);
		//this.trainingSet = sets;

		this.trainingSets = new HashMap<String, DataSet>();
		this.testingSets = new HashMap<String, DataSet>();

		int inputsSize = neuralNetwork.getInputsCount();
		//trainingSet.getInputSize();
		int outputsSize = neuralNetwork.getOutputsCount();
		//trainingSet.getOutputSize();


		//Separate Training Set into various Sets, depending on missing values
		for(DataSetRow row : sets[0].getRows()){
			if(row.getDesiredOutput()[0] ==  ArrfReader.NULLVAL)
				continue;

			byte[] missingValues = getMissingValues(row.getInput());
			String mvCode = Arrays.toString(missingValues);

			if(!trainingSets.containsKey(mvCode))
				trainingSets.put(mvCode, new DataSet(inputsSize, outputsSize));
			trainingSets.get(mvCode).add(row);
		}

		//Separate Training Set into various Sets, depending on missing values
		for(DataSetRow row : sets[1].getRows()){
			if(row.getDesiredOutput()[0] ==  ArrfReader.NULLVAL)
				continue;

			byte[] missingValues = getMissingValues(row.getInput());
			String mvCode = Arrays.toString(missingValues);

			if(!testingSets.containsKey(mvCode))
				testingSets.put(mvCode, new DataSet(inputsSize, outputsSize));
			testingSets.get(mvCode).add(row);
		}
	}




	private void learn(){
		for(String key : trainingSets.keySet()){	
			DataSet set = trainingSets.get(key);
			System.out.println("Set: " + key);

			//backup and remove necessary connections. NN -> NSIM
			LinkedHashMap<Integer, List<Connection>> backupConnections = adaptToNSIM(neuralNetwork, key);

			//learn the training set
			neuralNetwork.learn(set);

			//reset necessary connections. NSIM -> NN
			if(backupConnections != null)
				adaptToNN(neuralNetwork, backupConnections);

		}	
	}


	private double test() {
		double test_size = 0;
		double correctCounter = 0;

		for(String key : testingSets.keySet()){	
			DataSet set = testingSets.get(key);

			//backup and remove necessary connections. NN -> NSIM
			LinkedHashMap<Integer, List<Connection>> backupConnections = adaptToNSIM(neuralNetwork, key);

			//test the training set
			for(DataSetRow row : set.getRows()){
				neuralNetwork.setInput(row.getInput());
				neuralNetwork.calculate();
				double[] networkOutput = neuralNetwork.getOutput();
				
				test_size += 1;
				if(Math.abs(networkOutput[0] - row.getDesiredOutput()[0]) < 0.01){
					correctCounter += 1;
				}
			}

			//reset necessary connections. NSIM -> NN
			if(backupConnections != null)
				adaptToNN(neuralNetwork, backupConnections);

		}	
		return correctCounter / test_size;

	}

	private void printStatus(double accuracy2, double delta) {
		System.out.println("-----------------------------------------------");
		System.out.println("\nAccuracy: " + accuracy);
		System.out.println("Time taken: " + delta);
		System.out.println("-----------------------------------------------");

	}


	private byte[] getMissingValues(double[] input) {
		byte[] res = new byte[input.length];
		for(int i = 0; i < input.length; i++)
			if(input[i] == ArrfReader.NULLVAL)
				res[i] = 1;
		return res;
	}



	private static void adaptToNN(NeuralNetwork<BackPropagation> neuralNetwork, LinkedHashMap<Integer, List<Connection>> backupConnections) {
		ArrayList<Neuron> neurons = new ArrayList<Neuron>(neuralNetwork.getInputNeurons());
		Iterator<Integer> it = backupConnections.keySet().iterator();
		do {
			int n = it.next();
			Neuron neuron = neurons.get(n);
			ArrayList<Connection> connections = new ArrayList<Connection>(backupConnections.get(n));
			for(int i=0; i<connections.size(); i++){
				Connection connection = connections.get(i);
				neuralNetwork.createConnection(neuron, connection.getToNeuron(), connection.getWeight().getValue());
			}
		} while(it.hasNext());
	}



	private static LinkedHashMap<Integer, List<Connection>> adaptToNSIM(NeuralNetwork<BackPropagation> neuralNetwork, String key) {
		String trimmedKey = key.substring(1, key.length()-1);
		String[] tokens = trimmedKey.split(", ");

		//find MVs positions
		ArrayList<Integer> missingValues = new ArrayList<Integer>();
		for(int i = 0; i < tokens.length; i++){
			if(Integer.parseInt(tokens[i]) == 1)
				missingValues.add(i);
		}
		if(missingValues.size()==0)
			return null;

		//adapt network
		LinkedHashMap<Integer, List<Connection>> connectionsBackup = new LinkedHashMap<Integer, List<Connection>>();
		ArrayList<Neuron> neurons = new ArrayList<Neuron>(neuralNetwork.getInputNeurons());
		for(int i=0; i<missingValues.size(); i++) {
			Neuron neuron = neurons.get(i);
			connectionsBackup.put(i,neuron.getOutConnections());
			neuron.removeAllOutputConnections();
		}
		return connectionsBackup;
	}







}
