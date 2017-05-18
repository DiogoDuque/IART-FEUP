package encog;

import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.encog.engine.network.activation.ActivationTANH;
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

	/**
	 * The main method.
	 * @param args No arguments are used.
	 */
	public static void main(final String args[]) {

		ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
		ArrayList<ArrayList<Double>> data = reader.getFullDataSet();

		double input[][] = new double[data.size()][data.get(0).size() - 1];
		double output[][] = new double[data.size()][1];

		for(int i = 0; i < data.size(); i++){

			ArrayList<Double> currentCompany = data.get(i);

			for(int j = 0; j < data.get(0).size(); j++)
			{
				if(j == data.get(0).size()-1)
				{
					output[i][0] = currentCompany.get(j);
				}
				else
				{
					input[i][j] = currentCompany.get(j);
				}
			}

		}
 
		// create a neural network, without using a factory
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,64));
		network.addLayer(new BasicLayer(new ActivationTANH(),true,43));
		network.addLayer(new BasicLayer(new ActivationTANH(),false,1));
		network.getStructure().finalizeStructure();
		network.reset();
 
		// create training data
		MLDataSet trainingSet = new BasicMLDataSet(input, output);

		// Normalization
		Normalizer normalizer = new MinMaxNormalizer(0, 1);
		normalizer.normalizeDataSet(trainingSet);
 
		// train the neural network
		final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
 
		int epoch = 1;
 
		do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while(train.getError() > 0.10);
		train.finishTraining();
 
		// test the neural network
		int rightCounter = 0;

		System.out.println("Neural Network Results:");
		for(MLDataPair pair: trainingSet ) {
			final MLData outputData = network.compute(pair.getInput());
			System.out.println(pair.getInput() + ", actual=" + outputData.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

			if(outputData.getData(0) - pair.getIdeal().getData(0) < 0.5)
				rightCounter++;
		}

		System.out.println("Got " + rightCounter + " of " + trainingSet.size());

		Encog.getInstance().shutdown();
	}
}
