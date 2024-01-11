package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Check whether the gene list is consistent across all files
 * @author tshaw
 *
 */
public class FindOverlappingGeneList {
	
	public static String description() {
		return "Perform check whether the files have the same gene order. If satisfy the condidtion, then merge files into a single matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile1] [inputFile2] [inputFile3] [...]";
	}
	public static void execute(String[] args) {
		try {
			
			HashMap geneList = new HashMap();
			String first_file = args[0];			
			FileInputStream fstream = new FileInputStream(first_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split_header.length != split.length) {
					System.out.println("Number of samples does't match number of values in each row.");
					System.exit(0);;
				}
				if (!geneList.containsKey(split[0])) {
					geneList.put(split[0], 0);
				}
			}
			in.close();
			//System.out.print(split_header[0]);
			for (String inputFile: args) {
				int i = 0;
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();				
				split_header = header.split("\t");
				
				for (int j = 1; j < split_header.length; j++) {
					//System.out.print("\t" + split_header[j]);
				}
				
				/*
				while (in.ready()) {
					String str = in.readLine();
				
					String[] split = str.split("\t");
					if (split_header.length != split.length) {
						System.out.println("Number of samples does't match number of values in each row.");
						System.exit(0);;
					}
					if (!geneList.get(i).equals(split[0])) {
						System.out.println("GeneName Mismatch");
					}
					i++;
				}*/
				in.close();
			}
			//System.out.print("\n");
			FileInputStream[] fstream_all = new FileInputStream[args.length];
			DataInputStream[] din_all = new DataInputStream[args.length];
			BufferedReader[] in_all = new BufferedReader[args.length];
			int[] len_header = new int[args.length];
			for (int i = 0; i < args.length; i++) {
				fstream_all[i] = new FileInputStream(args[i]);
				din_all[i] = new DataInputStream(fstream_all[i]);
				in_all[i] = new BufferedReader(new InputStreamReader(din_all[i]));
			}

			 
			for (int i = 0; i < args.length; i++) {
				fstream_all[i] = new FileInputStream(args[i]);
				din_all[i] = new DataInputStream(fstream_all[i]);
				in_all[i] = new BufferedReader(new InputStreamReader(din_all[i]));
				header = in_all[i].readLine();
				len_header[i] = header.split("\t").length;
				
			}
			
			

			int found_count = 0;
			//System.out.print(geneName);
			for (int i = 0; i < args.length; i++) {
				HashMap file_gene_list = new HashMap();
				while (in_all[i].ready()) {
					String line = in_all[i].readLine();					
					String[] split_line = line.split("\t");
					String geneName = split_line[0];
					file_gene_list.put(geneName, geneName);
					
				}
				in_all[i].close();
				
				Iterator itr = file_gene_list.keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					if (geneList.containsKey(geneName)) {
						int count = (Integer)geneList.get(geneName);
						count++;
						geneList.put(geneName, count);
					}
				}
			}
			System.out.println("GeneSymbol");
			Iterator itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (geneList.containsKey(geneName)) {
					int count = (Integer)geneList.get(geneName);
					if (count == args.length) {
						System.out.println(geneName);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
