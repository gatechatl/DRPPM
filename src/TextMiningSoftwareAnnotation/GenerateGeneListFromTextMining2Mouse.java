package TextMiningSoftwareAnnotation;

import idconversion.cross_species.HumanMouseGeneNameConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateGeneListFromTextMining2Mouse {

	public static void main(String[] args) {
		
		HashMap map = new HashMap();
		HashMap ataxia = new HashMap();
		try {
			
			String inputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\disease_textmining_jensonlab.txt";
			String outputFolder = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\Disease_Mouse";
			String outputListTxt = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\DiseaseListMouse.txt";
			String outputAtaxia = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\AtaxiaMouse.txt";
			String human2mouseFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\hs_mm_homo_r66.txt";

			HashMap human2mouse = HumanMouseGeneNameConversion.human2mouse(human2mouseFile);
			
			FileWriter fwriter3 = new FileWriter(outputAtaxia);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (human2mouse.containsKey(split[1])) {
					split[1] = (String)human2mouse.get(split[1]);
				}
				if (map.containsKey(split[3])) { 
					LinkedList list = (LinkedList)map.get(split[3]);
					if (!list.contains(split[1])) {
						list.add(split[1]);
						map.put(split[3], list);
					}
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(split[1])) {
						list.add(split[1]);
						map.put(split[3], list);
					}
					
				}
				if (split[3].toUpperCase().contains("ATAXIA")) {
					if (ataxia.containsKey(split[1])) {
						LinkedList list = (LinkedList)ataxia.get(split[1]);
						if (!list.contains(split[3])) {
							list.add(split[3]);
							ataxia.put(split[1], list);
						}
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[3])) {
							list.add(split[3]);
							ataxia.put(split[1], list);
						}
					}
				}
			}
			in.close();
			
			FileWriter fwriter2 = new FileWriter(outputListTxt);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
						
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String diseaseName = (String)itr.next();
				
				out2.write(diseaseName.replaceAll(" ", "_").replaceAll("\\/", "_") + ".txt\n");
				FileWriter fwriter = new FileWriter(outputFolder + "\\" + diseaseName.replaceAll(" ", "_").replaceAll("\\/", "_") + ".txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				LinkedList list = (LinkedList)map.get(diseaseName);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String str = (String)itr2.next();
					out.write(str + "\n");
				}
				out.close();
			}
			out2.close();
			
			
			itr = ataxia.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				LinkedList disease_list = new LinkedList();
				Iterator itr2 = map.keySet().iterator();
				while (itr2.hasNext()) {
					String disease = (String)itr2.next();
					LinkedList list = (LinkedList)map.get(disease);
					if (list.contains(geneName)) {
						if (disease.toUpperCase().contains("ATAXIA")) {
							if (!disease_list.contains(disease)) {
								disease_list.add(disease);
							}
						}
					}
				}
				String disease_str = "";
				itr2 = disease_list.iterator();
				while (itr2.hasNext()) {
					String disease = (String)itr2.next();
					if (disease_str.equals("")) {
						disease_str = disease;
					} else {
						disease_str += "," + disease;
					}
				}
				out3.write(geneName + "\t" + disease_str + "\n");
			}
			out3.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
