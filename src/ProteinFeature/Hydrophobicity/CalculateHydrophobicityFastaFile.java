package ProteinFeature.Hydrophobicity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import misc.CommandLine;
import ProteinFeature.Charge.CalculateCharge;
import ProteinFeature.Charge.GenerateChargeGraphForEachProtein;

public class CalculateHydrophobicityFastaFile {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Calculates the hydrophobicity based on the calculation from R.\n";
	}
	public static String parameter_info() {
		return "[fastaFile] [outputFile] [TypeName]";
	}
	public static void execute(String[] args) {
		
		try {
			String fastaFile = args[0];
			String outputFile = args[1];
			String typeName = args[2]; 
			HashMap map = loadFastaFile(fastaFile);
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("Name\tType\tHydrophobicity\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				
				// this seems to work only in interactive mode. pepstats is required to be installed
				double hydrophobicity = calculate_hydrophobicity(seq) ;
				//double avg_charge = charge / seq.length();
				out.write(name + "\t" + typeName + "\t" + hydrophobicity + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calculate_hydrophobicity(String seq) {		
		try {
			
			String buffer = UUID.randomUUID().toString();
			String script_file = buffer + ".r";
			String script = "library(Peptides)\n";
			script += "data = hydrophobicity(\"" + seq + "\")\n";
			script += "write(data, file=\"" + buffer + "\")\n";			
			CommandLine.writeFile(script_file, script);
			CommandLine.executeCommand("R --vanilla < " + script_file);
			FileInputStream fstream = new FileInputStream(buffer);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String hydrophobicity = in.readLine();
			in.close();
			File f = new File(buffer);
			f.delete();
			f = new File(script_file);
			f.delete();
			return new Double(hydrophobicity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Double.NaN;
		
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
