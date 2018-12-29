package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class McKinnonIntronRetentionQuantification {


	public static String description() {
		return "McKinnon generate a matrix table of normalized intron read count.";
	}
	public static String type() {
		return "MCKINNON";
	}
	public static String parameter_info() {
		return "[coverageFileList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String coverageFileList = args[0];			
			String outputFile = args[1];
			String geneList = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap sample_u12_IRValue = new HashMap();
			//HashMap sampleNames = new HashMap();
			LinkedList sampleNames = new LinkedList();
			HashMap introns = new HashMap();
			FileInputStream fstream = new FileInputStream(coverageFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				sampleNames.add(split[0]);
				int count = new Integer(split[1]);
				FileInputStream fstream2 = new FileInputStream(split[0]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));		
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					String intron_name = split2[3];
					introns.put(intron_name, intron_name);
					double reads = new Double(split2[6]);
					double norm = reads / count * 1000000;
					sample_u12_IRValue.put(split[0] + "\t" + intron_name, norm);
				}
				in2.close();
				
			}
			in.close();

			HashMap gene = new HashMap();
			fstream = new FileInputStream(geneList);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				gene.put(str.toUpperCase(), str.toUpperCase());
			}
			out.write("Intron");
			Iterator itr2 = sampleNames.iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out.write("\t" + sampleName);
			}
			out.write("\tannotation\n");
			
			Iterator itr = introns.keySet().iterator();
			while (itr.hasNext()) {
				String intron = (String)itr.next();
				String[] split_intron = intron.split("_");
				boolean hit = false;
				if (gene.containsKey(split_intron[1].toUpperCase())) {
					hit = true;
				}
				out.write(intron);
				itr2 = sampleNames.iterator();
				while (itr2.hasNext()) {
					String sampleName = (String)itr2.next();
					double norm = (Double)sample_u12_IRValue.get(sampleName + "\t" + intron);
					out.write("\t" + norm);
				}
				out.write("\t" + hit + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
