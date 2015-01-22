import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class NGRAM {


	int train_total_num=640;
	int test_total_num=160;
	int review_total_num=80;
	

	Vector<String> wordset = new Vector<String>();

	int [][][]word_count = new int [2][5][review_total_num];
	Vector [][][] words = new Vector[2][5][review_total_num];

	
	public static List<String> ngrams(int n, String str) {
	    List<String> ngrams = new ArrayList<String>();
	    String[] words = str.split("\\s+");
	    for (int i = 0; i < words.length - n + 1; i++)
	        ngrams.add(concat(words, i, i+n));
	     return ngrams;
	}

	public static String concat(String[] words, int start, int end) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = start; i < end; i++)
	        sb.append((i > start ? " " : "") + words[i]);
	    return sb.toString();
	}

	public NGRAM() throws Exception {
		System.out.println("============NGRAM WORDSET MAKING==========");
		
		for(int i=0;i<2;i++) {
			for(int j=0;j<5;j++) { 
				for(int q=0;q<review_total_num;q++) { 
					words[i][j][q] = new Vector();
				}
			}
		}
				
		wordset();
		
		System.out.println("WORDSET SIZE : " + wordset.size());
		System.out.println("==========================================");
	
	}

	public double[][] feature(int index,int flag) throws Exception { // flag=0 TRAINING, index EXCEPT ALL
																	 //	flag=1 TEST, index ONLY 
		
	
		if(flag==1) { 
		
			double[][] test = new double[test_total_num][wordset.size()];
			int test_count=0;
			
			for(int i=0;i<2;i++) {

				for(int k=0;k<review_total_num;k++)  {
					for(int w=0;w<words[i][index][k].size();w++) {
						String[] temp = (String[])words[i][index][k].elementAt(w);
						String review_word = temp[0];
						double c = Double.valueOf(temp[1]).doubleValue();
						
						int point = wordset.indexOf(review_word);
						test[test_count][point]=c/(double)(word_count[i][index][k]);
					
					}
	
					test_count++;
				}
			}
	
			return test;
		}
		
		else if(flag==0) { 
	
			double[][] train = new double[train_total_num][wordset.size()];
			int train_count=0;
			
			for(int i=0;i<2;i++) { 
				for(int j=0;j<5;j++) {
					if(j==index) continue;
					for(int k=0;k<review_total_num;k++) {
						for(int w=0;w<words[i][j][k].size();w++) {
							String[] temp = (String[])words[i][j][k].elementAt(w);
							String review_word = temp[0];
							double c = Double.valueOf(temp[1]).doubleValue();
							
							int point = wordset.indexOf(review_word);
							train[train_count][point]=c/(double)(word_count[i][j][k]+0.001);
						}
						train_count++;
					}
				}
			}
			return train;
		}
	
		return null;
		
	}

	private Vector duplicate_remove_and_count(Vector v) {
		// TODO Auto-generated method stub
		
		Vector d = new Vector();
	
		for (int i = 0 ; i < v.size() ; i++ ) {
			String v_tmp = (String)v.elementAt(i);  
			Boolean existFlag = false;     
		
			if ( d.size() == 0 ) {
				String[] tmp_s = {v_tmp,"0"};
			    d.addElement(tmp_s);
			}   
			for (int z = 0 ; z < d.size() ; z++ ) {
				String[][] zz = new String[d.size()][2];
			    d.copyInto(zz);
			    
			    String[] d_tmp = (String[])d.elementAt(z);
			    
			    if ( d_tmp[0].equals(v_tmp) ) {
			     existFlag = true;
			    
			     d_tmp[1] = Integer.toString(Integer.parseInt(d_tmp[1]) + 1);
			    }
			 }
			 if ( existFlag == false ) {
			    String[] tmp_s = {v_tmp,"1"};
			    d.addElement(tmp_s);
			 } 
		}

		return d;
	}

	private Vector wordset_duplicate_remove(Vector v) {
		// TODO Auto-generated method stub
		
		Vector d = new Vector();
	
		
		for (int i = 0 ; i < v.size() ; i++ ) {
			String v_tmp = (String)v.elementAt(i);  
			Boolean existFlag = false;    
			   
			
			if ( d.size() == 0 ) {
			   d.addElement(v_tmp);
			}
			   
			
			for (int z = 0 ; z < d.size() ; z++ ) {
			   String d_tmp = (String)d.elementAt(z);
			
			  
			    if ( d_tmp.equals(v_tmp) ) {
			    	existFlag = true;
			    }    
		    }
			   
			
			if ( existFlag == false ) {
				d.addElement(v_tmp);
			}
		}
		
		return d;
	}

	private void wordset() throws Exception {
		// TODO Auto-generated method stub
		
		String temp="DataSet/OttDATA/";
		String temp1,temp2,folder,file_dire;
		
		Vector uni_raw = new Vector();
		Vector bi_raw = new Vector();

		int q=0;
		
		for(int i=0;i<2;i++) {
			if(i==0) temp1="deceptive_from_MTurk/fold";
			else temp1="truthful_from_TripAdvisor/fold";
			
			for(int j=0;j<5;j++) {
				temp2 = Integer.toString(j+1);
				folder = file_dire = temp + temp1 + temp2;
						
				File file = new File(folder);
				
				if(!file.isDirectory()) {
					System.out.println("There is no Directory");
					System.exit(1);
				}
				
				File[] list = file.listFiles();
				
				q=0;
				
				for(File f:list) {
					if(f.isFile()) {
						file_dire = folder + "/" + f.getName();
						BufferedReader br = new BufferedReader(new FileReader(file_dire));
						String line="",fullLine = "";
						
						while( (line = br.readLine()) != null) {
							fullLine+=line;
						}
						fullLine = fullLine.toLowerCase();    //LowerCase
						
						Vector uniuni = new Vector();
						Vector bibi = new Vector();
						
						for(String ngram : ngrams(1,fullLine)) {
							uni_raw.addElement(ngram);
							uniuni.addElement(ngram);
							word_count[i][j][q]++;
						}
						
						for(String ngram : ngrams(2,fullLine)) { 
							bi_raw.addElement(ngram);
							bibi.addElement(ngram);
						}
						

						words[i][j][q].addAll(duplicate_remove_and_count(uniuni));
						words[i][j][q].addAll(duplicate_remove_and_count(bibi));

						q++;
					}
				}
			}
		}
			
		wordset.addAll(wordset_duplicate_remove(uni_raw));
		System.out.println("UNIGRAM FEATURES MAKE... DONE");
		wordset.addAll(wordset_duplicate_remove(bi_raw));
		System.out.println("BIGRAM FEATURES MAKE...DONE");

	}
}


