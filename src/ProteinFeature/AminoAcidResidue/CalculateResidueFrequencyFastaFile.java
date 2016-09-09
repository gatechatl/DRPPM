package ProteinFeature.AminoAcidResidue;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CalculateResidueFrequencyFastaFile {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Calculates the residue frequency\n";
	}
	public static String parameter_info() {
		return "[fastaFile] [outputFile] [TypeName] [ResidueFrequency]";
	}
	public static void execute(String[] args) {
		
		try {
			String fastaFile = args[0];
			String outputFile = args[1];
			String typeName = args[2]; 
			String residues = args[3];
			HashMap map = loadFastaFile(fastaFile);
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("Name\tType\tAvgResidueFrequency\tResidueFrequency\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				
				// this seems to work only in interactive mode. pepstats is required to be installed
				//double charge = GenerateChargeGraphForEachProtein.calculate_charge(seq) ;
				double residue_frequency = residue_frequency(seq, residues);
				double avg_residue_frequency = residue_frequency / seq.length();
				out.write(name + "\t" + typeName + "\t" + avg_residue_frequency + "\t" + residue_frequency + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double residue_frequency(String seq, String residues) {
		double count = 0;
		String[] res = residues.split(",");
		for (int i = 0; i < seq.length(); i++) {
			for (String r: res) {
				if (seq.substring(i, i + 1).equals(r)) {
					count++;
				}
			}
		}
		return count;
	}
	public static HashMap loadFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.replaceAll(">",  "");					
				} else {					
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

