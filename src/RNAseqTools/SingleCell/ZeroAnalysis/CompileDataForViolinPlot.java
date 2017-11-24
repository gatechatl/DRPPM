package RNAseqTools.SingleCell.ZeroAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Generate data matrix for violin plot
 * @author tshaw
 *
 */
public class CompileDataForViolinPlot {

	public static String type() {
		return "SCRNASEQ";
	}
	public static String description() {
		return "Generate data matrix for violin plot";
	}
	public static String parameter_info() {
		return "[inputFile] [geneNameFile: file containing a list of genes] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputFile = args[0];
			String geneNameFile = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			HashMap filterGene = new HashMap();
			FileInputStream fstream = new FileInputStream(geneNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				filterGene.put(str, str);
			}
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				if (filterGene.containsKey(geneName)) {
					for (int i = 1; i < split.length; i++) {
						String sampleName = split_header[i];
						if (map.containsKey(sampleName)) {
							HashMap values = (HashMap)map.get(sampleName);
							values.put(geneName, new Double(split[i]));
							map.put(sampleName, values);
						} else {
							HashMap values = new HashMap();
							values.put(geneName, new Double(split[i]));
							map.put(sampleName, values);
						}
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tFPKM\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				HashMap values = (HashMap)map.get(sampleName);
				Iterator itr2 = values.keySet().iterator();
				while (itr2.hasNext()) {
					String geneName = (String)itr2.next();
					double value = (Double)values.get(geneName);
					out.write(sampleName + "\t" + MathTools.log2(value + 0.0001) + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
