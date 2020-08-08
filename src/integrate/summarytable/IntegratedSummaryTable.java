package integrate.summarytable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class IntegratedSummaryTable {

	public static String type() {
		return "INTEGRATION";
	}
	public static String description() { 
		return "Integrated summary table for Peter McKinnon's Project";
	}
	public static String parameter_info() {
		return "[inputFile] [index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int index = new Integer(args[1]);
			String outputFile = args[2];
			HashMap map = new HashMap();
			HashMap genes = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			String[] samples = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("\\(")[0];
				
				for (int i = 1; i < 128; i++) {
					
					if (split[index].equals("TRUE")) {
						
						if (split[0].contains("Fusion")) {
							genes.put(geneName, geneName);
							if (split[i].equals("1")) {
								if (map.containsKey(samples[i])) {
									HashMap gene = (HashMap)map.get(samples[i]);
									if (gene.containsKey(geneName)) {
										String stuff = (String)gene.get(geneName);
										stuff += ",7";
										gene.put(geneName, stuff);
									} else {
										gene.put(geneName, "7");
									}
									map.put(samples[i], gene);
								} else {
									HashMap gene = new HashMap();
									gene.put(geneName, "7");
									map.put(samples[i], gene);
								}
							}
						} // fusion
						
						if (split[0].contains("FPKM")) {
							
							if (!split[i].equals("NA")) {
								
								if (new Double(split[i]) > 100) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",3";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "3");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "3");
										map.put(samples[i], gene);
									}
								} 
							} // is not NA
						} // highly expressed
						
						
						if (split[0].contains("SNV")) {
							if (!split[i].equals("NA")) {
								if (split[i].contains("missense")) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",4";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "4");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "4");
										map.put(samples[i], gene);
									}
								}
								if (split[i].contains("nonsense")) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",6";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "6");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "6");
										map.put(samples[i], gene);
									}
								}
							} // is not NA
						} // SNV
						
						if (split[0].contains("Indel")) {
							if (!split[i].equals("NA")) {
								if (split[i].contains("frameshift")) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",5";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "5");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "5");
										map.put(samples[i], gene);
									}
								}
								if (split[i].contains("proteinIns")) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",2";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "2");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "2");
										map.put(samples[i], gene);
									}
								}
								if (split[i].contains("proteinDel")) {
									genes.put(geneName, geneName);
									if (map.containsKey(samples[i])) {
										HashMap gene = (HashMap)map.get(samples[i]);
										if (gene.containsKey(geneName)) {
											String stuff = (String)gene.get(geneName);
											stuff += ",1";
											gene.put(geneName, stuff);
										} else {
											gene.put(geneName, "1");
										}
										map.put(samples[i], gene);
									} else {
										HashMap gene = new HashMap();
										gene.put(geneName, "1");
										map.put(samples[i], gene);
									}
								}
							} // is not NA
						} // Indel
						
					} // if true 	
				} // for loop
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			for (int i = 1; i < 129; i++) {
				out.write("\t" + samples[i]);
			}
			out.write("\n");
			Iterator itr = genes.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write(geneName);
				for (int i = 1; i < 129; i++) {
					if (map.containsKey(samples[i])) {
						HashMap gene = (HashMap)map.get(samples[i]);
						if (gene.containsKey(geneName)) {
							String content = (String)gene.get(geneName);
							out.write("\t" + content);
						} else {
							out.write("\t0");
						}
					} else {
						out.write("\t0");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
