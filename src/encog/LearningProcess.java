package encog;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.prune.PruneSelective;
import utils.NN;

public class LearningProcess {

    private static long elapsedTime;
    private static int epoch;

    /**
     * Trains a certain propagation object according to a maximum error parameter.
     * @param network     The Basic Network to be trained.
     * @param propagation The propagation object.
     * @param maxError The maximum propagation error.
     */
    public static void train(BasicNetwork network, Propagation propagation, double maxError){

        // begin counting time
        elapsedTime = System.nanoTime();

        epoch = 1;

        do {
            propagation.iteration();

            System.out.println("Epoch #" + epoch + " Error:" + propagation.getError());
            epoch++;
        } while(propagation.getError() > maxError);

        propagation.finishTraining();

        // finish counting time
        elapsedTime = System.nanoTime() - elapsedTime;

    }

    /**
     *
     * @return Time elapsed during training in nanoseconds.
     */
    public static long getElapsedTime() {
        return elapsedTime;
    }

    /**
     *
     * @return Number of training iterations made by the learning process.
     */
    public static int getEpoch() {
        return epoch;
    }

    private void prune(BasicNetwork network, Propagation propagation){

        PruneSelective pruneSelective = new PruneSelective(network);
        MLDataSet trainingSet = propagation.getTraining();

        for(MLDataPair pair : trainingSet){

            double[] inputs = pair.getInputArray();

            byte[] missingValues = NN.getMissingValues(inputs);


        }

    }
}
