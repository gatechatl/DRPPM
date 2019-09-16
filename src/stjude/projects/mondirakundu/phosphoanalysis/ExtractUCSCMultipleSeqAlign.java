package stjude.projects.mondirakundu.phosphoanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author tshaw
 *
 */
public class ExtractUCSCMultipleSeqAlign {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Extract UCSC Multiple Sequence Alignment Section";
	}
	public static String parameter_info() {
		return "[fastaFile] [alignmentFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String fastaFile = args[0];
			String alignmentFile = args[1];
			String outputFile = args[2];
			
			String gene = "";
			String refSeq = "";
			String seq = "";
			
			String fasta = "";
			FileInputStream fstream = new FileInputStream(fastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					
					String[] split = str.split(" ");
					refSeq = str;
					gene = split[1].split("\\.")[0];
					//System.out.println("title: " + str);
				} else {
					fasta += str.trim();
				}
			}
			in.close();
			
			String hg38 = "";
			String panTro4 = "";
			String rheMac3 = "";
			String mm10 = "";
			String rn5 = "";
			String canFam3 = "";
			String monDom5 = "";
			
			//System.out.println("hello" + fasta);
			
			LinkedList list = new LinkedList();
			
			FileInputStream fstream2 = new FileInputStream(alignmentFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));					
			while (in2.ready()) {
				String str = in2.readLine().trim();
				if (str.contains(">")) {
					if (str.contains(gene)) {
						String geneName = str;
						String sequence = in2.readLine().trim();
						if (geneName.contains("hg38")) {
							hg38 += sequence;
						}
						if (geneName.contains("panTro4")) {
							panTro4 += sequence;
						}
						if (geneName.contains("rheMac3")) {
							rheMac3 += sequence;
						}
						if (geneName.contains("mm10")) {
							mm10 += sequence;
						}
						if (geneName.contains("rn5")) {
							rn5 += sequence;
						}
						if (geneName.contains("canFam3")) {
							canFam3 += sequence;
						}
						if (geneName.contains("monDom5")) {
							monDom5 += sequence;
						}
						//list.add(geneName + "\n" + sequence + "\n");
						
					}
				}
			}
			in.close();
			
			/*String hg38 = "";
			String panTro4 = "";
			String rheMac3 = "";
			String mm10 = "";
			String rn5 = "";
			String canFam3 = "";
			String monDom5 = "";
			*/
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(refSeq + " Homo_sapiens\n");
			out.write(hg38 + "\n");
			out.write(refSeq + " Pan_troglodytes\n");
			out.write(panTro4 + "\n");
			out.write(refSeq + " Macaca_mulatta\n");
			out.write(rheMac3 + "\n");
			out.write(refSeq + " Mus_musculus\n");
			out.write(mm10 + "\n");
			out.write(refSeq + " Rattus_norvegicus\n");
			out.write(rn5 + "\n");
			out.write(refSeq + " Canis_lupus_familiaris\n");
			out.write(canFam3 + "\n");
			out.write(refSeq + " Monodelphis_domestica\n");
			out.write(monDom5 + "\n");
			/*Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				out.write(line + "\n");
			}*/
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
