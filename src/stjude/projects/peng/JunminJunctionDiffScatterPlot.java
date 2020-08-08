package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JunminJunctionDiffScatterPlot {

	public static void main(String[] args) {
		
		try {
			
			String total_inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\TotalStranded_Junction\\ScatterPlot\\AD_6M_Total_Diff_Gene_Junction.txt";
			HashMap total = new HashMap();
			FileInputStream fstream = new FileInputStream(total_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				total.put(split[0], split[2]);				
			}
			in.close();
			
			String mRNA_inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\mRNA_Junction\\ScatterPlot\\AD_6M_mRNA_Diff_Gene_Junction.txt";
			HashMap mRNA = new HashMap();
			fstream = new FileInputStream(mRNA_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mRNA.put(split[0], split[2]);				
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\Integrate_Junctions\\mRNA_vs_total_junction_FC_output.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Junction\tmRNA_FC\tRiboZero_FC\n");
			Iterator itr = total.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (mRNA.containsKey(key)) {
					out.write(key + "\t" + mRNA.get(key) + "\t" + total.get(key) + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
