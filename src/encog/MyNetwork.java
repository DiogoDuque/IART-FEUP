package encog;

import normalizers.Normalizer;
import org.encog.ConsoleStatusReportable;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;
import utils.Converter;

import java.util.ArrayList;
import java.util.Arrays;


public class MyNetwork {

    private int inputSize, outputSize;
    private ActivationFunction activationFunction;
    private BasicNetwork network;
    private boolean ready;
    private Normalizer normalizer;

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

    public void train(MLDataSet trainingSet, double maxError, int maxIterations){

        Propagation propagation = new ResilientPropagation(this.network, trainingSet);
        propagation.setThreadCount(4);

        LearningProcess.iterateWithRule(propagation, maxError, maxIterations);
        System.out.println("Time elapsed during training: " + Converter.nanosecondsToSeconds(LearningProcess.getElapsedTime()) + " seconds.");

    }

    public double ask (MLData input){

        return this.network.compute(input).getData(0);

    }
    public void test(MLDataSet dataSet){
        // test the neural network
		int rightCounter = 0;

		System.out.println("Neural Network Results:");
		for(MLDataPair pair: dataSet ) {
			final MLData outputData = this.network.compute(pair.getInput());
			//System.out.println(pair.getInput() + ", actual=" + outputData.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

			if(outputData.getData(0) - pair.getIdeal().getData(0) < 0.5)
				rightCounter++;
		}

		System.out.println("Got " + rightCounter + " of " + dataSet.size());
    }

    public void createLayersAuto(MLDataSet trainingSet) throws Exception {
        createLayers(trainingSet, getHiddenLayerSize(trainingSet));
    }

    private void createLayers(MLDataSet trainingSet, int hiddenLayerSize) throws Exception {
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
        this.network.addLayer(new BasicLayer(this.activationFunction,true,hiddenLayerSize));
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

    public BasicNetwork getNetwork() {
        return network;
    }

    public void setNetwork(BasicNetwork network) {
        this.network = network;
    }

    public boolean isReady() {
        return ready;
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
