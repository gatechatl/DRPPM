package rnaseq.splicing.customjumpdatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import idconversion.tools.GTFFile;

public class SpliceWRAPCustomDatabaseGeneral {

	
	public static void main(String[] args) {
		
		try {
			


			//HashMap geneName2proteinname = new HashMap();
			//HashMap proteinName2geneName = new HashMap();
			HashMap transcript2protein = new HashMap();
			HashMap protein2transcript = new HashMap();


			//String outputFile_pit_lookup = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetention/JD_IntronRetentionDatabase.lookup.txt";
			//String outputFile_pit_lookup = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV2/JD_IntronRetentionDatabase.lookup.txt";
			String outputFile_pit_lookup = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV3/JD_IntronRetentionDatabase.lookup.txt";
			FileWriter fwriter_pit_lookup = new FileWriter(outputFile_pit_lookup);
			BufferedWriter out_pit_lookup = new BufferedWriter(fwriter_pit_lookup);
			
			//String outputFile_pit = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetention/JD_IntronRetentionDatabase.pit";
			//String outputFile_pit = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV2/JD_IntronRetentionDatabase.pit";
			String outputFile_pit = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV3/JD_IntronRetentionDatabase.pit";
			FileWriter fwriter_pit = new FileWriter(outputFile_pit);
			BufferedWriter out_pit = new BufferedWriter(fwriter_pit);
			
			//String outputFile_fasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetention/JD_IntronRetentionDatabase.pit.fasta";
			//String outputFile_fasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV2/JD_IntronRetentionDatabase.pit.fasta";
			String outputFile_fasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV3/JD_IntronRetentionDatabase.pit.fasta";
			FileWriter fwriter_fasta = new FileWriter(outputFile_fasta);
			BufferedWriter out_fasta = new BufferedWriter(fwriter_fasta);
			
			out_pit.write("UniprotAC\tSJPGnumber\tGroupName\tAbundance\tResidueLength\tProteinName\tFullDescription\tAnnotation\n");
			
			
			HashMap convert_name = new HashMap();
			boolean found = false;
			String name = "";
			String protein_id = "";
			String seq = "";
			String transcript_id = "";
			HashMap sequence = new HashMap();
			//String inputFasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetention/dr_vs_ndr_yescarta_intron_seqs_emboss_50bpFlank.pep.flat";
			//String inputFasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV2/combined.pep.fasta";
			String inputFasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseIntronRetentionV3/combined.pep.fasta";
			FileInputStream fstream = new FileInputStream(inputFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
					name = name.replaceAll("\\(+\\)", "for").replaceAll("\\(-\\)", "rev");
					seq = "";
				
				} else {					
					seq += str;
					sequence.put(name, seq);
					System.out.println(name + "\t" + seq);
					//map.put(name, seq);
				}
			}
			in.close();
			
			int count_hits = 0;
			
			int index = 100001;
			HashMap reverse_convert_name = new HashMap();
			//HashMap tag_name = new HashMap();
			Iterator itr = sequence.keySet().iterator();
			while (itr.hasNext()) {
				String tag_name = (String)itr.next();
				seq = (String)sequence.get(tag_name);
				convert_name.put("SJPG" + index + ".001", tag_name);		
				reverse_convert_name.put(tag_name, "SJPG" + index + ".001");
				//out_fasta.write(">" + "SJPG" + index + ".001" + "\n");																				
				out_fasta.write(">" + tag_name.replaceAll(">", "") + "\n");
				out_fasta.write(seq + "\n");
				out_pit.write("SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "-" + "\t" + seq.length() + "\t" + tag_name + "\t" + tag_name + "\t-\n");
				index++;
			}
			
			itr = sequence.keySet().iterator();
			while (itr.hasNext()) {
				String tag_name = (String)itr.next();
				String aa_seq = (String)sequence.get(tag_name);
		        StringBuilder aa_seq_reverse = new StringBuilder(); 
		        
		        // append a string into StringBuilder input1 
		        aa_seq_reverse.append(aa_seq); 
		  
		        String tag = (String)reverse_convert_name.get(tag_name);
		        // reverse StringBuilder input1 
		        aa_seq_reverse = aa_seq_reverse.reverse(); 
		        aa_seq = aa_seq_reverse.toString();
		        String new_tag = ">" + "##Decoy__" + tag.replaceAll(">", "");

		        
				
				convert_name.put("##Decoy__" + index + ".001", new_tag);
				
		        //out_fasta.write(">" + "##Decoy__" + index + ".001" + "\n");
				out_fasta.write(">" + "##Decoy__" + tag_name.replaceAll(">", "") + "\n");
		        out_fasta.write(aa_seq.toString() + "\n");
				out_pit.write("##Decoy__" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "-" + "\t" + seq.length() + "\t" + tag_name + "\t" + tag_name + "\t-\n");
				index++;
			}
			out_fasta.close();
			

			itr = convert_name.keySet().iterator();
			while (itr.hasNext()) {
				String sjpg_name = (String)itr.next();
				String tag = (String)convert_name.get(sjpg_name);
				out_pit_lookup.write(sjpg_name + "\t" + tag + "\n");
			}
			out_pit_lookup.close();
			out_pit.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
