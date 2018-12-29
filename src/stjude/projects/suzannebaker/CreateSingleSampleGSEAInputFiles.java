package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import specialized.algorithm.SpecializedAlgorithms;
import specialized.algorithm.ValueComparator;

/**
 * Create the python version of the single sample GSEA input files
 * @author tshaw
 *
 */
public class CreateSingleSampleGSEAInputFiles {

	public static String description() {
		return "Generate input file for ssGSEA python version";
	}
	public static String type() {
		return "SSGSEA";
	}
	public static String parameter_info() {
		return "[inputMatrix] [gmtFile] [outputFolder] [outputPythonFolder] [outputScript]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String gmtFile = args[1];
			String outputFolder = args[2];
			String outputPythonFolder = args[3];
			String outputScript = args[4];
			FileWriter fwriter_script = new FileWriter(outputScript);
			BufferedWriter out_script = new BufferedWriter(fwriter_script);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			HashMap[] maps = new HashMap[split_header.length];
			TreeMap[] treeMap = new TreeMap[split_header.length];
			for (int i = 0; i < maps.length; i++) {
				maps[i] = new HashMap();				
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					maps[i].put(split[0], new Double(split[i]));
				}
				//System.out.println(split[0] + "\t" + split.length);				
			}
			in.close();			
			for (int i = 0; i < maps.length; i++) {
				treeMap[i] = SpecializedAlgorithms.sortMapByValue(maps[i]);
			}
			//System.out.println("mapsize: " + maps[1].size());
			//System.out.println("treeMapSize: " + treeMap[1].size());
			
			for (int i = 1 ; i < maps.length; i++) {
				String pythonFile = outputPythonFolder + "/" + split_header[i] + ".py";
				pythonFile.replaceAll("\\.\\.",  "\\.");
				String rankFile = outputFolder + "/" + split_header[i] + ".rnk";
				String outputDir = outputFolder + "/" + split_header[i];
				FileWriter fwriter = new FileWriter(rankFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				Iterator itr = treeMap[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					//System.out.println(geneName);
					double value = (Double)maps[i].get(geneName);
					//if (value >= 1.0) {
					out.write(geneName + "\t" + value + "\n");
					//}
					//System.out.println(geneName + "\t" + value);
				}
				out.close();
				
				fwriter = new FileWriter(pythonFile);
				out = new BufferedWriter(fwriter);
				out.write("import gseapy\n");
				out.write("gseapy.ssgsea(data='" + rankFile + "', gene_sets='" + gmtFile + "',outdir='" + outputDir + "',min_size=1,max_size=20000)\n");
				out.close();
				out_script.write("python " + pythonFile + "\n");
				
			}
			out_script.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
