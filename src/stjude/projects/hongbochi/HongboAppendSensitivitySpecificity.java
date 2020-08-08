package stjude.projects.hongbochi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class HongboAppendSensitivitySpecificity {

	public static String parameter_info() {
		return "[inputComprehensiveFile] [MTORC_Sensitivity_Specificity]";
	}
	public static String type() {
		return "HONGBO";
	}
	public static String description() {
		return "Append sensitivity and specificity to the MTORC1 rapamycin";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String MTORC1_sensitivity_specificity = args[1];
			HashMap map_sensitivity = new HashMap();
			HashMap map_specificity = new HashMap();
			FileInputStream fstream = new FileInputStream(MTORC1_sensitivity_specificity);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double score = new Double(split[0]);
				double sensitivity = new Double(split[5]);
				double specificity = new Double(split[6]);
				score = new Double(Math.round(score * 10)) / 10;
				map_sensitivity.put(score, sensitivity);
				map_specificity.put(score, specificity);
			}
			in.close();						
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));	
			String header = in.readLine();
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String line = str;
				double score = new Double(split[29]);
				score = new Double(Math.round(score * 10)) / 10;
				
				if (map_sensitivity.containsKey(score)) {
					double sensitivity = (Double)map_sensitivity.get(score);
					double specificity = (Double)map_specificity.get(score);
					line += "\t" + sensitivity + "\t" + specificity;
				} else {
					line += "\tNA\tNA";
				}
				System.out.println(line);
			}
			in.close();						
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
