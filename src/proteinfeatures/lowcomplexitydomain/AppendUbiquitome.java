package proteinfeatures.lowcomplexitydomain;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendUbiquitome {

	public static String type() {
		return "JPaulTaylor";
	}
	public static String description() {
		return "Append ubiquitome proteome";
	}
	public static String parameter_info() {
		return "[inputFile] [protein_metadata]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			String protein_metadata = args[1];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();
			
			fstream = new FileInputStream(protein_metadata);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[1])) {
					String line = "";
					if (split[0].equals("Human_Proteome")) {
						line = "UBIQ";
					} else {
						line += split[0] + ",UBIQ";
					}
					line += "\t" + split[1] + "\t" + split[2];
					System.out.println(line);
				} else {
					System.out.println(str);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
