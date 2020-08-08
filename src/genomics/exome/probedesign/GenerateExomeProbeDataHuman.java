package genomics.exome.probedesign;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author tshaw
 *
 */
public class GenerateExomeProbeDataHuman {

	public static String parameter_info() {
		return "[inputBamListFile] [humanInputPath] [exomeRBSPath]";
	}
	public static void execute(String[] args) {
		try {
			String inputBamListFile = args[0];
			String mouseInputPath = args[1];
			String exomeRBSPath = args[2];
			
			String[] mouse_chrs = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X", "Y"};
			
			FileInputStream fstream = new FileInputStream(inputBamListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					String[] bamFileSplit = str.split("\\/");
					String bamFile = bamFileSplit[bamFileSplit.length - 1].split("-")[0].replaceAll(".bam", "");
					for (String chr: mouse_chrs) {
						String script = generateScript(str, mouseInputPath, bamFile, exomeRBSPath, chr);
						System.out.println(script);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public static String generateScript(String bamFile, String mouse_folder, String fileName, String exomeRBSOutput, String chr) {
		String script = "/nfs_exports/apps/gnu-apps/NextGen/gwbin/coverage/CDS_windows.pl " + mouse_folder + " " + fileName + " " + bamFile + " " + exomeRBSOutput + " " + chr;
		return script;
	}
}
