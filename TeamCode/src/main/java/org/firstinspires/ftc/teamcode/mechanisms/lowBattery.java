package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Green Giant LB")
public class lowBattery extends LinearOpMode {
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor Intake, Transfer, Shooter;
    Servo Flap;
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
        Flap = hardwareMap.get(Servo.class, "flap");

        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

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

            /*if (gamepad2.right_bumper) {
                aprilTagDebug();
            } else {
                driverControl();
            }*/

            if (gamepad2.a) {
                nudgeBall(0.3);
            }

            Intake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
            Transfer.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

            if (gamepad2.y) Shooter.setPower(1);
            if (gamepad2.x) Shooter.setPower(0);
            if (gamepad2.left_bumper) Shooter.setPower(-1);

        }


        aprilTag.stop();
    }

    private void driverControl() {
        double drive = -1 * gamepad1.left_stick_y;
        double strafe = -1 * gamepad1.left_stick_x;
        double turn = 0.85 * gamepad1.right_stick_x;

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
            Shooter.setPower(1);
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