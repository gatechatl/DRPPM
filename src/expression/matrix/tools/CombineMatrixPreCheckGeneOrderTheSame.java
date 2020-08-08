package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class CombineMatrixPreCheckGeneOrderTheSame {

	
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
			LinkedList geneList = new LinkedList();
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
				geneList.add(split[0]);
			}
			in.close();
	
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
			
			Iterator itr = geneList.iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				
				//System.out.print(geneName);
				for (int i = 0; i < args.length; i++) {
					String line = in_all[i].readLine();					
					String[] split_line = line.split("\t");
					if (len_header[i] != split_line.length) {
						System.out.println("Number of samples does't match number of values in each row.");
						System.exit(0);;
					}
					if (!split_line[0].equals(geneName)) {
						System.out.println("GeneName Mismatch");
						System.exit(0);
					}
				}
			}
			System.out.println("So far so good");
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
		
}
