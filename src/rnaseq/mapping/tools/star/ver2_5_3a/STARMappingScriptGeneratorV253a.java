package rnaseq.mapping.tools.star.ver2_5_3a;

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
public class STARMappingScriptGeneratorV253a {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Given a file list generate the script for performing STAR mapping for version 2.5.3a";
	}
	public static String parameter_info() {
		return "[inputFile] [STARPath] [refPath] [gtfFile: optional]\ninputfile must list the fq1 fq2 outputfolder";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String STARPath = args[1];
			String refPath = args[2];
			String gtfFile = args[3];
			/*String gtfFile = "";
			if (args.length > 3) {
				gtfFile = args[3];
			}*/
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fq1 = split[0];
				String fq2 = split[1];
				String outputFolder = split[2];
				System.out.println(STARScript(fq1, fq2, STARPath, refPath, gtfFile, outputFolder));
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String STARScript(String fq1, String fq2, String STARPath, String refPath, String gtfFile, String outputFolder) {
		File f = new File(fq1);
				
		/*String gtfFileScript = "";
		if (!gtfFile.equals("")) {
			gtfFileScript = "--sjdbGTFfile " + gtfFile;
		}*/
		return STARPath + " --genomeDir " + refPath + " --readFilesIn " + fq1 + " " + fq2 + " --runThreadN 8 --outFilterMultimapScoreRange 1 --outFilterMultimapNmax 20 --outFilterMismatchNmax 10 --alignIntronMax 500000 --alignMatesGapMax 1000000 --sjdbScore 2 --alignSJDBoverhangMin 1 --genomeLoad NoSharedMemory --limitBAMsortRAM 70000000000 --readFilesCommand cat --outFilterMatchNminOverLread 0.33 --outFilterScoreMinOverLread 0.33 --sjdbOverhang 100 --outSAMstrandField intronMotif --outSAMattributes NH HI NM MD AS XS --sjdbGTFfile " + gtfFile + "  --limitSjdbInsertNsj 2000000 --outSAMunmapped None --outSAMtype BAM SortedByCoordinate --outSAMheaderHD @HD VN:1.4 --twopassMode Basic --outSAMmultNmax 1 --outFileNamePrefix " + outputFolder;
	}
}
