package frc.robot;

//Imports
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup; //Replaces SpeedControllerGroup - EG 1.14.22
/*Must install rev robotics library in order to use SPARK Max speed controllers. 
 *Follow directions on site below.
 *https://docs.revrobotics.com/sparkmax/software-resources/spark-max-api-information#java-api
 *Help docs - https://codedocs.revrobotics.com/java/com/revrobotics/package-summary.html
*/
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


//The goal of this class is to create methods that can be used to operate the robot's drivetrain.
//This will clean up the code in Robot.java and will allow for easier future fixes.
public class RobotDrivetrain {
  
  //Creates SPARK MAX objects for each speed controller in the drivetrain.
  //Orientation assumes killswitch is at front of robot
  //The first parameter refers to the CAN ID - Use Rev Tool to determine CAN IDs
  //MotorType.kBrushless MUST be used when using NEO brushless motors
  private CANSparkMax leftFront = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax leftRear = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax rightFront = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax rightRear = new CANSparkMax(4, MotorType.kBrushless);
  
  
  //Groups left side speed controllers together and right side speed controllers together
  //This is cleaner than setting up two different differential drives
  private MotorControllerGroup driveLeft = new MotorControllerGroup(leftFront, leftRear);
  private MotorControllerGroup  driveRight = new MotorControllerGroup(rightFront, rightRear);
  //The DifferentialDrive class gives us access to the arcade drive, tank drive, and curvature drive methods.
  private DifferentialDrive robotDrive = new DifferentialDrive(driveLeft, driveRight);

  //Constructor is called in Robot.java to create RobotDrivetrain objects.
  public RobotDrivetrain(){} //constructor
  
  //Method for arcade drive - used in Dpad controls and autonomous mode
  //xSpeed gives the forward/backward
  //zRotation gives the left/right
  //Values run from -1.0 to 1.0
  //Use a value of 0 if you want robot to be stationary in particular direction
  public void arcadeDrive(double xSpeed, double zRotation){
    robotDrive.arcadeDrive(xSpeed, zRotation);
  }

  //Method for tank drive - not currently used
  //leftSpeed (-1.0 to 1.0) - comes from left stick
  //rightSpeed (-1.0 to 1.0) - comes from right stick
  public void tankDrive(double leftSpeed, double rightSpeed){
    robotDrive.tankDrive(leftSpeed, rightSpeed);
  }

  //Method for curvature drive - used for main drivetrain controls on robot
  //Right side motor controllers are inverted to ensure that both sides of the bot move in the same direction when desired
  //xSpeed (0 to 1.0) to go forward and (-1.0 to 0) to move backward
  //zRotation (-1.0 to 1.0) controls direction
  //isQuickTurn (fix me) - needs tested - change to false to remove
  public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn){
    rightFront.setInverted(true);
    rightRear.setInverted(true);
    robotDrive.curvatureDrive(xSpeed, zRotation, isQuickTurn);
  }

  /*
  * D-Pad setup - these are used for small movements
  * 0 = up arrow (forward)
  * 90 = right arrow (right)
  * 180 = down arrow (backward)
  * 270 = left arrow (left)
  * speeds can be adjusted
  * first parameter is the forward/backward speed
  * Setting equal to 0 for left/right ensures it stays in place
  */
  public void dPadGetter(int dPad){
    if (dPad==0){
      robotDrive.arcadeDrive(0.5, 0); //forward
    }
    if (dPad==90){
      robotDrive.arcadeDrive(0, 0.5); //right
    }
    if (dPad==180){
      robotDrive.arcadeDrive(-0.5, 0); //reverse
    }
    if (dPad==270){
      robotDrive.arcadeDrive(0, -0.5); //left
    }
  }
  //This method should reset the built-in encoders on the SPARK MAX speed controllers to 0
  //Needs Tested
  //How can we utilize the encoders? What can they do? - EG 1.15.22
  public void resetEncoders(){
    leftFront.getEncoder().setPosition(0);
    leftRear.getEncoder().setPosition(0);
    rightFront.getEncoder().setPosition(0);
    rightRear.getEncoder().setPosition(0); 
  }
}