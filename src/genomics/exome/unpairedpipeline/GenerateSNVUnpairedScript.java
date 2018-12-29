package genomics.exome.unpairedpipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import misc.CommandLine;

public class GenerateSNVUnpairedScript {

	public static void main(String[] args) {
		String bamFile = "/nfs_exports/genomes/1/projects/EXCAP/LeventakiSpliceCell/BucketRaw/SJALCL/SJALCL011825_C5-SupM2-cells.bam";
		
		String bamSampleFile = grabSampleName(bamFile);
		//String refSampleFile = grabSampleName(refBamFile);
		String samplePath = grabPath(bamFile);
		System.out.println(bamSampleFile);
		System.out.println(samplePath);
	}
	public static String parameter_info() {
		return "[inputBamList] [refBam] [shellscriptFile] [SnpDetectFolder]";
	}
	public static void execute(String[] args) {

		String inputBam = args[0];
		String refBam = args[1];		
		String shellScriptFile = args[2];
		String SnpDetectFolder = args[3];
		
		try {
			
			FileInputStream fstream = new FileInputStream(inputBam);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					System.out.println(generateScript(shellScriptFile, str, refBam, SnpDetectFolder));
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static String grabSampleName(String str) {
		String[] split = str.split("\\/");
		String sample = split[split.length - 1].split("-")[0].split("\\.")[0];		
		return sample;
	}
	public static String grabPath(String str) {
		String[] split = str.split("\\/");
		String path = "";
		for (int i = 0; i < split.length - 1; i++) {
			if (i == 0) {
				path = split[i];
			} else {
				path += "/" + split[i];
				
			}
		}
		return path;
	}
	public static String generateScript(String shellScriptPath, String bamFile, String refBamFile, String outputPath) {
		String bamSampleFile = grabSampleName(bamFile);
		String refSampleFile = grabSampleName(refBamFile);
		String samplePath = grabPath(bamFile);
		
		String script = "sh " + shellScriptPath + " " + bamSampleFile.replaceAll("_",  "") + " " + bamFile + " " + refBamFile + " " + bamSampleFile + "_" + refSampleFile + ".control.out " + bamSampleFile + "_" + refSampleFile + ".low.out " + bamSampleFile + "_" + refSampleFile + ".high_20.out ";
		
		String softLink = "ln -s " + outputPath + "/" + bamSampleFile + "_" + refSampleFile + ".low.out " + bamSampleFile + "_" + refSampleFile + ".low.out";
		CommandLine.executeCommand(softLink);
		softLink = "ln -s " + outputPath + "/" + bamSampleFile + "_" + refSampleFile + ".control.out " + bamSampleFile + "_" + refSampleFile + ".control.out";
		CommandLine.executeCommand(softLink);
		
		softLink = "ln -s " + outputPath + "/" + bamSampleFile + "_" + refSampleFile + ".high_20.out " + bamSampleFile + "_" + refSampleFile + ".high_20.out";
		CommandLine.executeCommand(softLink);
		
		return script;
	}
}
