package stjude.projects.jinghuizhang.hla.trust4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Check the sample name to determine and append disease type
 * @author tshaw
 *
 */
public class JinghuiZhangAppendSJDiseaseType {

	public static String description() {
		return "Check the sample name to determine disease type.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputFile] [index_name] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			int index_name = new Integer(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tType\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sjname = split[index_name];
				sjname = sjname.replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "").replaceAll("_D", "").replaceAll("_R", "").replaceAll("_M", "").replaceAll("_A", "").replaceAll("_X", "").replaceAll("_G", "");
				out.write(str + "\t" + sjname + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
