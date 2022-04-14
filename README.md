# 2022 FRC 1711 Euphrosyne Robot Code

[![Build Status](https://github.com//frc1711/Euphrosyne/actions/workflows/main.yml/badge.svg)](https://github.com//frc1711/Euphrosyne/actions/workflows/main.yml)

This repo will hold the 2022 FRC 1711 Raptors robot code.

## Controls

### Drive Controller
![](./info/drive-controller.png)

### Central Controller
![](./info/central-controller.png)

## Auton Paths
### AutoTaxi
Moves out of the tarmac.

- Initial Position: The robot can be positioned in any way such that the shooter initially faces the hub.

### AutoLowGoalTaxi
Shoots a single cargo ball into the hub, and then performs `AutoTaxi`, moving out of the tarmac.

- Initial Position: The robot can be positioned in any way such that it can immediately shoot into the hub.

### AutoTwoBallSensor
Shoots a single cargo ball into the hub, then drives away from the hub, running the intake and cargo handler
until a ball is detected. Once a ball is detected, it drives directly back to the hub and shoots the second ball.

- Initial Position: The robot can be positioned in any way such that it can immediately shoot into the hub and
would run over the second ball if it were to drive directly away from the hub.

- Functional Notes: The ball is detected through measuring changes in the cargo handler's torque, so if something
comes into contact with the cargo handler this auton may not function properly.

- Usage Notes: The auton will drive and search for a cargo ball for a maximum of 8 seconds before it turns around.
Also, it may take a fraction of a second after the ball passes through intake before it is detected--for this reason,
this auton isn't a good option if the robot needs to turn around precisely when the cargo passes through intake. **In
the case that the robot is positioned on the part of the tarmac where the wall is directly behind the second ball it
will attempt to intake, `AutoTwoBallWall` is recommended instead.**

### AutoTwoBallWall
Shoots a single ball into the hub, drives away, turns and picks up a ball, turns back, and shoots.

- Initial Position: The robot should be positioned such that it is squared against the hub, with the shooter-side bumper
being about five inches away (the shooter faces the hub).

- Usage Notes: While this auton is a good option when `AutoTwoBallSensor` isn't available due to the wall being too close
to the second ball, otherwise, `AutoTwoBallSensor` should be used. See `AutoTwoBallSensor`'s usage notes for distinguishing
between when to use `AutoTwoBallWall` versus `AutoTwoBallSensor`.

### AutoTrifecta
Not currently functioning, but meant to shoot a single ball into the hub, drive back, pick up two more, then shoot them both.

- Initial Position: The robot should be positioned such that it is squared against the hub, with the shooter-side bumper
being about five inches away (the shooter faces the hub).

- Functional Notes: This auton can take around 13 to 14 seconds in total, so `AutonWaitPeriod` should be set to zero whenever
it is used.

- Usage Notes: Not currently functioning. Also tends to accumulate a decent amount of positional error over time as slight
errors in the auton paths compound and have the potential to make the final path unreliable.

## When making changes/updates/improvements

Please create your changes in a branch other then the main line Development branch, and do a PR to merge your commits back in.  Kind of like creating this readme.md file.  This will it's own branch, and then using a PR merge it into the base repo.

## Creating source code README files

Started creating a README.md file for the folders in src\main.  Just do document what each folder and file does.  But not to interfere with @gabrielseaver9678 code files, and force merges and such.  Really would like to make 

## The code
### Auton Code
[Auton ReadMe](./src/main/java/frc/robot/commands/auton/)
