package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Need to rename the exon quantification matrix
 * @author tshaw
 *
 */
public class JinghuiZhangRenameTCGAExonQuantification {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String conversionFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\conversion\\conversion.txt";
			FileInputStream fstream = new FileInputStream(conversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[0]);				
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM_renamed.20190716.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM.20190201.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].contains("unc.edu.")) {
					String[] split_name = split[0].split("\\.");
					if (map.containsKey(split_name[2])) {
						String new_name = (String)map.get(split_name[2]);
						out.write(new_name + "\t" + split[1] + "\t" + split[2] + "\n");
					}
				} else if (split[0].contains("TCGA")) {
					String[] split_name = split[0].split("-");
					String new_name = split_name[0] + "-" + split_name[1] + "-" + split_name[2];
					out.write(new_name + "\t" + split[1] + "\t" + split[2] + "\n");
				} else {
					out.write(str + "\n");
				}
								
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
