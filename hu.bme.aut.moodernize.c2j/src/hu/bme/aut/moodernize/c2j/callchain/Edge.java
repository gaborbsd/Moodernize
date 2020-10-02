package hu.bme.aut.moodernize.c2j.callchain;

public class Edge {
    private Vertex source, target;

    
    public Edge(Vertex source, Vertex target) {
	super();
	this.source = source;
	this.target = target;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getTarget() {
        return target;
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }
}
