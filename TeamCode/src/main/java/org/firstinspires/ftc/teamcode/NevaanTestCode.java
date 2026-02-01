package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class NevaanTestCode extends LinearOpMode {
    DcMotor Shooter;
    DcMotor Intake;
    DcMotor Transfer;
    @Override
    public void runOpMode() {
        Shooter = hardwareMap.get(DcMotor.class, "Shooter");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Transfer = hardwareMap.get(DcMotor.class, "Transfer");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_left);{
                Shooter.setPower(0.9);
            }

            Intake.setPower(gamepad1.left_trigger-gamepad1.right_trigger);
            Transfer.setPower(gamepad1.left_trigger-gamepad1.right_trigger);

        }
    }


}