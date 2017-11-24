package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Given a file list generate the stock script for performing STAR mapping
 * The file list must list the fq1 fq2 outputfolder
 * @author tshaw
 *
 */
public class STARMappingScriptGenerator {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Given a file list generate the script for performing STAR mapping";
	}
	public static String parameter_info() {
		return "[inputFile] [STARPath] [refPath]\ninputfile must list the fq1 fq2 outputfolder";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String STARPath = args[1];
			String refPath = args[2];			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fq1 = split[0];
				String fq2 = split[1];
				String outputFolder = split[2];
				System.out.println(STARScript(fq1, fq2, STARPath, refPath, outputFolder));
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String STARScript(String fq1, String fq2, String STARPath, String refPath, String outputFolder) {
		File f = new File(fq1);
		String addCompressionTag = ""; 
		if (f.getName().substring(f.getName().length() - 2, f.getName().length()).equals("gz")) {
			addCompressionTag = "--readFilesCommand gzip -c";
		}		
		return STARPath + " --outSAMstrandField intronMotif --genomeDir " + refPath + " --readFilesIn " + fq1 + " " + fq2 + " --genomeLoad NoSharedMemory --runThreadN 8 --chimSegmentMin 20 --chimJunctionOverhangMin 20 --outFileNamePrefix " + outputFolder + " --outSAMunmapped Within --outSAMtype BAM SortedByCoordinate " + addCompressionTag;
	}
}
