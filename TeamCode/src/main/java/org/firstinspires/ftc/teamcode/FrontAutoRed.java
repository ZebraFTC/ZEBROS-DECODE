package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class FrontAutoRed extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor Intake;
    private DcMotor Transfer;
    private DcMotor Shooter;
    private Servo Flap;

    private ElapsedTime timer = new ElapsedTime();

    public enum AutoCase {
        taxi_auto,
        three_ball,
        six_ball
    }

    AutoCase selectedCase = AutoCase.six_ball;


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

        telemetry.addLine("Use dpad to select auto");
        while (!isStarted() && !isStopRequested()) {
            if (gamepad1.dpad_up) {
                selectedCase = AutoCase.taxi_auto;
            }
            if (gamepad1.dpad_left) {
                selectedCase = AutoCase.three_ball;
            }
            if (gamepad1.dpad_right) {
                selectedCase = AutoCase.six_ball;
            }
        }

        telemetry.addData("Selected Auto", selectedCase);
        telemetry.update();

        waitForStart();

        switch (selectedCase) {
            case taxi_auto:
                runTaxi();
                break;
            case three_ball:
                runThreeBall();
                break;
            case six_ball:
                runSixBall();
                break;
        }

    }

    public void runTaxi() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.3, false);

        drive(0.5, -0.5, 0.5, -0.5, 0.28, false);
        drive(0.5, -0.5, -0.5, 0.5, 1, false);
        drive(-0.5, 0.5, 0.5, -0.5, 1, false);
    }

    public void runThreeBall() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.3, false);

        shootThreeBall();

        drive(0.5, -0.5, 0.5, -0.5, 0.28, false);
        drive(0.5, -0.5, -0.5, 0.5, 1, false);
        drive(-0.5, 0.5, 0.5, -0.5, 1, false);
    }

    public void runSixBall() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);

        shootThreeBall();

        drive(0.5, -0.5, 0.5, -0.5, 0.24, false);
        drive(0.5, -0.5, -0.5, 0.5, 0.70, false );
        drive(0.5, 0.5, 0.5, 0.5, 2.3, true ); // intake first time
        drive(-0.5, -0.5, -0.5, -0.5, 1.6, false);
        drive(-0.5, 0.5, -0.5, 0.5, 0.28, false); // turn for shooter second time
        drive(0.5, 0.5, 0.5, 0.5, 0.3, false);

        shootThreeBall();


        drive(0.5, -0.5, 0.5, -0.5, 0.229, false);
        drive(0.5, -0.5, -0.5, 0.5, 1, false);
    }

    private void nudgeBall() {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 0.1) {
            Intake.setPower(-0.4);
            Transfer.setPower(-0.4);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 0.3) {
            Intake.setPower(0);
            Transfer.setPower(0);
        }
    }

    private void shootThreeBall() {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.6);
        }
        nudgeBall();
        nudgeBall();
        nudgeBall();
        Shooter.setPower(0);
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
            idle();

        }
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
    }
}
