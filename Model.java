/**
*@author Hemil Desai
*@username desai38
* 
*/

import java.util.*;

public class Model {
    
    public Object lock = new Object();
    private HashSet<Location> locations = new HashSet<Location>();
    private HashSet<Request> requests = new HashSet<Request>();
    private HashSet<Volunteer> volunteers = new HashSet<Volunteer>();
    private HashMap<String, Location> l = new HashMap<String, Location>();
    private HashMap<String, Request> r = new HashMap<String, Request>();
    private HashMap<String, Volunteer> v = new HashMap<String, Volunteer>();
    
    public Model() {
        Object o = new Object();
        lock = o;
    }
    
    public void addLocation(Location location) {
        locations.add(location);
        l.put(location.getName(), location);
    }
    
    public void addRequest(Request request) {
        requests.add(request);
        r.put(request.getName(), request);
    }
    
    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
        v.put(volunteer.getName(), volunteer);
    }
    
    public Location getLocationByName(String name) {
        return l.get(name);
    }
    
    public HashSet<Location> getLocations() {
        return locations;
    }
    
    public Request getRequestByName(String name) {
        return r.get(name);
    }
    
    public HashSet<Request> getRequests() {
        return requests;
    }
    
    public Volunteer getVolunteerByName(String name) {
        return v.get(name);
    }
    
    public HashSet<Volunteer> getVolunteers() {
        return volunteers;
    }
    
    public void removeRequest(Request request) {
        requests.remove(request);
        request.getStart().removeRequest(request);
        r.remove(request.getName());
    }
    
    public void removeVolunteer(Volunteer volunteer) {
        volunteers.remove(volunteer);
        v.remove(volunteer.getName());
        if(volunteer.getCurrentLocation() != null) {
           volunteer.getCurrentLocation().removeVolunteer(volunteer);
        }
    }
}
    
    