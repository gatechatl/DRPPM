package RNAseqTools.SingleCell.Bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

public class VariantMatrixBootstrap {

	public static String description() {
		return "Generate bootstrapped variant matrix";		
	}
	
	public static String type() {
		return "SingleCell";		
	}
	
	public static String parameter_info() {
		return "[inputVariantMatrix] [outputShuffledVariantMatrix]";		
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
		    BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream); 
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				LinkedList list = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					list.add(split[i]);
				}
				int size = list.size();
				Random rand = new Random();
				while (list.size() > 0) {
					int index = rand.nextInt(size);
					String val = (String)list.get(index);
					out.write("\t" + val);
					list.remove(index);
					size = list.size();
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
