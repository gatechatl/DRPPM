package RNAseqTools.Summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Given a list of bam files and exon bed and intron bed
 * generate the shell script that can calculate the coverage
 * @author tshaw
 *
 */
public class IntronExonCoverageBED {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Given a list of bam files and exon bed and intron bed generate the shell script that can calculate the coverage";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFolder] [intronBED] [exonBED] [shellFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFolder = args[1];
			String intronBED = args[2];
			String exonBED = args[3];
			String shellFile = args[4];
			
			FileWriter fwriter2 = new FileWriter(shellFile);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				String name = f.getName().replaceAll(".bam", "");
				out2.write("sh " + outputFolder + "/" + name + ".sh\n");
	        	FileWriter fwriter = new FileWriter(outputFolder + "/" + name + ".sh");
	            BufferedWriter out = new BufferedWriter(fwriter);
	            String script = generateScript(str.trim(), outputFolder + "/" + name, intronBED, exonBED);
	            out.write(script + "\n");
	            out.close();
			}
			in.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateScript(String inputBamFile, String outputFile, String intronBED, String exonBED) {
		String script = "bamToBed -i " + inputBamFile + " > " + outputFile + ".bed\n";
		script += "intersectBed -a " + outputFile + ".bed -b " + intronBED + " > " + outputFile + ".intron.bed\n";
		script += "intersectBed -a " + outputFile + ".bed -b " + exonBED + " > " + outputFile + ".exon.bed\n";
		//script += "coverageBed -a " + outputFile + ".intron.bed -b " + intronBED + " -hist > " + outputFile + ".bed_intron.coverageBed.txt\n";
		//script += "coverageBed -a " + outputFile + ".exon.bed -b " + exonBED + " -hist > " + outputFile + ".bed_exon.coverageBed.txt\n";
		script += "coverageBed -a " + outputFile + ".intron.bed -b " + intronBED + " > " + outputFile + ".bed_intron.coverageBed.txt\n";
		script += "coverageBed -a " + outputFile + ".exon.bed -b " + exonBED + " > " + outputFile + ".bed_exon.coverageBed.txt\n";
		return script;
	}
}
