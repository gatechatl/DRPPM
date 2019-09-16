package proteomics.phospho.tools.pssm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * 
 * @author tshaw
 *
 */
public class NormalizePWMWithBackground {

	public static String type() {
		return "PSSM";
	}
	public static String description() {
		return "Normalize the PWM against the background";
	}
	public static String parameter_info() {
		return "[inputFile] [backgroundSTFile] [backgroundYFile] [outputIndexFile] [outputPath]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String backgroundSTFile = args[1];
			String backgroundYFile = args[2];
			String outputIndexFile = args[3];
			String outputPath = args[4];
			HashMap[] background_ST = new HashMap[15];
			HashMap[] background_Y = new HashMap[15];
			
			double pseudo = 0.0000000001;
			for (int i = 0; i < 15; i++) {
				background_ST[i] = new HashMap();
				background_Y[i] = new HashMap();
			}
			
			FileInputStream fstream = new FileInputStream(backgroundYFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int index = new Integer(split[0]) + 7;
				for (int i = 1; i < split.length; i++) {
					background_Y[index].put(header[i], new Double(split[i]));
				}				
			}
			in.close();
			
			fstream = new FileInputStream(backgroundSTFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int index = new Integer(split[0]) + 7;
				for (int i = 1; i < split.length; i++) {
					background_ST[index].put(header[i], new Double(split[i]));
				}				
			}
			in.close();
			
			HashMap map = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {					
					String[] split = str.split("\t");
					int index = 2;
					String kinase = split[0].replaceAll(">", "") + "_" + split[1];
					String newKinaseName = kinase;
					// add an index to the kinase name
					while (map.containsKey(newKinaseName)) {
						newKinaseName = kinase + "_" + index;
						index++;
					}
					if (!kinase.equals(newKinaseName)) {
						kinase = newKinaseName;
					}
					HashMap[] pssm = new HashMap[15];
					for (int i = 0; i < 15; i++) {
						pssm[i] = new HashMap();
					}
					if (split[1].equals("ST")) {
						String firstLine = in.readLine();
						String[] aminoacids = firstLine.split("\t");
						for (int i = 0; i < 15; i++) {							
							String line = in.readLine();
							String[] split2 = line.split("\t");
							for (int j = 1; j < split2.length; j++) {								
								double background = (Double)background_ST[i].get(aminoacids[j]);
								double value = new Double(split2[j]);
								double score = 0;
								if (background == 0) {
									score = 0;
								} else {
									score = value * MathTools.log2(value / background);
								}
								pssm[i].put(aminoacids[j], score);								
							}
						}
					} else if (split[1].equals("Y")) {
						String firstLine = in.readLine();
						String[] aminoacids = firstLine.split("\t");
						for (int i = 0; i < 15; i++) {							
							String line = in.readLine();
							String[] split2 = line.split("\t");
							for (int j = 1; j < split2.length; j++) {								
								double background = (Double)background_Y[i].get(aminoacids[j]);
								double value = new Double(split2[j]);
								//double score = value * MathTools.log2(value / background);
								double score = 0;
								if (background == 0) {
									score = 0;
								} else {
									score = value * MathTools.log2(value / background);
								}
								pssm[i].put(aminoacids[j], score);								
							}
						}
					}
					map.put(kinase, pssm);
				}
			}
	
			FileWriter fwriter = new FileWriter(outputIndexFile);
            BufferedWriter out = new BufferedWriter(fwriter);

			Iterator itr = map.keySet().iterator();
			
			while (itr.hasNext()) {
				String kinaseName = (String)itr.next();
				HashMap[] pssm = (HashMap[])map.get(kinaseName);
				
				out.write(kinaseName + "\t" + outputPath + "/" + kinaseName + ".csv\n");
				FileWriter fwriter2 = new FileWriter(outputPath + "/" + kinaseName + ".csv");
	            BufferedWriter out2 = new BufferedWriter(fwriter2);
	
				String[] amino_acids = {"A", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "V", "W", "Y"};
				out2.write("Location");
				for (String aa: amino_acids) {
					out2.write("\t" + aa);
				}
				out2.write("\n");
				for (int i = 0; i < 15; i++) {
					out2.write((i - 7) + "");
					for (String aa: amino_acids) {															
						double frequency = (Double)pssm[i].get(aa);
						out2.write("\t" + frequency);												
					}					
					out2.write("\n");
				}
				out2.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
