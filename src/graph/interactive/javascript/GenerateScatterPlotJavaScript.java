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
		script += "<!DOCTYPE html>\n";
		script += "<html>\n";

		script += "<head>\n";
		script += "  <script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n";
		script += "</head>\n";

		script += "<body>\n";
		script += "  <script>\n";
		    
		script += "    var data = [\n";
		for (int i = 0; i < names.length - 1; i++) {				
			script += "      {\n";
			script += "        x: " + x[i] + ",\n";
			script += "        y: " + y[i] + ",\n";
			script += "	name:\"" + names[i] + "\"\n";
			script += "      },\n";
		}
		script += "      {\n";
		script += "        x: " + x[names.length - 1] + ",\n";
		script += "        y: " + y[names.length - 1] + ",\n";
		script += "	name:\"" + names[names.length - 1] + "\"\n";
		script += "      },\n";
		script += "    ];\n";
		
		     // size and margins for the chart
		script += "    var margin = {\n";
		script += "      top: 20,\n";
		script += "      right: 15,\n";
		script += "      bottom: 60,\n";
		script += "      left: 60\n";
		script += "    }, width = 800 - margin.left - margin.right,\n";
		script += "      height = 600 - margin.top - margin.bottom;\n";

		script += "    var x = d3.scale.linear()\n";
		script += "      .domain([" + min_x + ", " + max_x + "])\n";
		script += "      .range([0, width]);\n";

		script += "    var y = d3.scale.linear()\n";
		script += "      .domain([" + min_y + ", " + max_y + "])\n";
		script += "      .range([height, 0]);\n";

		script += "    var tip = d3.select('body')\n";
		script += "      .append('div')\n";
		script += "      .attr('class', 'tip')\n";
		script += "      .style('border', '1px solid steelblue')\n";
		script += "      .style('padding', '5px')\n";
		script += "      .style('position', 'absolute')\n";
		script += "      .style('display', 'none')\n";
		script += "      .on('mouseover', function(d, i) {\n";
		script += "        tip.transition().duration(1);\n";	
		script += "      })\n";
		script += "      .on('mouseout', function(d, i) {\n";
		script += "        tip.style('display', 'none');\n";
		script += "      });\n";

		script += "    var chart = d3.select('body')\n";
		script += "      .append('svg')\n";
		script += "      .attr('width', width + margin.right + margin.left)\n";
		script += "      .attr('height', height + margin.top + margin.bottom)\n";
		script += "      .attr('class', 'chart')\n";

		script += "     var main = chart.append('g')\n";
		script += "      .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')')\n";
		script += "      .attr('width', width)\n";
		script += "      .attr('height', height)\n";
		script += "      .attr('class', 'main')\n";

		script += "     var xAxis = d3.svg.axis()\n";
		script += "      .scale(x)\n";
		script += "      .orient('bottom');\n";

		script += "    main.append('g')\n";
		script += "      .attr('transform', 'translate(0,' + height + ')')\n";
		script += "      .attr('class', 'main axis date')\n";
		script += "      .call(xAxis);\n";

		script += "    var yAxis = d3.svg.axis()\n";
		script += "      .scale(y)\n";
		script += "      .orient('left');\n";

		script += "    main.append('g')\n";
		script += "      .attr('transform', 'translate(0,0)')\n";
		script += "      .attr('class', 'main axis date')\n";
		script += "      .call(yAxis);\n";

		script += "    var g = main.append(\"svg:g\");\n";

		script += "    g.selectAll(\"scatter-dots\")\n";
		script += "      .data(data)\n";
		script += "      .enter().append(\"svg:circle\")\n";
		script += "      .attr(\"cy\", function(d) {\n";
		script += "        return y(d.y);\n";
		script += "      })\n";
		script += "      .attr(\"cx\", function(d, i) {\n";
		script += "        return x(d.x);\n";
		script += "      })\n";
		script += "      .attr(\"r\", 5)\n";
		script += "      .style(\"opacity\", 1.0)\n";
		script += "      .style(\"fill\", \"red\")\n";
		script += "      .on('mouseover', function(d, i) {\n";
		script += "        tip.transition().duration(0);\n";
		script += "	tip.html(d.name);\n";
		script += "        tip.style('top', y(d.y) + 'px');\n";
		script += "        tip.style('left', x(d.x) + 'px');\n";
		script += "        tip.style('display', 'block');\n";
		script += "        tip.style(\"background\",'#BCC5F7');\n";
		script += "      })\n";
		script += "      .on('mouseout', function(d, i) {\n";
		script += "        tip.transition()\n";
		script += "        .delay(100)\n";
		script += "        .style('display', 'none');\n";
		script += "      })\n";
		script += "  </script>\n";
		script += "</body>\n";

		script += "</html>\n";
		return script;
	}
}
