package rnaseq.quantification.kallisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SummarizeKallistoAbundanceMatrixGeneral {


	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Grab Kallisto abundance matrix TPM";
	}
	public static String parameter_info() {
		return "[abundance file path] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String outputFileMatrix = args[1];
			FileWriter fwriter = new FileWriter(outputFileMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Transcript");
			
			HashMap map = new HashMap();
			HashMap samples = new HashMap();
			HashMap transcript = new HashMap();
			String abundance_lst = args[0];
			FileInputStream fstream = new FileInputStream(abundance_lst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String abundance_path = split[1];
				//out.write("kallisto quant -i " + index + " " + split[0] + " " + split[1] + " -o " + split[2] + "_kallisto\n");
				FileInputStream fstream2 = new FileInputStream(abundance_path);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					out.write("\t" + split2[0]);
				}
			}
			in.close();
			out.write("\n");
			fstream = new FileInputStream(abundance_lst);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String abundance_path = split[2];
				//out.write("kallisto quant -i " + index + " " + split[0] + " " + split[1] + " -o " + split[2] + "_kallisto\n");
				out.write(split[2]);
				
				samples.put(split[2], split[2]);
				FileInputStream fstream2 = new FileInputStream(abundance_path);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					//map.put(split[2] + "\t" + split2[0], split2[4]);
					out.write("\t" + split2[4]);
					
				}
				out.write("\n");
				in2.close();
			}
			in.close();
			
			out.close();
			/*
			Iterator itr = samples.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write("\t" + sample);
			}
			out.write("\n");
			Iterator itr2 = transcript.keySet().iterator();
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
			out.close();
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
