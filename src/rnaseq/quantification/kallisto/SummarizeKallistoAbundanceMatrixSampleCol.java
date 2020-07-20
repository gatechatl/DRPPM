package rnaseq.quantification.kallisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SummarizeKallistoAbundanceMatrixSampleCol {


	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Grab Kallisto abundance matrix TPM";
	}
	public static String parameter_info() {
		return "[fastq_lst: 3 column] [num_col] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			int num_col = new Integer(args[1]);
			String outputFileMatrix = args[2];
			FileWriter fwriter = new FileWriter(outputFileMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Transcript");
			
			int idx = 1;
			HashMap map = new HashMap();
			HashMap samples = new HashMap();
			HashMap transcript = new HashMap();
			String fastq_lst = args[0];
			FileInputStream fstream = new FileInputStream(fastq_lst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String abundance_path = split[2] + "_kallisto/abundance.tsv";
				//out.write("kallisto quant -i " + index + " " + split[0] + " " + split[1] + " -o " + split[2] + "_kallisto\n");
				FileInputStream fstream2 = new FileInputStream(abundance_path);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					
					if (transcript.containsKey(idx)) {
						LinkedList list = (LinkedList)transcript.get(idx);
						list.add(split2[0]);
						transcript.put(idx, list);
						if (list.size() > num_col) {
							idx++;
						}					
					} else {
						LinkedList list = new LinkedList();
						list.add(split2[0]);
						transcript.put(idx, list);
					}
					
				}			
			}
			in.close();
			
			Iterator itr = samples.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write("\t" + sample);
			}
			out.write("\n");
			
			for (int i = 1; i <= idx; i++) {
				fstream = new FileInputStream(fastq_lst);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String abundance_path = split[2] + "_kallisto/abundance.tsv";
					//out.write("kallisto quant -i " + index + " " + split[0] + " " + split[1] + " -o " + split[2] + "_kallisto\n");
					
					
					samples.put(split[2], split[2]);
					FileInputStream fstream2 = new FileInputStream(abundance_path);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						LinkedList list = (LinkedList)transcript.get(i);
						if (list.contains(split2[0])) {
							map.put(split[2] + "\t" + split2[0], split2[4]);
						}
						//out.write("\t" + split2[4]);						
					}
					//out.write("\n");
					in2.close();
				}
				in.close();				
				
				
				
				//Iterator itr2 = transcript.keySet().iterator();
				Iterator itr2 = ((LinkedList)transcript.get(i)).iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					out.write(gene);
					itr = samples.keySet().iterator();
					while (itr.hasNext()) {
						String sample = (String)itr.next();
						out.write("\t" + map.get(sample + "\t" + gene));
					}
					out.write("\n");				
				}								
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
