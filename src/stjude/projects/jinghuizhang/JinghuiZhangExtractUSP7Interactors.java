package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangExtractUSP7Interactors {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\PROTEOMICS\\PPI\\Combined\\BioPlex_String400_Inweb150_Biogrid_ULK1_USP7_COMPASS_MYC_TAL1.sif";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\USP7_Jurkat_shRNA\\proteomics\\limma\\USP7_interactors.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//System.out.println(split[0] + "\t" + split[2]);
				if (split[0].equals("USP7")) {
					map.put(split[2], split[2]);
				}
				//System.out.println(split[0] + "\t" + split[2]);
				if (split[2].equals("USP7")) {
					map.put(split[0], split[0]);
				}
			}
			in.close();
			
			out.write(">>USP7_PPI\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write(gene + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
