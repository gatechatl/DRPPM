package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SortBamFiles {

	public static String description() {
		return "Sort the bam file.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[BamFileList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileLst = args[0];
			FileInputStream fstream = new FileInputStream(fileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				System.out.println("samtools sort " + str + " " + str.substring(0, str.length() - 4) + ".sorted");
				
			}
			in.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
