package stjude.projects.xiaotuma.aml.rnaseq.rnaindel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class XiaotuMaGenerateRNAindelScript {


	public static String description() {
		return "Generate RNAindel script";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputBam list File] ";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				String out_vcf_file = f.getName().replaceAll(".STAR.Aligned.sortedByCoord.out.bam", ".vcf");
				System.out.println("rnaindel analysis -i " + str + " -r /research/rgs01/applications/hpcf/authorized_apps/cab/Automation/REF/Homo_sapiens/Gencode_ERCC92/r31/STAR-index/2.7/GRCh38.primary_assembly.genome.fa -o "  + out_vcf_file + " -d RNAindel_data_dir/data_dir_38/ -q 60");
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
