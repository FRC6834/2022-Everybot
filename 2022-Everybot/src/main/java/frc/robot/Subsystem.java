package frc.robot;

//Imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//Subsystem class is general - specific methods for each subsystem
public class Subsystem {
    
    //Class Variables
    private CANSparkMax everyBotIntakeMotor = new CANSparkMax(6, MotorType.kBrushed);
    private CANSparkMax everyBotArmMotor = new CANSparkMax(7, MotorType.kBrushed);    
    
    //Constructor will be called in Robot.java to create Subsystem object
    public Subsystem(){}

    //Code for everyBot intake
    public void everyBotIntake(boolean in, boolean out){
        if(in){
            everyBotIntakeMotor.set(0.5);
        }
        else if(out){
            everyBotIntakeMotor.set(-0.5);
        }
        else{
            everyBotIntakeMotor.set(0);
        }
    }    
    //Code for everyBot arm
    public void everyBotArm(boolean up, boolean down){
        if(up){
            everyBotArmMotor.set(0.1);
        }
        else if(down){
            everyBotArmMotor.set(-0.1);
        }
        else{
            everyBotArmMotor.set(0);
        }
    }
}