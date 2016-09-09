package GeneNetworkDisplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class CreateNetworkDisplay {

	public static String parameter_info() {
		return "[inputFolder] [outputfolder] [networkName]";
	}
	public static void execute(String[] args) {
		//String inputFile = "C:\\Users\\tshaw\\Desktop\\INTEGRATION\\GenerateHTMLExamples\\graph.txt";
		try {
			String inputFile = args[0];
			String outputFolder = args[1];
			String networkName = args[2];
			String outputJSFile = outputFolder + "/output.js";
			String outputHTML = outputFolder + "/index.html";
			
			File file = new File(outputFolder);
			file.mkdir();
			LinkedList kegg_conn_list = readRelationshipFromFile(inputFile);
			LinkedList kegg_list = readEntryFromFile(inputFile);
			
			HashMap expression = new HashMap();
			HashMap raw_expression = new HashMap();
			//System.out.println(kegg_conn_list.size());
			//System.out.println(kegg_list.size());
			

			FileWriter fwriter = new FileWriter(outputJSFile);
			BufferedWriter out = new BufferedWriter(fwriter);
								
			out.write(generateNodes(kegg_conn_list, kegg_list, expression, raw_expression));
			out.close();
			
			fwriter = new FileWriter(outputHTML);
			out = new BufferedWriter(fwriter);
			out.write(generateHTML(networkName));
			out.close();
			
			executeCommand("cd " + outputFolder + "; wget 10.4.13.112/js/pic.css");
			executeCommand("cd " + outputFolder + "; wget 10.4.13.112/js/cytoscape.js-cxtmenu.js");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void executeCommand(String executeThis) {
    	try {
    		
    		String buffer = UUID.randomUUID().toString();
        	writeFile(buffer + "tempexecuteCommand.sh", executeThis);
        	String[] command = {"sh", buffer + "tempexecuteCommand.sh"};
        	Process p1 = Runtime.getRuntime().exec(command);
        	BufferedReader inputn = new BufferedReader(new InputStreamReader(p1.getInputStream()));
        	String line=null;
        	while((line=inputn.readLine()) != null) {}
        	inputn.close();
        	p1.destroy();
        	File f = new File(buffer + "tempexecuteCommand.sh");
        	f.delete();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static void writeFile(String fileName, String command) {
    	try {
        	FileWriter fwriter2 = new FileWriter(fileName);
            BufferedWriter out2 = new BufferedWriter(fwriter2);
            out2.write(command + "\n");
            out2.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static String generateCSS() {
		String script = "";
		script += "body {\n";
		script += "font: 14px helvetica neue, helvetica, arial, sans-serif;\n";
		script += "}\n";
		script += "#cy {\n";
		script += "width: 100%;\n";
		script += "height: 600px;\n";
		script += "border: 1px solid #888;\n";
		script += "}\n";

		script += "#right {\n";
		script += "border: 1px solid #888\n";
		script += "}\n";
		script += "#eat {\n";
		script += "position: absolute;\n";
		script += "left: 1em;\n";
		script += "top: 1em;\n";
		script += "font-size: 1em;\n";
		script += "z-index: -1;\n";
		script += "color: #c88;\n";
		script += "}\n";
		return script;
	}
	public static String generateHTML(String networkName) {
		String script = "";
		script += "<!DOCTYPE html>\n";
		script += "<html>\n";
		script += "<head>\n";
		script += "<link href=\"pic.css\" rel=\"stylesheet\">\n";
		
		script += "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js\"></script>\n";
		script += "<meta charset=utf-8 />\n";
		script += "<title>Cytoscape.js images</title>\n";
		script += "<script src=\"http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js\"></script>\n";
		script += "<script src=\"https://ajax.googleapis.com/ajax/libs/angularjs/1.2.16/angular.min.js\"></script>\n";
		script += "<script src=\"http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js\"></script>\n";
		script += "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.js\"></script>\n";
		script += "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.js\"></script>\n";
		script += "<link href=\"http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n";
		script += "<script src=\"https://rawgit.com/cytoscape/cytoscape.js-qtip/master/cytoscape-qtip.js\"></script>\n";
		script += "<script src=\"cytoscape.js-cxtmenu.js\"></script>\n";
		script += "</head>\n";
		script += "<body>\n";
		script += "<h1>" + networkName + "</h1>\n";
		script += "<table style=\"width:100%\">\n";
		script += "<tr>\n";
		script += "<td width=75% height=100%><div id=\"cy\"></div></td>\n";
		//script += "<td width=25% height=100% valign="top"><div id="right"><IMG SRC="../../Network_Legend.png" ALT="some text" WIDTH=300 HEIGHT=240></div></td>\n";
		script += "</tr>\n";
		script += "</table>\n";
		script += "</body>\n";
		script += "<script src=\"output.js\"></script>\n";
		script += "<script type=\"text/javascript\">\n";
		script += "</script>\n";
		script += "</html>\n";

		return script;
	}
	public static LinkedList readEntryFromFile(String inputFile) {
		LinkedList conn_list = new LinkedList();
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
				map.put(split[2], split[2]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			KEGGentry entry = new KEGGentry();
			entry.ID = key;
			entry.KEGG_NAME = key;
			entry.NAME = key;
			entry.SHORT_NAME = key;
			entry.XCOORD = "100";
			entry.YCOORD = "100";
			conn_list.add(entry);
		}
		return conn_list;
	}
	public static LinkedList readRelationshipFromFile(String inputFile) {
		LinkedList conn_list = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				KEGGrelation relation = new KEGGrelation();
				relation.ENTRY1 = split[0];
				relation.ENTRY1_SHORT_NAME = split[0];
				relation.ENTRY2 = split[2];
				relation.ENTRY2_SHORT_NAME = split[2];
				LinkedList conn = new LinkedList();
				conn.add(split[1]);
				relation.NAME = conn;
				conn_list.add(relation);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn_list;
	}

	public static String generateNodes(LinkedList kegg_conn_list, LinkedList kegg_list, HashMap expression, HashMap rawExpression) {
		String str = "$(function(){ // on dom ready\n";

		str += "$('#cy').cytoscape({\n";
		str += "style: cytoscape.stylesheet()\n";
		str += ".selector('node')\n";
		str += ".css({\n";
		str += "'shape': 'data(faveShape)',\n";
        str += "'width': 'mapData(biz, 0, 1, 50, 100)',\n";
        str += "'height': 'mapData(biz, 0, 1, 50, 100)',\n";
		//str += "'height': 160,\n";
		//str += "'width': 160,\n";
		str += "'font-size': 15,\n";
		str += "'content': 'data(name)',\n";
		str += "'background-fit': 'cover',\n";
		str += "'border-color': 'data(faveColor)',\n";
		//str += "'border-color': '#000',\n";
		//str += "'border-width': 3,\n";
		str += "'border-width': 'data(bwidth)',\n";
		str += "'border-opacity': 0.5\n";
		str += "})\n";
		str += ".selector('.eating')\n";
		str += ".css({\n";
		str += "'border-color': 'red'\n";
		str += "})\n";
		str += ".selector('.eater')\n";
		str += ".css({\n";
		str += "'border-width': 3\n";
		str += "})\n";
		str += ".selector('edge')\n";
		str += ".css({\n";
		str += "'width': 1,\n";
		str += "'target-arrow-shape': 'data(arrowshape)',\n";
		str += "'target-arrow-fill': 'hollow',\n";
		str += "'line-style': 'data(linestyle)',\n";
		//str += "'line-color': '#d3d3d3',\n";
		//str += "'line-color': '#2f4f4f',\n";
		str += "'line-color': 'data(linecolor)',\n";
		//str += "'target-arrow-color': '#d3d3d3'\n";
		//str += "'target-arrow-color': '#2f4f4f'\n";
		str += "'target-arrow-color': 'data(linecolor)'\n";
		str += "})\n";
		int count = 0;
		Iterator itr = kegg_list.iterator();
		while (itr.hasNext()) {
			KEGGentry entry = (KEGGentry)itr.next();
			String key = entry.SHORT_NAME;
			str += ".selector('#" + key + "')\n";
			str += ".css({\n";
			//if (filter.containsKey(key)) {
			//	System.out.println("ls " + "../../../Genes/" + key + ".png");
			
			double value = Double.NaN;
			String rgb = "rgb(255, 255, 255)";
			if (expression.containsKey(key)) {
				String expr = (String)expression.get(key);
				if (expr.equals("NA")) {
					value = Double.NaN;
				} else {
					value = new Double(expr);
					value = value - 0.5;
					//System.out.println(value);
					if (value >= 0) {
						int ratio = new Double((1.0 - value / 0.5) * 255).intValue();
						rgb = "rgb(255," + ratio + "," + ratio + ")"; 
					} else {
						int ratio = new Double((1.0 - value / -0.5) * 255).intValue();
						rgb = "rgb(" + ratio + "," + ratio + ", 255)";
					}
				}
			}
			if (value == Double.NaN) {
				rgb = "rgb(255, 255, 255)";
			}
			str += "'background-color': '" + rgb + "'\n";
			/*if (rawExpression.containsKey(key)) {
				str += "'content': '" + (String)rawExpression.get(key) + "FPKM'\n";
			} else {
				str += "'content': '" + (String)rawExpression.get(key) + "FPKM'\n";
			}*/
			//str += "'background-image': 'Output/" + key + ".png'\n";
				
				
			//} else {
			//	str += "'background-image': '../../../Genes/NotAvailable.png'\n";
			//}
			
			//str += "'background-image': 'Genes/CDK4.png'\n";
			count++;
			if (kegg_list.size() != count) {
				str += "})\n";
			} else {
				str += "}),\n";
			}
		}
		str += "elements: {\n";
		str += "nodes: [\n";
		itr = kegg_list.iterator();
		count = 0;
		while (itr.hasNext()) {
			KEGGentry entry = (KEGGentry)itr.next();
			String key = entry.SHORT_NAME;
			//String key = (String)itr.next();
			String genes = entry.KEGG_NAME.replaceAll("\t", ",");
			
			count++;
			int size = 1000;
			int bwidth = 3;
			String bcolor = "#000";
			
			/*if (oncogene.containsKey(key)) {
				bwidth = 20;
			}
			if (TF.containsKey(key)) {
				bcolor = "#6FB1FC";
			}
			if (kinase.containsKey(key)) {
				bcolor = "#ff0000";
			}*/
			
			String faveShape = "ellipse";
			if (entry.MEMBRANE) {
				faveShape = "hexagon";
			}
			double raw = -1;
			String expr = "";
		        if (rawExpression.containsKey(key)) {
				expr = "" + new Double(new Double(new Double((String)rawExpression.get(key)) * 100).intValue()) / 100;
			}	
			if (kegg_list.size() != count) {				
				str += "{ data: { id: '" + key + "', name: '" + key + "(" + expr + ")" + "', genes: '" + genes + "', weight: 50, faveShape: '" + faveShape + "', baz: " + size + ", faveColor: '" + bcolor + "', bwidth: " + bwidth + "}, position:{x: " + new Double(new Double(entry.XCOORD) * 1.5).intValue() + ",y: " + new Double(new Double(entry.YCOORD) * 1.5).intValue() + "}},\n";
			} else {
				str += "{ data: { id: '" + key + "', name: '" + key + "(" + expr + ")" + "', genes: '" + genes + "', weight: 50, faveShape: '" + faveShape + "', baz: " + size + ", faveColor: '" + bcolor + "', bwidth: " + bwidth + "}, position:{x: " + new Double(new Double(entry.XCOORD) * 1.5).intValue() + ",y: " + new Double(new Double(entry.YCOORD) * 1.5).intValue() + "}}\n";
				//str += "{ data: { id: '" + key + "', weight: 50, faveShape: 'rectangle', baz: " + size + "} }\n";
			}
		}
		str += "],\n";
		str += "edges: [\n";		
		itr = kegg_conn_list.iterator();
		count = 0;
		while (itr.hasNext()) {
			
			KEGGrelation relation = (KEGGrelation)itr.next();
			
			String[] split = {relation.ENTRY1_SHORT_NAME, relation.ENTRY2_SHORT_NAME}; 
			count++;
			String arrow_type = "none";
			String line_style = "solid";
			String line_color = "black";
			Iterator itr2 = relation.NAME.iterator();
			while (itr2.hasNext()) {
				String name = (String)itr2.next();
				
				if (name.equals("activation")) {
					arrow_type = "triangle";
				}
				if (name.equals("inhibition")) {
					arrow_type = "tee";
				}
				if (name.equals("expression")) {
					arrow_type = "triangle";
				}
				if (name.equals("repression")) {
					arrow_type = "tee";
				}
				if (name.equals("missing interaction")) {
					line_style = "dotted";
					arrow_type = "none";
				}
				if (name.equals("indirect effect")) {
					line_style = "dotted";
					arrow_type = "triangle";
				}
				if (name.equals("binding/association")) {
					line_style = "dashed";
					
				}
				if (name.equals("binding/association")) {
					line_style = "dashed";
					line_color = "brown";
				}
				if (name.equals("phosphorylation")) {
					arrow_type = "diamond";
					line_color = "blue";
				}
				if (name.equals("dephosphorylation")) {
					arrow_type = "diamond";
					line_color = "purple";
				}
				if (name.equals("glycosylation")) {
					arrow_type = "circle";
					line_color = "green";
				}
				if (name.equals("ubiquitination")) {
					arrow_type = "square";
					arrow_type = "orange";
				}
				if (name.equals("methylation")) {
					arrow_type = "backcurve";
					arrow_type = "yellow";
				}
			}
			
			if (kegg_conn_list.size() != count) {					
				str += "{ data: { source: '" + split[0] + "', target: '" + split[1] + "', arrowshape: '" + arrow_type + "', linestyle: '" + line_style + "', linecolor: '" + line_color + "'} },\n";
			} else {
				str += "{ data: { source: '" + split[0] + "', target: '" + split[1] + "', arrowshape: '" + arrow_type + "', linestyle: '" + line_style + "', linecolor: '" + line_color + "' } }\n";
			}
		} 
		str += "]\n";
		str += "},\n";
		str += "layout: {\n";
		str += "name: 'breadthfirst',\n";
		//str += "name: '" + graphType + "',\n";
		//str += "name: 'preset',\n";
		str += "directed: true,\n";
		str += "padding: 10\n";
		str += "},\n";

		str += "ready: function(){\n";
		str += "window.cy = this;\n";

		str += "cy.on('tap', 'node', function(){\n";
		str += "var nodes = this;\n";
		str += "var tapped = nodes;\n";
	    str += "var food = [];\n";
		str += "}); // on tap\n";
		str += "cy.userPanningEnabled( true );\n";
		str += "cy.panningEnabled( true );\n";
		str += "cy.boxSelectionEnabled( false );\n";
		str += "cy.cxtmenu({\n";
		str += "commands: [\n";
		str += "{\n";
		str += "content: 'Show TF Targets',\n";
		str += "select: function(){\n";
		str += "console.log( this.id() );\n";
		str += "window.open(\"../../../GeneToTF/runGeneToTFGet.php?uploadtext=\" + this.data('genes'));\n";
		str += "}\n";
		str += "},\n";
		str += "{\n";
		str += "content: 'Show Substrates',\n";
		str += "select: function(){\n";
		str += "console.log( this.id() );\n";
		str += "window.open(\"../../../GeneToKinase/runGeneToKinaseGet.php?uploadtext=\" + this.data('genes'));\n";
		str += "}\n";
		str += "},\n";
		str += "{\n";
		str += "content: 'Show Pathways',\n";
		str += "select: function(){\n";
		str += "console.log( this.id() );\n";
		//alert("I am an alert box!" + this.id());
		str += "window.open(\"../../../GeneToPathway/runGeneToPathwayGet.php?uploadtext=\" + this.data('genes'));\n";
		str += "}\n";
		str += "}\n";
		str += "]\n";
		str += "});\n";
		str += "} // on ready\n";
		str += "}); // cy init\n";
		str += "}); // on dom ready\n";
		
		return str;
	}
	public String generateScript() {
		String str = "<!DOCTYPE html>\n";
		str += "<html>\n";
		str += "<head>\n";
		str += "<link href=\"pic.css\" rel=\"stylesheet\">\n";
		str += "<!--<meta name=\"description\" content=\"pic.js\"/>-->\n";
		str += "<script src=pic.js\"\"></script>\n";
		str += "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js\"></script>\n";
		str += "<meta charset=utf-8 />\n";
		str += "<title>Cytoscape.js images</title>\n";
		str += "<script src=\"http://cytoscape.github.io/cytoscape.js/api/cytoscape.js-latest/cytoscape.min.js\"></script>\n";
		str += "</head>\n";
		str += "<body>\n";
		str += "<div id=\"cy\"></div>\n";
		str += "<div id=\"eat\">Tap to eat</div>\n";
		str += "</body>\n";
		str += "<script type=\"text/javascript\">\n";
		
		return str;
	}
	public static String grabMeta(String str, String tag) {
		String[] split = str.split("\" ");
		for (String query: split) {
			if (query.contains(tag + "=")) {
				query = query.replaceAll(tag + "=", "");
				query = query.replaceAll("\"", "");
				query = query.replaceAll("<graphics ", "");
				query = query.replaceAll("<entry ", "");
				query = query.replaceAll("<subtype ", "");
				query = query.replaceAll("<relation ", "");
				query = query.trim();
				return query;
			}
		}
		return "";
	}
	static class KEGGentry {
		public String ID = "";
		public String SHORT_NAME = "";
		public String NAME = "";
		public String KEGG_NAME = "";
		public String SHAPE = "";
		public String LINK = "";
		public String XCOORD = "";
		public String YCOORD = "";
		public boolean MEMBRANE = false;
		public boolean KINASE = false;
		public boolean TF = false;
		
	}
	static class KEGGrelation {
		
		public String ENTRY1 = "";
		public String ENTRY2 = "";
		public String ENTRY1_SHORT_NAME = "";
		public String ENTRY2_SHORT_NAME = "";
		public String TYPE = "";
		public LinkedList NAME = new LinkedList();
	}

}
