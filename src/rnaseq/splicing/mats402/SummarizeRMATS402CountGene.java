package rnaseq.splicing.mats402;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SummarizeRMATS402CountGene {

	public static String type() {
		return "rMATS";
	}
	public static String description() {
		return "rMATS 4.0.2 result compare splicing result.";
	}
	public static String parameter_info() {
		return "[keyword] [summary file1...] [summary file2...] [...]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String keyword = args[0].replaceAll("\"", "");
			HashMap total_genes = new HashMap();
			HashMap sampleList = new HashMap();
			LinkedList sampleNames = new LinkedList();
			for (int i = 1; i < args.length - 1; i++) {
				if (!args[i].isEmpty()) {
					File f = new File(args[i]);
					if (f.exists()) {
						System.out.println("Load: " + args[i]);
						sampleNames.add(args[i]);
						FileInputStream fstream = new FileInputStream(args[i]);
						DataInputStream din = new DataInputStream(fstream);
						BufferedReader in = new BufferedReader(new InputStreamReader(din));			
						while (in.ready()) {
							String str = in.readLine();
							String[] split = str.split("\t");
							if (split[0].equals(keyword)) {
								for (String gene: split[1].split(",")) {
									total_genes.put(gene, gene);
									if (sampleList.containsKey(args[i])) {
										LinkedList list = (LinkedList)sampleList.get(args[i]);
										if (!list.contains(gene)) {
											list.add(gene);
										}
										sampleList.put(args[i], list);
									} else {
										LinkedList list = new LinkedList();
										list.add(gene);
										sampleList.put(args[i], list);
									}
								}
							}
						}
						in.close();		
					}
				}
			}
			System.out.println(keyword + " count:\t" + total_genes.size());
			System.out.print("Gene");
						
			Iterator itr = sampleNames.iterator();
			while (itr.hasNext()) {
				
				String sampleName = (String)itr.next();
				if (sampleList.containsKey(sampleName)) {
					System.out.print("\t" + sampleName);
				}
			}
			System.out.println();
			Iterator itr2 = total_genes.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				System.out.print(geneName);
				itr = sampleNames.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					boolean found = false;
					if (sampleList.containsKey(sampleName)) {
						LinkedList list = (LinkedList)sampleList.get(sampleName);
						if (list.contains(geneName)) {
							found = true;
						}
						System.out.print("\t" + found);
					}
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
