package neuralNetwork;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.Arrays;

import static neuralNetwork.Network.createDataSet;

public class Normalizer {

    public static double minMaxNormalization(double min, double max, double value, double rangeMin, double rangeMax)
    {
        return ((rangeMax - rangeMin) * ((value - min) / (max - min)) + rangeMin);
    }

    public static double getMin(double[] array)
    {
        double min = array[0];

        for(int i = 1; i < array.length; i++)
        {
            if(array[i] < min)
                min = array[i];
        }

        return min;
    }

    public static double getMax(double[] array)
    {
        double max = array[0];

        for(int i = 1; i < array.length; i++)
        {
            if(array[i] > max)
                max = array[i];
        }

        return max;
    }

    public static DataSet minMaxNormalization(DataSet set, double rangeMin, double rangeMax){

        double[] minimumValues = new double[set.getInputSize()];
        double[] maximumValues = new double[set.getInputSize()];

        Arrays.fill(minimumValues, Double.MAX_VALUE);
        Arrays.fill(maximumValues, Double.MIN_VALUE);

        // Gets the minimum and maximum values for each attribute
        for(int i = 0; i < set.size(); i++)
        {
            DataSetRow currentRow = set.getRowAt(i);
            double[] input = currentRow.getInput();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] < minimumValues[j])
                    minimumValues[j] = input[j];

                if(input[j] > maximumValues[j])
                    maximumValues[j] = input[j];
            }
        }

        for(int i = 0; i < set.size(); i++)
        {
            DataSetRow currentRow = set.getRowAt(i);
            double[] input = currentRow.getInput();

            for(int j = 0; j < input.length; j++)
            {
                input[j] = minMaxNormalization(minimumValues[j], maximumValues[j], input[j], rangeMin, rangeMax);
            }
        }

        return set;

    }



    // FOR TESTING ONLY
    public static void main(String[] args) {

        DataSet set = createDataSet("test", 64, 1);
        minMaxNormalization(set, -1, 1);

        //System.out.println(Arrays.toString(set.getRowAt(1).getInput()));
        System.out.println(set);
    }

}
