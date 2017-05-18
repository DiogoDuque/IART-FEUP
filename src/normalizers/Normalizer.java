package normalizers;


import org.encog.ml.data.MLDataSet;
import org.neuroph.core.data.DataSet;

public interface Normalizer {

    void normalizeDataSet(DataSet dataSet);
    void normalizeDataSet(MLDataSet dataSet);
    double[] normalizeInputArray(double[] array);

}
