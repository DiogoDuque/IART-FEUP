import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.util.ArrayList;
import java.util.Random;

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
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(layers);

        //create training set
        DataSet trainingSet = new DataSet(64, 1);

        //add training data of all years to training set
        for(ArrfReader reader : readerArray) {
            for (int i = 0; i < reader.getFullDataSet().size(); i++) {
                trainingSet.addRow(new DataSetRow(reader.getCompanyData(i), reader.getBankruptcyOfCompany(i)));
            }
        }

        //learn the training set
        neuralNetwork.learn(trainingSet);

        return neuralNetwork;
    }
}
