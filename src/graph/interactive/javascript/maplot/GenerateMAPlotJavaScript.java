package graph.interactive.javascript.maplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

public class GenerateMAPlotJavaScript {

	public static String description() {
		return "Generate html-javascript file for displaying MA Plot";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [name_index] [pval_index] [logFC_index] [logFC_cutoff] [fdr_index] [fdr cutoff] [avg_expr_index] [SkipHeaderFlag:true/false] [writeNameFlag:true/false";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int name_index = new Integer(args[1]);
			int pval_index = new Integer(args[2]);
			int logFC_index = new Integer(args[3]);			
			double logFC_cutoff = new Double(args[4]);			
			int fdr_index = new Integer(args[5]);
			double fdr_cutoff = new Double(args[6]);
			int avg_expr_index = new Integer(args[7]);
			boolean skipHeader = new Boolean(args[8]);
			boolean writeNameFlag = new Boolean(args[9]);
			HashMap gene_list = new HashMap();
			if (args.length > 10) {
				String geneListFile = args[10];
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
			double min_logFC = Double.MAX_VALUE;
			double max_logFC = Double.MIN_VALUE;
			double min_expr = Double.MAX_VALUE;
			double max_expr = Double.MIN_VALUE;
			
			LinkedList logFC_list = new LinkedList();
			LinkedList pval_list = new LinkedList();
			LinkedList fdr_list = new LinkedList();
			LinkedList name_list = new LinkedList();
			LinkedList avg_expr_list = new LinkedList();
			
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
				double pval_value = -Math.log10(new Double(split[pval_index]));
				double logFC_value = new Double(split[logFC_index]);
				double fdr_value = new Double(split[fdr_index]);
				double avg_expr_value = new Double(split[avg_expr_index]);
				if (min_logFC > logFC_value) {
					min_logFC = logFC_value;					
				}
				if (max_logFC < logFC_value) {
					max_logFC = logFC_value;					
				}
				if (min_expr > avg_expr_value) {
					min_expr = avg_expr_value;					
				}
				if (max_expr < avg_expr_value) {
					max_expr = avg_expr_value;					
				}
				
				pval_list.add(pval_value + "");
				logFC_list.add(split[logFC_index]);
				fdr_list.add(split[fdr_index]);
				name_list.add(split[name_index]);
				avg_expr_list.add(split[avg_expr_index]);
				
			}
			in.close();
			min_logFC = min_logFC - Math.abs(max_logFC - min_logFC) * 0.1;
			max_logFC = max_logFC + Math.abs(max_logFC - min_logFC) * 0.1;
			
			min_expr = min_expr - Math.abs(max_expr - min_expr) * 0.1;
			max_expr = max_expr + Math.abs(max_expr - min_expr) * 0.1;
			
			double[] pval = MathTools.convertListStr2Double(pval_list);
			double[] logFC = MathTools.convertListStr2Double(logFC_list);
			double[] fdr = MathTools.convertListStr2Double(fdr_list);
			double[] avg_expr = MathTools.convertListStr2Double(avg_expr_list);
			String[] names = new String[name_list.size()];
			int index = 0;
			Iterator itr = name_list.iterator();
			while (itr.hasNext()) {
				names[index] = (String)itr.next();
				index++;
			}
			File f = new File(inputFile);
			
			String description = "File was generated based on: " + f.getName() + "<br>";
			description += "Each node represent a gene. Colored node represent gene that pass FDR < 0.05 and log2FC > 1.<br>";
			System.out.println(generate_scatterplot_javascript(names, avg_expr, logFC, min_expr, max_expr, min_logFC, max_logFC, fdr, avg_expr, fdr_cutoff, logFC_cutoff, writeNameFlag, description, gene_list));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_scatterplot_javascript(String[] names, double[] x, double[] y, double min_x, double max_x, double min_y, double max_y, double[] fdr, double[] avg_expr, double fdr_cutoff, double logFC_cutoff, boolean writeNameFlag, String description, HashMap gene_list) {
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
			script.append("        fdr: " + fdr[i] + ",\n");
			script.append("        avg_expr: " + avg_expr[i] + ",\n");
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
			script.append("	name:\"" + names[i] + "\"\n");
			script.append("      },\n");
		}
		script.append("      {\n");
		script.append("        fdr: " + fdr[names.length - 1] + ",\n");
		script.append("        avg_expr: " + avg_expr[names.length - 1] + ",\n");
		script.append("        x: " + x[names.length - 1] + ",\n");
		script.append("        y: " + y[names.length - 1] + ",\n");
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

		script.append("     main.append('g')\n");
	    script.append("      .style(\"font-size\",\"20px\")\n");		
		script.append("      .attr('transform', 'translate(0,' + height + ')')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(xAxis);\n");

		script.append("    var yAxis = d3.svg.axis()\n");
		script.append("      .scale(y)\n");
		script.append("      .orient('left');\n");

		script.append("    main.append('g')\n");
	    script.append("      .style(\"font-size\",\"20px\")\n");
		script.append("      .attr('transform', 'translate(0,0)')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(yAxis);\n");

		
		script.append("    var g = main.append(\"svg:g\");\n");
		
		// show the undifferentiated genes
		script.append("    g.selectAll(\"scatter-dots\")\n");
		script.append("      .data(data)\n");
		script.append("      .enter().append(\"svg:circle\")\n");
		script.append("      .attr(\"cy\", function(d) {\n");
		script.append("        return y(d.y);\n");
		script.append("      })\n");
		script.append("      .attr(\"cx\", function(d, i) {\n");
		script.append("        return x(d.x);\n");
		script.append("      })\n");
		script.append("      .attr(\"r\", 5)\n");
		//script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {return 0.0;} else {return 0.7;}})\n");
		if (gene_list.size() > 0) {
			script.append("      .style(\"opacity\", function(d, i) {\n");
			script.append("if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {\n");
			Iterator itr = gene_list.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (index == 0) {
					script.append("    if (d.name ==  \"" + geneName + "\") {return 0.0;}\n");
				} else {
					script.append("    else if (d.name ==  \"" + geneName + "\") {return 0.0;}\n");				
				}				
				index++;
				//script.append("if (d.name ==  \"" + geneName + "\" && d.y > 0) {return \"red\";} else if (d.name ==  \"" + geneName + "\" && d.y < 0) {return \"blue\";} else {return \"lightgray\";}\n");
			}
			script.append(" else {return 0.7;}\n");
			script.append("     } else {return 0.7;}})\n");
			script.append("\n");
		} else {
			script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {if (d.y > 0) return \"red\"; if (d.y < 0) return 0.0; } else {return 0.7;}})\n");
		}
		
		//script.append("      .style(\"fill\", \"red\")\n");
		if (gene_list.size() > 0) {
			script.append("      .style(\"fill\", \"lightgray\")\n");
			script.append("\n");
		} else {
			script.append("      .style(\"fill\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {if (d.y > 0) return \"red\"; if (d.y < 0) return \"blue\"; } else {return \"lightgray\";}})\n");
		}
		
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
		script.append("      .attr(\"r\", 5)\n");
		//script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {return 0.7;} else {return 0.0;}})\n");		
		//script.append("      .style(\"fill\", \"red\")\n");
		if (gene_list.size() > 0) {
			script.append("      .style(\"fill\", function(d, i) {\n");
			script.append("if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {\n");
			Iterator itr = gene_list.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (index == 0) {
					script.append("    if (d.name ==  \"" + geneName + "\") {return \"red\";}\n");
				} else {
					script.append("    else if (d.name ==  \"" + geneName + "\") {return \"red\";}\n");				
				}				
				index++;
				//script.append("if (d.name ==  \"" + geneName + "\" && d.y > 0) {return \"red\";} else if (d.name ==  \"" + geneName + "\" && d.y < 0) {return \"blue\";} else {return \"lightgray\";}\n");
			}
			script.append(" else {return \"lightgray\";}\n");
			script.append("     } else {return \"lightgray\";}})\n");
			script.append("\n");
		} else {
			script.append("      .style(\"fill\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {if (d.y > 0) return \"red\"; if (d.y < 0) return \"blue\"; } else {return \"lightgray\";}})\n");
		}
		
