package PhosphoTools.KinaseActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Normalize the phospho values against the whole proteome
 * @author tshaw
 *
 */
public class NormalizePhosphoAgainstWholeWithOffset {

	public static String description() {
		return "Normalize the phospho values against the whole proteome";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[phosphoFile] [wholeFile] [offset: 0.0 to 1.0] [outputFile] [kinase annotation flag true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			String phosphoFile = args[0];
			String wholeFile = args[1];
			double offset = new Double(args[2]);
			String outputFile = args[3];
			boolean kinase_annotation_flag = new Boolean(args[4]);
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(wholeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], str);
				
			}
			in.close();
			
			fstream = new FileInputStream(phosphoFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				
				if (map.containsKey(geneName)) {
					out.write(split[0]);
					String line = (String)map.get(geneName);
					String[] split2 = line.split("\t");
					double[] pho = new double[10];
					int j = 0;
					if (kinase_annotation_flag) {
						for (int i = split.length - 11; i < split.length - 1; i++) {
							pho[j] = new Double(split[i]);
							j++;
						}
					} else {
						for (int i = split.length - 10; i < split.length; i++) {
							pho[j] = new Double(split[i]);
							j++;
						}
					}
					
					j = 0;
					for (int i = split2.length - 10; i < split2.length; i++) {
						double norm = pho[j] - new Double(split2[i]) * offset;
						j++;
						out.write("\t" + norm);
					}
					// write the kinase annotation
					if (kinase_annotation_flag) {
						out.write("\t" + split[split.length - 1] + "\n");
					} else {
						out.write("\n");
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
