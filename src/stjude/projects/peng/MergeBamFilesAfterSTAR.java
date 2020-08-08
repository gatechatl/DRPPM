package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Merge the bam files after being processed by STAR. The generated script should be moved to the folder where the bam files are located.
 * @author tshaw
 *
 */
public class MergeBamFilesAfterSTAR {

	
	public static String description() {
		return "Merge the bam files after being processed by STAR. The generated script should be moved to the folder where the bam files are located.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[folder] [indices]";
	}
	public static void execute(String[] args) {
		
		
		try {
			
			String folder = args[0];
			String indices = args[1];
			String[] split_indices = indices.split(",");
			HashMap map = new HashMap();
			File f = new File(folder);
			if (f.exists()) {
				for (File file: f.listFiles()) {
					if (file.getName().contains(".bam")) {
						String[] tags = file.getName().split("_");
						String name = "";
						for (String index_str: split_indices) {
							int index = new Integer(index_str);
							if (name.equals("")) {
								name = tags[index];;
							} else {
								name += "_" + tags[index];
							}						
						}
						if (map.containsKey(name)) {
							LinkedList list = (LinkedList)map.get(name);
							list.add(file.getName());
							map.put(name, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(file.getName());
							map.put(name, list);
						}
					}
				}
			}
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String line = "samtools merge " + name + ".bam";
				LinkedList list = (LinkedList)map.get(name);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String fileNames = (String)itr2.next();
					line += " " + fileNames;
				}
				System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
