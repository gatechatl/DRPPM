package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class EXONCAPBasicStatsIndelPairedFile {


	public static String type() {
		return "EXONCAP";
	}
	public static String description() {
		return "Calculate the basic statistics for the INDEL";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleFile] [outputStatTable] [outputMutType]";
	}
	
	public static void execute(String[] args) {
		
		String inputFile = args[0];
		String sampleFile = args[1];
		String outputStatTable = args[2];
		String outputMutType = args[3];
		
		generateBasicTable(inputFile, sampleFile, outputStatTable, outputMutType);
	}
	
	/**
	 * 
	 * @param inputFile
	 * @param sampleFile
	 * @param outputFile
	 * @param outputChromosomeFile
	 */
	public static void generateBasicTable(String inputFile, String sampleFile, String outputFile, String outputChromosomeFile) {
		try {
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_chr = new FileWriter(outputChromosomeFile);
			BufferedWriter out_chr = new BufferedWriter(fwriter_chr);
			
			//String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\MutantAllelePlots\\samples.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\SNV_History\\2015_SNV_filtered.txt";
			HashMap sampleType = new HashMap();
			HashMap chr_list = new HashMap();
			HashMap map = new HashMap();
			HashMap chr_map = new HashMap();
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[1];				
				String sampleName = split[0];
				map.put(sampleName, type);
				
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String quality = split[2];
				String sampleName = split[0];
				String type = split[6];
				String chr = split[4];
				
				if (quality.contains("SJHQ")) {
					chr_list.put(chr, chr);
					if (chr_map.containsKey(sampleName)) {
						HashMap chr_count = (HashMap)chr_map.get(sampleName);
						if (chr_count.containsKey(chr)) {
							int count = (Integer)chr_count.get(chr);
							chr_count.put(chr, count + 1);
						} else {
							chr_count.put(chr, 1);
						}
						chr_map.put(sampleName, chr_count);
					} else {
						HashMap chr_count = new HashMap();
						if (chr_count.containsKey(chr)) {
							int count = (Integer)chr_count.get(chr);
							chr_count.put(chr, count + 1);
						} else {
							chr_count.put(chr, 1);
						}
						chr_map.put(sampleName, chr_count);
					}
					if (sampleType.containsKey(sampleName)) {
						HashMap mutType = (HashMap)sampleType.get(sampleName);
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					} else {
						HashMap mutType = new HashMap();
						if (mutType.containsKey(type)) {
							int count = (Integer)mutType.get(type);
							mutType.put(type, count + 1);
						} else {
							mutType.put(type, 1);
						}
						sampleType.put(sampleName, mutType);
					}
				}
			}
			in.close();
			
			out_chr.write("sampleName");
			Iterator itr = chr_list.keySet().iterator();
			while (itr.hasNext()) {
				String chr = (String)itr.next();
				out_chr.write("\tchr" + chr);
			}
			out_chr.write("\n");
						
			itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				out_chr.write(sampleName);
				HashMap chr_count = (HashMap)chr_map.get(sampleName);
				
				Iterator itr3 = chr_list.keySet().iterator();
				while (itr3.hasNext()) {
					String chr = (String)itr3.next();
					if (chr_count.containsKey(chr)) {
						int count = (Integer)chr_count.get(chr);
						out_chr.write("\t" + count);
					} else {
						out_chr.write("\t" + 0);
					}
				}
				out_chr.write("\n");
			}
			out_chr.close();
			//System.out.println("sampleName\tproteinDel\tsplice_region\tnonsense\tproteinIns\tsplice\tsplice_region\tframeshift\tUTR_5");
			out.write("sampleName\tproteinDel\tproteinIns\tframeshift\tsplice_region\tsplice\tTotal\n");
			itr = sampleType.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String sampeType = (String)map.get(sampleName);
				HashMap mutType = (HashMap)sampleType.get(sampleName);
				int proteinDel = 0;
				int splice_region = 0;
				int proteinIns = 0;
				int splice = 0;
				int frameshift = 0;
				int UTR_5 = 0;
				int nonsense = 0;
				int total = 0;
				
				if (mutType.containsKey("proteinDel")) {
					proteinDel = (Integer)mutType.get("proteinDel");
				}
				if (mutType.containsKey("splice_region")) {
					splice_region = (Integer)mutType.get("splice_region");
				}
				if (mutType.containsKey("proteinIns")) {
					proteinIns = (Integer)mutType.get("proteinIns");
				}
				if (mutType.containsKey("splice")) {
					splice = (Integer)mutType.get("splice");
				}
				if (mutType.containsKey("splice_region")) {
					splice_region = (Integer)mutType.get("splice_region");
				}
				if (mutType.containsKey("frameshift")) {
					frameshift = (Integer)mutType.get("frameshift");
				}
				
				total = proteinDel + splice_region + proteinIns + splice + splice_region + frameshift + UTR_5 + nonsense;
				if (sampleType != null) {
					out.write(sampeType + "_" + sampleName + "\t" + proteinDel + "\t" + proteinIns + "\t" + frameshift + "\t" + splice_region + "\t" + splice + "\t" + total + "\n");
				} else {
					out.write(sampleName + "\t" + proteinDel + "\t" + proteinIns + "\t" + frameshift + "\t" + splice_region + "\t" + splice + "\t" + total + "\n");
				}
				//System.out.println(sampeType + "_" + sampleName + "\t" + proteinDel + "\t" + splice_region + "\t" + nonsense + "\t" + proteinIns + "\t" + splice + "\t" + splice_region + "\t" + frameshift + "\t" + UTR_5);
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
}
