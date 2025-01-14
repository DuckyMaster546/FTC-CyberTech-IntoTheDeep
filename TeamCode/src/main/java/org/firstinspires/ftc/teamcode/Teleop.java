package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOpNew")
public class Teleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx motorFrontLeft = hardwareMap.get(DcMotorEx.class, "LFMotor");
        DcMotorEx motorBackLeft = hardwareMap.get(DcMotorEx.class, "LBMotor");
        DcMotorEx motorFrontRight = hardwareMap.get(DcMotorEx.class, "RFMotor");
        DcMotorEx motorBackRight = hardwareMap.get(DcMotorEx.class, "RBMotor");

        DcMotorEx motorLeftLinear = hardwareMap.get(DcMotorEx.class, "LLinear");
        DcMotorEx motorRightLinear = hardwareMap.get(DcMotorEx.class, "RLinear");

        Servo scissorLeftServo = hardwareMap.get(Servo.class, "SLeft");
        Servo scissorRightServo = hardwareMap.get(Servo.class, "SRight");

        Servo bigArmRotationLeft = hardwareMap.get(Servo.class, "LBARotation");
        Servo bigArmRotationRight = hardwareMap.get(Servo.class, "RBARotation");

        Servo smallArmRotation = hardwareMap.get(Servo.class, "SARotation");
        Servo outakeClaw = hardwareMap.get(Servo.class, "CWRotation");

        Servo intakeClaw = hardwareMap.get(Servo.class, "IClaw"); // yet to figure out rotation direction
        Servo intakeWrist = hardwareMap.get(Servo.class, "IWrist"); // yet to figure out rotation direction

        Servo intakeRotationRight = hardwareMap.get(Servo.class, "RIRotation"); // yet to figure out rotation direction
        Servo intakeRotationLeft = hardwareMap.get(Servo.class, "LIRotation"); // yet to figure out rotation direction

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        motorRightLinear.setDirection(DcMotorSimple.Direction.REVERSE);
        motorLeftLinear.setDirection(DcMotorSimple.Direction.FORWARD);

        motorBackRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        motorRightLinear.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLeftLinear.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        bigArmRotationRight.setDirection(Servo.Direction.REVERSE);
        intakeRotationLeft.setDirection(Servo.Direction.REVERSE);
        intakeWrist.setDirection(Servo.Direction.REVERSE);
        scissorLeftServo.setDirection(Servo.Direction.REVERSE);

        double speedAdjust = 1;

        int initialRightPos = motorRightLinear.getCurrentPosition();
        int initialLeftPos = motorLeftLinear.getCurrentPosition();
        int curRightPos = motorRightLinear.getCurrentPosition();
        int curLeftPos = motorLeftLinear.getCurrentPosition();

        // Left
        // Control Hub
        // - Port 0: LBMotor
        // - Port 1: LFMotor
        // - Port 2: LLinear
        // - Port 0: LIRotation -> Left Intake Rotation
        // - Port 1: IWrist -> Intake Wrist
        // - Port 2: SLeft -> Scissor Left
        // - Port 3: LBARotation -> Left Big Arm Rotation
        // - Port 4: SARotation -> Small Arm Rotation
        // - Port 5: CWRotation -> Claw Rotation

        // Right
        // Expansion Hub
        // - Port 0: RBMotor
        // - Port 1: RFMotor
        // - Port 2: RLinear
        // - Port 0: IClaw -> Intake Claw
        // - Port 1: RIRotation -> Right Intake Rotation FUCKED
        // - Port 2: SRight -> Scissor Right
        // - Port 3: RBARotation -> Right Big Arm Rotation

        boolean sequenceActiveGP = false;  // To track if the sequence is running
        long actionStartTimeGP = 0;
        int stepGP = 0;

        boolean sequenceActiveTS = false;  // To track if the sequence is running
        long actionStartTimeTS = 0;
        int stepTS = 0;

        boolean sequenceActiveClawtoInit = false;  // To track if the sequence is running
        long actionStartTimeClawtoInit = 0;
        int stepClawtoInit = 0;

        boolean previousButtonStateOutakeClaw = false;
        int counter1 = 0;

        // init big arm
        bigArmRotationLeft.setPosition(0.34);
        bigArmRotationRight.setPosition(0.34);
        smallArmRotation.setPosition(0.56);
        outakeClaw.setPosition(0);

        // init intake
