package stjude.projects.jinghuizhang.alexgout.cloudproject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AlexGoutDoubleCheckSampleNames {

	public static void main(String[] args) {
		
		try {
			
			HashMap type = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\misc\\AlexGoutStJudeCloudAnalysis\\UnsupervisedClustering\\From_Alex_Annotation_20191107.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				type.put(split[0], str);
			}
			in.close();
			
			int count = 0;
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\misc\\AlexGoutStJudeCloudAnalysis\\UnsupervisedClustering\\Tim_NormBatchCorrExprMatrix_FPKM_lognorm_filter_top1000mad.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i <split_header.length; i++) {
				if (type.containsKey(split_header[i])) {
					count++;
				} else {
					System.out.println(split_header[i]);
				}
			}
			in.close();
			System.out.println(count + "\t" + split_header.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
