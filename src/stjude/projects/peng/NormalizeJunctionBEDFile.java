package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Normalize BED Junction file 
 * @author tshaw
 *
 */
public class NormalizeJunctionBEDFile {
	public static String description() {
		return "Normalize the BED junction count information which is on column 3 and 4.";
	}
	public static String type() {
		return "PENG";
	}
	public static String parameter_info() {
		return "[inputBEDFile] [outputBEDFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputBEDFile = args[0];
			String outputBEDFile = args[1];
			double total_junction = 0;
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				total_junction += new Double(split[3]);				
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputBEDFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(inputBEDFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				total_junction += new Double(split[3]);
				double normalized_junction = new Double(split[3]) * 1000000 / total_junction;
				normalized_junction = new Double(new Double(normalized_junction * 100).intValue()) / 100;
				String new_line = split[0];
				for (int i = 1; i < split.length; i++) {
					if (i == 3 || i == 4) {
						new_line += "\t" + normalized_junction; 
					} else {
						new_line += "\t" + split[i];
					}
				}
				out.write(new_line + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
