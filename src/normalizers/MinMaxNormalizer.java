package normalizers;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.Arrays;

import static reader.ArrfReader.NULLVAL;
import static neuroph.Network.createDataSet;

public class MinMaxNormalizer implements normalizers.Normalizer {

    private double rangeMin, rangeMax;
    private double[] minimumValues, maximumValues;

    public MinMaxNormalizer(double rangeMin, double rangeMax)
    {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    private void setDataSetAttributesRangeValues(DataSet dataSet){

        this.minimumValues = new double[dataSet.getInputSize()];
        this.maximumValues = new double[dataSet.getInputSize()];

        Arrays.fill(minimumValues, Double.MAX_VALUE);
        Arrays.fill(maximumValues, Double.MIN_VALUE);

        for(int i = 0; i < dataSet.size(); i++)
        {
            DataSetRow currentRow = dataSet.getRowAt(i);
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

    private void setDataSetAttributesRangeValues(MLDataSet dataSet){

        this.minimumValues = new double[dataSet.getInputSize()];
        this.maximumValues = new double[dataSet.getInputSize()];

        Arrays.fill(minimumValues, Double.MAX_VALUE);
        Arrays.fill(maximumValues, Double.MIN_VALUE);

        for(int i = 0; i < dataSet.size(); i++)
        {
            MLDataPair currentPair = dataSet.get(i);
            double[] input = currentPair.getInputArray();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] < minimumValues[j] && input[j] != NULLVAL)
                    minimumValues[j] = input[j];

                if(input[j] > maximumValues[j] && input[j] != NULLVAL)
                    maximumValues[j] = input[j];
            }
        }
    }

    private double normalize(double min, double max, double value)
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

        MinMaxNormalizer minMaxNormalizer = new MinMaxNormalizer(-1, 1);
        minMaxNormalizer.normalizeDataSet(set);

        //System.out.println(Arrays.toString(set.getRowAt(1).getInput()));
        System.out.println(set);
    }

    @Override
    public void normalizeDataSet(DataSet dataSet) {

        setDataSetAttributesRangeValues(dataSet);

        for(int i = 0; i < dataSet.size(); i++)
        {
            DataSetRow currentRow = dataSet.getRowAt(i);
            double[] input = currentRow.getInput();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] != NULLVAL)
                    input[j] = normalize(this.minimumValues[j], this.maximumValues[j], input[j]);

                currentRow = new DataSetRow(input, currentRow.getDesiredOutput());
                dataSet.set(i, currentRow);
            }
        }

    }

    @Override
    public void normalizeDataSet(MLDataSet dataSet) {

        setDataSetAttributesRangeValues(dataSet);

        for(int i = 0; i < dataSet.size(); i++)
        {
            MLDataPair currentPair = dataSet.get(i);
            double[] input = currentPair.getInputArray();

            for(int j = 0; j < input.length; j++)
            {
                if(input[j] != NULLVAL)
                    input[j] = normalize(this.minimumValues[j], this.maximumValues[j], input[j]);
                else
                {
                    input[j] = this.minimumValues[j] + this.maximumValues[j] / 2;
                }

                currentPair.setInputArray(input);
            }
        }

    }


    @Override
    public double[] normalizeInputArray(double[] array) {

        if(array.length != this.getMaximumValues().length) {
            System.err.println("InputData array and MaximumValues have different lengths.");
            return null;
        }

        double[] ret = new double[array.length];

        for(int i = 0; i < array.length; i++)
        {
            ret[i] = normalize(this.minimumValues[i], this.maximumValues[i], array[i]);
        }

        return ret;

    }

    public double getRangeMin() {
        return rangeMin;
    }

    public double getRangeMax() {
        return rangeMax;
    }

    public double[] getMinimumValues() {
        return minimumValues;
    }

    public double[] getMaximumValues() {
        return maximumValues;
    }
}
