package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Merge the two intron retention table
 * @author tshaw
 *
 */
public class MergeIntronRetentionTable {

	
	public static String description() {
		return "Merge two Intron Retention Table based on geneID (first column)";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[fileMatrix1] [fileMatrix2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile1 = args[0];
			String inputFile2 = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("\\.")[0];
				map.put(name, str);				
			}
			in.close();
						
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header2 = in.readLine();
			out.write(header1 + "\t" + header2 + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("\\.")[0];
				if (map.containsKey(name)) {
					String orig_line = (String)map.get(name);
					out.write(orig_line + "\t" + str + "\n");
				}							
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
