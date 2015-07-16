package RNAseqTools.IntronRetention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.FileTools;

public class IntronMappingPercentageSummary {

	public static void execute(String[] args) {
		
		String inputFile = args[0];
		String outputFile = args[1];
		String groupingFile = args[2];
		
		createTable(inputFile, outputFile);
		
	}
	
	
	public static void createTable(String inputFile, String outputFile) {
		
		try {
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			LinkedList listFile = FileTools.readFileList(inputFile);
			String script = "";
			Iterator itr = listFile.iterator();		
			out.write("File\t%mapped To Intron\tIntronReads\tExonReads\tGeneReads\tAllReads\n");
			while (itr.hasNext()) {
				String file = (String)itr.next();
				String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
				//script += "drppm -CountNumberOfUniqReads " + bedFile + " true > " + bedFile + ".count" + "\n";
				
				String all_file = bedFile + ".count";
				String exon_file = bedFile + "_exon.bed.count";
				String gene_file = bedFile + "_gene.bed.count";
				String intron_file = bedFile + "_intron.bed.count";
				double all_reads = readReadNum(all_file);
				double exon_reads = readReadNum(exon_file);
				double gene_reads = readReadNum(gene_file);
				double intron_reads = readReadNum(intron_file);
				out.write(bedFile + "\t" + intron_reads / all_reads + "\t" + intron_reads + "\t" + exon_reads + "\t" + gene_reads + "\t" + all_reads + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double readReadNum(String inputFile) {
		double num = -1;
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				num = new Double(split[1]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
}