//        intakeClaw.setPosition(0);
//        intakeWrist.setPosition(0.07);
//        intakeRotationRight.setPosition(0);
//        intakeRotationLeft.setPosition(0);

        intakeRotationLeft.setPosition(0.2);
        intakeRotationRight.setPosition(0.2);
        intakeWrist.setPosition(0);

        waitForStart();
        while (opModeIsActive()) {
            double y = -gamepad2.left_stick_y;  // Forward/backward movement
            double x = gamepad2.left_stick_x;  // Left/right strafing
            double rx = gamepad2.right_stick_x;  // Rotation

            // Calculate motor powers with all movements combined
            double frontRightPower = y - x - rx;
            double frontLeftPower = y + x + rx;
            double backLeftPower = y - x + rx;
            double backRightPower = y + x - rx;

            // Set motor power with corrected values for strafing direction
            motorBackLeft.setPower(backLeftPower * speedAdjust); // 1.7
            motorBackRight.setPower(backRightPower * speedAdjust); // 1.3
            motorFrontLeft.setPower(frontLeftPower * speedAdjust);
            motorFrontRight.setPower(frontRightPower * speedAdjust);

            if (gamepad1.left_trigger > 0) { // Go Down
                motorLeftLinear.setPower(gamepad1.left_trigger);
                motorRightLinear.setPower(gamepad1.left_trigger);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            } else if (gamepad1.left_trigger == 0) {
                motorLeftLinear.setPower(0);
                motorRightLinear.setPower(0);
                curRightPos = motorRightLinear.getCurrentPosition();
                curLeftPos = motorLeftLinear.getCurrentPosition();
            };

            if (gamepad1.right_trigger > 0) { // Go Up
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

            // zero everything
            if (gamepad2.b) {
                scissorLeftServo.setPosition(0);
                scissorRightServo.setPosition(0);
            };

            if (gamepad1.right_bumper && !sequenceActiveClawtoInit) {
                // Start the sequence
                sequenceActiveClawtoInit = true;
                actionStartTimeClawtoInit = System.currentTimeMillis();
                stepClawtoInit = 0;  // Start from step 0
            };

            if (sequenceActiveClawtoInit) {
                switch (stepClawtoInit) {
                    case 0:
                        // Open Outake Claw
                        outakeClaw.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeClawtoInit >= 300) {
                            actionStartTimeClawtoInit = System.currentTimeMillis();
                            stepClawtoInit++;  // Move to next step
                        }
                        break;
                    case 1:
                        // Init Position
                        bigArmRotationLeft.setPosition(0.34);
                        bigArmRotationRight.setPosition(0.34);
                        smallArmRotation.setPosition(0.56);
                        outakeClaw.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeClawtoInit >= 100) {
                            sequenceActiveClawtoInit = false;  // End the sequence
                        }
                        break;
                }
            }

            // A Button Macro -> Grab to Prescore
            if (gamepad2.x && !sequenceActiveGP) {
                // Start the sequence
                sequenceActiveGP = true;
                actionStartTimeGP = System.currentTimeMillis();
                stepGP = 0;  // Start from step 0
            };

            if (sequenceActiveGP) {
                switch (stepGP) {
                    case 0:
                        // Open Intake Claw
                        intakeClaw.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeGP >= 100) {
                            actionStartTimeGP = System.currentTimeMillis();
                            stepGP++;  // Move to next step
                        }
                        break;
                    case 1:
                        // Reset positions
                        intakeClaw.setPosition(0);
                        intakeWrist.setPosition(0.07);
                        intakeRotationRight.setPosition(0);
                        intakeRotationLeft.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeGP >= 300) {
                            actionStartTimeGP = System.currentTimeMillis();
                            stepGP++;  // Move to next step
                        }
                        break;
                    case 2:
                        // Close claw
                        intakeClaw.setPosition(0.33);
                        if (System.currentTimeMillis() - actionStartTimeGP >= 300) {
                            actionStartTimeGP = System.currentTimeMillis();
                            stepGP++;
                        }
                        break;
                    case 3:
                        // Prescore position
                        intakeRotationLeft.setPosition(0.2);
                        intakeRotationRight.setPosition(0.2);
                        intakeWrist.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeGP >= 100) {
                            sequenceActiveGP = false;  // End the sequence
                        }
                        break;
                }
            }

            // B Button Macro -> PreScore // RIGHT TRIGGER
            if (gamepad2.right_bumper) {
                scissorLeftServo.setPosition(0.28);
                scissorRightServo.setPosition(0.28);
                intakeWrist.setPosition(0);
                intakeRotationLeft.setPosition(0.2);
                intakeRotationRight.setPosition(0.2);
            };


            // Left Bumper Button Macro -> Transfer to Score
            if (gamepad2.left_bumper && !sequenceActiveTS) {
                // Start the sequence
                sequenceActiveTS = true;
                actionStartTimeTS = System.currentTimeMillis();
                stepTS = 0;  // Start from step 0
            }

            if (sequenceActiveTS) {
                switch (stepTS) {
                    case 0:
                        // Intake Transfer Position + Slides In
                        scissorRightServo.setPosition(0);
                        scissorLeftServo.setPosition(0);

                        intakeRotationLeft.setPosition(0.2);
                        intakeRotationRight.setPosition(0.2);
                        intakeWrist.setPosition(0.16);
                        if (System.currentTimeMillis() - actionStartTimeTS >= 600) {
                            actionStartTimeTS = System.currentTimeMillis();
                            stepTS++;  // Move to next step
                        }
                        break;
                    case 1:
                        intakeRotationLeft.setPosition(0.37);
                        intakeRotationRight.setPosition(0.37);
                        intakeWrist.setPosition(0.42);
                        if (System.currentTimeMillis() - actionStartTimeTS >= 600) {
                            actionStartTimeTS = System.currentTimeMillis();
                            stepTS++;  // Move to next step
                        }
                        break;
                    case 2:
                        // Close Outtake Claw
                        bigArmRotationLeft.setPosition(0.37);
                        bigArmRotationRight.setPosition(0.37);
                        smallArmRotation.setPosition(0.56);
                        outakeClaw.setPosition(0.33);
                        if (System.currentTimeMillis() - actionStartTimeTS >= 200) {
                            actionStartTimeTS = System.currentTimeMillis();
                            stepTS++;
                        }
                        break;
                    case 3:
                        // Open Intake Claw
                        intakeClaw.setPosition(0);
                        intakeRotationLeft.setPosition(0.2);
                        intakeRotationRight.setPosition(0.2);
                        intakeWrist.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeTS >= 200) {
                            actionStartTimeTS = System.currentTimeMillis();
                            stepTS++;  // Move to next step
                        }
                        break;
                    case 4:
                        // Score
                        bigArmRotationLeft.setPosition(0.06);
                        bigArmRotationRight.setPosition(0.06);
                        smallArmRotation.setPosition(0);
                        if (System.currentTimeMillis() - actionStartTimeTS >= 100) {
                            sequenceActiveTS = false;  // End the sequence
                        }
                        break;
                };
            };

            // intakerotationright -> down is 0 (safe), up is 1
            // intakerotationleft -> down is 0 (safe), up is 1, it required a reverse direction
            // intakeClaw -> open is 0 (safe), closed is 0.33 (safe)
            // intakeWrist -> pick up down is 0 (safe), up to transition to intake is 1, it required a reverse direction
            // bigArmRotationRight -> up is 0 (safe), down is 1, it required a reverse direction
            // bigArmRotationLeft -> up is 0(safe), down is 1
            // outakeClaw -> open is 0 (safe), closed is 0.33 (safe)
            // smallArmRotation -> out is 0 (score, safe), inside to transition is 1
            // scissorRightServo -> in is 0 (safe), outside is 1
            // scissorLeftServo -> in is 0 (safe), outside is 1, required a reverse direction
        }
    }
}
