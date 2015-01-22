import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class LIWC {
	
	int LIWC_DIMENSION=69;
	int REVIEW_TOTAL=80;

	public double[][] feature(int index,int flag,int dimension) throws Exception { 
												   //flag==0  TRAINING, index 빼고 다 
		                                          //flag==1  TEST , index 만
		// TODO Auto-generated method stub
		
		double [][] liwc_features = new double[dimension][LIWC_DIMENSION];
		String temp="DataSet/LIWC/";
		String temp1,temp2,file_dire;
		int count=0;
		
		for(int i=0;i<2;i++) { 
			if(i==0) temp1="DECEPTIVE/deceptive_fold";
			else     temp1="TRUTHFUL/truthful_fold";
			
			if(flag==1) { //TEST
				temp2=Integer.toString(index+1) + ".dat";
				
				file_dire = temp+temp1+temp2;
				//System.out.println(file_dire);
				
				BufferedReader br = new BufferedReader(new FileReader(file_dire));
				String line=br.readLine(); //첫줄은 버린다
				
				for(int j=0;j<REVIEW_TOTAL;j++) {
					line = br.readLine();
					String[] raw_feature= line.split("\t");
					for(int k=2;k<LIWC_DIMENSION+2;k++) {
						liwc_features[count][k-2]=Double.valueOf(raw_feature[k]).doubleValue();
					}
					count++;
				}
				
			}
			else {
				for(int j=0;j<5;j++) {
					if(j==index) continue;
					temp2=Integer.toString(j+1)+".dat";
					
					file_dire = temp+temp1+temp2;
					
					BufferedReader br = new BufferedReader(new FileReader(file_dire));
					String line=br.readLine(); //첫줄은 버린다.
					
					for(int q=0;q<REVIEW_TOTAL;q++) {
						line = br.readLine();
						String[] raw_feature= line.split("\t");
						for(int k=2;k<LIWC_DIMENSION+2;k++) {
							liwc_features[count][k-2]=Double.valueOf(raw_feature[k]).doubleValue();
						}
						count++;
					}
					
				}
				
			}
			
		}
		
		return liwc_features;
	}
	
	
}
