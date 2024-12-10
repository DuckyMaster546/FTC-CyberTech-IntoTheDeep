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

        Servo bigArmRotationLeft = hardwareMap.get(Servo.class, "LBARotation");
        Servo bigArmRotationRight = hardwareMap.get(Servo.class, "RBARotation");

        Servo smallArmRotation = hardwareMap.get(Servo.class, "SARotation");
        Servo claw = hardwareMap.get(Servo.class, "CWRotation");

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        motorBackRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intakeAngleLeftServo.setDirection(Servo.Direction.REVERSE);
        claw.setDirection(Servo.Direction.REVERSE);

        // Left
        // Control Hub
        // - Port 0: LBMotor
        // - Port 1: LFMotor
        // - Port 0: ILeft
        // - Port 1: ALeft
        // - Port 2: SLeft
        // - Port 3: LBARotation
        // - Port 4: SARotation
        // - Port 5: CWRotation

        // Right
        // Expansion Hub
        // - Port 0: RBMotor
        // - Port 1: RFMotor
        // - Port 0: IRight
        // - Port 1: ARight
        // - Port 2: SRight
        // - Port 3: RBARotation

        //speed adjust thing
        double speedAdjust = 1;

        int counter1 = 0;
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

            // yash's controller
            if (gamepad2.left_trigger > 0) {
//                intakeAngleLeftServo.setPosition(0.35);
//                intakeAngleRightServo.setPosition(0.35);
                scissorLeftServo.setPosition(-1);
                scissorRightServo.setPosition(1);
            } else if (gamepad2.left_trigger == 0) {
//                intakeAngleLeftServo.setPosition(0.45);
//                intakeAngleRightServo.setPosition(0.45);
                scissorLeftServo.setPosition(1);
                scissorRightServo.setPosition(-1);
            };

            // amogh's controller
            if (gamepad1.y) { // moving intake angled up and down not working
//                if (counter2 == 0) {
//                    counter2 += 1;
//                    intakeAngleLeftServo.setPosition(0.32);
//                    intakeAngleRightServo.setPosition(0.32);
//                } else if (counter2 == 1) {
//                    counter2 -= 1;
//                    intakeAngleLeftServo.setPosition(0.45);
//                    intakeAngleRightServo.setPosition(0.45);
//                };

                intakeAngleLeftServo.setPosition(0.6);

            };

            if (gamepad1.a) {
                intakeLeftServo.setPosition(0);
                intakeRightServo.setPosition(1);
            } else {
                intakeRightServo.setPosition(0.5);
                intakeLeftServo.setPosition(0.5);
            };

            if (gamepad1.b) {
                intakeRightServo.setPosition(0);
                intakeLeftServo.setPosition(1);
            } else {
                intakeRightServo.setPosition(0.5);
                intakeLeftServo.setPosition(0.5);
            };

            if (gamepad1.left_bumper) { // claw open and close
                if (counter1 == 0) {
                    counter1 += 1;
                    claw.setPosition(0);
                } else if (counter1 == 1) {
                    counter1 -= 0;
                    claw.setPosition(0.25);
                };
            };

            if (gamepad1.x) { // regular score macro

            };

            if (gamepad1.right_bumper) { // specimen get macro

            }

//              bigArmRotationRight.setPosition(0.53); // default position final
//              bigArmRotationRight.setPosition(0); // score macro position final + specimen final
//              claw.setPosition(0); // close claw -> needs to be reversed final
//              claw.setPosition(0.25); // open claw -> needs to be reversed final


        }

    }
}
//}
