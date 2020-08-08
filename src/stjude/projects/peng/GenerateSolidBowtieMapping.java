package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateSolidBowtieMapping {

	public static String description() {
		return "Generate the bowtie.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[fastqFileList] [indexPath]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileLst = args[0];
			String indexPath = args[1];
			FileInputStream fstream = new FileInputStream(fileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				System.out.println("bowtie -S -C " + indexPath + " -q " + str + " > " + str + ".sam");
				
			}
			in.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
