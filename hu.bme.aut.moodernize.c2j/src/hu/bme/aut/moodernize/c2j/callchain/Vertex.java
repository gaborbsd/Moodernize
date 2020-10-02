package hu.bme.aut.moodernize.c2j.callchain;

public class Vertex {
    private String name;

    public Vertex(String name) {
	super();
	this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof Vertex)) {
	    return false;
	}
	
        return ((Vertex) obj).getName().equals(name);
    }
}
