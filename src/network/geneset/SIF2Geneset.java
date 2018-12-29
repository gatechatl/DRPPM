package network.geneset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SIF2Geneset {
	public static String description() {
		return "SIF conversion to other geneset format.";
	}
	public static String type() {
		return "NETWORK";
	}

	public static String parameter_info() {
		return "[sif_file] [geneName] [outputSIF] [outputGeneTxtFile] [outputGmtFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String sif_file = args[0];
			String geneName = args[1].toUpperCase();
			String outputSIF = args[2];
			String outputGeneTxtFile = args[3];
			String outputGmtFile = args[4];
			

			FileWriter fwriter_sif = new FileWriter(outputSIF);
			BufferedWriter out_sif = new BufferedWriter(fwriter_sif);
			
			FileWriter fwriter_txt = new FileWriter(outputGeneTxtFile);
			BufferedWriter out_txt = new BufferedWriter(fwriter_txt);
			
			FileWriter fwriter_gmt = new FileWriter(outputGmtFile);
			BufferedWriter out_gmt = new BufferedWriter(fwriter_gmt);
			
			out_sif.write("A\tconnection\tB\n");
			HashMap gene = new HashMap();
			FileInputStream fstream = new FileInputStream(sif_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].toUpperCase().equals(geneName)) {
					gene.put(split[0], split[0]);
					gene.put(split[2], split[2]);
					out_sif.write(str + "\n");
				} else if (split[0].toUpperCase().equals(geneName)) {
					gene.put(split[0], split[0]);
					gene.put(split[2], split[2]);
					out_sif.write(str + "\n");
				}
			}
			in.close();
			out_sif.close();
			out_gmt.write(">" + geneName + "\t>" + geneName);
			out_txt.write(">" + geneName + "\n");
			Iterator itr = gene.keySet().iterator();
			while (itr.hasNext()) {
				String str = (String)itr.next();
				out_gmt.write("\t" + str);
				out_txt.write(str + "\n");
			}
			out_gmt.close();
			out_txt.close();
			
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
