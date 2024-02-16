// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AmpAddOn;

import frc.robot.subsystems.AmpAddOn;
import frc.robot.testingdashboard.Command;
import frc.robot.Constants;


public class UNScoreAmp extends Command {
  AmpAddOn m_AmpAddOn;
  
  /** Creates a new UNScoreAmp. */
  public UNScoreAmp() {
    super(AmpAddOn.getInstance(), "Basic", "UNScore");
    m_AmpAddOn = AmpAddOn.getInstance();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_AmpAddOn);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_AmpAddOn.spinIn(Constants.AMP_SPEED);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_AmpAddOn.spinStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
