package normalizers;


import org.neuroph.core.data.DataSet;

public interface Normalizer {

    void normalizeDataSet();
    double[] normalizeInputArray(double[] array);

}
