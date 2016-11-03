package WholeExonTools.UnpairedPipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateSNVUnpairedScriptSimple {

	public static String type() {
		return "SNV";
	}
	public static String description() {
		return "Generate SNV Unpaired Script";
	}
	public static String parameter_info() {
		return "[inputMatrix] [shellscriptFile] ";
	}
	public static void execute(String[] args) {
		
		String inputBam = args[0];		
		String shellScriptFile = args[1];
		
		try {
			
			FileInputStream fstream = new FileInputStream(inputBam);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String tumor_bam = split[1];
				String germline_bam = split[2];
				String high20 = split[3];
				String low = split[4];
				String control = split[5];
				
				String script = "sh " + shellScriptFile + " " + sampleName + " " + tumor_bam + " " + germline_bam + " " + control + " " + low + " " + high20;
				System.out.println(script);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
