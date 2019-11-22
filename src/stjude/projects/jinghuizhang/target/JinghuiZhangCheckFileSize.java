package stjude.projects.jinghuizhang.target;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JinghuiZhangCheckFileSize {



	public static String type() {
		return "JinghuiZhang";
	}
	public static String description() {
		return "Check the file size.";
	}
	public static String parameter_info() {
		return "[inputFileLst] [greater_or_less_than: true/false]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];
			boolean greater_or_less_than = new Boolean(args[1]);
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				if (greater_or_less_than) {
					if (getFileSizeKiloBytes(new File(str)) > 65536) {
						;
						File copied_file = new File("/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/TARGET_RNAseq/two_pass_star_mapping/hg38_mapping/" + (new File(str)).getParentFile().getName() + ".STAR.Aligned.sortedByCoord.out.bam");
						if (!copied_file.exists()) {
							System.out.println("cp -r " + (new File(str)).getParentFile().getName()  + "/" + (new File(str)).getParentFile().getName() + ".STAR.Aligned.sortedByCoord.out.bam /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/TARGET_RNAseq/two_pass_star_mapping/hg38_mapping/");
						}
						//System.out.println(str);
					}
				} else {
					if (getFileSizeKiloBytes(new File(str)) < 65536) {
						//System.out.println(str);
						System.out.println("sh " + (new File(str)).getParentFile().getName() + ".sh");
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static double getFileSizeMegaBytes(File file) {
		return (double) file.length() / (1024 * 1024);
	}
	
	private static double getFileSizeKiloBytes(File file) {
		return (double) file.length() / 1024;
	}
	
	private static double getFileSizeBytes(File file) {
		return (double) file.length();
	}
}
