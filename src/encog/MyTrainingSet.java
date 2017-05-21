package encog;

import normalizers.Normalizer;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import reader.ArrfReader;
import utils.NN;

import java.util.*;

public class MyTrainingSet {

    private Map<String, MLDataSet> trainingSets;
    private double[][] input, output;

    public MyTrainingSet(double[][] input, double[][] output)
    {
        this.input = input;
        this.output = output;

        this.trainingSets = new HashMap<>();
    }

    public void normalize(Normalizer normalizer){

        for(Map.Entry<String, MLDataSet> entry : this.trainingSets.entrySet()){
            normalizer.normalizeDataSet(entry.getValue());
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, MLDataSet> entry : this.trainingSets.entrySet()){

            sb.append("Missing values pattern: ").append(entry.getKey()).append("\n");

            for(MLDataPair pair : entry.getValue())
            {
                sb.append("\tPair: ").append(pair).append("\n");
            }
        }

        return sb.toString();
    }

    public double[][] getInput() {
        return input;
    }

    public double[][] getOutput() {
        return output;
    }

    public Map<String, MLDataSet> getTrainingSets() {
        return trainingSets;
    }

    // FOR TESTING
    public static void main(String[] args) {

        ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
        ArrayList<double[][]> inputAndOutput = reader.getInputAndOutput();

        MyTrainingSet myTrainingSet = new MyTrainingSet(inputAndOutput.get(0), inputAndOutput.get(1));
        System.out.println(myTrainingSet);

    }
}
