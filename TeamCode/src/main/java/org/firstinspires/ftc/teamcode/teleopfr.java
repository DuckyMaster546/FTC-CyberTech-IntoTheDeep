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


@TeleOp(name = "TeleOp")
public class teleopfr extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        //Drive Motors
        DcMotorEx motorFrontLeft = hardwareMap.get(DcMotorEx.class, "LFMotor");
        DcMotorEx motorBackLeft = hardwareMap.get(DcMotorEx.class, "LBMotor");
        DcMotorEx motorFrontRight = hardwareMap.get(DcMotorEx.class, "RFMotor");
        DcMotorEx motorBackRight = hardwareMap.get(DcMotorEx.class, "RBMotor");

        Servo intakeLeftServo = hardwareMap.get(Servo.class, "ILeft");
        Servo intakeRightServo = hardwareMap.get(Servo.class, "IRight");

        Servo intakeAngleLeftServo = hardwareMap.get(Servo.class, "ALeft");
        Servo intakeAngleRightServo = hardwareMap.get(Servo.class, "ARight");

        Servo scissorLeftServo = hardwareMap.get(Servo.class, "SLeft");
        Servo scissorRightServo = hardwareMap.get(Servo.class, "SRight");

        ColorSensor colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        motorBackRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intakeAngleLeftServo.setDirection(Servo.Direction.REVERSE);

        // Left
        // Control Hub
        // - Port 0: LBMotor
        // - Port 1: LFMotor
        // - Port 0: ILeft
        // - Port 1: ALeft
        // - Port 2: SLeft

        // Right
        // Expansion Hub
        // - Port 0: RBMotor
        // - Port 1: RFMotor
        // - Port 0: IRight
        // - Port 1: ARight
        // - Port 2: SRight

        //speed adjust thing
        double speedAdjust = 1;

        int counter2 = 0;

        waitForStart();


        while (opModeIsActive()) {
            // gamepad controls
            double y = -gamepad2.left_stick_y;  // Forward/backward movement
            double x = gamepad2.left_stick_x;  // Left/right strafing
            double rx = gamepad2.right_stick_x;  // Rotation

// Calculate motor powers with all movements combined
            double frontRightPower = y - x - rx;
            double frontLeftPower = y + x + rx;
            double backLeftPower = y - x + rx;
            double backRightPower = y + x - rx;

            // Set motor power with corrected values for strafing direction
            motorBackLeft.setPower(backLeftPower * speedAdjust);
            motorBackRight.setPower(backRightPower * speedAdjust);
            motorFrontLeft.setPower(frontLeftPower * speedAdjust);
            motorFrontRight.setPower(frontRightPower * speedAdjust);

            if (gamepad2.left_trigger > 0) {
                intakeAngleLeftServo.setPosition(0.35);
                intakeAngleRightServo.setPosition(0.35);

                scissorLeftServo.setPosition(-1);
                scissorRightServo.setPosition(1);
            } else if (gamepad2.left_trigger == 0) {
                intakeAngleLeftServo.setPosition(0.45);
                intakeAngleRightServo.setPosition(0.45);

                scissorLeftServo.setPosition(1);
                scissorRightServo.setPosition(-1);
            }

            if (gamepad2.left_bumper) {
                if (counter2 == 0) {
                    counter2 += 1;
                    intakeLeftServo.setPosition(0);
                    intakeRightServo.setPosition(1);
                } else if (counter2 == 1) {
                    counter2 += 1;
                    intakeRightServo.setPosition(0);
                    intakeLeftServo.setPosition(1);
                } else if (counter2 == 2) {
                    counter2 = 0;
                    intakeRightServo.setPosition(0.5);
                    intakeLeftServo.setPosition(0.5);
                }
            };



            int red = colorSensor.red();
            int green = colorSensor.green();
            int blue = colorSensor.blue();

            // Display RGB values on the Driver Station screen
            telemetry.addData("Red", red);
            telemetry.addData("Green", green);
            telemetry.addData("Blue", blue);
            telemetry.update();

        }

    }
}
//}