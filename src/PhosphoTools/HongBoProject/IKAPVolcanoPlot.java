package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class IKAPVolcanoPlot {

	public static void main(String[] args) {
		
		try {
			
			String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\RawIKAP_Anova_Yuxin_v2_20160418.txt";
            FileInputStream fstream = new FileInputStream(fileName);
            DataInputStream din = new DataInputStream(fstream); 
            BufferedReader in = new BufferedReader(new InputStreamReader(din));
            String header = in.readLine();
            while (in.ready()) {
                String str = in.readLine();
                String[] split = str.split("\t");
                double v1 = new Double(split[1]);
                double v2 = new Double(split[2]);
                double v3 = new Double(split[3]);
                double v4 = new Double(split[4]);
                double v5 = new Double(split[5]);
                double v6 = new Double(split[6]);
                double v7 = new Double(split[7]);
                double v8 = new Double(split[8]);
                double v9 = new Double(split[9]);
                double v10 = new Double(split[10]);
                double fdr = new Double(split[12]);
                double ratio2v0 = (v3 + v4) / 2 - (v1 + v2) / 2;
                double abs2v0 = Math.abs(ratio2v0);
                double ratio8v0 = (v5 + v6) / 2 - (v1 + v2) / 2;
                
                double ratio16v0 = (v7 + v8) / 2 - (v1 + v2) / 2;
                double max = Double.MIN_VALUE;
                double min = Double.MAX_VALUE;
                if (ratio16v0 > ratio8v0 && ratio16v0 > ratio2v0) {
                	max = ratio16v0;
                } else if (ratio8v0 > ratio16v0 && ratio8v0 > ratio2v0) {
                	max = ratio8v0;
                } else if (ratio2v0 > ratio16v0 && ratio2v0 > ratio8v0) {
                	max = ratio2v0;
                }
                if (ratio16v0 < ratio8v0 && ratio16v0 < ratio2v0) {
                	min = ratio16v0;
                } else if (ratio8v0 < ratio16v0 && ratio8v0 < ratio2v0) {
                	min = ratio8v0;
                } else if (ratio2v0 < ratio16v0 && ratio2v0 < ratio8v0) {
                	min = ratio2v0;
                }
                if (Math.abs(max) > Math.abs(min)) {
                	System.out.println(split[0] + "\t" + max + "\t" + fdr);
                } else {
                	System.out.println(split[0] + "\t" + min + "\t" + fdr);	
                }
            }
            in.close();
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
