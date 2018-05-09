package metagenomics.qiime.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Convert the 
 * @author tshaw
 *
 */
public class GenerateJasonMatrixTable {

	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							
			int index = 1;
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			boolean header = true;
			while (in.ready()) {
				String str = in.readLine();
				
				if (header) {
					String[] split2 = str.split("\t");
					out.write("OTU\t" + split2[0]);
					for (int i = 1; i < split2.length; i++) {
						out.write("\t" + split2[i]);
					}
				} else {
					String[] split2 = str.split("\t");
					//String name = convert(split2[split2.length - 1]); 
					//out.write(split2[0]);
					out.write("Index" + index);
					for (int i = 1; i < split2.length; i++) {
						out.write("\t" + split2[i]);
					}
				}
				header = false;
				out.write("\n");
				index++;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String convert(String stuff) {
		String result = "";
		String[] split = stuff.split("; ");
		for (int i = 0; i < split.length; i++) {
			String id = split[i].split("_")[split[i].split("_").length - 1];
			result += id + "_";
		}
		return result;
	}
}
