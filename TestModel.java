import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Model test class. Run this class using JUnit 4 to test against model classes (Model, Location, Request, and
 * Volunteer).  JUnit is built in to Eclipse and DrJava.  To run from the command line, use these instructions...
 * 
 * Using JUnit from the Command Line on CS Lab Machines...
 * 
 * 1. Compile with: javac -cp /homes/cs180/lib/jar/junit-4.7.jar:. TestModel.java
 * 2. Run tests with: java -cp /homes/cs180/lib/jar/junit-4.7.jar:. org.junit.runner. TestModel
 * 
 * Using JUnit from the Command Line on Your Own Computer...
 * 
 * 1. Download the latest junit-4*.jar file and hamcrest-core-*.jar file from junit.org.
 * 2. Edit the CP line below to reference these two jar files and your source directory.
 * 3. Run these commands in a shell script (e.g., $ sh test.sh).
 * CP=lib/junit-4.11.jar:lib/hamcrest-core-1.3.jar:src
 * javac -cp $CP -sourcepath src src/TestModel.java
 * java -cp $CP org.junit.runner.JUnitCore TestModel
 * 
 * Note for the curious: junit-4.7.jar apparently includes the hamcrest utilities bundled.  They are no
 * longer bundled in junit-4.11.jar, so must be separately downloaded.  Hamcrest is not needed to compile,
 * but it is needed to run.  See http://junit.org for more information.
 * 
 * @author jtk
 * @version 2014
 */
public class TestModel {
    Model model;
    Location locationLWSN;
    Location locationPMU;
    Volunteer volunteer;
    Request request;

    /**
     * Runs once before any tests. Use to initialize static fields.
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * Runs once after all tests.
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Runs before each test method. Use to initialize member fields.
     * 
     * Create a new Model, two locations, a volunteer, and a request. These are used in many of the tests.
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
	model = new Model();
	locationLWSN = new Location(model, "LWSN", 5.0, 10.0);
	locationPMU = new Location(model, "PMU", 100.0, 500.0);
	volunteer = new Volunteer(model, "Helpful", 100, locationLWSN);
	request = new Request(model, "Purdue_Pete", locationLWSN, locationPMU, 1000);
    }

    /**
     * Runs after each test method. Cleanup, if necessary.
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests that the constructor succeeded (a bit overkill).
     */
    @Test
    public void testModel() {
	assertNotNull(model);
    }

    /**
     * Tests that the locations were created properly.
     */
    @Test
    public void testAddLocation() {
	assertEquals("LWSN", locationLWSN.getName());
	double[] xy = locationLWSN.getXY();
	assertEquals(5.0, xy[0], 0.1);
	assertEquals(10.0, xy[1], 0.1);
    }

    /**
     * Tests that the request was created properly and that it was added to the proper location.
     */
    @Test
    public void testAddRequest() {
	assertEquals("Purdue_Pete", request.getName());
	assertEquals(locationLWSN, request.getStart());
	assertEquals(locationPMU, request.getDestination());
	assertEquals(1000, request.getValue());
	assertTrue(locationLWSN.getRequests().contains(request));
	assertFalse(locationPMU.getRequests().contains(request));
    }

    /**
     * Tests that the volunteer was created correctly.
     */
    @Test
    public void testAddVolunteer() {
	assertEquals("Helpful", volunteer.getName());
	assertEquals(100, volunteer.getScore());
	assertEquals(locationLWSN, volunteer.getCurrentLocation());
    }
    
    @Test
    public void testStartMoving() {
	assertEquals(locationLWSN, volunteer.getCurrentLocation());
	assertEquals(1, locationLWSN.getVolunteers().size());

	volunteer.startMoving(locationPMU, 500);

	assertEquals(0, locationLWSN.getVolunteers().size());
	assertEquals(null, volunteer.getCurrentLocation());
	
	// Sleep for a bit to let some movement happen...
	sleep(300);
	double[] xy = volunteer.getCurrentPosition();
	assertTrue(5.0 < xy[0]);
	assertTrue(5.0 < xy[1]);
	
	// Sleep for a bit more. By now the movement should be completed...
	sleep(300);
	xy = volunteer.getCurrentPosition();
	double[] xyPMU = locationPMU.getXY();
	assertEquals(xy[0], xyPMU[0], 0.1);
	assertEquals(xy[0], xyPMU[0], 0.1);
    }
    
