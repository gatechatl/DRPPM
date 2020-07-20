package rnaseq.splicing.rnapeg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RNApegPSIExonSkipping {


	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		return "PSI Exon Skipping Quantification\n";
	}
	public static String parameter_info() {
		return "[rnapeg_annotation_lst] [exon_test] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String rnapeg_annotation_lst = args[0];
			String exon_test = args[1];
			String outputFile = args[2];
			
			HashMap exon = new HashMap();
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(exon_test);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String up = split[1];
				String dn = split[2];
				String skip = split[3];
				if (!exon.containsKey(split[0])) {
					exon.put(split[0], str);
				}
				map.put(up, split[0] + "\tup");
				map.put(dn, split[0] + "\tdn");
				map.put(skip, split[0] + "\tskip");
				//System.out.println(up + "\t" + dn + "\t" + skip);
			}
			in.close();
			HashMap sampleName = new HashMap();
			HashMap hit = new HashMap();
			fstream = new FileInputStream(rnapeg_annotation_lst);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				FileInputStream fstream2 = new FileInputStream(str.trim());
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					sampleName.put(str.split("-")[0], "");
					if (map.containsKey(split2[0])) {
						System.out.println(str.split("-")[0] + "\t" + split2[0]);
						hit.put(str.split("-")[0] + "\t" + split2[0], new Double(split2[1]));
					}
				}
				in2.close();
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Sample");
			
			Iterator itr2 = exon.keySet().iterator();
			while (itr2.hasNext()) {
				String exon_name = (String)itr2.next();
				String line = (String)exon.get(exon_name);
				String[] split_line = line.split("\t");
				double up_count = 0.0;
				double dn_count = 0.0;
				double skip_count = 0.0;
				out.write("\t" + exon_name + "_PSI\t" + exon_name + "_Up\t" + exon_name + "_Dn\t" + exon_name + "_Skip");
			}
			out.write("\n");
			
			Iterator itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write(sample);
				itr2 = exon.keySet().iterator();
				while (itr2.hasNext()) {
					String exon_name = (String)itr2.next();
					String line = (String)exon.get(exon_name);
					String[] split_line = line.split("\t");
					double up_count = 0.0;
					double dn_count = 0.0;
					double skip_count = 0.0;
					if (hit.containsKey(sample + "\t" + split_line[1])) {
						up_count = (Double)hit.get(sample + "\t" + split_line[1]);
					}
					if (hit.containsKey(sample + "\t" + split_line[2])) {
						dn_count = (Double)hit.get(sample + "\t" + split_line[2]);
					}
					if (hit.containsKey(sample + "\t" + split_line[3])) {
						skip_count = (Double)hit.get(sample + "\t" + split_line[3]);
					}
					double psi = ((up_count + dn_count) / 2) / ((up_count + dn_count) / 2 + skip_count);
					out.write("\t" + psi + "\t" + up_count + "\t" + dn_count + "\t" + skip_count);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
