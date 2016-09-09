package NetworkTools.jung;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class Graph_Algos {
	static int edgeCount_Directed = 0;	// This works with the inner MyEdge class
	class MyNode 
	{
	  //static int edgeCount = 0;	// This works with the inner MyEdge class
	  String id;
	  public MyNode(String id) 
	  {
	  this.id = id;
	  }
	  public String toString() 
	  {	 
	  return "V"+id;  
	  }	
	}
	
	class MyLink 
	{
		double weight;
		int id;
		
		public MyLink(double weight) 
		{
		 this.id = edgeCount_Directed++;
		 this.weight = weight;
		} 
		
		public String toString() 
		{
		 return "E"+id;
		}
	}
	//used to construct graph and call graph algorithm used in JUNG
	public HashMap Closeness_Centrality_Score(LinkedList<String> Distinct_nodes, LinkedList<String> source_vertex, LinkedList<String> target_vertex, LinkedList<Double> Edge_Weight)
	{
	//CREATING weighted directed graph
			  Graph<MyNode, MyLink> g = new DirectedSparseGraph<Graph_Algos.MyNode, Graph_Algos.MyLink>();
			  //create node objects
			  Hashtable<String, MyNode> Graph_Nodes = new Hashtable<String, Graph_Algos.MyNode>();
			  LinkedList<MyNode> Source_Node = new LinkedList<Graph_Algos.MyNode>();
			  LinkedList<MyNode> Target_Node = new LinkedList<Graph_Algos.MyNode>();
			  LinkedList<MyNode> Graph_Nodes_Only = new LinkedList<Graph_Algos.MyNode>();
			  //LinkedList<MyLink> Graph_Links = new LinkedList<Graph_Algos.MyLink>();
			  //create graph nodes
			  for(int i=0;i<Distinct_nodes.size();i++)
			  {
			  String node_name = Distinct_nodes.get(i);
			  MyNode data = new MyNode(node_name);
			  Graph_Nodes.put(node_name, data);
			  Graph_Nodes_Only.add(data);
			  }
			  //Now convert all source and target nodes into objects
			  for(int t=0;t<source_vertex.size();t++)
			  {
			  Source_Node.add(Graph_Nodes.get(source_vertex.get(t)));
			  Target_Node.add(Graph_Nodes.get(target_vertex.get(t)));
			  }
			  //Now add nodes and edges to the graph
			  for(int i=0;i<Edge_Weight.size();i++)
			  {
			  g.addEdge(new MyLink(Edge_Weight.get(i)),Source_Node.get(i), Target_Node.get(i), EdgeType.DIRECTED);
			  }
			  Transformer<MyLink, Double> wtTransformer = new Transformer<MyLink,Double>() 
			  { 
						 public Double transform(MyLink link) 
						 { 
						 return link.weight;  
						 }
				  };
			  edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality<MyNode,MyLink> CC1 = new edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality<MyNode, MyLink>(g,wtTransformer);
			  //Calculating Closeness Centrality score of nodes
			  HashMap map = new HashMap();
			  for(int i=0;i<Graph_Nodes_Only.size();i++)
			  {
			  //System.out.println("Graph Node "+Graph_Nodes_Only.get(i)+" Closeness Centrality" +CC1.getVertexScore(Graph_Nodes_Only.get(i)));
				  String node = Graph_Nodes_Only.get(i) + "";
				  node = node.substring(1, node.length());
				  double val = CC1.getVertexScore(Graph_Nodes_Only.get(i));
				  if (new Double(val).isNaN()){
					  val = 0.00001;
				  }
				  map.put(node, val);
			  }
		  return map;
	}
	
	public static void main(String[] args) 
	{
	
		//let the nodes of graph are: {A, B, C, D, E, F, G}
		//Directed edges are: {AB=0.7, BC=0.9, CD=0.57, DB=1.0, CA=1.3, AD=0.3, DF=0.2, DE=0.8, EG=0.4, FE=0.6, GF=0.2}
		
		 Graph_Algos GA1 = new  Graph_Algos();
		
		LinkedList<String> Distinct_Vertex = new LinkedList<String>();
		LinkedList<String> Source_Vertex = new LinkedList<String>();
		LinkedList<String> Target_Vertex = new LinkedList<String>();
		LinkedList<Double> Edge_Weight = new LinkedList<Double>();
		
		//add the distinct vertexes
		Distinct_Vertex.add("A");
		Distinct_Vertex.add("B");
		Distinct_Vertex.add("C");
		Distinct_Vertex.add("D");
		Distinct_Vertex.add("E");
		Distinct_Vertex.add("F");
		Distinct_Vertex.add("G");

		Source_Vertex.add("A"); Target_Vertex.add("B"); Edge_Weight.add(10.0);
		Source_Vertex.add("A"); Target_Vertex.add("C"); Edge_Weight.add(1.0);
		Source_Vertex.add("C"); Target_Vertex.add("D"); Edge_Weight.add(1.0);		
		Source_Vertex.add("D"); Target_Vertex.add("A"); Edge_Weight.add(1.0);
		Source_Vertex.add("A"); Target_Vertex.add("E"); Edge_Weight.add(1.0);
		Source_Vertex.add("A"); Target_Vertex.add("F"); Edge_Weight.add(1.0);
		Source_Vertex.add("A"); Target_Vertex.add("G"); Edge_Weight.add(1.0);
		
		/*Source_Vertex.add("F"); Target_Vertex.add("A"); Edge_Weight.add(1.0);
		Source_Vertex.add("F"); Target_Vertex.add("B"); Edge_Weight.add(1.0);
		Source_Vertex.add("E"); Target_Vertex.add("A"); Edge_Weight.add(1.0);
		Source_Vertex.add("A"); Target_Vertex.add("B"); Edge_Weight.add(1.0);
		Source_Vertex.add("B"); Target_Vertex.add("C"); Edge_Weight.add(1.0);
		Source_Vertex.add("C"); Target_Vertex.add("D"); Edge_Weight.add(1.0);
		Source_Vertex.add("D"); Target_Vertex.add("B"); Edge_Weight.add(1.0);
		Source_Vertex.add("C"); Target_Vertex.add("A"); Edge_Weight.add(1.0);
		Source_Vertex.add("A"); Target_Vertex.add("D"); Edge_Weight.add(1.0);
		Source_Vertex.add("D"); Target_Vertex.add("F"); Edge_Weight.add(1.0);
		Source_Vertex.add("D"); Target_Vertex.add("E"); Edge_Weight.add(0.8);
		Source_Vertex.add("E"); Target_Vertex.add("G"); Edge_Weight.add(1.0);
		Source_Vertex.add("F"); Target_Vertex.add("E"); Edge_Weight.add(1.0);
		Source_Vertex.add("G"); Target_Vertex.add("F"); Edge_Weight.add(1.0);

		*/
		/*Source_Vertex.add("A"); Target_Vertex.add("B"); Edge_Weight.add(0.7);
		Source_Vertex.add("B"); Target_Vertex.add("C"); Edge_Weight.add(0.9);
		Source_Vertex.add("C"); Target_Vertex.add("D"); Edge_Weight.add(0.57);
		Source_Vertex.add("D"); Target_Vertex.add("B"); Edge_Weight.add(1.0);
		Source_Vertex.add("C"); Target_Vertex.add("A"); Edge_Weight.add(1.3);
		Source_Vertex.add("A"); Target_Vertex.add("D"); Edge_Weight.add(0.3);
		Source_Vertex.add("D"); Target_Vertex.add("F"); Edge_Weight.add(0.2);
		//Source_Vertex.add("D"); Target_Vertex.add("E"); Edge_Weight.add(0.8);
		Source_Vertex.add("E"); Target_Vertex.add("G"); Edge_Weight.add(0.4);
		Source_Vertex.add("F"); Target_Vertex.add("E"); Edge_Weight.add(0.6);
		Source_Vertex.add("G"); Target_Vertex.add("F"); Edge_Weight.add(0.2);
		*/
		//System.out.println("Graph \n "+);
		System.out.println("Closeness Centrality calculation ");
		GA1.Closeness_Centrality_Score(Distinct_Vertex, Source_Vertex, Target_Vertex, Edge_Weight);
	} 
}
