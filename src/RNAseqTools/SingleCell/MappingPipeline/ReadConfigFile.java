package RNAseqTools.SingleCell.MappingPipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ReadConfigFile {
	public static String STARPATH = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/bin/STAR"; // St jude's human path
	public static String STARINDEX = "/nfs_exports/genomes/1/Homo_sapiens/GRCh37-lite/STAR/GRCh37p13_GenCode19"; // St Jude's human index path
	public static String STARINDEX_MM9 = "/nfs_exports/genomes/1/Mus_musculus/mm9/STAR"; // St Jude's human index path
	public static String TRIMMOMATICPATH = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/Trimmomatic-0.36/trimmomatic-0.36.jar";
	public static String ADAPTORPATH = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/Trimmomatic-0.36/adapters";
	public static String BSUBCMDFILE = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/shellscripts/bsub_array_for_cmdfile.sh";
	public static String TRIMMOMATICSCRIPTSHELL = "TRIMMOMATIC_Script.sh";
	public static String STARSCRIPTSHELL = "STAR_Script.sh";
	public static String BAMLST = "bam.lst";
	public static String HTSEQPERL = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/perlscript/getSingleCommand.pl";
	public static String HTSEQRPM = "htseq_rpm.txt";
	public static String HTSEQFPKM = "htseq_fpkm.txt";
	public static String HTSEQFPKMGENENAME = "htseq_fpkm_geneName.txt";
	public static String HTSEQZEROBINNING = "htseq_zero_binning.txt";
	public static String HTSEQZEROBINNINGPNG = "htseq_zero_binning.txt.png";
	public static String HTSEQFPKMBINNING = "htseq_binning.txt";	
	public static String GTFFILE = "/nfs_exports/apps/internal/rnaseq/tshaw/RNASEQ_Tools/GTF/gencode.v19.annotation_for_cufflink.gtf";
	public static String STARSUMMARYFILE = "STARMappingSummaryFile.txt";
	
	public static String HTSEQFPKMGENENAMECLEANED = "htseq_fpkm_geneName_cleaned.txt";
	public static String HTSEQFPKMGENENAMECLEAENDMEDIAN = "htseq_fpkm_geneName_cleaned_median.txt";
	public static String HTSEQFPKMGENENAMECLEAENDMEDIANPNG = "htseq_fpkm_geneName_cleaned_median.txt.png";
	public static String HEATMAPR = "mad_heatmap.r";
	public static void readFile(String inputFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				str = str.replaceAll(" ", "");	
				str = str.split("#")[0];
				String[] split = str.split("=");
				if (split.length > 1) {
					split[1].replaceAll("\"", "");
					if (split[0].equals("STARPATH")) {
						STARPATH = split[1];
					}
					if (split[0].equals("TRIMMOMATICPATH")) {
						TRIMMOMATICPATH = split[1];
					}
					if (split[0].equals("ADAPTORPATH")) {
						ADAPTORPATH = split[1];
					}
					if (split[0].equals("TRIMMOMATICSCRIPTSHELL")) {
						TRIMMOMATICSCRIPTSHELL = split[1];
					}
					if (split[0].equals("STARINDEX")) {
						STARINDEX = split[1];
					}
					if (split[0].equals("STARSUMMARYFILE")) {
						STARSUMMARYFILE = split[1];
					}
					if (split[1].equals("BSUBCMDFILE")) {
						BSUBCMDFILE = split[1];
					}
					if (split[1].equals("STARSCRIPTSHELL")) {
						STARSCRIPTSHELL = split[1];
					}
					if (split[1].equals("BAMLST")) {
						BAMLST = split[1];
					}
					if (split[1].equals("HTSEQPERL")) {
						HTSEQPERL = split[1];
					}
					if (split[1].equals("HTSEQRPM")) {
						HTSEQRPM = split[1];
					}
					if (split[1].equals("GTFFILE")) {
						GTFFILE = split[1];
					}
					if (split[1].equals("HTSEQFPKMGENENAME")) {
						HTSEQFPKMGENENAME = split[1];
					}
					if (split[1].equals("HTSEQZEROBINNING")) {
						HTSEQZEROBINNING = split[1];
					}
					if (split[1].equals("HTSEQFPKMBINNING")) {
						HTSEQFPKMBINNING = split[1];
					}
					if (split[1].equals("HTSEQZEROBINNINGPNG")) {
						HTSEQZEROBINNINGPNG = split[1];
					}
					if (split[1].equals("HTSEQFPKMGENENAMECLEANED")) {
						HTSEQFPKMGENENAMECLEANED = split[1];
					}
					if (split[1].equals("HTSEQFPKMGENENAMECLEAENDMEDIAN")) {
						HTSEQFPKMGENENAMECLEAENDMEDIAN = split[1];
					}
					if (split[1].equals("HTSEQFPKMGENENAMECLEAENDMEDIANPNG")) {
						HTSEQFPKMGENENAMECLEAENDMEDIANPNG = split[1];
					}
					if (split[1].equals("HEATMAPR")) {
						HEATMAPR = split[1];
					}
					if (split[1].equals("STARINDEX_MM9")) {
						STARINDEX_MM9 = split[1];
					}
				} // check split.length size
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
