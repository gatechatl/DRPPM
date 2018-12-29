package proteomics.phospho.kinaseactivity.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterSitePhosphoWithPeptidePhospho {
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String description() {
		return "Filter Sites with peptide psm";
	}
	public static String parameter_info() {
		return "[peptideFile] [peptide_psm_index] [pep_accession_index] [pep_site_index] [siteFile] [site_accession_index] [psm_cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String peptideFile = args[0];			
			int peptide_psm_index = new Integer(args[1]);
			int pep_accession_index = new Integer(args[2]);
			int pep_site_index = new Integer(args[3]);
			String siteFile = args[4];
			int site_accession_index = new Integer(args[5]);
			double psm_cutoff = new Double(args[6]);
			String outputFile = args[7];
			
			HashMap good_sites = new HashMap();
			FileInputStream fstream = new FileInputStream(peptideFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double psm = new Double(split[peptide_psm_index]);
				if (psm >= psm_cutoff) {
					String accession = split[pep_accession_index];
					String[] site_split = split[pep_site_index].split(",");
					for (String site: site_split) {
						good_sites.put(accession + ":" + site, accession + ":" + site);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(siteFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String site_accession = split[site_accession_index];
				if (good_sites.containsKey(site_accession)) {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
