package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous
public class EncoderTestAuto extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor Intake;
    private DcMotor Transfer;
    private DcMotor Shooter;
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


    AutoCase selectedCase = AutoCase.taxi_auto;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");


        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


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
        drive(-100, -100, -100, -100, false);
//
//
//        drive(0.5, -0.5, 0.5, -0.5, 0.267, false);
//        drive(0.5, -0.5, -0.5, 0.5, 0.795, false);
    }


    public void runThreeBall() {
        Shooter.setPower(0.7);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.4);
        Shooter.setPower(-0.5);


//        drive(0.5, -0.5, 0.5, -0.5, 0.267, false); // leave zone
//        drive(0.5, -0.5, -0.5, 0.5, 0.795, false);
    }


    public void runSixBall() {
        Shooter.setPower(0.7);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.4);
        Shooter.setPower(-0.5);


//        drive(0.5, -0.5, 0.5, -0.5, 0.267  , false);
//        drive(0.5, -0.5, -0.5, 0.5, 0.795, false );
//        drive(0.5,0.5,0.5,0.5,2,true);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.35, false);
//        drive(-0.5, 0.5, -0.5, 0.5, 0.3, false); // turn for shooter second time
//        drive(0.5, 0.5, 0.5, 0.5, 0.25, false);


        shootThreeBall();


//        drive(0.5, -0.5, 0.5, -0.5, 0.28, false); // leave zone
//        drive(0.5, -0.5, -0.5, 0.5, 1, false);
//        drive(0.5,0.5,0.5,0.5,1,false);
    }
    public void runNineBall() {
        Shooter.setPower(0.7);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.15, false);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.4);
        Shooter.setPower(-0.5);


//        drive(0.5, -0.5, 0.5, -0.5, 0.267  , false);
//        drive(0.5, -0.5, -0.5, 0.5, 0.795, false );
//        drive(0.5,0.5,0.5,0.5,2,true);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.35, false);
//        drive(-0.5, 0.5, -0.5, 0.5, 0.3, false); // turn for shooter second time
//        drive(0.5, 0.5, 0.5, 0.5, 0.25, false);


        shootThreeBall();


//        drive(0.5, -0.5, 0.5, -0.5, 0.35, false);
//        drive(0.5, -0.5, -0.5, 0.5, 1.9, false);
//        drive(0.5, 0.5, 0.5, 0.5, 1.8, true);
//        drive(-0.5, -0.5, -0.5, -0.5, 1.4, false);
//        drive(-0.5, 0.5, 0.5, -0.5, 1.8, false );
//        drive(-0.5, 0.5, -0.5, 0.5, 0.4, false); // turn for shooter third time
//        drive(0.5, 0.5, 0.5, 0.5, 0.2, false);


        while (opModeIsActive() && timer.seconds() < 2) {
            telemetry.addLine("Shooter turning on...");
        }
        telemetry.update();
        nudgeBall(0.25);
        nudgeBall(0.53);


        //drive(-1, 1, 1, -1, 0.6, false); // leave zone
        //drive(0.5, 0.5, 0.5, 0.5, 0.2, false);
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




    public void drive(int FLTarget, int FRTarget, int BLTarget, int BRTarget, boolean intakeAndTransfer) {

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + FLTarget);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + FRTarget);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + BLTarget);
        backRight.setTargetPosition(backRight.getCurrentPosition() + BRTarget);

        frontLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backLeft.setPower(0.5);
        backRight.setPower(0.5);

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
//        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

