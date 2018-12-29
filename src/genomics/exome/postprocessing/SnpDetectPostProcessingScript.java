package genomics.exome.postprocessing;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * To facilitate the post processing this automatically 
 * generates the script for post processing of SnpDetect
 * @author tshaw
 *
 */
public class SnpDetectPostProcessingScript {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String lstFile = args[1];
			HashMap map = grabBamFiles(lstFile);
			String snpDetectPath = args[2];
			String bamPath = args[3];
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[0].split("_")[0];
				String typeG = split[0].split("-")[0].split("_")[1];
				String typeT = split[0].split("-")[0].split("_")[2];
				System.out.println(generateScript(sample, typeG, typeT, snpDetectPath, bamPath, map));
			}
			in.close();
			//
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap grabBamFiles(String lstFile) {
		HashMap map = new HashMap();
		try {
						
			FileInputStream fstream = new FileInputStream(lstFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("/");
				String file = split[split.length - 1];
				String sample = file.split("-")[0].replaceAll(".bam", "");
				map.put(sample, file);
				//System.out.println(sample);
				//System.out.println(file);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String generateScript(String sample, String typeG, String typeT, String snpDetectPath, String bamPath, HashMap map) {
		String sampleGT = sample + "_" + typeG + "_" + typeT;
		String control = snpDetectPath + "/" + sample + "_" + typeT + "_" + typeG + ".control.out";
		String low = snpDetectPath + "/" + sample + "_" + typeT + "_" + typeG + ".low.out";
		String high = snpDetectPath + "/" + sample + "_" + typeT + "_" + typeG + ".high_20.out";
		String bamFileG = bamPath + "/" + (String)map.get(sample + "_" + typeG);
		String bamFileT = bamPath + "/" + (String)map.get(sample + "_" + typeT);		
		String input = "/home/tshaw/DNATOOLS/run_all_SNV_postprocess_sample_v2_xma " + sampleGT + " " + control + " " + low + " " + high + " " + bamFileG + " " + bamFileT;
		return input;		
	}
}
