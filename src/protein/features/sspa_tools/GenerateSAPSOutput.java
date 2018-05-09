package protein.features.sspa_tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import misc.CommandLine;

/**
 * 
 * @author tshaw
 *
 */
public class GenerateSAPSOutput {

	public static String parameter_info() {
		return "[inputFile] [outputFolder] [SAPSOutputFolder] [outputFolderList] [species]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFolder = args[1];
			String SAPSOutputFolder = args[2];
			String outputFileList = args[3];
			String species = args[4];
			File folder = new File(SAPSOutputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			boolean write = false;
			String geneName = "";
			
			String outputFile = "temp";

        	FileWriter fwriter2 = new FileWriter(outputFileList);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.close();
            File f = new File("temp");
            f.delete();
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String original_str = str;
				str = str.replaceAll("\t", " ");
				while (str.contains("  ")) {
					str = str.replaceAll("\t", " ");
					str = str.replaceAll("\r", "");
					str = str.replaceAll("\n", "");
					str = str.replaceAll("  ", " ");					
				}
				String[] split = str.split(" ");
				if (str.length() >= 3) {
					if (str.substring(0, 3).equals("ID ") && str.contains(species)) {
						//System.out.println(str);
						geneName = split[1];
						write = true;						
						fwriter = new FileWriter(outputFolder + "/" + geneName + ".dat");
						out = new BufferedWriter(fwriter);
						//out2.write(outputFolder + "/" + geneName + ".output" + "\n");
						out2.write(SAPSOutputFolder + "/" + geneName + ".saps.output" + "\n");
					}
				}
				if (str.length() >= 2) {
					if (str.substring(0, 2).equals("//")) {
						write = false;
						out.close();
						
						// run saps here
						String command = "saps -s " + species + " " + outputFolder + "/" + geneName + ".dat > " + SAPSOutputFolder + "/" + geneName + ".saps.output";
						CommandLine.executeCommand(command);
					}
					
				}
				if (write) {
					out.write(original_str + "\n");
				}
			}
			in.close();
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
