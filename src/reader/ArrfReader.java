package reader;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * How to use:
 *
 * Arrf reader = new ArrfReader(<filePath>);
 * reader.getCompany(<company_position_in_file>).get(<data_position>);
 */
public class ArrfReader {
    public static final double NULLVAL = Math.PI;

    private String filePath;
    private Instances dataInstances;
    private ArrayList<ArrayList<Double>> fullDataSet;

    /**
     * Class that stores the data included in the ARRF file.
     * @param filePath .arrf filepath
     */
    public ArrfReader(String filePath)
    {
        this.filePath = filePath;
        this.fullDataSet = new ArrayList<>();

        this.readFile();
        this.readDataSet();
    }


    public ArrayList<Double> readCompanyData(int instanceNum)
    {
        ArrayList<Double> data = new ArrayList<>();

        List<String> dataString = Arrays.asList(this.dataInstances.instance(instanceNum).toString().split(","));

        for(int i = 0; i < dataString.size(); i++)
        {
            String s = dataString.get(i);

            if (s.equalsIgnoreCase("?")) {
                data.add(NULLVAL); //this will be detected later
            } else {
                data.add(Double.parseDouble(s));
            }
        }

        return data;
    }

    private void readDataSet()
    {
        for(int i = 0; i < this.dataInstances.numInstances() - 1; i++)
        {
            this.fullDataSet.add(readCompanyData(i));
        }
    }

    private void readFile()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.filePath));
            this.dataInstances = new Instances(reader);
            reader.close();

            // setting class attribute
            dataInstances.setClassIndex(dataInstances.numAttributes() - 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Double> getBankruptcyOfCompany(int companyNum)
    {
        ArrayList<Double> ret = new ArrayList<>();
        double bankrupt = this.fullDataSet.get(companyNum).get(64);

        if(bankrupt != 1d && bankrupt != 0d )
        {
            ret.add(-1d);
        }
        
        else
        {
            ret.add(bankrupt);
        }

        return ret;
    }

    public ArrayList<Double> getCompanyData(int companyNum){

        ArrayList<Double> ret = new ArrayList<>(this.fullDataSet.get(companyNum));

        ret.remove(64); // so the bankruptcy result is not included

        return ret;
    }

    public ArrayList<ArrayList<Double>> getFullDataSet() {
        return fullDataSet;
    }

    public double[] getColumnValues(int column) {

        try {
            ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
            replaceMissingValues.setInputFormat(this.dataInstances);
            this.dataInstances = Filter.useFilter(this.dataInstances, replaceMissingValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.dataInstances.attributeToDoubleArray(column);

    }

    // FOR TESTING
    public static void main(String[] args) {

        ArrfReader reader = new ArrfReader("./dataset/"+ "test2" + ".arff");
        System.out.println(Arrays.toString(reader.getColumnValues(3)));

    }

    /**
     *
     * @return An ArrayList with the input 2D array one position 0 and the output 2D array on position 1.
     */
    public ArrayList<double[][]> getInputAndOutput(){

        int numInputNeurons = this.fullDataSet.get(0).size() - 1;
        int numOutputNeurons = 1;
        int totalNumOfNeurons = numInputNeurons + numOutputNeurons;
        int numberOfEntries = this.fullDataSet.size();

        double input[][] = new double[numberOfEntries][numInputNeurons];
        double output[][] = new double[numberOfEntries][numOutputNeurons];

        for(int i = 0; i < numberOfEntries; i++){

            ArrayList<Double> currentCompany = this.fullDataSet.get(i);

            for(int j = 0; j < totalNumOfNeurons; j++)
            {
                if(j == numInputNeurons)
                {
                    output[i][0] = currentCompany.get(j);
                }
                else
                {
                    input[i][j] = currentCompany.get(j);
                }
            }

        }

        ArrayList<double[][]> ret = new ArrayList<>();
        ret.add(input);
        ret.add(output);

        return ret;
    }
}
