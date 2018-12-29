package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Prior to -ProteinFeatureHistoBarPlot we needed to generate figures comparing target vs background.
 * @author tshaw
 *
 */
public class JunminPengAnnotateProteinFeature {

	public static String description() {
		return "Prior to -ProteinFeatureHistoBarPlot we needed to generate figures comparing target vs background.";
	}
	public static String type() {
		return "JUNMIN_PENG";
	}
	public static String parameter_info() {
		return "[targetFile] [proteinFeature] [output_BackgroundFile] [output_QueryFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String targetFile = args[0]; // two column file
			String proteinFeature = args[1];
			String output_BackgroundFile = args[2];
			String output_QueryFile = args[3];
			
			FileWriter fwriter_background = new FileWriter(output_BackgroundFile);
			BufferedWriter out_background = new BufferedWriter(fwriter_background);			

			FileWriter fwriter_query = new FileWriter(output_QueryFile);
			BufferedWriter out_query = new BufferedWriter(fwriter_query);
			
			HashMap accession2type = new HashMap();
			FileInputStream fstream = new FileInputStream(targetFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				accession2type.put(split[0], split[1]);
				
			}
			in.close();
			
			fstream = new FileInputStream(proteinFeature);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out_background.write("Type\t" + header + "\n");
			out_query.write("Type\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (accession2type.containsKey(split[1])) {
					String type = (String)accession2type.get(split[1]);
					out_query.write(type + "\t" + str + "\n");
					out_background.write(type + "\t" + str + "\n");
				} else {
					out_background.write("Background" + "\t" + str + "\n");
				}
				
			}
			in.close();
			out_query.close();
			out_background.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
