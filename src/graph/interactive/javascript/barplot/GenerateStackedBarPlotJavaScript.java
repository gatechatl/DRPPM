package graph.interactive.javascript.barplot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate a stacked barplot based on the matrix provided
 * @author tshaw
 *
 */
public class GenerateStackedBarPlotJavaScript {


	public static String description() {
		return "Generate html-javascript file with a stacked barplot based on the matrix provided";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[inputMatrix] [title html]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String caption = args[1];
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			String[] titles = new String[split_header.length - 1];
			for (int i = 1; i < split_header.length; i++) {
				titles[i - 1] = split_header[i].replaceAll("\\.", "_").replaceAll("-", "_");
			}
			LinkedList sample = new LinkedList();
			LinkedList data = new LinkedList();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample.add(split[0]);
				String[] values = new String[split_header.length - 1];
				for (int i = 1; i < split_header.length; i++) {
					values[i - 1] = split[i];
				}
				data.add(values);
			}
			in.close();
			
			int i = 0;
			String[] sampleNames = new String[sample.size()];
			Iterator itr = sample.iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				sampleNames[i] = name;
				i++;
			}
			System.out.println(generate_stacked_barplot(caption, titles, sampleNames, data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_stacked_barplot(String title, String[] header, String[] sampleName, LinkedList data) {
		StringBuffer script = new StringBuffer();
		script.append ("<!DOCTYPE html>\n");
		script.append ("<html lang=\"en\">\n");
		script.append ("<head>\n");
		script.append ("  <meta charset=\"UTF-8\" />\n");
		script.append ("  <title>d3.js learning</title>\n");
		script.append ("  <script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n");
		script.append ("  <style type=\"text/css\">\n");
		script.append ("  svg {\n");
		script.append ("    font: 10px sans-serif;\n");
		script.append ("    shape-rendering: crispEdges;\n");
		script.append ("  }\n");
		script.append ("\n");
		script.append ("  .axis path,\n");
		script.append ("  .axis line {\n");
		script.append ("    fill: none;\n");
		script.append ("    stroke: #000;\n");
		script.append ("  }\n");
		script.append (" \n");
		script.append ("  path.domain {\n");
		script.append ("    stroke: none;\n");
		script.append ("  }\n");
		script.append (" \n");
		script.append ("  .y .tick line {\n");
		script.append ("    stroke: #ddd;\n");
		script.append ("  }\n");
		script.append ("  </style>\n");
		script.append ("\n");
		script.append ("</head>\n");
		script.append ("<body>\n");
		script.append("" + title + "<br><br>\n");
		script.append ("<script type=\"text/javascript\">\n");
		script.append ("\n");
		script.append ("// Setup svg using Bostock's margin convention\n");
		script.append ("\n");
		script.append ("var margin = {top: 20, right: 160, bottom: 100, left: 30};\n");
		script.append ("\n");
		script.append ("var width = 960 - margin.left - margin.right,\n");
		script.append ("    height = 550 - margin.top - margin.bottom;\n");
		script.append ("\n");
		script.append ("var svg = d3.select(\"body\")\n");
		script.append ("  .append(\"svg\")\n");
		script.append ("  .attr(\"width\", width + margin.left + margin.right)\n");
		script.append ("  .attr(\"height\", height + margin.top + margin.bottom)\n");
		script.append ("  .append(\"g\")\n");
		script.append ("  .attr(\"transform\", \"translate(\" + margin.left + \",\" + margin.top + \")\");\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("/* Data in strings like it would be if imported from a csv */\n");
		script.append ("\n");
		script.append ("var data = [\n");
		
		for (int i = 0; i < data.size(); i++) {
			String line = "";
			String[] values = (String[])data.get(i);
			line += "{ name: \"" + sampleName[i] + "\"";
			for (int j = 0; j < header.length; j++) {
				line += ", " + header[j] + ": \"" + values[j] + "\"";
			}
			line += "},\n";
			script.append(line);
		}
		/*script.append ("  { year: \"2006\", redDelicious: \"10\", mcintosh: \"15\", oranges: \"9\", pears: \"6\" },\n");
		script.append ("  { year: \"2007\", redDelicious: \"12\", mcintosh: \"18\", oranges: \"9\", pears: \"4\" },\n");
		script.append ("  { year: \"2008\", redDelicious: \"05\", mcintosh: \"20\", oranges: \"8\", pears: \"2\" },\n");
		script.append ("  { year: \"2009\", redDelicious: \"01\", mcintosh: \"15\", oranges: \"5\", pears: \"4\" },\n");
		script.append ("  { year: \"2010\", redDelicious: \"02\", mcintosh: \"10\", oranges: \"4\", pears: \"2\" },\n");
		script.append ("  { year: \"2011\", redDelicious: \"03\", mcintosh: \"12\", oranges: \"6\", pears: \"3\" },\n");
		script.append ("  { year: \"2012\", redDelicious: \"04\", mcintosh: \"15\", oranges: \"8\", pears: \"1\" },\n");
		script.append ("  { year: \"2013\", redDelicious: \"06\", mcintosh: \"11\", oranges: \"9\", pears: \"4\" },\n");
		script.append ("  { year: \"2014\", redDelicious: \"10\", mcintosh: \"13\", oranges: \"9\", pears: \"5\" },\n");
		script.append ("  { year: \"2015\", redDelicious: \"16\", mcintosh: \"19\", oranges: \"6\", pears: \"9\" },\n");
		script.append ("  { year: \"2016\", redDelicious: \"19\", mcintosh: \"17\", oranges: \"5\", pears: \"7\" },\n");*/
		script.append ("];\n");
		script.append ("\n");
		//script.append ("var parse = d3.time.format(\"%Y\").parse;\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("// Transpose the data into layers\n");
		
		String names = "";
		for (int j = header.length - 1; j >= 0; j--) {
			if (j == header.length - 1) {
				names = "\"" + header[j] + "\"";
			} else {
				names += ", \"" + header[j] + "\"";
			}
		}
		script.append ("var dataset = d3.layout.stack()([" + names + "].map(function(names) {\n");
		script.append ("  return data.map(function(d) {\n");
		script.append ("    return {x: d.name, y: +d[names]};\n");
		script.append ("  });\n");
		script.append ("}));\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("// Set x, y and colors\n");
		script.append ("var x = d3.scale.ordinal()\n");
		script.append ("  .domain(dataset[0].map(function(d) { return d.x; }))\n");
		script.append ("  .rangeRoundBands([10, width-10], 0.02);\n");
		script.append ("\n");
		script.append ("var y = d3.scale.linear()\n");
		script.append ("  .domain([0, d3.max(dataset, function(d) {  return d3.max(d, function(d) { return d.y0 + d.y; });  })])\n");
		script.append ("  .range([height, 0]);\n");
		script.append ("\n");
		
		String[] colors= {"#fcdf99", "#ff0080", "#0080ff", "#80ff00", "#ff8000", "#ff0000", "#ffe7e5", "#c6c0c2", "#0cc375", "#fef65b", "#fa5618", "#3d85c6", "#f1c727", "#064152", "#d1ad57", "#ff6f61", "#527682", "#ffb2aa", "#b94f74"};
		String color = "";
		for (int j = 0; j < header.length; j++) {
			
			
			if (j == 0) {
				color = "\"" + colors[j - (header.length * (header.length / colors.length))] + "\"";
			} else {
				color += ", \"" + colors[j - (header.length * (header.length / colors.length))] + "\"";
			}
		}
		script.append ("var colors = [" + color + "];\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("// Define and draw axes\n");
		script.append ("var yAxis = d3.svg.axis()\n");
		script.append ("  .scale(y)\n");
		script.append ("  .orient(\"left\")\n");
		script.append ("  .ticks(5)\n");
		script.append ("  .tickSize(-width, 0, 0)\n");
		script.append ("  .tickFormat( function(d) { return d } );\n");
		script.append ("\n");
		script.append ("var xAxis = d3.svg.axis()\n");
		script.append ("  .scale(x)\n");
		script.append ("  .orient(\"bottom\")\n");
		script.append ("  .tickFormat( function(d) { return d } );\n");
		//script.append ("  .tickFormat(d3.time.format(\"%Y\"));\n");
		script.append ("\n");
		script.append ("svg.append(\"g\")\n");
		script.append ("  .attr(\"class\", \"y axis\")\n");
		script.append ("  .call(yAxis);\n");
		script.append ("\n");
		script.append ("svg.append(\"g\")\n");
		script.append ("  .attr(\"class\", \"x axis\")\n");
		script.append ("  .attr(\"transform\", \"translate(0,\" + (height) + \")\")\n");
		script.append ("  .call(xAxis)\n");
		script.append ("        .selectAll(\"text\")\n");	
		script.append ("            .style(\"text-anchor\", \"end\")\n");
		script.append ("            .attr(\"dx\", \"-.8em\")\n");
		script.append ("            .attr(\"dy\", \".15em\")\n");
		script.append ("            .attr(\"transform\", function(d) {\n");
		script.append ("                return \"rotate(-65)\" \n");
		script.append ("                });\n");

		/*
		script.append ("     svg.append(\"text\")\n");
		script.append ("            .attr(\"transform\", \"translate(\"+ (width * 3 / 4) +\",\"+(50)+\")\")\n");
		script.append ("            .style(\"font-size\", \"20px\")\n");
		script.append ("            .style(\"text-anchor\",\"end\")\n"); 
		script.append ("            .attr(\"startOffset\",\"100%\")\n");		
		script.append ("            .text(\"" + title + "\");\n");
        */
		
		script.append ("\n");
		script.append ("\n");
		script.append ("// Create groups for each series, rects for each segment \n");
		script.append ("var groups = svg.selectAll(\"g.cost\")\n");
		script.append ("  .data(dataset)\n");
		script.append ("  .enter().append(\"g\")\n");
		script.append ("  .attr(\"class\", \"cost\")\n");
		script.append ("  .style(\"fill\", function(d, i) { return colors[i]; });\n");
		script.append ("\n");
		script.append ("var rect = groups.selectAll(\"rect\")\n");
		script.append ("  .data(function(d) { return d; })\n");
		script.append ("  .enter()\n");
		script.append ("  .append(\"rect\")\n");
		script.append ("  .attr(\"x\", function(d) { return x(d.x); })\n");
		script.append ("  .attr(\"y\", function(d) { return y(d.y0 + d.y); })\n");
		script.append ("  .attr(\"height\", function(d) { return y(d.y0) - y(d.y0 + d.y); })\n");
		script.append ("  .attr(\"width\", x.rangeBand())\n");
		script.append ("  .on(\"mouseover\", function() { tooltip.style(\"display\", null); })\n");
		script.append ("  .on(\"mouseout\", function() { tooltip.style(\"display\", \"none\"); })\n");
		script.append ("  .on(\"mousemove\", function(d) {\n");
		script.append ("    var xPosition = d3.mouse(this)[0] - 15;\n");
		script.append ("    var yPosition = d3.mouse(this)[1] - 25;\n");
		script.append ("    tooltip.attr(\"transform\", \"translate(\" + xPosition + \",\" + yPosition + \")\");\n");
		script.append ("    tooltip.select(\"text\").text(d.y);\n");
		script.append ("  });\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("// Draw legend\n");
		script.append ("var legend = svg.selectAll(\".legend\")\n");
		script.append ("  .data(colors)\n");
		script.append ("  .enter().append(\"g\")\n");
		script.append ("  .attr(\"class\", \"legend\")\n");
		script.append ("  .attr(\"transform\", function(d, i) { return \"translate(30,\" + i * 19 + \")\"; });\n");
		script.append (" \n");
		script.append ("legend.append(\"rect\")\n");
		script.append ("  .attr(\"x\", width - 18)\n");
		script.append ("  .attr(\"width\", 18)\n");
		script.append ("  .attr(\"height\", 18)\n");
		script.append ("  .style(\"fill\", function(d, i) {return colors.slice().reverse()[i];});\n");
		script.append (" \n");
		script.append ("legend.append(\"text\")\n");
		script.append ("  .attr(\"x\", width + 5)\n");
		script.append ("  .attr(\"y\", 9)\n");
		script.append ("  .attr(\"dy\", \".35em\")\n");
		script.append ("  .style(\"text-anchor\", \"start\")\n");
		script.append ("  .text(function(d, i) { \n");
		script.append ("    switch (i) {\n");
		for (int j = 0; j < header.length; j++) {
			script.append("     case " + j + ": return \"" + header[j] + "\";\n");			
		}
		/*script.append ("      case 0: return \"Anjou pears\";\n");
		script.append ("      case 1: return \"Naval oranges\";\n");
		script.append ("      case 2: return \"McIntosh apples\";\n");
		script.append ("      case 3: return \"Red Delicious apples\";\n");*/
		script.append ("    }\n");
		script.append ("  });\n");
		script.append ("\n");
		script.append ("\n");
		script.append ("// Prep the tooltip bits, initial display is hidden\n");
		script.append ("var tooltip = svg.append(\"g\")\n");
		script.append ("  .attr(\"class\", \"tooltip\")\n");
		script.append ("  .style(\"display\", \"none\");\n");
		script.append ("    \n");
		script.append ("tooltip.append(\"rect\")\n");
		script.append ("  .attr(\"width\", 30)\n");
		script.append ("  .attr(\"height\", 20)\n");
		script.append ("  .attr(\"fill\", \"white\")\n");
		script.append ("  .style(\"opacity\", 0.5);\n");
		script.append ("\n");
		script.append ("tooltip.append(\"text\")\n");
		script.append ("  .attr(\"x\", 15)\n");
		script.append ("  .attr(\"dy\", \"1.2em\")\n");
		script.append ("  .style(\"text-anchor\", \"middle\")\n");
		script.append ("  .attr(\"font-size\", \"12px\")\n");
		script.append ("  .attr(\"font-weight\", \"bold\");\n");
		script.append ("\n");
		script.append ("</script>\n");
		script.append ("</body>\n");
		script.append ("</html>\n");
		script.append ("\n");


		return script.toString();

	}
}
