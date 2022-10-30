package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.ArcturusDrive;

@Autonomous(group = "drive")
public class TwoConeAutoL extends LinearOpMode {
    private ElapsedTime     runtime = new ElapsedTime();

    private static final Pose2d ORIGIN = new Pose2d(-63.0, -56.0, 0.0);

    private ArcturusDrive drive;
    private double number;
    private DcMotorEx lift,leftFront, leftRear, rightRear, rightFront;
    private Servo claw;
    private long freightboxdelay;
    double WorkingMotorMax = 0.6825;
    // private ArcturusVision vision;

    @Override
    public void runOpMode() {
        number = 0.5;
        // Drive
        drive = new ArcturusDrive(hardwareMap);
        //
        // intaketilt = hardwareMap.get(Servo.class, "ringpusher");
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        claw = hardwareMap.get(Servo.class, "claw");
        claw.setPosition(0.9);

        //leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("sdjflkasfjsdlaf", "press play or you have 5 seconds to live");
        telemetry.update();
        waitForStart();


        //intaketilt.setPosition(0.58);

        //moving right to our substation
        drive.setMotorPowers(0.3,-0.4,0.3,-0.3);
        sleep(1750);

        //moving forward
        drive.setMotorPowers(0.3,0.3,0.3,0.3);
        sleep(1750);

        //moving a small bit right
        drive.setMotorPowers(0.3,-0.4,0.3,-0.3);
        sleep(750);

        //lift  up
        lift.setTargetPosition(4400);
        lift.setPower(0.5);

        //go forward to score
        drive.setMotorPowers(0.1,0.1,0.1,0.1);
        sleep(1000);

        //open claw
        claw.setPosition(0.65);
        sleep(1000);

        //go backward
        drive.setMotorPowers(-0.1,-0.1,-0.1,-0.1);
        sleep (1000);

        //going left cone stacks
        drive.setMotorPowers(0.3, -0.4, 0.3, -0.3);
        sleep(3000);

        //lift claw to grabbing position
        lift.setTargetPosition(0);
        lift.setPower(1687.5);

        //going forward
        drive.setMotorPowers(0.3, 0.4, 0.3, 0.3);
        sleep(1200);

        //close claw
        claw.setPosition(9);
        sleep(1000);

        //going backward
        drive.setMotorPowers(-0.3, -0.4, -0.3, -0.3);
        sleep(1200);

        //lift claw to dropping position
        lift.setTargetPosition(4400);
        lift.setPower(0.5);

        //going right to pole
        drive.setMotorPowers(-0.3, 0.4, -0.3, 0.3);
        sleep(3000);

        //going forward
        drive.setMotorPowers(0.3, 0.4, 0.3, 0.4);
        sleep(1000);

        //opening claw
        claw.setPosition(0.65);
        sleep(1000);

        //going backward
        drive.setMotorPowers(-0.3, -0.4, -0.3, -0.3);
        sleep(1000);

        //going right a bit
        drive.setMotorPowers(0.3, -0.4, 0.3, -0.3);
        sleep(1000);

        //Going backward
        drive.setMotorPowers(-0.3, -0.4, -0.3, -0.3);
        sleep(3000);


    }


}


