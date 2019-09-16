package proteomics.phospho.tools.kinase.substrate.predictions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * It seems like some of the kinase doesn't track well through correlation approach. We need to first remove kinase that have a low correlation
 * @author tshaw
 *
 */
public class AUCFilter {

	public static String type() {
		return "KINASE";
	}
	public static String parameter_info() {
		return "[inputFile] [cutoff] [outputFile]";
	}
	public static String description() {
		return "Filter out kinase with low AUC";
	}
	
	public static void execute(String[] args) {
		
		
		try {
			
			String inputFile = args[0];
			double cutoff = new Double(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();		
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (new Double(split[2]) >= cutoff) {
					map.put(split[1], split[1]);
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();					
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[1])) {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
