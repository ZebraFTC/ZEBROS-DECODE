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


        drive(-0.5, -0.5, -0.5, -0.5, 1.5, false);

        shootOneBall();
        shootOneBall();
        shootOneBall();
        drive(-0.5, 0.5, -0.5, 0.5, 0.5, false);
        drive(0.5, 0.5, 0.5, 0.5, 0.6, false );
        drive(0.5, -0.5, 0.5, -0.5, 0.3, false);
        drive(0.5, 0.5, 0.5, 0.5, 0.5, true );
        drive(0.5, 0.5, 0.5, 0.5, 0.5, false );
        drive(0.5, -0.5, -0.5, 0.5, 0.5, false );

    }

    private void shootOneBall() {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.78);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.78);
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
            Intake.setPower(-0.67);
            Transfer.setPower(-0.67);
        }
        Intake.setPower(0);
        Transfer.setPower(0);





    }


    public void drive(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower, double time, boolean intakeAndTransfer) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < time) {     // While the timer is running
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
            if (intakeAndTransfer) {              // If set tossee4- true it runs the intake and the transfer
                Intake.setPower(-0.67);
                Transfer.setPower(-0.67);
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
