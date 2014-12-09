/**
*@author Hemil Desai
*@username desai38
* 
*/

import java.util.*;

public class Location {
    
    private Model m = new Model();
    private String name;
    private double x;
    private double y;
    private HashSet<Volunteer> volunteers = new HashSet<Volunteer>();
    private HashSet<Request> requests = new HashSet<Request>();
    
    public Location (Model model, String name, double x, double y) {
    
    m = model;
    this.name = name;
    this.x = x;
    this.y = y;
    
    m.addLocation(this);
    
    }
    
    public void addRequest(Request request) {
        requests.add(request);
    }
    
    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
    }
    
    public String getName() {
        return name;
    }
    
    public HashSet<Request> getRequests() {
        return requests;
    }
    
    public HashSet<Volunteer> getVolunteers() {
        return volunteers;
    }
    
    public double[] getXY() {
        double[] c = new double[2];
        c[0] = x;
        c[1] = y;
        return c;
    }
    
    public void removeRequest(Request request) {
        requests.remove(request);
    } 
    
    public void removeVolunteer(Volunteer volunteer) {
        volunteers.remove(volunteer);
    }

}