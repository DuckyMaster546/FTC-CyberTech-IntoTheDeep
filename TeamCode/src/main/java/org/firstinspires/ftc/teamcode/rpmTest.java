package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "rpmTest")
public class rpmTest extends LinearOpMode {
    // Declare motor and RPM calculation variables
    private DcMotor motor;
    private long lastTime;
    private int lastEncoderPosition;
    private double rpm;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motor (replace "motor" with your motor's configuration name)
        motor = hardwareMap.get(DcMotor.class, "motor");

        // Initialize time and encoder position tracking
        lastTime = System.currentTimeMillis();
        lastEncoderPosition = motor.getCurrentPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Set the motor to run at 50% power
            motor.setPower(1);

            // Get the current time and encoder position
            long currentTime = System.currentTimeMillis();
            int currentEncoderPosition = motor.getCurrentPosition();

            // Calculate the time difference in seconds
            double timeDelta = (currentTime - lastTime) / 1000.0;

            // Calculate the change in encoder position (ticks per time delta)
            int ticksDelta = currentEncoderPosition - lastEncoderPosition;

            // Assuming your motor has 384.5 ticks per revolution (adjust for your motor type)
            double ticksPerRevolution = 8192;

            // Calculate RPM
            rpm = (ticksDelta / timeDelta) * (60.0 / ticksPerRevolution);

            // Send RPM to telemetry
            telemetry.addData("Motor RPM", rpm);
            telemetry.update();

            // Update last encoder position and time for next loop iteration
            lastEncoderPosition = currentEncoderPosition;
            lastTime = currentTime;
        }

        // Stop the motor when the opMode ends
        motor.setPower(0);
    }
}