package schoolevacuation;

import java.util.Random;
import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

public class Student
{
	Circle representation;
	private int grade;
	private char gender;
	private String currentLocation;
	
	private final double CIRCLE_RADIUS = 3.5;
	Random generator = new Random();
	
	// Coordinates for the top and bottom of the building stairways
	public double stairwayTopx = 0, stairwayTopy = 0, stairwayBottomx = 0, stairwayBottomy = 0;
	
	public Student(int grade, char gender, double x, double y, String currentLocation)
	{
		this.grade = grade;
		this.gender = gender;
		this.currentLocation = currentLocation;

		representation = createRepresentation(x, y, grade, gender);
		representation.toFront();
	}
	
	// A colored circle that will represent the student
	private Circle createRepresentation(double x, double y, int grade, char gender)
	{
		Color color;
		
		if (gender == 'M')
			if (grade == 9)
				color = Color.LIGHTSKYBLUE;
			else if (grade == 10)
				color = Color.DEEPSKYBLUE;
			else if (grade == 11)
				color = Color.DODGERBLUE;
			else
				color = Color.ROYALBLUE;
		else
			if (grade == 9)
				color = Color.LIGHTPINK;
			else if (grade == 10)
				color = Color.PINK;
			else if (grade == 11)
				color = Color.HOTPINK;
			else
				color = Color.DEEPPINK;
		
		Circle representation = new Circle(x, y, CIRCLE_RADIUS);
		representation.setFill(color);
		
		return representation;
	}
	
	private void setSBuildingStairwayCoordinates()
	{
		// random point at S Building stairway top
		stairwayTopx = 630.0;
		stairwayTopy = 200.0;
		stairwayTopy += (generator.nextDouble() * 50);
		// random point at S Building stairway bottom
		stairwayBottomx = 730.0;
		stairwayBottomy = 210.0;
		stairwayBottomy += (generator.nextDouble() * 35);
	}
	
	private void setHBuildingStairwayCoordinates()
	{
		// random point at H Building stairway top
		stairwayTopx = 630.0;
		stairwayTopy = 602.0;
		stairwayTopy += (generator.nextDouble() * 50);
		// random point at H Building stairway bottom
		stairwayBottomx = 730.0;
		stairwayBottomy = 612.0;
		stairwayBottomy += (generator.nextDouble() * 35);
	}
	
	public void move(double xDest, double yDest, String currentLocation, String destination)
	{
		Path path = new Path();
		
		// --------------------------------------------------------------
		
		if (currentLocation.equals("S Building"))
			setSBuildingStairwayCoordinates();
		
		else if (currentLocation.equals("H Building"))
			setHBuildingStairwayCoordinates();
		
		else if (currentLocation.equals("Lunch or PT"))
		{
			if (destination.equals("S Building"))
				setSBuildingStairwayCoordinates();
			else if (destination.equals("H Building"))
				setHBuildingStairwayCoordinates();
			// evacuate to field
			else
			{
				// no need for stairways, go straight to field
				stairwayTopx = xDest;
				stairwayTopy = yDest;
				stairwayBottomx = xDest;
				stairwayBottomy = yDest;
			}
		}
		
		// current location is at the field
		else
		{
			if (destination.equals("S Building"))
				setSBuildingStairwayCoordinates();
			else if (destination.equals("H Building"))
				setHBuildingStairwayCoordinates();
			// go to lunch/PT
			else
			{
				// no need for stairways, go straight to lunch/PT
				stairwayTopx = xDest;
				stairwayTopy = yDest;
				stairwayBottomx = xDest;
				stairwayBottomy = yDest;
			}
		}
		
		// --------------------------------------------------------------

		if (destination.equals("Football Field"))
		{
			PathTransition pathTransition = createPath(stairwayTopx, stairwayTopy, stairwayBottomx, stairwayBottomy, xDest, yDest);
			pathTransition.play();
		}
		else if (destination.equals("Lunch or PT"))
		{
			PathTransition pathTransition = createPath(stairwayTopx, stairwayTopy, stairwayBottomx, stairwayBottomy, xDest, yDest);
			pathTransition.play();
		}
		// move to either classroom
		else
		{
			PathTransition pathTransition = createPath(stairwayBottomx, stairwayBottomy, stairwayTopx, stairwayTopy, xDest, yDest);
			pathTransition.play();
		}
	}
	
	private PathTransition createPath(double dest1x, double dest1y, double dest2x, double dest2y, double dest3x, double dest3y)
	{
		Polyline polyline = new Polyline();
		polyline.getPoints().addAll(new Double[]
		{representation.getCenterX(), representation.getCenterY(),
		 dest1x, dest1y,
		 dest2x, dest2y,
		 dest3x, dest3y});
		representation.setCenterX(dest3x);
		representation.setCenterY(dest3y);
		
		PathTransition pathTransition = new PathTransition();
		pathTransition.setNode(representation);
		
		
		int duration = generator.nextInt(7000) + 5800;
		pathTransition.setDuration(Duration.millis(duration));
		pathTransition.setPath(polyline);
		
		return pathTransition;
	}
	
	public String getCurrentLocation()
	{
		return this.currentLocation;
	}
	
	public Node getNode()
	{
		return this.representation;
	}
	
	public void setCurrentLocation(String newCurrentLocation)
	{
		this.currentLocation = newCurrentLocation;
	}
}
