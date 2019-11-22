package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangConvertGeneSignature2GMT {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\PanImmune_GeneSet_Definition.gmt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String inputImmuneSignatureFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\PanImmune_GeneSet_Definition.txt";
			FileInputStream fstream = new FileInputStream(inputImmuneSignatureFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (map.containsKey(split[0])) {
					String text = (String)map.get(split[0]);
					text += "\t" + split[1];
					map.put(split[0], text);
				} else {
					map.put(split[0], split[1]);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				String line = (String)map.get(type);
				out.write(type + "\t" + type + "\t" + line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
