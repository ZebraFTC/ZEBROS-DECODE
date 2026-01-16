package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class BackAutoBlue extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor Intake;
    private DcMotor Transfer;
    private DcMotor Shooter;
    private Servo Flap;

    private static ElapsedTime timer = new ElapsedTime();

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");

        Flap = hardwareMap.get(Servo.class, "flap");
        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);



        waitForStart();

        while (opModeIsActive() && timer.seconds() < 15) {
            telemetry.addLine("Waiting to move");
        }
        telemetry.update();

        drive(0.5, 0.5, 0.5, 0.5, 0.5, false);
    }

    private void nudgeBall(double intakeTime) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < intakeTime) {
            Intake.setPower(-0.7);
            Transfer.setPower(-0.7);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1) {
            Intake.setPower(0);
            Transfer.setPower(0);
        }
    }

    private void shootThreeBall() {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 2) {
            Shooter.setPower(0.7);
        }

        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.4);
        Shooter.setPower(-0.5);

    }


    public void drive(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower, double time, boolean intakeAndTransfer) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < time) {     // While the timer is running
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
            if (intakeAndTransfer) {              // If set toss4- true it runs the intake and the transfer
                Intake.setPower(-0.75);
                Transfer.setPower(-0.75);
            }

        }
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
    }
}
