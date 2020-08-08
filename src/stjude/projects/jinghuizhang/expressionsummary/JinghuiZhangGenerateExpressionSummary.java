package stjude.projects.jinghuizhang.expressionsummary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangGenerateExpressionSummary {



	public static String type() {
		return "Immune";
	}
	public static String description() {
		return "Generate a summarized expression matrix.";
	}
	public static String parameter_info() {
		return "[inputFileMatrix] [outputSummarizedMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			

			String outputFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\updated_pcgp_fpkm_zero_chemokine_sjid.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name");
			String inputConversionFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\updated_pcgp_fpkm_zero_chemokine.txt";
			FileInputStream fstream = new FileInputStream(inputConversionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			HashMap names = new HashMap();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				String type = split_header[i].split("_")[0].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
				names.put(type, type);
			}
			Iterator itr = names.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				out.write("\t" + name);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				HashMap map = new HashMap();
				for (int i = 1; i < split.length; i++) {
					String type = split_header[i].split("_")[0].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
					if (map.containsKey(type)) {
						LinkedList list = (LinkedList)map.get(type);
						list.add(split[i]);
						map.put(type, list);						
					} else {
						LinkedList list = new LinkedList();
						list.add(split[i]);
						map.put(type, list);
					}
				}
				itr = names.keySet().iterator();
				while (itr.hasNext()) {
					String name = (String)itr.next();
					LinkedList list = (LinkedList)map.get(name);
					double[] values = MathTools.convertListStr2Double(list);
					double median = MathTools.median(values);
					out.write("\t" + median);
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
