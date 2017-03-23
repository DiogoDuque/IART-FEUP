import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

/**
 * Created by Diogo on 22-03-2017.
 */
public class Network {
    public static void main(String[] args){
        NeuralNetwork neuralNetwork = createNet();

        //set network input
        neuralNetwork.setInput(0, 1);
        //calculate network
        neuralNetwork.calculate();
        //get network output
        double[] networkOutput = neuralNetwork.getOutput();
        System.out.println(networkOutput[0]);

    }

    public static NeuralNetwork createNet(){
        //create new perceptron network
        NeuralNetwork neuralNetwork = new Perceptron(64, 1);

        //create training set
        DataSet trainingSet =
                new DataSet(64, 1);
        //add training data to training set (inputs,outputs)
        /*trainingSet. addRow (new DataSetRow(new double[]{0, 0},
                new double[]{0}));
        trainingSet. addRow (new DataSetRow (new double[]{0, 1},
                new double[]{1}));
        trainingSet. addRow (new DataSetRow (new double[]{1, 0},
                new double[]{1}));
        trainingSet. addRow (new DataSetRow (new double[]{1, 1},
                new double[]{1}));*/

        //learn the training set
        neuralNetwork.learn(trainingSet);

        return neuralNetwork;
    }
}
