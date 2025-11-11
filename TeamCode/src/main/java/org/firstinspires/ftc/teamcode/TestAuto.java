package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class TestAuto extends LinearOpMode {
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
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.5) {
            frontLeft.setPower(-0.5);
            frontRight.setPower(-0.5);
            backLeft.setPower(-0.5);
            backRight.setPower(-0.5);
        }
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        shootOneBall(0.5);
        shootOneBall(0.5);
        shootOneBall(0.7);

    }

    private void shootOneBall(double transSpeed) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.85);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.85);
            Flap.setPosition(0.3);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.5) {
            Shooter.setPower(0);
            Flap.setPosition(0.05);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.5) {
            Shooter.setPower(0);
            Intake.setPower(-transSpeed);
            Transfer.setPower(-transSpeed);
        }
        Intake.setPower(0);
        Transfer.setPower(0);
    }

}
