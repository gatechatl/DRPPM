package protein.features.combineresults;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CombineAAFreqProteinFeature {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Combine aa freq and other protein feature info";
	}
	public static String parameter_info() {
		return "[aa_freq_file] [protein_feature_file] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap protein_aa_freq = new HashMap();
			String aa_freq_file = args[0];
			String protein_feature_file = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(aa_freq_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[0].split("\\|")[1];
				System.out.println(accession);
				String stuff = "";
				for (int i = 1; i < split.length; i++) {
					if (i == 1) {
						stuff = split[i];
					} else {
						stuff += "\t" + split[i];
					}
				}
				protein_aa_freq.put(accession, str);
			}
			in.close();

			fstream = new FileInputStream(protein_feature_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header2 = in.readLine();
			out.write(header2 + "\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (protein_aa_freq.containsKey(split[1])) {
					out.write(str + "\t" + protein_aa_freq.get(split[1]) + "\n");
				} else {
					System.out.println("problem");
				}								
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
