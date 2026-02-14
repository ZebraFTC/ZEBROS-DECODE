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
        while (opModeIsActive() && timer.seconds() < 25) {
            telemetry.addLine("Waiting to move");
        }
        telemetry.update();
        drive(2000, 2000, 2000, 2000, false);
    }


    public void runThreeBall() {
        Shooter.setPower(0.9);
        ShooterAssist.setPower(-0.9);
        drive(350, 350, 350, 350, false);
        drive(-250, 250, -250, 250, false);

        while (opModeIsActive() && timer.seconds() < 2.5) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4, 1);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(200, -200, 200, -200, false);
        while (opModeIsActive() && timer.seconds() < 15) {
            telemetry.addLine("Waiting to move");
        }
        telemetry.update();
        drive(800, 800, 800, 800, false);
    }


    public void runSixBall() {
        Shooter.setPower(0.9);
        ShooterAssist.setPower(-0.9);
        drive(350, 350, 350, 350, false);
        drive(-250, 250, -250, 250, false);

        while (opModeIsActive() && timer.seconds() < 2.5) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4, 1);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(-560, 560, -560, 560, false);
        drive(820, -820, -820, 820, false);
        drive(2000,2000,2000,2000,true);
        changeDriveSpeed(-1850, -1850, -1850, -1850, false, 0.7);
        drive(-920, 920, 920, -920, false); // turn for shooter second time
        drive(540, -540, 540, -540, false);

        shootThreeBall();

        drive(250, -250, 250, -250, false);
        drive(800, 800, 800, 800, false);
    }
    public void runNineBall() {
        Shooter.setPower(0.9);
        ShooterAssist.setPower(-0.9);
        drive(350, 350, 350, 350, false);
        drive(-250, 250, -250, 250, false);

        while (opModeIsActive() && timer.seconds() < 2.5) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(4, 1);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

        drive(-560, 560, -560, 560, false);
        drive(820, -820, -820, 820, false);
        drive(2000,2000,2000,2000,true);
        changeDriveSpeed(-1850, -1850, -1850, -1850, false, 0.7);
        drive(-920, 920, 920, -920, false); // turn for shooter second time
        drive(540, -540, 540, -540, false);

        shootThreeBall();

        drive(-600, 600, -600, 600, false);
        drive(1500, -1500, -1500, 1500, false);
        drive(2000, -2000, -2000, 2000, false);
        drive(2000,2000,2000,2000,true);
        drive(-2000, -2000, -2000, -2000, false);
        drive(-1500, 1500, 1500, -1500, false); // turn for shooter second time
        drive(600, -600, 600, -600, false);

        shootThreeBall();

        drive(200, -200, 200, -200, false);
        drive(800, 800, 800, 800, false);
    }
    private void nudgeBall(double intakeTime, double intakeSpeed) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < intakeTime) {
            Intake.setPower(-intakeSpeed);
            Transfer.setPower(-intakeSpeed);
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
            Shooter.setPower(0.95);
            ShooterAssist.setPower(-0.95);
        }

        nudgeBall(4, 1);
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

