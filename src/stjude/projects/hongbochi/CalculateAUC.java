package stjude.projects.hongbochi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Calculate the AUC after the MTORC1 motif pipeline
 * @author tshaw
 *
 */
public class CalculateAUC {

	public static String parameter_info() {
		return "[inputFile]";
	}
	public static String type() {
		return "HONGBO";
	}
	public static String description() { 
		return "Calculate the AUC after the MTORC1 motif pipeline";
	}
	public static void execute(String[] args) {
		
		try {

			HashMap map = new HashMap();
			double last_fpr = 0;
			double last_tpr = 0;
			double area_under_curve = 0;
			LinkedList values = new LinkedList();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				values.add(str);
			}
			in.close();
			LinkedList values_copy = (LinkedList)values.clone();			
			for (int i = 0; i < values.size(); i++) {
				String str = (String)values_copy.removeLast();
				String[] split = str.split("\t");
				double cutoff = new Double(split[0]);
				double true_positive = new Double(split[1]);
				double true_neg = new Double(split[2]);
				double false_positive = new Double(split[3]);
				double false_neg = new Double(split[4]);
				//double sensitivity = new Double(split[5]);
				//double specificity = new Double(split[6]);
				double pos_count = true_positive + false_neg;
				double neg_count = false_positive + true_neg;
				
				double TPR = true_positive / pos_count;
				double sensitivity = true_positive / pos_count;
				double specificity = true_neg / (true_neg + false_positive);				
				double FPR = (1 - specificity);
				
				double dFPR = FPR - last_fpr;
				double dTPR = TPR - last_tpr;
				//System.out.println(FPR + "\t" + TPR);
				//System.out.println("Diff: " + dFPR + "\t" + dTPR);
				//if (dFPR > 0 && dTPR > 0) {
					System.out.println(TPR + "\t" + dTPR + "\t" + FPR + "\t" + dFPR);
					area_under_curve += TPR * dFPR - (dFPR * dTPR) / 2;
					//values.add(cutoff + "\t" + FPR + "\t" + TPR + "\t" + sensitivity + "\t" + specificity);
					last_fpr = FPR;
					last_tpr = TPR;
				//}
			}
			
			System.out.println(area_under_curve);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
