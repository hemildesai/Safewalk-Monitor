import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;

import javax.swing.*;

import java.util.Iterator;

public class View extends JPanel{
    
    Model model;
    private Image mapImage;
    private Image volunteerImage;
    private Image requesterImage;
    
    
    final static int DIAMETER = 16; // diameter of circle at each location
    final static float dash1[] = { 10.0f };
    final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    Graphics2D g2;
    float scaleWidth;  //  Width Parameter for resizing the image
    float scaleHeight; //  Height Parameter for resizing the image
    
    public View(Model model) {
        
        
        this.model = model;
        
        mapImage = loadImage("CampusCropped-Faded.jpg");
        volunteerImage = loadImage("volunteer.jpg");
        requesterImage = loadImage("request.jpg");
        
        
        // The following code displays a blank JPanel with SafeWalkView in the title bar    
        JFrame frame = new JFrame("SafeWalkView");
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Adding the canvas (this) to the main panel at the CENTER...
        mainPanel.add(this, BorderLayout.CENTER);
        
        
        // Adding the main panel to the frame...
        frame.add(mainPanel);
        JScrollPane pane = new JScrollPane(this);
        frame.add(pane);
        frame.getContentPane().add(pane);
        frame.setSize(500,600);
        // Sets visible and away we go...
        frame.setVisible(true);
        
    }
    
    
    
    /**
     * Called on the Event Dispatch Thread (EDT) in response to a call to
     * repaint(). Accesses the model to decide what to paint. Since the model is
     * also being accessed and updated on a different thread (by the Controller
     * when messages arrive), must get the lock before accessing the model.
     */
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        g2 = (Graphics2D) gr; //Stores the graphic as a 2D graphic image
        
        /** 
         *  Insert your code by following the instructions in TODO:
         */  
        scaleWidth = getWidth()/((float) mapImage.getWidth(null));
        scaleHeight = getHeight()/((float) mapImage.getHeight(null));
        
        // TODO Calculates the 'scaleWidth' and 'scaleHeight' parameters as width of the Panel
        //divided by the mapImage Width or Height.
        // To implement this use the getWidth() and getHeight() methods defined in the JPanel class 
        // and the type-casted (float)mapImage.getWidth(null) and (float)mapImage.getHeight(null) values 
        // respectively for this section of the code. Note that these parameters are for resizing the 
        // image and the coordinates as per the panel dimensions. 
        
        
        
