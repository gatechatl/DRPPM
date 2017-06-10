package PhosphoTools.RarefractionCurve;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**
 * Perform rarefaction estimation of protein coverage
 * Estimate the number of PSM required for complete coverage
 * @author tshaw
 *
 */
public class EstimatingTotalCoverage {
	public static String type() {
		return "PHOSPHO";
	}
	public static String description() {
		return "Estimate the total coverage of phosphopeptides";
	}
	public static String parameter_info() {
		return "[id_uni_pep_quan.txt] [subsamples: 0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0] [number of simulations]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFiles = args[0];
			String[] subsample = args[1].split(",");
			int n_simulation = new Integer(args[2]);
			LinkedList list = new LinkedList();
			LinkedList psms = new LinkedList();
			HashMap total_tag = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFiles);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("Peptides") && split.length > 14) {
					String sites = split[14];					
					String tag = split[2] + ":" + sites;
				
				
					int psm = new Integer(split[5]);
					for (int i = 0; i < psm; i++) {
						psms.add(tag);
					}
					for (String site: sites.split(",")) {
						String uniqTag = split[2] + ":" + site;
						total_tag.put(uniqTag, uniqTag);
					}
				}
			}
			in.close();
			System.out.println("Simulation\tN\tPercentageOfPSMs\tPSM\tNumUniqSites\tTotalSites");
			for (int n = 0; n < n_simulation; n++) {				
				for (int i = 0; i < subsample.length; i++) {
					HashMap map = new HashMap(); // keep track of number of uniq tags;
					Random rand = new Random();
					int total = psms.size();
					if (new Double(subsample[i]) <= 1) {
						int subgroup = new Double(new Double(subsample[i]) * new Double(total)).intValue();
						LinkedList tempPSMs = (LinkedList)psms.clone();
						for (int j = 0; j < subgroup; j++) {
							int index = rand.nextInt(tempPSMs.size());
							String raw_tag = (String)tempPSMs.remove(index);
							String accession = raw_tag.split(":")[0];
							String sites = raw_tag.split(":")[1];
							for (String site: sites.split(",")) {
								map.put(accession + ":" + site, "");
							}
						}
						System.out.println("simulation\t" + n + "\t" + subsample[i] + "\t" + subgroup + "\t" + map.size() + "\t" + total_tag.size());
					} else {
						int subgroup = new Double(new Double(subsample[i]) * new Double(total)).intValue();
						System.out.println("simulation\t" + n + "\t" + subsample[i] + "\t" + subgroup + "\tNA\t" + total_tag.size());
					}
					
				}				
			}
			
			/*Iterator itr = list.iterator();
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				
			}
			in.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
