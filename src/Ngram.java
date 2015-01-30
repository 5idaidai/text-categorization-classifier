import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class Ngram {
	public final static String original_data_path = "data/OttDATA";
	public final static String ngram_data_path = "data/NgramData";

	public static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		Unigram unigram = new Unigram();
		Bigram bigram = new Bigram();
		String update = "n";

		Set<File> ngram_files = (Set<File>) listFileTree(new File(
				ngram_data_path));

		if (ngram_files.isEmpty() == false) {
			System.out
					.println("Ngram data already exists. Do you want to update the data? (y/n)");
			update = scanner.nextLine();
		}

		if (ngram_files.isEmpty() | update.equals("y")) {
			ngram(unigram);
			ngram(bigram);
		}
		
		feature(unigram);
		feature(bigram);
		
		System.out.println("Ngram Finish");
	}

	public static HashMap<String, ArrayList<Double>> feature(Ngrams ngrams) {
		System.out.printf("\n====== Generating Feature : %s ======\n", ngrams.name);
		Set<File> files = (Set<File>) listFileTree(new File(ngrams.data_path));
		ArrayList<HashMap<String, Double>> tokens_with_frequency_list = new ArrayList<HashMap<String, Double>>();
		HashMap<String, ArrayList<Double>> tokens_with_frequency_set = new HashMap<String, ArrayList<Double>>();
		
		
		System.out.print("Token-Set Generating ");
		for (File f : files) {
			HashMap<String, Double> tokens_with_frequency = new HashMap<String, Double>();
			try {
				FileInputStream fileIn = new FileInputStream(f);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				tokens_with_frequency = (HashMap<String, Double>) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
			} catch (ClassNotFoundException c) {
				c.printStackTrace();
			}

			for (String token : tokens_with_frequency.keySet()) {
				ArrayList<Double> frequency_list = new ArrayList<Double>();
				tokens_with_frequency_set.put(token, frequency_list);
			}
			
			tokens_with_frequency_list.add(tokens_with_frequency);
			System.out.print(">");
		}
		System.out.println("\n[Done]\n");
		
		System.out.print("Frequency Matching ");
		for (HashMap<String, Double> tokens_with_frequency : tokens_with_frequency_list) {
			for (String token : tokens_with_frequency_set.keySet()) {
				ArrayList<Double> frequency_list = new ArrayList<Double>();
				frequency_list = tokens_with_frequency_set.get(token);
				Double frequency = tokens_with_frequency.get(token);
				frequency = (frequency == null) ? 0.0 : frequency;
				frequency_list.add(frequency);
			}
			System.out.print(">");
		}
		System.out.println("\n[Done]\n");

		return tokens_with_frequency_set;
	}
	
	public static void ngram(Ngrams ngrams) throws IOException {
		System.out.printf("\n====== Generating N-Gram : %s ======\n", ngrams.name);
		Set<File> files = (Set<File>) listFileTree(new File(original_data_path));
		for (File f : files) {
			String parent_path = f.getPath().replace(original_data_path, "");
			String sentence = FileUtils.readFileToString(f, "utf-8")
					.toLowerCase();
			List<String> tokens = tokenizer(ngrams.n, sentence);
			HashMap<String, Double> tokens_with_frequency = frequencyCount(
					sentence.length(), tokens);
			File file = new File(ngrams.data_path + parent_path);
			try {
				FileOutputStream fileOut = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(tokens_with_frequency);
				out.close();
				fileOut.close();
			} catch (IOException i) {
				i.printStackTrace();
			}
			System.out.print(">");
		}
		System.out.println("\n[Done]\n");
	}

	public static List<String> tokenizer(int n, String sentence) {
		List<String> tokens = new ArrayList<String>();
		String[] words = sentence.split("\\s+");
		for (int i = 0; i < words.length - n + 1; i++)
			tokens.add(concat(words, i, i + n));

		return tokens;
	}

	private static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);

		return sb.toString();
	}

	public static HashMap<String, Double> frequencyCount(int sentence_length,
			List<String> tokens) {
		double sentence_frequency = (1.00 / sentence_length);
		HashMap<String, Double> tokens_with_frequency = new HashMap<>();
		for (String token : tokens) {
			Double frequency = tokens_with_frequency.get(token);
			frequency = (frequency == null) ? sentence_frequency
					: (frequency + sentence_frequency);
			tokens_with_frequency.put(token, frequency);
		}

		return tokens_with_frequency;
	}

	private static Collection<File> listFileTree(File dir) {
		Set<File> fileTree = new HashSet<File>();
		for (File entry : dir.listFiles()) {
			if (entry.isFile()) {
				fileTree.add(entry);
			} else {
				fileTree.addAll(listFileTree(entry));
			}
		}

		return fileTree;
	}
}

abstract class Ngrams {
	String name;
	String data_path;
	int n;
}

class Unigram extends Ngrams {
	public Unigram() {
		super.name = "Unigram";
		super.data_path = "data/NgramData/Unigram";
		super.n = 1;
	}
}

class Bigram extends Ngrams {
	public Bigram() {
		super.name = "Bigram";
		super.data_path = "data/NgramData/Bigram";
		super.n = 2;
	}
}