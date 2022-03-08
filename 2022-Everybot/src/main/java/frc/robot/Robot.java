/*
*The purpose of this code is to design the base drivetrain code for The Bionic Warrios (6834)
*The current drivetrain design (2021-2022) is made up of four NEO brushless motors, two per side 
*and four Spark MAX speed controllers.
*This code contains functionality for tank drive AND curvature drive. To pick the desired drive,
*comment out the code that should NOT be used. -EG 9/2/21
*/

package frc.robot;

//Imports
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; //not sure what this is for - EG
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController; //improved functionality for xbox controller use
import edu.wpi.first.util.sendable.SendableRegistry; //allows us to add info we select to dashboard


public class Robot extends TimedRobot { 
  
  //Initializations
  private double startTime; // used for timer in autonomous mode find in autoInit
  //Conroller0 controls entire Everybot - drivetrain and subsystems
  private XboxController controller0 = new XboxController(0); //0 refers to USB port # - left side
  //The drivetrain object and dot operators will be called upon when accessing RobotDrivetrain methods
  private RobotDrivetrain drivetrain = new RobotDrivetrain();
  //The sub object and dot operators will be called upon when accessing Subsystem methods
  private Subsystem sub = new Subsystem();
   
  @Override
  public void robotInit() {    //This method only runs once when the code first starts
    //Sets encoder positions to 0
    drivetrain.resetEncoders();
  }

  @Override
  public void robotPeriodic() {    
    /*
    //Where should this go?
    //Should put encoder position values on shuffleboard - EG    
    SmartDashboard.putNumber("Front Left Encoder", leftFront.getEncoder().getPosition());
    SmartDashboard.putNumber("Front Right Encoder", rightFront.getEncoder().getPosition());
    SmartDashboard.putNumber("Rear Left Encoder", leftRear.getEncoder().getPosition());
    SmartDashboard.putNumber("Rear Right Encoder", rightRear.getEncoder().getPosition());
    //Can add in intakes, pneumatics and other objects as needed. Follow format.
    SendableRegistry.add(robotDrive, "drive"); 
    */
  }
  
  @Override
  public void autonomousInit() {
    //Should give the time since auto was initialized
    startTime = Timer.getFPGATimestamp(); //used in auto periodic 
  }

  @Override
  public void autonomousPeriodic() {    
    //Does this give the time since the robot was turned on or the time since auto was started??? - EG 9/2/21
    double time  = Timer.getFPGATimestamp();
    //Not sure why time-startTime works the way it does -EG 9/2/21
    //Speeds go between 0 and 1
    //Set at 50% speed right now
    //Code has robot move forward for 1 second
    if (time - startTime < 1){
      drivetrain.arcadeDrive(0.5, 0);
    }
    else{
      drivetrain.arcadeDrive(0, 0);
    }
  }

  @Override
  public void teleopInit() {}
  
  @Override
  public void teleopPeriodic() {
    //Comment out the code that you don't want to use - pick tankDrive or curvatureDrive
    //D-pad functionality works regardless of drive type chosen

    //Tank Drive
    //Need y-axis for each stick
    //Hand.kLeft gives the left analog stick and Hand.kRight gives the right analog stick
    //Speeds are currently set at 50%
    //drivetrain.tankDrive(-0.5*controller.getLeftY(), -0.5*controller.getRightY()); 
    
    //Curvature Drive  
    double fSpeed = controller0.getRightTriggerAxis(); //forward speed from right trigger
    double rSpeed = controller0.getLeftTriggerAxis(); //reverse speed from left trigger
    double turn = controller0.getLeftX(); //gets the direction from the left analog stick
    boolean quickTurn = controller0.getLeftBumper(); //makes quick turn if left bumper is pressed
    if (fSpeed > 0){
      drivetrain.curvatureDrive(fSpeed, turn, quickTurn); // if quickTurn doesn't work, change to false
    }
    else if (rSpeed > 0){
      drivetrain.curvatureDrive(-1*rSpeed, turn, quickTurn);
    }
        
    int dPad = controller0.getPOV(); //scans to see which directional arrow is being pushed
    drivetrain.dPadGetter(dPad);

    //Arm
    boolean armUp = controller0.getXButton();
    boolean armDown = controller0.getYButton();
    sub.everyBotArm(armUp, armDown);

    //Intake
    boolean intakeIn = controller0.getAButton();
    boolean intakeOut = controller0.getBButton();
    sub.everyBotArm(intakeIn, intakeOut);
  }
  

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}