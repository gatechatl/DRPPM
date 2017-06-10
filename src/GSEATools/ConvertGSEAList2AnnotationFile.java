package GSEATools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Specialized function that converts GSEA gmt file to a regular list
 * @author tshaw
 *
 */
public class ConvertGSEAList2AnnotationFile {

	//ConvertGSEAList2AnnotationFile
	public static String description() {
		return "Specialized function that converts GSEA gmt file to a regular list";
	}
	public static String type() {
		return "GSEA";
	}
	public static String parameter_info() {
		return "[inputFIle] [outputFolder] [outputLinkForEachEntry]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String outputFolder = args[1];
			String outputReferenceLink = args[2];
			
			FileWriter fwriter2 = new FileWriter(outputReferenceLink);
			BufferedWriter out2 = new BufferedWriter(fwriter2);				
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.equals("")) {
					str = str.replaceAll(" /// ", "\t");
					String[] split = str.split("\t");
					
					for (String tag: split[0].split(",")) {
						out2.write(tag + "\t" + outputFolder + "/" + tag + "_gmt.txt\n");
						FileWriter fwriter = new FileWriter(outputFolder + "/" + tag + "_gmt.txt");
						BufferedWriter out = new BufferedWriter(fwriter);				
						for (int i = 1; i < split.length; i++) {
							out.write(split[i] + "\n");
						}
						out.close();
					}
				}
			}
			in.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
