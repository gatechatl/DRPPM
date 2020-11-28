package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class IntronRetentionPipelineWrapper {

	public static String type() {
		return "INTRONRETENTION";
	}
	public static String description() {
		return "Generate the data matrix for intron retention. Need to run step1 then step2 on cluster and finally step3 once everything finishes. Works with /hpcf/apps/bedtools/vendor/2.17.0/bin/bedtools";
	}
	public static String parameter_info() {
		return "[bamFileList] [config file] [readLength] [prefix for outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String bamFileList = args[0];
			String config_file = args[1];
			int readLength = new Integer(args[2]);			
			String prefix = args[3];			
			String output_shell_script = "step2_" + prefix + ".run_this_on_cluster.sh";
			String output_setup_script = "step1_" + prefix + ".run_this_first.sh";			
			String output_SD_meta = prefix + ".metainfo.txt";
			String output_summary = "step3_" + prefix + ".summary.sh";
			
			String intron_only_bed = "NA";
			String exon_bed = "NA";
			String gene_bed = "NA";			
			String kgXref = "NA"; ///rgs01/resgen/dev/wc/tshaw/REFERENCE/IDCONVERSION/UCSC/hg19/kgXref.txt
			String hg38gtf = "NA";
			FileInputStream fstream = new FileInputStream(config_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("=");
				if (split[0].trim().toUpperCase().equals("INTRON_ONLY_BED")) {
					intron_only_bed = split[1];
				}
				if (split[0].trim().toUpperCase().equals("EXON_BED")) {
					exon_bed = split[1];
				}
				if (split[0].trim().toUpperCase().equals("GENE_BED")) {
					gene_bed = split[1];
				}				
				if (split[0].trim().toUpperCase().equals("KGXREF")) {
					kgXref = split[1];
				}
				if (split[0].trim().toUpperCase().equals("HG38GTF")) {
					hg38gtf = split[1];
				}
				
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(output_setup_script);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(output_SD_meta);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileWriter fwriter_summary = new FileWriter(output_summary);
			BufferedWriter out_summary = new BufferedWriter(fwriter_summary);

			FileWriter fwriter_shell = new FileWriter(output_shell_script);
			BufferedWriter out_shell = new BufferedWriter(fwriter_shell);
			
			fstream = new FileInputStream(bamFileList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String bamFilePath = split[1];
				File f = new File(bamFilePath);
				String sample_bam = sampleName + ".bam.lst";
				FileWriter fwriter3 = new FileWriter(sample_bam);
				BufferedWriter out3 = new BufferedWriter(fwriter3);
				out3.write(bamFilePath);
				out3.close();
				
				String sample_script = sampleName + ".sh";
			
				out.write("drppm -Bam2Bed " + sample_bam + ">" + sample_script + "\n");
				out.write("drppm -IntersectBed " + sample_bam + " " + intron_only_bed + " " + exon_bed + " " + gene_bed + ">>" + sample_script + "\n");
				out.write("drppm -CountNumUniqReadsScript " + sample_bam + ">>" + sample_script + "\n");
				out.write("echo 'drppm -IntronMappingPercentageSummary " + sample_bam + " " + sampleName + "_intron_summary.txt' >> " + sample_script + "\n");
				out.write("drppm -FilterBEDReadsScript " + sample_bam + " " + readLength + " 2 >> " + sample_script + "\n");
				out.write("drppm -CalculateCoverageBed " + sample_bam + " " + intron_only_bed + " " + exon_bed + " >> " + sample_script + "\n");				
				out.write("drppm -CalculateSplicingDeficiencyScript " + sample_bam + " " + intron_only_bed + " " + exon_bed + " >> " + sample_script + "\n");
				
				if (!hg38gtf.equals("NA")) {
					out_summary.write("drppm -EnsemblTranscriptID2GeneNameAppened " + f.getName() + ".bed_SD.txt " + hg38gtf + " " + f.getName() + ".bed_SD_geneName.txt\n");
				} else {
					out_summary.write("drppm -kgXrefConversion " + f.getName() + ".bed_SD.txt " + kgXref + " " + f.getName() + ".bed_SD_geneName.txt\n");
				}
				out.write("\n");
				
				out2.write(sampleName + "\t" + f.getName() + ".bed_SD_geneName.txt\n");
				out_shell.write("sh " + sample_script + "\n");
			}			
			in.close();	
			if (!hg38gtf.equals("NA")) {
				out_summary.write("drppm -CombineSplicingDeficiencyNameMetaHG38 " + output_SD_meta + " " + prefix + ".summary.txt");
			} else {
				out_summary.write("drppm -CombineSplicingDeficiencyNameMeta " + output_SD_meta + " " + prefix + ".summary.txt");
			}
			out_summary.close();
			out.close();
			out2.close();
			out_shell.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
