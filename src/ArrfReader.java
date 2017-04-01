import weka.core.Instance;
import weka.core.Instances;
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

        for(String s : dataString)
        {
            if(s.equalsIgnoreCase("?"))
            {
                data.add(0d);  // TODO: Verify if this is the best practice
            }
            else {
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
    
    public Double getBankruptcyOfCompany(int companyNum)
    {
        double bankrupt = getCompanyData(companyNum).get(64);

        if(bankrupt != 1d && bankrupt != 0d )
        {
            return -1d;
        }
        
        else
        {
            return bankrupt;
        }
    }

    public ArrayList<Double> getCompanyData(int companyNum){
        return this.fullDataSet.get(companyNum);
    }

    public ArrayList<ArrayList<Double>> getFullDataSet() {
        return fullDataSet;
    }

    public static void main(String[] args) {

        ArrfReader reader = new ArrfReader("/home/jazz/Downloads/Dane/1year.arff");

        System.out.println(reader.getCompanyData(1).get(1));

    }

}
