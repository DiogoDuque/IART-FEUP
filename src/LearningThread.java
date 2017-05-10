import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;

public class LearningThread  extends Thread{
	private NeuralNetwork<BackPropagation> neuralNetwork;
	private DataSet trainingSet;
	private Map<String, DataSet> trainingSets;
	private DataSet testingSet;
	private double desired_accuracy;
	private double lastTime;
	
	

	public LearningThread(NeuralNetwork<BackPropagation> neuralNetwork, DataSet trainingSet, DataSet testingSet,
			double desired_accuracy) {
		super();
		this.neuralNetwork = neuralNetwork;
		this.trainingSet = trainingSet;
		this.testingSet = testingSet;
		this.desired_accuracy = desired_accuracy;
		prepare();
	}



	@Override
	public void run() {
		if (lastTime == 0){
			lastTime = System.currentTimeMillis();
		}
		double accuracy = 0;

		while(accuracy < desired_accuracy){
			learn();
			
			accuracy = test();
			System.out.println("Accuracy: " + accuracy);
			
			double currentTime = System.currentTimeMillis();
			System.out.println("Time taken: " + (currentTime - lastTime));
			lastTime = currentTime;
		}
	}

	private void learn(){
		for(String key : trainingSets.keySet()){
			//Uncomment for only full values;
			if(!key.equals("[0, 0, 0]"))
				continue;
			
			DataSet set = trainingSets.get(key);
			System.out.println("\nSet: " + key);
			//neuralNetwork.learn(set);

			//backup and remove necessary connections. NN -> NSIM
			System.out.println("Taking out connections");
            LinkedHashMap<Integer, List<Connection>> backupConnections = adaptToNSIM(neuralNetwork, key);
            
            //learn the training set
            System.out.println("Learning");
        	neuralNetwork.learn(set);
            
        	
        	//reset necessary connections. NSIM -> NN
        	System.out.println("Restoring connections");
            if(backupConnections != null)
                adaptToNN(neuralNetwork, backupConnections);
            
            
		}
		System.out.println("--------------------------\n--------------------");
		
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
                connection.getToNeuron().addInputConnection(neuron,connection.getWeightedInput());
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
	
	private void prepare(){
		this.trainingSets = new HashMap<String, DataSet>();
		
		int inputsSize = trainingSet.getInputSize();
		int outputsSize = trainingSet.getOutputSize();
		
		
		for(DataSetRow row : trainingSet.getRows()){
			if(row.getDesiredOutput()[0] ==  ArrfReader.NULLVAL)
				continue;
			
			byte[] missingValues = getMissingValues(row.getInput());
			String mvCode = Arrays.toString(missingValues);
			
			if(!trainingSets.containsKey(mvCode))
				trainingSets.put(mvCode, new DataSet(inputsSize, outputsSize));
			trainingSets.get(mvCode).add(row);
		}
		
		for(DataSet set : trainingSets.values())
			set.shuffle();
	}

	private byte[] getMissingValues(double[] input) {
		byte[] res = new byte[input.length];
		for(int i = 0; i < input.length; i++)
			if(input[i] == ArrfReader.NULLVAL)
				res[i] = 1;
		return res;
	}



	private double test() {
		double correctCounter = 0;
		
		double test_size = testingSet.getRows().size();
		
		for(DataSetRow row : testingSet.getRows()){
			neuralNetwork.setInput(row.getInput());
	    	neuralNetwork.calculate();
	    	double[] networkOutput = neuralNetwork.getOutput();
	    	if(Math.abs(networkOutput[0] - row.getDesiredOutput()[0]) < 0.1){
	    		correctCounter += 1;
	    	}
		}
		
		return correctCounter / test_size;
		
	}
	
	
	
	
	
}
