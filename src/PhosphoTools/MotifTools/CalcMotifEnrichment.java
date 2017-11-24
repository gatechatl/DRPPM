package PhosphoTools.MotifTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Calculates the motif enrichment for phosphorylation
 * Primarily based on HPRD manually annotated resource
 * @author tshaw
 *
 */
public class CalcMotifEnrichment {

	public static void execute(String[] args) {
		
		try {
			
			//String motifFile = args[0];			
			String phosphoFastaFile = args[0];
			String phosphoHaveMotifFile = args[1];
			String diffFastaFile = args[2];
			String diffRegulated_motif = args[3];
			String all_motif_file = args[4];
			String option = args[5].toUpperCase(); // Motif, Name, Group, Family
			String outputFile = args[6];
			
			HashMap motif2KinaseGroup = MotifTools.grabMotifKinaseGroup(all_motif_file);
			HashMap motif2KinaseFamily = MotifTools.grabMotifKinaseFamily(all_motif_file);
			HashMap motif2MotifName = MotifTools.grabMotifName(all_motif_file);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							
			
			// print out the header
			/**if (site_diff != null) {
					a = site_diff.size();
				}
				int b = 0;
				if (kinase_diff_nomotiff_site_map != null) {
					b = kinase_diff_nomotiff_site_map.size();
				}
				
				int c = 0;
				if (site_motif != null) {
					c = site_motif.size();
				}
				int d = 0;
				if (kinase_nodiff_nomotif_site_map != null) {
					d = kinase_nodiff_nomotif_site_map.size();
				}*/
			out.write(option + "_Name" + "\t" + "Fisher_pvalue" + "\t" + "Corrected_Pvalue" + "\t" + "# Diff_MotifFound" + "\t" + "# Diff_NotFound" + "\t" + "# NoDiff_Found" + "\t" + "# NoDiff_NotFound" + "\n");
			HashMap all_motif = new HashMap();
			//HashMap all_motif = MotifTools.grabMotifAndName(motifFile);
			/*
			HashMap kinase_diff_nomotiff_map = MotifTools.grabMotifAndName(motifFile);
			HashMap kinase_nodiff_motif_map = MotifTools.grabMotifAndName(motifFile);
			HashMap kinase_nodiff_nomotif_map = MotifTools.grabMotifAndName(motifFile);
			*/			
			
			HashMap kinase_diff_motif_map = new HashMap();
			
			HashMap kinase_nodiff_motif_map = new HashMap();
			
			HashMap site_diff = new HashMap();
			HashMap site_motif = new HashMap();
			
			
			FileInputStream fstream = new FileInputStream(diffRegulated_motif);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();				
				String[] split = str.split("\t");
				String tag = split[0];
				String orig_key = split[4] + "\t" + split[5];
				
				String motif = "NA";
				if (option.equals("GROUP")) {
					if (motif2KinaseGroup.containsKey(orig_key)) {
						motif = (String)motif2KinaseGroup.get(orig_key);
					}
				} else if (option.equals("FAMILY")) {
					if (motif2KinaseFamily.containsKey(orig_key)) {
						motif = (String)motif2KinaseFamily.get(orig_key);
					}
				} else if (option.equals("NAME")) {
					if (motif2MotifName.containsKey(orig_key)) {
						motif = (String)motif2MotifName.get(orig_key);
					}
				} else {
					motif = orig_key;
				}
				all_motif.put(motif, motif);
				String loc = split[1].replaceAll("\\*,", "");
				//site_diff.put(tag + "\t" + loc, "");
				//System.out.println(motif);
				if (kinase_diff_motif_map.containsKey(motif)) {
					HashMap site = (HashMap)kinase_diff_motif_map.get(motif);
					site.put(tag + "\t" + loc, "");
					kinase_diff_motif_map.put(motif, site);
				} else {
					HashMap site = new HashMap();
					site.put(tag + "\t" + loc, "");
					kinase_diff_motif_map.put(motif, site);
				}
				//total_phospho_site.put(tag + "_" + loc, tag + "_" + loc);								
			}
			in.close();		
			
			fstream = new FileInputStream(phosphoHaveMotifFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();				
				String[] split = str.split("\t");
				String tag = split[0];
				String orig_key = split[4] + "\t" + split[5];
				
				String motif = "NA";
				if (option.equals("GROUP")) {
					if (motif2KinaseGroup.containsKey(orig_key)) {
						motif = (String)motif2KinaseGroup.get(orig_key);
					}
				} else if (option.equals("FAMILY")) {
					if (motif2KinaseFamily.containsKey(orig_key)) {
						motif = (String)motif2KinaseFamily.get(orig_key);
					}
				} else if (option.equals("NAME")) {
					if (motif2MotifName.containsKey(orig_key)) {
						motif = (String)motif2MotifName.get(orig_key);
					}
				} else {
					motif = orig_key;
				}
				all_motif.put(motif, motif);
				String loc = split[1].replaceAll("\\*,", "");
				//site_motif.put(tag + "\t" + loc, "");
				if (kinase_diff_motif_map.containsKey(motif)) {
					HashMap site = (HashMap)kinase_diff_motif_map.get(motif);
					//System.out.println("phosphoHaveMotifFile: " + tag + "\t" + loc);
					if (!site.containsKey(tag + "\t" + loc)) {
						if (kinase_nodiff_motif_map.containsKey(motif)) {
							HashMap new_site = (HashMap)kinase_nodiff_motif_map.get(motif);
							new_site.put(tag + "\t" + loc, "");
							kinase_nodiff_motif_map.put(motif, new_site);
						} else {
							HashMap new_site = new HashMap();
							new_site.put(tag + "_" + loc, "");
							kinase_nodiff_motif_map.put(motif, new_site);
						}
					}
				} else {
					if (kinase_nodiff_motif_map.containsKey(motif)) {
						HashMap new_site = (HashMap)kinase_nodiff_motif_map.get(motif);
						new_site.put(tag + "\t" + loc, "");
						kinase_nodiff_motif_map.put(motif, new_site);
					} else {
						HashMap new_site = new HashMap();
						new_site.put(tag + "\t" + loc, "");
						kinase_nodiff_motif_map.put(motif, new_site);
					}
				}
			}
			in.close();						
			
			Iterator itr = all_motif.keySet().iterator();
			
			while (itr.hasNext()) {
				String orig_key2 = (String)itr.next();
				/*String motif = "NA";
				if (option.equals("GROUP")) {
					if (motif2KinaseGroup.containsKey(orig_key2)) {
						motif = (String)motif2KinaseGroup.get(orig_key2);
					}
				} else if (option.equals("FAMILY")) {
					if (motif2KinaseFamily.containsKey(orig_key2)) {
						motif = (String)motif2KinaseFamily.get(orig_key2);
					}
				} else if (option.equals("NAME")) {
					if (motif2MotifName.containsKey(orig_key2)) {
						motif = (String)motif2MotifName.get(orig_key2);
					}
				} else {
					motif = orig_key2;
				}*/
				String motif = orig_key2;
				site_diff = (HashMap)kinase_diff_motif_map.get(motif);
				site_motif = (HashMap)kinase_nodiff_motif_map.get(motif);
				HashMap kinase_nodiff_nomotif_site_map = new HashMap();
				HashMap kinase_diff_nomotiff_site_map = new HashMap();
				
				fstream = new FileInputStream(diffFastaFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.contains(">")) {
						String[] split = str.split("\t");
						String tag = split[0].replaceAll(">", "");
						String[] locations = split[1].split("#,");
						for (String loc: locations) {
							if (!loc.equals("")) {
								String orig_key = tag + "\t" + loc.replaceAll("\\*,", "");
								String key = "NA";
								if (option.equals("GROUP")) {
									if (motif2KinaseGroup.containsKey(orig_key)) {
										key = (String)motif2KinaseGroup.get(orig_key);
									}
								} else if (option.equals("FAMILY")) {
									if (motif2KinaseFamily.containsKey(orig_key)) {
										key = (String)motif2KinaseFamily.get(orig_key);
									}
								} else if (option.equals("NAME")) {
									if (motif2MotifName.containsKey(orig_key)) {
										key = (String)motif2MotifName.get(orig_key);
									}
								} else {
									key = orig_key;
								}
								
								
								//System.out.println("diffFasta: " + key);
								if (site_diff != null) {
									if (!site_diff.containsKey(orig_key)) {
										kinase_diff_nomotiff_site_map.put(orig_key, orig_key);
									}
								} else {
									kinase_diff_nomotiff_site_map.put(orig_key, orig_key);
								}
							}
						}					
					}
				}
				in.close();
				
				
				fstream = new FileInputStream(phosphoFastaFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.contains(">")) {
						String[] split = str.split("\t");
						String tag = split[0].replaceAll(">", "");
						String[] locations = split[1].split("#,");
						for (String loc: locations) {
							if (!loc.equals("")) {
								String orig_key = tag + "\t" + loc.replaceAll("\\*,", "");;
								String key = "NA";
								if (option.equals("GROUP")) {
									if (motif2KinaseGroup.containsKey(orig_key)) {
										key = (String)motif2KinaseGroup.get(orig_key);
									}
								} else if (option.equals("FAMILY")) {
									if (motif2KinaseFamily.containsKey(orig_key)) {
										key = (String)motif2KinaseFamily.get(orig_key);
									}
								} else if (option.equals("NAME")) {
									if (motif2MotifName.containsKey(orig_key)) {
										key = (String)motif2MotifName.get(orig_key);
									}
								} else {
									key = orig_key;
								}
								
								boolean cond1 = false;
								boolean cond2 = false;
								boolean cond3 = false;
								if (kinase_diff_nomotiff_site_map != null) {
									if (!kinase_diff_nomotiff_site_map.containsKey(orig_key)) {
										cond1 = true;
									} 
								} else {
									cond1 = true;
								}
								if (site_diff != null) {
									if (!site_diff.containsKey(orig_key)) {
										cond2 = true;
									} 
								} else {
									cond2 = true;
								}
								if (site_motif != null) {
									if (!site_motif.containsKey(orig_key)) {
										cond3 = true;
									} 
								} else {
									cond3 = true;
								}
								
								if (cond1 && cond2 && cond3) {
									kinase_nodiff_nomotif_site_map.put(orig_key, orig_key);
								}
							}
						}					
					}
				}
				in.close();
				int a = 0;
				if (site_diff != null) {
					a = site_diff.size();
				}
				int b = 0;
				if (kinase_diff_nomotiff_site_map != null) {
					b = kinase_diff_nomotiff_site_map.size();
				}
				
				int c = 0;
				if (site_motif != null) {
					c = site_motif.size();
				}
				int d = 0;
				if (kinase_nodiff_nomotif_site_map != null) {
					d = kinase_nodiff_nomotif_site_map.size();
				}
				double fisher_pvalue = MathTools.fisherTest(a, b, c, d);
				out.write(motif + "\t" + fisher_pvalue + "\t" + fisher_pvalue * all_motif.size() + "\t" + a + "\t" + b + "\t" + c + "\t" + d + "\n");								
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
