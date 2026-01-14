package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class greengiant3 extends LinearOpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private static ElapsedTime timer = new ElapsedTime();
    //defining motor variables
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;
    public DcMotor Intake;
    public DcMotor Transfer;
    public DcMotor Shooter;
    public Servo Flap;
    @Override
    public void runOpMode() throws InterruptedException {
        //hardware maps motors, tells the algorithm where they're plugged in
        FrontLeft = hardwareMap.get(DcMotor.class, "FL");
        FrontRight = hardwareMap.get(DcMotor.class, "FR");
        BackLeft = hardwareMap.get(DcMotor.class, "BL");
        BackRight = hardwareMap.get(DcMotor.class, "BR");
        Flap = hardwareMap.get(Servo.class, "flap");
        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart(); //runs everything above, then waits for init to finish code
        while (opModeIsActive()) {
            if (gamepad1.a) {
                repositionBot();
            }
        }
    }
    public void drive(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower, double time, boolean intakeAndTransfer) {
        timer.reset();
        while (opModeIsActive() && timer.seconds() < time) {     // While the timer is running
            FrontLeft.setPower(frontLeftPower);
            FrontRight.setPower(frontRightPower);
            BackLeft.setPower(backLeftPower);
            BackRight.setPower(backRightPower);
            if (intakeAndTransfer) {              // If set toss4- true it runs the intake and the transfer
                Intake.setPower(-0.75);
                Transfer.setPower(-0.75);
            }

        }
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
        Intake.setPower(0);
        Transfer.setPower(0);
    }
    AprilTagDetection id23 = aprilTagWebcam.getTagById(23);
    public void repositionBot() {
        if (aprilTagWebcam.getYaw(id23) != 0.0) {
            if (aprilTagWebcam.getYaw(id23) > 0.0) {
                drive(0.5, -0.5, 0.5, -0.5, 3, false);
            } else if (aprilTagWebcam.getYaw(id23) < 0.0) {
                drive(-0.5, 0.5, -0.5, 0.5, 3, false);
            } else {
                drive(0,0,0,0,0.1,false);
            }
        }
    } 
}