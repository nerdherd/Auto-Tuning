package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autotuning{
    public static double voltagDouble(TalonFX AutotuningMotor, double Voltage){
        if(Voltage > AutotuningMotor.getBusVoltage()){
            SmartDashboard.putString("Warning", "The Voltage is greater than the battery can provide");
            return 0.0;
        }
        else{
            return Voltage/AutotuningMotor.getBusVoltage();
        }
    }
    public static double voltagDouble(TalonSRX AutotuningMotor, double Voltage){
        if(Voltage > AutotuningMotor.getBusVoltage()){
            SmartDashboard.putString("Warning", "The Voltage is greater than the battery can provide");
            return 0.0;
        }
        else{
            return Voltage/AutotuningMotor.getBusVoltage();
        }
    }
}