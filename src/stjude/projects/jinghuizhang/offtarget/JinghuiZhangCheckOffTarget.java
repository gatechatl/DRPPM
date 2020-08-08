package stjude.projects.jinghuizhang.offtarget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Custom off target
 * @author tshaw
 *
 */
public class JinghuiZhangCheckOffTarget {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\CRISPR_Hits_outputFile.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String listOfCandidate = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\CRISPR_RGEN_Tools_result_20181009.txt";
			FileInputStream fstream = new FileInputStream(listOfCandidate);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[3];
				String loc = split[4];
				map.put(chr + "\t" + loc, "");
			}
			in.close();
			String[] batch = {"B1", "B1", "B1", "B2", "B2", "B2", "B2", "B2", "B3", "B3", "B3", "B3"};
			HashMap all_location_sample = new HashMap();
			HashMap all_location_crispr_hit = new HashMap();
			HashMap all_location_batch = new HashMap();
			HashMap all_locatioN_gene = new HashMap();
			String[] files = {"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_bs_clone\\SJTALL020010_C144.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_bs_clone\\SJTALL020010_C145.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_bs_clone\\SJTALL020010_C146.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_dl\\SJTALL020010_C137.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_dl\\SJTALL020010_C138.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_dl\\SJTALL020010_C139.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_dl\\SJTALL020010_C140.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Jurkat_dl\\SJTALL020010_C141.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Shondra\\SJTALL054825_C1.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Shondra\\SJTALL054826_C1.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Shondra\\SJTALL054827_C1.high_20.out.filter.anno.txt",
					"\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_CRISPR_Information\\Offtarget\\USP7_KO_Shondra\\SJTALL054828_C1.high_20.out.filter.anno.txt",
					};
							
			int index = 0;
			for (String file: files) {
				
				String current_batch = batch[index];
				index++;
				
				File f = new File(file);
				String name = f.getName();
				System.out.println("progress: " + name);
				fstream = new FileInputStream(file);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String chr = split[8];
					String hit = "";
					int loc = new Integer(split[9]);
					Iterator itr = map.keySet().iterator();
					while (itr.hasNext()) {
						String site = (String)itr.next();
						String predicted_crispr_site_chr = site.split("\t")[0];
						int predicted_crispr_site_loc = new Integer(site.split("\t")[1]);
						if (chr.equals(predicted_crispr_site_chr)) {
							if (loc - 50 < predicted_crispr_site_loc && predicted_crispr_site_loc < loc + 50) {
								hit += site + ",";
								if (all_location_crispr_hit.containsKey(chr + "\t" + loc)) {
									String prev_site = (String)all_location_crispr_hit.get(chr + "\t" + loc);
									prev_site += site + ",";
									all_location_crispr_hit.put(chr + "\t" + loc, prev_site);
								} else {
									all_location_crispr_hit.put(chr + "\t" + loc, site + ",");
								}
							}
						}
					}
					if (all_location_sample.containsKey(chr + "\t" + loc)) {
						String prev_sampleName = (String)all_location_sample.get(chr + "\t" + loc);
						if (!prev_sampleName.contains(name)) {
							prev_sampleName += name + ",";
						}
						all_location_sample.put(chr + "\t" + loc, prev_sampleName);
					} else {
						all_location_sample.put(chr + "\t" + loc, name + ",");
					}
					
					if (all_location_batch.containsKey(chr + "\t" + loc)) {
						String prev_batch = (String)all_location_batch.get(chr + "\t" + loc);
						if (!prev_batch.contains(current_batch)) {
							prev_batch += current_batch + ",";
						}
						all_location_batch.put(chr + "\t" + loc, prev_batch);
					} else {
						all_location_batch.put(chr + "\t" + loc, current_batch + ",");
					}
					all_locatioN_gene.put(chr + "\t" + loc, split[0]);
				}
				in.close();
			}
			
			Iterator itr = all_location_sample.keySet().iterator();
			while (itr.hasNext()) {
				String site = (String)itr.next();
				String sampleName = (String)all_location_sample.get(site);
				String batchName = (String)all_location_batch.get(site);
				String crispr_hit = "NA";
				
				if (all_location_crispr_hit.containsKey(site)) {
					crispr_hit = (String)all_location_crispr_hit.get(site);
				}
				if (all_location_crispr_hit.containsKey(site)) {
					crispr_hit = (String)all_location_crispr_hit.get(site);
				}
				out.write(all_locatioN_gene.get(site) + "\t" + site + "\t" + sampleName + "\t" + sampleName.split(",").length + "\t" + batchName + "\t" + batchName.split(",").length + "\t" + crispr_hit + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
