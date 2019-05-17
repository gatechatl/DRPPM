package mappingtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Convert Bam back to fastq
 * @author tshaw
 *
 */
public class Bam2Fastq {

	public static String type() {
		return "FASTQ";
	}
	public static String description() {
		return "Convert bam file to fastq file. Input file should contain two column with the first column being the sample name and the second column is the file path. The ouput shell script contains the list shell script per sample.";
	}
	public static String parameter_info() {
		return "[inputFile: sampleName [tab] filepath] [summary_outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String summary_outputFile = args[1];
			FileWriter fwriter = new FileWriter(summary_outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String file = split[1];
				out.write("sh " + sampleName + ".sh\n");
				FileWriter fwriter2 = new FileWriter(sampleName + ".sh");
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				out2.write("samtools view -u -f 1 -F 12 " + file + " > " + sampleName + "_map_map.bam\n");
				out2.write("samtools view -u -f 4 -F 264 " + file + " > " + sampleName + "_unmap_map.bam\n");
				out2.write("samtools view -u -f 8 -F 260 " + file + " > " + sampleName + "_map_unmap.bam\n");
				out2.write("samtools view -u -f 12 -F 256 " + file + " > " + sampleName + "_unmap_unmap.bam\n");
				out2.write("samtools merge -u " + sampleName + "_unmapped.bam " + sampleName + "_unmap_map.bam " + sampleName + "_map_unmap.bam " + sampleName + "_unmap_unmap.bam\n");
				out2.write("samtools sort -n " + sampleName + "_map_map.bam " + sampleName + "_mapped.sort\n");
				out2.write("samtools sort -n " + sampleName + "_unmapped.bam " + sampleName + "_unmapped.sort\n");
				out2.write("bamToFastq -i " + sampleName + "_mapped.sort.bam " + "-fq " + sampleName + "_mapped.1.fastq -fq2 " + sampleName + "_mapped.2.fastq\n");
				out2.write("bamToFastq -i " + sampleName + "_unmapped.sort.bam " + "-fq " + sampleName + "_unmapped.1.fastq -fq2 " + sampleName + "_unmapped.2.fastq\n");
				out2.write("cat " + sampleName + "_mapped.1.fastq " + sampleName + "_unmapped.1.fastq > " + sampleName + ".R1.fastq\n");
				out2.write("cat " + sampleName + "_mapped.2.fastq " + sampleName + "_unmapped.2.fastq > " + sampleName + ".R2.fastq\n");
				out2.close();
				
			}
			in.close();			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
