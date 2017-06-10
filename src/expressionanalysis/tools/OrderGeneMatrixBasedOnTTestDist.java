package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import org.apache.commons.math3.stat.inference.TTest;

import specialized.algorithm.SpecializedAlgorithms;
import Statistics.General.MathTools;

public class OrderGeneMatrixBasedOnTTestDist {

	public static String description() {
		return "Order the gene matrix based on the tdist";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [groupAFile] [groupBFile] [takeLog2Flag true/false] [order descending flag true/false] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];			
			String groupAFile = args[1];
			String groupBFile = args[2];
			boolean takeLog = Boolean.valueOf(args[3]);
			boolean orderDescending = Boolean.valueOf(args[4]);
			String outputFile = args[5];
			
			
			//[OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			in.close();
			
			LinkedList listA = new LinkedList();
			fstream = new FileInputStream(groupAFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String sample = in.readLine();
				listA.add(sample);
			}			
			in.close();
			
			LinkedList listB = new LinkedList();
			fstream = new FileInputStream(groupBFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String sample = in.readLine();
				listB.add(sample);
			}
			in.close();
			
			String[] header_split = header.split("\t");
			int indexA = 0;
			int indexB = 0;

			int[] groupA = new int[listA.size()];
			int[] groupB = new int[listB.size()];
			for (int index = 0; index < header_split.length; index++) {
				//System.out.println("sampleName: " + sampleName);
				if (listA.contains(header_split[index])) {
					groupA[indexA] = index;
					//System.out.println(indexA + "\t" + index);
					indexA++;
				}
				if (listB.contains(header_split[index])) {
					groupB[indexB] = index;
					indexB++;
				}
				index++;
			}
			TTest ttest = new TTest();
			
			HashMap map_line = new HashMap();
			HashMap map = new HashMap();
			// read the file's matrix again			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map_line.put(split[0], str);
				double[] valuesA = new double[listA.size()];
				double[] valuesB = new double[listB.size()];
				indexA = 0;
				indexB = 0;
				for (int i = 1; i < split.length; i++) {
					if (listA.contains(header_split[i])) {
						valuesA[indexA] = new Double(split[i]);
						if (takeLog) {
							valuesA[indexA] = MathTools.log2(valuesA[indexA]);
						}
						indexA++;
					}
					if (listB.contains(header_split[i])) {
						valuesB[indexB] = new Double(split[i]);
						if (takeLog) {
							valuesB[indexB] = MathTools.log2(valuesB[indexB]);
						}
						indexB++;
					}
				}
				double tdist = ttest.t(valuesA, valuesB);
				map.put(split[0], tdist);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header + "\n");
			LinkedList reverse = new LinkedList();
			TreeMap treeMap = SpecializedAlgorithms.sortMapByValue(map);
			Iterator itr = treeMap.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				reverse.add(geneName);
				String line = (String)map_line.get(geneName);
				if (orderDescending) {
					out.write(line + "\n");
				}
			}
			if (orderDescending) {
				out.close();
			} else {
				while (reverse.size() > 0) {
					String geneName = (String)reverse.removeLast();
					String line = (String)map_line.get(geneName);
					out.write(line + "\n");
				}
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
