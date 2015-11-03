package MappingTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import MISC.CommandLine;

/**
 * Grabs the bam file and estimate the insert size based on a small proprotion of reads
 * @author tshaw
 *
 */
public class MappingInsertSizeEstimation {

	public static String parameter_info() {
		return "[inputBamLst] [sampleSize] [maxInsertSize]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String sampleSize = args[1];
			String maxInsertSize = args[2];
			System.out.println("SampleName\tPosIns_N\tPosIns_Mean\tPosIns_StDev\tNegIns_N\tNegIns_Mean\tNegIns_StDev\tInsertHigh_Count\tInsertLow_Count\tInsertEqZero_Count\tInsertEqZeroBadMap_Count\tInsertEqZeroNoPair_Count");
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String fileName_wo_path = str.split("/")[str.split("/").length - 1];
				String commandPos = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($9 > 0 && $9 < " + maxInsertSize + ") { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N\",\"M\",\"sqrt ((S2-M*M*N)/(N-1))}' > tmp";				
				CommandLine.executeCommand(commandPos);
				//System.out.println(commandPos);
				String pos_result = readOutput("tmp");
				String commandNeg = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($9 < 0 && $9 > -" + maxInsertSize + ") { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N\",\"M\",\"sqrt ((S2-M*M*N)/(N-1))}' > tmp";				
				CommandLine.executeCommand(commandNeg);
				//System.out.println(commandNeg);
				String neg_result = readOutput("tmp");
				String commandHigh = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($9 > " + maxInsertSize + ") { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N}' > tmp";				
				CommandLine.executeCommand(commandHigh);
				String high_result = readOutput("tmp");
				if (high_result.equals("")) {
					high_result = "0";
				}
				String commandLow = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($9 < -" + maxInsertSize + ") { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N}' > tmp";				
				CommandLine.executeCommand(commandLow);
				String low_result = readOutput("tmp");
				if (low_result.equals("")) {
					low_result = "0";
				}
				//System.out.println(commandHigh);
				String commandZero = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($9 == 0) { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N}' > tmp";				
				CommandLine.executeCommand(commandZero);
				//System.out.println(commandZero);
				String zero_result = readOutput("tmp");
				
				String commandZeroBadMap = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($5 == 0 && $9 == 0) { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N}' > tmp";				
				CommandLine.executeCommand(commandZeroBadMap);
				//System.out.println(commandZero);
				String zerobadmap_result = readOutput("tmp");
				
				String commandZeroNoPair = "samtools view " + str + " | head -n " + sampleSize + " | awk '{ if ($6 == \"*\" && $9 == 0) { N+=1; S+=$9; S2+=$9*$9 }} END { M=S/N; print N}' > tmp";				
				CommandLine.executeCommand(commandZeroBadMap);
				//System.out.println(commandZero);
				String zeronopair_result = readOutput("tmp");
				System.out.println(fileName_wo_path + "\t" + pos_result + "\t" + neg_result + "\t" + high_result + "\t" + low_result + "\t" + zero_result + "\t" + zerobadmap_result + "\t" + zeronopair_result);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String readOutput(String inputFile) {
		String result = "";
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split(",");
				for (String stuff: split) {
					if (result.equals("")) {
						result = stuff;
					} else {
						result += "\t" + stuff;
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
