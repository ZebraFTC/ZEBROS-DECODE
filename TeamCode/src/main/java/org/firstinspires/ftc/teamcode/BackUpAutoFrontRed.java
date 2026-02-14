package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous
public class BackUpAutoFrontRed extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor Intake;
    private DcMotor Transfer;
    private DcMotor Shooter;
    private DcMotor ShooterAssist;
    private int FLP = 0;
    private int FRP = 0;
    private int BLP = 0;
    private int BRP = 0;
    private ElapsedTime timer = new ElapsedTime();


    public enum AutoCase {
        taxi_auto,
        three_ball,
        six_ball,
        nine_ball
    }


    AutoCase selectedCase = AutoCase.six_ball;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");


        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");
        ShooterAssist = hardwareMap.get(DcMotor.class, "shootertwo");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterAssist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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
        drive(-2000, -2000, -2000, -2000, false);

        drive(350, -350, 350, -350, false);
        drive(1383, -1383, -1383, 1383, false);
    }


    public void runThreeBall() {
        Shooter.setPower(0.85);
        ShooterAssist.setPower(-0.85);
        changeDriveSpeed(-1950, -1950, -1950, -1950, false, 0.7);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(350, -350, 350, -350, false);
        drive(1383, -1383, -1383, 1383, false);
    }


    public void runSixBall() {
        Shooter.setPower(0.85);
        ShooterAssist.setPower(-0.85);
        changeDriveSpeed(-1950, -1950, -1950, -1950, false, 0.7);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(360, -360, 360, -360, false);
        drive(675, -675, -675, 675, false);

        drive(1800,1800,1800,1800,true);
        drive(-1800, -1800, -1800, -1800, false);
        drive(-300, 300, 300, -300, false);
        drive(-450, 450, -450, 450, false); // turn for shooter second time
        drive(400, 400, 400, 400, false);

        shootThreeBall();


        drive(400, -400, 400, -400, false); // leave zone
        drive(1500, -1500, -1500, 1500, false);
    }
    public void runNineBall() {
        Shooter.setPower(0.85);
        ShooterAssist.setPower(-0.85);
        changeDriveSpeed(-1950, -1950, -1950, -1950, false, 0.7);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(360, -360, 360, -360, false);
        drive(700, -700, -700, 700, false);

        drive(1800,1800,1800,1800,true);
        drive(-1800, -1800, -1800, -1800, false);
        drive(300, -300, -300, 300, false);
        drive(450, 450, -450, 450, false); // turn for shooter second time
        drive(400, 400, 400, 400, false);

        shootThreeBall();

        drive(350, -350, 350, -350, false);
        drive(1500, -1500, -1500, 1500, false);

        drive(1800,1800,1800,1800,true);
        drive(-1850, -1850, -1850, -1850, false);
        drive(-1700, 1700, 1700, -1700, false);
        drive(-400, 400, -400, 400, false); // turn for shooter second time
        drive(430, 430, 430, 430, false);

        shootThreeBall();

        drive(350, -350, 350, -350, false); // leave zone
        drive(1500, -1500, -1500, 1500, false);
    }
    private void nudgeBall(double intakeTime) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < intakeTime) {
            Intake.setPower(-1);
            Transfer.setPower(-1);
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
            Shooter.setPower(0.85);
            ShooterAssist.setPower(-0.85);
        }

        nudgeBall(4);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);


    }

    public void drive(int FLTarget, int FRTarget, int BLTarget, int BRTarget, boolean intakeAndTransfer) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + FLTarget);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + FRTarget);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + BLTarget);
        backRight.setTargetPosition(backRight.getCurrentPosition() + BRTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (intakeAndTransfer) {
            frontLeft.setPower(0.6);
            frontRight.setPower(0.6);
            backLeft.setPower(0.6);
            backRight.setPower(0.6);
        } else {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        }


        double timeout = getRuntime() + 5.0;

        while (opModeIsActive()
                && (frontLeft.isBusy()
                || frontRight.isBusy()
                || backLeft.isBusy()
                || backRight.isBusy())
                && getRuntime() < timeout) {

            if (intakeAndTransfer) {
                Intake.setPower(-1);
            }
        }
        stopRobot();
    }
    public void changeDriveSpeed(int FLTarget, int FRTarget, int BLTarget, int BRTarget, boolean intakeAndTransfer, double speed) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + FLTarget);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + FRTarget);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + BLTarget);
        backRight.setTargetPosition(backRight.getCurrentPosition() + BRTarget);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (intakeAndTransfer) {
            frontLeft.setPower(0.7);
            frontRight.setPower(0.7);
            backLeft.setPower(0.7);
            backRight.setPower(0.7);
        } else {
            frontLeft.setPower(speed);
            frontRight.setPower(speed);
            backLeft.setPower(speed);
            backRight.setPower(speed);
        }


        double timeout = getRuntime() + 5.0;

        while (opModeIsActive()
                && (frontLeft.isBusy()
                || frontRight.isBusy()
                || backLeft.isBusy()
                || backRight.isBusy())
                && getRuntime() < timeout) {

            if (intakeAndTransfer) {
                Intake.setPower(-1);
                Transfer.setPower(-1);
            }
        }
        stopRobot();
    }
    private void stopRobot() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
    }
}

