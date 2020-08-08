package jump.pipeline.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReplaceUniprotGeneSymbol2NCBIGeneSymbol {

	public static String upstream() {
		return "NA";
	}
	public static String downstream() {
		return "NA";
	}
	public static String type() {
		return "JUMP";
	}
	public static String description() {
		return "I realize that Uniprot based gene Symbol often doesn't match to the official gene symbol. If there is a uniprot id, either append or replace a column that contains gene symbol.";
	}
	public static String parameter_info() {
		return "[matrix.txt] [uniprotID index] [index for replacing Uniprot Gene Symbol. -1 for append at end] [HUMAN_ID_Table_ALL.txt/MOUSE_ID_Table_ALL.txt] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int uniprot_index = new Integer(args[1]);
			int GN_index = new Integer(args[2]);
			String id_table = args[3];
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int count_id_table_mismatch = 0;
			HashMap uniprot2geneSymbol = new HashMap();
			FileInputStream fstream = new FileInputStream(id_table);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (uniprot2geneSymbol.containsKey(split[0])) {
					String orig = (String)uniprot2geneSymbol.get(split[0]);
					if (!orig.equals(split[1])) {
						count_id_table_mismatch++;
					}
				}
				uniprot2geneSymbol.put(split[0], split[1]);
			}
			in.close();
			
			int count_replaced = 0;
			int count_same = 0;
			int count_missing = 0;
			int count_tr_missing = 0;
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			if (GN_index > -1) {
				out.write(header + "\n");				
			} else {
				out.write(header + "\tOfficialGeneSymbol\n");
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneSymbol = "NA";
				if (GN_index > -1) {
					geneSymbol =  split[GN_index];
				}
				String accession = split[uniprot_index];
				String original_accession = split[uniprot_index];
				if (split[uniprot_index].contains("|")) {
					accession = split[uniprot_index].split("\\|")[1];
				}
				//System.out.println(accession);
				if (uniprot2geneSymbol.containsKey(accession)) {
					geneSymbol = (String)uniprot2geneSymbol.get(accession);
					if (geneSymbol.equals(split[GN_index])) {
						count_same++;
					} else {
						System.out.println("Original: " + split[GN_index] + "\t" + "New: " + geneSymbol);
						count_replaced++;
					}
				} else {
					accession = accession.split("-")[0];
					if (uniprot2geneSymbol.containsKey(accession)) {
						geneSymbol = (String)uniprot2geneSymbol.get(accession);
						if (geneSymbol.equals(split[GN_index])) {
							count_same++;
						} else {
							count_replaced++;
						}
					} else {
						System.out.println(original_accession);
						if (original_accession.split("\\|")[0].equals("tr")) {
							count_tr_missing++;
						}
						count_missing++;
					}
				}
				if (GN_index > -1) {
					if (GN_index == 0) {
						out.write(geneSymbol);
					} else {
						out.write(split[0]);
					}
					for (int i = 1; i < split.length; i++) {
						if (GN_index == i) {
							out.write("\t" + geneSymbol);	
						} else {
							out.write("\t" + split[i]);
						}
					}
				} else {
					out.write(str + "\t" + geneSymbol + "\n");
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
			System.out.println("Summary:");
			System.out.println("Replaced GN: " + count_replaced);
			System.out.println("Same GN: " + count_same);
			System.out.println("Missing GN: " + count_missing);
			System.out.println("Missing GN that's tr: " + count_tr_missing);
			
			System.out.println("Count ID Table mismatch: " + count_id_table_mismatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
