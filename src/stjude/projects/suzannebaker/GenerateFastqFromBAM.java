package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateFastqFromBAM {

	public static String type() {
		return "BAM";
	}
	public static String description() {
		return "Convert ubam to fastq files. Inputfile should be two column with first column the folder to the ubam file and the second column being the sample prefix name.";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
						
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//File f = new File(str);
				
				System.out.println("mkdir " + split[1]);
				File files = new File(split[0]);
				String combine1 = "cat";
				String combine2 = "cat";
				for (File f: files.listFiles()) {					
					System.out.println("bedtools bamtofastq -i " + f.getPath() + " -fq " + split[1] + "/" + f.getName() + "_R1.fq -fq2 " + split[1] + "/" + f.getName() + "_R2.fq");
					combine1 += " " + split[1] + "/" + f.getName() + "_R1.fq";
					combine2 += " " + split[1] + "/" + f.getName() + "_R2.fq";
				}
				combine1 += " > " + split[1] + "_R1.fq";
				combine2 += " > " + split[1] + "_R2.fq";
				System.out.println(combine1);
				System.out.println(combine2);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
