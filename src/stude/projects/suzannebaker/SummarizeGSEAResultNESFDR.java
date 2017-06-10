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

public class SummarizeGSEAResultNESFDR {
	public static String description() {
		return "Summarize Single Sample GSEA";
	}
	public static String type() {
		return "SSGSEA";
	}
	public static String parameter_info() {
		return "[inputSingleSampleGSEAFile] [sampleInfoFile] [ssGSEAFolder] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String sampleInfoFile = args[1];
			String ssGSEAFolder = args[2];
			String outputFile = args[3];
			int count = 0;
			HashMap sample2group = new HashMap();
			LinkedList list = new LinkedList();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(sampleInfoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2group.put(split[0], split[4]);				
				list.add(split[4]);
				count++;
			}
			in.close();
			
			
			//String[] split_header = header.split("\t");
			HashMap geneSet = new HashMap();
			HashMap[] maps_enrichment_score = new HashMap[count];
			HashMap[] maps_fdr = new HashMap[count];
			//HashMap[] maps = new HashMap[count];
			for (int i = 0; i < count; i++) {
				//maps[i] = new HashMap();	
				maps_enrichment_score[i] = new HashMap();
				maps_fdr[i] = new HashMap();
			}
			in.close();
			
			int i = 0;
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String groupName = (String)itr.next();				
				String sampleFile = ssGSEAFolder + "/" + groupName + "/" + "gseapy.gsea.gene_sets.report.csv";
				fstream = new FileInputStream(sampleFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				System.out.println(sampleFile);
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split(",");
					if (split.length > 4) {
						maps_enrichment_score[i].put(split[0], split[2]);
						maps_fdr[i].put(split[0], split[4]);
						geneSet.put(split[0], split[0]);
					} else {
						System.out.println(str);
					}
				}
				in.close();
				i++;
			}
			
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (i = 1; i < split.length; i++) {
					
					String fdr = (String)maps_fdr[i - 1].get(split[0]);
					if (fdr.equals("")) {
						fdr = "NA";
					}
					String nes = (String)maps_enrichment_score[i - 1].get(split[0]);
					if (nes.equals("")) {
						nes = "NA";
					}
					out.write("\t" + split[i] + "," + fdr + "," + nes);
				}
				out.write("\n");
				//list.add(split[4]);
				//count++;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
