package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.mechanisms.TestAprilTagCode;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "April Tag TeleOp")
public class TestTeleOpAprilTag extends LinearOpMode {
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor Intake, Transfer, Shooter;
    Servo Flap;
    public double shooterPower;
    private static ElapsedTime timer = new ElapsedTime();

    TestAprilTagCode aprilTag = new TestAprilTagCode();

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
        Flap = hardwareMap.get(Servo.class, "flap");

        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        Shooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        aprilTag.init(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            // 1. Refresh camera data
            aprilTag.update();

            // 2. Default movement to Joysticks
            double drive = -1 * gamepad1.left_stick_y;
            double strafe = -1 * gamepad1.left_stick_x;
            double turn = 0.5 * gamepad1.right_stick_x;

            // 3. AprilTag Override
            if (gamepad1.a) {
                // Get powers to align to the tag at 20cm distance
                double[] autoPowers = aprilTag.getAlignmentPowers(TARGET_TAG_ID, 100.0);

                // Check if the tag is actually in view before overriding
                if (aprilTag.getTagById(TARGET_TAG_ID) != null) {
                    drive  = autoPowers[0];
                    strafe = autoPowers[1];
                    turn   = autoPowers[2];
                    telemetry.addLine("ALIGNING TO TAG...");
                } else {
                    telemetry.addLine("TAG NOT FOUND - MANUAL CONTROL ONLY");
                }
            }

            if (gamepad2.dpad_up) {
                shooterPower = 1;
            }
            else if (gamepad2.dpad_left) {
                shooterPower = 0.7;
            }
            else if (gamepad2.dpad_right) {
                shooterPower = 0.8;
            }
            else if (gamepad2.dpad_down) {
                shooterPower = 0.9;
            }
            else if (gamepad2.x) {
                shooterPower = 0;
            }
            Shooter.setPower(shooterPower);
            telemetry.addData("Shooter Power: ", shooterPower);
            telemetry.update();

            if (gamepad2.a) {
                nudgeBall(0.3);
            }

            // 4. FINAL MOTOR OUTPUT (Keep this at the bottom of the loop)
            setDrivePower(drive, strafe, turn);

            // 5. Handling other mechanisms (Non-blocking)
            if (gamepad1.dpad_left)  TARGET_TAG_ID = 20;
            if (gamepad1.dpad_right) TARGET_TAG_ID = 24;

            telemetry.addData("Target Tag", TARGET_TAG_ID);
            if (TARGET_TAG_ID == 20) {
                telemetry.addLine("BLUE ALLIANCE");
            } else if (TARGET_TAG_ID == 24) {
                telemetry.addLine("RED ALLIANCE");
            }
            telemetry.update();

            Intake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
            Transfer.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

            if (gamepad2.left_bumper) Shooter.setPower(-1);
        }


        aprilTag.stop();
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

    private void setDrivePower(double drive, double strafe, double turn) {
        FrontLeft.setPower(drive + turn - strafe);
        FrontRight.setPower(drive - turn + strafe);
        BackLeft.setPower(drive + turn + strafe);
        BackRight.setPower(drive - turn - strafe);
    }
}