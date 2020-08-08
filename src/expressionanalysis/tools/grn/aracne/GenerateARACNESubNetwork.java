package expressionanalysis.tools.grn.aracne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Extract ARACNE's Mutual Information subnetwork
 * @author tshaw
 *
 */
public class GenerateARACNESubNetwork {

	public static String description() {
		return "Generate ARACNE subnetwork given a genelist.";
	}
	public static String type() {
		return "Gene Regulatory Networks";
	}
	public static String parameter_info() {
		return "[inputADJFile] [inputGeneListFile] [outputFile]";
	}
	public static void execute(String[] args) {				
		try {			

			String inputGeneListFile = args[1];
			String inputADJFile = args[0];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputGeneListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();
			
			fstream = new FileInputStream(inputADJFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (str.substring(0, 1).equals(">")) {
					out.write(str + "\n");
				} else {
					if (map.containsKey(split[0])) {
						out.write(str + "\n");
					}
				}
				
			}
			in.close();
			out.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
