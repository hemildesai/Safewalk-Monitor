/**
*@author Hemil Desai
*@username desai38
*/

import java.util.*;

public class Request {
    
    private Model m = new Model();
    private String name;
    private Location start;
    private Location destination;
    private int value;
    
    public Request(Model model, String name, Location start, Location destination, int value) {
        this.m = model;
        this.name = name;
        this.start = start;
        this.destination = destination;
        this.value = value;
        
        //Request r = new Request(model,name,start,destination,value);
        m.addRequest(this);
        this.start.addRequest(this);
    }
    
    public Location getDestination() {
        return destination;
    }
    
    public String getName() {
        return name;
    }
    
    public Location getStart() {
        return start;
    }
    
    public int getValue() {
        return value;
    }
}