package proteomics.annotation.uniprot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import expressionanalysis.tools.gsea.GSEATools;

/**
 * Using the uniprot reference, append uniprot info regarding  
 * the dna or rna binding site and its fasta sequence
 * @author tshaw
 *
 */
public class ExportNucleotideBindingMotifInfo {

	public static String parameter_info() {
		return "[uniprot_refFile] [inputFile] [geneIndex]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap dna_binding = new HashMap();
			HashMap nucleotide_binding = new HashMap();
			HashMap rna_binding = new HashMap();
			HashMap ribosomal_protein = new HashMap();
			String refFile = args[0];
			String inputFile = args[1];
			int gene_index = new Integer(args[2]);
			String fastaFile = args[3];
			String gsea_dna_binding_file = args[4];
			String gsea_rna_binding_file = args[5];
			String gsea_nucleotide_binding_file = args[6];
			HashMap gsea_dna_binding = GSEATools.grabGeneList(gsea_dna_binding_file);
			HashMap gsea_rna_binding = GSEATools.grabGeneList(gsea_rna_binding_file);
			HashMap gsea_nucleotide_binding = GSEATools.grabGeneList(gsea_nucleotide_binding_file);
			FileInputStream fstream = new FileInputStream(refFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[split.length - 1];
				if (split.length > 8) {
					String note = split[8];
					String coord = split[0] + ":" + split[3] + "-" + split[4];
					if (split[2].equals("DNA binding")) {
						if (dna_binding.containsKey(geneName)) {
							String new_coord = (String)dna_binding.get(geneName);
							new_coord += "," + coord;
							dna_binding.put(geneName, new_coord);
						} else {
							dna_binding.put(geneName, coord);
						}
						
					}
					if (split[2].equals("Nucleotide binding")) {
						if (nucleotide_binding.containsKey(geneName)) {
							String new_coord = (String)nucleotide_binding.get(geneName);
							new_coord += "," + coord;
							nucleotide_binding.put(geneName, new_coord);
						} else {
							nucleotide_binding.put(geneName, coord);
						}
						//nucleotide_binding.put(geneName, geneName);
					}
					if (split[2].equals("Region") || split[2].equals("Domain") || split[2].equals("Chain")) {
						
						if (note.contains("RNA-binding")) {
							if (rna_binding.containsKey(geneName)) {
								String new_coord = (String)rna_binding.get(geneName);
								new_coord += "," + coord;
								rna_binding.put(geneName, new_coord);
							} else {
								rna_binding.put(geneName, coord);
							}
							//rna_binding.put(geneName, geneName);
						}
					}						
					if (split[2].equals("Chain")) {
						if (note.contains("ribosomal")) {
							ribosomal_protein.put(geneName, coord);
						}
					}
				}
			}
			in.close();
			
			
			/*
			String geneName = "";
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					geneName = str.split("\\|")[1];
					if (map.containsKey(geneName)) {
						System.out.println("What?");
					}
					
				} else {
					if (map.containsKey(geneName)) {
						String stuff = (String)map.get(geneName);
						map.put(geneName, stuff + str);
					} else {
						map.put(geneName, str);
					}
				}
			}
			in.close();
			*/
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[gene_index];
				String dna_binding_note = "NA";
				String rna_binding_note = "NA";
				String nucleotide_binding_note = "NA";
				String go_dna_binding = "No";
				String go_rna_binding = "No";
				String go_nucleotide_binding = "No";
				String ribosomal_note = "NA";
				String nuc_seq = "NA";
				if (dna_binding.containsKey(geneName)) {
					dna_binding_note = (String)dna_binding.get(geneName);
				}
				if (rna_binding.containsKey(geneName)) {
					rna_binding_note = (String)rna_binding.get(geneName);
				}
				if (nucleotide_binding.containsKey(geneName)) {
					nucleotide_binding_note = (String)nucleotide_binding.get(geneName);
					/*if (map.containsKey(geneName)) {
						String seq = (String)map.get(geneName);
						
					}*/
				}
				if (ribosomal_protein.containsKey(geneName)) {
					ribosomal_note = (String)ribosomal_protein.get(geneName);
				}
				if (gsea_dna_binding.containsKey(geneName)) {
					go_dna_binding = "Yes";
				}
				if (gsea_rna_binding.containsKey(geneName)) {
					go_rna_binding = "Yes";
				}
				if (gsea_nucleotide_binding.containsKey(geneName)) {
					go_nucleotide_binding = "Yes";
				}
				System.out.println(str + "\t" + dna_binding_note + "\t" + rna_binding_note + "\t" + nucleotide_binding_note + "\t" + ribosomal_note + "\t" + go_dna_binding + "\t" + go_rna_binding + "\t" + go_nucleotide_binding);
			}
			in.close();
			
			
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
