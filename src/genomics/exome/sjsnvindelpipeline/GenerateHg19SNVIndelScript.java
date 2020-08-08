package genomics.exome.sjsnvindelpipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Generates the script for running the St Jude snv-indel pipeline
 * @author tshaw
 *
 */
public class GenerateHg19SNVIndelScript {

	public static String description() {
		return "Generates the script for running the St Jude snv-indel pipeline";
	}
	public static String type() {
		return "snv-indel-script";
	}
	public static String parameter_info() {
		return "[inputFIle] [outputFile]\n Input file should contain the following columns:\ntumorBam,germlineBam,outputDir,fileName_D_G,fileName_G";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				String tumorBam = split[0];
				String germlineBam = split[1];				
				String outputDir = split[2];
				String sampleNameD = split[3];
				String sampleNameG = split[4];
				out.write(create_high20_script(tumorBam, germlineBam, outputDir, sampleNameD) + "\n");
				out.write(create_low_script(tumorBam, germlineBam, outputDir, sampleNameD) + "\n");
				out.write(create_germline_script(germlineBam, outputDir, sampleNameG) + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String create_high20_script(String tumorBam, String germlineBam, String outputDir, String sampleName) {
		return "sh /home/tshaw/EXONCAP/Tools/snp_high_20_tn.sh /nfs_exports/genomes/1/Homo_sapiens/Hg19/NIB/ /nfs_exports/genomes/1/Homo_sapiens/Hg19/SUPPORT/dbsnp_binary_v2.blob " + tumorBam + " " + germlineBam + " " + " " + outputDir + " " + sampleName + ".high_20.out normal";
	}
	public static String create_low_script(String tumorBam, String germlineBam, String outputDir, String sampleName) {
		return "sh /home/tshaw/EXONCAP/Tools/snp_low_tn.sh /nfs_exports/genomes/1/Homo_sapiens/Hg19/NIB/ " + tumorBam + " " + germlineBam + " " + outputDir + " " + sampleName + ".low.out normal";
	}
	public static String create_germline_script(String germlineBam, String outputDir, String sampleName) {
		return "sh /home/tshaw/EXONCAP/Tools/snp_germline.sh /nfs_exports/genomes/1/Homo_sapiens/Hg19/NIB/ /nfs_exports/genomes/1/Homo_sapiens/Hg19/SUPPORT/dbsnp_binary_v2.blob " + germlineBam + " " + " " + outputDir + " " + sampleName + ".germline.out normal";
	}
}
