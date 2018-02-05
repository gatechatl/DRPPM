package graph.interactive.javascript;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class GenerateScatterPlotJavaScript {

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
				double x_value = new Double(split[x_index]);
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
				
				split[x_index] = new Double(new Double(new Double(new Double(split[x_index]) * 10000).intValue()) / 10000).toString();
				split[y_index] = new Double(new Double(new Double(new Double(split[y_index]) * 10000).intValue()) / 10000).toString();
				x_list.add(split[x_index]);
				y_list.add(split[y_index]);
				name_list.add(split[name_index]);
			}
			in.close();
			min_x = min_x - Math.abs(max_x - min_x) * 0.1;
			max_x = max_x + Math.abs(max_x - min_x) * 0.1;
			
			min_y = min_y - Math.abs(max_y - min_y) * 0.1;
			max_y = max_y + Math.abs(max_y - min_y) * 0.1;
			
			double[] x = MathTools.convertListStr2Double(x_list);
			double[] y = MathTools.convertListStr2Double(y_list);
			String[] names = new String[name_list.size()];
			int index = 0;
			Iterator itr = name_list.iterator();
			while (itr.hasNext()) {
				names[index] = (String)itr.next();
				index++;
			}
			
			System.out.println(generate_scatterplot_javascript(names, x, y, min_x, max_x, min_y, max_y));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_scatterplot_javascript(String[] names, double[] x, double[] y, double min_x, double max_x, double min_y, double max_y) {
		String script = "";
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<!DOCTYPE html>\n");
		buffer.append("<html>\n");

		buffer.append("<head>\n");
		buffer.append("  <script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n");
		buffer.append("</head>\n");

		buffer.append("<body>\n");
		buffer.append("  <script>\n");
		    
		buffer.append("    var data = [\n");
		for (int i = 0; i < names.length - 1; i++) {				
			buffer.append("      {\n");
			buffer.append("        x: " + x[i] + ",\n");
			buffer.append("        y: " + y[i] + ",\n");
			buffer.append("	name:\"" + names[i] + "\"\n");
			buffer.append("      },\n");
		}
		buffer.append("      {\n");
		buffer.append("        x: " + x[names.length - 1] + ",\n");
		buffer.append("        y: " + y[names.length - 1] + ",\n");
		buffer.append("	name:\"" + names[names.length - 1] + "\"\n");
		buffer.append("      },\n");
		buffer.append("    ];\n");
		
		     // size and margins for the chart
		buffer.append("    var margin = {\n");
		buffer.append("      top: 20,\n");
		buffer.append("      right: 15,\n");
		buffer.append("      bottom: 60,\n");
		buffer.append("      left: 60\n");
		buffer.append("    }, width = 800 - margin.left - margin.right,\n");
		buffer.append("      height = 600 - margin.top - margin.bottom;\n");

		buffer.append("    var x = d3.scale.linear()\n");
		buffer.append("      .domain([" + min_x + ", " + max_x + "])\n");
		buffer.append("      .range([0, width]);\n");

		buffer.append("    var y = d3.scale.linear()\n");
		buffer.append("      .domain([" + min_y + ", " + max_y + "])\n");
		buffer.append("      .range([height, 0]);\n");

		buffer.append("    var tip = d3.select('body')\n");
		buffer.append("      .append('div')\n");
		buffer.append("      .attr('class', 'tip')\n");
		buffer.append("      .style('border', '1px solid steelblue')\n");
		buffer.append("      .style('padding', '5px')\n");
		buffer.append("      .style('position', 'absolute')\n");
		buffer.append("      .style('display', 'none')\n");
		buffer.append("      .on('mouseover', function(d, i) {\n");
		buffer.append("        tip.transition().duration(1);\n");
		buffer.append("      })\n");
		buffer.append("      .on('mouseout', function(d, i) {\n");
		buffer.append("        tip.style('display', 'none');\n");
		buffer.append("      });\n");

		buffer.append("    var chart = d3.select('body')\n");
		buffer.append("      .append('svg')\n");
		buffer.append("      .attr('width', width + margin.right + margin.left)\n");
		buffer.append("      .attr('height', height + margin.top + margin.bottom)\n");
		buffer.append("      .attr('class', 'chart')\n");

		buffer.append("     var main = chart.append('g')\n");
		buffer.append("      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')\n");
		buffer.append("      .attr('width', width)\n");
		buffer.append("      .attr('height', height)\n");
		buffer.append("      .attr('class', 'main')\n");

		buffer.append("     var xAxis = d3.svg.axis()\n");
		buffer.append("      .scale(x)\n");
		buffer.append("      .orient('bottom');\n");

		buffer.append("    main.append('g')\n");
		buffer.append("      .attr('transform', 'translate(0,' + height + ')')\n");
		buffer.append("      .attr('class', 'main axis date')\n");
		buffer.append("      .call(xAxis);\n");

		buffer.append("    var yAxis = d3.svg.axis()\n");
		buffer.append("      .scale(y)\n");
		buffer.append("      .orient('left');\n");

		buffer.append("    main.append('g')\n");
		buffer.append("      .attr('transform', 'translate(0,0)')\n");
		buffer.append("      .attr('class', 'main axis date')\n");
		buffer.append("      .call(yAxis);\n");

		buffer.append("    var g = main.append(\"svg:g\");\n");

		buffer.append("    g.selectAll(\"scatter-dots\")\n");
		buffer.append("      .data(data)\n");
		buffer.append("      .enter().append(\"svg:circle\")\n");
		buffer.append("      .attr(\"cy\", function(d) {\n");
		buffer.append("        return y(d.y);\n");
		buffer.append("      })\n");
		buffer.append("      .attr(\"cx\", function(d, i) {\n");
		buffer.append("        return x(d.x);\n");
		buffer.append("      })\n");
		buffer.append("      .attr(\"r\", 5)\n");
		buffer.append("      .style(\"opacity\", 1.0)\n");
		buffer.append("      .style(\"fill\", \"red\")\n");
		buffer.append("      .on('mouseover', function(d, i) {\n");
		buffer.append("        tip.transition().duration(0);\n");
		buffer.append("	tip.html(d.name);\n");
		buffer.append("        tip.style('top', y(d.y) + 'px');\n");
		buffer.append("        tip.style('left', x(d.x) + 'px');\n");
		buffer.append("        tip.style('display', 'block');\n");
		buffer.append("        tip.style(\"background\",'#BCC5F7');\n");
		buffer.append("      })\n");
		buffer.append("      .on('mouseout', function(d, i) {\n");
		buffer.append("        tip.transition()\n");
		buffer.append("        .delay(100)\n");
		buffer.append("        .style('display', 'none');\n");
		buffer.append("      })\n");
		buffer.append("  </script>\n");
		buffer.append("</body>\n");

		buffer.append("</html>\n");
		script = buffer.toString();
		return script;
	}
}
