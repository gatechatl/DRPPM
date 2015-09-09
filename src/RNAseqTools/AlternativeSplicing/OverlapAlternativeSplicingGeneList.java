package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class OverlapAlternativeSplicingGeneList {

	public static void execute(String[] args) {
		
		try {
			
			String inputFilePath = args[0];
			String inputFile2Path = args[1];
			double fdrCutoff = new Double(args[2]);
			double incRatioCutoff = new Double(args[3]);
			String outputFolder = args[4];
			
			/*String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);*/

			
			String[] files = {"A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt"};
			for (String file: files) {
				HashMap map = new HashMap();
				
				
				FileInputStream fstream = new FileInputStream(inputFilePath + "/" + file);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double fdr = new Double(split[split.length - 4]);
					double incLevel = new Double(split[split.length - 1]);
					if (fdr < fdrCutoff && Math.abs(incLevel) > incRatioCutoff) {
						map.put(split[1], str);
					}
				}
				in.close();
				
				HashMap map2 = new HashMap();
				fstream = new FileInputStream(inputFile2Path + "/" + file);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double fdr = new Double(split[split.length - 4]);
					double incLevel = new Double(split[split.length - 1]);
					if (fdr < fdrCutoff && Math.abs(incLevel) > incRatioCutoff) {
						map2.put(split[1], str);
					}
				}
				in.close();
				
				HashMap overlap = new HashMap();
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					if (map2.containsKey(key)) {
						
						overlap.put(key, key);
					}				
				}
				
				FileWriter fwriter3 = new FileWriter(outputFolder + "/" + file + ".diff.txt");
				BufferedWriter out3 = new BufferedWriter(fwriter3);
				out3.write("inputFile\tDifference\n");
				
				FileWriter fwriter2 = new FileWriter(outputFolder + "/" + file + ".overlap_cases.txt");
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				out2.write("inputFile\t" + header + "\n");
				
				FileWriter fwriter = new FileWriter(outputFolder + "/" + file + ".unique.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("inputFile\t" + header + "\n");
				itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					if (!overlap.containsKey(key)) {						
						String stuff = (String)map.get(key);
						out.write(inputFilePath + "\t" + stuff + "\n");
					} else {
						String stuff1 = (String)map.get(key);
						String stuff2 = (String)map2.get(key);
						String[] split1 = stuff1.split("\t");
						String[] split2 = stuff2.split("\t");
						String gene = split1[2];
						double incLevel1 = Math.abs(new Double(split1[split1.length - 1]));
						double incLevel2 = Math.abs(new Double(split2[split2.length - 1]));
						out2.write(inputFilePath + "\t" + stuff1 + "\n");
						out2.write(inputFile2Path + "\t" + stuff2 + "\n");
						out3.write(gene + "\t" + (incLevel1 - incLevel2) + "\n");
					}
					
				}
				itr = map2.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					if (!overlap.containsKey(key)) {						
						String stuff = (String)map2.get(key);
						out.write(inputFile2Path + "\t" + stuff + "\n");
					}				
				}
				out2.close();
				out3.close();
				out.close();
				System.out.println(file + "\t" + map.size() + "\t" + map2.size() + "\t" + overlap.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
