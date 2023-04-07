package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseTalon;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Autotuning extends SubsystemBase{

    // Bind these in Robot Container
    // ps4Controller.R2().whileTrue(_____.increment());
    // ps4Controller.R2().onFalse(Commands.runOnce(() -> _____.setPower(0)));
    // ps4Controller.L2().whileTrue(_____.decrement());
    // ps4Controller.L2().onFalse(Commands.runOnce(() -> _____.setPower(0)));

    public static double voltage = 0;
    private BaseTalon talon;

    // FX Port 17
    public Autotuning(boolean isFX, int port) {
        if(isFX) {
            talon = new TalonFX(port);
        }
        else {
            talon = new TalonSRX(port);
        }

        talon.setInverted(true);
        talon.setNeutralMode(NeutralMode.Brake);
        talon.configVoltageCompSaturation(11, 0);
        talon.enableVoltageCompensation(true);

        SmartDashboard.putNumber("Seconds Increment", 0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("AT Supply Percent", voltage);
        SmartDashboard.putNumber("AT Supply Voltage", talon.getMotorOutputVoltage());
        SmartDashboard.putNumber("AT Stator Current", talon.getStatorCurrent());
        SmartDashboard.putNumber("AT Velocity", talon.getSelectedSensorVelocity());
    }

    public double voltageDouble(double voltage){
        if(voltage > talon.getBusVoltage()){
            SmartDashboard.putString("Warning", "The Voltage is greater than the battery can provide");
            return 0.0;
        }
        else{
            return voltage/talon.getBusVoltage();
        }
    }

    public Command increment(double seconds){
        return new ParallelCommandGroup(
          Commands.repeatingSequence(
            Commands.waitSeconds(seconds),
            Commands.runOnce(() -> voltage += 0.05)
          ),
          Commands.run(() -> talon.set(ControlMode.PercentOutput, voltage))
        ).finallyDo((xD) -> setPower(0));
      }

    public Command decrement(double seconds){
    return new ParallelCommandGroup(
        Commands.repeatingSequence(
            Commands.waitSeconds(seconds),
            Commands.runOnce(() -> voltage -= 0.05)
        ),
        Commands.run(() -> talon.set(ControlMode.PercentOutput, voltage))
    ).finallyDo((xD) -> setPower(0));
    }

    // Rename to "setPowah"
    public void setPower(double power) {
        voltage = 0;
        talon.set(ControlMode.PercentOutput, power);
    }
}