L2G2Members:
Zinah Al-Najjar, 101108056
Mack Wallace, 101104366
Vilmos Feher, 101168919
Shawaiz Khan, 100917863
Faris Abo-Mazeed, 101115920

Dear user,Thank you for using our elevator program whichwill allow you to simulate an operating elevator!

Contents:

Elevator.java 
ElevatorSubsystem.java - old code 
FloorMovementData.java 
FloorRequest.java
FloorRequestData.java
FloorSubsystem.java - old code 
FloorTest.java
MainElevatorSys.java 
MainFloorSys.java
MainSchedulerSys.java
Motor.java
Output.java
ResponseData.java
SchedulerSubsystem.java- old code 
InputInformation-invalid.txt
InputInformation-ORIGINAL.txt
InputInformation.txt
Request-Flow.txt



Setup:Download the zip file from Github or create anempty project and pull the project. Once it has beenpulled successfully, go to package explorer and follow the path"Elevator System-> src -> default package -> simulation". This willopen the main class and allow you to run the program.

to Run the program follow the order: 

1) Run MainSchedulerSys as java application 
2) Run MainElevatorSys as java application 
3) Run MainFloorSys as java application 

Testing:Test cases inside floorTest

Breakdown of responsibilities iteration 1:

code implementation: Zinah, Vilmos, Mack 
UML diagram: Shawaize
Sequnce diagram: Zinah, Vilmos
Readme: Vilmos
Test classes: Mack, Faris 
JavaDoc: Zinah 


Breakdown of responsibilities iteration 2:

code implementation: Zinah, Mack 
UML diagram: Shawaize
Sequnce diagram: Zinah, Vilmos- stayed the same as Iteration 1 
Readme: Vilmos
Test classes: Mack, Faris- stayed the same as Iteration 1 
JavaDoc: Faris  
State Machine Diagrams: Zinah, Mack, Vilmos, Shawaize



Breakdown of responsibilities iteration 3:

code implementation: Zinah, Mack 
UML diagram: Vilmos 
Sequnce diagram: Zinah
Readme: Zinah 
Test classes: Mack, Faris - stayed the same as Iteration 1 
JavaDoc: Zinah 
Fixing State Machine code from Iteration 2: Zinah 
*Fixing output format: Zinah 

*Vilmos attempted to fix the output format but the github push created an issue with the project structure so we couldn't use his code. 

Known issues that must be addressed before next iteration: 
The elevator threads is moving from its current floor to the origin floor (floor where the user is currently at) but it is not moving from orgin floor (floor where the user is currently at) to the destination floor (requested by the user)


GitHub repository link:

https://github.com/zinahalnajjar/Elevator-System-Project---SYSC-3303

