package RNAseqTools.SingleCell.CNV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

/**
 * Calculate the RNAseq CNV similar to what's described in Tirosh et al. Nature 2016.
 * @author tshaw
 *
 */
public class GenerateRNAseqCNVValues {

	public static void main(String[] args) {
		
		String meta = "gene_id \"ENSG00000243485.2\"; transcript_id \"ENST00000469289.1\"; gene_type \"lincRNA\"; gene_status \"NOVEL\"; gene_name \"MIR1302-11\"; transcript_type \"lincRNA\"; transcript_status \"KNOWN\"; transcript_name \"MIR1302-11-002\"; exon_number 2;  exon_id \"ENSE00001890064.1\";  level 2; tag \"not_best_in_genome_evidence\"; havana_gene \"OTTHUMG00000000959.2\"; havana_transcript \"OTTHUMT00000002841.2\";";
		System.out.println(grabGeneName(meta));
	}
	// order the 
	
	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Calculate CNV value coverage";
	}
	public static String parameter_info() {
		return "[inputMatrix] [inputGTFFile] [outputFileReorderedMatrix] [outputRoughtAverage] [take log? true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map_chr2gene = new HashMap();
			HashMap listedGeneName = new HashMap();
			
			String inputMatrix = args[0];
			String inputGTFFile = args[1];
			String outputFileReorderedMatrix = args[2];
			String outputRoughtAverage = args[3];
			boolean log = new Boolean(args[4]);
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int countNonZeros = 0;
				for (int i = 1; i < split.length; i++) {
					if (new Double(split[i]) > 0) {
						countNonZeros++;
					}
				}
				if (countNonZeros > split.length / 10) {
					listedGeneName.put(split[0], str);
				}
				
			}
			in.close();
			System.out.println("listedGeneName.size(): " + listedGeneName.size());
			fstream = new FileInputStream(inputGTFFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				if (chr.length() < 6) {
					if (split.length > 8) {
						String geneName = grabGeneName(split[8]);
						if (listedGeneName.containsKey(geneName)) {
							if (map_chr2gene.containsKey(chr)) {
								LinkedList list = (LinkedList)map_chr2gene.get(chr);
								if (!list.contains(geneName)) {
									list.add(geneName);
								}
								map_chr2gene.put(chr, list);
							} else {
								LinkedList list = new LinkedList();
								list.add(geneName);
								map_chr2gene.put(chr, list);
							}
						}
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFileReorderedMatrix);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("chr\t" + header + "\n");
			String[] chrs = {"chr1", "chr2", "chr3", "chr4", "chr5", "chr6", "chr7", "chr8", "chr9", "chr10", "chr11", "chr12", "chr13", "chr14", "chr15", "chr16", "chr17", "chr18", "chr19", "chr20", "chr21", "chr22", "chrX", "chrY"};
			for (String chr: chrs) {
				if (map_chr2gene.containsKey(chr)) {
					LinkedList list = (LinkedList)map_chr2gene.get(chr);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String geneName = (String)itr.next();
						String line = (String)listedGeneName.get(geneName);
						out.write(chr + "\t" + line + "\n");
					}
				}
			}
			out.close();
			
			
			FileWriter fwriterSummary = new FileWriter(outputRoughtAverage);
            BufferedWriter outSummary = new BufferedWriter(fwriterSummary);
            outSummary.write("CHR\tIndex\tGene");
            for (int i = 1; i < header.split("\t").length; i++) {
            	outSummary.write("\t" + header.split("\t")[i]);
            }
            outSummary.write("\n");
			int numSamples = header.split("\t").length - 1;
			int binSize = 100;
			LinkedList[] list = new LinkedList[numSamples];
			
			for (String chr: chrs) {
				int index = 1;
				if (map_chr2gene.containsKey(chr)) {
					for (int i = 0; i < list.length; i++) {
						list[i] = new LinkedList();
					}
					LinkedList dataList = (LinkedList)map_chr2gene.get(chr);
					Iterator itr = dataList.iterator();
					while (itr.hasNext()) {
						String geneName = (String)itr.next();
						
						String line = (String)listedGeneName.get(geneName);
						//if (geneName.equals("Met")) {
						//	System.out.println(line);
						//}
						String[] split = line.split("\t");
						for (int i = 1; i < split.length; i++) {
							if (log) {
								list[i - 1].add((MathTools.log2(new Double(split[i]) + 1)) + "");								
							} else {
								list[i - 1].add(new Double(split[i]) + "");
							}
							//if (geneName.equals("Met")) {
							//	System.out.println("write in summary");
							//	System.out.println(list[i-1].size());
							//}
							if (list[i - 1].size() >= binSize) {
								double[] values;
								
								
								values = MathTools.convertListStr2Double(list[i - 1]);
								
								/*double[] log2_values = new double[values.length];
								int j = 0;
								for (double value: values) {
									log2_values[j] = MathTools.log2(value);
									j++;
								}*/
								double mean = MathTools.mean(values);
								if (i == 1) {
									outSummary.write(chr + "\t" + index + "\t" + geneName);
									index++;
								}
								outSummary.write("\t" + mean);
								list[i - 1].removeFirst();
								if (i == split.length - 1) {
									outSummary.write("\n");
								}
							}
						}										
					} // 				
				}
			} // for loop for chr
			outSummary.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String grabGeneName(String str) {
		
		String[] split = str.split(";");
		String cleanGeneName = "";
		for (String s: split) {
			if (s.contains("gene_name")) {
				cleanGeneName = s.replaceAll("gene_name", "").replaceAll(" ", "").replaceAll("\"", "");
			}
		}
		return cleanGeneName;
	}
}
