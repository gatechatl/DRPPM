package graph.interactive.javascript.barplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

public class GenerateVerticalBarPlotJavaScript {

	public static String description() {
		return "Generate html-javascript file for displaying scatterplot. The input matrix must contain a header containing the sampleNames";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [geneName] [expr_indices] [group_color] [title] [xaxis_label] [yaxis_label]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneName = args[1];
			String expr_indices = args[2];			
			//int text_index = new Integer(args[3]); // tag that will show up near the end
			//int name_index = new Integer(args[4]); // name of bar plot		
			//int highlight_index = new Integer(args[5]);
			String color_group = args[3];
			String title = args[4];
			String xaxis_label = args[5];
			String yaxis_label = args[6];			
			//boolean skipHeader = new Boolean(args[6]);
			
			double min_x = Double.MAX_VALUE;
			double max_x = Double.MIN_VALUE;
			//double min_y = Double.MAX_VALUE;
			//double max_y = Double.MIN_VALUE;
			String[] split_group_expr_indices = expr_indices.split(";");
			String[] split_group_color = color_group.split(";");
			LinkedList x_list = new LinkedList();
			LinkedList text_list = new LinkedList();
			LinkedList name_list = new LinkedList();
			LinkedList highlight_list = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = "";			
			header = in.readLine();
			String[] split_header = header.split("\t");
			int total_indices = 0;
			for (String group_expr_indices: split_group_expr_indices) {
				String[] split_sample_indices = group_expr_indices.split(",");
				for (String str_indices: split_sample_indices) {
					total_indices++;
				}
			}
			String[] labels = new String[total_indices];
			int index = 0;
			for (String group_expr_indices: split_group_expr_indices) {
				String[] split_sample_indices = group_expr_indices.split(",");
				for (String str_indices: split_sample_indices) {
					int i = new Integer(str_indices);
					labels[index] = split_header[i];
					index++;
				}
			}

			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(geneName)) {
					index = 0;
					double[] x = new double[total_indices];
					String[] colors = new String[total_indices];
					int group_id = 0;
					for (String group_expr_indices: split_group_expr_indices) {
						String[] split_sample_indices = group_expr_indices.split(",");
						for (String str_indices: split_sample_indices) {
							int indices = new Integer(str_indices);
							double x_value = new Double(split[indices]);
							x_value = Math.round(x_value * 100D) / 100D;
							if (min_x > x_value) {
								min_x = x_value;					
							}
							if (max_x < x_value) {
								max_x = x_value;				 	
							}
							x[index] = x_value;
							colors[index] = split_group_color[group_id];
							index++;
						}
						 
						group_id++;
					}
					// generate plot for this
					String[] names = labels;			
					String[] text = new String[names.length];
					for (int i = 0; i < names.length; i++) {
						text[i] = labels[i] + ":" + x[i];
					}
					String[] highlight = text;
					System.out.println(generate_vertical_barplot_javascript(names, x, colors, text, highlight, title, xaxis_label, yaxis_label));
					break;
				}
				
																
				//text_list.add(split[text_index]);
				//name_list.add(split[name_index]);
				//highlight_list.add(split[highlight_index]);
			}
			in.close();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_vertical_barplot_javascript(String[] labels, double[] values, String[] colors, String[] text, String[] highlight, String title, String xaxis_label, String yaxis_label) {
		String script = "";
		script += "<html>\n";
		script += "<head>\n";
		script += "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.12/angular.min.js\"></script>\n"; 
		script += "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script>\n"; 
		script += "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/d3/4.3.0/d3.min.js\"></script>\n";
		script += "</head>\n"; 

		script += "<body ng-app=\"myApp\" ng-controller=\"myCtrl\">\n"; 

		script += "    <svg></svg>\n";

		script += "    <script>\n";
		
		script += "    var margin = {\n";
		script += "      top: 50,\n";
		script += "      right: 100,\n";
		script += "      bottom: 100,\n";
		script += "      left: 100\n";		
		script += "    }, width = 1200 - margin.left - margin.right,\n";
		script += "      height = 1000 - margin.top - margin.bottom;\n";		

		        //module declaration 
		script += "        var app = angular.module('myApp',[]);\n";

		        //Controller declaration
		script += "        app.controller('myCtrl',function($scope){\n";

		script += "            $scope.svgWidth = 1000;//svg Width\n";
		script += "            $scope.svgHeight = 600;//svg Height\n"; 

		            //Data in proper format 
		

		script += "    data = [\n";
		for (int i = 0; i < labels.length - 1; i++) {
			script += "        {label:\"" + labels[i] + "\", value:" + values[i] + ", text: \"" + text[i] + "\", highlight: \"" + highlight[i] + "\", color: \"" + colors[i] + "\"},\n";
		}
		script += "        {label:\"" + labels[labels.length - 1] + "\", value:" + values[values.length - 1]  + ", text: \"" + text[text.length - 1] + "\", highlight: \"" + highlight[highlight.length - 1] + "\", color: \"" + colors[colors.length - 1] + "\"}\n";
		script += "    ];\n";

		/*script += "            var data = [\n";
		                  {"letter": "A","frequency": "5.01"},
		                  {"letter": "B","frequency": "7.80"},
		                  {"letter": "C","frequency": "15.35"},
		                  {"letter": "D","frequency": "22.70"},
		                  {"letter": "E","frequency": "34.25"},
		                  {"letter": "F","frequency": "10.21"},
		                  {"letter": "G","frequency": "7.68"},
		script += "            ];\n";
		*/
		
		                //removing prior svg elements ie clean up svg 
		script += "                d3.select('svg').selectAll(\"*\").remove();\n";

		                //resetting svg height and width in current svg 
		script += "                d3.select(\"svg\").attr(\"width\", $scope.svgWidth).attr(\"height\", $scope.svgHeight);\n";

		                //Setting up of our svg with proper calculations 
		script += "                var svg = d3.select(\"svg\");\n";
		
		
		script += "                var margin = {top: 100, right: 50, bottom: 100, left: 100};\n";
		script += "                var width = svg.attr(\"width\") - margin.left - margin.right;\n";
		script += "                var height = svg.attr(\"height\") - margin.top - margin.bottom;\n";

		                //Plotting our base area in svg in which chart will be shown 
		script += "                var g = svg.append(\"g\").attr(\"transform\", \"translate(\" + margin.left + \",\" + margin.top + \")\");\n";

		                //X and Y scaling 
		script += "                var x = d3.scaleBand().rangeRound([0, width]).padding(0.4);\n";
		script += "                var y = d3.scaleLinear().rangeRound([height, 0]);\n";

		script += "                x.domain(data.map(function(d) { return d.label; }));\n";
		script += "                y.domain([0, d3.max(data, function(d) { return +d.value; })]);\n";

		                //Final Plotting 

		                //for x axis 
		script += "                g.append(\"g\")\n";
		script += "                    .call(d3.axisBottom(x))\n";
		script += "                    .style(\"font-size\", \"15px\")\n";
		script += "                    .attr(\"transform\", \"translate(0,\" + height + \")\");\n";

		                //for y axis 
		script += "                g.append(\"g\")\n";
		script += "                    .call(d3.axisLeft(y))\n";
		script += "                    .style(\"font-size\", \"15px\")\n";
		script += "                    .append(\"text\").attr(\"transform\", \"rotate(-90)\").attr(\"text-anchor\", \"end\");\n";

		                  //for rectangles 
		script += "                  g.selectAll(\".bar\")\n";
		script += "                    .data(data)\n";
		script += "                    .enter()\n";
		script += "                    .append(\"rect\")\n";
		script += "                    .attr(\"class\", \"bar\")\n";
		script += "                    .attr(\"x\", function(d) { return x(d.label); })\n";
		script += "                    .attr(\"y\", function(d) { return y(d.value); })\n";
		script += "		    .style(\"opacity\", 0.7)\n";
		script += "		    .style(\"fill\", function(d) { return d.color;})\n";
		script += "                    .attr(\"width\", x.bandwidth())\n";
		script += "                    .attr(\"height\", function(d) { return height - y(d.value); });\n";

		script += "               g.selectAll(\".bar\")\n";
		script += "                .append(\"text\")\n";
		script += "                .attr(\"dy\", \".75em\")\n";
		script += "                .attr(\"y\", function(d) { return y(d.value); })\n";
		script += "                .attr(\"x\", function(d) { return x(d.label); })\n";
		script += "                .attr(\"text-anchor\", \"middle\")\n";
		script += "                .text(function(d) { return y(d.label); });\n";

		script += "	       g.selectAll(\".text\")\n";
		script += "                .data(data)\n";
		script += "                .enter()\n";
		script += "                .append(\"text\")\n";
		script += "                .attr(\"dy\", \"-.75em\")\n";
		script += "                .attr(\"y\", function(d) { return y(d.value); })\n";
		script += "                .attr(\"dx\", \"1.5em\")\n";
		script += "		.attr(\"x\", function(d) { return x(d.label); })\n";
		script += "                .attr(\"text-anchor\", \"middle\")\n";
		script += "                .text(function(d) { return d.value; });\n";

		
		script += "     svg.append(\"text\")\n";
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script += "            .attr(\"transform\", \"translate(\"+ (width * 2 / 3) +\",\"+(height + 150)+\")\")\n";
		script += "            .style(\"font-size\", \"20px\")\n";
        script += "            .style(\"text-anchor\",\"end\")\n"; 
        script += "            .attr(\"startOffset\",\"100%\")\n";		
        script += "            .text(\"" + xaxis_label + "\");\n";

		script += "     svg.append(\"text\")\n";
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script += "            .attr(\"transform\", \"translate(\"+ (width * 3 / 4) +\",\"+(50)+\")\")\n";
		script += "            .style(\"font-size\", \"30px\")\n";
        script += "            .style(\"text-anchor\",\"end\")\n"; 
        script += "            .attr(\"startOffset\",\"100%\")\n";		
        script += "            .text(\"" + title + "\");\n";
        
        script += "     svg.append(\"text\")\n";
        script += "            .attr(\"text-anchor\", \"middle\")\n";
        script += "            .style(\"font-size\", \"20px\")\n";
        script += "            .attr(\"transform\", \"translate(\"+ 30 +\",\"+(height * 3/4)+\")rotate(-90)\")\n";
        script += "            .text(\"" + yaxis_label + "\");\n";
        
		script += "        });\n";

		script += "    </script>\n"; 

		script += "</body>\n"; 

		script += "</html>\n"; 
		
		return script;
	
	}

}
