package graph.interactive.javascript.barplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

/**
 * Generate a horizontal barplot. Template is based on Juan Cruz-Benito's work.
 * LICENSE# The MIT License (MIT) Copyright (c) 2015 Juan Cruz-Benito. http://juancb.es Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 * @author tshaw
 *
 */
public class GenerateHorizontalBarPlotJavaScript {

	public static String description() {
		return "Generate html-javascript file for displaying scatterplot";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [x_index] [text_index] [name_index] [metaInfo_index] [title] [xaxis_label] [SkipHeaderFlag:true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int x_index = new Integer(args[1]); // value
			int text_index = new Integer(args[2]); // tag that will show up near the end
			int name_index = new Integer(args[3]); // name of bar plot		
			int highlight_index = new Integer(args[4]);
			String title = args[5];
			String xaxis_label = args[6];
			boolean skipHeader = new Boolean(args[7]);
			
			double min_x = Double.MAX_VALUE;
			double max_x = Double.MIN_VALUE;
			//double min_y = Double.MAX_VALUE;
			//double max_y = Double.MIN_VALUE;
			
			LinkedList x_list = new LinkedList();
			LinkedList text_list = new LinkedList();
			LinkedList name_list = new LinkedList();
			LinkedList highlight_list = new LinkedList();
			
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
				//double y_value = new Double(split[y_index]);
				
				x_list.add(split[x_index]);
				if (min_x > x_value) {
					min_x = x_value;					
				}
				if (max_x < x_value) {
					max_x = x_value;				 	
				}
				//if (min_y > y_value) {
				//	min_y = y_value;					
				//}
				//if (max_y < y_value) {
				//	max_y = y_value;					
				//}
				
				text_list.add(split[text_index]);
				name_list.add(split[name_index]);
				highlight_list.add(split[highlight_index]);
			}
			in.close();
			min_x = min_x - Math.abs(max_x - min_x) * 0.1;
			max_x = max_x + Math.abs(max_x - min_x) * 0.1;
			
			//min_y = min_y - Math.abs(max_y - min_y) * 0.1;
			//max_y = max_y + Math.abs(max_y - min_y) * 0.1;
			
			double[] x = MathTools.convertListStr2Double(x_list);
			//double[] y = MathTools.convertListStr2Double(y_list);
			String[] names = new String[name_list.size()];			
			int index = 0;
			Iterator itr = name_list.iterator();
			while (itr.hasNext()) {
				names[index] = (String)itr.next();							
				index++;
			}
			
			index = 0;
			String[] text = new String[text_list.size()];
			itr = text_list.iterator();
			while (itr.hasNext()) {
				text[index] = (String)itr.next();							
				index++;
			}
			
			index = 0;
			String[] highlight = new String[highlight_list.size()];
			itr = highlight_list.iterator();
			while (itr.hasNext()) {
				highlight[index] = (String)itr.next();							
				index++;
			}
			System.out.println(generate_horizontal_barplot_javascript(names, x, text, highlight, title, xaxis_label));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	public static String generate_horizontal_barplot_javascript(String[] labels, double[] values, String[] text, String[] highlight, String title, String xaxis_label) {
		String script = "";
		script += "<!DOCTYPE html>\n";
		script += "<meta charset=\"utf-8\">\n";
		script += "<style>\n";
		script += "    body {\n";
		script += "        font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n";
		script += "        width: 960px;\n";
		int size = 300;
		size = size + labels.length * 30;
		script += "        height: " + size + "px;\n";
		
		script += "        position: relative;\n";
		script += "    }\n";

		script += "    svg {\n";
		script += "        width: 100%;\n";
		script += "        height: 100%;\n";
		script += "        position: center;\n";
		script += "    }\n";

		script += "    .toolTip {\n";
		script += "        font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n";
		script += "        position: absolute;\n";
		script += "        display: none;\n";
		script += "        width: 400;\n";
		script += "        height: auto;\n";
		script += "        background: none repeat scroll 0 0 white;\n";
		script += "        border: 0 none;\n";
		script += "        border-radius: 8px 8px 8px 8px;\n";
		script += "        box-shadow: -3px 3px 15px #888888;\n";
		script += "        color: black;\n";
		script += "        font: 12px sans-serif;\n";
		script += "        padding: 5px;\n";
		script += "        text-align: center;\n";
		script += "    }\n";

		script += "    text {\n";
		script += "        font: 10px sans-serif;\n";
		script += "        color: white;\n";
		script += "    }\n";
		script += "    text.value {\n";
		script += "        font-size: 120%;\n";
		script += "        fill: white;\n";
		script += "    }\n";

		script += "    .axisHorizontal path{\n";
		script += "        fill: none;\n";
		script += "    }\n";

		script += "    .axisHorizontal .tick line {\n";
		script += "        stroke-width: 1;\n";
		script += "        stroke: rgba(0, 0, 0, 0.2);\n";
		script += "    }\n";

		script += "    .bar {\n";
		script += "        fill: steelblue;\n";
		script += "        fill-opacity: .9;\n";
		script += "    }\n";

		script += "</style>\n";
		script += "<body>\n";
		script += "<header>" + title + "</header>\n";
		script += "<script src=\"http://d3js.org/d3.v3.min.js\"></script>\n";
		script += "<script>\n";

		script += "    data = [\n";
		for (int i = 0; i < labels.length - 1; i++) {
			script += "        {label:\"" + labels[i] + "\", value:" + values[i] + ", text: \"" + text[i] + "\", highlight: \"" + highlight[i] + "\"},\n";
		}
		script += "        {label:\"" + labels[labels.length - 1] + "\", value:" + values[values.length - 1]  + ", text: \"" + text[text.length - 1] + "\", highlight: \"" + highlight[highlight.length - 1] + "\"}\n";
		script += "    ];\n";


		script += "    var div = d3.select(\"body\").append(\"div\").attr(\"class\", \"toolTip\");\n";

		script += "    var axisMargin = 20,\n";
		script += "            margin = 20,\n";
		script += "            padding = 0,\n";
		script += "            valueMargin = 4,\n";
		script += "            width = parseInt(d3.select('body').style('width'), 10),\n";
		script += "            height = parseInt(d3.select('body').style('height'), 10),\n";
		script += "            barHeight = (height-axisMargin-margin*2)* 0.4/data.length,\n";
		script += "            barPadding = (height-axisMargin-margin*2)*0.6/data.length,\n";
		script += "            data, bar, svg, scale, xAxis, labelWidth = 0;\n";

		script += "    max = d3.max(data, function(d) { return d.value; });\n";

		script += "    svg = d3.select('body')\n";
		script += "            .append(\"svg\")\n";
		script += "            .attr(\"width\", width)\n";
		script += "            .attr(\"height\", height);\n";


		script += "    bar = svg.selectAll(\"g\")\n";
		script += "            .data(data)\n";
		script += "            .enter()\n";
		script += "            .append(\"g\");\n";

		script += "    bar.attr(\"class\", \"bar\")\n";
		script += "            .attr(\"cy\",0)\n";
		script += "            .style('fill', 'darkgreen')\n";
		script += "            .attr(\"transform\", function(d, i) {\n";
		script += "                return \"translate(\" + margin + \",\" + (i * (barHeight + barPadding) + barPadding) + \")\";\n";
		script += "            });\n";

		script += "    bar.append(\"text\")\n";
		script += "            .attr(\"class\", \"label\")\n";
		script += "            .attr(\"x\", barHeight / 2)\n";
		script += "            .attr(\"dx\", \".35em\") //vertical align middle\n";
		script += "            .attr(\"dy\", \"1.0em\") //vertical align middle\n";
		script += "            .text(function(d){\n";
		script += "                if (d.label.length > 50) { \n";
		script += "                    return d.label.substring(0, 50);\n";
		script += "                } else {\n";
		script += "                    return d.label;}\n";
		script += "            }).each(function() {\n";
		script += "        labelWidth = Math.ceil(Math.max(labelWidth, this.getBBox().width));\n";
		script += "    });\n";

		script += "    scale = d3.scale.linear()\n";
		script += "            .domain([0, max])\n";
		script += "            .range([0, width - margin*2 - labelWidth]);\n";

		script += "    xAxis = d3.svg.axis()\n";
		script += "            .scale(scale)\n";
		script += "            .tickSize(-height + 2*margin + axisMargin)\n";
		script += "            .orient(\"bottom\");\n";

		script += "    bar.append(\"rect\")\n";
		script += "            .attr(\"transform\", \"translate(\"+labelWidth+\", 0)\")\n";
		script += "            .attr(\"height\", barHeight)\n";
		script += "            .attr(\"width\", function(d){\n";
		script += "                return scale(d.value);\n";
		script += "            });\n";

		script += "svg.append(\"text\")\n";
		//script += "            .attr(\"text-anchor\", \"middle\")\n";
		script += "            .attr(\"transform\", \"translate(\"+ (width * 3 / 5) +\",\"+(height-(padding/3))+\")\")\n";
		script += "            .style(\"font-size\", \"20px\")\n";
        script += "            .style(\"text-anchor\",\"end\")\n"; 
        script += "            .attr(\"startOffset\",\"100%\")\n";		
        script += "            .text(\"" + xaxis_label + "\");\n";
        
        
		/*
		 * 
    bar.append("rect")

            .attr("transform", function(d){ return "translate(" + (scale(d.value) + labelWidth)+ ", 0)";})
            .attr("height", barHeight)
	    .attr("dx", -valueMargin + labelWidth) //margin right
	    .style("fill","red")
            .attr("width", function(d){
                return scale(d.value);
            });

		 */
		
		script += "    bar.append(\"text\")\n";
		script += "            .attr(\"class\", \"value\")\n";
		script += "            .attr(\"y\", barHeight / 2)\n";
		script += "            .attr(\"dx\", -valueMargin + labelWidth) //margin right\n";
		script += "            .attr(\"dy\", \".35em\") //vertical align middle\n";
		script += "            .attr(\"text-anchor\", \"end\")\n";
		script += "            .style(\"font-size\", \"12px\")\n";
		script += "            .style(\"font-weight\", \"bold\")\n";
		script += "            .style('fill', 'lightgray')\n";
        script += "            .style(\"text-anchor\",\"end\")\n"; 
        script += "            .attr(\"startOffset\",\"100%\")\n";
		script += "            .text(function(d){\n";
		script += "                return (d.text);\n";
		script += "            })\n";
		script += "            .attr(\"x\", function(d){\n";
		script += "                var width = this.getBBox().width;\n";
		script += "                return Math.max(width + valueMargin, scale(d.value));\n";
		script += "            });\n";

		script += "    bar\n";
		script += "            .on(\"mousemove\", function(d){\n";
		script += "                div.style(\"left\", d3.event.pageX+10+\"px\");\n";
		script += "                div.style(\"top\", d3.event.pageY-25+\"px\");\n";
		script += "                div.style(\"display\", \"inline-block\");\n";	
		script += "                var split = d.highlight.split(\",\");\n";
		script += "                var new_highlight = \"\";\n";
		script += "                var count = 0;\n";
		script += "                for (var i = 0; i < split.length; i++) {\n";
		script += "                    count = count + 1;\n";
		script += "                    new_highlight += split[i] + \",\";\n";
		script += "                    if (count % 10 == 0 ) {new_highlight += \"<br>\"};\n";
		script += "                }\n";
		script += "                div.html((d.label)+\"<br>\"+(d.value)+\"<br>\"+(new_highlight));\n";
		script += "            });\n";
		script += "    bar\n";
		script += "            .on(\"mouseout\", function(d){\n";
		script += "                div.style(\"display\", \"none\");\n";
		script += "            });\n";

		script += "    svg.insert(\"g\",\":first-child\")\n";
		script += "            .attr(\"class\", \"axisHorizontal\")\n";
		script += "            .attr(\"transform\", \"translate(\" + (margin + labelWidth) + \",\"+ (height - axisMargin - margin)+\")\")\n";
		script += "            .call(xAxis);\n";

		script += "</script>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "<br>\n";
		script += "</body>\n";
		return script;

	}
	
	
}
