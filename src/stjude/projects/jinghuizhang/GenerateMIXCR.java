package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateMIXCR {

	public static String description() {
		return "Generate MIXCR script";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[three column file (sampleName) (R1_fq) (R2_fq)]";
	}
	public static void execute(String[] args) {
		
		try {
			

			String outputFile_step1 = "step1_align_mixcr.sh";
			FileWriter fwriter_step1 = new FileWriter(outputFile_step1);
			BufferedWriter out_step1 = new BufferedWriter(fwriter_step1);			
			
			String outputFile_step2 = "step2_assemble_partial.sh";
			FileWriter fwriter_step2 = new FileWriter(outputFile_step2);
			BufferedWriter out_step2 = new BufferedWriter(fwriter_step2);			
			
			String outputFile_step3 = "step3_assemble_partial2.sh";
			FileWriter fwriter_step3 = new FileWriter(outputFile_step3);
			BufferedWriter out_step3 = new BufferedWriter(fwriter_step3);			
			
			String outputFile_step4 = "step4_extend_alignments.sh";
			FileWriter fwriter_step4 = new FileWriter(outputFile_step4);
			BufferedWriter out_step4 = new BufferedWriter(fwriter_step4);						
			
			String outputFile_step5 = "step5_final_assembly.sh";
			FileWriter fwriter_step5 = new FileWriter(outputFile_step5);
			BufferedWriter out_step5 = new BufferedWriter(fwriter_step5);						
			
			String outputFile_step6 = "step6_TRA_export_clones.sh";
			FileWriter fwriter_step6 = new FileWriter(outputFile_step6);
			BufferedWriter out_step6 = new BufferedWriter(fwriter_step6);						
			
			String outputFile_step7 = "step7_TRB_export_clones.sh";
			FileWriter fwriter_step7 = new FileWriter(outputFile_step7);
			BufferedWriter out_step7 = new BufferedWriter(fwriter_step7);						
			
			String outputFile_step8 = "step8_TRD_export_clones.sh";
			FileWriter fwriter_step8 = new FileWriter(outputFile_step8);
			BufferedWriter out_step8 = new BufferedWriter(fwriter_step8);						
			
			String outputFile_step9 = "step9_TRG_export_clones.sh";
			FileWriter fwriter_step9 = new FileWriter(outputFile_step9);
			BufferedWriter out_step9 = new BufferedWriter(fwriter_step9);						
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String R1_fq = split[1];
				String R2_fq = split[2];
				
				out_step1.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar align -p rna-seq -OallowPartialAlignments=true --report " + name + ".alignmentReport.log " + R1_fq + " " + R2_fq + " " + name + ".alignments.vdjca\n");
				out_step2.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar assemblePartial --report " + name + ".assembleReport.log " + name + ".alignments.vdjca " + name + ".alignments2.vdjca\n");
				out_step3.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar assemblePartial --report " + name + ".assembleReport2.log " + name + ".alignments2.vdjca " + name + ".alignments3.vdjca\n");
				out_step4.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar extendAlignments --report " + name + ".extendAlignment.log " + name + ".alignments3.vdjca " + name + ".alignments3.extended.vdjca\n");
				out_step5.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar assemble --report " + name + ".finalAlignment.log " + name + ".alignments3.extended.vdjca " + name + ".clones.clns\n");
				out_step6.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar exportClones -c TRA " + name + ".clones.clns " + name + ".TRA.clones.txt\n");
				out_step7.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar exportClones -c TRB " + name + ".clones.clns " + name + ".TRB.clones.txt\n");
				out_step8.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar exportClones -c TRD " + name + ".clones.clns " + name + ".TRD.clones.txt\n");
				out_step9.write("java -jar ~/RNASEQ/Tools/MiXCR/mixcr-2.1.5/mixcr.jar exportClones -c TRG " + name + ".clones.clns " + name + ".TRG.clones.txt\n");
			}
			in.close();
			out_step1.close();
			out_step2.close();
			out_step3.close();
			out_step4.close();
			out_step5.close();
			out_step6.close();
			out_step7.close();
			out_step8.close();
			out_step9.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
