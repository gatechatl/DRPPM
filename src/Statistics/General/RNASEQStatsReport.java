package Statistics.General;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RNASEQStatsReport {

	public static void execute(String[] args) {
		
		try {
			
			File folder = new File(args[0]);
			
			HashMap coding = grabCoverage(args[2]);
			HashMap intron = grabCoverage(args[3]);
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			File[] listOfFiles = folder.listFiles(); 

			STATISTICS[] stats = new STATISTICS[listOfFiles.length];
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					
					String files = listOfFiles[i].getName();
					if (files.contains("flagstat.txt")) {
						stats[i] = getStats(listOfFiles[i].getPath(), files.replace(".flagstat.txt", ""));
						//out.write("\t" + files);
						stats[i] = setCoding(stats[i], coding);
						stats[i] = setIntron(stats[i], intron);
						
						stats[i].createMap();
					}
					
				}
			}
			//out.write("\n");

			String[] vals = {"TOTAL_READS", "MAPPED", "NONDUPS_MAPPED", "PERCENT_MAPPED", "PERCENT_DUPS", "CODING1X", "CODING2X", "CODING5X", "CODING10X" 
					, "CODING20X", "CODING30X", "INTRON1X", "INTRON2X", "INTRON5X", "INTRON10X", "INTRON20X"
					, "INTRON30X"};
			
			out.write("FILES");
			for (String val: vals) {
				out.write("\t" + val);
			}
			out.write("\n");
			for (int i = 0; i < stats.length; i++) {
				out.write(stats[i].NAME);
				for (String val: vals) {
					//out.write(val);

					String str = (String)stats[i].MAP.get(val);
					out.write("\t" + str);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static STATISTICS setCoding(STATISTICS stat, HashMap map) {
		String stuff = stat.NAME.split("-")[0];
		if (map.containsKey(stuff)) {
			String[] split = ((String)map.get(stuff)).split("\t");
			stat.CODING1X = split[0];
			stat.CODING2X = split[1];
			stat.CODING5X = split[2];
			stat.CODING10X = split[3];
			stat.CODING20X = split[4];
			stat.CODING30X = split[5];
		}
		return stat;
	}
	public static STATISTICS setIntron(STATISTICS stat, HashMap map) {
		String stuff = stat.NAME.split("-")[0];
		if (map.containsKey(stuff)) {
			String[] split = ((String)map.get(stuff)).split("\t");
			stat.INTRON1X = split[0];
			stat.INTRON2X = split[1];
			stat.INTRON5X = split[2];
			stat.INTRON10X = split[3];
			stat.INTRON20X = split[4];
			stat.INTRON30X = split[5];
		}
		return stat;
	}
	public static HashMap grabCoverage(String fileName) {
		HashMap map = new HashMap();
		try {
			

			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] names = split[0].split("\\/");
				String name = names[names.length - 1].split("\\.")[0];
				map.put(name, split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6]);
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static STATISTICS getStats(String fileName, String name) {
		STATISTICS stat = new STATISTICS();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				
				stat.NAME = name;
				stat.TOTAL = in.readLine().split(" ")[0];
				stat.QC_FAILURE = in.readLine().split(" ")[0];
				stat.DUPLICATES = in.readLine().split(" ")[0];
				stat.MAPPED = in.readLine().split(" ")[0];
				stat.PAIRED = in.readLine().split(" ")[0];
				stat.READ1 = in.readLine().split(" ")[0];
				stat.READ2 = in.readLine().split(" ")[0];
				stat.MAPPED_PAIRED = in.readLine().split(" ")[0];
				stat.ITSELF_MATE_MAPPED = in.readLine().split(" ")[0];
				stat.SINGLETON = in.readLine().split(" ")[0];
				stat.MATE_MAPPED_TO_DIFF_CHR = in.readLine().split(" ")[0];
				stat.MATE_MAPPED_TO_DIFF_CHR_5MAPQ = in.readLine().split(" ")[0];
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stat;
	}
	static class STATISTICS {

		HashMap MAP = new HashMap();
		String NAME = "";
		String TOTAL = "";
		String PERCENT_MAPPED = "";
		String NONDUPS_MAPPED = "";
		String PERCENT_DUPS = "";
		String QC_FAILURE = "";
		String DUPLICATES = "";
		String MAPPED = "";
		String PAIRED = "";
		String READ1 = "";
		String READ2 = "";
		String MAPPED_PAIRED = "";
		String ITSELF_MATE_MAPPED = "";
		String SINGLETON = "";
		String MATE_MAPPED_TO_DIFF_CHR = "";
		String MATE_MAPPED_TO_DIFF_CHR_5MAPQ = "";
		String CODING1X = "";
		String CODING2X = "";
		String CODING5X = "";
		String CODING10X = "";
		String CODING20X = "";
		String CODING30X = "";
		
		String INTRON1X = "";
		String INTRON2X = "";
		String INTRON5X = "";
		String INTRON10X = "";
		String INTRON20X = "";
		String INTRON30X = "";
		
		public void createMap() {
			MAP.put("TOTAL_READS", TOTAL);
			MAP.put("QC_FAILURE", QC_FAILURE);
			MAP.put("DUPLICATES", DUPLICATES);
			MAP.put("MAPPED", MAPPED);
			MAP.put("PAIRED", PAIRED);
			MAP.put("READ1", READ1);
			MAP.put("READ2", READ2);
			MAP.put("MAPPED_PAIRED", MAPPED_PAIRED);
			MAP.put("ITSELF_MATE_MAPPED", ITSELF_MATE_MAPPED);
			MAP.put("SINGLETON", SINGLETON);
			MAP.put("MATE_MAPPED_TO_DIFF_CHR", MATE_MAPPED_TO_DIFF_CHR);
			MAP.put("MATE_MAPPED_TO_DIFF_CHR_5MAPQ", MATE_MAPPED_TO_DIFF_CHR_5MAPQ);
			MAP.put("CODING1X", CODING1X);
			MAP.put("CODING2X", CODING2X);
			MAP.put("CODING5X", CODING5X);
			MAP.put("CODING10X", CODING10X);
			MAP.put("CODING20X", CODING20X);
			MAP.put("CODING30X", CODING30X);
			MAP.put("INTRON1X", INTRON1X);
			MAP.put("INTRON2X", INTRON2X);
			MAP.put("INTRON5X", INTRON5X);
			MAP.put("INTRON10X", INTRON10X);
			MAP.put("INTRON20X", INTRON20X);
			MAP.put("INTRON30X", INTRON30X);
			
			double percent_mapped = new Double(MAPPED) / new Double(TOTAL);
			double nondups_mapped = new Double(MAPPED) - new Double(DUPLICATES);
			double percent_dups = new Double(DUPLICATES) / new Double(MAPPED);
			PERCENT_MAPPED = new Double(percent_mapped).toString();
			NONDUPS_MAPPED = new Double(nondups_mapped).toString();
			PERCENT_DUPS = new Double(percent_dups).toString();
			MAP.put("PERCENT_MAPPED", PERCENT_MAPPED);
			MAP.put("NONDUPS_MAPPED", NONDUPS_MAPPED);
			MAP.put("PERCENT_DUPS", PERCENT_DUPS);
		}
	}
}

