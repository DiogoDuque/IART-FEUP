package encog;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import reader.ArrfReader;
import utils.NN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyTrainingSet {

    private Map<String, MLDataSet> trainingSets;
    private double[][] input, output;

    public MyTrainingSet(double[][] input, double[][] output)
    {
        this.input = input;
        this.output = output;

        this.trainingSets = new HashMap<>();

        divideInputByMissingValues();
    }

    private void divideInputByMissingValues(){

        for(int i = 0; i < this.input.length; i++)
        {
            byte[] missingValuesArray = NN.getMissingValues(this.input[i]);
            String missingValues = Arrays.toString(missingValuesArray);

            MLData input = new BasicMLData(this.input[i]);
            MLData output = new BasicMLData(this.output[i]);

            MLDataPair mlDataPair = new BasicMLDataPair(input, output);

            MLDataSet set;

            // if there isn't a training set for this pattern of MVs, create a new one
            if(!this.trainingSets.containsKey(missingValues))
            {
                set = new BasicMLDataSet();
                set.add(mlDataPair);

                this.trainingSets.put(missingValues, set);
            }
            else
            {
                set = this.trainingSets.get(missingValues);
                set.add(mlDataPair);
            }
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

    // FOR TESTING
    public static void main(String[] args) {

        ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
        ArrayList<double[][]> inputAndOutput = reader.getInputAndOutput();

        MyTrainingSet myTrainingSet = new MyTrainingSet(inputAndOutput.get(0), inputAndOutput.get(1));
        System.out.println(myTrainingSet);


    }
}
