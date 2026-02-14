package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "GREENGiant")
public class greengiant3 extends LinearOpMode {

    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor Intake, Transfer, Shooter, ShooterAssist;

    public double shooterPower = 0;
    private static ElapsedTime timer = new ElapsedTime();

    AprilTagWebcam aprilTag = new AprilTagWebcam();

    static int TARGET_TAG_ID = 1;

    // Drive speed toggle
    private boolean fastMode = false;
    private boolean lastRightBumperState = false;

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

            // ================= DRIVE SPEED TOGGLE =================
            if (gamepad1.right_bumper && !lastRightBumperState) {
                fastMode = !fastMode;
            }
            lastRightBumperState = gamepad1.right_bumper;

            // ================= SHOOTER SPEED SELECTION =================
            if (gamepad2.dpad_up) {
                shooterPower = 1.0;
            }
            else if (gamepad2.dpad_left) {
                shooterPower = 0.9;
            }
            else if (gamepad2.dpad_right) {
                shooterPower = 0.8;
            }
            else if (gamepad2.dpad_down) {
                shooterPower = 0.85;
            }
            else if (gamepad2.b) {
                shooterPower = 0.95;
            }

            // ================= SHOOTER CONTROL =================
            if (gamepad2.y) {  // HOLD to reverse
                Shooter.setPower(-shooterPower);
                ShooterAssist.setPower(shooterPower);
            }
            else if (gamepad2.x) {  // Stop shooter
                Shooter.setPower(0);
                ShooterAssist.setPower(0);
            }
            else {  // Normal forward
                Shooter.setPower(shooterPower);
                ShooterAssist.setPower(-shooterPower);
            }

            // ================= DRIVER CONTROL =================
            driverControl();

            // ================= INTAKE =================
            Intake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

            // ================= TRANSFER =================
            if (gamepad2.right_bumper) {
                Transfer.setPower(-1);
            }
            else if (gamepad2.left_bumper) {
                Transfer.setPower(1);
            }
            else {
                Transfer.setPower(0);
            }

            if (gamepad2.a) {
                Intake.setPower(0);
                Transfer.setPower(0);
            }

            telemetry.addData("Fast Mode", fastMode);
            telemetry.addData("Shooter Power", shooterPower);
            telemetry.update();
        }

        aprilTag.stop();
    }

    private void driverControl() {

        double driveMultiplier;

        // HOLD X for slow precision mode
        if (gamepad1.x) {
            driveMultiplier = -0.6;
        }
        // Fast toggle mode
        else if (fastMode) {
            driveMultiplier = -1.0;   // 1.0 is max motor power
        }
        // Normal mode
        else {
            driveMultiplier = -0.8;
        }

        double drive = driveMultiplier * gamepad1.left_stick_y;
        double strafe = driveMultiplier * gamepad1.left_stick_x;
        double turn = 0.5 * gamepad1.right_stick_x;

        setDrivePower(drive, strafe, turn);
    }

    private void setDrivePower(double drive, double strafe, double turn) {
        FrontLeft.setPower(drive + turn - strafe);
        FrontRight.setPower(drive - turn + strafe);
        BackLeft.setPower(drive + turn + strafe);
        BackRight.setPower(drive - turn - strafe);
    }
}
