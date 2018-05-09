package metagenomics.qiime.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert the 
 * @author tshaw
 *
 */
public class GenerateQIIMEMatrixTable {

	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							

			HashMap map = new HashMap();
			String metaInputFile = args[2];
			FileInputStream fstream = new FileInputStream(metaInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[3]);
			}
			in.close();
			String inputFile = args[0];
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			boolean header = true;
			while (in.ready()) {
				String str = in.readLine();
				
				if (header) {
					String[] split2 = str.split("\t");
					String add = (String)map.get(split2[1]);
					out.write(split2[split2.length - 1] + "\t" + split2[1] + "_" + add);
					for (int i = 2; i < split2.length - 1; i++) {
						add = (String)map.get(split2[i]);
						out.write("\t" + split2[i] + "_" + add);
					}
				} else {
					String[] split2 = str.split("\t");
					//String name = convert(split2[split2.length - 1]); 
					out.write(split2[split2.length - 1] + "\t" + split2[0]);
					for (int i = 1; i < split2.length - 1; i++) {
						out.write("\t" + split2[i]);
					}
				}
				header = false;
				out.write("\n");
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
