package functional.pathway.visualization.webcytoscape;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import misc.CommandLine;

/**
 * Use Json file to display network in javascript/html
 * @author tshaw
 *
 */
public class DisplayJsonFileNetwork {

	public static String description() {
		return "Use Json file to display network in javascript/html";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[jsonFile] [networkName] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String networkName = args[1];
			String outputFolder = args[2];			
			
			File file = new File(outputFolder);
			if (file.exists()) {
				System.out.println("Folder: " + outputFolder + " already exist.");
				System.exit(0);
			}
			file.mkdir();
			
			String outputJSFile = outputFolder + "/output.js";
			String outputHTML = outputFolder + "/index.html";
			
			String json = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				json += str;
			}
			in.close();
			FileWriter fwriter = new FileWriter(outputJSFile);
			BufferedWriter out = new BufferedWriter(fwriter);
								
			out.write(regenerateJavaScript(json));
			out.close();
			
			fwriter = new FileWriter(outputHTML);
			out = new BufferedWriter(fwriter);
			out.write(NetworkDisplayTool.generateHTML(networkName));
			out.close();
			
			CommandLine.executeCommand("cd " + outputFolder + "; wget 10.4.13.112/js/pic.css");
			CommandLine.executeCommand("cd " + outputFolder + "; wget 10.4.13.112/js/cytoscape.js-cxtmenu.js");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String regenerateJavaScript(String jsonFileContent) {
		String str = "$(function(){ // on dom ready\n";

		str += "$('#cy').cytoscape({\n";
		str += "style: cytoscape.stylesheet()\n";
		str += ".selector('node')\n";
		str += ".css({\n";
		str += "'shape': 'data(faveShape)',\n";
        //str += "'width': 'mapData(biz, 0, 1, 50, 100)',\n";
		str += "'width': 'data(biz)',\n";
        //str += "'height': 'mapData(biz, 0, 1, 50, 100)',\n";
		str += "'height': 'data(biz)',\n";
		//str += "'height': 160,\n";
		//str += "'width': 160,\n";
		str += "'font-size': 10,\n";
		str += "'content': 'data(name)',\n";
		str += "'background-fit': 'cover',\n";
		str += "'border-color': 'data(faveColor)',\n";
		str += "'background-color': 'data(backcolor)',\n";
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
		str += "'width': 'data(width)',\n";
		str += "'target-arrow-shape': 'data(arrowshape)',\n";
		str += "'target-arrow-fill': 'hollow',\n";
		str += "'line-style': 'data(linestyle)',\n";
		str += "'opacity': 'data(opacity)',\n";
		str += "'line-color': 'data(linecolor)',\n";
		str += "'target-arrow-color': 'data(linecolor)'\n";
		str += "})\n";
		str += ",\n";
		str += jsonFileContent.substring(1, jsonFileContent.length() - 1);
		str += ",\n";
		str += "layout: {\n";
		str += "name: 'preset',\n";
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
		str += "}\n";
		str += "});\n";
		str += "});\n";
		str += "function outputJPG() {\n";
		str += "var jpg64 = cy.jpg();\n";
		str += "window.open(jpg64);\n";
		str += "}\n";
		
		str += "function outputPNG() {\n";
		str += "var png = cy.png();\n";
		str += "window.open(png);\n";
		str += "}\n";

		str += "function outputJson() {\n";
		str += "var json = cy.json();\n";
		str += "var data = \"data:text/json;charset=utf-8,\" + encodeURIComponent(JSON.stringify(json));\n";
		str += "var dlAnchorElem = document.getElementById('downloadAnchorElem');\n";
		str += "dlAnchorElem.setAttribute(\"href\", data);\n";
		str += "dlAnchorElem.setAttribute(\"download\", \"graph.json\");\n";
		str += "dlAnchorElem.click();\n";
		str += "}\n";

		str += "function setFontSize() {\n";
		str += "var jobValue = document.getElementsByName('txtFontSize')[0].value;\n";
		str += "cy.nodes().style({\"font-size\": jobValue})\n";
		str += "}\n";
		str += "function setLayoutCose() {\n";
		str += "cy.layout({ name: 'cose' });\n";
		str += "}\n";
		str += "function setLayoutGrid() {\n";
		str += "cy.layout({ name: 'grid' });\n";
		str += "}\n";
		str += "function setLayoutPreset() {\n";
		str += "cy.layout({ name: 'preset'});\n";
		str += "cy.layout({directed: true});\n";
		str += "cy.layout({padding: 10});\n";
		str += "}\n";
		str += "function setLayoutCircle() {\n";
		str += "cy.layout({ name: 'circle' });\n";
		str += "}\n";
		str += "function setLayoutDagre() {\n";
		str += "cy.layout({ name: 'dagre' });\n";
		str += "}\n";
		str += "function setLayoutSpread() {\n";
		str += "cy.layout({ name: 'spread' });\n";
		str += "}\n";
		str += "function setLayoutRandom() {\n";
		str += "cy.layout({ name: 'random' });\n";
		str += "}\n";
		str += "function setLayoutBreadthFirst() {\n";
		str += "cy.layout({ name: 'breadthfirst' });\n";
		str += "}\n";
		str += "function setLayoutConcentric() {\n";
		str += "cy.layout({ name: 'concentric' });\n";
		str += "}\n";
		return str;
	}
}
