package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TrimmomaticScriptGenerator {
	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Given a file list generate the script for performing fastq Trimming prior to mapping";
	}
	public static String parameter_info() {
		return "[inputFile] [TrimmomaticPath] [ADAPTORPATH] \ninputfile must list the fq1 fq2 outputfolder";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String TrimmomaticPath = args[1];			
			String ADAPTORPATH = args[2];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fq1 = split[0];
				String fq2 = split[1];
				System.out.println(TrimmomaticScript(fq1, fq2, TrimmomaticPath, ADAPTORPATH));
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String TrimmomaticScript(String fq1, String fq2, String TrimmomaticPath, String ADAPTORPATH) {
		return "java -jar " + TrimmomaticPath + " PE -threads 8 -phred33 " + fq1 + " " + fq2 + " " + fq1 + ".trim.fastq " + fq1 + ".unpaired.trim.fastq " + fq2 + ".trim.fastq " + fq2 + ".unpaired.trim.fastq ILLUMINACLIP:" + ADAPTORPATH + "/NexteraPE-PE.fa:2:30:10 TRAILING:25 LEADING:25 SLIDINGWINDOW:4:20 MINLEN:40";
	}
}
