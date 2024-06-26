// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AmpAddOn.AmpResetTargetAngle;
import frc.robot.commands.BarrelPivot.ResetTargetAngle;
import frc.robot.commands.Lights.MakeCool;
import frc.robot.commands.Lights.MakeRainbow;
import frc.robot.subsystems.BarrelPivot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Vision;
import frc.robot.testingdashboard.TDNumber;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private MakeRainbow m_makeRainbow;
  private MakeCool m_makeCool;
  private ResetTargetAngle m_resetTargetAngle;
  private AmpResetTargetAngle m_ampResetTargetAngle;

  private Vision m_vision;
  private RobotContainer m_robotContainer;

  private boolean m_endProcessingDone = false;
  private double m_teleopStartTime;
  private double m_elapsedTeleopTime;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    m_vision = Vision.getInstance();

    m_makeRainbow = new MakeRainbow();
    m_makeCool = new MakeCool();
    m_resetTargetAngle = new ResetTargetAngle();
    m_ampResetTargetAngle = new AmpResetTargetAngle();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    if (m_makeCool != null) {
      m_makeCool.cancel();
    }

    m_makeRainbow.schedule();
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

    if (m_makeRainbow != null) {
      m_makeRainbow.cancel();
    }
    
    m_makeCool.schedule();
    if (RobotMap.V_ENABLED) {
      m_vision.enablePoseUpdates();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    if (m_makeRainbow != null) {
      m_makeRainbow.cancel();
    }

    if (m_makeCool != null) {
      m_makeCool.cancel();
    }

    if (RobotMap.BP_ENABLED) {
      m_resetTargetAngle.schedule();
    }
    if (RobotMap.A_PIVOT_ENABLED) {
      m_ampResetTargetAngle.schedule();
    }

    if (RobotMap.V_ENABLED) {
      m_vision.enablePoseUpdates();
    }

    m_teleopStartTime = Timer.getFPGATimestamp();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    m_elapsedTeleopTime = Timer.getFPGATimestamp() - m_teleopStartTime;
    if(!m_endProcessingDone &&
       m_elapsedTeleopTime > Constants.END_MATCH_TIME_S) {
        doEndMatchProcessing();
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}

  private void doEndMatchProcessing() {
    printSpeakerHeightOffset();

    m_endProcessingDone = true;
  }

  private void printSpeakerHeightOffset(){
      TDNumber speakerHeightOffset = new TDNumber(BarrelPivot.getInstance(), "Auto Pivot", "Speaker Height Offset (meters)");
      if(speakerHeightOffset.get() != Constants.SPEAKER_HEIGHT_OFFSET) {
        System.out.println("Adjusted Speaker Height Offset = " + speakerHeightOffset.get());
      }
  }
}
