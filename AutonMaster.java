package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.firstinspires.ftc.teamcode.tuning.TuningOpModes;

import java.util.List;


@Autonomous(name="AutonMaster 13217", group="Autonomous")

public final class AutonMaster extends LinearOpMode {

    @Override
    public void runOpMode() {
        VisionPortal.Builder build = new VisionPortal.Builder();
        build.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        OurRobot myRobot = new OurRobot(build);

        String teamPropLocation = new String("notFound");
        while (!isStarted() && !isStopRequested()) {
            if (teamPropLocation.equals("notFound")) {
                teamPropLocation = myRobot.telemetryTfod();
            }
            telemetry.addData("team prop location: ", teamPropLocation);
            telemetry.addData("go to: ", teamPropLocation);
            telemetry.update();
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
            waitForStart();


            //x is always -25
            //y for left, middle, right, respectively
            //-15, 0, 10

            if (teamPropLocation == "left"){
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .strafeToConstantHeading(new Vector2d(20,-15))
                                .build()
                );
            }
            if (teamPropLocation == "notFound"  || teamPropLocation == "middle"){
                Actions.runBlocking(    
                        drive.actionBuilder(drive.pose)
                                .strafeToConstantHeading(new Vector2d(20,0))
                                .build()
                );
            }
            if (teamPropLocation == "right"){
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .strafeToConstantHeading(new Vector2d(20,10))
                                .build()
                );
            }
        }
    }
}

//TODO: clean up useless comments and remove extra redundancy that may cause bugs

class OurRobot{
    private DcMotor lf = null;
    private DcMotor lb = null;
    private DcMotor rf = null;
    private DcMotor rb = null;
    private DcMotor arm = null;

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private static final String TFOD_MODEL_ASSET = "MyModelStoredAsAsset.tflite";
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/TeamPropTest2CenterStageModel.tflite";
    private static final String[] LABELS = {"prop", "p"};
    private TfodProcessor tfod;
    private VisionPortal visionPortal;
    private VisionPortal.Builder builder;


    //This constructor takes in the motors which are of class DcMotor
    public OurRobot(VisionPortal.Builder build1){
        builder = build1;
        initTfod();
    }


    //Tensorflow Object Detection functions:
    public void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()
                .setModelFileName(TFOD_MODEL_FILE)
                .setModelLabels(LABELS)
                .build();
        builder.addProcessor(tfod);
        visionPortal = builder.build();
    }

    public String telemetryTfod() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();

        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop() + recognition.getBottom()) / 2;
            if (x <= 190) {
                return "left";
            } else if (x <= 465) {
                return "middle";
            } else {
                return "right";
            }
        }
        return "notFound";
    }
}
