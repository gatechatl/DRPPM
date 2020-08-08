package stjude.projects.jinghuizhang.hla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGenerateHLASnpLocationFrequent {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\common_frequent_HLA_snp.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\script_grab_common_frequent_snp_loc.r";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("library(rsnps)\n");
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String snpid = str;
				out.write(snpid + " = annotations(snp = c(\"" + snpid + "\"), output = 'metadata')\n");
				out.write("write(paste(" + snpid + "[1,2], " + snpid + "[2,2], " + snpid + "[3,2], sep=\" \"), file = \"\\\\\\\\gsc.stjude.org\\\\project_space\\\\zhanggrp\\\\AltSpliceAtlas\\\\common\\\\analysis\\\\HLA\\\\snploc\\\\combined.txt\", append = \"T\")\n");
				
			}
			in.close();
			out.close();							
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
