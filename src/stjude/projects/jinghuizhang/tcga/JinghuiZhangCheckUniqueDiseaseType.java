package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangCheckUniqueDiseaseType {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\meta_20000_old.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//System.out.println(str);
				if (split.length > 1) {
					map.put(split[1], split[1]);
				}
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				//System.out.println(type);
				String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\meta_information\\" + type.replaceAll(" ", "_").replaceAll(",", "") + ".py";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write(createScript(type));
				out.close();
				System.out.println("python " + type.replaceAll(" ", "_").replaceAll(",", "") + ".py > " + type.replaceAll(" ", "_").replaceAll(",", "") + "_meta.txt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createScript(String type) {
		String script = "";
		script += "import requests\n";
		script += "import json\n";
		script += "\n";
		script += "cases_endpt = 'https://api.gdc.cancer.gov/cases'\n";
		script += "\n";
		
		script += "fields = [\n";
		script += "\"submitter_id\",\n";
		//script += "    \"case_id\",\n";
		//script += "    \"project.name\",\n";
		//script += "    \"project.primary_site\",\n";
		script += "    \"project.project_id\",\n";
		//script += "    \"project.program.name\",\n";
		script += "    \"primary_site\",\n";
		script += "    \"disease_type\"\n";
		//script += "    \"tissue_source_site.project\",\n";
		//script += "    \"tissue_source_site.tissue_source_site_id\",\n";
		//script += "    \"diagnoses.vital_status\"\n";
		script += "    ]\n";
		script += "\n";
		script += "fields = ','.join(fields)\n";
		
		script += "filters = {\n";
		script += "	    \"op\": \"in\",\n";
		script += "	    \"content\":{\n";
		script += "	        \"field\": \"primary_site\",\n";
		script += "	        \"value\": [\"" + type + "\"]\n";
		script += "	        }\n";
		script += "	    }\n";

		script += "params = {\n";
	    script += "    \"filters\": json.dumps(filters),\n";
		script += "    \"fields\": fields,\n";
		script += "    \"format\": \"TSV\",\n";
		script += "    \"size\": \"10000\"\n";
		script += "    }\n";

		script += "response = requests.get(cases_endpt, params = params)\n";
		script += "print(response.content)\n";
		return script;
	}
}
