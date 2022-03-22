# Auton files and folders

What do these files and folders do for the robot.  @gabrielseaver9678 did a really nice job on these.


## base folder

This folder contains all the individual auton commands

## Files

* **_AutoLowGoalTaxi.java_** - Is a SequentialCommandGroup method with all the commands needed to run a lowgoal, and then taxi off the tarmac. 
* **_AutoTaxi.java_** - SequentialCommandGroup method to roll the robot off the tarmac.
* **_AutoTwoBallSensor.java_** - SequentialCommandGroup, one of the more complicated autons, shoots the loaded cargo into the low goal, runs the intake and drives to another cargo, returns to the hub and shots the low goal again.
