package encog;

import normalizers.MinMaxNormalizer;
import normalizers.Normalizer;
import org.encog.neural.networks.training.propagation.Propagation;
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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
public class Main {

	public static final String NETWORK_FOLDER = "neural_networks/";
	public static final String LOGS_FOLDER = "logs/";

    public static double currentAccuracy;
    public static ArrfReader reader;

    public static MyNetwork run(ArrfReader reader, boolean normalizeDataSets, double maxError){
        Main.reader = reader;

        StringBuilder networkNameBuilder = new StringBuilder();

        if(normalizeDataSets)
        {
            networkNameBuilder.append("normalized_");
        }
        else
        {
            networkNameBuilder.append("non-normalized_");
        }

        if(reader.isTreatMVsWithMean()){
            networkNameBuilder.append("mean_");
        }
        else
        {
            networkNameBuilder.append("default-value");
        }

        networkNameBuilder.append(maxError);

        String networkName = networkNameBuilder.toString();

        ArrayList<double[][]> inputAndOutput = reader.getInputAndOutput();

        double input[][] = inputAndOutput.get(0);
        double output[][] = inputAndOutput.get(1);

        int numInputNeurons = input[0].length;
        int numOutputNeurons = output[0].length;

        System.out.println(numInputNeurons + " input neurons and " + numOutputNeurons + " output neurons.");

        // create training data
        MLDataSet trainingSet = new BasicMLDataSet(input, output);
        Normalizer normalizer = null;

        // Normalization
        if(normalizeDataSets) {
            normalizer = new MinMaxNormalizer(0, 1);
            normalizer.normalizeDataSet(trainingSet);
        }

        MyNetwork network;

        File networkFile = new File(NETWORK_FOLDER + networkName + ".eg");
        if(!networkFile.exists())
        {
            // create new network
            network = new MyNetwork(networkName, numInputNeurons, numOutputNeurons, new ActivationSigmoid());

            // adapt to set
            try {
                network.createLayersAuto(trainingSet);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not adapt training set to network.");
                return null;
            }

            network.train(trainingSet, maxError);

            logInfoToFile(LOGS_FOLDER + networkName + ".txt", network.getNetwork(), networkName);
        }
        else{
            network = new MyNetwork(networkName, (BasicNetwork) EncogDirectoryPersistence.loadObject(networkFile), numInputNeurons, numOutputNeurons, new ActivationSigmoid());
            currentAccuracy = 1 - network.getNetwork().calculateError(trainingSet);
        }

        network.setNormalizer(normalizer);

        EncogDirectoryPersistence.saveObject(networkFile, network.getNetwork());

        Encog.getInstance().shutdown();

        return network;
    }

    public void testHiddenLayerSize(ArrfReader reader, boolean normalizeDataSets, double maxError, int hiddenLayerSize){

    }


    public static String printInfo(BasicNetwork network, String networkName)
    {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();

        sb.append("---------------------------------------").append("\n");

        sb.append("Current time: ").append(sdf.format(new Date())).append("\n\n");
        sb.append("Name: ").append(networkName).append("\n");
        sb.append("Layers: ").append("\n");
        for(int i = 0; i < network.getLayerCount(); i++)
        {
            sb.append("\tLayer " + (i+1) + ": " + network.getLayerNeuronCount(i) + " neurons").append("\n");
        }

        sb.append(LearningProcess.trainingInfo()).append("\n");

        sb.append("---------------------------------------").append("\n");

        System.out.println(sb.toString());

        return sb.toString();
    }

    public static void logInfoToFile(String filepath, BasicNetwork network, String networkName){

        try {
            PrintWriter out = new PrintWriter(filepath);
            out.write(printInfo(network, networkName));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
