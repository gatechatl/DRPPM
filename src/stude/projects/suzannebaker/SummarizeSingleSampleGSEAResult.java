package stude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

public class SummarizeSingleSampleGSEAResult {
	public static String description() {
		return "Summarize Single Sample GSEA";
	}
	public static String type() {
		return "SSGSEA";
	}
	public static String parameter_info() {
		return "[inputMatrixFile.tab] [ssGSEAFolder] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String ssGSEAFolder = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap geneSet = new HashMap();
			HashMap[] maps = new HashMap[split_header.length];
			for (int i = 0; i < maps.length; i++) {
				maps[i] = new HashMap();				
			}
			in.close();
			
			for (int i = 1; i < split_header.length; i++) {
				String sampleFile = ssGSEAFolder + "/" + split_header[i] + "/" + "gseapy.SingleSample.gene_sets.report.csv";
				fstream = new FileInputStream(sampleFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split(",");
					maps[i].put(split[0], split[2]);
					geneSet.put(split[0], split[0]);
				}
			}
			out.write("SampleName");
			//for (int i = 1; i < split_header.length; i++) {
			Iterator itr = geneSet.keySet().iterator();
			while (itr.hasNext()) {
				String geneSetName = (String)itr.next();
				out.write("\t" + geneSetName);
				//out.write("\t" + split_header[i]);	
			}
			//}
			out.write("\n");
			for (int i = 1; i < split_header.length; i++) {
				out.write(split_header[i]);
				itr = geneSet.keySet().iterator();
				while (itr.hasNext()) {
					String geneSetName = (String)itr.next();
					// out.write(geneSetName);
					
				
					String value = (String)maps[i].get(geneSetName);
					out.write("\t" + value);
					
				}				
				out.write("\n");
			}
		
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
