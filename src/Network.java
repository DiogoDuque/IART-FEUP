import org.neuroph.core.Connection;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

import java.util.*;

/**
 * Created by Diogo on 22-03-2017.
 */
public class Network {
    public static void main(String[] args){

        NeuralNetwork neuralNetwork = createNet();
        neuralNetwork.save("bankruptcy_net.nnet"); //saves the last network version to file that can be opened with NeurophStudio

        ArrfReader reader1year = new ArrfReader("./dataset/1year.arff");

        //EXAMPLE OF HOW TO QUICK-TEST THE NETWORK using the first company of the first year
        //set network input
        ArrayList<Double> inputData = reader1year.getCompanyData(7000);

        double[] inputs = new double[inputData.size()];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputData.get(i);                // java 1.5+ style (outboxing)
        }

        neuralNetwork.setInput(inputs);
        //calculate network
        neuralNetwork.calculate();
        //get network output
        double[] networkOutput = neuralNetwork.getOutput();
        System.out.println("NeuralNet Result: "+networkOutput[0]);

    }

    public static NeuralNetwork createNet() {
        // Load Dataset
        ArrfReader reader1year = new ArrfReader("./dataset/1year.arff");
        ArrfReader reader2year = new ArrfReader("./dataset/2year.arff");
        ArrfReader reader3year = new ArrfReader("./dataset/3year.arff");
        ArrfReader reader4year = new ArrfReader("./dataset/4year.arff");
        ArrfReader reader5year = new ArrfReader("./dataset/5year.arff");

        ArrayList<ArrfReader> readerArray = new ArrayList<>();
        readerArray.add(reader1year);
        readerArray.add(reader2year);
        readerArray.add(reader3year);
        readerArray.add(reader4year);
        readerArray.add(reader5year);

        //create new network
        ArrayList<Integer> layers = new ArrayList<>();
        layers.add(64);
        layers.add(43);
        layers.add(1);
        NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(layers);
        neuralNetwork.getLearningRule().setMaxIterations(100);

        //add training data of all years to training set
        for(ArrfReader reader : readerArray) {
            int datasetSize = reader.getFullDataSet().size();
            for (int i = 0; i < datasetSize; i++) {
                System.out.println("Progress: "+i+" of "+datasetSize);
                //check if Bankruptcy value is not missing
                ArrayList<Double> bankruptcy = reader.getBankruptcyOfCompany(i);
                if(bankruptcy.get(0)==ArrfReader.NULLVAL)
                    continue;
                //backup and remove necessary connections. NN -> NSIM
                ArrayList<Double> inputs = reader.getCompanyData(i);
                LinkedHashMap<Integer, List<Connection>> backupConnections = adaptToNSIM(neuralNetwork,inputs);
                //learn the training set
                DataSet trainingSet = new DataSet(64, 1);
                trainingSet.addRow(new DataSetRow(inputs, bankruptcy));
                neuralNetwork.learn(trainingSet);
                //reset necessary connections. NSIM -> NN
                if(backupConnections != null)
                    adaptToNN(neuralNetwork,backupConnections);
            }
        }
        return neuralNetwork;
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

    private static LinkedHashMap<Integer, List<Connection>> adaptToNSIM(NeuralNetwork<BackPropagation> neuralNetwork, ArrayList<Double> inputs) {
        //find MVs positions
        ArrayList<Integer> missingValues = new ArrayList<Integer>();
        for(int i=0; i<inputs.size(); i++){
            if(inputs.get(i)==ArrfReader.NULLVAL)
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
