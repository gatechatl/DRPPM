package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Bam2FQ {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Convert bam to fq";
	}
	public static String parameter_info() {
		return "[inputPathList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFileBedTools = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			

			FileWriter fwriterBedTools = new FileWriter(outputFileBedTools);
			BufferedWriter outBedTools = new BufferedWriter(fwriterBedTools);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String filePath = split[0];
				String fileName = split[1];
				File bamList = new File(filePath + "/ubam/1/");
				String fq1 = "";
				String fq2 = "";
				for (File f: bamList.listFiles()) {
					if (fq1.equals("")) {
						fq1 = f.getName() + "_1.fq";
						fq2 = f.getName() + "_2.fq";
					} else {
						fq1 += "," + f.getName() + "_1.fq";
						fq2 += "," + f.getName() + "_2.fq";
					}
					//System.out.println("bedtools bamtofastq -i " + f.getPath() + " -fq " + f.getName() + "_1.fq" + " -fq2 " + f.getName() + "_2.fq");
					outBedTools.write("bedtools bamtofastq -i " + f.getPath() + " -fq " + f.getName() + "_1.fq" + " -fq2 " + f.getName() + "_2.fq" + "\n");
				}
				out.write(getSTAR(fq1, fq2, fileName) + "\n");
			}
			in.close();
			out.close();
			outBedTools.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getSTAR(String fq1, String fq2, String outputFolder) {
		return "/home/tshaw/RNASEQ/Tools/STAR-master/bin/Linux_x86_64_static/STAR --outSAMstrandField intronMotif --genomeDir /nfs_exports/apps/internal/rnaseq/tshaw/XenoGraphReference/STAR/star-genome-xenograph-ercc/ --readFilesIn " + fq1 + " " + fq2 + " --genomeLoad NoSharedMemory --runThreadN 8 --chimSegmentMin 20 --chimJunctionOverhangMin 20 --outFileNamePrefix " + outputFolder + " --outSAMunmapped Within --outSAMtype BAM SortedByCoordinate";
	}
}
