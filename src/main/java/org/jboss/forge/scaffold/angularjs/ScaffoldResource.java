package org.jboss.forge.scaffold.angularjs;

public class ScaffoldResource {

    String source;
    String destination;

    public ScaffoldResource(String source) {
        this(source, null);
    }

    public ScaffoldResource(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
}
