package stjude.projects.jiyangyu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * For one of the project from Dr. Yu, we need to convert all the geneNames to proper names
 * @author tshaw
 *
 */
public class JiyangYuConvertGeneNames {

	public static String type() {
		return "JIYANGYU";
	}
	public static String description() {
		return "For one of the project from Dr. Yu, we need to convert all the geneNames to proper names";
	}
	public static String parameter_info() {
		return "[inputFile] [humanFasta] [geneConversionTable] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String humanFasta = args[1];
			String geneConversionTable = args[2];
			String outputFile = args[3];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap tag2accession = new HashMap();
			HashMap accession2geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(humanFasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split(" ")[0].split("\\|");
					String tag = split[2];
					String accession = split[1];
					if (!accession.contains("-")) {
						tag2accession.put(tag, accession);
						accession2geneName.put(accession, "NA");
					}
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(geneConversionTable);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (accession2geneName.containsKey(split[0])) {
					String geneName = split[4];
					accession2geneName.put(split[0], split[4]);
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();	
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name_str = split[0];
				String[] split_name = name_str.split(",");
				String newName = "";
				for (int i = 0; i < split_name.length; i++) {
					if (i == 0) {						
						if (split_name[i].contains("_HUMAN")) {
							if (tag2accession.containsKey(split_name[i])) {
								String accession = (String)tag2accession.get(split_name[i]);
								if (accession2geneName.containsKey(accession)) {
									String geneName = (String)accession2geneName.get(accession);
									if (!geneName.equals("NA")) {
										newName = geneName;
									} else {
										newName = split_name[i];
									}
								} else {
									newName = split_name[i];
								}
							} else {
								newName = split_name[i];
							}
						} else {
							newName = split_name[i];
						}
					} else {
						if (split_name[i].contains("_HUMAN")) {
							if (tag2accession.containsKey(split_name[i])) {
								String accession = (String)tag2accession.get(split_name[i]);
								if (accession2geneName.containsKey(accession)) {
									String geneName = (String)accession2geneName.get(accession);
									if (!geneName.equals("NA")) {
										newName += "," + geneName;
									} else {
										newName += "," + split_name[i];
									}
								} else {
									newName += "," + split_name[i];
								}
							} else {
								newName += "," + split_name[i];
							}
						} else {
							newName += "," + split_name[i];
						}
					}
				}
				
				if (!newName.contains("DECOY")) {
					out.write(newName);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
