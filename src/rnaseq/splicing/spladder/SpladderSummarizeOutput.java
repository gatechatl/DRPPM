package rnaseq.splicing.spladder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SpladderSummarizeOutput {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Generate summary outputfiles for spladder.";
	}
	public static String parameter_info() {
		return "[filePath] [output_prefix]";
	}
	public static void execute(String[] args) {
		try {
			
			HashMap alt_3prime = new HashMap();
			HashMap alt_3prime_loc = new HashMap();
			HashMap alt_5prime = new HashMap();
			HashMap alt_5prime_loc = new HashMap();
			HashMap exon_skip = new HashMap();
			HashMap exon_skip_loc = new HashMap();
			HashMap intron_retention = new HashMap();
			HashMap intron_retention_loc = new HashMap();
			HashMap mult_exon_skip = new HashMap();
			HashMap mult_exon_skip_loc = new HashMap();
			HashMap mutex_exons = new HashMap();
			HashMap mutex_exons_loc = new HashMap();
			HashMap sample_name_map = new HashMap();
			
			
			String filePath = args[0];
			String output_prefix = args[1];
			
			String output_alt_3prime = output_prefix + "_alt_3prime.txt";
			FileWriter fwriter_alt_3prime = new FileWriter(output_alt_3prime);
            BufferedWriter out_alt_3prime = new BufferedWriter(fwriter_alt_3prime);

			String output_alt_5prime = output_prefix + "_alt_5prime.txt";
			FileWriter fwriter_alt_5prime = new FileWriter(output_alt_5prime);
            BufferedWriter out_alt_5prime = new BufferedWriter(fwriter_alt_5prime);
            
            String output_exon_skip = output_prefix + "_exon_skip.txt";
			FileWriter fwriter_exon_skip = new FileWriter(output_exon_skip);
            BufferedWriter out_exon_skip = new BufferedWriter(fwriter_exon_skip);
            
            String output_intron_retention = output_prefix + "_intron_retention.txt";
			FileWriter fwriter_intron_retention = new FileWriter(output_intron_retention);
            BufferedWriter out_intron_retention = new BufferedWriter(fwriter_intron_retention);
            
            String output_multi_exon = output_prefix + "_multi_exon.txt";
			FileWriter fwriter_multi_exon = new FileWriter(output_multi_exon);
            BufferedWriter out_multi_exon = new BufferedWriter(fwriter_multi_exon);
            

            String output_mutex_exon = output_prefix + "_mutex_exon.txt";
			FileWriter fwriter_mutex_exon = new FileWriter(output_mutex_exon);
            BufferedWriter out_mutex_exon = new BufferedWriter(fwriter_mutex_exon);
            
			File files = new File(filePath);
			for (File f: files.listFiles()) {
				
				if (f.isDirectory()) {
					String sampleName = f.getName();
					sample_name_map.put(sampleName, sampleName);
					
					for (File f2: f.listFiles()) {
						if (f2.getName().equals("merge_graphs_alt_3prime_C3.confirmed.txt")) {
							
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9];
								String geneID = split[3];
								String psi = split[14];
								alt_3prime.put(sampleName + "\t" + geneID + "\t" + loc, psi);
								alt_3prime_loc.put(geneID + "\t" + loc,  loc);
							}
							in.close();
						}
						if (f2.getName().equals("merge_graphs_alt_5prime_C3.confirmed.txt")) {
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								if (split.length >= 15) {
									String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9];
									String geneID = split[3];
									String psi = split[14];
									alt_5prime.put(sampleName + "\t" + geneID + "\t" + loc, psi);
									alt_5prime_loc.put(geneID + "\t" + loc,  loc);
								} else {
									System.out.println(f2.getPath());
								}
							}
							in.close();
						}
						if (f2.getName().equals("merge_graphs_exon_skip_C3.confirmed.txt")) {
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9];
								String geneID = split[3];
								String psi = split[16];
								exon_skip.put(sampleName + "\t" + geneID + "\t" + loc, psi);
								exon_skip_loc.put(geneID + "\t" + loc,  loc);
							}
							in.close();
						}
						if (f2.getName().equals("merge_graphs_intron_retention_C3.confirmed.txt")) {
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9];
								String geneID = split[3];
								String psi = split[14];
								intron_retention.put(sampleName + "\t" + geneID + "\t" + loc, psi);
								intron_retention_loc.put(geneID + "\t" + loc,  loc);
							}
							in.close();
						}
						if (f2.getName().equals("merge_graphs_mult_exon_skip_C3.confirmed.txt")) {
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9];
								String geneID = split[3];
								String psi = split[18];
								mult_exon_skip.put(sampleName + "\t" + geneID + "\t" + loc, psi);
								mult_exon_skip_loc.put(geneID + "\t" + loc,  loc);
							}
							in.close();
						}						
						if (f2.getName().equals("merge_graphs_mutex_exons_C3.confirmed.txt")) {
							FileInputStream fstream = new FileInputStream(f2.getPath());
							DataInputStream din = new DataInputStream(fstream);
							BufferedReader in = new BufferedReader(new InputStreamReader(din));
							in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								String loc = split[0] + ":" + split[4] + "," + split[5] + "," + split[6] + "," + split[7] + "," + split[8] + "," + split[9] + "," + split[10] + "," + split[11];
								String geneID = split[3];
								String psi = split[20];
								mult_exon_skip.put(sampleName + "\t" + geneID + "\t" + loc, psi);
								mult_exon_skip_loc.put(geneID + "\t" + loc,  loc);
							}
							in.close();
						}
					}
				}
			}
			
			out_alt_3prime.write("EventLocation\tEventType\tGeneID");
			out_alt_5prime.write("EventLocation\tEventType\tGeneID");
			out_exon_skip.write("EventLocation\tEventType\tGeneID");
			out_intron_retention.write("EventLocation\tEventType\tGeneID");
			out_multi_exon.write("EventLocation\tEventType\tGeneID");
			out_mutex_exon.write("EventLocation\tEventType\tGeneID");
			Iterator itr = sample_name_map.keySet().iterator();
			while (itr.hasNext()) {
				String sample_name = (String)itr.next();
				out_alt_3prime.write("\t" + sample_name);
				out_alt_5prime.write("\t" + sample_name);
				out_exon_skip.write("\t" + sample_name);
				out_intron_retention.write("\t" + sample_name);
				out_multi_exon.write("\t" + sample_name);
				out_mutex_exon.write("\t" + sample_name);
			}
			out_alt_3prime.write("\n");
			out_alt_5prime.write("\n");
			out_exon_skip.write("\n");
			out_intron_retention.write("\n");
			out_multi_exon.write("\n");
			out_mutex_exon.write("\n");
			
			Iterator itr2 = alt_3prime_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_alt_3prime.write(location + "\talt_3prime\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (alt_3prime.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)alt_3prime.get(sample_name + "\t" + geneID + "\t" + location);
						out_alt_3prime.write("\t" + psi);
					} else {
						out_alt_3prime.write("\t" + "NA");
					}
				}
				out_alt_3prime.write("\n");
			}
			out_alt_3prime.close();
			
			itr2 = alt_5prime_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_alt_5prime.write(location + "\talt_5prime\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (alt_5prime.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)alt_5prime.get(sample_name + "\t" + geneID + "\t" + location);
						out_alt_5prime.write("\t" + psi);
					} else {
						out_alt_5prime.write("\t" + "NA");
					}
				}
				out_alt_5prime.write("\n");
			}
			out_alt_5prime.close();
			

			itr2 = exon_skip_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_exon_skip.write(location + "\texon_skip\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (exon_skip.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)exon_skip.get(sample_name + "\t" + geneID + "\t" + location);
						out_exon_skip.write("\t" + psi);
					} else {
						out_exon_skip.write("\t" + "NA");
					}
				}
				out_exon_skip.write("\n");
			}
			out_exon_skip.close();

			itr2 = intron_retention_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_intron_retention.write(location + "\tintron_retention\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (intron_retention.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)intron_retention.get(sample_name + "\t" + geneID + "\t" + location);
						out_intron_retention.write("\t" + psi);
					} else {
						out_intron_retention.write("\t" + "NA");
					}
				}
				out_intron_retention.write("\n");
			}
			out_intron_retention.close();
			
			itr2 = mult_exon_skip_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_multi_exon.write(location + "\tmulti_exon\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (mult_exon_skip.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)mult_exon_skip.get(sample_name + "\t" + geneID + "\t" + location);
						out_multi_exon.write("\t" + psi);
					} else {
						out_multi_exon.write("\t" + "NA");
					}
				}
				out_multi_exon.write("\n");
			}
			out_multi_exon.close();
			
			itr2 = mutex_exons_loc.keySet().iterator();
			while (itr2.hasNext()) {
				String geneID_location = (String)itr2.next();
				String geneID = geneID_location.split("\t")[0];
				String location = geneID_location.split("\t")[1];
				out_mutex_exon.write(location + "\tmutex_exon\t" + geneID);
				itr = sample_name_map.keySet().iterator();
				while (itr.hasNext()) {
					String sample_name = (String)itr.next();
					if (mutex_exons.containsKey(sample_name + "\t" + geneID + "\t" + location)) {
						String psi = (String)mutex_exons.get(sample_name + "\t" + geneID + "\t" + location);
						out_mutex_exon.write("\t" + psi);
					} else {
						out_mutex_exon.write("\t" + "NA");
					}
				}
				out_mutex_exon.write("\n");
			}
			out_mutex_exon.close();
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
