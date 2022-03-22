package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class MoffittFixingDuplicatedCounts {


	public static String description() {
		return "Correcting the recount3 outputs with duplicated uniq and multiple junction counts in SJ.out.tab";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[folder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String folder = args[0];
			File files = new File(folder);
			for (File f: files.listFiles()) {
				if (f.getName().contains("SJ.out.tab") && !f.getName().contains("tmp")) {
					
					String inputFile = f.getPath();
					String outputFile = inputFile + ".tmp";
					FileWriter fwriter = new FileWriter(outputFile);
					BufferedWriter out = new BufferedWriter(fwriter);
					
					
					FileInputStream fstream = new FileInputStream(inputFile);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						
						if (split[6].equals(split[7])) {
							out.write(split[0]);;
							for (int i = 1; i < 6; i++) {
								out.write("\t" + split[i]);
							}
							out.write("\t0");
							for (int i = 7; i < split.length; i++) {
								out.write("\t" + split[i]);
							}
							out.write("\n");
						} else {
							out.write(str + "\n");
						}
					}
					in.close();
					out.close();
					
					FileWriter fwriter2 = new FileWriter(inputFile);
					BufferedWriter out2 = new BufferedWriter(fwriter2);
					
					FileInputStream fstream2 = new FileInputStream(outputFile);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						out2.write(str2 + "\n");
					}
					in2.close();
					out2.close();
					
					File delete_f = new File(inputFile + ".tmp");
					if (delete_f.exists()) {
						delete_f.delete();
					}
					
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
