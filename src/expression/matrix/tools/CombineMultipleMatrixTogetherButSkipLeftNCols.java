package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Perform check whether the files have the same gene order.
 * If contain same gene order, then merge files into a single matrix
 * 
 * @author tshaw
 *
 */
public class CombineMultipleMatrixTogetherButSkipLeftNCols {
	
	public static String description() {
		return "Perform check whether the files have the same gene order. If satisfy the condidtion, then merge files into a single matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[Keep the first N col the same] [inputFile1] [inputFile2] [inputFile3] [...]";
	}
	public static void execute(String[] args) {
		try {
			LinkedList geneList = new LinkedList();
			String first_file = args[1];		
			int n_cols = new Integer(args[0]);
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
				String name = split[0];
				for (int i = 1; i < n_cols; i++) {
					name += "\t" + split[i];
				}
				geneList.add(name);
			}
			in.close();
			
			String header_name = split_header[0];
			for (int i = 1; i < n_cols; i++) {
				header_name += "\t" + split_header[i];
			}
			System.out.print(header_name);
			for (int i = 1; i < args.length; i++) {
				
				String inputFile = args[i];
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();				
				split_header = header.split("\t");
				
				for (int j = n_cols; j < split_header.length; j++) {
					System.out.print("\t" + split_header[j]);
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
			System.out.print("\n");
			FileInputStream[] fstream_all = new FileInputStream[args.length];
			DataInputStream[] din_all = new DataInputStream[args.length];
			BufferedReader[] in_all = new BufferedReader[args.length];
			int[] len_header = new int[args.length];
			for (int i = 1; i < args.length; i++) {
				fstream_all[i] = new FileInputStream(args[i]);
				din_all[i] = new DataInputStream(fstream_all[i]);
				in_all[i] = new BufferedReader(new InputStreamReader(din_all[i]));
			}

			 
			for (int i = 1; i < args.length; i++) {
				fstream_all[i] = new FileInputStream(args[i]);
				din_all[i] = new DataInputStream(fstream_all[i]);
				in_all[i] = new BufferedReader(new InputStreamReader(din_all[i]));
				header = in_all[i].readLine();
				len_header[i] = header.split("\t").length;
				
			}
			
			Iterator itr = geneList.iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				
				System.out.print(geneName);
				for (int i = 1; i < args.length; i++) {
					String line = in_all[i].readLine();					
					String[] split_line = line.split("\t");
					if (len_header[i] != split_line.length) {
						System.out.println("Number of samples does't match number of values in each row.");
						System.exit(0);;
					}
					//if (!split_line[0].equals(geneName)) {
					//	System.out.println("GeneName Mismatch");
					//}
					for (int j = n_cols; j < split_line.length; j++) {
						System.out.print("\t" + split_line[j]);
					}
				}
				System.out.print("\n");
			}
			for (int i = 1; i < args.length; i++) {
				in_all[i].close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
