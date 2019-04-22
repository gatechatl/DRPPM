package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGenerateRNAseQCScript {

	public static void main(String[] args) {
		
		try {
			String sampleFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\QC\\RNAseQC\\PCGP_sample.txt"; 
			String outputMasterFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\script.sh";
			FileWriter fwriter_master = new FileWriter(outputMasterFile);
			BufferedWriter out_master = new BufferedWriter(fwriter_master);			
			
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\" + split[0] + ".sample.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);			
				out.write(header + "\n");
				//out.write(str + "\n");
				out.write(split[0] + "\t/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_RNAseq/bam/" + split[0] + ".bam" + "\t" + split[2] + "\n");
				out.close();
				
				out_master.write("sh " + split[0] + ".sh\n");
				String output_shell_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\" + split[0] + ".sh";
				FileWriter fwriter_shell = new FileWriter(output_shell_file);
				BufferedWriter out_shell = new BufferedWriter(fwriter_shell);			
				String picard = "/hpcf/apps/java/jdk1.8.0_60/bin/java -jar /hpcf/apps/picard/install/2.0.1/picard.jar AddOrReplaceReadGroups I=" + split[1] + " O=/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/PCGP_RNAseq/bam/" + split[0] + ".bam RGID=foo RGLB=bar RGPL=illumina RGSM=Sample1 RGPU=R1 CREATE_INDEX=True VALIDATION_STRINGENCY=SILENT\n";				
				String script = "/nfs_exports/apps/compilers/jdk1.6.0_15/bin/java -jar /rgs01/resgen/dev/wc/tshaw/Tools/Java/RNA-SeQC_v1.1.8.jar -s " + split[0] + ".sample.txt -t /scratch_space/tshaw/AltSplice/PCGP_RNAseQC/gencode.v19.genes.v7.patched_contigs.gtf -r /rgs01/reference/public/genomes/Homo_sapiens/GRCh37-lite/FASTA/GRCh37-lite.fa -o " + split[0] + "\n";
				out_shell.write(picard);
				out_shell.write(script);
				out_shell.close();
			}
			
			out_master.close();
			in.close();
		} catch (Exception e) {
			
		}
	}
}
