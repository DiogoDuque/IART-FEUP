package normalizers;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.Arrays;

import static neuralNetwork.ArrfReader.NULLVAL;
import static neuralNetwork.Network.createDataSet;

public class MinMaxNormalizer implements normalizers.Normalizer {

    private DataSet dataSet;
    private double rangeMin, rangeMax;
    private double[] minimumValues, maximumValues;

    public MinMaxNormalizer(DataSet dataSet, double rangeMin, double rangeMax)
    {
        this.dataSet = dataSet;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;

        getDataSetAttributesRangeValues();
    }

    private void getDataSetAttributesRangeValues(){

        this.minimumValues = new double[dataSet.getInputSize()];
        this.maximumValues = new double[dataSet.getInputSize()];

        Arrays.fill(minimumValues, Double.MAX_VALUE);
        Arrays.fill(maximumValues, Double.MIN_VALUE);

        for(int i = 0; i < this.dataSet.size(); i++)
        {
            DataSetRow currentRow = this.dataSet.getRowAt(i);
            double[] input = currentRow.getInput();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] < minimumValues[j] && input[j] != NULLVAL)
                    minimumValues[j] = input[j];

                if(input[j] > maximumValues[j] && input[j] != NULLVAL)
                    maximumValues[j] = input[j];
            }
        }
    }

    public double normalizeValue(double min, double max, double value)
    {
        return ((this.rangeMax - this.rangeMin) * ((value - min) / (max - min)) + this.rangeMin);
    }

    public static double getMinValue(double[] array)
    {
        double min = array[0];

        for(int i = 1; i < array.length; i++)
        {
            if(array[i] < min && array[i] != NULLVAL)
                min = array[i];
        }

        return min;
    }

    public static double getMaxValue(double[] array)
    {
        double max = array[0];

        for(int i = 1; i < array.length; i++)
        {
            if(array[i] > max && array[i] != NULLVAL)
                max = array[i];
        }

        return max;
    }

    // FOR TESTING ONLY
    public static void main(String[] args) {

        DataSet set = createDataSet("test", 64, 1);

        MinMaxNormalizer minMaxNormalizer = new MinMaxNormalizer(set, -1, 1);
        minMaxNormalizer.normalizeDataSet();

        //System.out.println(Arrays.toString(set.getRowAt(1).getInput()));
        System.out.println(set);
    }

    @Override
    public void normalizeDataSet() {

        for(int i = 0; i < this.dataSet.size(); i++)
        {
            DataSetRow currentRow = this.dataSet.getRowAt(i);
            double[] input = currentRow.getInput();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] != NULLVAL)
                    input[j] = normalizeValue(this.minimumValues[j], this.maximumValues[j], input[j]);
            }
        }

    }
}