package rnaseq.mapping.tools.bw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * This assumes that the library prep was using reverse
 * Reference: https://deeptools.readthedocs.io/en/2.1.1/content/tools/bamCoverage.html
 * @author tshaw
 *
 */
public class Bam2StrandedBW {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Convert bam to bw reverse stranded";
	}
	public static String parameter_info() {
		return "[inputBamFileList] [outputDirectory] [outputShellScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String bamfileList = args[0];
			String outputDirectory = args[1];
			String outputRunSh = args[2];
			
			FileWriter fwriter_run = new FileWriter(outputRunSh);
			BufferedWriter out_run = new BufferedWriter(fwriter_run);
			
			FileInputStream fstream = new FileInputStream(bamfileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				File f = new File(str);
				String sampleName = f.getName().replaceAll(".bam", "");
				String outputShFile = outputDirectory + "/" + sampleName + ".sh";
				out_run.write("sh " + outputShFile + "\n");
				
				FileWriter fwriter = new FileWriter(outputShFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				String forwardbam1 = outputDirectory + "/" + sampleName + ".fwd1.bam";
				String forwardbam2 = outputDirectory + "/" + sampleName + ".fwd2.bam";
				String forwardbam = outputDirectory + "/" + sampleName + ".fwd.bam";
				String forwardbigwig = outputDirectory + "/" + sampleName + ".fwd.bigWig";
				String forwardrm = outputDirectory + "/" + sampleName + ".fwd*.bam";
				out.write("samtools view -b -f 128 -F 16 " + str + " > " + forwardbam1 + "\n");
				out.write("samtools view -b -f 80 " + str + " > " + forwardbam2 + "\n");
				out.write("samtools merge -f " + forwardbam + " " + forwardbam1 + " " + forwardbam2 + "\n");
				out.write("samtools index " + forwardbam + "\n");
				out.write("bamCoverage -b " + forwardbam + " -o " + forwardbigwig + "\n");
				out.write("rm " + forwardrm + "\n");				

				String reversebam1 = outputDirectory + "/" + sampleName + ".rev1.bam";
				String reversebam2 = outputDirectory + "/" + sampleName + ".rev2.bam";
				String reversebam = outputDirectory + "/" + sampleName + ".rev.bam";
				String reversebigwig = outputDirectory + "/" + sampleName + ".rev.bigWig";
				String reverserm = outputDirectory + "/" + sampleName + ".rev*.bam";
				
				out.write("samtools view -b -f 128 -F 16 " + str + " > " + reversebam1 + "\n");
				out.write("samtools view -b -f 64 -F 16 " + str + " > " + reversebam2 + "\n");
				out.write("samtools merge -f " + reversebam + " " + reversebam1 + " " + reversebam2 + "\n");
				out.write("samtools index " + reversebam + "\n");
				out.write("bamCoverage -b " + reversebam + " -o " + reversebigwig + "\n");
				out.write("rm " + reverserm + "\n");			
				out.close();
			}
			in.close();			
			
			out_run.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
