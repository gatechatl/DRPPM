package graph.interactive.javascript.volcanoplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class GenerateVolcanoPlotJavaScriptUserInputPathways {

	public static String description() {
		return "Generate html-javascript file for displaying Volcano Plot";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [name_index] [pval_index] [logFC_index] [logFC_cutoff] [fdr_index] [fdr cutoff] [avg_expr_index] [SkipHeaderFlag:true/false] [writeNameFlag:true/false] [SignificantPathwayList]";
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
			double min_pval = Double.MAX_VALUE;
			double max_pval = Double.MIN_VALUE;
			
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
				
				if (min_logFC > logFC_value) {
					min_logFC = logFC_value;					
				}
				if (max_logFC < logFC_value) {
					max_logFC = logFC_value;					
				}
				if (min_pval > pval_value) {
					min_pval = pval_value;					
				}
				if (max_pval < pval_value) {
					max_pval = pval_value;					
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
			
			min_pval = min_pval - Math.abs(max_pval - min_pval) * 0.1;
			max_pval = max_pval + Math.abs(max_pval - min_pval) * 0.1;
			
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
			
			System.out.println(generate_scatterplot_javascript(names, logFC, pval, min_logFC, max_logFC, min_pval, max_pval, fdr, avg_expr, fdr_cutoff, logFC_cutoff, writeNameFlag, description, gene_list));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_scatterplot_javascript(String[] names, double[] x, double[] y, double min_x, double max_x, double min_y, double max_y, double[] fdr, double[] avg_expr, double fdr_cutoff, double logFC_cutoff, boolean writeNameFlag, String description, HashMap gene_list) {
		
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
		script.append("  <script>\n");
		    
		script.append("    var data = [\n");
		for (int i = 0; i < names.length - 1; i++) {				
			script.append("      {\n");
			script.append("        fdr: " + fdr[i] + ",\n");
			script.append("        avg_expr: " + avg_expr[i] + ",\n");
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

		
		// start the function for the image rendering
		script.append("    function renderImage(nodeName) {\n");
		
		script.append("    main.append('g')\n");
	    script.append("      .style(\"font-size\",\"20px\")\n");
		script.append("      .attr('transform', 'translate(0,0)')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(yAxis);\n");

		script.append("        var split_nodeName = nodeName.split(\"\\n\");\n");
		
		
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
		script.append("          .style(\"opacity\", function(d, i) {\n");			
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i]) {return 0.0;}\n");
		script.append("          }\n");
		script.append("          return 0.2;})\n");
		
		//script.append("      .style(\"fill\", \"red\")\n");
		script.append("          .style(\"fill\", \"lightgray\")\n");
		
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(0);\n");
		script.append("	tip.html(d.name + \"<br>AvgExpr: \" + round(d.avg_expr,2) + \"<br>log2FC:\" + round(d.x,2));\n");
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
		
		
		
		// show the differentiated genes
		
		
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
		script.append("      .style(\"opacity\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && Math.abs(d.x) > " + logFC_cutoff + ") {if (d.x > 0) return \"0.3\"; if (d.x < 0) return 0.3; } else {return 0.0;}})\n");		
		//script.append("      .style(\"fill\", \"red\")\n");
		script.append("      .style(\"fill\", function(d, i) {if (d.fdr < " + fdr_cutoff + " && d.x > " + logFC_cutoff + ") {return \"red\";} else if (d.fdr < " + fdr_cutoff + " && d.x < " + logFC_cutoff + ") {return \"blue\";} else {return \"lightgray\";}})\n");
		
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(0);\n");
		script.append("	tip.html(d.name + \"<br>AvgExpr: \" + round(d.avg_expr,2) + \"<br>log2FC:\" + round(d.x,2));\n");
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

		// show the highlighted genes				
		script.append("    g.selectAll(\"scatter-dots\")\n");
		script.append("      .data(data)\n");
		script.append("      .enter().append(\"svg:circle\")\n");
		script.append("      .attr(\"cy\", function(d) {\n");
		script.append("        return y(d.y);\n");
		script.append("      })\n");
		script.append("      .attr(\"cx\", function(d, i) {\n");
		script.append("        return x(d.x);\n");
		script.append("      })\n");
		
		script.append("          .style(\"r\", function(d, i) {\n");			
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i]) {return 6;}\n");
		script.append("          }\n");
		script.append("          return 0;})\n");
		
		script.append("          .style(\"opacity\", function(d, i) {\n");			
		script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
		script.append("            if (d.name == split_nodeName[i]) {return 0.8;}\n");
		script.append("          }\n");
		script.append("          return 0.0;})\n");
		// opacity
		
		//script.append("      .style(\"fill\", \"red\")\n");
		script.append("          .style(\"fill\", \"purple\")\n");
		script.append("          .style(\"stroke\", \"black\")\n");		
		script.append("          .style(\"stroke-width\", \"1.4\")\n");		
				
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(0);\n");
		script.append("	tip.html(d.name + \"<br>AvgExpr: \" + round(d.avg_expr,2) + \"<br>log2FC:\" + round(d.x,2));\n");
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
		
		
		if (writeNameFlag) {
			script.append("    g.selectAll(\"text\")\n");
			script.append("      .data(data)\n");
			script.append("      .enter()\n");
			script.append("      .append(\"text\")\n");
			script.append("          .text(function(d) {\n");
			script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
			script.append("            if (d.name == split_nodeName[i]) {return d.name;}\n");
			script.append("          }\n");
			script.append("            return \"\";\n");
			script.append("          })\n");
			script.append("      .attr(\"x\", function(d) {\n");
			script.append("        return x(d.x) - 10;\n");
			script.append("      })\n");
			script.append("      .attr(\"y\", function(d) {\n");
			script.append("        return y(d.y) - 10;  // Returns scaled circle y\n");
			script.append("      })\n");
			script.append("      .attr(\"font_family\", \"sans-serif\")\n");
			script.append("      .attr(\"font-size\", \"15px\")\n");
			script.append("          .style(\"opacity\", function(d, i) {\n");			
			script.append("          for (i = 0; i < split_nodeName.length; i++) {\n");
			script.append("            if (d.name == split_nodeName[i]) {return 0.8;}\n");
			script.append("          }\n");
			script.append("          return 0.0;})\n");
			script.append("      .attr(\"fill\", \"black\");   // Font color\n");

		}		
		
		// end of renderImage
		script.append("    } // end of renderImage\n");
		script.append("    var inital_node_names = \"" + inital_nodeNames + "\";\n");
		script.append("    renderImage(inital_node_names)\n");
		
		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(height + 50)+\")\")\n");
		script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"Log2FoldChange\");\n");

		script.append("     main.append(\"text\")\n");
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(0)+\")\")\n");
		script.append("            .style(\"font-size\", \"30px\")\n");
        script.append("            .style(\"text-anchor\",\"end\")\n"); 
        script.append("            .attr(\"startOffset\",\"100%\")\n");		
        script.append("            .text(\"Volcano Plot\");\n");
        
        script.append("     main.append(\"text\")\n");
        script.append("            .attr(\"text-anchor\", \"middle\")\n");
        script.append("            .style(\"font-size\", \"20px\")\n");
        script.append("            .attr(\"transform\", \"translate(\"+ -50 +\",\"+(height/2)+\")rotate(-90)\")\n");
        script.append("            .text(\"Log10(Pvalue)\");\n");
        
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