		// opacity
		if (gene_list.size() > 0) {
			script.append("      .style(\"opacity\", function(d, i) {\n");
			script.append("if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {\n");
			Iterator itr = gene_list.keySet().iterator();
			int index = 0;
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (index == 0) {
					script.append("    if (d.name ==  \"" + geneName + "\") {return 0.7;}\n");
				} else {
					script.append("    else if (d.name ==  \"" + geneName + "\") {return 0.7;}\n");				
				}				
				index++;
				//script.append("if (d.name ==  \"" + geneName + "\" && d.y > 0) {return \"red\";} else if (d.name ==  \"" + geneName + "\" && d.y < 0) {return \"blue\";} else {return \"lightgray\";}\n");
			}
			script.append(" else {return 0.0;}\n");
			script.append("     } else {return 0.0;}})\n");
			script.append("\n");
		} else {
			script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.y) > " + logFC_cutoff + ") {if (d.y > 0) return \"red\"; if (d.y < 0) return 0.7; } else {return 0.0;}})\n");
		}
		
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(0);\n");
		script.append("	tip.html(d.name + \"<br>Log2FC: \" + round(d.y,2) + \"<br>adj.pval:\" + d.fdr);\n");		
		script.append("        tip.style('top', (y(d.y) - 40) + 'px');\n");
		script.append("        tip.style('left', (x(d.x)) + 'px');\n");
		script.append("        tip.style('display', 'block');\n");
		script.append("        tip.style(\"background\",'#BCC5F7');\n");
		script.append("      })\n");
		script.append("      .on('mouseout', function(d, i) {\n");
		script.append("        tip.transition()\n");
		script.append("        .delay(100)\n");
		script.append("        .style('display', 'none');\n");
		
		script.append("      })\n");		
		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(height + 50)+\")\")\n");
		script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"EXPRESSION (Log2)\");\n");

		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(0)+\")\")\n");
		script.append("            .style(\"font-size\", \"30px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"MA Plot\");\n");
        
        script.append("     main.append(\"text\")\n");
        script.append("            .attr(\"text-anchor\", \"middle\")\n");
        script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .attr(\"transform\", \"translate(\"+ -50 +\",\"+(height/2)+\")rotate(-90)\")\n");
        script.append("            .text(\"Fold Change (Log2)\");\n");
        
		if (writeNameFlag) {
			script.append("    g.selectAll(\"text\")\n");
			script.append("      .data(data)\n");
			script.append("      .enter()\n");
			script.append("      .append(\"text\")\n");
			script.append("      .text(function(d) {\n");
			script.append("        return d.name;\n");
			script.append("      })\n");
			script.append("      .attr(\"x\", function(d) {\n");
			script.append("        return x(d.x);\n");
			script.append("      })\n");
			script.append("      .attr(\"y\", function(d) {\n");
			script.append("        return y(d.y);  // Returns scaled circle y\n");
			script.append("      })\n");
			script.append("      .attr(\"font_family\", \"sans-serif\")\n");
			script.append("      .attr(\"font-size\", \"8px\")\n");
			script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.x) > " + logFC_cutoff + ") return 1.0; else return 0;})\n");
			script.append("      .attr(\"fill\", \"black\");   // Font color\n");

		}
        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(" + min_x + "))\n");
	    script.append("    .attr(\"x2\", x(" + max_x + "))\n");
		script.append("    .attr(\"y1\", y(0))\n");
		script.append("    .attr(\"y2\", y(0))\n");
		script.append("    .attr(\"stroke-width\", 5)\n");
		script.append("    .style(\"opacity\",0.4)\n");
		script.append("    .style(\"stroke\", \"black\");\n");

        script.append("    main.append('line')\n");
		script.append("    .attr(\"x1\", x(0))\n");
	    script.append("    .attr(\"x2\", x(0))\n");
		script.append("    .attr(\"y1\", y(" + min_y + "))\n");
		script.append("    .attr(\"y2\", y(" + max_y + "))\n");
		script.append("    .attr(\"stroke-width\", 5)\n");
		script.append("    .style(\"opacity\",0.4)\n"); 
		script.append("    .style(\"stroke\", \"black\");\n");
		
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
