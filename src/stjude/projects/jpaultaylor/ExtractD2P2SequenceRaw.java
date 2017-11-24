package stjude.projects.jpaultaylor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ExtractD2P2SequenceRaw {

	public static String type() {
		return "D2P2";
	}
	public static String description() {
		return "Extract D2P2 disorder region fasta file";
	}
	public static String parameter_info() {
		return "[path] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String path = args[0];
			String outputFile = args[1];
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
			File f = new File(path);
			for (File files: f.listFiles()) {
				String file = files.getPath();
				int start = 0;
				int end = 0;
				String seq = "";
				String accession = files.getName().split("_")[0];
				FileInputStream fstream = new FileInputStream(file);
	            DataInputStream din = new DataInputStream(fstream);
	            BufferedReader in = new BufferedReader(new InputStreamReader(din));
	            in.readLine();
	            while (in.ready()) {
	            	String str = in.readLine();
	            	String[] split = str.split("\t");
	            		            	
	            	if (new Double(split[1]) > 0.6) {
	            		if (seq.equals("")) {
	            			start = new Integer(split[0]);
	            		}
	            		seq += split[3];
	            		
	            	} else {
	            			            		
	            		end = new Integer(split[0]);
	            		if (seq.length() > 10) {
	            			out.write(">" + accession + " (" + start + "-" + end + ")\n" + seq + "\n");
	            		}
	            		seq = "";
	            	}
	            	
	            }
	            in.close();
	            if (seq.length() > 10) {
        			out.write(">" + accession + " (" + start + "-" + end + ")\n" + seq + "\n");
        		}
			}
			
			out.close();            
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
