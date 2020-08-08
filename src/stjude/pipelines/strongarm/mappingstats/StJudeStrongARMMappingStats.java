package stjude.pipelines.strongarm.mappingstats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class StJudeStrongARMMappingStats {

	public static String type() {
		return "StJude";
	}
	public static String description() {
		return "Generate a mapping statistics table based on sampleName";
	}
	public static String parameter_info() {
		return "[sampleName list] [project_path] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String sampleName_file = args[0]; // sampleName.txt 
			String project_path = args[1]; // /rgs01/resgen/prod/tartan/index/data/XiaotuMa/AMLFredHutch/
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tTotalReads\tReadsMapped\tNonDupMapped\tMpd%\tDup%\tExon1x\tExon2x\tExon5x\tExon10x\tExon20x\tExon30x\tExon40x\tExon45x\tIntron1x\tIntron2x\tIntron5x\tIntron10x\tIntron20x\tIntron30x\tIntron40x\tIntron45x\tBamFilePath\tRunCICERO?\n");
			
			FileInputStream fstream = new FileInputStream(sampleName_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				
				
				String flagstat_file = project_path + "/" + sampleName + "/TRANSCRIPTOME/bam/" + sampleName + ".flagstat.txt";
				String exonFile = project_path + "/" + sampleName + "/TRANSCRIPTOME/coverage-post/" + sampleName + ".exon.cumulative.txt";
				String intronFile = project_path + "/" + sampleName + "/TRANSCRIPTOME/coverage-post/" + sampleName + ".intron.cumulative.txt";
				
				File f1 = new File(flagstat_file);
				File f2 = new File(exonFile);
				File f3 = new File(intronFile);
				
				if (f1.exists() && f2.exists() && f3.exists()) {
					out.write(sampleName);
					FLAGSTAT flagstat = getStats(flagstat_file, sampleName);
					flagstat.createMap();
					out.write("\t" + flagstat.MAP.get("TOTAL_READS") + "\t" + flagstat.MAP.get("MAPPED") + "\t" + flagstat.MAP.get("NONDUPS_MAPPED") + "\t" + flagstat.MAP.get("PERCENT_MAPPED") + "\t" + flagstat.MAP.get("PERCENT_DUPS")); 
					FileInputStream fstream2 = new FileInputStream(flagstat_file);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						
					}
					in2.close();
					
					//String codingExonFile = project_path + "/" + sampleName + "/TRANSCRIPTOME/coverage-post/" + sampleName + ".codingexon.cumulative.txt";
					
					fstream2 = new FileInputStream(exonFile);
					din2 = new DataInputStream(fstream2);
					in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						str2 = in2.readLine();
						str2 = in2.readLine();
						str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						out.write("\t" + split2[1] + "\t" + split2[2] + "\t" + split2[3] + "\t" + split2[4] + "\t" + split2[5] + "\t" + split2[6] + "\t" + split2[7] + "\t" + split2[8]);
					}
					in2.close();
					
					//String codingExonFile = project_path + "/" + sampleName + "/TRANSCRIPTOME/coverage-post/" + sampleName + ".codingexon.cumulative.txt";
									
					fstream2 = new FileInputStream(intronFile);
					din2 = new DataInputStream(fstream2);
					in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						str2 = in2.readLine();
						str2 = in2.readLine();
						str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						out.write("\t" + split2[1] + "\t" + split2[2] + "\t" + split2[3] + "\t" + split2[4] + "\t" + split2[5] + "\t" + split2[6] + "\t" + split2[7] + "\t" + split2[8]);
					}
					in2.close();
					boolean ran_cicero = false;
					File f = new File(project_path + "/" + sampleName + "/TRANSCRIPTOME/cicero-post/final_fusions-event_fusion.txt");
					if (f.exists()) {
						ran_cicero = true;
					}
					String bam_file_path = project_path + "/" + sampleName + "/TRANSCRIPTOME/bam/" + sampleName + ".bam";
					bam_file_path = bam_file_path.replaceAll("//", "/");
					out.write("\t" + bam_file_path + "\t" + ran_cicero + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FLAGSTAT getStats(String fileName, String name) {
		FLAGSTAT stat = new FLAGSTAT();
		String lastLine = "";
		try {
			
			int count = 0;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				count++;
			}
			in.close();
			
			if (count == 12) {
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					
					stat.NAME = name;
					
					stat.TOTAL = in.readLine().split(" ")[0];					
					stat.QC_FAILURE = in.readLine().split(" ")[0];
					stat.DUPLICATES = in.readLine().split(" ")[0];
					stat.MAPPED = in.readLine().split(" ")[0];
					stat.PAIRED = in.readLine().split(" ")[0];
					stat.READ1 = in.readLine().split(" ")[0];
					stat.READ2 = in.readLine().split(" ")[0];
					stat.MAPPED_PAIRED = in.readLine().split(" ")[0];
					stat.ITSELF_MATE_MAPPED = in.readLine().split(" ")[0];
					stat.SINGLETON = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR_5MAPQ = in.readLine().split(" ")[0];
					
					
				}
				in.close();
			} else if (count == 11) {
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					
					stat.NAME = name;
					
					//stat.QC_FAILURE = in.readLine().split(" ")[0];
					stat.TOTAL = in.readLine().split(" ")[0];
					stat.DUPLICATES = in.readLine().split(" ")[0];
					stat.MAPPED = in.readLine().split(" ")[0];
					stat.PAIRED = in.readLine().split(" ")[0];
					stat.READ1 = in.readLine().split(" ")[0];
					stat.READ2 = in.readLine().split(" ")[0];
					stat.MAPPED_PAIRED = in.readLine().split(" ")[0];
					stat.ITSELF_MATE_MAPPED = in.readLine().split(" ")[0];
					stat.SINGLETON = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR_5MAPQ = in.readLine().split(" ")[0];
					
					
				}
				in.close();
			} else if (count == 13) {
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					
					stat.NAME = name;
					
					//stat.QC_FAILURE = in.readLine().split(" ")[0];
					stat.TOTAL = in.readLine().split(" ")[0];
					stat.SECONDARY = in.readLine().split(" ")[0];
					stat.SUPPLEMENTARY = in.readLine().split(" ")[0];
					stat.DUPLICATES = in.readLine().split(" ")[0];
					stat.MAPPED = in.readLine().split(" ")[0];
					stat.PAIRED = in.readLine().split(" ")[0];
					stat.READ1 = in.readLine().split(" ")[0];
					stat.READ2 = in.readLine().split(" ")[0];
					stat.MAPPED_PAIRED = in.readLine().split(" ")[0];
					stat.ITSELF_MATE_MAPPED = in.readLine().split(" ")[0];
					stat.SINGLETON = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR = in.readLine().split(" ")[0];
					stat.MATE_MAPPED_TO_DIFF_CHR_5MAPQ = in.readLine().split(" ")[0];					
					
				}
				in.close();
			} else {
				System.out.println("QC File: " + fileName + " is not in the expected format");
			}
		} catch (Exception e) {
			System.out.println("Failed to read: " + fileName);
			System.out.println(lastLine);
			e.printStackTrace();
		}
		return stat;
	}
	static class FLAGSTAT {

		HashMap MAP = new HashMap();
		String NAME = "";
		String TOTAL = "";
		String PERCENT_MAPPED = "";
		String NONDUPS_MAPPED = "";
		String PERCENT_DUPS = "";
		String QC_FAILURE = "";
		String DUPLICATES = "";
		String MAPPED = "";
		String PAIRED = "";
		String READ1 = "";
		String READ2 = "";
		String MAPPED_PAIRED = "";
		String ITSELF_MATE_MAPPED = "";
		String SINGLETON = "";
		String MATE_MAPPED_TO_DIFF_CHR = "";
		String MATE_MAPPED_TO_DIFF_CHR_5MAPQ = "";
		String SECONDARY = "";
		String SUPPLEMENTARY = "";
		String CODING1X = "";
		String CODING2X = "";
		String CODING5X = "";
		String CODING10X = "";
		String CODING20X = "";
		String CODING30X = "";
		
		String INTRON1X = "";
		String INTRON2X = "";
		String INTRON5X = "";
		String INTRON10X = "";
		String INTRON20X = "";
		String INTRON30X = "";
		
		public void createMap() {
			MAP.put("TOTAL_READS", TOTAL);
			MAP.put("QC_FAILURE", QC_FAILURE);
			MAP.put("DUPLICATES", DUPLICATES);
			MAP.put("MAPPED", MAPPED);
			MAP.put("PAIRED", PAIRED);
			MAP.put("READ1", READ1);
			MAP.put("READ2", READ2);
			MAP.put("MAPPED_PAIRED", MAPPED_PAIRED);
			MAP.put("ITSELF_MATE_MAPPED", ITSELF_MATE_MAPPED);
			MAP.put("SINGLETON", SINGLETON);
			MAP.put("MATE_MAPPED_TO_DIFF_CHR", MATE_MAPPED_TO_DIFF_CHR);
			MAP.put("MATE_MAPPED_TO_DIFF_CHR_5MAPQ", MATE_MAPPED_TO_DIFF_CHR_5MAPQ);
			MAP.put("CODING1X", CODING1X);
			MAP.put("CODING2X", CODING2X);
			MAP.put("CODING5X", CODING5X);
			MAP.put("CODING10X", CODING10X);
			MAP.put("CODING20X", CODING20X);
			MAP.put("CODING30X", CODING30X);
			MAP.put("INTRON1X", INTRON1X);
			MAP.put("INTRON2X", INTRON2X);
			MAP.put("INTRON5X", INTRON5X);
			MAP.put("INTRON10X", INTRON10X);
			MAP.put("INTRON20X", INTRON20X);
			MAP.put("INTRON30X", INTRON30X);
			
			double percent_mapped = new Double(MAPPED) / new Double(TOTAL);
			double nondups_mapped = new Double(MAPPED) - new Double(DUPLICATES);
			double percent_dups = new Double(DUPLICATES) / new Double(MAPPED);
			PERCENT_MAPPED = new Double(percent_mapped).toString();
			NONDUPS_MAPPED = new Double(nondups_mapped).toString();
			PERCENT_DUPS = new Double(percent_dups).toString();
			MAP.put("PERCENT_MAPPED", PERCENT_MAPPED);
			MAP.put("NONDUPS_MAPPED", NONDUPS_MAPPED);
			MAP.put("PERCENT_DUPS", PERCENT_DUPS);
		}
	}
}
