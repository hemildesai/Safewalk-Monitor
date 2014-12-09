import javax.swing.*;
import java.util.*;


public class Controller extends SwingWorker<Object, Object> implements Observer {
    
    private Model model;
    private View view;
    private static final String HOST = "pc.cs.purdue.edu";
    private static final int PORT = 1337;

    private Connector connector;
    
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        connector = new Connector(HOST, PORT, "connect monitor", this);
        
        execute();
    }
    
    public void update(Observable arg1, Object arg2) {
        String message = (String) arg2;
        String[] fields = message.split(" ");
        
        synchronized (model.lock) {
            if(fields[0].equals("location")) {
                handleLocation(fields);
            } else if(fields[0].equals("volunteer")) {
                handleVolunteer(fields);
            } else if(fields[0].equals("request")) {
                handleRequest(fields);
            } else if(fields[0].equals("moving")) {
                handleMoving(fields);
            } else if(fields[0].equals("walking")) {
                handleWalking(fields);
            } else if(fields[0].equals("delete")){
                handleDelete(fields);
            }
        }
    }
    
    public void handleLocation(String[] fields) {
        String name = fields[1];
        Double x = Double.parseDouble(fields[2]);
        Double y = Double.parseDouble(fields[3]);
        
        model.addLocation(new Location(model, name, x, y));
    }
    
    public void handleRequest(String[] fields) {
        String name = fields[1];
        String fromLocation = fields[2];
        String toLocation = fields[3];
        int value = Integer.parseInt(fields[4]);
        Request old = model.getRequestByName(name);
        if(old != null) {
            model.removeRequest(old);
        }
        Location from = model.getLocationByName(fromLocation);
        Location to = model.getLocationByName(toLocation);
        if(from != null && to != null) {
        Request r  = new Request(model, name, from, to, value);
        }
    }
    
    public void handleVolunteer(String[] fields) {
        String name = fields[1];
        String loc = fields[2];
        int score = Integer.parseInt(fields[3]);
        Volunteer v = model.getVolunteerByName(name);
        Location l = model.getLocationByName(loc);
        if(l != null && v == null) {
        Volunteer v1 = new Volunteer(model, name, score, l);
       
       
        } else if (l != null && v != null) {
            Location current = v.getCurrentLocation();
            if(current == null) {
            current = l;
            v = new Volunteer(model, name, score, l);
            //current.addVolunteer(v);
            }
            
        }
    }
    
    public void handleWalking(String[] fields) {
        String v = fields[1];
        String r = fields[2];
        String fromLocation = fields[3];
        String toLocation = fields[4];
        Long time = Long.parseLong(fields[5]);
        
        Location from = model.getLocationByName(fromLocation);
        Location to = model.getLocationByName(toLocation);
        
        Volunteer vw = model.getVolunteerByName(v);
        Request rw = model.getRequestByName(r);
        if(vw != null && rw != null && from != null && to != null) {
        vw.startWalking(rw, time);
        //from.removeVolunteer(vw);
        }
    }
    
    public void handleMoving(String[] fields) {
        String v = fields[1];
        String des = fields[3];
        Long time = Long.parseLong(fields[4]);
        
        Location dest = model.getLocationByName(des);
        Volunteer vm = model.getVolunteerByName(v);
        Location cu = vm.getCurrentLocation();
        if (vm != null && des != null && cu != null) {
        vm.startMoving(dest, time);
            //cu.removeVolunteer(vm);
        }
    }
    
    public void handleDelete(String[] fields) {
        String v = fields[1];
        Volunteer vd = model.getVolunteerByName(v);
        if(vd != null) {
        model.removeVolunteer(vd);
    }
    }
    
    /**
     * This method is run on a background SwingWorker thread. It periodically
     * updates the display.
     */
    @Override
    protected Object doInBackground() throws Exception {
        while (true) {
            Thread.sleep(100); // Tune as appropriate
            view.repaint(); // Causes paintComponent(...) to be invoked on EDT
        }
    }
    
}
        