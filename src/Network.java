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

        ArrfReader reader1year = new ArrfReader("./dataset/1year.arff");
        ArrfReader reader2year = new ArrfReader("./dataset/2year.arff");
        ArrfReader reader3year = new ArrfReader("./dataset/3year.arff");
        ArrfReader reader4year = new ArrfReader("./dataset/4year.arff");
        ArrfReader reader5year = new ArrfReader("./dataset/5year.arff");

        NeuralNetwork neuralNetwork = createNet();
        neuralNetwork.save("bankruptcy_net.nnet"); //saves the last network version to file that can be opened with NeurophStudio

        //EXAMPLE OF HOW TO QUICK-TEST THE NETWORK (RANDOM)
        //set network input
        double[] inputs = new double[64];
        Random r = new Random();
        for(int i=0; i<64; i++)
            inputs[i]= r.nextDouble();
        neuralNetwork.setInput(inputs);
        //calculate network
        neuralNetwork.calculate();
        //get network output
        double[] networkOutput = neuralNetwork.getOutput();
        System.out.println("NeuralNet Result: "+networkOutput[0]);

    }

    public static NeuralNetwork createNet() {
        //create new network
        ArrayList<Integer> layers = new ArrayList<>();
        layers.add(64);
        layers.add(10);
        layers.add(1);
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(layers);

        //create training set
        DataSet trainingSet = new DataSet(64, 1);
        Random r = new Random();
        //add training data to training set RANDOM
        for(int i=0; i<10; i++){
            double[] inputs = new double[64];
            for(int j=0; j<64; j++)
                inputs[j]=r.nextDouble();
            trainingSet.addRow(new DataSetRow(inputs, new double[]{r.nextInt(2)}));
        }

        //learn the training set
        //neuralNetwork.learn(trainingSet);

        return neuralNetwork;
    }
}
