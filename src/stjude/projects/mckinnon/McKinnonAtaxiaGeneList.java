package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class McKinnonAtaxiaGeneList {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap comprehensive_database = new HashMap();
			HashMap knowledge_map = new HashMap();
			String disease_knowledge_file = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\human_disease_knowledge_full.tsv";
			FileInputStream fstream = new FileInputStream(disease_knowledge_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();								
				String[] split = str.split("\t");
				String gene = split[1];
				String text = split[3].toUpperCase();
				
				if (comprehensive_database.containsKey("Knowledge_" + text)) {
					LinkedList list = (LinkedList)comprehensive_database.get("Knowledge_" + text);
					if (!list.contains(gene)) {
						list.add(gene);
					}
					comprehensive_database.put("Knowledge_" + text, list);			
				} else {
					LinkedList list = new LinkedList();
					list.add(gene);
					comprehensive_database.put("Knowledge_" + text, list);
				}
				if (text.contains("ATAXIA")) {
					if (knowledge_map.containsKey(gene)) {
						String prev_text = (String)knowledge_map.get(gene);
						prev_text += "," + text;
						knowledge_map.put(gene, prev_text);
					} else {
						knowledge_map.put(gene, text);
					}
				}		
			}
			in.close();
			
			String outputFile2 = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\KnowledgeDiseaseDB.gmt";
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			Iterator itr = comprehensive_database.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out2.write(pathway + "\t" + pathway);
				LinkedList list = (LinkedList)comprehensive_database.get(pathway);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene2 = (String)itr2.next();
					out2.write("\t" + gene2);
				}
				out2.write("\n");
			}
			out2.close();
			
			comprehensive_database = new HashMap();
			
			HashMap experiment_map = new HashMap();
			String disease_experiment_file = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\human_disease_experiments_full.tsv";
			fstream = new FileInputStream(disease_experiment_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();								
				String[] split = str.split("\t");
				String gene = split[1];
				String text = split[3].toUpperCase();
				if (text.contains("ATAXIA")) {
					if (experiment_map.containsKey(gene)) {
						String prev_text = (String)experiment_map.get(gene);
						prev_text += "," + text;
						experiment_map.put(gene, prev_text);
					} else {
						experiment_map.put(gene, text);
					}
				}			
				if (comprehensive_database.containsKey("Experiment_" + text)) {
					LinkedList list = (LinkedList)comprehensive_database.get("Experiment_" + text);
					if (!list.contains(gene)) {
						list.add(gene);
					}
					comprehensive_database.put("Experiment_" + text, list);			
				} else {
					LinkedList list = new LinkedList();
					list.add(gene);
					comprehensive_database.put("Experiment_" + text, list);
				}
			}
			in.close();
			
			outputFile2 = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\ExperimentDiseaseDB.gmt";
			fwriter2 = new FileWriter(outputFile2);
			out2 = new BufferedWriter(fwriter2);
			itr = comprehensive_database.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out2.write(pathway + "\t" + pathway);
				LinkedList list = (LinkedList)comprehensive_database.get(pathway);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene2 = (String)itr2.next();
					out2.write("\t" + gene2);
				}
				out2.write("\n");
			}
			out2.close();
			comprehensive_database = new HashMap();
			
			HashMap textmining_map = new HashMap();
			String disease_textmining_file = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\human_disease_textmining_full.tsv";
			fstream = new FileInputStream(disease_textmining_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));		
			while (in.ready()) {
				String str = in.readLine();								
				String[] split = str.split("\t");
				String gene = split[1];
				String text = split[3].toUpperCase();
				if (text.contains("ATAXIA")) {
					if (textmining_map.containsKey(gene)) {
						String prev_text = (String)textmining_map.get(gene);
						prev_text += "," + text;
						textmining_map.put(gene, prev_text);
					} else {
						textmining_map.put(gene, text);
					}
				}			
				
				if (comprehensive_database.containsKey("TextMining_" + text)) {
					LinkedList list = (LinkedList)comprehensive_database.get("TextMining_" + text);
					if (!list.contains(gene)) {
						list.add(gene);
					}
					comprehensive_database.put("TextMining_" + text, list);			
				} else {
					LinkedList list = new LinkedList();
					list.add(gene);
					comprehensive_database.put("TextMining_" + text, list);
				}
			}
			in.close();
			
			outputFile2 = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\TextMiningDiseaseDB.gmt";
			fwriter2 = new FileWriter(outputFile2);
			out2 = new BufferedWriter(fwriter2);
			itr = comprehensive_database.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out2.write(pathway + "\t" + pathway);
				LinkedList list = (LinkedList)comprehensive_database.get(pathway);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String gene2 = (String)itr2.next();
					out2.write("\t" + gene2);
				}
				out2.write("\n");
			}
			out2.close();
			
			System.out.println(knowledge_map.size());
			System.out.println(experiment_map.size());
			System.out.println(textmining_map.size());
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\RNASEQ\\AtaxiaGeneList\\Ataxia_Summary.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Type\tGene\tText\n");
			itr = knowledge_map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String text = (String)knowledge_map.get(gene);
				out.write("knowledge" + "\t" + gene + "\t" + text + "\n");
			}
			itr = experiment_map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String text = (String)experiment_map.get(gene);
				out.write("Experiment" + "\t" + gene + "\t" + text + "\n");
			}
			itr = textmining_map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String text = (String)textmining_map.get(gene);
				out.write("TextMining" + "\t" + gene + "\t" + text + "\n");
			}
			out.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
