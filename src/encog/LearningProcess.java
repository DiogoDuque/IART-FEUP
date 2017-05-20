package encog;

import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.prune.PruneSelective;
import utils.Converter;
import utils.NN;

import java.util.ArrayList;
import java.util.Map;

public class LearningProcess {

    private static long elapsedTime;
    private static int epoch;

    /**
     * Trains a certain propagation object according to a maximum error parameter.
     * @param propagation The propagation object.
     * @param maxError The maximum propagation error.
     */
    public static void iterateWithRule(Propagation propagation, double maxError){

        // begin counting time
        elapsedTime = System.nanoTime();

        StringBuilder sb = new StringBuilder();

        for(MLDataPair pair : propagation.getTraining())
        {
            sb.append("\tPair of propagation: ").append(pair).append("\n");
        }

        System.out.println(sb.toString());

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

    public static void train(MyNetwork myNetwork, MyTrainingSet trainingSet, double maxError) throws Exception {

        for(Map.Entry<String, MLDataSet> entry: trainingSet.getTrainingSets().entrySet()){

            /*for(int i = 0; i < entry.getValue().size(); i++)
            {
                System.out.println(i + ": " + entry.getValue().get(i));
            }*/

            MLDataSet currentSet = entry.getValue();
            System.out.println(currentSet.size());

            Propagation propagation = new ResilientPropagation(myNetwork.getNetwork(), currentSet);
            System.out.println("Current set: " + currentSet);
            propagation.setThreadCount(4);

            ArrayList<Integer> missingValues = Converter.readArrayFromString(entry.getKey());
            myNetwork.adaptToMissingValues(missingValues);

            iterateWithRule(propagation, maxError);
        }

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

    private static void prune(BasicNetwork network, Propagation propagation){

        PruneSelective pruneSelective = new PruneSelective(network);
        MLDataSet trainingSet = propagation.getTraining();

        for(MLDataPair pair : trainingSet){

            double[] inputs = pair.getInputArray();

            byte[] missingValues = NN.getMissingValues(inputs);


        }

    }
}
