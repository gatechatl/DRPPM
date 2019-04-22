package rnaseq.tools.singlecell.tenxgenomics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Read a folder from 10x Genomics and generate a FPKM matrix 
 * Program could be version dependent.
 * @author tshaw
 *
 */
public class TenXGenomics2Matrix {

	public static String parameter_info() {
		return "[10X filtered folder] [outputMatrixFile]";
	}
	public static String type() {
		return "10XGenomics";
	}
	public static String description() {		
		return "Read a folder from 10x Genomics and generate its FPKM matrix ";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folder = args[0];
			String outputFile = args[1];
			
			String barcodeFile = folder + "/barcodes.tsv"; 
			String genesFile = folder + "/genes.tsv";
			String matrixFile = folder + "/matrix.mtx";
			HashMap sampleNames = new HashMap();
			int index = 0;
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("geneID\tGeneSymbol");
			FileInputStream fstream = new FileInputStream(barcodeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				index++;
				sampleNames.put(index, str);
				out.write("\t" + str);
			}
			in.close();			
			
			out.write("\n");
			index = 0;			
			
			HashMap geneNames = new HashMap();
			fstream = new FileInputStream(genesFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				index++;
				geneNames.put(index, str);
			}
			in.close();
			
			HashMap gene2sample = new HashMap();
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				int geneID = new Integer(split[0]);
				int sampleID = new Integer(split[1]);
				int umiCount =  new Integer(split[2]);
				if (gene2sample.containsKey(geneID)) {
					HashMap sample2val = (HashMap)gene2sample.get(geneID);
					sample2val.put(sampleID, umiCount);
					gene2sample.put(geneID, sample2val);					
				} else {
					HashMap sample2val = new HashMap();
					sample2val.put(sampleID, umiCount);
					gene2sample.put(geneID, sample2val);
				}
			}
			in.close();
			
			for (int geneID = 1; geneID <= geneNames.size(); geneID++) {
				if (gene2sample.containsKey(geneID)) {
					String geneName = (String)geneNames.get(geneID);				
					out.write(geneName);
					HashMap sample2val = (HashMap)gene2sample.get(geneID);
					for (int sampleID = 1; sampleID <= sampleNames.size(); sampleID++) {
						String sampleName = (String)sampleNames.get(sampleID);
						int umi_count = 0;
						if (sample2val.containsKey(sampleID)) {
							umi_count = (Integer)sample2val.get(sampleID);						
						}
						out.write("\t" + umi_count);
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
