import java.util.ArrayList;
import java.util.Random;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


public class Main extends Application
{	
	public final double BUILDING_WALKWAY_WIDTH = 30;
	public final int NUM_STUDENTS = 2000;
	
	Random generator = new Random();
	Pane root = new Pane();

	ArrayList<Student> students = new ArrayList<>(NUM_STUDENTS);

	public static void main(String[] args)
	{
		launch(args);
	}

	public Rectangle createRectangle(double x, double y, double width, double height, Color fill, double strokeWidth, Color stroke)
	{
		Rectangle rect = new Rectangle();

		rect.setX(x);
		rect.setY(y);
		rect.setWidth(width);
		rect.setHeight(height);
		rect.setFill(fill);
		rect.setStrokeWidth(strokeWidth);
		rect.setStroke(stroke);

		return rect;
	}

	public void addStudent(String location)
	{
		int grade;
		char gender;
		String currentLocation;
		double x, y;

		int randGrade = generator.nextInt(4);
		if (randGrade == 0)
			grade = 9;
		else if (randGrade == 1)
			grade = 10;
		else if (randGrade == 2)
			grade = 11;
		else
			grade = 12;

		int randGender = generator.nextInt(2);
		if (randGender == 0)
			gender = 'F';
		else
			gender = 'M';

		if (location.equals("S Building"))
		{
			x = 180.0;
			x += (generator.nextDouble() * 440);
			y = 80.0;
			y += (generator.nextDouble() * 140);

			currentLocation = "S Building";
		}
		else
		{
			x = 180.0;
			x += (generator.nextDouble() * 440);
			y = 630.0;
			y += (generator.nextDouble() * 140);

			currentLocation = "H Building";
		}

		Student student = new Student(grade, gender, x, y, currentLocation);
		students.add(student);

		root.getChildren().add(student.getNode());
	}

	public void createStudents()
	{
		// Randomly distribute students in S & H buildings
		for (int i = 0; i < NUM_STUDENTS; i++)
		{
			if (i < NUM_STUDENTS / 2)
				this.addStudent("S Building");
			else
				this.addStudent("H Building");
		}
	}

	public void moveStudents(String destination)
	{
		double x, y;
		String currentLocation;
		for (Student student : students)
		{
			if (destination.equals("Football Field"))
			{
				x = 965.0;
				x += (generator.nextDouble() * 225);
				y = 185.0;
				y += (generator.nextDouble() * 525);
				
				currentLocation = destination;
			}
			
			else if (destination.equals("Lunch or PT"))
			{
				x = 260.0;
				x += (generator.nextDouble() * 390);
				y = 360.0;
				y += (generator.nextDouble() * 130);
				
				currentLocation = destination;
			}
			
			// destinations = classrooms
			else
			{
				int building = generator.nextInt(2);
				if (building == 0)
				{
					x = 180.0;
					x += (generator.nextDouble() * 440);
					y = 80.0;
					y += (generator.nextDouble() * 140);

					currentLocation = "S Building";
					// need to set a more specific destination if going to a classroom
					destination = "S Building";
				}
				else
				{
					x = 180.0;
					x += (generator.nextDouble() * 440);
					y = 630.0;
					y += (generator.nextDouble() * 140);

					currentLocation = "H Building";
					// need to set a more specific destination if going to a classroom
					destination = "H Building";
				}
			}

			student.move(x, y, student.getCurrentLocation(), destination);
			student.setCurrentLocation(currentLocation);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("School Evacuation");

		// Basic campus layout
		Rectangle sBuilding = createRectangle(150.0, 50.0, 500.0, 200.0, Color.DARKCYAN, BUILDING_WALKWAY_WIDTH, Color.DARKSLATEGREY);	
		Rectangle sLeftStair = createRectangle(68.0, 202.0, 80.0, 50.0, Color.DARKSLATEGREY, 2.0, Color.DARKSLATEGREY);
		Rectangle sRightStair = createRectangle(652.0, 202.0, 80.0, 50.0, Color.DARKSLATEGREY, 2.0, Color.DARKSLATEGREY);

		Rectangle hBuilding = createRectangle(150.0, 600.0, 500.0, 200.0, Color.LIGHTSTEELBLUE, BUILDING_WALKWAY_WIDTH, Color.LIGHTSLATEGRAY);
		Rectangle hLeftStair = createRectangle(68.0, 602.0, 80.0, 50.0, Color.LIGHTSLATEGRAY, 2.0, Color.LIGHTSLATEGRAY);
		Rectangle hRightStair = createRectangle(652.0, 602.0, 80.0, 50.0, Color.LIGHTSLATEGRAY, 2.0, Color.LIGHTSLATEGRAY);

		Rectangle field = createRectangle(950.0, 175.0, 250.0, 550.0, Color.FORESTGREEN, 8.0, Color.SIENNA);

		Rectangle amphitheater = createRectangle(250.0, 350.0, 300.0, 150.0, Color.DARKSEAGREEN, 8.0, Color.OLIVEDRAB);
		Rectangle stage = createRectangle(558.0, 350.0, 100.0, 150.0, Color.BEIGE, 8.0, Color.BURLYWOOD);

		// Create buttons that'll control the movement
		Button classrooms = new Button();
		classrooms.setText("Classrooms");
		classrooms.setLayoutX(500);
		classrooms.setLayoutY(850);
		classrooms.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				moveStudents("Classrooms");
			}
		});
		
		Button lunchOrPT = new Button();
		lunchOrPT.setText("Lunch/PT");
		lunchOrPT.setLayoutX(600);
		lunchOrPT.setLayoutY(850);
		lunchOrPT.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				moveStudents("Lunch or PT");
			}
		});
		
		Button evacuation = new Button();
		evacuation.setText("Evacuation");
		evacuation.setLayoutX(690);
		evacuation.setLayoutY(850);
		evacuation.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				moveStudents("Football Field");
			}
		});

		// Add components & configure scene
		root.getChildren().add(sBuilding);
		root.getChildren().add(sLeftStair);
		root.getChildren().add(sRightStair);

		root.getChildren().add(hBuilding);
		root.getChildren().add(hLeftStair);
		root.getChildren().add(hRightStair);

		root.getChildren().add(field);
		root.getChildren().add(amphitheater);
		root.getChildren().add(stage);

		root.getChildren().add(classrooms);
		root.getChildren().add(lunchOrPT);
		root.getChildren().add(evacuation);
		
		this.createStudents();

		Scene scene = new Scene(root, 1200, 900);
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
	}
}
