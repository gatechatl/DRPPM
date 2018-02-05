package microarray.tools.methylation.EPIC850K;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.CommandLine;

public class BMIQNormalizationSingleSample {

	public static String description() {
		return "Calculate BMIQ normalization for each sample";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[inputFile] [probeName_index] [type_index] [IndexAfterValues] [bmiq_path] [sampelNameFile] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			int probeName = new Integer(args[1]);
			int type = new Integer(args[2]);
			int lastIndex = new Integer(args[3]);
			String bmiq_path = args[4];
			String sampleNameFile = args[5];
			//String outputFile = args[6];
			
			//FileWriter fwriter = new FileWriter(outputFile);
			//BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList sampleName = new LinkedList();
			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sampleName.add(str);
			}
						
			//HashMap[] maps = new HashMap[sampleName.size()];
			//for (int i = 0; i < maps.length; i++) {
			//	maps[i] = new HashMap();
			//}
			//System.out.println(maps.length);
			HashMap map_line = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String original_header = in.readLine();
			String[] split_original_header = original_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//map_line.put(split[probeName], str);
			}
			
			//HashMap map_type = new HashMap();
			
			int j = 0;
			Iterator itr = sampleName.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				System.out.println(name);
				LinkedList key_list = new LinkedList();
				String tempOutputFile = "temp" + name;
				FileWriter fwriter2 = new FileWriter(tempOutputFile);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				
				
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din)); 
				String header = in.readLine();
				String[] split_header = header.split("\t");
				
				int sampleIndex = -1;
				for (int i = 0; i < split_header.length; i++) {
					if (split_header[i].equals(name)) {
						sampleIndex = i;
					}
				}
				
				out2.write(split_header[probeName] + "\t" + split_header[sampleIndex] + "\t" + split_header[type] + "\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					//map_type.put(split[probeName], split[type]);
					key_list.add(split[probeName]);
					//maps[j].put(split[probeName], split[sampleIndex]);
					if (!(split[sampleIndex].equals("NA") || split[sampleIndex].equals(" ") || split[sampleIndex].trim().equals(""))) {
						out2.write(split[probeName] + "\t" + split[sampleIndex] + "\t" + split[type] + "\n");

					}
				}
				in.close();
				out2.close();
				
				CommandLine.writeFile("temp.r", rppm_script("temp" + name, bmiq_path, "tempOutput_" + name));
				try {
					Thread.sleep(3000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				CommandLine.executeCommand("R --vanilla < temp.r");
				try {
					Thread.sleep(3000);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				CommandLine.executeCommand("mv Type1fit-1.pdf " + name + "_Type1fit-1.pdf");
				CommandLine.executeCommand("mv Type2fit-1.pdf " + name + "_Type2fit-1.pdf");
				CommandLine.executeCommand("mv CheckBMIQ-1.pdf " + name + "_CheckBMIQ-1.pdf");
				
				HashMap map = new HashMap();
				FileInputStream fstream2 = new FileInputStream("tempOutput_" + name);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine(); // header
				while (in2.ready()) {
					String str = in2.readLine();
					String[] split = str.split("\t");
					map.put(split[1].replaceAll("\"", ""), split[2].replaceAll("\"", ""));
				}
				in2.close();
				
				
				FileWriter fwriter = new FileWriter(name + "_normalized_withNAs.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("TargetID\t" + name + "\n");
				Iterator itr2 = key_list.iterator();
				while (itr2.hasNext()) {
					String tag = (String)itr2.next();
					String value = "NA";
					if (map.containsKey(tag)) {
						value = (String)map.get(tag);
					}
					out.write(tag + "\t" + value + "\n");
										
				}
				out.close();
			}
			/*
			out.write(split_original_header[probeName]);
			for (int i = 0; i < split_original_header.length; i++) {
				if (sampleName.contains(split_original_header[i])) {
					out.write("\t" + split_original_header[i]);
				}
				if (i >= lastIndex) {
					out.write("\t" + split_original_header[i]);
				}
			}
			out.write("\n");
			itr = map_type.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				out.write(tag);
				for (j = 0; j < maps.length; j++) {
					out.write("\t" + maps[j].get(tag));
				}
				String line = (String)map_line.get(tag);
				String[] split_line = line.split("\t");
				for (int i = lastIndex; i < split_line.length; i++) {
					out.write("\t" + split_line[i]);
				}
				out.write("\n");
			}
			//out.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String rppm_script(String inputFile, String bmiq_path, String outputFile) {
		String line = "library(RPMM)\n";
		line += "source(\"" + bmiq_path + "\");\n";
		line += "data = read.csv(\"" + inputFile + "\", header=T, sep=\"\\t\");\n";
		line += "type = data[,3]\n";
		line += "methyldata = data[,2]\n";
		//line += "colnames(methyldata)\n";
		line += "m = BMIQ(methyldata,type,nfit=10000)$nbeta\n";
		line += "colnames(m) = colnames(methyldata)[1:length(colnames(m))]\n";
		line += "final_data = cbind(paste(data[,1]), paste(m), paste(data[,3]))\n";
		line += "write.table(final_data, file=\"" + outputFile + "\", sep = \"\\t\")\n";
		return line;
	}
}
