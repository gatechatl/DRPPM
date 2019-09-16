package proteomics.phospho.tools.gsea;

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

/**
 * Convert the group txt to a gmt file
 * @author tshaw
 *
 */
public class ConvertKinaseGroupTxt2Gmt {

	public static String type() {
		return "GSEA";
	}
	public static String description() {
		return "Convert the group txt to a gmt.";
	}
	public static String parameter_info() {
		return "[pathFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String pathFile = args[0];
			String outputFile = args[1];
			
			HashMap map = new HashMap();
			String name = "";
			File files = new File(pathFile);
			for (File file: files.listFiles()) {
				FileInputStream fstream = new FileInputStream(file.getPath());
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					if (str.contains(">")) {
						name = str.replaceAll(">", "");
					} else {
						if (map.containsKey(name)) {
							LinkedList list = (LinkedList)map.get(name);
							list.add(str);
							map.put(name, list);						
						} else {
							LinkedList list = new LinkedList();
							list.add(str);
							map.put(name, list);
						}
					}
				}
				in.close();
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				out.write(name + "\t" + name);
				LinkedList list = (LinkedList)map.get(name);
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
