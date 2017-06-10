package graph.interactive.javascript;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

public class GenerateFoldchangeGeneLengthPlot {

	public static String description() {
		return "Generate html-javascript file for displaying scatterplot";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [x_index] [y_index] [name_index] [SkipHeaderFlag:true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int x_index = new Integer(args[1]);
			int y_index = new Integer(args[2]);
			int name_index = new Integer(args[3]);			
			boolean skipHeader = new Boolean(args[4]);
			
			double min_x = Double.MAX_VALUE;
			double max_x = Double.MIN_VALUE;
			double min_y = Double.MAX_VALUE;
			double max_y = Double.MIN_VALUE;
			
			LinkedList x_list = new LinkedList();
			LinkedList y_list = new LinkedList();
			LinkedList orig_x_list = new LinkedList();
			LinkedList name_list = new LinkedList();
			
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
				
				double x_value = Math.log10(new Double(split[x_index]));
				double y_value = new Double(split[y_index]);
				
				
				if (min_x > x_value) {
					min_x = x_value;					
				}
				if (max_x < x_value) {
					max_x = x_value;					
				}
				if (min_y > y_value) {
					min_y = y_value;					
				}
				if (max_y < y_value) {
					max_y = y_value;					
				}
				
				
				orig_x_list.add(Math.round(new Double(split[x_index]) * 100) / 100 + "");
				x_list.add(x_value + "");
				y_list.add(y_value + "");
				name_list.add(split[name_index]);
			}
			in.close();
			min_x = min_x - Math.abs(max_x - min_x) * 0.1;
			max_x = max_x + Math.abs(max_x - min_x) * 0.1;
			
			min_y = min_y - Math.abs(max_y - min_y) * 0.1;
			max_y = max_y + Math.abs(max_y - min_y) * 0.1;
			
			double[] x = MathTools.convertListStr2Double(x_list);
			double[] y = MathTools.convertListStr2Double(y_list);
			double[] orig_x = MathTools.convertListStr2Double(orig_x_list);
			String[] names = new String[name_list.size()];
			int index = 0;
			Iterator itr = name_list.iterator();
			while (itr.hasNext()) {
				names[index] = (String)itr.next();
				index++;
			}
			
			System.out.println(generate_scatterplot_javascript(names, x, y, orig_x, min_x, max_x, min_y, max_y));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_scatterplot_javascript(String[] names, double[] x, double[] y, double[] orig_x, double min_x, double max_x, double min_y, double max_y) {
		StringBuffer script = new StringBuffer();
		//String script = "";
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
			script.append("        x: " + x[i] + ",\n");
			script.append("        y: " + y[i] + ",\n");
			script.append("        orig_x: " + orig_x[i] + ",\n");
			script.append("	name:\"" + names[i] + "\"\n");
			script.append("      },\n");
		}
		script.append("      {\n");
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

		script.append("    main.append('g')\n");
		script.append("      .attr('transform', 'translate(0,' + height + ')')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(xAxis);\n");

		script.append("    var yAxis = d3.svg.axis()\n");
		script.append("      .scale(y)\n");
		script.append("      .orient('left');\n");

		script.append("    main.append('g')\n");
		script.append("      .attr('transform', 'translate(0,0)')\n");
		script.append("      .attr('class', 'main axis date')\n");
		script.append("      .call(yAxis);\n");

		script.append("    var g = main.append(\"svg:g\");\n");

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
		script.append("      .style(\"opacity\", 1.0)\n");
		script.append("      .style(\"fill\", \"black\")\n");
		script.append("      .on('mouseover', function(d, i) {\n");
		script.append("        tip.transition().duration(0);\n");
		script.append("	tip.html(d.name + \",GeneLength=\" + d.orig_x + \",FoldChange=\" + d.y);\n");
		script.append("        tip.style('top', y(d.y) + 'px');\n");
		script.append("        tip.style('left', x(d.x) + 'px');\n");
		script.append("        tip.style('display', 'block');\n");
		script.append("        tip.style(\"background\",'#BCC5F7');\n");
		script.append("      })\n");
		script.append("      .on('mouseout', function(d, i) {\n");
		script.append("        tip.transition()\n");
		script.append("        .delay(100)\n");
		script.append("        .style('display', 'none');\n");
		script.append("      })\n");
		
		script.append("     main.append(\"text\")\n");
		//script.append("            .attr(\"text-anchor\", \"middle\")\n");
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(height + 50)+\")\")\n");
		script.append("            .style(\"font-size\", \"20px\")\n");
		script.append("            .style(\"text-anchor\",\"end\")\n"); 
		script.append("            .attr(\"startOffset\",\"100%\")\n");		
		script.append("            .text(\"Transcript Length (Log10)\");\n");

		script.append("     main.append(\"text\")\n");
		//script.append("            .attr(\"text-anchor\", \"middle\")\n");
		script.append("            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(0)+\")\")\n");
		script.append("            .style(\"font-size\", \"30px\")\n");
		script.append("            .style(\"text-anchor\",\"end\")\n"); 
		script.append("            .attr(\"startOffset\",\"100%\")\n");		
		script.append("            .text(\"Transcript Length vs Fold Change (Log2)\");\n");
        
		script.append("     main.append(\"text\")\n");
		script.append("            .attr(\"text-anchor\", \"middle\")\n");
		script.append("            .style(\"font-size\", \"20px\")\n");
		script.append("            .attr(\"transform\", \"translate(\"+ -50 +\",\"+(height/2)+\")rotate(-90)\")\n");
		script.append("            .text(\"Fold Change (Log2)\");\n");
        
		script.append("  </script>\n");
		script.append("</body>\n");

		script.append("</html>\n");
		
		
		
		
		return script.toString();
		
	}
}
