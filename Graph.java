import java.io.*;
import java.util.*;

public class Graph {
    public class Vertex {
        private int value;
        public char color;

        Vertex(int value){
            this.value = value;
        }

        public int get_value(){
            return value;
        }

        public boolean equals(Vertex other){
            return other.get_value() == value;            
        }
    }

    public class Edge implements Comparable<Edge>{
        Vertex origin, destination;
        public int value = 1;

        Edge(Vertex origin, Vertex destination){
            this.origin = origin;
            this.destination = destination;
        }

        Edge(Vertex origin, Vertex destination, int value){
            this.origin = origin;
            this.destination = destination;
            this.value = value;
        }

        public Vertex[] endpoints(){
            Vertex[] ret = {origin, destination};
            return ret;
        }

        public Vertex opposite(Vertex start){
            if (start.equals(origin))
                return destination;
            return origin;
        }

        public int compareTo(Edge other) {
            return value - other.value;
        } 
    }

    Vector<Vertex> nodes = new Vector<Vertex>();
    Vector<Edge> edges = new Vector<Edge>();
    private boolean directed = false;

    Graph(){}

    Graph(boolean directed){
        this.directed = directed;
    }

    public boolean is_directed(){
        return directed;
    }

    public void add_vertex(Vertex v){
        nodes.add(v);
    }

    public void add_edge(Vertex v1, Vertex v2){
        edges.add(new Edge(v1, v2));
        if (!directed)
            edges.add(new Edge(v2, v1));
    }

    public void add_edge(Edge e){
        edges.add(e);
    }

    public int size(){
        return nodes.size();
    }

    public Vector<Edge> edges_with_origin(Vertex target){
        Vector<Edge> edges = new Vector<Edge>();

        for (Edge edge : this.edges){
            Vertex[] points = edge.endpoints();
            if (points[0].equals(target)){
                edges.add(edge);
            }
            else if (!directed){
                if (points[1].equals(target)){
                    edges.add(edge);
                }
            }
        }

        return edges;
    }
    
    public Vector<Vertex> leads_to(Vertex target){
        Vector<Vertex> origin = new Vector<Vertex>();

        for (Edge edge : this.edges){
            Vertex[] points = edge.endpoints();
            if (points[1].equals(target)){
                origin.add(points[0]);
            }
            else if (!directed){
                if (points[0].equals(target)){
                    origin.add(points[1]);
                }
            }
        }

        return origin;
    }

    public Vector<Vertex> BFS(Vector<Vertex> sources){
        Vector<Vertex> level = sources;
        Vector<Vertex> discovered = sources;

        while (level.size()>0){
            Vector<Vertex> next_level = new Vector<Vertex>();
            for (Vertex u : level){
                for (Edge e : edges_with_origin(u)){
                    Vertex v = e.opposite(u);
                    if (!discovered.contains(v)){
                        discovered.add(v);
                        next_level.add(v);
                    }
                }
            }
            level = next_level;
        }

        return discovered;
    }

    public Vector<Vertex> BFS(Vertex source){
        Vector<Vertex> tmp = new Vector<Vertex>();
        tmp.add(source);
        return BFS(tmp);
    }

    
    public Vector<Vertex> coloring(Vector<Vertex> sources){
        Vector<Vertex> level = sources;
        Vector<Vertex> discovered = sources;
        
        for (Vertex u : level){
            u.color = 'r';
        }
        boolean red = false;

        while (level.size()>0){
            Vector<Vertex> next_level = new Vector<Vertex>();
            for (Vertex u : level){
                for (Edge e : edges_with_origin(u)){
                    Vertex v = e.opposite(u);
                    if (!discovered.contains(v)){
                        if (red)
                            v.color = 'r';
                        else
                            v.color = 'b';
                        discovered.add(v);
                        next_level.add(v);
                    } else {
                        if ((red && v.color!='r') || (!red && v.color!='b')){
                            System.out.println("Graph not biparti!");
                            return null;
                        }
                    }
                }
            }
            red = !red;
            level = next_level;
        }

        return discovered;
    }

    public Graph prim(){
        Graph mst = new Graph();

        if (this.size() == 0)
            return mst;

        Vertex start = nodes.firstElement();
        mst.add_vertex(start);
        Vector<Edge> possible_edges = edges_with_origin(start);
        Collections.sort(possible_edges);  

        while (mst.size() < this.size()){
            while (possible_edges.isEmpty()){
                // multiple disjoint graphs
                for (Vertex v : nodes){
                    if (!mst.nodes.contains(v)){
                        start = v;
                        mst.add_vertex(start);
                        possible_edges = edges_with_origin(start);
                        Collections.sort(possible_edges); 
                        break;
                    }
                }
            }

            while (!possible_edges.isEmpty()){
                Edge current = possible_edges.remove(0);
                Vertex[] points = current.endpoints();
                if (mst.nodes.contains(points[0])){
                    if (!mst.nodes.contains(points[1])){
                        mst.add_vertex(points[1]);
                        mst.add_edge(current);
                        possible_edges.addAll(edges_with_origin(points[1]));
                        Collections.sort(possible_edges);
                    }
                } else {
                    mst.add_vertex(points[0]);
                    mst.add_edge(current);
                    possible_edges.addAll(edges_with_origin(points[0]));
                    Collections.sort(possible_edges);
                }
            }

        }

        return mst;
    }

    public Graph kruskal(){
        Graph mst = new Graph();

        if (this.size() == 0)
            return mst;

        Vector<Edge> sorted_edges = edges;
        Collections.sort(sorted_edges);  

        while (mst.size() < this.size() && !sorted_edges.isEmpty()){
            Edge current = sorted_edges.remove(0);
            Vertex[] points = current.endpoints();
            
            if (mst.nodes.contains(points[0]) && mst.nodes.contains(points[1])){
                // see if we need to join 2 subgraphs
                Vector<Vertex> subGraph = mst.BFS(points[0]);
                if (!subGraph.contains(points[1]))
                    mst.add_edge(current);

            } else {

                // see if we need to add one or both extremities
                if (!mst.nodes.contains(points[0]))
                    mst.add_vertex(points[0]);

                if (!mst.nodes.contains(points[1]))
                    mst.add_vertex(points[1]);

                mst.add_edge(current);
            }
        }

        if (sorted_edges.isEmpty()){
            // add single nodes
            for (Vertex v : nodes){
                if (!mst.nodes.contains(v)){
                    mst.add_vertex(v);
                }
            }
        }

        return mst;
    }
}
