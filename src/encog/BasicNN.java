package encog;

import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.layers.Layer;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.util.simple.EncogUtility;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import reader.ArrfReader;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import java.io.File;
import java.util.ArrayList;

/**
 * XOR: This example is essentially the "Hello World" of neural network
 * programming.  This example shows how to construct an Encog neural
 * network to predict the output from the XOR operator.  This example
 * uses backpropagation to train the neural network.
 * 
 * This example attempts to use a minimum of Encog features to create and
 * train the neural network.  This allows you to see exactly what is going
 * on.  For a more advanced example, that uses Encog factories, refer to
 * the XORFactory example.
 * 
 */
public class BasicNN {

	public static final String NETWORK_FOLDER = "neural_networks/";

	/**
	 * The main method.
	 * @param args No arguments are used.
	 */
	public static void main(final String args[]) {

	    boolean normalizeDataSet = true;

		ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
		ArrayList<ArrayList<Double>> data = reader.getFullDataSet();

		int numInputNeurons = data.get(0).size() - 1;
        int numOutputNeurons = 1;
        int totalNumOfNeurons = numInputNeurons + numOutputNeurons;

        System.out.println(numInputNeurons + " input neurons and " + numOutputNeurons + " output neurons.");

		double input[][] = new double[data.size()][numInputNeurons];
		double output[][] = new double[data.size()][numOutputNeurons];

		for(int i = 0; i < data.size(); i++){

			ArrayList<Double> currentCompany = data.get(i);

			for(int j = 0; j < totalNumOfNeurons; j++)
			{
				if(j == numInputNeurons)
				{
					output[i][0] = currentCompany.get(j);
				}
				else
				{
					input[i][j] = currentCompany.get(j);
				}
			}

		}

        // create training data
        MLDataSet trainingSet = new BasicMLDataSet(input, output);

        // Normalization
        if(normalizeDataSet) {
            Normalizer normalizer = new MinMaxNormalizer(0, 1);
            normalizer.normalizeDataSet(trainingSet);
        }


        String networkName = "n1";
 
		MyNetwork network;

		File networkFile = new File(NETWORK_FOLDER + networkName + ".eg");
		if(!networkFile.exists())
		{
		    // create new network
            network = new MyNetwork(networkName, numInputNeurons, numOutputNeurons, new ActivationSigmoid());

            // adapt to training set
            try {
                network.adapt(trainingSet);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not adapt training set to network.");
                return;
            }
        }
		else{
            network = new MyNetwork(networkName, (BasicNetwork) EncogDirectoryPersistence.loadObject(networkFile), numInputNeurons, numOutputNeurons, new ActivationSigmoid());
        }



		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network.getNetwork(), trainingSet);
		train.setThreadCount(4);


		int epoch = 1;
 
		do {
			train.iteration();

			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.15);

		train.finishTraining();
 
		// test the neural network
		int rightCounter = 0;

		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData outputData = network.getNetwork().compute(pair.getInput());
			//System.out.println(pair.getInput() + ", actual=" + outputData.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

			if(outputData.getData(0) - pair.getIdeal().getData(0) < 0.5)
				rightCounter++;
		}

		postNetworkLayersInfo(network.getNetwork(), networkName);
		System.out.println("Got " + rightCounter + " of " + trainingSet.size());
        System.out.println("This NN error rate is: " + train.getError());

		EncogDirectoryPersistence.saveObject(networkFile, network.getNetwork());

		Encog.getInstance().shutdown();
	}



    public static void postNetworkLayersInfo(BasicNetwork network, String networkName)
    {
        System.out.println("Name: " + networkName);
        System.out.println("Layers: ");
        for(Layer l : network.getStructure().getLayers())
        {
            System.out.println("\t" + l.getNeuronCount() + " neurons");
        }
    }
}
