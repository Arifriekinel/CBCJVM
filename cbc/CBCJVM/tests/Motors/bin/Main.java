//package cbccore.tests;

import cbccore.motors.Drive;
import cbccore.motors.LeftTurnMovement;
import cbccore.motors.Motor;
import cbccore.motors.NonBlockingDriver;
import cbccore.motors.StraightMovement;
import cbccore.motors.TwoWheelDriver;

public class Main {
	private final static int SPEED = 1000;
	public static void main(String[] args) {
		// Motors encapsulate all motor functionality
		Motor left = new Motor(0);
		Motor right = new Motor(1);
		// Drivers are fed movements and must do something with them
		TwoWheelDriver driver = new TwoWheelDriver(left, right);
		
		// Drives encapsulate complex movement patterns
		Drive toEnemy = new Drive();
		toEnemy.add(new StraightMovement(SPEED, 100));
		toEnemy.add(new LeftTurnMovement(SPEED, 90));
		toEnemy.add(new StraightMovement(SPEED, 100));
		
		// Blocking drive
		driver.drive(toEnemy);
		
		// Non-blocking drive
		NonBlockingDriver nonBlock = new NonBlockingDriver(driver);
		nonBlock.getDrives().add(toEnemy);
		nonBlock.run();
	}
}
