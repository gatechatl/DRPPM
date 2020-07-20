package stjude.projects.jinghuizhang.hla;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangGenerateComprehensiveSNPTable {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\snploc\\combined.txt";
			String outputFileA = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\snploc\\combined_A.snv4";
			String outputFileC = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\snploc\\combined_C.snv4";
			String outputFileG = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\snploc\\combined_G.snv4";
			String outputFileT = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\HLA\\snploc\\combined_T.snv4";
								
			FileWriter fwriterA = new FileWriter(outputFileA);
			BufferedWriter outA = new BufferedWriter(fwriterA);			
			
			FileWriter fwriterC = new FileWriter(outputFileC);
			BufferedWriter outC = new BufferedWriter(fwriterC);			
				
			FileWriter fwriterG = new FileWriter(outputFileG);
			BufferedWriter outG = new BufferedWriter(fwriterG);			
			
			FileWriter fwriterT = new FileWriter(outputFileT);
			BufferedWriter outT = new BufferedWriter(fwriterT);			
	
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				if (!map.containsKey(str)) {
					if (split.length == 3) {
						String chr = split[1];
						String loc = split[2];
						String[] nucs = {"A", "C", "G", "T"};
						String nuc1 = "A";
						for (String nuc2: nucs) {
							if (!nuc1.equals(nuc2)) {
								outA.write("chr" + chr + "." + loc + "." + nuc1 + "." + nuc2 + "\n");
							}
						}
						nuc1 = "C";
						for (String nuc2: nucs) {
							if (!nuc1.equals(nuc2)) {
								outC.write("chr" + chr + "." + loc + "." + nuc1 + "." + nuc2 + "\n");
							}
						}
						nuc1 = "G";
						for (String nuc2: nucs) {
							if (!nuc1.equals(nuc2)) {
								outG.write("chr" + chr + "." + loc + "." + nuc1 + "." + nuc2 + "\n");
							}
						}
						nuc1 = "T";
						for (String nuc2: nucs) {
							if (!nuc1.equals(nuc2)) {
								outT.write("chr" + chr + "." + loc + "." + nuc1 + "." + nuc2 + "\n");
							}
						}
					}
				}
				map.put(str, str);
			}
			in.close();
			outA.close();
			outC.close();
			outG.close();
			outT.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
