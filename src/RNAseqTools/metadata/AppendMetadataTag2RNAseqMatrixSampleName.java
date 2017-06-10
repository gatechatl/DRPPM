package RNAseqTools.metadata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Append the meta data to the sampleName of the matrix.
 * @author tshaw
 *
 */
public class AppendMetadataTag2RNAseqMatrixSampleName {

	public static String dependencies() {
		return "";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Append the meta data to the sampleName of the matrix.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [metaFile] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String metaFile = args[1];
			String outputFile = args[2];			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(metaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);				
			}
			in.close();						
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] header_split = header.split("\t");
			out.write(header_split[0]);
			for (int i = 1; i < header_split.length; i++) {
				if (map.containsKey(header_split[i])) {
					String meta = (String)map.get(header_split[i]);
					out.write("\t" + header_split[i] + "_" + meta);
				} else {
					out.write("\t" + header_split[i]);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
			}
			in.close();						
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
