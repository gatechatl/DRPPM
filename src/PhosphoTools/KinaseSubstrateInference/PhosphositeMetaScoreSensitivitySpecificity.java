package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.RunRScript;
import Statistics.General.MathTools;

public class PhosphositeMetaScoreSensitivitySpecificity {

	public static String description() {
		return "Calculate Phosphosite Score.";
	}
	public static String type() {
		return "Kinase Substrate Prediction based on correlation information";
	}
	public static String parameter_info() {
		return "[phosphositeResult] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String phosphositeResult = args[0];
			String outputFileFolder = args[1];
			String outputROC = args[2];
			HashMap kinase_result = new HashMap();
			HashMap kinase_list = new HashMap();
		
			FileWriter fwriter2 = new FileWriter(outputROC);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("Kinase\tROC\tNumPositiveControl\n");
			FileInputStream fstream = new FileInputStream(phosphositeResult);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String known_kinases = split[split.length - 1];
				
				for (int i = 1; i < split.length - 1; i = i + 2) {
					if (!split[i].equals("NA")) {
						String predicted_kinase = split_header[i].split("_")[0];
						kinase_list.put(predicted_kinase,  predicted_kinase);
						double score = new Double(split[i]);
						boolean isKnownSite = false;
						for (String known_kinase: known_kinases.split(",")) {
							// if the predicted kinase is the same as the known kinase this is a positive
							if (predicted_kinase.equals(known_kinase.toUpperCase())) {
								isKnownSite = true;
							}
						}
						if (isKnownSite) {
							if (kinase_result.containsKey(predicted_kinase + "_POS")) {
								LinkedList scores = (LinkedList)kinase_result.get(predicted_kinase + "_POS");
								scores.add(score);
								kinase_result.put(predicted_kinase + "_POS", scores);
							} else {
								LinkedList scores = new LinkedList();
								scores.add(score);
								kinase_result.put(predicted_kinase + "_POS", scores);
							}
						} else {
							if (kinase_result.containsKey(predicted_kinase + "_NEG")) {
								LinkedList scores = (LinkedList)kinase_result.get(predicted_kinase + "_NEG");
								scores.add(score);
								kinase_result.put(predicted_kinase + "_NEG", scores);
							} else {
								LinkedList scores = new LinkedList();
								scores.add(score);
								kinase_result.put(predicted_kinase + "_NEG", scores);
							}
						}
					}
				}
			}
			in.close();
			Iterator itr = kinase_list.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = (String)itr.next();
				
				String outputFile = outputFileFolder + "/" + kinase + "_sensitivity_specificity.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				

				out.write("Score\tSensitivity\tSpecificity\tTruePos\tFalseNeg\tFalsePos\tTrueNeg\n");
				LinkedList positive_set = new LinkedList();
				LinkedList negative_set = new LinkedList();
				if (kinase_result.containsKey(kinase + "_POS")) {
					positive_set = (LinkedList)kinase_result.get(kinase + "_POS");
				} 
				if (kinase_result.containsKey(kinase + "_NEG")) {
					negative_set = (LinkedList)kinase_result.get(kinase + "_NEG");
				}
				double[] positive_set_array = new double[positive_set.size()];
				int index = 0;
				Iterator itr2 = positive_set.iterator();				
				while (itr2.hasNext()) {
					double score = (Double)itr2.next();
					positive_set_array[index] = score;
					index++;
				}				
				Arrays.sort(positive_set_array);
				
				double area_under_curve = 0;
				double prev = 0;
				double last_sensitivity = 0;
				double last_fpr = 0;
				double last_tpr = 0;
				double last_dtpr = 0;
				out.write("NA" + "\t0.0\t1.0\tNA\tNA\tNA\tNA\n");
				String lines = "";
				for (int i = positive_set_array.length - 1; i >= 0; i--) {
					double score = positive_set_array[i];
					double true_positive = calc_true_positive(score, positive_set);
					double false_negative = positive_set.size() - true_positive;
					double false_positive = calc_false_positive(score, negative_set);
					double true_negative = negative_set.size() - false_positive;
					double sensitivity = true_positive / positive_set.size();
					double specificity = true_negative / negative_set.size();
					
					double FPR = (1 - specificity);
					double TPR = sensitivity;
					double dFPR = FPR - last_fpr;
					double dTPR = TPR - last_tpr;
					//last_dtpr = dTPR;
					//area_under_curve += sensitivity * ((1 - specificity) - prev);
					area_under_curve += TPR * dFPR - (dFPR * dTPR) / 2;
					prev = 1 - specificity;
					//double x_axis = 1.0 - specificity;
					last_sensitivity = sensitivity;
					out.write(score + "\t" + sensitivity + "\t" + specificity + "\t" + true_positive + "\t" + false_negative + "\t" + false_positive + "\t" + true_negative + "\n");
					
					last_fpr = FPR;
					last_tpr = TPR;
				}
				out.write("NA" + "\t1.0\t0.0\tNA\tNA\tNA\tNA\n");
				//if (prev < 1) {					
				area_under_curve += (1 - last_fpr) * last_tpr - ((1 - last_fpr) * last_dtpr) / 2; 
				//}
				out.close();
				

				String outputFileR = outputFileFolder + "/" + kinase + "_sensitivity_specificity.txt.r";
				FileWriter fwriterR = new FileWriter(outputFileR);
				BufferedWriter outR = new BufferedWriter(fwriterR);
				outR.write(plotROC(outputFile, outputFile + ".png", kinase + " ROC(" + new Double(Math.round(area_under_curve * 100)) / 100 + ")"));
				outR.close();
				
				out2.write(kinase + "\t" + area_under_curve + "\t" + positive_set_array.length + "\n");
				RunRScript.runRScript(outputFileR);
			}
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String plotROC(String file, String outputFile, String title) {
		String script = "mydata = read.csv(\"" + file + "\", header = TRUE, sep=\"\\t\")\n";
		script += "png(\"" + outputFile + "\")\n";
		script += "plot((1 - mydata[,\"Specificity\"]) * 100, (mydata[,\"Sensitivity\"]) * 100, xlab=\"False Positive Rate %\", ylab=\"True Positive Rate %\", main=\"" + title + "\", ylim=c(0, 100), xlim=c(0,100));\n";
		script += "lines(x = c(0,100), y = c(0,100), col=\"red\")\n";
		script += "lines((1 - mydata[,\"Specificity\"]) * 100, (mydata[,\"Sensitivity\"]) * 100);\n";;		
		script += "dev.off();\n";
		return script;
	}
	public static int calc_true_positive(double cutoff, LinkedList pos_set) {
		Iterator itr = pos_set.iterator();
		int count = 0;
		while (itr.hasNext()) {
			double val = (Double)itr.next();
			if (val >= cutoff) {
				count++;
			}
		}
		return count; 
	}
	
	public static int calc_false_positive(double cutoff, LinkedList neg_set) {
		Iterator itr = neg_set.iterator();
		int count = 0;
		while (itr.hasNext()) {
			double val = (Double)itr.next();
			if (val >= cutoff) {
				count++;
			}
		}
		return count; 
	}
}