    @Test
    public void testStartWalking() {
	assertEquals(locationLWSN, volunteer.getCurrentLocation());
	assertEquals(1, locationLWSN.getVolunteers().size());
	assertEquals(1, locationLWSN.getRequests().size());
	assertEquals(request, model.getRequestByName(request.getName()));
	
	volunteer.startWalking(request, 500);

	assertEquals(0, locationLWSN.getVolunteers().size());
	assertEquals(0, locationLWSN.getRequests().size());
	assertEquals(null, volunteer.getCurrentLocation());
	assertEquals(null, model.getRequestByName(request.getName()));
	
	// Sleep for a bit to let some movement happen...
	sleep(300);
	double[] xy = volunteer.getCurrentPosition();
	assertTrue(5.0 < xy[0]);
	assertTrue(5.0 < xy[1]);
	
	// Sleep for a bit more. By now the movement should be completed...
	sleep(300);
	xy = volunteer.getCurrentPosition(); // after arrival; triggers isMoving to return false
	double[] xyPMU = locationPMU.getXY();
	assertEquals(xy[0], xyPMU[0], 0.1);
	assertEquals(xy[0], xyPMU[0], 0.1);
    }

    /**
     * Tests that the locations can be looked up in the model.
     */
    @Test
    public void testGetLocationByName() {
	assertEquals(locationLWSN, model.getLocationByName("LWSN"));
	assertEquals(locationPMU, model.getLocationByName("PMU"));
	Location location = new Location(model, "PMU", 300, 300);
	model.addLocation(location);
	assertEquals(location, model.getLocationByName("PMU"));
    }

    /**
     * Tests that the model data structure that stores the locations is the right size.
     */
    @Test
    public void testGetLocations() {
	assertTrue(model.getLocations().size() == 2);
    }

    /**
     * Tests that the request can be looked up in the model.
     */
    @Test
    public void testGetRequestByName() {
	assertEquals(request, model.getRequestByName("Purdue_Pete"));
    }

    /**
     * Tests that the model data structure that stores the requests is the right size.
     */
    @Test
    public void testGetRequests() {
	assertTrue(model.getRequests().size() == 1);
    }

    /**
     * Tests that the volunteer can be looked up by name in the model.
     */
    @Test
    public void testGetVolunteerByName() {
	assertEquals(volunteer, model.getVolunteerByName("Helpful"));
    }

    /**
     * Tests that the volunteer data structure is the right size.
     */
    @Test
    public void testGetVolunteers() {
	assertTrue(model.getVolunteers().size() == 1);
    }

    /**
     * Tests that requests are properly removed from the model and the associated location.
     */
    @Test
    public void testRemoveRequest() {
	Request r = model.getRequestByName("Purdue_Pete");
	Location l = r.getStart();

	// Assert that we found "Purdue_Pete" and that the request is contained at the start location...
	assertNotNull(r);
	assertTrue(l.getRequests().contains(r));

	int c = model.getRequests().size();
	model.removeRequest(request);

	// Confirm the request has been removed from both the model data structure as well as the location...
	assertFalse(l.getRequests().contains(r));
	assertNull(model.getRequestByName("Purdue_Pete"));
	assertTrue(model.getRequests().size() == c - 1);
    }

    @Test
    public void testRemoveVolunteer() {
	Volunteer v = model.getVolunteerByName("Helpful");
	Location l = v.getCurrentLocation();
	
	// Assert that we found "Helpful" in the model...
	assertNotNull(v);
	assertTrue(l.getVolunteers().contains(v));
	
	int c = model.getVolunteers().size();
	model.removeVolunteer(volunteer);

	// Assert that "Helpful" is removed from the model, and from the location... 
	assertNull(model.getVolunteerByName("Helpful"));
	assertTrue(model.getVolunteers().size() == c - 1);
	assertFalse(l.getVolunteers().contains(v));
    }

    private void sleep(long ms) {
	try {
	    Thread.sleep(ms);
	} catch (InterruptedException e) {
	    // ignore
	}
    }
}
