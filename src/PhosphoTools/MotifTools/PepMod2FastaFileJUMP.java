package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.stat.inference.TTest;

import Statistics.General.MathTools;

/**
 * Input is of JUMPq's phosphopep_all_prot_quan.txt file
 * @author tshaw
 *
 */
public class PepMod2FastaFileJUMP {

	public static void main(String[] args) {
		/*String test = "MIANSLNHDS#PPY*HTPT%RR";
		LinkedList list = MotifTools.convert(MotifTools.replaceTag(test.trim()));
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String seq = (String)itr.next();
			System.out.println(seq);
		}*/
		double[] list1 = {1, 2, 3, 2};
		double[] list2 = {4, 5, 6, 7, 8};
		System.out.println(calcTTest(list2, list1));
	}
	public static void execute(String[] args) {
		
		try {
			
			String pepModFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\MOUSE.fasta";
			int name_index = new Integer(args[1]);
			int peptide_index = new Integer(args[2]);
			String group_index = args[3]; // example: 			
			double cutoff = new Double(args[4]);
			//int logChange_index = new Integer(args[5]);
			double logChangeCutOff = new Double(args[5]);
			String outputFileUP = args[6];
			String outputFileDN = args[7];
			String outputFileAll = args[8];
			FileWriter fwriterUP = new FileWriter(outputFileUP);
			BufferedWriter outUP = new BufferedWriter(fwriterUP);
			
			FileWriter fwriterDN = new FileWriter(outputFileDN);
			BufferedWriter outDN = new BufferedWriter(fwriterDN);
			
			FileWriter fwriterAll = new FileWriter(outputFileAll);
			BufferedWriter outAll = new BufferedWriter(fwriterAll);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(pepModFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine(); // skip header
						
			int count = 1;
			String name = "";
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				// pvalue is calculated based on T-Test
				String[] splitGroup = group_index.split(":");
				String[] group1Split = splitGroup[0].split(",");
				String[] group2Split = splitGroup[1].split(",");
				double[] group1Val = new double[group1Split.length];
				
				for (int i = 0; i < group1Split.length; i++) {
					group1Val[i] = new Double(split[new Integer(group1Split[i])]);
				}
				double[] group2Val = new double[group2Split.length];
				for (int i = 0; i < group2Split.length; i++) {
					group2Val[i] = new Double(split[new Integer(group2Split[i])]);
				}
				//double pvalue = new Double(split[pvalue_index]);
				double pvalue = calcTTest(group1Val, group2Val);
				double logFC = calcLogFold(group1Val, group2Val);
				
				if (pvalue <= cutoff) {
					LinkedList list = MotifTools.convert(MotifTools.replaceTag(split[peptide_index].trim()));
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String seq = (String)itr.next();
						
						//double logFC = new Double(split[logChange_index]);
						if (logFC >= logChangeCutOff) {
							outUP.write(">" + split[name_index] + "\n" + seq + "\n");
						} else if (logFC <= -logChangeCutOff) {
							outDN.write(">" + split[name_index] + "\n" + seq + "\n");
						}
						outAll.write(">" + split[name_index] + "\n" + seq + "\n");
						count++;
					}
				} else {
					LinkedList list = MotifTools.convert(MotifTools.replaceTag(split[peptide_index].trim()));
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String seq = (String)itr.next();						
						//double logFC = new Double(split[logChange_index]);
						outAll.write(">" + split[name_index] + "\n" + seq + "\n");						
						count++;
					}
				}
			}
			in.close();
			outUP.close();
			outDN.close();
			outAll.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calcTTest(double[] list1, double[] list2) {
		TTest ttest = new TTest();
		return ttest.tTest(list1, list2);
	}
	public static double calcLogFold(double[] list1, double[] list2) {
		double mean1 = MathTools.mean(list1);
		double mean2 = MathTools.mean(list2);
		//System.out.println(mean1 + "\t" + mean2 + "\t" + MathTools.log2(mean1 / mean2));
		return (MathTools.log2(mean1 / mean2));
	}
}
