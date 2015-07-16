package DifferentialExpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.apache.commons.math3.stat.inference.TTest;

import Statistics.General.MathTools;

public class CalculateTTest {

	public static void main(String[] args) {
		try {
			int[] pdgfra = {1, 3, 4};
			int[] ntrk = {5, 6, 7};
			int[] cntrl = {8, 9, 10};
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName_Collapse.txt";
			String outputFile1 = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_TTest_PvC.txt";
			String outputFile2 = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_TTest_NvC.txt";
			String outputFile3 = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_TTest_PvN.txt";
			calculateTTest(inputFile, outputFile1, pdgfra, cntrl);
			calculateTTest(inputFile, outputFile2, ntrk, cntrl);
			calculateTTest(inputFile, outputFile3, pdgfra, ntrk);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static void calculateTTest(String inputFile, String outputFile, int[] index1, int[] index2) {
		TTest test = new TTest();
		try {
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] d1 = new double[index1.length];
				double[] d2 = new double[index2.length];
				int count = 0;
				
				for (int i: index1) {					
					d1[count] = new Double(split[i]);
					count++;
				}
				count = 0;
				for (int i: index2) {
					d2[count] = new Double(split[i]);
					count++;
				}
				double group1 = MathTools.mean(d1);
				double group2 = MathTools.mean(d2);
				out.write(split[0] + "\t" + group1 + "\t" + group2 + "\t" + (group1 - group2) + "\t" + test.tTest(d1, d2) + "\n");
			}
			
			in.close();
			out.close();
			//System.out.println(test.tTest(d1, d2));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
