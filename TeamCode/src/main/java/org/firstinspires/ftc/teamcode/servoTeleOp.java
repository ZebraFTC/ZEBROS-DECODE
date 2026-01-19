package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class servoTeleOp extends LinearOpMode {
    public Servo Flap;
    @Override
    public void runOpMode() throws InterruptedException {
        Flap = hardwareMap.get(Servo.class, "flap");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                Flap.setPosition(0.3);
            } else if (gamepad1.b) {
                Flap.setPosition(0.1);
            }
        }
    }
}
