package rnaseq.mapping.tools.star;

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

import misc.CommandLine;

/**
 * Generate mapping's input file
 * @author tshaw
 *
 */
public class MergeBamFiles {

	public static String description() {
		return "MergeBamFiles";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFile] [index of R1 or R2 split by \"_\"]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folder = args[0];
			//String split_str = args[1];
			String[] indexes = args[1].split(",");
			//int index = new Integer(args[1]);			
			
			File files = new File(folder);
			HashMap map = new HashMap();
			for (File f: files.listFiles()) {
				
				String fileName = f.getName();
				String[] split = fileName.split("_");
				String name = "";
				for (String idx: indexes) {
					int index = new Integer(idx);  
					if (split.length > (index + 1)) {
						if (name.equals("")) {
							name = split[index];
						} else {
							name += "_" + split[index];
						}
					}
				}
				if (fileName.contains(".bam")) {
					map.put(name, new LinkedList());
				}
			}
			for (File f: files.listFiles()) {				
				String fileName = f.getName();
				if (fileName.contains(".bam")) {
					String[] split = fileName.split("_");
					String name = "";
					for (String idx: indexes) {
						int index = new Integer(idx);  
						if (split.length > (index + 1)) {
							if (name.equals("")) {
								name = split[index];
							} else {
								name += "_" + split[index];
							}
						}
					}
										
					if (map.containsKey(name)) {
						LinkedList list = (LinkedList)map.get(name);
						list.add(f.getPath());
						map.put(name, list);
					}							
				
				}
			}
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				LinkedList list = (LinkedList)map.get(sampleName);
				File sampleFolder = new File(sampleName);
				if (!sampleFolder.exists()) {
					sampleFolder.mkdir();
				}
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String fileName = (String)itr2.next();
					File f = new File(fileName);					
					String command = "ln -s " + fileName + " " + sampleName + "/" + f.getName();
					CommandLine.executeCommand(command);
				}
				String script = "cd " + sampleName + "\n";
				script += "samtools merge " + sampleName + ".bam *.bam\n";
				FileWriter fwriter = new FileWriter(sampleName + ".sh");
	            BufferedWriter out = new BufferedWriter(fwriter);
	            out.write(script);
	            out.close();
	            System.out.println("sh " + sampleName + ".sh");
	            
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

