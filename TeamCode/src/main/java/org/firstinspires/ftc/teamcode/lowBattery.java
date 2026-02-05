package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Green Giant")
public class lowBattery extends LinearOpMode {
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor Intake, Transfer, Shooter, ShooterAssist;
    public double shooterPower;
    private static ElapsedTime timer = new ElapsedTime();

    AprilTagWebcam aprilTag = new AprilTagWebcam();

    static int TARGET_TAG_ID = 1; // CHANGE IF NEEDED

    @Override
    public void runOpMode() {

        FrontLeft = hardwareMap.get(DcMotor.class, "FL");
        FrontRight = hardwareMap.get(DcMotor.class, "FR");
        BackLeft = hardwareMap.get(DcMotor.class, "BL");
        BackRight = hardwareMap.get(DcMotor.class, "BR");

        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");
        ShooterAssist = hardwareMap.get(DcMotor.class, "shootertwo");

        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ShooterAssist.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        aprilTag.init(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.dpad_left) {
                TARGET_TAG_ID = 20;
                telemetry.addLine("Blue Alliance");
            }
            if (gamepad1.dpad_right) {
                TARGET_TAG_ID = 24;
                telemetry.addLine("Red Alliance");
            }
            telemetry.update();

            if (gamepad2.dpad_up) {
                shooterPower = 1;
            }
            else if (gamepad2.dpad_left) {
                shooterPower = 0.9;
            }
            else if (gamepad2.dpad_right) {
                shooterPower = 0.8;
            }
            else if (gamepad2.dpad_down) {
                shooterPower = 0.7;
            }
            else if (gamepad2.x) {
                shooterPower = 0;
            }
            Shooter.setPower(shooterPower);
            ShooterAssist.setPower(-shooterPower);
            telemetry.addData("Shooter Power: ", shooterPower);

            driverControl();

            Intake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
            if (gamepad2.right_bumper) {
                Transfer.setPower(-1);
            } else {
                Transfer.setPower(0);
            }
            if (gamepad2.left_bumper) {
                Transfer.setPower(1);
            } else {
                Transfer.setPower(0);
            }

            if (gamepad2.x) {
                Shooter.setPower(0);
                ShooterAssist.setPower(0);
            }
            if (gamepad2.y) {
                Shooter.setPower(-1);
                ShooterAssist.setPower(1);
            }

            if (gamepad2.a) {
                shootThreeBall();
            }

        }


        aprilTag.stop();
    }

    private void driverControl() {
        double drive_var = -1;
        if (gamepad1.x) {
            drive_var = -0.8;
        }
        if (gamepad1.y) {
            drive_var = -1;
        }
        double drive = drive_var * gamepad1.left_stick_y;
        double strafe = drive_var * gamepad1.left_stick_x;
        double turn = 0.5 * gamepad1.right_stick_x;
        telemetry.addData("Drive Power: ", drive_var);
        telemetry.update();

        setDrivePower(drive, strafe, turn);
    }

    private void aprilTagDebug() {
        AprilTagDetection tag = aprilTag.getTagById(TARGET_TAG_ID);

        // Always STOP in debug mode
        setDrivePower(0, 0, 0);

        if (tag == null) {
            telemetry.addLine("Searching for tag ID " + TARGET_TAG_ID);
        } else {
            telemetry.addLine("TARGET TAG FOUND!");
            telemetry.addData("Tag ID", tag.id);
            telemetry.addData("Yaw (deg)", tag.ftcPose.yaw);
            telemetry.addData("Range (cm)", tag.ftcPose.range);
        }

        telemetry.update();
    }
    private void nudgeBall(double intakeTime) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < intakeTime) {
            Intake.setPower(-1);
            Transfer.setPower(-1);
            idle();
        }
        timer.reset();
        while (opModeIsActive() && timer.seconds() < 1) {
            Intake.setPower(0);
            Transfer.setPower(0);
            idle();
        }
    }

    private void shootThreeBall() {
        nudgeBall(0.25);
        nudgeBall(0.5);
        nudgeBall(0.4);
        Shooter.setPower(0);
        ShooterAssist.setPower(0);

    }

    private void setDrivePower(double drive, double strafe, double turn) {
        FrontLeft.setPower(drive + turn - strafe);
        FrontRight.setPower(drive - turn + strafe);
        BackLeft.setPower(drive + turn + strafe);
        BackRight.setPower(drive - turn - strafe);
    }
}