
public class COMBINE {

	public double[][] sum(double[][] liwc_features,double[][] ngram_features) {
		// TODO Auto-generated method stub
		double[][] final_features = new double[liwc_features.length] [liwc_features[0].length + ngram_features[0].length];
		
		for(int i=0;i<liwc_features.length;i++) {
			System.arraycopy(liwc_features[i], 0, final_features[i], 0, liwc_features[i].length);
			System.arraycopy(ngram_features[i], 0, final_features[i], liwc_features[i].length, ngram_features[i].length);
			
		}
	 	return final_features;
	}
}
