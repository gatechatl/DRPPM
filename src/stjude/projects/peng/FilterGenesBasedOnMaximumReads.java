package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Customized script for filtering reads for splicing deficiency. For duplicated geneNames pick the one with the higher read count
 * @author tshaw
 *
 */
public class FilterGenesBasedOnMaximumReads {
	public static String description() {
		return "Customized script for filtering reads for splicing deficiency. For duplicated geneNames pick the one with the higher read count.";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputMatrixFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String accession = split[1];				
				if (map.containsKey(geneName)) {
					LinkedList list = (LinkedList)map.get(geneName);
					list.add(str);
					map.put(geneName, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(str);
					map.put(geneName, list);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				LinkedList list = (LinkedList)map.get(geneName);
				Iterator itr2 = list.iterator();
				String max_line = "";
				int total = 0;
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					int count = 0;
					count += new Integer(split[3]);
					count += new Integer(split[5]);
					count += new Integer(split[8]);
					count += new Integer(split[10]);
					count += new Integer(split[13]);
					count += new Integer(split[15]);
					count += new Integer(split[18]);
					count += new Integer(split[20]);
					count += new Integer(split[23]);
					count += new Integer(split[25]);
					count += new Integer(split[28]);
					count += new Integer(split[30]);
					if (total < count) {
						total = count;
						max_line = line;
					}
				}				
				System.out.println(max_line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
