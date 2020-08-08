package stjude.projects.hongbochi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CalculateROCforMTORC1Motif {

	public static String type() {
		return "HONGBOCHI";
	}
	public static String parameter_info() {
		return "[phosphositeRegulationFile] [moitf_score_file] [outputFile]";
	}
	public static String description() {
		return "Calculate the ROC for MTORC1 Motif";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphositeRegulationFile = args[0];
			String moitf_score_file = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList lines = new LinkedList();
			FileInputStream fstream = new FileInputStream(phosphositeRegulationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			HashMap map = new HashMap();
			//String header = in.readLine();
			//String[] split = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[10].contains("mTOR motif") && split[10].contains("Torin1-sensitive")) {
					String accession = split[1];
					String site = split[3].replaceAll("S", "").replaceAll("T","");
					map.put(accession + "_" + site, accession + "_" + site);
				}			
			}
			in.close();

			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
					
			int count = 0;
			int pos_count = 0;
			fstream = new FileInputStream(moitf_score_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double value = new Double(split[1]);
				if (min > value) {
					min = value;
				}
				if (max < value) {
					max = value;
				}
				if (map.containsKey(split[0])) {
					pos_count++;
					//System.out.println(str + "\ttrue");
					lines.add(str + "\ttrue");
					out.write(str + "\ttrue\n");
				} else {
					//System.out.println(str + "\tfalse");
					lines.add(str + "\tfalse");
					out.write(str + "\tfalse\n");
				}
				count++;
			}
			in.close();
			out.close();
			
			LinkedList values = new LinkedList();
			
			double last_fpr = 0;
			double last_tpr = 0;
			for (double cutoff = min; cutoff <= max; cutoff = cutoff + 0.1) {
				double cutoff_count = 0;
				//double pos_cutoff_count = 0;
				double true_positive = 0;
				double true_neg = 0;
				double false_neg = 0;
				double false_positive = 0;
				//fstream = new FileInputStream(outputFile);
				//din = new DataInputStream(fstream);
				//in = new BufferedReader(new InputStreamReader(din));
				//while (in.ready()) {
					//String str = in.readLine();
				Iterator itr = lines.iterator();
				while (itr.hasNext()) {
					String str = (String)itr.next();
					String[] split = str.split("\t");
					if (new Double(split[1]) >= cutoff) {
						if (split[3].equals("true")) {
							true_positive++;
							//pos_cutoff_count++;
						} else {
							false_positive++;
						}
						cutoff_count++;
					} else {
						if (split[3].equals("false")) {
							true_neg++;
						} else {
							false_neg++;
						}
					}
					
				}
				//in.close();
				double TPR = true_positive / pos_count;

				double sensitivity = true_positive / pos_count;
				double specificity = true_neg / (true_neg + false_positive);
				
				double FPR = (1 - specificity);				
				double dFPR = FPR - last_fpr;
				double dTPR = TPR - last_tpr;
				//if (dFPR > 0 && dTPR > 0) {
					
				//}
				//last_dtpr = dTPR;
				//area_under_curve += sensitivity * ((1 - specificity) - prev);
				
				

				//double specificity = (count - pos_count) / (count - pos_count + false_positive);
				System.out.println(cutoff + "\t" + true_positive + "\t" + true_neg + "\t" + false_positive + "\t" + false_neg + "\t" + sensitivity + "\t" + specificity);
				if (dFPR > 0 && dTPR > 0) {
					values.add(cutoff + "\t" + FPR + "\t" + TPR + "\t" + sensitivity + "\t" + specificity);
					last_fpr = FPR;
					last_tpr = TPR;
				}
			}
			double area_under_curve = 0;
			last_fpr = 0;
			last_tpr = 0;			
			LinkedList values_copy = (LinkedList)values.clone();
			//Iterator itr = values.iterator();			
			for (int i = 0; i < values.size(); i++) {
				//String line = (String)itr.next();
				String line = (String)values_copy.removeLast();
				String[] split = line.split("\t");
				
				double FPR = new Double(split[1]);
				double TPR = new Double(split[2]);
				
				double dFPR = FPR - last_fpr;
				double dTPR = TPR - last_tpr;
				area_under_curve += TPR * dFPR - (dFPR * dTPR) / 2;
				
				last_fpr = FPR;
				last_tpr = TPR;
			}
			//area_under_curve += (1 - last_fpr) * last_tpr - ((1 - last_fpr) * last_tpr) / 2;
			area_under_curve += (1 - last_fpr) * last_tpr;
			
			System.out.println("ROC: " + area_under_curve);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


