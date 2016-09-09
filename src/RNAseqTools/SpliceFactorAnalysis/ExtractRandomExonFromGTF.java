package RNAseqTools.SpliceFactorAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class ExtractRandomExonFromGTF {

	
	public static String parameter_info() {
		return "[input] [total] [organism] [outputFileUpStream] [outputFileExon] [outputFileDnStream]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			int total = new Integer(args[1]);
			String organism = args[2];
			String outputFileUpstream = args[3];
			String outputFileExon = args[4];
			String outputFileDnstream = args[5];
			
			HashMap map = extractExon(inputFile);
			
			String outputFileUpstreamFasta = outputFileUpstream + ".fasta";
			String outputFileExonFasta = outputFileExon + ".fasta";
			String outputFileDnstreamFasta = outputFileDnstream + ".fasta";
			FileWriter fwriter1 = new FileWriter(outputFileUpstream);
			BufferedWriter outUp = new BufferedWriter(fwriter1);

			FileWriter fwriter2 = new FileWriter(outputFileExon);
			BufferedWriter outExon = new BufferedWriter(fwriter2);

			FileWriter fwriter3 = new FileWriter(outputFileDnstream);
			BufferedWriter outDn = new BufferedWriter(fwriter3);

			for (int i = 0; i < total; i++) {
				
				Random rand = new Random();
				int id = rand.nextInt(map.size());

				String line = (String)map.get(id);
				String[] split = line.split("\t");
				
				
				String chr = split[0];
				String strand = split[1];
				int start = new Integer(split[2]);
				int end = new Integer(split[3]);
				if (strand.equals("+")) {
					
					
					start = start - 14;
					end = end + 9;
					String upstream = chr + "\t+\t" + (start - 100) + "\t" + (start - 1);
					String exon = chr + "\t+\t" + (start) + "\t" + (end);
					String dnstream= chr + "\t+\t" + (end + 1) + "\t" + (end + 100);
					
					outUp.write(upstream + "\n");
					outExon.write(exon + "\n");
					outDn.write(dnstream + "\n");
				
					
					
				} else {
					start = start - 8;
					end = end + 15;
					
					String dnstream = chr + "\t-\t" + (start - 100) + "\t" + (start - 1);
					String exon = chr + "\t-\t" + (start) + "\t" + (end);
					String upstream= chr + "\t-\t" + (end + 1) + "\t" + (end + 100);
					outUp.write(upstream + "\n");
					outExon.write(exon + "\n");
					outDn.write(dnstream + "\n");
					
				
				}
			
			}
			
			System.out.println(map.size())
			;
			outUp.close();
			outExon.close();
			outDn.close();
			
			GrabExonInformation.grabExon(outputFileUpstream, outputFileUpstreamFasta, organism);
			GrabExonInformation.grabExon(outputFileExon, outputFileExonFasta, organism);
			GrabExonInformation.grabExon(outputFileDnstream, outputFileDnstreamFasta, organism);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap extractExon(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			int count = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				String[] split = str.split("\t");
				String chr = split[0];
				int start = new Integer(split[3]);
				int end = new Integer(split[4]);
				String strand = split[6];
				String type = split[2];
				if (type.equals("exon")) {
					if (end - start > 50 && end - start < 250) {
						String line = chr + "\t" + strand + "\t" + start + "\t" + end;
						if (!map.containsKey(line)) {
							count++;			
							map.put(count, line);
							
							/*if (map.size() % 1000 == 0) {
								System.out.println(map.size());
							}*/
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
