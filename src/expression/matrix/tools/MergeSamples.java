package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Merging expression into an average
 * @author tshaw
 *
 */
public class MergeSamples {

	public static String description() {
		return "Merging groups of samples together by their average";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [samplegroup example: 1,3:2,5] [groupNames]";
	}
	public static void execute(String[] args) {
				
		try {
			String inputFile = args[0];
			String[] samplegroup = args[1].split(":");
			String[] groupNames = args[2].split(",");
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			for (int i = 0; i < groupNames.length; i++) {
				out.write("\t" + groupNames[i]);
			}
			out.write("\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 0; i < samplegroup.length; i++) {
					double avg_group = 0; 
					String[] samples = samplegroup[i].split(",");
					for (int j = 0; j < samples.length; j++) {
						int index = new Integer(samples[j]);
						avg_group += new Double(split[index]);						
					}
					avg_group = avg_group / samples.length;
					out.write("\t" + avg_group);
				}
				out.write("\n");
				
			}
			in.close();
			out.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
