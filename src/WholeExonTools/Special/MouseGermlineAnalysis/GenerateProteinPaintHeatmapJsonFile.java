package WholeExonTools.Special.MouseGermlineAnalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Given the gene list file generate JSON file
 * @author tshaw
 *
 */
public class GenerateProteinPaintHeatmapJsonFile {

	public static void main(String[] args) {
		
		
		try {
			boolean fusion = false;
			HashMap samples = new HashMap();
			HashMap fusionGenes = new HashMap();
			String sampleFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\ProteinPaint\\ProteinPaint_2015_SNV_final_hg19.txt";
			String geneFile = "C:\\Users\\tshaw\\Desktop\\EXONCAP\\McKinnon\\Reorganize\\ProteinPaint\\AnnotationRelevantGeneList_20151230.txt";
			//String sampleFile = args[0];
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String sample = "";
				if (str.contains("#fusion")) {
					str = in.readLine();
					str = in.readLine();
					split = str.split("\t");
					fusion = true;
				}
				
				if (fusion) {
					sample = split[0];
					samples.put(sample,  sample);
					
				} else {
					sample = split[6];
					samples.put(sample,  sample);
				}
			}
			in.close();
			
			HashMap geneList = new HashMap();
			//String sampleFile = args[0];
			String groupTitle = "";
			fstream = new FileInputStream(geneFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					groupTitle = str.replaceAll(">", "").replaceAll(" ", "_");
				} else {
					if (geneList.containsKey(groupTitle)) {
						LinkedList list = (LinkedList)geneList.get(groupTitle);
						list.add(str.trim());
						geneList.put(groupTitle, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(str.trim());
						geneList.put(groupTitle, list);
					}
				}
			}
			in.close();
			System.out.println(generateJSon(samples, geneList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateJSon(HashMap samples, HashMap geneList) {
		
		String result = "{\n";
		result += "  \"samplegroup\": [\n";
		result += "    {\n";
		result += "    \"name\": \"HGG\",\n";
		result += "    \"lst\": [\n";
		int i = 0;
		Iterator itr = samples.keySet().iterator();
		while (itr.hasNext()) {
			String sample = (String)itr.next();
			i++;
			if (i != samples.size()) {
				result += "        {\n";
				result += "          \"name\": \"" + sample + "\"\n";
				result += "        },\n";				
			} else {
				result += "        {\n";
				result += "          \"name\": \"" + sample + "\"\n";
				result += "        }\n";				
			}
		}
		result += "      ]\n";
		result += "    }\n";
		result += "  ],\n";
		result += "  \"genegroup\": [\n";
		itr = geneList.keySet().iterator();
		i = 0;
		while (itr.hasNext()) {
			String pathway = (String)itr.next();
		    result += "    {\n";
		    result += "      \"name\": \"" + pathway + "\",\n";
		    result += "      \"lst\": [\n";
		    LinkedList list = (LinkedList)geneList.get(pathway);
		    int j = 0;
		    Iterator itr2 = list.iterator();
		    while (itr2.hasNext()) {
		    	String gene = (String)itr2.next();
		    	j++;
		    	if (j != list.size()) {
		    		result += "        {\n";
		    		result += "          \"name\": \"" + gene + "\",\n";
		    		result += "          \"sortorder\": 0,\n";
		            result += "          \"samplecount\": 0\n";
		            result += "        },\n";
		    	} else {
		    		result += "        {";
		    		result += "          \"name\": \"" + gene + "\",\n";
		    		result += "          \"sortorder\": 0,\n";
		            result += "          \"samplecount\": 0\n";
		            result += "        }\n";		    		
		    	}		    			    	
		    }
		    result += "      ],\n";
		    result += "      \"border\": \"black\",\n";
		    result += "      \"borderwidth\": 1,\n";
		    result += "      \"laboversize\": true\n";
		    i++;
		    if (i != geneList.size()) {
		    	result += "    },\n";
		    } else {
		    	result += "    }\n";
		    }
		}
		result += "  ],\n";
		result += "  \"rowh\": 20,\n";
		result += "  \"colw\": 20,\n";
		result += "  \"rowspace\": 2,\n";
		result += "  \"colspace\": 2,\n";
		result += "  \"rowgspace\": 10,\n";
		result += "  \"colgspace\": 10,\n";
		result += "  \"rowlabtickspace\": 4,\n";
		result += "  \"collabtickspace\": 4,\n";
		result += "  \"geneonrow\": true,\n";
		result += "  \"rowheadleft\": true,\n";
		result += "  \"colheadtop\": true,\n";
		result += "  \"cellbg\": \"#f4f4f4\",\n";
		result += "  \"rowtick\": 5,\n";
		result += "  \"coltick\": 5,\n";
		result += "  \"rowglabspace\": 5,\n";
		result += "  \"colglabspace\": 5,\n";
		result += "  \"rowglabfontsize\": 15,\n";
		result += "  \"colglabfontsize\": 15,\n";
		result += "  \"crudefill\": false,\n";
		result += "  \"samplecount4gene\": true\n";
		result += "}\n";
		return result;	  
	}
}
