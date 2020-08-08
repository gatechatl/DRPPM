package proteomics.phospho.tools.pssm.distribution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class RandomSelectionPSSM {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "PSSM score distribution";
	}
	public static String parameter_info() {
		return "[motifScoreFile] [num_random_score] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			String motifScoreFile = args[0];
			int randomMotif = new Integer(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(motifScoreFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				list.add(str);
				
				
			}
			in.close();
			Random rn = new Random();
			for (int i = 0; i < randomMotif; i++) {
				
				int id = rn.nextInt(list.size());
				String line = (String)list.get(id);
				String[] split = line.split("\t");
				out.write(split[0] + "\t" + split[1] + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
