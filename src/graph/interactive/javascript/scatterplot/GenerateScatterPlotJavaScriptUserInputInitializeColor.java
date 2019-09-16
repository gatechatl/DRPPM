package graph.interactive.javascript.scatterplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;
/**
 * Generates html-javascript file of scatter plot with user-input that defines color 
 * need to write code to initialize the color for single cell analysis
 * @author tshaw
 *
 */

public class GenerateScatterPlotJavaScriptUserInputInitializeColor {

	public static String description() {
		return "Generate html-javascript file with user input (GeneName[space]color) ex: Myc blue";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [name_index] [x_axis_index] [y_axis_index] [meta_index] [x_logFC_cutoff] [y_logFC_cutoff] [xaxis_title] [yaxis_title] [meta_title] [color_opacity_name] [color_index_name] [node_size] [SkipHeaderFlag:true/false] [writeNameFlag:true/false";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int name_index = new Integer(args[1]);
			int x_axis_index = new Integer(args[2]);								
			int y_axis_index = new Integer(args[3]);			
			String[] meta_index = args[4].split(","); // Color should be included as a meta_index
			double x_axis_cutoff = new Double(args[5]);
			double y_axis_cutoff = new Double(args[6]);
			String xaxis_title = args[7];
			String yaxis_title = args[8];
			String[] meta_title = args[9].split(",");
			String color_opacity_name = args[10];
			String color_index_name = args[11];
			int node_size = new Integer(args[12]); 
			boolean skipHeader = new Boolean(args[13]);
			boolean writeNameFlag = new Boolean(args[14]);
			HashMap gene_list = new HashMap();
			if (args.length > 15) {
				String geneListFile = args[15];
				FileInputStream fstream = new FileInputStream(geneListFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));			
				while (in.ready()) {
					String str = in.readLine();
					String geneName = str.split("\t")[0].trim();
					gene_list.put(geneName, geneName);
				}
				in.close();
			}
			double min_x_logFC = Double.MAX_VALUE;
			double max_x_logFC = Double.MIN_VALUE;
			
			double min_y_logFC = Double.MAX_VALUE;
			double max_y_logFC = Double.MIN_VALUE;
			
			LinkedList name_list = new LinkedList();			
			LinkedList x_axis_list = new LinkedList();
			LinkedList y_axis_list = new LinkedList();
			LinkedList[] meta_list = new LinkedList[meta_index.length];
			for (int i = 0; i < meta_index.length; i++) {
				meta_list[i] = new LinkedList();
			}
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = "";
			if (skipHeader) {
				header = in.readLine();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				double x_axis_value = new Double(split[x_axis_index]);
				double y_axis_value = new Double(split[y_axis_index]);
				
				if (min_x_logFC > x_axis_value) {
					min_x_logFC = x_axis_value;					
				}
				if (max_x_logFC < x_axis_value) {
					max_x_logFC = x_axis_value;					
				}
				
				if (min_x_logFC > x_axis_value) {
					min_x_logFC = x_axis_value;					
				}
				if (max_x_logFC < x_axis_value) {
					max_x_logFC = x_axis_value;					
				}
				
				if (min_y_logFC > y_axis_value) {
					min_y_logFC = y_axis_value;					
				}
				if (max_y_logFC < y_axis_value) {
					max_y_logFC = y_axis_value;					
				}
				
				if (min_y_logFC > y_axis_value) {
					min_y_logFC = y_axis_value;					
				}
				if (max_y_logFC < y_axis_value) {
					max_y_logFC = y_axis_value;					
				}
				
				
				x_axis_list.add(x_axis_value + "");
				y_axis_list.add(y_axis_value + "");
				
				name_list.add(split[name_index]);
				for (int i = 0; i < meta_index.length; i++) {
					String meta_value = split[new Integer(meta_index[i])];
					meta_list[i].add(meta_value.replaceAll(",", ";") + "");
				}								
			}
			in.close();
			min_x_logFC = min_x_logFC - Math.abs(max_x_logFC - min_x_logFC) * 0.1;
			max_x_logFC = max_x_logFC + Math.abs(max_x_logFC - min_x_logFC) * 0.1;
			
			min_y_logFC = min_y_logFC - Math.abs(max_y_logFC - min_y_logFC) * 0.1;
			max_y_logFC = max_y_logFC + Math.abs(max_y_logFC - min_y_logFC) * 0.1;			
			
			double[] x_axis = MathTools.convertListStr2Double(x_axis_list);
			double[] y_axis = MathTools.convertListStr2Double(y_axis_list);
			String[] meta = new String[y_axis.length];
			for (int i = 0; i < meta.length; i++) {
				meta[i] = "";
			}
			
			for (int i = 0; i < meta_index.length; i++) {
				Iterator itr = meta_list[i].iterator();
				int j = 0;
				while (itr.hasNext()) {					
					String value = (String)itr.next();
					if (i == 0) {
						meta[j] = value;
					} else {
						meta[j] += "," + value;
					}
					j++;
				}
			}
			
			
			
			String[] names = new String[name_list.size()];
			int index = 0;
			Iterator itr = name_list.iterator();
			while (itr.hasNext()) {
				names[index] = (String)itr.next();
				index++;
			}
			File f = new File(inputFile);
			
			String description = "File was generated based on: " + f.getName() + "<br>";
			//description += "Each node represent a gene. Colored node represent gene that pass FDR < 0.05 and log2FC > 1.<br>";
			System.out.println(generate_scatterplot_meta_complex_javascript(names, x_axis, y_axis, meta, min_x_logFC, max_x_logFC, min_y_logFC, max_y_logFC, x_axis_cutoff, y_axis_cutoff, xaxis_title, yaxis_title, meta_title, color_opacity_name, color_index_name, node_size, writeNameFlag, description, gene_list));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_scatterplot_meta_complex_javascript(String[] names, double[] x, double[] y, String[] meta, double min_x, double max_x, double min_y, double max_y, double x_axis_cutoff, double y_axis_cutoff, String xaxis_title, String yaxis_title, String[] meta_title, String color_opacity_name, String color_index_name, int node_size, boolean writeNameFlag, String description, HashMap gene_list) {
		
		String inital_nodeNames = "";
		Iterator itr = gene_list.keySet().iterator();
		while (itr.hasNext()) {
			String geneName = (String)itr.next();
			inital_nodeNames += geneName + "\\n";
		}
		StringBuffer script = new StringBuffer();
		script.append("<!DOCTYPE html>\n");
		script.append("<html>\n");

		script.append("<head>\n");
		script.append("  <script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n");
		script.append("</head>\n");

		script.append("<body>\n");
		//script.append("Each node represent a gene. Colored node represent gene that pass FDR < 0.05 and log2FC > 1.")
		script.append(description + "\n");
		script.append("  <script>\n");
		/*double xmin = Double.MAX_VALUE;
		double xmax = Double.MIN_VALUE;
		double ymin = Double.MAX_VALUE;
		double ymax = Double.MIN_VALUE;*/
		script.append("    var data = [\n");
		for (int i = 0; i < names.length - 1; i++) {				
			script.append("      {\n");
			//script.append("        fdr: " + fdr[i] + ",\n");
			/*if (xmin > avg_expr[i]) {
				xmin = avg_expr[i];
			}
			if (xmax < avg_expr[i]) {
				xmax = avg_expr[i];
			}
			if (ymin > y[i]) {
				ymin = y[i];
			}
			if (ymax < y[i]) {
				ymax = y[i];
			}*/
			script.append("        x: " + x[i] + ",\n");
			script.append("        y: " + y[i] + ",\n");
			String[] meta_values = meta[i].split(",");
			for (int j = 0; j < meta_values.length; j++) {
				script.append(" " + meta_title[j] + ":\"" + meta_values[j] + "\",\n");
			}
			script.append("	name:\"" + names[i] + "\"\n");
			
			script.append("      },\n");
		}
		script.append("      {\n");
		//script.append("        fdr: " + fdr[names.length - 1] + ",\n");

		script.append("        x: " + x[names.length - 1] + ",\n");
		script.append("        y: " + y[names.length - 1] + ",\n");

		String[] meta_values = meta[names.length - 1].split(",");
		for (int j = 0; j < meta_values.length; j++) {
			script.append(" " + meta_title[j] + ":\"" + meta_values[j] + "\",\n");
		}
		
		//script.append(" meta:\"" + meta[names.length - 1] + "\",\n");
		script.append("	name:\"" + names[names.length - 1] + "\"\n");
		script.append("      },\n");
		script.append("    ];\n");
		
		     // size and margins for the chart
		script.append("    var margin = {\n");
		script.append("      top: 50,\n");
		script.append("      right: 100,\n");
		script.append("      bottom: 100,\n");
		script.append("      left: 100\n");		
		script.append("    }, width = 1000 - margin.left - margin.right,\n");
		script.append("      height = 800 - margin.top - margin.bottom;\n");		

		script.append("    var x = d3.scale.linear()\n");
		script.append("      .domain([" + min_x + ", " + max_x + "])\n");
		script.append("      .range([0, width]);\n");

		script.append("    var y = d3.scale.linear()\n");
		script.append("      .domain([" + min_y + ", " + max_y + "])\n");
		script.append("      .range([height, 0]);\n");

		script.append("    var tip = d3.select('body')\n");
		script.append("      .append('div')\n");
		script.append("      .attr('class', 'tip')\n");
		script.append("      .style('border', '1px solid steelblue')\n");
		script.append("      .style('padding', '5px')\n");
		script.append("      .style('position', 'absolute')\n");
		script.append("      .style('display', 'none')\n");
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(1);\n");	
		script.append("      })\n");
		script.append("      .on('mouseout', function(d, i) {\n");
		script.append("        tip.style('display', 'none');\n");
		script.append("      });\n");

		script.append("    var chart = d3.select('body')\n");
		script.append("      .append('svg')\n");
		script.append("      .attr('width', width + margin.right + margin.left)\n");
		script.append("      .attr('height', height + margin.top + margin.bottom)\n");
		script.append("      .attr('class', 'chart')\n");

		script.append("     var main = chart.append('g')\n");
		script.append("      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')\n");
		script.append("      .attr('width', width)\n");
		script.append("      .attr('height', height)\n");
		script.append("      .attr('class', 'main')\n");

		script.append("     var xAxis = d3.svg.axis()\n");
		script.append("      .scale(x)\n");
		script.append("      .orient('bottom');\n");

		script.append("    var yAxis = d3.svg.axis()\n");
		script.append("      .scale(y)\n");
		script.append("      .orient('left');\n");
		
		// start the function for the image rendering
		script.append("    function renderImage(nodeName) {\n");

		// append the axis
		script.append("     main.append('g')\n");
	    script.append("      .style(\"font-size\",\"20px\")\n");		
		script.append("      .attr('transform', 'translate(0,' + height + ')')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(xAxis);\n");

		script.append("    main.append('g')\n");
	    script.append("      .style(\"font-size\",\"20px\")\n");
		script.append("      .attr('transform', 'translate(0,0)')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(yAxis);\n");

		script.append("        var split_nodeName = nodeName.split(\"\\n\");\n");
		
        
        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + min_x + "))\n");
	    script.append("    .attr(\"x2\", x(" + max_x + "))\n");
		script.append("    .attr(\"y1\", y(0))\n");
		script.append("    .attr(\"y2\", y(0))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n");
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");

        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + min_x + "))\n");
	    script.append("    .attr(\"x2\", x(" + max_x + "))\n");
		script.append("    .attr(\"y1\", y(" + y_axis_cutoff + "))\n");
		script.append("    .attr(\"y2\", y(" + y_axis_cutoff + "))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n");
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");

        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + min_x + "))\n");
	    script.append("    .attr(\"x2\", x(" + max_x + "))\n");
		script.append("    .attr(\"y1\", y(" + -y_axis_cutoff + "))\n");
		script.append("    .attr(\"y2\", y(" + -y_axis_cutoff + "))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n");
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");

        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(0))\n");
	    script.append("    .attr(\"x2\", x(0))\n");
		script.append("    .attr(\"y1\", y(" + min_y + "))\n");
		script.append("    .attr(\"y2\", y(" + max_y + "))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n"); 
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");
		
        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + x_axis_cutoff + "))\n");
	    script.append("    .attr(\"x2\", x(" + x_axis_cutoff + "))\n");
		script.append("    .attr(\"y1\", y(" + min_y + "))\n");
		script.append("    .attr(\"y2\", y(" + max_y + "))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n"); 
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");
		
		script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + -x_axis_cutoff + "))\n");
	    script.append("    .attr(\"x2\", x(" + -x_axis_cutoff + "))\n");
		script.append("    .attr(\"y1\", y(" + min_y + "))\n");
		script.append("    .attr(\"y2\", y(" + max_y + "))\n");
		script.append("    .attr(\"stroke-width\", 1)\n");
		script.append("    .style(\"opacity\",0.4)\n"); 
		script.append("    .style(\"stroke-dasharray\", (\"10, 10\"))\n");
		script.append("    .style(\"stroke\", \"black\");\n");
		
		script.append("        var g = main.append(\"svg:g\");\n");
		
		// show the undifferentiated genes
		script.append("        g.selectAll(\"scatter-dots\")\n");
		script.append("          .data(data)\n");
		script.append("          .enter().append(\"svg:circle\")\n");
		script.append("          .attr(\"cy\", function(d) {\n");
		script.append("            return y(d.y);\n");
		script.append("          })\n");
		script.append("          .attr(\"cx\", function(d, i) {\n");
		script.append("            return x(d.x);\n");
		script.append("          })\n");
		
		script.append("          .attr(\"r\", " + node_size + ")\n");
		
		script.append("          .style(\"stroke\", \"black\")\n");		
		script.append("          .style(\"stroke-width\", \"1.4\")\n");		
		
		/*script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i]) {return 0.0;}\n");
		script.append("          }\n");
		script.append("          return 1.0;})\n");*/
		
		script.append("\n");
		script.append("          .style(\"fill\", function(d, i) { return d." + color_index_name + "})\n");
		script.append("          .style(\"opacity\", function(d, i) { return d." + color_opacity_name + ";})\n");
		script.append("          .on('mouseover', function(d, i) {\n");
		script.append("            tip.transition().duration(0);\n");
		script.append("	    tip.html(d.name + \"<br>Log2FC: \" + round(d.y,2) + \"<br>adj.pval:\" + d.fdr);\n");		
		script.append("            tip.style('top', (y(d.y) + 150) + 'px');\n");
		script.append("            tip.style('left', (x(d.x) + 50) + 'px');\n");
		script.append("            tip.style('display', 'block');\n");
		script.append("            tip.style(\"background\",'#BCC5F7');\n");
		script.append("          })\n");
		script.append("          .on('mouseout', function(d, i) {\n");
		script.append("            tip.transition()\n");
		script.append("            .delay(100)\n");
		script.append("            .style('display', 'none');\n");		
		script.append("          })\n");		
		
		// show the differentially expressed genes
		script.append("    g.selectAll(\"scatter-dots\")\n");
		script.append("      .data(data)\n");
		script.append("      .enter().append(\"svg:circle\")\n");
		script.append("      .attr(\"cy\", function(d) {\n");
		script.append("        return y(d.y);\n");
		script.append("      })\n");
		script.append("      .attr(\"cx\", function(d, i) {\n");
		script.append("        return x(d.x);\n");
		script.append("      })\n");
		script.append("      .attr(\"r\", " + node_size + ")\n");
		script.append("      .style(\"fill\", function(d, i) { return d." + color_index_name + "})\n");
		//script.append("      .style(\"fill\", function(d, i) {if (Math.abs(d.x) > " + x_axis_cutoff + ") {if (d.x > 0) return \"red\"; if (d.x < 0) return \"red\"; } else {return \"lightgray\";}})\n");
		//script.append("      .style(\"fill\", function(d, i) {if (Math.abs(d.y) > " + y_axis_cutoff + ") {if (d.y > 0) return \"blue\"; if (d.y < 0) return \"blue\"; } else {return \"lightgray\";}})\n");
		//script.append("      .style(\"fill\", function(d, i) {if (Math.abs(d.y) > " + y_axis_cutoff + " && Math.abs(d.x) > " + x_axis_cutoff + ") {return \"purple\";} else if (Math.abs(d.x) > " + x_axis_cutoff + ") {return \"red\"} else if (Math.abs(d.y) > " + y_axis_cutoff + ") {return \"blue\"} else {return \"lightgray\";}})\n");
		
		// opacity
		//script.append("      .style(\"opacity\", function(d, i) {if (Math.abs(d.y) > " + logFC_cutoff + ") {if (d.y > 0) return \"1.0\"; if (d.y < 0) return 1.0; } else {return 0.0;}})\n");
		script.append("          .on('mouseover', function(d, i) {\n");
		script.append("            tip.transition().duration(0);\n");
		//script.append("	    tip.html(d.name + \"<br>Log2FC: \" + round(d.y,2) + \"<br>adj.pval:\" + d.fdr);\n");
		script.append("	    tip.html(d.name + \"<br>");
		for (int j = 0; j < meta_values.length; j++) {
			//script.append(" " + meta_title[j] + ":\"" + meta_values[j] + "\",\n");
			 script.append(meta_title[j] + ": \" + d." + meta_title[j] + " + \"<br>");
		}
		script.append("\");\n");
		
		//script.append("	    tip.html(d.name + \"<br>meta: \" + d.meta<br>);\n");
		script.append("            tip.style('top', (y(d.y) + 150) + 'px');\n");
		script.append("            tip.style('left', (x(d.x) + 50) + 'px');\n");
		script.append("            tip.style('display', 'block');\n");
		script.append("            tip.style(\"background\",'#BCC5F7');\n");
		script.append("          })\n");
		script.append("          .on('mouseout', function(d, i) {\n");
		script.append("            tip.transition()\n");
		script.append("            .delay(100)\n");
		script.append("            .style('display', 'none');\n");		
		script.append("          })\n");			
						
		// show the highlighted genes with circle
		script.append("        g.selectAll(\"scatter-dots\")\n");
		script.append("          .data(data)\n");
		script.append("          .enter().append(\"svg:circle\")\n");
		script.append("          .attr(\"cy\", function(d) {\n");
		script.append("            return y(d.y);\n");
		script.append("          })\n");
		script.append("          .attr(\"cx\", function(d, i) {\n");
		script.append("            return x(d.x);\n");
		script.append("          })\n");
		
		script.append("          .style(\"r\", function(d, i) {\n");			
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i].split(\" \")[0]) {return " + node_size + ";}\n");
		//script.append("            if (d.name == split_nodeName[i]) {return 6;}\n");
		script.append("          }\n");
		script.append("          return 0;})\n");
		
		script.append("          .style(\"fill\", function(d, i) {\n");
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i].split(\" \")[0]) {return split_nodeName[i].split(\" \")[1];}\n");
		script.append("          }\n");
		script.append("          })\n");
		
		script.append("          .style(\"stroke\", \"black\")\n");		
		script.append("          .style(\"stroke-width\", \"1.4\")\n");		
		
		script.append("          .style(\"opacity\", function(d, i) { return d." + color_opacity_name + ";})\n");
		/*script.append("          .style(\"opacity\", function(d, i) {\n");			
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i].split(\" \")[0]) {return 0.8;}\n");
		script.append("          }\n");
		script.append("          return 0.0;})\n");*/
		// opacity
		
		script.append("          .on('mouseover', function(d, i) {\n");
		script.append("            tip.transition().duration(0);\n");
		
		script.append("	    tip.html(d.name + \"<br>");
		for (int j = 0; j < meta_values.length; j++) {
			//script.append(" " + meta_title[j] + ":\"" + meta_values[j] + "\",\n");
			 script.append(meta_title[j] + ": \" + d." + meta_title[j] + " + \"<br>");
		}
		script.append("\");\n");
		
		//script.append("	    tip.html(d.name + \"<br>" + meta_title + ": \" + d.meta);\n");		
		script.append("            tip.style('top', (y(d.y) + 150) + 'px');\n");
		script.append("            tip.style('left', (x(d.x) + 50) + 'px');\n");
		script.append("            tip.style('display', 'block');\n");
		script.append("            tip.style(\"background\",'#BCC5F7');\n");
		script.append("          })\n");
		script.append("          .on('mouseout', function(d, i) {\n");
		script.append("            tip.transition()\n");
		script.append("            .delay(100)\n");
		script.append("            .style('display', 'none');\n");		
		script.append("          })\n");		

		
		
		if (writeNameFlag) {
			script.append("        g.selectAll(\"text\")\n");
			script.append("          .data(data)\n");
			script.append("          .enter()\n");
			script.append("          .append(\"text\")\n");
			script.append("          .text(function(d) {\n");
			script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
			script.append("            if (d.name == split_nodeName[i]) {return d.name;}\n");
			script.append("          }\n");
			script.append("            return \"\";\n");
			script.append("          })\n");
			script.append("          .attr(\"x\", function(d) {\n");
			script.append("            return x(d.x)-10;\n");
			script.append("          })\n");
			script.append("          .attr(\"y\", function(d) {\n");
			script.append("            return y(d.y)-10;  // Returns scaled circle y\n");
			script.append("          })\n");
			script.append("          .attr(\"font_family\", \"sans-serif\")\n");
			script.append("          .attr(\"font-size\", \"15px\")\n");
			script.append("          .style(\"opacity\", function(d, i) { return d." + color_opacity_name + ";})\n");
			/*script.append("          .style(\"opacity\", function(d, i) {\n");			
			script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
			script.append("            if (d.name == split_nodeName[i]) {return 1.0;}\n");
			script.append("          }\n");
			script.append("          return 0.0;})\n");*/
			//script.append("          .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.x) > " + logFC_cutoff + ") return 1.0; else return 0;})\n");
			script.append("          .attr(\"fill\", \"black\");   // Font color\n");
			
		}
		
		script.append("    } // end of renderImage\n");
		script.append("    var inital_node_names = \"" + inital_nodeNames + "\";\n");
		script.append("    renderImage(inital_node_names)\n");
		
		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(height + 50)+\")\")\n");
		script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"" + xaxis_title + "\");\n");

		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(0)+\")\")\n");
		script.append("            .style(\"font-size\", \"30px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"\");\n");
        
        script.append("     main.append(\"text\")\n");
        script.append("            .attr(\"text-anchor\", \"middle\")\n");
        script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .attr(\"transform\", \"translate(\"+ -50 +\",\"+(height/2)+\")rotate(-90)\")\n");
        script.append("            .text(\"" + yaxis_title + "\");\n");

        // user controls and input area
		script.append("    function downloadSVG() {\n");
		script.append("    	  const svg=document.querySelector('svg')\n");
		script.append("    	  const styles = window.getComputedStyle(svg)\n");
		script.append("    	  const d3svg=d3.select(svg)\n");
		script.append("    	  for(const s of styles) {\n");
		script.append("    	    d3svg.style(s,styles.getPropertyValue(s))\n");
		script.append("    	  }\n");

		script.append("    	  const a=document.createElement('a')\n");
		script.append("    	  document.body.appendChild(a)\n");
		script.append("    	  a.addEventListener('click',function(){\n");
		script.append("    	    const serializer = new XMLSerializer()\n");
		script.append("    	    const svg_blob = new Blob([serializer.serializeToString(svg)], {'type': \"image/svg+xml\"})\n");
		script.append("    	    a.download='chord.svg'\n");
		script.append("    	    a.href=URL.createObjectURL(svg_blob)\n");
		script.append("    	    document.body.removeChild(a)\n");
		script.append("    	  },false)\n");
		script.append("    	  a.click()\n");
		script.append("    	}\n");

		script.append("    	    var textDiv=d3.select(\"body\").append(\"div\")\n");
		script.append("    	    var controlsDiv=textDiv.append('div')\n");


		script.append("    	    var downloadBtn=controlsDiv.append('button')\n");
		script.append("    	      .html('Download SVG')\n");
		script.append("    	      .on('click',downloadSVG)\n");
		script.append("    	    var submitBtn=controlsDiv.append('button')\n");
		script.append("    	      .html('Update')\n");
		script.append("    	      .on('click',()=>{\n");
		script.append("    	        main.selectAll('g').remove()\n");
		script.append("    	        var line=textarea.property('value')\n");
		script.append("    	        renderImage(line)\n");      
		script.append("    	      })\n");
			    					     
		script.append("    	var textarea=textDiv.append('div')\n");
		script.append("    	  .style('display','inline-block')\n");
		script.append("    	  .style('padding','10px')\n");
		script.append("    	.append('textarea')\n");
		script.append("    	  .attr('cols',80)\n");
		script.append("    	  .attr('rows',20)\n");
        script.append("       .text(inital_node_names)\n");
		
        script.append("      function round(value, ndec){\n");
        script.append("        var n = 10;\n");
        script.append("        for(var i = 1; i < ndec; i++){\n");
        script.append("            n *=10;\n");
        script.append("        }\n");

        script.append("        if(!ndec || ndec <= 0)\n");
        script.append("            return Math.round(value);\n");
        script.append("        else\n");
        script.append("            return Math.round(value * n) / n;\n");
        script.append("      }\n");

        

		script.append("  </script>\n");
		
		script.append("</body>\n");

		script.append("</html>\n");


		return script.toString();
	}
	
	
}
