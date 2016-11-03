package gene_network_display;

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
 * 
 * @author tshaw
 *
 */
public class NetworkNodeHighlight {

	public static String parameter_info() {
		return "[nodemeta_file] [inputFile] [dataType] [cutoffs] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String nodemeta_file = args[0];
			String inputFile = args[1];
			String dataType = args[2];
			String cutoffs = args[3];
			String outputFile = args[4];
			
			FileInputStream fstream = new FileInputStream(nodemeta_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				NodeEntry entry = new NodeEntry();
				entry.NAME = split[0].toUpperCase();
				entry.WEIGHT = split[1];
				entry.OUTCOLOUR = split[2];
				entry.BACKCOLOUR = split[3];
				entry.XAXIS = split[4];
				entry.YAXIS = split[5];
				entry.SHAPE = split[6];
				entry.VALUE = split[7];
				
				map.put(entry.NAME, entry);
			}
			in.close();
			
			HashMap kinase = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));	
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				double pvalue = new Double(split[2]);
				if (kinase.containsKey(split[1])) {
					String[] split2 = ((String)kinase.get(split[1])).split("\t");
					if (pvalue < new Double(split2[2])) {
						kinase.put(split[1], str);
					}
				} else {
					kinase.put(split[1], str);
				}
			}
			
			Iterator itr = kinase.keySet().iterator();
			while (itr.hasNext()) {				
				String key = (String)itr.next();
				String str = (String)kinase.get(key);
				str = str.toUpperCase();
				System.out.println(str);
				String[] split = str.split("\t");								
				if (map.containsKey(split[1])) {
					
					NodeEntry entry = (NodeEntry)map.get(split[1]);				
					if (dataType.equals("PVALUE")) {

						String[] split_cutoffs = cutoffs.split(",");
						String rgb = "";
						int n = 0;
						if (split[0].contains("UP") && split[0].contains("DN")) {
							
						} else if (split[0].contains("UP")) {
							for (int i = split_cutoffs.length - 1; i >= 0; i--) {
								
								double cutoff = new Double(split_cutoffs[i]);
								if (new Double(split[2]) < cutoff) {
									System.out.println(entry.NAME + "\t" + "lower:");
									int ratio = new Double((1 - new Double((n)) / (split_cutoffs.length + 1)) * 230).intValue() + 1;
									rgb = "rgb(255, " + ratio + ", " + ratio + ")";
								}
								n++;
							}
						} else if (split[0].contains("DN")) {
							for (int i = split_cutoffs.length - 1; i >= 0; i--) {
								
								double cutoff = new Double(split_cutoffs[i]);
								if (new Double(split[2]) < cutoff) {
									System.out.println(entry.NAME + "\t" + "lower:");
									int ratio = new Double((1 - new Double((n)) / (split_cutoffs.length + 1)) * 230).intValue() + 1;
									rgb = "rgb(" + ratio + ", " + ratio + ", 255)";
								}
								n++;
							}
						}
						if (rgb.equals("")) {							
							rgb = "rgb(0, 255, 0)";							
						}
						
						entry.BACKCOLOUR = rgb;
						map.put(split[1], entry);
					}
					
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header + "\n");
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				NodeEntry entry = (NodeEntry)map.get(name);
				out.write(entry.generateLine() + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static class NodeEntry {	
		public String NAME = "";		
		public String WEIGHT = "";
		public String OUTCOLOUR = "";
		public String BACKCOLOUR = "";
		public String XAXIS = "";
		public String YAXIS = "";
		public String SHAPE = "";
		public String VALUE = "";		
		public String generateLine() {
			return NAME + "\t" + WEIGHT + "\t" + OUTCOLOUR + "\t" + BACKCOLOUR + "\t" + XAXIS + "\t" + YAXIS + "\t" + SHAPE + "\t" + VALUE;
		}
	}
}
