package RNATools.PCPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


/**
 * Perl path of the PCPA tools needs to be defined
 * bowtie needs to be installed for version ???
 * @author tshaw
 *
 */
public class GeneratePCPAHumanScriptComplete {

	public static String description() {
		return "After extracting the PolyA enriched fq file, generate the complete script for PCPA";
	}
	public static String type() {
		return "PCPA";
	}
	public static String parameter_info() {
		return "[fileName] [hg19FASTA] [perlPath] [geneBed] [hg19BowtieIndex]";
	}
	public static void execute(String[] args) {
		try {
			String fileName = args[0];
			String hg19FASTA = args[1];
			String perlPath = args[2];
			String geneBed = args[3];
			String hg19BowtieIndex = args[4];
			String addChr = "";
			if (args.length > 5) {
				addChr = args[5];
			}
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				FileWriter fwriter = new FileWriter("PCPA_" + split[0] + ".sh");
				BufferedWriter out = new BufferedWriter(fwriter);
				System.out.println("sh " + "PCPA_" + split[0] + ".sh");
				String sampleBam = split[1];
				String sample = split[0].replaceAll(".bam", "");
				sample = sample.replaceAll(".fq", "");
				
				out.write(step1_createBowtie(sample, hg19BowtieIndex) + "\n");
				out.write(step2_toTab(sample, perlPath) + "\n");
				out.write(step3_split(sample, perlPath) + "\n");
				out.write(step4_IP(sample, hg19FASTA, perlPath) + "\n");
				out.write(step5_IP(sample, hg19FASTA, perlPath) + "\n");
				out.write(step6_idfullESTcluster2idESTcluster(sample, perlPath) + "\n");
				out.write(step7_polyA_Annotation(sample, perlPath) + "\n");
				out.write(step8a_bamtobed(sampleBam, sample + ".bed") + "\n");
				if (addChr.equals("yes")) {
					out.write(step8a2_addChr(sample + ".bed") + "\n");										
				}
				out.write(step8b_intersectBed(sample + ".bed", geneBed, sample + "gene.bed") + "\n");
				//#out.write(step8_polyA2paseq(sample, perlPath) + "\n");
				out.write(step9_pla2bed(sample) + "\n");
				out.write(step10_generateCoverageBed(sample, sample + "gene.bed") + "\n");
				out.write(step11_polyA_dist(sample, perlPath) + "\n");
				out.write(step12_polyA_hist(sample) + "\n"); 									
				out.close();
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String step1_createBowtie(String sample, String mm9BowtieIndex) {
		return "bowtie -v 2 -k 2 --best -p 1 " + mm9BowtieIndex + " -q " + sample + ".fq > " + sample + ".map";
	}
	public static String step2_toTab(String sample, String perlPath) {
		return "perl " + perlPath + "/mapToTab.pl " + sample + ".map";
	}
	public static String step3_split(String sample, String perlPath) {
		return "perl " + perlPath + "/split2sets_hg19edition.pl uniq_" + sample + ".tab";
	}
	public static String step4_IP(String sample, String mouseFASTA, String perlPath) {
		return "perl " + perlPath + "/filter_internal_priming_hg19edition.pl " + mouseFASTA + " chrFix_uniq_" + sample + ".tab";
	}
	public static String step5_IP(String sample, String mouseFASTA, String perlPath) {
		return "perl " + perlPath + "/EST_cluster_hg19_fullOutput.pl noIP_chrFix_uniq_" + sample + ".tab PAS_noIP.txt solexa " + sample + " N " + sample + "_solexa_chrFix";
	}
	public static String step6_idfullESTcluster2idESTcluster(String sample, String perlPath) {
		return "perl " + perlPath + "/idfullESTcluster2idESTcluster.pl " + sample + "_solexa_chrFix.idfullESTcluster " + sample + "_solexa_chrFix";
	}
	public static String step7_polyA_Annotation(String sample, String perlPath) {
		return "perl " + perlPath + "/annotateCleavageCluster_hg19.pl " + sample + "_solexa_chrFix.idESTcluster " + perlPath + "/sorted_hg19_knownGene_entrezID.txt " + perlPath + "/hg19_hs_polyAsite.db2 " + sample + "_solexa_chrFix"; 
	}
	public static String step8a_bamtobed(String sampleBam, String outputBed) {
		return "bamToBed -i " + sampleBam + " > " + outputBed;
	}
	public static String step8a2_addChr(String outputBed) {
		return "drppm -AddChr " + outputBed;
	}
	public static String step8b_intersectBed(String bedFile, String geneBED, String outputGeneBed) {
		return "intersectBed -a " + bedFile + " -b " + geneBED + " > " + outputGeneBed;
	}
	
	public static String step8_polyA2paseq(String sample, String perlPath) {
		return "perl " + perlPath + "/polyA2paseq_fastaqEdition.pl " + sample + "_solexa_chrFix.idfullESTcluster " + sample + "_solexa_chrFix.ESTinfo processed_highQ_$FST " + sample + "_solexa_chrFix";   
	}
	public static String step9_pla2bed(String sample) {
		return "drppm -PLA2BEDFile " + sample + "_solexa_chrFix.pla > " + sample + "_solexa_chrFix.pla.bed";		
	}
	public static String step10_generateCoverageBed(String sample, String coverageBedPath) {
		return "coverageBed -a " + coverageBedPath + " -b " + sample + "_solexa_chrFix.pla.bed > " + sample + "_solexa_chrFix.pla.bed.coverageBed.txt";
	}
	public static String step11_polyA_dist(String sample, String perlPath) {
		return "drppm -CalculatePolyADistribution " + sample + "_solexa_chrFix.pla " + perlPath + "/sorted_hg19_knownGene_entrezID.txt " + sample + ".dist " + sample + "_solexa_chrFix.pla.bed.coverageBed.txt"; 
	}
	public static String step12_polyA_hist(String sample) {
		return "drppm -GeneratePolyAHistogramOutput " + sample + ".dist " + sample + ".final"; 
	}
	
}

