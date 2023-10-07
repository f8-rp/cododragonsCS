
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Auton Master", group="Linear OpMode")

public class AutonMaster extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    @Override
    public void runOpMode() {

        //get the hardware configurations
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightRear");
        
        //set direction of motors
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();
        //create an object of OurRobot class called myRobot such that myRobot has access to all methods that 
        //OurRobot can do
        //create it with the constructor that takes in all the configured motor objects. 
        OurRobot myRobot = new OurRobot(leftFrontDrive,leftBackDrive,rightFrontDrive,rightBackDrive);
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //test the move and stop method that is in the OurRobot function through the myRobot object.
            myRobot.move();
            sleep(2000);
            myRobot.stop();
            sleep(2000);
            
            
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}


//create a class for our robot functions called OurRobot
class OurRobot{
    
    private DcMotor lf = null;
    private DcMotor lb = null;
    private DcMotor rf = null;
    private DcMotor rb = null;


    //This constructor takes in the motors which are of class DcMotor
    public OurRobot(DcMotor leftFront, DcMotor leftBack, DcMotor rightFront, DcMotor rightBack){
        lf = leftFront; 
        lb = leftBack;
        rf = rightFront;
        rb = rightBack;
    }
    
    //functions that OurRobot can do :
    void move(){
        lf.setPower(0.2);
        rf.setPower(0.2);
        lb.setPower(0.2);
        rb.setPower(0.2);
        
    }
    void stop(){
        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
    }

}

