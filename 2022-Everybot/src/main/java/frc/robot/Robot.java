/*
*The purpose of this code is to design the Everybot code for The Bionic Warriors (6834)
*The current drivetrain design (2021-2022) is made up of four NEO brushless motors, two per side 
*and four Spark MAX motor controllers
*There are two additional brushed motors, both using Spark MAX motor controllers.
*One of these motors controls the intake/shooting mechanism and the other controls the arm that
*lifts the intake/shooter up and down.
*/

package frc.robot;

//Imports
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; //not sure what this is for - EG
import edu.wpi.first.wpilibj.XboxController; //improved functionality for xbox controller use
import edu.wpi.first.util.sendable.SendableRegistry; //allows us to add info we select to dashboard
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.cameraserver.CameraServer;



public class Robot extends TimedRobot { 
  
  //Initializations
  private double startTime; // used for timer in autonomous mode find in autoInit
  //Conroller0 controls entire Everybot - drivetrain and subsystems
  private XboxController controller0 = new XboxController(0); //0 refers to USB port # - left side
  //The drivetrain object and dot operators will be called upon when accessing RobotDrivetrain methods
  private RobotDrivetrain drivetrain = new RobotDrivetrain();
  //The sub object and dot operators will be called upon when accessing Subsystem methods
  //private Subsystem sub = new Subsystem();
  private CANSparkMax everyBotIntakeMotor = new CANSparkMax(9, MotorType.kBrushed);
  private CANSparkMax everyBotArmMotor = new CANSparkMax(12, MotorType.kBrushless); 
  private int invert = 1;
  private int invertBuffer = 0;

  @Override
  public void robotInit() {    //This method only runs once when the code first starts
    //Sets encoder positions to 0
    drivetrain.resetEncoders();
    CameraServer.startAutomaticCapture();
    everyBotArmMotor.setIdleMode(IdleMode.kBrake);
    everyBotArmMotor.set(0.12);
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
    everyBotArmMotor.set(0.12);
  }

  @Override
  public void autonomousPeriodic() {    
    //Does this give the time since the robot was turned on or the time since auto was started??? - EG 9/2/21
    double time  = Timer.getFPGATimestamp();
    
    //First two seconds of auto
    //Keeps arm raised and shoots preloaded cargo
    if (time - startTime < 2){
      everyBotArmMotor.set(.12);
      everyBotIntakeMotor.set(1);
    }
    //Sec 2-5
    //Stops intake motor and reverses
    else if(time - startTime < 5){
      drivetrain.curvatureDrive(0.2, 0, -1);
      everyBotIntakeMotor.set(0);
      everyBotArmMotor.set(0.08);
    }
    //Sec 5-6
    //Turns around
    else if(time - startTime <6){
      drivetrain.curvatureDrive(0.2, 1, -1);
    }
    //End of auto mode
    //Lowers arm
    else{
      everyBotArmMotor.set(-.15);
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
    
    if (controller0.getLeftBumper()&& invert == 1&& invertBuffer<1){
      invert = -1;
      invertBuffer = 20;
    }else if(controller0.getLeftBumper()&&invertBuffer<1){
      invert = 1;
      invertBuffer = 20;
    }else if(invertBuffer>=1){
      invertBuffer--;
    }
    
    if (fSpeed > 0){
      drivetrain.curvatureDrive(fSpeed*.65, turn, invert);
    }
    else if (rSpeed > 0){
      drivetrain.curvatureDrive(-1*rSpeed*.65, turn, invert);
    }
    
    
    //D-Pad controls for fine movements
    int dPad = controller0.getPOV(); //scans to see which directional arrow is being pushed
    drivetrain.dPadGetter(dPad);

    //Arm
    boolean armUp = controller0.getXButton();
    boolean armDown = controller0.getYButton();
    if(armUp){
      everyBotArmMotor.set(0.12);
    }
    if(armDown){
      everyBotArmMotor.set(-0.15);
    }
    if(controller0.getRightBumper()){
      everyBotArmMotor.set(0);
    }

    //Intake
    boolean intakeIn = controller0.getAButton();
    boolean intakeOut = controller0.getBButton();
    //sub.everyBotArm(intakeIn, intakeOut);
    if(intakeIn){
      everyBotIntakeMotor.set(-0.5);
    }
    else if(intakeOut){
      everyBotIntakeMotor.set(1);
    }
    else{
      everyBotIntakeMotor.set(0);
    }    
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