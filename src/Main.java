public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int i;
		int [][]preci_recall = new int [5][6];   

		NGRAM ngram = new NGRAM();
		
		for(i=0; i<5; i++) {
			
			System.out.println((i+1) + " th FOLD IS A TEST SET");
			
			double[][] test_ngram_features = ngram.feature(i,1);
			double[][] train_ngram_features = ngram.feature(i,0);
			
			System.out.println("NGRAM FEATURES...OK");
	
//			
//			LIWC liwc = new LIWC();
//			
//			double[][] test_liwc_features = liwc.feature(i,1,test_ngram_features.length);
//			double[][] train_liwc_features = liwc.feature(i,0,train_ngram_features.length);
//
//			System.out.println("LIWC FEATURES..OK");
//			
//			COMBINE combine = new COMBINE();
//			double[][] train_features = combine.sum(train_liwc_features,train_ngram_features);
//			double[][] test_features = combine.sum(test_liwc_features,test_ngram_features);
//			
//			System.out.println("COMBINE...OK");
		
			SVMLIGHT svmlight = new SVMLIGHT();
			preci_recall[i]=svmlight.calcc(train_ngram_features, test_ngram_features);

		}
		
//	     0 : truthful TP
//	     1 : truthful TP+FP
//	     2 : truthful TP+FN
//	     3 : deceptive TP
//	     4 : deceptive TP+FP
//	     5 : deceptive TP+FN
		
		int truthful_TP_sum=0,truthful_TPFP_sum=0,truthful_TPFN_sum=0;
		int deceptive_TP_sum=0,deceptive_TPFP_sum=0,deceptive_TPFN_sum=0;
		
		for(i=0;i<5;i++) {
			truthful_TP_sum+=preci_recall[i][0];
			truthful_TPFP_sum+=preci_recall[i][1];
			truthful_TPFN_sum+=preci_recall[i][2];
			
			deceptive_TP_sum+=preci_recall[i][3];
			deceptive_TPFP_sum+=preci_recall[i][4];
			deceptive_TPFN_sum+=preci_recall[i][5];
		}
				
		System.out.println("\n\nTRUTHFUL_TP_SUM : " + truthful_TP_sum);
		System.out.println("TRUTHFUL_TPFP_SUM : " + truthful_TPFP_sum);
		System.out.println("TRUTHFUL_TPFN_SUM : " + truthful_TPFN_sum);
		
		System.out.println("DECEPTIVE_TP_SUM : " + deceptive_TP_sum);
		System.out.println("DECEPTIVE_TPFP_SUM : " + deceptive_TPFP_sum);
		System.out.println("DECEPTIVE_TPFN_SUM : " + deceptive_TPFN_sum);
		
		System.out.println("\nTRUTHFUL PRECISION : " + (double)(truthful_TP_sum)/(double)(truthful_TPFP_sum));
		System.out.println("TRUTHFUL RECALL : " + (double)(truthful_TP_sum)/(double)(truthful_TPFN_sum));
		
		System.out.println("DECEPTIVE PRECISION : " + (double)(deceptive_TP_sum)/(double)(deceptive_TPFP_sum));
		System.out.println("DECEPTIVE RECALL : " + (double)(deceptive_TP_sum)/(double)(deceptive_TPFN_sum));
		

	}

}
