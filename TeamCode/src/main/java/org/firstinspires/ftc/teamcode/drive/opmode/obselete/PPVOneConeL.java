package org.firstinspires.ftc.teamcode.drive.opmode.obselete;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.opmode.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import org.firstinspires.ftc.teamcode.drive.ArcturusDriveNoRR;

import java.util.ArrayList;

@Disabled
@Autonomous(group = "drive")
public class PPVOneConeL extends LinearOpMode
{
    private ArcturusDriveNoRR drive;
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;
    private DcMotorEx lift, leftFront, leftRear, rightRear, rightFront;
    private Servo claw;


    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int IDTOI1 = 0;
    int IDTOI2 = 1;
    int IDTOI3 = 2;// Tag ID 18 from the 36h11 family

    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {
        //
        // intaketilt = hardwareMap.get(Servo.class, "ringpusher");
        drive = new ArcturusDriveNoRR(hardwareMap);
        lift =  hardwareMap.get(DcMotorEx.class, "leftShooter");
        lift.setTargetPosition(80);
        lift.setPower(1);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        claw = hardwareMap.get(Servo.class, "claw");
        claw.setPosition(0.9);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */




        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == IDTOI1 || tag.id == IDTOI2 || tag.id == IDTOI3)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }


        claw.setPosition(0.9);
        sleep(1000);
        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */
        lift.setTargetPosition(80);
        lift.setPower(0.8);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(2000);

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
            leftFront.setPower(0.3);
            rightFront.setPower(0.3);
            rightRear.setPower(0.3);
            leftRear.setPower(0.3);
            sleep(1500);
        }
        //START SCORING CONE
        drive.goingRight((1200*5/3)+400,0.3);
        sleep(1000);

        drive.goingBackward(200,0.5);
        sleep(1000);

        drive.goingForward((800*5/3)+300,0.3);
        sleep(1000);
        drive.goingRight((600*5/3)+300,0.3);
        sleep(1000);

        lift.setTargetPosition(5100);
        lift.setPower(1.0);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(3000);

        drive.goingForward((175*5/3)+25,0.3);
        sleep(1000);

        lift.setTargetPosition(3500);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(0.8);
        sleep(3000);

        claw.setPosition(0.7);
        sleep(1000);

        drive.goingBackward((175*5/3)+25,0.3);

        lift.setTargetPosition(80);
        lift.setPower(1);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sleep(3000);
        /* Actually do something useful */
        //if(tagOfInterest == null){
        // leftFront.setPower(0.1);
        //rightFront.setPower(0.1);
        //rightRear.setPower(0.1);
        //leftRear.setPower(0.1);
        //sleep(1000);
        /*
         * Insert your autonomous code here, presumably running some default configuration
         * since the tag was never sighted during INIT
         */
        //}
        //else
        //
        if(tagOfInterest.id == IDTOI1) {
            drive.turnLeft(1300,0.3);
            sleep(1000);
            drive.goingForward(2000,0.5);
        }
        else if(tagOfInterest.id == IDTOI2){
            drive.turnLeft(1300,0.3);
            sleep(1000);
            drive.goingForward(1100,0.5);
        }
        else {
            drive.turnLeft(1300,0.3);
            sleep(1000);
            drive.goingForward(300,0.5);
        }
        /*
         * Insert your autonomous code here, probably using the tag pose to decide your configuration.
         */


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        // while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}