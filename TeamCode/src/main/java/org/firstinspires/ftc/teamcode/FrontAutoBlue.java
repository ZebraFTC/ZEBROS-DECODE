package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class FrontAutoBlue extends LinearOpMode {
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
        six_ball,
        nine_ball
    }

    AutoCase selectedCase = AutoCase.nine_ball;


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
            if (gamepad1.dpad_down) {
                selectedCase = AutoCase.nine_ball;
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
            case nine_ball:
                runNineBall();
                break;
        }

    }

    public void runTaxi() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);

        drive(-0.5, 0.5, -0.5, 0.5, 0.267, false);
        drive(-0.5, 0.5, 0.5, -0.5, 0.8, false);
    }

    public void runThreeBall() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);

        shootThreeBall();

        drive(-0.5, 0.5, -0.5, 0.5, 0.267, false); // leave zone
        drive(-0.5, 0.5, 0.5, -0.5, 0.795, false);
    }

    public void runSixBall() {
        Shooter.setPower(0.7);
        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);

        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.7);
        Shooter.setPower(-0.5);

        drive(-0.5, 0.5, -0.5, 0.5, 0.267  , false);
        drive(-0.5, 0.5, 0.5, -0.5, 0.8, false );
        drive(0.5,0.5,0.5,0.5,2,true);
        drive(-0.5, -0.5, -0.5, -0.5, 1.4, false);
        drive(0.5, -0.5, 0.5, -0.5, 0.276, false); // turn for shooter second time
        drive(0.5, 0.5, 0.5, 0.5, 0.3, false);

        shootThreeBall();

        drive(-0.5, 0.5, -0.5, 0.5, 0.28, false); // leave zone
        drive(-0.5, 0.5, 0.5, -0.5, 1.5, false);
    }
    public void runNineBall() {
        Shooter.setPower(0.7);
        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);

        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        telemetry.update();
        nudgeBall(0.25);
        nudgeBall(0.53);
        nudgeBall(0.7);
        Shooter.setPower(-0.5);

        //our robot is racist

        drive(-0.5, 0.5, -0.5, 0.5, 0.282  , false);
        drive(-0.5, 0.5, 0.5, -0.5, 0.915, false );
        drive(0.5,0.5,0.5,0.5,2.1,true);
        drive(-0.5, -0.5, -0.5, -0.5, 1.5, false);
        drive(0.5, -0.5, 0.5, -0.5, 0.4, false); // turn for shooter second time
        drive(0.5, 0.5, 0.5, 0.5, 0.2, false);

        shootThreeBall();

        drive(-0.5, 0.5, -0.5, 0.5, 0.3, false);
        drive(-0.5, 0.5, 0.5, -0.5, 2.3, false);
        drive(0.5, 0.5, 0.5, 0.5, 2, true);
        drive(-0.5, -0.5, -0.5, -0.5, 1.4, false);
        drive(0.5, -0.5, -0.5, 0.5, 1.8, false );
        drive(0.5, -0.5, 0.5, -0.5, 0.4 , false); // turn for shooter third time
        drive(0.5, 0.5, 0.5, 0.5, 0.2, false);

        shootThreeBall();

        drive(0.5, -0.5, 0.5, -0.5, 0.3, false); // leave zone
        drive(0.5, 0.5, 0.5, 0.5, 0.2, false);
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
        Shooter.setPower(0.7);
        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        telemetry.update();

        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.7);
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
                Intake.setPower(-1);
                Transfer.setPower(-1);
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