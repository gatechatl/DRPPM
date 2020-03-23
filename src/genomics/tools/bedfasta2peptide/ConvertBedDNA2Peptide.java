package genomics.tools.bedfasta2peptide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import protein.features.translate.Nucleotide2Protein;
import misc.CommandLine;

/**
 * Convert bed to fasta and match to existing protein database for peptide
 * @author tshaw
 *
 */
public class ConvertBedDNA2Peptide {

	public static String description() {
		return "Convert BED candidates to DNA and possible peptide";
	}
	public static String type() {
		return "GSEA";
	}
	public static String parameter_info() {
		return "[input_candidate_bed] [genome_reference: h37_hg19_chrOnly.fa] [inputUniprotFasta: Homo_sapiens_uniprot_sprot_combined.fasta] [candidate_peptide.txt] []";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String input_candidate_bed = args[0];
			String genome_reference = args[1]; // /rgs01/reference/public/genomes/Homo_sapiens/hg19/STAR/Genome_hg19/h37_hg19_chrOnly.fa
			String inputUniprotFasta = args[2]; //"Z:\\ResearchHome\\PojectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\candidates\\uniprot_download_20190514\\Homo_sapiens_uniprot_sprot_combined.fasta";
			String candidate_fasta = input_candidate_bed + ".fasta";
			String outputPeptideFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\candidates\\candidate_peptide.txt";					
			//bedtools getfasta -bed candidate.bed -fi /rgs01/reference/public/genomes/Homo_sapiens/hg19/STAR/Genome_hg19/h37_hg19_chrOnly.fa -fo candidate.fasta
			String bedtools_command = "bedtools getfasta -bed " + input_candidate_bed + " -fi " + genome_reference + " -fo " + candidate_fasta; 
					
			CommandLine.executeCommand(bedtools_command);
			
			File f = new File(candidate_fasta);
			if (!f.exists()) {
				System.exit(0);
			}

			// next append the geneName to each fasta file
			LinkedList gene_list = new LinkedList();			
			FileInputStream fstream = new FileInputStream(input_candidate_bed);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String geneName = str.split("\t")[3].split("_")[0];
				gene_list.add(geneName);
			}
			in.close();

			String candidate_fasta_geneName = input_candidate_bed + ".geneName.fasta";
			String outputFile = candidate_fasta_geneName;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			Iterator itr = gene_list.iterator();
			
			String fastaFile = candidate_fasta;
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String geneName = (String)itr.next();
					out.write(str + "\t" + geneName + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
			
			// perform six frame translation and check whether any peptide match with the uniprot protein sequence.
			
			
			String inputQueryFasta = candidate_fasta_geneName;
			
			String uniprotID = "NA";
			String geneName = "NA";
			
			fwriter = new FileWriter(outputPeptideFile);
			out = new BufferedWriter(fwriter);
			
			HashMap uniprot2fasta = new HashMap();
			HashMap geneName2uniprot = new HashMap();
			
			fstream = new FileInputStream(inputUniprotFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split(" ");
					uniprotID = split[0].replaceAll(">", "").split("\\|")[1];
					geneName = "NA";
					for (String stuff: split) {
						if (stuff.contains("GN=")) {
							geneName = stuff.split("=")[1];
						}
					}					
					if (geneName2uniprot.containsKey(geneName)) {
						LinkedList list = (LinkedList)geneName2uniprot.get(geneName);
						list.add(uniprotID);
						geneName2uniprot.put(geneName, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(uniprotID);
						geneName2uniprot.put(geneName, list);
					}
				} else {
					if (uniprot2fasta.containsKey(uniprotID)) {
						String prev_seq = (String)uniprot2fasta.get(uniprotID);
						prev_seq += str;
						uniprot2fasta.put(uniprotID, prev_seq);
					} else {
						uniprot2fasta.put(uniprotID, "");
					}
				}
			}
			in.close();
			
			int count_total_hit = 0;
			int count_query_hit = 0;
			int count_query = 0;
			String query_gene_name = "";
			String query_name = "";
			fstream = new FileInputStream(inputQueryFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\t");
					query_gene_name = split[1];
					query_name = str;
					count_query++;
				} else {
					String nucleotide_seq = str;
					String frame1 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, 1, false);
					String frame2 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, 2, false);
					String frame3 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, 3, false);
					String frame_rev1 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, -1, false);
					String frame_rev2 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, -2, false);
					String frame_rev3 = Nucleotide2Protein.nucleotide2protein(nucleotide_seq, -3, false);
					if (geneName2uniprot.containsKey(query_gene_name)) {
						boolean hit = false;
						LinkedList list = (LinkedList)geneName2uniprot.get(query_gene_name);
						itr = list.iterator();
						while (itr.hasNext()) {
							uniprotID = (String)itr.next();
							String fasta = (String)uniprot2fasta.get(uniprotID);
							for (int i = 0; i < fasta.length() - frame1.length(); i++) {
								if (frame1.equals(fasta.substring(i, i + frame1.length()))) {
									out.write(query_name + "\tframe1" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame1.length()) + "\t" + frame1 + "\n");
									count_total_hit++;
									hit = true;
								}
							}
							for (int i = 0; i < fasta.length() - frame2.length(); i++) {
								if (frame2.equals(fasta.substring(i, i + frame2.length()))) {
									out.write(query_name + "\tframe2" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame2.length()) + "\t" + frame2 +  "\n");
									count_total_hit++;
									hit = true;
								}
							}
							for (int i = 0; i < fasta.length() - frame3.length(); i++) {
								if (frame3.equals(fasta.substring(i, i + frame3.length()))) {
									out.write(query_name + "\tframe3" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame3.length()) + "\t" + frame3 + "\n");
									count_total_hit++;
									hit = true;
								}
							}
							for (int i = 0; i < fasta.length() - frame_rev1.length(); i++) {
								if (frame_rev1.equals(fasta.substring(i, i + frame_rev1.length()))) {
									out.write(query_name + "\tframe_rev1" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame_rev1.length()) + "\t" + frame_rev1 + "\n");
									count_total_hit++;
									hit = true;
								}
							}
							for (int i = 0; i < fasta.length() - frame_rev2.length(); i++) {
								if (frame_rev2.equals(fasta.substring(i, i + frame_rev2.length()))) {
									out.write(query_name + "\tframe_rev2" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame_rev2.length()) + "\t" + frame_rev2 + "\n");
									count_total_hit++;
									hit = true;
								}
							}
							for (int i = 0; i < fasta.length() - frame_rev3.length(); i++) {
								if (frame_rev3.equals(fasta.substring(i, i + frame_rev3.length()))) {
									out.write(query_name + "\tframe_rev3" + "\t" + uniprotID + "\t" + i + "\t" + (i + frame_rev3.length()) + "\t" + frame_rev3 + "\n");
									count_total_hit++;
									hit = true;
								}
							}
						}
						if (hit) {
							count_query_hit++;
						}
					}
					
				}
				
			}
			in.close();
			out.close();
			System.out.println("Uniprot Size: " + uniprot2fasta.size());
			System.out.println("Total Queries: " + count_query);
			System.out.println("Total Hits: " + count_total_hit);
			System.out.println("Num Query with Hits: " + count_query_hit);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
