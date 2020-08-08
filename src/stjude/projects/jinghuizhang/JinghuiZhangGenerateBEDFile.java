package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangGenerateBEDFile {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap sample_list = new HashMap();
			String sample_inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\N-terminal_C-terminal\\MidPoint\\STIL_TAL_Half_samplelist.txt";
			//String sample_inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\N-terminal_C-terminal\\Nterminal\\N-terminal_samplelist.txt";
			FileInputStream fstream = new FileInputStream(sample_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				sample_list.put(str, str);
			}
			in.close();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\N-terminal_C-terminal\\chromosome6_del_pre_bed_raw.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (sample_list.containsKey(split[0])) {
					if (map.containsKey(split[0])) {
						LinkedList list = (LinkedList)map.get(split[0]);
						list.add(str);
						map.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						list.add(str);
						map.put(split[0], list);
					}
				}
			}
			in.close();			
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				LinkedList list = (LinkedList)map.get(name);
				String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\N-terminal_C-terminal\\MidPoint\\" + name + ".bed";
				//String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\N-terminal_C-terminal\\Nterminal\\" + name + ".bed";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("track name=MidPoint_" + name + " description=\"" + name + "\" useScore=1\"\n");
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split_line = line.split("\t");
					if (split_line.length > 3) {
						out.write(split_line[1] + "\t" + split_line[2] + "\t" + split_line[3] + "\t" + split_line[0] + "\n");
					}
				}
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
