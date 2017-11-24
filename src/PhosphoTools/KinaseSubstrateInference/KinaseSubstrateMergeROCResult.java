package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Compare ROC Result
 * @author tshaw
 *
 */
public class KinaseSubstrateMergeROCResult {
	
	public static String description() {
		return "MergeROCResult";
	}
	public static String type() {
		return "KINASESUBSTRATE";
	}
	public static String parameter_info() {
		return "[rocFileList] [substrateCutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String rocFileList = args[0];
			int cutoff = new Integer(args[1]);
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Type\tKinase\tROC\tPositiveControl\n");
			FileInputStream fstream = new FileInputStream(rocFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length == 2) {
					String title = split[0];
					String path = split[1];
					
					FileInputStream fstream2 = new FileInputStream(path);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header2 = in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						String kinase = split2[0];
						double roc = new Double(split2[1]);
						int numPosControl = new Integer(split2[2]);
						if (numPosControl >= cutoff) {
							out.write(title + "\t" + kinase + "\t" + roc + "\t" + numPosControl + "\n");
						}
					}
					in2.close();			
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
