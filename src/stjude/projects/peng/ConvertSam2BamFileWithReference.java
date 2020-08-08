package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ConvertSam2BamFileWithReference {

	public static String description() {
		return "Generate script to convert sam to bam File.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[SamFileList] [referenceFastaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileLst = args[0];
			String referenceFasta = args[1];
			FileInputStream fstream = new FileInputStream(fileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				System.out.println("samtools view -bT " + referenceFasta + " " + str + " > " + str + ".bam");
				
			}
			in.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
