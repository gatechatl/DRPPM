package rnaseq.tools.summary;

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
 * Combines all the limma result
 * @author tshaw
 *
 */
public class CombineLIMMAResultExpression {

	public static String parameter_info() {
		return "[inputMatrix1,inputMatrix2...] [inputLIMMA1,inputLIMMA2,...] [tagName1,tagName2...] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputMatrixStr = args[0];
			String inputLIMMAStr = args[1];
			String tagStr = args[2];
			String outputFile = args[3];
			
			String[] inputMatrixStrSplit = inputMatrixStr.split(",");
			String[] inputLIMMAStrSplit = inputLIMMAStr.split(",");
			String[] tagStrSplit = tagStr.split(",");
			
			if (inputLIMMAStrSplit.length != tagStrSplit.length) {
				System.out.println("Unmatching LIMMA and Tags");
				System.exit(0);
			}
			LinkedList sampleNameMap = new LinkedList();
			HashMap geneNameMap = new HashMap();
			HashMap inputMatrixMap = new HashMap();
			HashMap[] inputLIMMAMap = new HashMap[inputLIMMAStrSplit.length];
			String[] tagNameMap = new String[inputLIMMAStrSplit.length];
			for (int i = 0; i < inputMatrixStrSplit.length; i++) {
				String inputMatrix = inputMatrixStrSplit[i];
				sampleNameMap = grabSampleName(sampleNameMap, inputMatrix);
				geneNameMap = grabGeneName(geneNameMap, inputMatrix);
				inputMatrixMap = grabData(inputMatrixMap, inputMatrix);
			}
			
			for (int i = 0; i < inputLIMMAStrSplit.length; i++) {
				inputLIMMAMap[i] = new HashMap();
				String inputLIMMA = inputLIMMAStrSplit[i];				
				inputLIMMAMap[i] = grabComparison(inputLIMMA);
			}			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene");
			Iterator itr2 = sampleNameMap.iterator();
			while (itr2.hasNext()) {
				String gene = (String)itr2.next();
				out.write("\t" + gene);
			}
			for (int i = 0; i < tagStrSplit.length; i++) {
				out.write("\t" + tagStrSplit[i] + "_FoldChange\t" + tagStrSplit[i] + "_AdjPval");
			}
			out.write("\n");
			
			Iterator itr = geneNameMap.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				Iterator itr3 = sampleNameMap.iterator();
				while (itr3.hasNext()) {
					String sampleName = (String)itr3.next();
					String tag = sampleName + "_" + geneName;
					String value = "NA";
					if (inputMatrixMap.containsKey(tag)) {
						value = (String)inputMatrixMap.get(tag);
					}
					out.write("\t" + value);					
				}
				for (int i = 0; i < inputLIMMAStrSplit.length; i++) {
					String stuff = "NA\tNA";
					if (inputLIMMAMap[i].containsKey(geneName)) {
						stuff = (String)inputLIMMAMap[i].get(geneName);
					}
					out.write("\t" + stuff);
										
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedList grabSampleName(LinkedList map, String matrixFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split = header.split("\t");
			for (int i = 1; i < split.length; i++) {
				map.add(split[i]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabGeneName(HashMap map, String matrixFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabComparison(String comparisonFile) {
		
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(comparisonFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String value = split[1] + "\t" + split[5];
				map.put(split[0].replaceAll("\"", ""), value);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabData(HashMap map, String matrixFile) {
		try {
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] samples = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					map.put(samples[i] + "_" + split[0], split[i]);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
