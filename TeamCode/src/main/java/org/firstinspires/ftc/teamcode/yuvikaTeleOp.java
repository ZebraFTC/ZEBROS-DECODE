package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class yuvikaTeleOp extends LinearOpMode {
    //defining motor variables
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;
    public Servo Arm;
    @Override
    public void runOpMode() throws InterruptedException {
        //hardware maps motors, tells the algorithm where they're plugged in
        FrontLeft = hardwareMap.get(DcMotor.class, "FL");
        FrontRight = hardwareMap.get(DcMotor.class, "FR");
        BackLeft = hardwareMap.get(DcMotor.class, "BL");
        BackRight = hardwareMap.get(DcMotor.class, "BR");
        Arm = hardwareMap.get(Servo.class, "arm");
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart(); //runs everything above, then waits for init to finish code
        while (opModeIsActive()) {
            //set gamepads and controls
            double drive = gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;
            FrontLeft.setPower(drive+turn-strafe);
            FrontRight.setPower(drive-turn+strafe);
            BackLeft.setPower(drive+turn+strafe);
            BackRight.setPower(drive-turn-strafe);
            if (gamepad1.a) {
                Arm.setPosition(0.5);
            } else if (gamepad1.b) {
                Arm.setPosition(0.7);
            }
        }
    }

}
