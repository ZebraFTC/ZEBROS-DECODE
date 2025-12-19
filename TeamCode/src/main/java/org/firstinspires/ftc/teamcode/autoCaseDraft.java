package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class autoCaseDraft extends LinearOpMode {
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
            } else if (gamepad1.dpad_left) {
                selectedCase = AutoCase.three_ball;
            } else if (gamepad1.dpad_right) {
                selectedCase = AutoCase.six_ball;
            } else {
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

        drive(-0.5, 0.5, -0.5, 0.5, 0.28, false);
        drive(-0.5, 0.5, 0.5, -0.5, 0.65, false );
    }

    public void runThreeBall() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.3, false);

        shootOneBall(0.5);
        shootOneBall(1);
        shootOneBall(-1);
    }

    public void runSixBall() {
        drive(-0.5, -0.5, -0.5, -0.5, 1.3, false);

        shootOneBall(0.5);
        shootOneBall(1);
        shootOneBall(-1);
        drive(-0.5, 0.5, -0.5, 0.5, 0.28, false);
        drive(-0.5, 0.5, 0.5, -0.5, 0.65, false );
        drive(0.5, 0.5, 0.5, 0.5, 2.3, true ); // intake first time
        drive(-0.5, -0.5, -0.5, -0.5, 1.6, false);
        drive(0.5, -0.5, 0.5, -0.5, 0.28, false); // turn for shooter second time
        drive(0.5, 0.5, 0.5, 0.5, 0.3, false);

        shootOneBall(0.5);
        shootOneBall(0.7);
        shootOneBall(-1);
        drive(-0.5, 0.5, -0.5, 0.5, 0.28, false);
        drive(-0.5, 0.5, 0.5, -0.5, 2.5, false );
        drive(0.5, 0.5, 0.5, 0.5, 2.3, true ); // intake first time
        drive(-0.5, -0.5, -0.5, -0.5, 1.6, false);
        drive(0.5, -0.5, -0.5, 0.5, 2, false );
        drive(0.5, -0.5, 0.5, -0.5, 0.28, false); // turn for shooter second time
        drive(0.5, 0.5, 0.5, 0.5, 0.3, false);

        shootOneBall(0.5);
        shootOneBall(0.7);
        shootOneBall(-1);

        //drive(-0.5, -0.5, -0.5, -0.5, 0.5, false );
        //drive(0.5, -0.5, -0.5, 0.5, 0.5, false );
    }

    private void shootOneBall(double transSpeed) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.8);
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1.0) {
            Shooter.setPower(0.8);
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
