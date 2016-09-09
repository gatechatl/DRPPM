package RNATools.PCPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Specialized class for appending pcpa result together (instead of cat)
 * @author tshaw
 *
 */
public class CombinePCPAResults {

	public static String type() {
		return "PCPA";
	}
	public static String description() {
		return "A specialized class for appending pcpa result that will also attach the filename that it came from.";
	}
	public static String parameter_info() {
		return "[folderLoc1] [folderLoc2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folderLoc = args[0];
			String folderLoc2 = args[1];
			String outputFile = args[2];
			
			File outputFileF = new File(outputFile);
			if (outputFileF.exists()) {
				System.out.println(outputFile + " File already exist");
				System.exit(0);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			File folder = new File(folderLoc);
			for (File f: folder.listFiles()) {
				if (f.getName().contains(".dist")) {
					FileInputStream fstream = new FileInputStream(f.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));			
					while (in.ready()) {
						String str = in.readLine();						
						out.write(str + "\t" + f.getName() + "\n");
					}
					in.close();
				}
			}
			
			folder = new File(folderLoc2);
			for (File f: folder.listFiles()) {
				if (f.getName().contains(".dist")) {
					FileInputStream fstream = new FileInputStream(f.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));			
					while (in.ready()) {
						String str = in.readLine();						
						out.write(f.getName() + "\t" + str + "\n");
					}
					in.close();
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
