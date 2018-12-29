package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CuffLinksScriptGenerator {

	/*cufflinks -p 8 --library-type fr-unstranded -o CD4NaiveERR431571 -G /nfs_exports/genomes/1/Homo_sapiens/GRCh37-lite/STAR/GRCh37p13_GenCode19/gencode.v19.annotation_for_cufflink.gtf ../mapping/STARMapping/CD4NaiveERR431571Aligned.sortedByCoord.out.bam*/
	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Given a file list generate the stock script for performing STAR mapping";
	}
	public static String parameter_info() {
		return "[inputFile] [cuffLinksPath] [gtfFile] [combineFileShell] [cuffLinksShell]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String cuffLinksPath = args[1];
			String gtfFile = args[2];	
			String combineFileShell = args[3];
			String cuffLinksShell = args[4];

			FileWriter fwriter2 = new FileWriter(cuffLinksShell);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            
			FileWriter fwriter = new FileWriter(combineFileShell);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("drppm -ExtractCufflinksFPKM SortedTCell_CufflinksFPKM_20150619.txt 9 4 ");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fq1 = split[0];
				String fq2 = split[1];
				String sampleName = split[2];
				String bamFile = sampleName + "Aligned.sortedByCoord.out.bam";
				String outputFolder = sampleName + "_CuffLinks";
				out.write(" " + sampleName + ":" + outputFolder + "/genes.fpkm_tracking");
				//System.out.println(CuffLinksScript(bamFile, cuffLinksPath, gtfFile, outputFolder));
				out2.write(CuffLinksScript(bamFile, cuffLinksPath, gtfFile, outputFolder));
			}
			in.close();
			out.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String CuffLinksScript(String bamFile, String CuffLinksPath, String gtfFile, String outputFolder) {
		return CuffLinksPath + " -p 8 --library-type fr-firststrand -o " + outputFolder + " -G " + gtfFile + " " + bamFile + "\n";
	}
}
