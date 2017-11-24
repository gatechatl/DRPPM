package stjude.projects.hongbochi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Append mTORC1 score to the raptor substrate file
 * @author tshaw
 *
 */
public class AppendMTORC1Motif2Table {

	public static String type() {
		return "HONGBO";
	}
	public static String description() {
		return "Append mTORC1 score to the raptor substrate file";
	}
	public static String parameter_info() {
		return "[mTORC1MotifScoreFile] [raptorSubstrateFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String mTORC1File = args[0];
			String raptorSubstrateFile = args[1];
			String mTORC1Folder = args[2];
			String outputFile = args[3];
			
			FileInputStream fstream = new FileInputStream(mTORC1File);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length == 2) {
					map.put(split[0],  split[1]);
				} else if (split.length <= 1) {
					System.out.println(str);
				}
								
			}
			in.close();			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(raptorSubstrateFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[1].toUpperCase();
				String accession = split[2].split("\\|")[1];				
				String sites = split[5];
				for (String site: sites.split(",")) {
					String id = geneName + "_" + accession + "_" + site;
					if (map.containsKey(id)) {
						String clean_site = site.replaceAll("S",  "").replaceAll("T", "").replaceAll("Y", "");
						out.write(str + "\t" + id + "\t" + map.get(id) + "\n");
					} else {
						String clean_site = site.replaceAll("S",  "").replaceAll("T", "").replaceAll("Y", "");
						String motifScore = "NA";
						File f = new File(mTORC1Folder + "/" + accession + ".txt");
						if (f.exists()) {
							FileInputStream fstream2 = new FileInputStream(mTORC1Folder + "/" + accession + ".txt");
							DataInputStream din2 = new DataInputStream(fstream2);
							BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
							while (in2.ready()) {
								String str2 = in2.readLine();
								String[] split2 = str2.split("\t");
								if (split2[0].equals(accession + "_" + clean_site)) {
									motifScore = split2[2];
									break;
								}
							}
							in2.close();
							out.write(str + "\t" + id + "\t" + motifScore + "\n");
						} else {
							System.out.println("Still Missing: " + id);
							out.write(str + "\t" + id + "\tNA\n");
						}
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
