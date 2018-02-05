package integrate.summarytable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class IntegrationAddGeneAnnotation {

	public static void main(String[] args) {
		
		
	}
	
	public static String type() {
		return "INTEGRATION";
	}
	public static String description() {
		return "Append gene annotation information to matrix";
	}
	public static String parameter_info() {
		return "[inputFile] [geneListFile] [index]";
	}
	public static void execute(String[] args) {
		try {
									
			
			String inputFile = args[0];
			HashMap map = generateHashMapList(args[1]);
			
			int index = new Integer(args[2]);
			Iterator itr = map.keySet().iterator();
			String additional_header = "";
			LinkedList tags = new LinkedList();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				tags.add(tag);
				additional_header += "\t" + tag;
			}
			additional_header += "\tMETAINFOHIT";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine() + additional_header;
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				boolean isTRUE = false;
				String[] split = str.split("\t");
				String geneName = split[index].replaceAll("\"", "").split("\\(")[0];
				String annotation = "";
				itr = tags.iterator();
				while (itr.hasNext()) {
					String tag = (String)itr.next();
					HashMap geneList = (HashMap)map.get(tag);
					if (geneList.containsKey(geneName)) {
						annotation += "\tTRUE";
						isTRUE = true;
					} else {
						annotation += "\tFALSE";	
					}
				}
				if (isTRUE) {
					annotation += "\tTRUE";
				} else {
					annotation += "\tFALSE";
				}
				System.out.println(str + annotation);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generateHashMapList(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream2 = new FileInputStream(inputFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str = in2.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				String fileName = split[1];
				HashMap geneList = createGeneList(fileName);
				map.put(name, geneList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap createGeneList(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
