// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  public static double voltage = 0;
  private TalonFX AutoTalonFX = new TalonFX(17);
  private TalonSRX AutoTalonSRX = new TalonSRX(0);
  private CommandPS4Controller ps4Controller = new CommandPS4Controller(0);

  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    AutoTalonFX.setInverted(true);
    AutoTalonFX.setNeutralMode(NeutralMode.Brake);
    
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // SmartDashboard.putNumber("Voltage Cap", 0);
    SmartDashboard.putNumber("Seconds Increment", 0);
    // SmartDashboard.putNumber("VoltageFX", 0);
    // SmartDashboard.putData("AutoTuning TalonFX", new RunCommand( () -> AutoTalonFX.set(ControlMode.PercentOutput, Autotuning.voltagDouble(AutoTalonFX, SmartDashboard.getNumber("VoltageFX", 0)))));
    // SmartDashboard.putData("Off", new RunCommand( () -> AutoTalonFX.set(ControlMode.PercentOutput, 0)));
    ps4Controller.R2().whileTrue(increment());
    ps4Controller.R2().onFalse(Commands.runOnce(() -> AutoTalonFX.set(ControlMode.PercentOutput, 0)));
    ps4Controller.L2().whileTrue(decrement());
    ps4Controller.L2().onFalse(Commands.runOnce(() -> AutoTalonFX.set(ControlMode.PercentOutput, 0)));
    // SmartDashboard.putNumber("VoltageSRX", 0);
    // SmartDashboard.putData("AutoTuning TalonSRX", new RunCommand(() -> AutoTalonSRX.set(ControlMode.PercentOutput, Autotuning.voltagDouble(AutoTalonSRX, SmartDashboard.getNumber("VoltageSRX", 0)))));
    // SmartDashboard.putData("Off", new RunCommand( () -> AutoTalonSRX.set(ControlMode.PercentOutput, 0)));
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }


  public Command increment(){
    final double finalDouble = SmartDashboard.getNumber("Seconds Increment", 0);

    return new ParallelCommandGroup(
      Commands.repeatingSequence(
        Commands.waitSeconds(finalDouble),
        Commands.runOnce(() -> voltage += 0.05)
      ),
      Commands.run(() -> AutoTalonFX.set(ControlMode.PercentOutput, Autotuning.voltagDouble(AutoTalonFX, voltage)))
    );
  }
  public Command decrement(){
    final double finalDouble = SmartDashboard.getNumber("Seconds Increment", 0);

    return new ParallelCommandGroup(
      Commands.repeatingSequence(
        Commands.waitSeconds(finalDouble),
        Commands.runOnce(() -> voltage -= 0.05)
      ),
      Commands.run(() -> AutoTalonFX.set(ControlMode.PercentOutput, Autotuning.voltagDouble(AutoTalonFX, voltage)))
    );
  }
}