package encog;

import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.encog.persist.EncogDirectoryPersistence;
import reader.ArrfReader;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import utils.Converter;

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

	    boolean normalizeDataSets = true;
		String networkName = "n3";

		ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
		ArrayList<double[][]> inputAndOutput = reader.getInputAndOutput();

		double input[][] = inputAndOutput.get(0);
		double output[][] = inputAndOutput.get(1);

		int numInputNeurons = input[0].length;
		int numOutputNeurons = output[0].length;

		System.out.println(numInputNeurons + " input neurons and " + numOutputNeurons + " output neurons.");

		// create training data
		MLDataSet adapterSet = new BasicMLDataSet(input, output);
		MyTrainingSet trainingSet = new MyTrainingSet(input, output);

		// Normalization
		if(normalizeDataSets) {
			Normalizer normalizer = new MinMaxNormalizer(0, 1);
			normalizer.normalizeDataSet(adapterSet);

			trainingSet.normalize(normalizer);
		}

		MyNetwork network;

		File networkFile = new File(NETWORK_FOLDER + networkName + ".eg");
		if(!networkFile.exists())
		{
		    // create new network
            network = new MyNetwork(networkName, numInputNeurons, numOutputNeurons, new ActivationSigmoid());

            // adapt to set
            try {
                network.adapt(adapterSet);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not adapt training set to network.");
                return;
            }
        }
		else{
            network = new MyNetwork(networkName, (BasicNetwork) EncogDirectoryPersistence.loadObject(networkFile), numInputNeurons, numOutputNeurons, new ActivationSigmoid());
        }

		try {
			LearningProcess.train(network, trainingSet, 0.3);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println("Time elapsed during training: " + Converter.nanosecondsToSeconds(LearningProcess.getElapsedTime()) + " seconds.");

        // test the neural network
		int rightCounter = 0;

		System.out.println("Neural Network Results:");
		for(MLDataPair pair: adapterSet ) {
			final MLData outputData = network.getNetwork().compute(pair.getInput());
			//System.out.println(pair.getInput() + ", actual=" + outputData.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

			if(outputData.getData(0) - pair.getIdeal().getData(0) < 0.5)
				rightCounter++;
		}

		postNetworkLayersInfo(network.getNetwork(), networkName);
		System.out.println("Got " + rightCounter + " of " + adapterSet.size());

		EncogDirectoryPersistence.saveObject(networkFile, network.getNetwork());

		Encog.getInstance().shutdown();
	}



    public static void postNetworkLayersInfo(BasicNetwork network, String networkName)
    {
        System.out.println("Name: " + networkName);
        System.out.println("Layers: ");
        for(int i = 0; i < network.getLayerCount(); i++)
        {
            System.out.println("\tLayer " + (i+1) + ": " + network.getLayerNeuronCount(i) + " neurons");
        }
    }
}
