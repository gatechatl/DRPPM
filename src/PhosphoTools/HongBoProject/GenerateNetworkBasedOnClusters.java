package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.CommandLine;

/**
 * 
 * @author tshaw
 *
 */
public class GenerateNetworkBasedOnClusters {

	public static String parameter_info() {
		return "[inputClusterFile] [graphFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			String graphFile = args[1];
			String outputFolder = args[2];
			
			File f = new File(outputFolder);
			if (!f.exists()) {
				f.mkdir();
			}
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String cluster = split[0];
				String[] genes = split[1].split(",");
				if (genes.length - 1 >= 9) {
					for (int i = 0; i < genes.length; i++) {
						if (map.containsKey(cluster)) {							
							LinkedList list = (LinkedList)map.get(cluster);
							list.add(genes[i]);
							map.put(cluster, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(genes[i]);
							map.put(cluster, list);
						}
					}
				} else {
					for (int i = 0; i < genes.length - 1; i++) {
						if (map.containsKey("Other")) {							
							LinkedList list = (LinkedList)map.get("Other");
							list.add(genes[i]);
							map.put("Other", list);
						} else {
							LinkedList list = new LinkedList();
							list.add(genes[i]);
							map.put("Other", list);
						}
					}
				}
			}
			in.close();
			
			LinkedList graph = new LinkedList();
			fstream = new FileInputStream(graphFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				graph.add(str);				
			}
			in.close();
			
			FileWriter fwriter_step1 = new FileWriter(outputFolder + "/step1.sh");
			BufferedWriter out_step1 = new BufferedWriter(fwriter_step1);	
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String cluster = (String)itr.next();
				LinkedList genes = (LinkedList)map.get(cluster);
				
				FileWriter fwriter = new FileWriter(outputFolder + "/" + cluster + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);	
				
				Iterator itr2 = graph.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					if (genes.contains(split[1]) && genes.contains(split[3])) {
						out.write(split[1] + "\tconnect\t" + split[3] + "\n");
					}
					
				}
				out.close();
				out_step1.write("drppm -GenerateNodeMetaData " + cluster + ".txt > " + cluster + "_node.txt\n");
				out_step1.write("drppm -GenerateEdgeMetaData " + cluster + ".txt > " + cluster + "_edge.txt\n");
				out_step1.write("drppm -CreateNetworkDisplayComplex " + cluster + "_edge.txt " + cluster + "_node.txt " + cluster + " COSE 20 " + cluster + "\n");
				
			}
			out_step1.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
