package NetworkTools.jung;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CalculateCentralityModifyDistance {

	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Calculate the centrality of a phosphorylation network";
	}
	public static String parameter_info() {
		return "[inputFile] [edgeType] [foldChangeFile] [outputFile]\nFoldChange must be between 0 and 1";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String edgeType = args[1];
			String foldChangeFile = args[2];
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String[] split_type = edgeType.split(",");
			HashMap nodes = new HashMap();
			HashMap edges = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				Graph_Algos GA1 = new  Graph_Algos();
				for (String type: split_type) {
					if (!split[0].equals(split[2])) {
						if (type.equals(split[1])) {
							nodes.put(split[0], split[0]);
							nodes.put(split[2], split[2]);
							edges.put(split[0] + "\t" + split[2], "");						
						}
					}
				}
			}
			in.close();

			
			HashMap foldChange = new HashMap();
			fstream = new FileInputStream(foldChangeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				foldChange.put(split[0], new Double(split[1]));
				
			}
			Graph_Algos GA1 = new  Graph_Algos();
			
			LinkedList<String> Distinct_Vertex = new LinkedList<String>();
			LinkedList<String> Source_Vertex = new LinkedList<String>();
			LinkedList<String> Target_Vertex = new LinkedList<String>();
			LinkedList<Double> Edge_Weight = new LinkedList<Double>();
			Iterator itr = nodes.keySet().iterator();
			while (itr.hasNext()) {
				String node = (String)itr.next();
				Distinct_Vertex.add(node);				
			}
			itr = edges.keySet().iterator();
			while (itr.hasNext()) {
				String edge_str = (String)itr.next();
				String[] edge_split = edge_str.split("\t");
				Source_Vertex.add(edge_split[0]);
				Target_Vertex.add(edge_split[1]);
				
				if (foldChange.containsKey(edge_split[0])) {
					double value = (Double)foldChange.get(edge_split[0]);
					if (value > 0) {
						Edge_Weight.add(1 / value);
					} else {
						Edge_Weight.add(1.0);
						
					}
				} else {
					System.out.println(edge_split[0] + " was not found so its weight was made neutral 1.0");
					Edge_Weight.add(1.0);
				}
			}
			//System.out.println("Closeness Centrality calculation ");
			out.write("GeneName\tClosenessCentralityScore\n");
			HashMap map = GA1.Closeness_Centrality_Score(Distinct_Vertex, Source_Vertex, Target_Vertex, Edge_Weight);
			itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String node = (String)itr.next();
				double value = (Double)map.get(node);
				out.write(node + "\t" + value + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

