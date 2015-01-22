import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import jnisvmlight.KernelParam;
import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;


public class SVMLIGHT {

	 public int[] calcc(double[][] train_features, double[][] test_features) throws Exception {
		// TODO Auto-generated method stub
		 
		int N=train_features.length;
		int L=test_features.length;
		int M=train_features[0].length;
		
		SVMLightInterface trainer = new SVMLightInterface();
		LabeledFeatureVector[] traindata = new LabeledFeatureVector[N];
		SVMLightInterface.SORT_INPUT_VECTORS = true;

	    for (int i = 0; i < N; i++) {
	    	int[] dims = new int[M];
	    	double[] values = new double[M];
	    	int flag=1;   
	    		//DECEPTIVE==> flag=1,    TRUTHFUL==> flag=-1
	 
	        if(i<N/2) flag=1;
	        else flag=-1;
	      
	        for(int j=0;j<M;j++) {
	        	dims[j]=j+1;
	        	values[j]=train_features[i][j];
	        }
	        traindata[i] = new LabeledFeatureVector(flag, dims,values);
	        traindata[i].normalizeL2();
	    }
	    TrainingParameters tp = new TrainingParameters();
	    KernelParam kp = new KernelParam();
	    
	    tp.getLearningParameters().verbosity = 1;
	    
	    System.out.println("TRAINING SVM-light MODEL ..");
	    SVMLightModel model = trainer.trainModel(traindata, tp);
	    System.out.println(" DONE.");

	    System.out.println("\nVALIDATING SVM-light MODEL in Java..");
	   
	    
	   /*===========================TEST======================*/ 
	   
	    LabeledFeatureVector[] testdata = new LabeledFeatureVector[N];
	    SVMLightInterface.SORT_INPUT_VECTORS = true;

	    
	    for (int i = 0; i < L; i++) {
	    	int[] dims = new int[M];
	    	double[] values = new double[M];
	      	int flag=1;
	     
	        if(i<N/2) flag=1;
	        else flag=-1;
	  
	        for(int j=0;j<M;j++) {
	        	dims[j]=j+1;
	        	values[j]=test_features[i][j];
	        }
	        testdata[i] = new LabeledFeatureVector(flag, dims,values);
	        testdata[i].normalizeL2();
	    }
	    
	    
	    int[] result = new int[6];
	    
	    int truePositive_deceptive=0, falsePositive_deceptive=0, falseNegative_deceptive=0;
	    int truePositive_truthful=0, falsePositive_truthful=0, falseNegative_truthful=0;
	    
	    
	    // ONLY DECEPTIVE!!!
	    for (int i = 0; i < L; i++) {
	    	double d = model.classify(testdata[i]);
	    	
	    	if(i<L/2) { //DECEPTIVE ....flag=1
	    		if(d>0) { //DECEPTIVE인데 deceptive로 예측 =====>TP
	    			truePositive_deceptive++;
	    		}
	    		else { //DECEPTIVE 인데 truthful 로 예측===>FN
	    			falseNegative_deceptive++;
	    		}
	    	}
	    	
	    	else { //TRUTHFUL ... flag=-1
	    		if(d>0) { //TRUTHFUL 인데 DECEPTIVE로 예측 ====> FP
	    			falsePositive_deceptive++;
	    		}
	    	}
	    }
	    
	    // ONLY TRUTHFUL!!!
	    for (int i = 0; i < L; i++) {
	    	double d = model.classify(testdata[i]);
	    	
	    	if(i<L/2) { //DECEPTIVE ....flag=1
	    		if(d<0) { //DECEPTIVE 인데  TRUTHFUL로 예측 =====>FP
	    			falsePositive_truthful++;
	    		}
	    		
	    	}
	    	
	    	else { //TRUTHFUL ... flag=-1
	    		if(d<0) { //TRUTHFUL인데  TRUTHFUL로 예측 ====> TP
	    			truePositive_truthful++;
	    		}
	    		else { //TRUTHFUL인데 DECEPTIVE로 예측===> FN
	    			falseNegative_truthful++;
	    		}
	    	}
	    }

//	     0 : truthful TP
//	     1 : truthful TP+FP
//	     2 : truthful TP+FN
//	     3 : deceptive TP
//	     4 : deceptive TP+FP
//	     5 : deceptive TP+FN
	    
	    result[0] = truePositive_truthful;
	    result[1] = truePositive_truthful+falsePositive_truthful;
	    result[2] = truePositive_truthful+falseNegative_truthful;
	    
	    result[3] = truePositive_deceptive;
	    result[4] = truePositive_deceptive+falsePositive_deceptive;
	    result[5] = truePositive_deceptive+falseNegative_deceptive;
	   
	    return result;
	}

}
