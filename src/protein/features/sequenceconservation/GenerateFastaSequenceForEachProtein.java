package protein.features.sequenceconservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate fasta sequences from uniprot fasta file
 * @author tshaw
 *
 */
public class GenerateFastaSequenceForEachProtein {

	public static String description() {
		return "Generate fasta sequences from uniprot fasta file";
	}
	public static String type() {
		return "PROTEINFEATURE";
	}
	public static String parameter_info() {
		return "[inputFile] [organismList] [outputFile] [outputAlign] [outputSEG]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap protein = new HashMap();
			String name = "";
						
			String inputFile = args[0];			
			String organismFile = args[1];
			String outputPath = args[2];
			String outputAlignmentScript = args[3];
			String outputSEGScript = args[4];
			String outputExtractSEG = args[5];
			String outputAlignSEG = args[6];
			
			FileWriter fwriter2 = new FileWriter(outputAlignmentScript);
            BufferedWriter out2 = new BufferedWriter(fwriter2);

			FileWriter fwriter3 = new FileWriter(outputSEGScript);
            BufferedWriter out3 = new BufferedWriter(fwriter3);

			FileWriter fwriter4 = new FileWriter(outputExtractSEG);
            BufferedWriter out4 = new BufferedWriter(fwriter4);

			FileWriter fwriter5 = new FileWriter(outputAlignSEG);
            BufferedWriter out5 = new BufferedWriter(fwriter5);

            LinkedList organism_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(organismFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!organism_list.contains(str)) {
					organism_list.add(str);
				}
			}
			in.close();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
					String[] split = name.split("\\|");
					String protein_name = split[2].split("_")[0];
					String organism = split[2].split("_")[1].split(" ")[0];
					//System.out.println(protein_name + "\t" + organism);
					if (organism_list.contains(organism)) {
						if (protein.containsKey(protein_name)) {
							HashMap organism_map = (HashMap)protein.get(protein_name);
							organism_map.put(organism, name + "\n");
							protein.put(protein_name, organism_map);						
						} else {
							HashMap organism_map = new HashMap();
							organism_map.put(organism, name + "\n");
							protein.put(protein_name, organism_map);
						}
					}
				} else {
					String[] split = name.split("\\|");
					String protein_name = split[2].split("_")[0];
					String organism = split[2].split("_")[1].split(" ")[0];
					if (organism_list.contains(organism)) {
						if (protein.containsKey(protein_name)) {
							HashMap organism_map = (HashMap)protein.get(protein_name);
							String line = (String)organism_map.get(organism);
							line += str;
							organism_map.put(organism, line);
							protein.put(protein_name, organism_map);
						} 				
					}
				}
				
			}
			in.close();
			
			
			Iterator itr = protein.keySet().iterator();
			while (itr.hasNext()) {
				String protein_name = (String)itr.next();	
				
				String outputFile = outputPath + "/" + protein_name + ".fasta";
				String alignFile = outputPath + "/" + protein_name + ".align";
				String segFile = outputPath + "/" + protein_name + ".seg";
				String segFastaFile = outputPath + "/" + protein_name + ".seg.fasta";
				String segFastaAlignFile = outputPath + "/" + protein_name + ".seg.fasta.aln";
				out2.write("muscle -in " + outputFile + " -out " + alignFile + "\n");
				out3.write("~/PROTEOMICS/Tools/SEG/seg " + alignFile + " -l > " + segFile + "\n");
				out4.write("drppm -AlignSEGSequence " + segFile + " " + segFastaFile + "\n");
				out5.write("muscle -in " + segFastaFile + " -out " + segFastaAlignFile + "\n");
				FileWriter fwriter = new FileWriter(outputFile);
	            BufferedWriter out = new BufferedWriter(fwriter);

				HashMap organism_map = (HashMap)protein.get(protein_name);
				Iterator itr2 = organism_map.keySet().iterator();
				while (itr2.hasNext()) {
					String organism = (String)itr2.next();
					String seq = (String)organism_map.get(organism);
					out.write(seq + "\n");
				}
				out.close();
			}
			out2.close();
			out3.close();
			out4.close();
			out5.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
