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
public class GeneratePCPAMouseScript {

	public static void execute(String[] args) {
		try {
			String fileName = args[0];
			String mouseFASTA = args[1];
			String perlPath = args[2];
			String coverageBedPath = args[3];
			String mm9BowtieIndex = args[4];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				FileWriter fwriter = new FileWriter("PCPA_" + str + ".sh");
				BufferedWriter out = new BufferedWriter(fwriter);

				String sample = str.replaceAll(".bam", "");
				sample = sample.replaceAll(".fq", "");
				out.write(step1_createBowtie(sample, mm9BowtieIndex) + "\n");
				out.write(step2_toTab(sample, perlPath) + "\n");
				out.write(step3_split(sample, perlPath) + "\n");
				out.write(step4_IP(sample, mouseFASTA, perlPath) + "\n");
				out.write(step5_IP(sample, mouseFASTA, perlPath) + "\n");
				out.write(step6_idfullESTcluster2idESTcluster(sample, perlPath) + "\n");
				out.write(step7_polyA_Annotation(sample, perlPath) + "\n");
				//out.write(step8_polyA2paseq(sample, perlPath) + "\n");
				out.write(step9_pla2bed(sample) + "\n");
				out.write(step10_generateCoverageBed(sample, coverageBedPath) + "\n");
				out.write(step11_polyA_dist(sample, coverageBedPath) + "\n");
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
		return "perl " + perlPath + "/split2sets_mm9edition.pl uniq_" + sample + ".tab";
	}
	public static String step4_IP(String sample, String mouseFASTA, String perlPath) {
		return "perl " + perlPath + "/filter_internal_priming_mm9edition.pl " + mouseFASTA + " chrFix_uniq_" + sample + ".tab";
	}
	public static String step5_IP(String sample, String mouseFASTA, String perlPath) {
		return "perl " + perlPath + "/EST_cluster_mm9_fullOutput.pl noIP_chrFix_uniq_" + sample + ".tab PAS_noIP.txt solexa " + sample + " N " + sample + "_solexa_chrFix";
	}
	public static String step6_idfullESTcluster2idESTcluster(String sample, String perlPath) {
		return "perl " + perlPath + "/idfullESTcluster2idESTcluster.pl " + sample + "_solexa_chrFix.idfullESTcluster " + sample + "_solexa_chrFix";
	}
	public static String step7_polyA_Annotation(String sample, String perlPath) {
		return "perl " + perlPath + "/annotateCleavageCluster_mm9.pl " + sample + "_solexa_chrFix.idESTcluster sorted_mm9_knownGene_entrezID.txt mm9_polyAsite.db2 " + sample + "_solexa_chrFix"; 
	}
	
	public static String step8_polyA2paseq(String sample, String perlPath) {
		return "perl " + perlPath + "/polyA2paseq_fastaqEdition.pl " + sample + "_solexa_chrFix.idfullESTcluster " + sample + "_solexa_chrFix.ESTinfo processed_highQ_$FST " + sample + "_solexa_chrFix";   
	}
	public static String step9_pla2bed(String sample) {
		return "drppm -PLA2BEDFile " + sample + "_solexa_chrFix.pla > " + sample + "_solexa_chrFix.pla.bed";		
	}
	public static String step10_generateCoverageBed(String sample, String coverageBedPath) {
		return "coverageBed -a " + coverageBedPath + "/" + sample + ".bam.bed_gene.bed -b " + sample + "_solexa_chrFix.pla.bed > " + sample + "_solexa_chrFix.pla.bed.coverageBed.txt";
	}
	public static String step11_polyA_dist(String sample, String coverageBedPath) {
		return "drppm -CalculatePolyADistribution " + sample + "_solexa_chrFix.pla sorted_mm9_knownGene_entrezID.txt " + sample + ".dist " + sample + "_solexa_chrFix.pla.bed.coverageBed.txt"; 
	}
	public static String step12_polyA_hist(String sample) {
		return "drppm -GeneratePolyAHistogramOutput " + sample + ".dist " + sample + ".final"; 
	}
}
