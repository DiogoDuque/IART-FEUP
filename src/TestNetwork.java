import java.util.ArrayList;
import java.util.Random;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;



public class TestNetwork {
	public static void main(String[] args){
	
		ArrayList<Integer> layers = new ArrayList<Integer>();
		layers.add(3);
		layers.add(2);
		layers.add(1);
		NeuralNetwork<BackPropagation> neuralNetwork = new MultiLayerPerceptron(layers);
		
		generateTrainingSet();
		DataSet trainingSet = generateTrainingSet();
		
		DataSet testingSet = generateTestingSet();
		
		LearningThread trainer = new LearningThread(neuralNetwork, trainingSet, testingSet, 0.98);
		trainer.run();
		
		
	}

	private static DataSet generateTrainingSet() {
		Random r = new Random();
		
		DataSet set = new DataSet(3, 1);
		
		for(int i = 0; i < 500; i++){
			double red = r.nextDouble();
			double blue = r.nextDouble();
			double green = r.nextDouble();
			double res = 0;
			if (red + blue + green >= 1.5){
				res = 1;
			}
			
			
			//Uncomment to generate nulls
			/*double MC = r.nextDouble();
			if (MC < 0.01)
				red = ArrfReader.NULLVAL;
			else if(MC < 0.02)
				blue = ArrfReader.NULLVAL;
			else if(MC < 0.03)
				green = ArrfReader.NULLVAL;
			else if(MC < 0.04)
				res = ArrfReader.NULLVAL;*/
			
			set.addRow(new DataSetRow(new double[]{red, blue, green}, new double[]{res}));
		}
		return set;
	}
	
	private static DataSet generateTestingSet() {
		Random r = new Random();
		
		DataSet set = new DataSet(3, 1);
		
		for(int i = 0; i < 50; i++){
			double red = r.nextDouble();
			double blue = r.nextDouble();
			double green = r.nextDouble();
			double res = 0;
			if (red + blue + green >= 1.5){
				res = 1;
			}
			
			/*double MC = r.nextDouble();
			if (MC < 0.01)
				red = ArrfReader.NULLVAL;
			else if(MC < 0.02)
				blue = ArrfReader.NULLVAL;
			else if(MC < 0.03)
				green = ArrfReader.NULLVAL;
			else if(MC < 0.04)
				res = ArrfReader.NULLVAL;*/
			
			set.addRow(new DataSetRow(new double[]{red, blue, green}, new double[]{res}));
		}
		return set;
	}
}
