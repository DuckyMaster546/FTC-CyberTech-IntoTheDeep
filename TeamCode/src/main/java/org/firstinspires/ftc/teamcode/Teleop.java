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
public class Teleop extends LinearOpMode {

//    private CRServo continuousServo;

    @Override
    public void runOpMode() throws InterruptedException {
        //Drive-Motors
        DcMotorEx motorFrontLeft = hardwareMap.get(DcMotorEx.class, "LFMotor");
        DcMotorEx motorBackLeft = hardwareMap.get(DcMotorEx.class, "LBMotor");
        DcMotorEx motorFrontRight = hardwareMap.get(DcMotorEx.class, "RFMotor");
        DcMotorEx motorBackRight = hardwareMap.get(DcMotorEx.class, "RBMotor");

        DcMotorEx motorLeftLinear = hardwareMap.get(DcMotorEx.class, "LLinear");
        DcMotorEx motorRightLinear = hardwareMap.get(DcMotorEx.class, "RLinear");

        CRServo intakeLeftServo = hardwareMap.crservo.get("ILeft");
        CRServo intakeRightServo = hardwareMap.crservo.get("IRight");

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

        motorRightLinear.setDirection(DcMotorSimple.Direction.REVERSE);
        motorLeftLinear.setDirection(DcMotorSimple.Direction.FORWARD);

        motorBackRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        motorRightLinear.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLeftLinear.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        intakeAngleLeftServo.setDirection(Servo.Direction.REVERSE);
        claw.setDirection(Servo.Direction.REVERSE);

        // Left
        // Control Hub
        // - Port 0: LBMotor
        // - Port 1: LFMotor
        // - Port 2: LLinear
        // - Port 0: ILeft -> Intake Left
        // - Port 1: ALeft -> Angle Left
        // - Port 2: SLeft -> Scissor Left
        // - Port 3: LBARotation -> Left Big Arm Rotation
        // - Port 4: SARotation -> Small Arm Rotation
        // - Port 5: CWRotation -> Claw Rotation

        // Right
        // Expansion Hub
        // - Port 0: RBMotor
        // - Port 1: RFMotor
        // - Port 2: RLinear
        // - Port 0: IRight -> Intake Right
        // - Port 1: ARight -> Angle Right
        // - Port 2: SRight -> Scissor Right
        // - Port 3: RBARotation -> Right Big Arm Rotation

        //speed adjust thing
        double speedAdjust = 1;

        int counter1 = 0;
        int counter2 = 0;

        int initialRightPos = motorRightLinear.getCurrentPosition();
        int initialLeftPos = motorLeftLinear.getCurrentPosition();

        int curRightPos = motorRightLinear.getCurrentPosition();
        int curLeftPos = motorLeftLinear.getCurrentPosition();

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
                scissorLeftServo.setPosition(1);
                scissorRightServo.setPosition(-1);
            };

            if (gamepad2.right_trigger > 0) {
                scissorLeftServo.setPosition(-1);
                scissorRightServo.setPosition(1);
            }

            // amogh's controller
            if (gamepad1.y) { //---- ------------------------------------------------------------------------------------------------------
                if (counter2 == 0) {
                    counter2 += 1;
                    intakeAngleRightServo.setPosition(0.68); // intake position
                    intakeAngleLeftServo.setPosition(0.68); // intake position
                } else if (counter2 == 1) {
                    counter2 -= 1;
                    intakeAngleRightServo.setPosition(0.83);// default position
                    intakeAngleLeftServo.setPosition(0.83);// default position
                };
            };

            if (gamepad1.a) { // intake
                intakeRightServo.setPower(1);
                intakeLeftServo.setPower(-1);
            } else if (gamepad1.b) {
                intakeRightServo.setPower(-1);
                intakeLeftServo.setPower(1);
            } else {
                intakeRightServo.setPower(0.0);
                intakeLeftServo.setPower(0.0);
            }

            if (gamepad1.left_bumper) { // claw open and close
                if (counter1 == 0) {
                    counter1 += 1;
                    claw.setPosition(0.1);
                } else if (counter1 == 1) {
                    counter1 -= 1;
                    claw.setPosition(0.35);
                };
            };

//            if (gamepad1.x) { // regular default position
//                smallArmRotation.setPosition(0.55); // default position final
//                bigArmRotationRight.setPosition(0.53); // default position final
//                claw.setPosition(0.35);
//            };

            if (gamepad1.right_bumper) { // specimen/score get macro
                smallArmRotation.setPosition(0.4); // score/specimen position final
                bigArmRotationRight.setPosition(0); // default position final
            }

            if (gamepad1.left_trigger > 0) {
                motorLeftLinear.setPower(gamepad1.left_trigger);
                motorRightLinear.setPower(gamepad1.left_trigger);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            } else if (gamepad1.left_trigger == 0) {
                motorLeftLinear.setPower(0);
                motorRightLinear.setPower(0);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            }

            if (gamepad1.right_trigger > 0) {
                motorLeftLinear.setPower(-gamepad1.right_trigger);
                motorRightLinear.setPower(-gamepad1.right_trigger);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            } else if (gamepad1.right_trigger == 0) {
                motorLeftLinear.setPower(0);
                motorRightLinear.setPower(0);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            };

//            if (gamepad1.x) { //------------------------------------------------------------------------------------------------------------
//                // Define a tolerance range for the motor position
//                int tolerance = 100; // Adjust this value based on your setup
//
//                // Set servos in position
//                smallArmRotation.setPosition(0.55); // default position final
//                bigArmRotationRight.setPosition(0.53); // default position final
//                claw.setPosition(0.35);
//                sleep(500);
//
//                // Move the linear motors back to their initial positions
//                while (true) { // Loop indefinitely
//                    // Determine direction for each motor
//                    double rightPower = (motorRightLinear.getCurrentPosition() < initialRightPos) ? -0.5 : 0.5;
//                    double leftPower = (motorLeftLinear.getCurrentPosition() < initialLeftPos) ? -0.5 : 0.5;
//
//                    // Check if motors are within the tolerance range of initial positions
//                    boolean rightInPosition = Math.abs(motorRightLinear.getCurrentPosition() - initialRightPos) <= tolerance;
//                    boolean leftInPosition = Math.abs(motorLeftLinear.getCurrentPosition() - initialLeftPos) <= tolerance;
//
//                    // Stop motors if they are in position
//                    if (rightInPosition) {
//                        rightPower = 0;
//                    }
//                    if (leftInPosition) {
//                        leftPower = 0;
//                    }
//
//                    // Set power to motors
//                    motorRightLinear.setPower(rightPower);
//                    motorLeftLinear.setPower(leftPower);
//
//                    // Break the loop if both motors are in position
//                    if (rightInPosition && leftInPosition) {
//                        break;
//                    }
//                }
//
//                // Stop motors after breaking out of the loop
//                motorRightLinear.setPower(0);
//                motorLeftLinear.setPower(0);
//            }

//              intakeAngleRightServo.setPosition(0.64); // intake position
//              intakeAngleLeftServo.setPosition(0.64); // intake position
//              intakeAngleRightServo.setPosition(0.83);// default position
//              intakeAngleLeftServo.setPosition(0.83);// default position
//              smallArmRotation.setPosition(0.55) // default position final
//              smallArmRotation.setPosition(0.4) // score/specimen position final
//              bigArmRotationRight.setPosition(0.53); // default position final
//              bigArmRotationRight.setPosition(0); // score macro position final + specimen final
//              claw.setPosition(0.1); // close claw -> needs to be reversed final
//              claw.setPosition(0.35); // open claw -> needs to be reversed final
        }
    }
}
//}