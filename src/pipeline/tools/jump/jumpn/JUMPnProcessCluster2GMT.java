package pipeline.tools.jump.jumpn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JUMPnProcessCluster2GMT {

	public static String type() {
		return "JUMPn";
	}
	public static String description() {
		return "Load a list of cluster file and extract the top genes into a gmt file.";
	}
	public static String parameter_info() {
		return "[listOfGeneClusterFile] [num_cutoff] [outputGMTFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String listOfClusterFile = args[0];
			int num_cutoff = new Integer(args[1]);
			String outputGMTFile = args[2];
			
			FileWriter fwriter = fwriter = new FileWriter(outputGMTFile);;
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap gene_list_map = new HashMap();
			FileInputStream fstream = new FileInputStream(listOfClusterFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String pathwayName = split[0];
				String filePath = split[1];
				LinkedList list = new LinkedList();
				FileInputStream fstream2 = new FileInputStream(filePath);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split_str2 = str2.split("\t");
					if (list.size() <= num_cutoff) {
						list.add(split_str2[0]);
					}
				}
				in2.close();
				gene_list_map.put(pathwayName, list);
			}
			in.close();
			Iterator itr = gene_list_map.keySet().iterator();
			while (itr.hasNext()) {
				String pathwayName = (String)itr.next();
				out.write(pathwayName + "\t" + pathwayName);
				LinkedList list = (LinkedList)gene_list_map.get(pathwayName);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene = (String)itr2.next();
					out.write("\t" + gene);
				}
				out.write("\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
