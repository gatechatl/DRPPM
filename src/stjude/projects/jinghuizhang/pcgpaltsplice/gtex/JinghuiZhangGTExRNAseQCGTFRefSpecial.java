package stjude.projects.jinghuizhang.pcgpaltsplice.gtex;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * I found that RNAseQC reorders the exon based on their directionality. For the reverse strand
 * @author tshaw
 *
 */
public class JinghuiZhangGTExRNAseQCGTFRefSpecial {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap gene2exon = new HashMap();
			LinkedList geneID_list = new LinkedList();
			HashMap geneID_pos = new HashMap();
			HashMap geneID_neg = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\gtf\\hg19\\fromGTEx_20190226_download\\RNAseQC_forGTEx_gencode.v19.genes.v7.patched_contigs.exon.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("RNAseQC_ID\tgeneSymbol\tchr\tstart\tend\tdirection\texon_length\n");
			String inputGTExGTFFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\gtf\\hg19\\fromGTEx_20190226_download\\gencode.v19.genes.v7.patched_contigs.gtf";
			FileInputStream fstream = new FileInputStream(inputGTExGTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!str.substring(0, 1).equals("#")) {
					String chr = split[0];
					String start = split[3];
					String end = split[4];
					int length = new Integer(end) - new Integer(start) + 1;
					String direction = split[6];
					String meta = split[8];
					String geneID = GTFFile.grabMeta(meta, "gene_id");
					String geneName = GTFFile.grabMeta(meta, "gene_name");
					if (!geneID_list.contains(geneID)) {
						geneID_list.add(geneID);
					}
					if (!gene2exon.containsKey(geneID)) {
						gene2exon.put(geneID, new LinkedList());
					}
					if (direction.equals("+")) {
						geneID_pos.put(geneID, geneID);
					} else if (direction.equals("-")) {
						geneID_neg.put(geneID, geneID);
					}
					if (split[2].equals("exon")) {
						LinkedList exon_list = (LinkedList)gene2exon.get(geneID);
						int id = exon_list.size();
						exon_list.add(geneName + "\t" + chr + "\t" + start + "\t" + end + "\t" + direction + "\t" + length);
						gene2exon.put(geneID, exon_list);
					}
				}
			}
			in.close();			

			Iterator itr = geneID_list.iterator();
			while (itr.hasNext()) {
				String geneID = (String)itr.next();
				
				if (geneID_pos.containsKey(geneID)) {
					LinkedList exon = (LinkedList)gene2exon.get(geneID);
					int id = 1;
					Iterator itr2 = exon.iterator();					
					while (itr2.hasNext()) {
						String line = (String)itr2.next();
						out.write(geneID + "_" + id + "\t" + line + "\n");
						id++;
					}
				}
				if (geneID_neg.containsKey(geneID)) {
					LinkedList exon = (LinkedList)gene2exon.get(geneID);
					Collections.reverse(exon);
					int id = 1;
					Iterator itr2 = exon.iterator();					
					while (itr2.hasNext()) {
						String line = (String)itr2.next();
						out.write(geneID + "_" + id + "\t" + line + "\n");
						id++;
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