        // Draw the map first...
        //TODO Draws the image using DrawImage() method
        //Syntax for drawImage method:  g2.drawImage(Image img, int x, int y, int width, int height, null);
        g2.drawImage(mapImage, 0, 0, (int) getWidth(), (int) getHeight(), null);
        synchronized (model.lock) {
            drawLocation();  //  Calls the drawLocation method
            drawIntransit(); //  Calls the drawIntransit method
        }
    }
    
    
    /** 
     * This method places indicators on each marked location and displays the volunteers 
     * and the requesters present at a particular location. The method first Iterates through 
     * each location HashSet<String> of locations stored in the model and converts the string
     * coordinates into integers. These values are used to draw two concentric circles at location.
     * 
     * Next the method displays a list of volunteers and requesters at the location. 
     * 
     */  
    
    private void drawLocation() {
        
        /** 
         *  Insert your code by following the instructions in TODO:
         */  
        
        
        //Refer Javadocs for using Iterator
        int x = 0; // Variable for X coordinate of the location 
        int y = 0; // Variable for Y coordinate of the location
        Iterator<Location> it= (model.getLocations()).iterator(); 
        Location l;
        while (it.hasNext()) {
            l = it.next();
            double[] xy = (l.getXY()); //Iterates through each location
            
            x = (int) Math.round(xy[0]); //Fetches x coordinate
            y = (int) Math.round(xy[1]); //Fetches y coordinate
            
            // Draw overlapping circle for the building location...
            g2.setColor(Color.BLACK); //Sets color to Black for outer circle
            
            //TODO use this command to fill the first circle g2.fillOval(Math.round(x * scaleWidth), Math.round(y * scaleHeight), DIAMETER, DIAMETER);
            g2.fillOval(Math.round(x * scaleWidth), Math.round(y * scaleHeight), DIAMETER, DIAMETER);
            
            g2.setColor(Color.YELLOW); //Sets color to Yellow for inner circle
            
            //TODO use this command to fill the second circle g2.fillOval(Math.round(x * scaleWidth), Math.round(y * scaleHeight), DIAMETER, DIAMETER);
            //Adjust the dimensions of the Yellow circle to make the circles appear like concentric circles
            g2.fillOval(Math.round(x * scaleWidth + 4), Math.round(y * scaleHeight + 4), DIAMETER - 8, DIAMETER - 8);
            
            g2.setColor(Color.BLACK); //Sets the color back to black
            
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            // Sets font to "Arial" with type= Font.PLAIN and size = 12
            
            
            int dy = g2.getFontMetrics().getHeight(); // Calculates line width dy
            
            y+=dy;   // Adds dy to the y coordinate to move to the next line in the image
            
            // This prints the volunteers and the requests in the next line 
            Request r;
            Volunteer v;
            Location location;
            
            
            Iterator<Request> rt = (l.getRequests().iterator());
            while(rt.hasNext()) {
                r = rt.next();
                drawName(requesterImage, String.format("%s %s %d",r.getName(),r.getDestination().getName(),r.getValue()), x, y);
                
                y+=dy;
            }
            
            Iterator<Volunteer> vt = (l.getVolunteers().iterator());
            while(vt.hasNext()) {
                v = vt.next();
                drawName(volunteerImage, String.format("%s %d",v.getName(),v.getScore()), x, y);
                
                y+=dy;
            }
        }
        /*int i2 = 3;
         Iterator<Volunteer> it3 = model.getVolunteers().iterator();
         while (it3.hasNext()) {
         
         v = it3.next(); //Iterates through each key
         //TODO gets location of each Volunteer (key) and prints the volunteers at each location 
         //using drawName method  
         location = v.getCurrentLocation();
         rv = location.getXY();
         x2 = (int) Math.round(rv[0]);
         y2 = (int) Math.round(rv[1]);
         drawName(volunteerImage, v.getName(), x2 + 20, y2 + i2);
         y+=dy;   // Adds dy to the y coordinate to move to the next line in the image
         i2 += 3;
         }*/
    }
    
    
    
    private void drawName(Image image, String name, int x, int y) {
        int gap = g2.getFontMetrics().getHeight(); 
        
        g2.drawImage(image, Math.round(x * scaleWidth) - gap, Math.round(y * scaleHeight), 12, 12, null);
        g2.drawString(name, Math.round(x * scaleWidth), Math.round(y * scaleHeight) + gap - 3);
    }
    /** Method drawIntransit
      *  This method draws movers from start location to end location and draws
      *  a moving green dot to depict the motion. Command for motion is given in 
      *  Controller.java
      */ 
    private void drawIntransit() {
        /** 
         *  Insert your code in TODO by following the instructions:
         */  
        Volunteer v;
        try{
            Iterator it = model.getVolunteers().iterator();
            while (it.hasNext()) {   
                
                v = (Volunteer) it.next();
                String r = v.getRequester();
                // Uses the getVolunteerPosition() method in model to get location of mover.
                //You may uncomment the following code for implementing this:
                int x = 0;
                int y = 0;
                
                double[] xy = v.getCurrentPosition();
                x = (int) Math.round(xy[0]);
                y = (int) Math.round(xy[1]);
                
                
                //String r = v.getRequester();
                // Draw mover name and anyone being walked
                //TODO Sets font to "Arial" with type= Font.PLAIN and size = 16 (As done above)
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                //TODO Calculates line width ('gap' variable) using methods g2.getFontMetrics().getHeight()
                int gap = g2.getFontMetrics().getHeight(); 
                //TODO sets the color to Color.Black using: g2.setColor(Color.BLACK);
                g2.setColor(Color.BLACK);
                //TODO Stores the mover name in a string variable for displaying
                
                //TODO Draws the mover name at the obtained position using drawString method 
                g2.drawString(v.getName(),(Math.round(x * scaleWidth) + DIAMETER/2) + 5, (Math.round(y * scaleHeight) + DIAMETER/2) + 5);
                //drawName(volunteerImage, v.getName(), x, y);
                if(!r.equals(null)) {
                    int dy = g2.getFontMetrics().getHeight(); // Calculates line width dy
                    
                    // Adds dy to the y coordinate to move to the next line in the image
                    
                    g2.drawString(r,(Math.round(x * scaleWidth) + DIAMETER/2 ) + 5, (Math.round((y+dy) * scaleHeight) + DIAMETER/2) + 5);
                }
                
                // Drawing the dashed line
                
                //TODO Fetches 'name' from String array and stores in a String variable
                
                //TODO Obtains start building of mover using HashMap<String,String> getVolunteer2Location() method
                double[] start = v.getStart();
                //TODO Obtains location coordinates of the Building using getLocation2Coordinate() method in model
                
                //TODO Splits the location String obtained into coordinates using parseInt() and store in two different variables 
                
                int x1 = (int)Math.round((start[0]));
                int y1 = (int)Math.round((start[1]));
                //TODO Stores destination Building name into a String variable
                
                //TODO gets coordinates of the building using getLocation2Coordinate() method
                double[] dest = v.getDestination();
                //TODO Splits the obtained String to save the coordinates in two int variables
                
                int x2 = (int)Math.round((dest[0]));
                int y2 = (int)Math.round((dest[1]));
                
                g2.setStroke(dashed); //sets drawing style to shaded line
                
                //TODO: Draws a line using GraphicsObject.drawLine() method 
                //Syntax: drawLine(int x1, int y1, int x2, int y2)
                g2.drawLine((Math.round(x1 * scaleWidth) + DIAMETER / 2), (Math.round(y1 * scaleHeight) + DIAMETER / 2), (Math.round(x2 * scaleWidth) + DIAMETER / 2), (Math.round(y2 * scaleHeight) + DIAMETER / 2));
                //for coordinate use the scaled and rounded coordinates with offset given in the example 
                //For example for x coordinate of starting point use (Math.round(xStart * scaleWidth) + DIAMETER / 2)
                
                
                // Draw filled circle at mover location on the dashed line
                //TODO Sets a moving green dot at the scaled coordinate
                g2.setColor(Color.GREEN);
                g2.fillOval(Math.round(x * scaleWidth), Math.round(y * scaleHeight), DIAMETER, DIAMETER);
                
                // Use setColor() and fillOval() method for drawing the moving green dot. 
            }
        } catch (NullPointerException e) {};
    }
    
    
    /** Method loadImage
      * Loads images from resources, allowing use in .jar files.
      * 
      * @param name
      *            file name where the icon is located.
      * @return the Image found in the file.
      */
    private Image loadImage(String name) {
        URL url = getClass().getResource(name); //Gets the image URL
        if (url == null)
            throw new RuntimeException("Could not find " + name);
        return new ImageIcon(url).getImage(); //Loads the image and returns the Image
    }
    
}