package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Rename the expression sample name to tcga sampleName
 * @author tshaw
 *
 */
public class JinghuiZhangRenameTCGAMatrix {

	public static String description() {
		return "Rename the expression sample name to tcga sampleName.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[expression file] [referenceFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String conversionFile = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM_renamed.20190716.txt";
			FileInputStream fstream = new FileInputStream(conversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[2]);				
			}
			in.close();
			
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\c8_mod.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList id = new LinkedList();
			String inputFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\c8.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				if (map.containsKey(split_header[i].split("_")[1])) {
					id.add(i);
					out.write("\t" + split_header[i].split("_")[1]);
				}
				
				
			}
			out.write("\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				Iterator itr = id.iterator();
				while (itr.hasNext()) {
					int index = (Integer)itr.next();
					out.write("\t" + split[index]);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
