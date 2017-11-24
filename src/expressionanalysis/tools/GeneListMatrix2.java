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

/**
 * Generate a matrix based on the gene list for generating heatmap
 * @author tshaw
 *
 */
public class GeneListMatrix2 {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Generate a matrix based on the gene list for generating heatmap. Note that it skips the first line in the genelist File";
	}
	public static String parameter_info() {
		return "[matrixFile] [pathwayFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String matrixFile = args[0];
			String pathwayFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(pathwayFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String pathwayName = in.readLine();
			LinkedList list2 = new LinkedList();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				/*String pathwayName = split[1];
				String[] genes = split[4].split(",");
				LinkedList list = new LinkedList();
				for (String gene: genes) {
					list.add(gene);
				}*/
				list2.add(split[0].toUpperCase());
			}
			in.close();
			map.put(pathwayName, list2);
			HashMap data_matrix = new HashMap();
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				data_matrix.put(split[0].toUpperCase(), str);
			}
			in.close();
			
			LinkedList finalList = new LinkedList();
			
			HashMap map2 = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				LinkedList list = (LinkedList)map.get(pathway);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					if (data_matrix.containsKey(gene)) {
						String line = (String)data_matrix.get(gene);
						String[] split = line.split("\t");
						if (map2.containsKey(split[0])) {
							int num = 2;
							String newLine = split[0] + "_" + num;
							while (map2.containsKey(newLine)) {
								num++;
								newLine = split[0] + "_" + num;
							}
							map2.put(newLine, newLine);
							for (int i = 1; i < split.length; i++) {
								newLine += "\t" + split[i];
							}
							finalList.add(newLine);
							
						} else {
							finalList.add(line);
							map2.put(split[0], split[0]);
						}
						
					}
				}
			}
			
			Iterator itr2 = finalList.iterator();
			while (itr2.hasNext()) {
				String line = (String)itr2.next();
				out.write(line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
