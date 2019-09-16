package proteomics.annotation.uniprot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generate a table with the following information
 * 1. UniProtKB-AC	3. GeneID (EntrezGene)	4. RefSeq	OfficialGeneSymbol (NCBI)	Description (uniprot)	LengthOfUniprot	Mass	(Maybe add additional function information)
 * @author tshaw
 *
 */
public class GenerateIDConversionMasterTable {

	public static String type() {
		return "IDCONVERSION";
	}
	public static String description() {
		return "Generate the ID conversion table";
	}
	public static String parameter_info() {
		return "[fastaFile] [idmappingFile] [geneinfoFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fastaFile = args[0];
			String idmappingFile = args[1];
			String geneinfoFile = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("UniProtKB-AC\tGeneID(EntrezGene)\tRefSeqProtein\tRefSeqNucleotide\tOfficialGeneSymbol(NCBI)\tDescription\tLengthOfUniprotMass\n");
			HashMap fasta_map = new HashMap();
			
			HashMap accession2id = new HashMap();
			HashMap accession2RefSeqNP = new HashMap();
			HashMap accession2RefSeqNT = new HashMap();
			HashMap geneID2GeneName = new HashMap();
			HashMap geneID2description = new HashMap();
			HashMap accession2length = new HashMap();
			HashMap accession2mass = new HashMap();
			
			String accession = "";
			String name = "";
			
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String tag = str;
					accession = tag.split("\\|")[1];
					name = tag.split("OS=")[0].replaceAll(tag.split("OS=")[0].split(" ")[0], "");
					//accession2name.put(accession, name);
					
				} else {
					if (fasta_map.containsKey(accession)) {
						String seq = (String)fasta_map.get(accession);
						seq += str.trim();
						fasta_map.put(accession, seq);
					} else {
						fasta_map.put(accession, str.trim());
					}
				}								
			}
			in.close();

			Iterator itr = fasta_map.keySet().iterator();
			while (itr.hasNext()) {
				accession = (String)itr.next();
				accession = accession.split("-")[0];
				String seq = (String)fasta_map.get(accession);
				if (accession2length.containsKey(accession)) {
					int length = (Integer)accession2length.get(accession);
					if (length < seq.length()) {
						length = seq.length();
						accession2mass.put(accession, calculate_aminoacid_monomass(seq));
					}
					accession2length.put(accession, length);					
				} else {
					accession2length.put(accession, seq.length());
					accession2mass.put(accession, calculate_aminoacid_monomass(seq));
				}
			}
			
			fstream = new FileInputStream(idmappingFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				accession = split[0].split("-")[0];
				if (split[1].equals("GeneID")) {
					accession2id.put(accession, split[2]);
				}
				if (split[1].equals("RefSeq")) {
					if (accession2RefSeqNP.containsKey(accession)) {
						String refseq = (String)accession2RefSeqNP.get(accession);
						refseq += "," + split[2];
						accession2RefSeqNP.put(accession, refseq);
					} else {
						accession2RefSeqNP.put(accession, split[2]);
					}
				}
				if (split[1].equals("RefSeq_NT")) {
					if (accession2RefSeqNT.containsKey(accession)) {
						String refseq = (String)accession2RefSeqNT.get(accession);
						refseq += "," + split[2];
						accession2RefSeqNT.put(accession, refseq);
					} else {
						accession2RefSeqNT.put(accession, split[2]);
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(geneinfoFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				geneID2GeneName.put(split[1], split[2]);
				geneID2description.put(split[1], split[8]);
			}
			in.close();
			
			
			itr = accession2id.keySet().iterator();
			while (itr.hasNext()) {
				accession = (String)itr.next();
				String geneID = (String)accession2id.get(accession);
				String geneName = (String)geneID2GeneName.get(geneID);
				String description = (String)geneID2description.get(geneID);
				String refseqNP = (String)accession2RefSeqNP.get(accession);
				String refseqNT = (String)accession2RefSeqNT.get(accession);
				int length = -1;
				double mass = Double.NaN;
				if (accession2length.containsKey(accession)) {
					length = (Integer)accession2length.get(accession);
					mass = (Double)accession2mass.get(accession);
				}
				
				
				//out.write("UniProtKB-AC\tGeneID(EntrezGene)\tRefSeqProtein\tRefSeqNucleotide\tOfficialGeneSymbol(NCBI)\tDescription\tLengthOfUniprotMass\n");
				out.write(accession + "\t" + geneID + "\t" + refseqNP + "\t" + refseqNT + "\t" + geneName + "\t" + description + "\t" + length + "\t" + mass + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double calculate_aminoacid_monomass(String seq) {
		double final_mass = 0.0;
		for (int i = 0; i < seq.length(); i++) {
			final_mass += getMonoMass(seq.substring(i, i + 1));
		}
		return final_mass;
	}
	public static double getMonoMass(String aa) {
		
		if (aa.toUpperCase().equals("A")) {
			return 71.03711;
		}
		if (aa.toUpperCase().equals("R")) {
			return 156.10111;
		}
		if (aa.toUpperCase().equals("N")) {
			return 114.04293;
		}
		if (aa.toUpperCase().equals("D")) {
			return 115.02694;
		}
		if (aa.toUpperCase().equals("C")) {
			return 103.00919;
		}
		if (aa.toUpperCase().equals("E")) {
			return 129.04259;
		}
		if (aa.toUpperCase().equals("Q")) {
			return 128.05858;
		}
		if (aa.toUpperCase().equals("G")) {
			return 57.02146;
		}
		if (aa.toUpperCase().equals("H")) {
			return 137.05891;
		}
		if (aa.toUpperCase().equals("I")) {
			return 113.08406;
		}
		if (aa.toUpperCase().equals("L")) {
			return 113.08406;
		}
		if (aa.toUpperCase().equals("K")) {
			return 128.09496;
		}
		if (aa.toUpperCase().equals("M")) {
			return 131.04049;
		}
		if (aa.toUpperCase().equals("F")) {
			return 147.06841;
		}
		if (aa.toUpperCase().equals("P")) {
			return 97.05276;
		}
		if (aa.toUpperCase().equals("S")) {
			return 87.03203;
		}
		if (aa.toUpperCase().equals("T")) {
			return 101.04768;
		}
		if (aa.toUpperCase().equals("W")) {
			return 186.07931;
		}
		if (aa.toUpperCase().equals("Y")) {
			return 163.06333;
		}
		if (aa.toUpperCase().equals("V")) {
			return 99.06841;
		}		
		return 0.0;
	}
}
