package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import org.firstinspires.ftc.robotcore.external.Func;
import java.util.Base64;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Auto")
public class Autonomous extends LinearOpMode {
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;

    public void forwardForDistance (double inches) {
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        double wheelDiameter = 3.78; // inches
        double wheelCircumference = wheelDiameter * Math.PI;
        double rotation = inches/wheelCircumference;
        double ticksPerRevolution = 537.7;
        int ticks = (int) (rotation * ticksPerRevolution);

        frontLeftMotor.setPower(1);
        backLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        backRightMotor.setPower(1);

        frontLeftMotor.setTargetPosition(ticks);
        frontRightMotor.setTargetPosition(ticks);
        backLeftMotor.setTargetPosition(ticks);
        backRightMotor.setTargetPosition(ticks);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (frontLeftMotor.isBusy() || frontRightMotor.isBusy() || backLeftMotor.isBusy() || backRightMotor.isBusy()) {

        };

        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);

    }

    @Override
    public void runOpMode() throws InterruptedException {
        this.backLeftMotor = hardwareMap.get(DcMotor.class, "LBMotor");
        this.backRightMotor = hardwareMap.get(DcMotor.class, "RBMotor");
        this.frontLeftMotor = hardwareMap.get(DcMotor.class, "LFMotor");
        this.frontRightMotor = hardwareMap.get(DcMotor.class, "RFMotor");

        this.frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        this.frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();
        while (opModeIsActive()) {
            forwardForDistance(10); //----------------------------------------------------------------------------------------------------------
        }
    }
}
