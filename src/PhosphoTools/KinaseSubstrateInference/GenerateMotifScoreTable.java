package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateMotifScoreTable {


	public static String description() {
		return "Generate the motif score table based on the matrix file";
	}
	public static String type() {
		return "KINASESUBSTRATE";
	}
	public static String parameter_info() {
		return "[inputMatrix] [uniprotPSSMLoc] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrix = args[0];
			String uniprotPSSMLoc = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap kinases = new HashMap();
			for (int i = 1; i < split_header.length; i = i + 2) {
				kinases.put(split_header[i].split("_")[0].toUpperCase(), -1);
			}
			out.write("Motif");
			Iterator itr = kinases.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				out.write("\t" + kinase + "\t" + kinase);
			}
			out.write("\t" + split_header[split_header.length - 1] + "\n");
			int count = 0;
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				count++;
				String accession = split[0].split("_")[1];
				String tag = accession + "_" + split[0].split("_")[2].replaceAll("S", "").replaceAll("T", "").replaceAll("Y", "");
				File f = new File(uniprotPSSMLoc + "/" + accession + ".txt");
				if (f.exists()) {
					out.write(split[0]);
					FileInputStream fstream2 = new FileInputStream(uniprotPSSMLoc + "/" + accession + ".txt");
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					String header2 = in2.readLine();
					String[] split_header2 = header2.split("\t");
					//LinkedList index = new LinkedList();
					for (int i = 1; i < split_header2.length; i++) {
						String motif_kinase = split_header2[i].split("_")[0];
						itr = kinases.keySet().iterator();
						while (itr.hasNext()) {
							String kinase = (String)itr.next();
							if (kinase.equals(motif_kinase)) {
								kinases.put(kinase,  i);
								//index.add(i);
							}
						}						
					}
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						if (split2[0].equals(tag)) {							
							itr = kinases.keySet().iterator();
							while (itr.hasNext()) {
								String kinase = (String)itr.next();
								
								int index = (Integer)kinases.get(kinase);
								if (index >= 0) {
									out.write("\t" + split2[index] + "\t" + split2[index]);
								} else {
									out.write("\tNA\tNA");
								}
							}
							break;
						}
					}
					in2.close();
					out.write("\t" + split[split.length - 1] + "\n");
				}
				if (count % 1000 == 0) {
					System.out.println(count);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
