package protein.features.charge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CalculateChargeFastaFile {

	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String description() {
		return "Calculates the charge based on the calculation from pepstats from emboss. Program must be installed and accessible as \"pepstats\"\n";
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
            out.write("Name\tType\tAvgCharge\tCharge\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String seq = (String)map.get(name);
				
				// this seems to work only in interactive mode. pepstats is required to be installed
				double charge = GenerateChargeGraphForEachProtein.calculate_charge(seq) ;
				double avg_charge = charge / seq.length();
				out.write(name + "\t" + typeName + "\t" + avg_charge + "\t" + charge + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
