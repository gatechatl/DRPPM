package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * After running MIXICR summarize VDJ, summarize the top hit.  
 * @author tshaw
 *
 */
public class SummarizeVDJclones {

	public static String type() {
		return "LEVENTAKI";
	}
	public static String description() {
		return "After running MIXICR summarize VDJ, summarize the top hit. ";
	}
	public static String parameter_info() {
		return "[fileList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileList = args[0];
			
			System.out.println("Name\tcloneNum\tpercentage\tseq\tVDJ");
			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				if (f.exists()) {
					
					FileInputStream fstream2 = new FileInputStream(str);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					in2.readLine();
					int cloneCount = 0;
					double cloneFraction = 0.0;
					String sequence = "";
					String vdjType = "";
					boolean first = true;
					boolean check = false;
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split = str2.split("\t");
						if (first) {
							cloneCount = new Integer(split[1]);
							cloneFraction = new Double(split[2]);
							if (cloneCount >= 10 && cloneFraction > 0.5) {
								check = true;
							}
							sequence = split[3];
							vdjType = split[5] + ";" + split[6] + ";" + split[7] + ";" + split[8];
						}
						first = false;
					}
					System.out.println(f.getName() + "\t" + check + "\t" + cloneCount + "\t" + cloneFraction + "\t" + sequence + "\t" + vdjType);
					in2.close();
				}
			}
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
