package encog;

import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;

import java.util.ArrayList;
import java.util.Arrays;


public class MyNetwork {

    private int inputSize, outputSize;
    private ActivationFunction activationFunction;
    private BasicNetwork network;

    public MyNetwork(String name, int inputSize, int outputSize, ActivationFunction activationFunction) {

        this.network = new BasicNetwork();

        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activationFunction = activationFunction;
    }

    public MyNetwork(String name, BasicNetwork network, int inputSize, int outputSize, ActivationFunction activationFunction) {

        this.network = network;

        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activationFunction = activationFunction;
    }


    public void adapt(MLDataSet trainingSet) throws Exception {

        if(outputSize != trainingSet.getIdealSize())
        {
            throw new Exception("Training set output size is " + trainingSet.getIdealSize()
                    + " and does not match network's " + outputSize + " output size.");
        }

        if(inputSize != trainingSet.getInputSize())
        {
            throw new Exception("Training set input size is " + trainingSet.getInputSize()
                    + " and does not match network's " + inputSize + " input size.");
        }

        this.network.addLayer(new BasicLayer(null,true, this.inputSize));
        this.network.addLayer(new BasicLayer(this.activationFunction,true,getHiddenLayerSize(trainingSet)));
        this.network.addLayer(new BasicLayer(this.activationFunction,false, this.outputSize));
        this.network.getStructure().finalizeStructure();
        this.network.reset();
    }

    private int getHiddenLayerSize(MLDataSet trainingSet)
    {
        FeedForwardPattern pattern = new FeedForwardPattern();
        pattern.setInputNeurons(trainingSet.getInputSize());
        pattern.setOutputNeurons(trainingSet.getIdealSize());
        pattern.setActivationFunction(this.activationFunction);

        PruneIncremental pruneIncremental = new PruneIncremental(trainingSet, pattern, 100, 1, 10, new ConsoleStatusReportable());

        // number of hidden neurons should be between the number of input and output neurons
        if(outputSize < inputSize)
            pruneIncremental.addHiddenLayer(this.outputSize, this.inputSize);
        else
            pruneIncremental.addHiddenLayer(this.inputSize, this.outputSize);   // smallest first

        pruneIncremental.process();
        return pruneIncremental.getHidden1Size();
    }

    public void adaptToMissingValues(ArrayList<Integer> missingValues) throws Exception {

        enableAllConnections();

        System.out.println(missingValues);

        if(missingValues.size() != this.inputSize)
        {
            throw new Exception("Could not adapt network to missing values. Missing values size is not consistent with input size.");
        }

        for(int i = 0; i < missingValues.size(); i++)
        {
            if(missingValues.get(i) == 1)   // missing value
            {
                int numOfHiddenNeurons = this.network.getLayerNeuronCount(1);

                for(int j = 0; j < numOfHiddenNeurons; j++) {
                    this.network.enableConnection(0, i, j, false);
                    System.out.println("Disable connections of neuron " + i);
                }
            }
        }

    }

    public void enableAllConnections()
    {
        for(int i = 0; i < this.network.getLayerCount(); i++)
        {
            for(int j = 0; j < this.network.getLayerNeuronCount(i); j++){
                if( i < this.network.getLayerCount() - 1)
                {
                    for(int k = 0; k < this.network.getLayerNeuronCount(i+1); k++)
                    {
                        this.network.enableConnection(i, j, k, true);
                    }

                }
            }
        }
    }

    public BasicNetwork getNetwork() {
        return network;
    }

    public void setNetwork(BasicNetwork network) {
        this.network = network;
    }
}