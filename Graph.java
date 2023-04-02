import java.io.*;
import java.util.*;

public class Graph {
    public class Vertex {
        private int value;

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

    public class Edge {
        Vertex origin, destination;
        int value = 1;

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

    public int size(){
        return nodes.size();
    }

    public Vector<Edge> edges_with_origin(Vertex target){
        Vector<Edge> edges = new Vector<Edge>();

        // TODO

        return edges;
    }
    
    public Vector<Vertex> leads_to(Vertex target){
        Vector<Vertex> origin = new Vector<Vertex>();

        // TODO

        return origin;
    }
}
