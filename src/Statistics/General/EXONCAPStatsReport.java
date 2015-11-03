package Statistics.General;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class EXONCAPStatsReport {

	public static String parameter_info() {
		return "[folder] [outputFile] [codingexon] [coding]";
	}
	public static void execute(String[] args) {
		
		try {
			
			File folder = new File(args[0]);
			String outputFile = args[1];
			HashMap codingexon = grabCoverage(args[2]);
			HashMap coding = grabCoverage(args[3]);
			
		
			
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
						stats[i] = setCodingExon(stats[i], codingexon);
						stats[i] = setCoding(stats[i], coding);
						
						stats[i].createMap();
					}
					
				}
			}
			//out.write("\n");

			String[] vals = {"TOTAL_READS", "MAPPED", "NONDUPS_MAPPED", "PERCENT_MAPPED", "PERCENT_DUPS", "CODING1X", "CODINGEXON2X", "CODINGEXON5X", "CODINGEXON10X", "CODINGEXON20X", "CODINGEXON30X" 
					, "CODING1X", "CODING2X", "CODING5X", "CODING10X", "CODING20X", "CODING30X"};
			
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
	public static STATISTICS setCodingExon(STATISTICS stat, HashMap map) {
		String stuff = stat.NAME.split("-")[0];
		if (map.containsKey(stuff)) {
			String[] split = ((String)map.get(stuff)).split("\t");
			stat.CODINGEXON1X = split[0];
			stat.CODINGEXON2X = split[1];
			stat.CODINGEXON5X = split[2];
			stat.CODINGEXON10X = split[3];
			stat.CODINGEXON20X = split[4];
			stat.CODINGEXON30X = split[5];
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
		String lastLine = "";
		try {
			
			int count = 0;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				count++;
			}
			in.close();
			
			if (count == 12) {
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
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
				in.close();
			} else if (count == 11) {
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					
					stat.NAME = name;
					
					//stat.QC_FAILURE = in.readLine().split(" ")[0];
					stat.TOTAL = in.readLine().split(" ")[0];
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
				in.close();
			} else {
				System.out.println("QC File is not in the expected format");
			}
		} catch (Exception e) {
			System.out.println("Failed to read: " + fileName);
			System.out.println(lastLine);
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
		
		String CODINGEXON1X = "";
		String CODINGEXON2X = "";
		String CODINGEXON5X = "";
		String CODINGEXON10X = "";
		String CODINGEXON20X = "";
		String CODINGEXON30X = "";
		
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
			MAP.put("CODINGEXON1X", CODINGEXON1X);
			MAP.put("CODINGEXON2X", CODINGEXON2X);
			MAP.put("CODINGEXON5X", CODINGEXON5X);
			MAP.put("CODINGEXON10X", CODINGEXON10X);
			MAP.put("CODINGEXON20X", CODINGEXON20X);
			MAP.put("CODINGEXON30X", CODINGEXON30X);
			
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

