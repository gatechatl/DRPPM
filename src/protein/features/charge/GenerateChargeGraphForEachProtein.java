package protein.features.charge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import protein.features.embosstools.ReadPepInfo.PEPSTATS;
import misc.CommandLine;


/**
 * Generate an average charge visualizer
 * @author tshaw
 *
 */
public class GenerateChargeGraphForEachProtein {

	public static String parameter_info() {
		return "[accessionFile] [tagFlag] [human_fasta] [window] [outputFolder]";
	}
	public static void execute(String[] args) {		
		try {
			
			// step1 input a fasta sequence ; will change to sliding window later
			
			String accession_file = args[0];
			String tag_flag = args[1];
			String human_fasta = args[2];
			int window = new Integer(args[3]);			
			String outputFolder = args[4];
			HashMap map = loadFastaFile(human_fasta);
			HashMap accession2name = loadFastaName(human_fasta);
			File f = new File(outputFolder);
			if (!f.exists()) {
				f.mkdir();
			}
						
			LinkedList list = grabList(accession_file, tag_flag);
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String accession = (String)itr.next();
				
				String name = (String)accession2name.get(accession);
				name = name.replaceAll("\\|", "_") + "_" + accession;
				
				String outputFile = outputFolder + "/" + name + ".txt";
				String outputImg = outputFolder + "/" + name + ".png";
				FileWriter fwriter = new FileWriter(outputFile);
	            BufferedWriter out = new BufferedWriter(fwriter);
	            out.write("AAPosition\tAminoAcid\tChargeScore\tType\n");
				if (map.containsKey(accession)) {
					String inputSeq = (String)map.get(accession);
					System.out.println(inputSeq);
					for (int i = 0; i < inputSeq.length() - window; i++) {
						String seq = inputSeq.substring(i, i + window);
						System.out.println((i + 1) + "\t" + inputSeq.substring(i, i + 1) + "\t" + calculate_charge(seq));
						String type = "Neg";					
						double charge = calculate_charge(seq) ;
						if (charge >= 0) {
							type = "Pos";
						}
						out.write((i + 1) + "\t" + inputSeq.substring(i, i + 1) + "\t" + charge + "\t" + type + "\n");
					}				
					for (int i = inputSeq.length() - window; i < inputSeq.length(); i++) {
						String seq = inputSeq.substring(i, inputSeq.length());
						System.out.println((i + 1) + "\t" + inputSeq.substring(i, i + 1) + "\t" + calculate_charge(seq));
						String type = "Neg";					
						double charge = calculate_charge(seq) ;
						if (charge >= 0) {
							type = "Pos";
						}
						out.write((i + 1) + "\t" + inputSeq.substring(i, i + 1) + "\t" + charge + "\t" + type + "\n");
					}
				}
				out.close();
				String scatter_plot_script = generateScatterPlotScript(outputFile, outputImg);
				CommandLine.writeFile("script.r", scatter_plot_script);
				CommandLine.executeCommand("R --vanilla < script.r");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static LinkedList grabList(String inputFile, String tag) {
		LinkedList list = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (split[0].equals(tag)) {
					list.add(split[2]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static String generateScatterPlotScript(String inputFile, String outputFile) {
		String script = "library(ggplot2);\n";
		script += "data = read.csv(\"" + inputFile + "\", sep=\"\t\", header=T);\n";
		script += "png(file=\"" + outputFile + "\", width = 600, height = 400);\n";
		//script += "d = ggplot() + geom_point(data=data, aes(x=AAPosition,y=ModScore,colour=Type)) + geom_point(data=data, aes(x=AAPosition,y=AcceptFlag,colour=Type))\n";
		script += "d = ggplot() + geom_point(data=data, aes(x=AAPosition,y=ChargeScore,colour=Type)) + scale_y_continuous(limits = c(-14.0, 14))\n";
		script += "d\n";
		script += "dev.off()\n";
		return script;
	}
	public static HashMap loadFastaName(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.split("\\|")[1];
					String geneName = "";
					String[] split = str.split(" ");
					for (String tag: split) {
						if (tag.contains("GN=")) {
							geneName = tag.replaceAll("GN=", "");
						}
					}
					map.put(name, geneName);
				} else {					
					
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap loadFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.split("\\|")[1];					
				} else {					
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static double calculate_charge(String seq) {
		return CalculateCharge.comprehensive_charge(seq);
		/*
		try {
			
			String buffer = UUID.randomUUID().toString();
			String inputFile = "input_" + buffer;
			String outputFile = "output_" + buffer;
			CommandLine.writeFile(inputFile, ">input\n" + seq);
			generatePepStatFile(inputFile, outputFile);
			
			HashMap map = new HashMap();
			String name = "";
			FileInputStream fstream = new FileInputStream(outputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				str = str.replaceAll("\t", " ");
				while (str.contains("  ")) {
					str = str.replaceAll("\t", " ");
					str = str.replaceAll("\r", "");
					str = str.replaceAll("\n", "");
					str = str.replaceAll("  ", " ");					
				}
				String[] split = str.split(" ");
				if (str.contains("PEPSTATS")) {
					name = split[2];
					PEPSTATS pepstats = new PEPSTATS();
					pepstats.NAME = name;
					map.put(name, pepstats);
				}
				if (str.contains("Charge ")) {
					PEPSTATS pepstats = (PEPSTATS)map.get(name);
					pepstats.CHARGE = new Double(split[7]);
					in.close();
					map.put(name, pepstats);
					File f = new File(inputFile);
					f.delete();
					f = new File(outputFile);
					f.delete();
					
					return pepstats.CHARGE;
					
				}
			}
			in.close();
			File f = new File(inputFile);
			f.delete();
			f = new File(outputFile);
			f.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Double.NaN;
		*/
	}
	public static void generatePepStatFile(String inputFile, String outputFile) {
		String command = "pepstats -sequence " + inputFile + " -outfile " + outputFile;
		CommandLine.executeCommand(command);
	}
	public static class PEPSTATS {
		String NAME = "";
		double MOL_WEIGHT = Double.NaN;
		double RESIDUES = Double.NaN;
		double AVG_MOL_WEIGHT = Double.NaN;
		double CHARGE = Double.NaN;
		double ISOELECTRIC_POINT = Double.NaN;
		double EXTINCTION_COEF = Double.NaN;
		double PROB_EXPR_INCLUSION = Double.NaN;
		double TINY = Double.NaN;
		double SMALL = Double.NaN;
		double ALIPHATIC = Double.NaN;
		double AROMATIC = Double.NaN;
		double NONPOLAR = Double.NaN;
		double POLAR = Double.NaN;
		double CHARGED = Double.NaN;
		double BASIC = Double.NaN;
		double ACIDIC = Double.NaN;
		
	}
}
