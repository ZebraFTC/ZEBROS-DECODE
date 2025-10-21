package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class greengiant2 extends LinearOpMode {
    //defining motor variables
    public DcMotor FrontLeft;
    public DcMotor FrontRight;
    public DcMotor BackLeft;
    public DcMotor BackRight;
    public DcMotor Intake;
    public DcMotor Transfer;
    public DcMotor Shooter;
    public Servo Flap;

    double integralSum = 0;
    double Kp = 0;
    double Ki = 0;
    double Kd = 0;

    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        //hardware maps motors, tells the algorithm where they're plugged in
        FrontLeft = hardwareMap.get(DcMotor.class, "FL");
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRight = hardwareMap.get(DcMotor.class, "FR");
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeft = hardwareMap.get(DcMotor.class, "BL");
        BackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRight = hardwareMap.get(DcMotor.class, "BR");
        BackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Flap = hardwareMap.get(Servo.class, "flap");
        Intake = hardwareMap.get(DcMotor.class, "intake");
        Transfer = hardwareMap.get(DcMotor.class, "transfer");
        Shooter = hardwareMap.get(DcMotor.class, "shooter");
        FrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart(); //runs everything above, then waits for init to finish code
        while (opModeIsActive()) {
            //set gamepads and controls
            double powerFL = PIDControl(100, FrontLeft.getCurrentPosition());
            double powerFR = PIDControl(100, FrontRight.getCurrentPosition());
            double powerBL = PIDControl(100, BackLeft.getCurrentPosition());
            double powerBR = PIDControl(100, BackRight.getCurrentPosition());

            double drive = -0.5*gamepad1.left_stick_y;
            double strafe = -0.5*gamepad1.left_stick_x;
            double turn = 0.5*gamepad1.right_stick_x;
            /**FrontLeft.setPower(drive+turn-strafe);
            FrontRight.setPower(drive-turn+strafe);
            BackLeft.setPower(drive+turn+strafe);
            BackRight.setPower(drive-turn-strafe);**/
            FrontLeft.setPower(powerFL * (drive+turn-strafe));
            FrontRight.setPower(powerFR * (drive-turn+strafe));
            BackLeft.setPower(powerBL * (drive+turn+strafe));
            BackRight.setPower(powerBR * (drive-turn-strafe));

            if (gamepad2.a) {
                Flap.setPosition(0.3);
            } else  {
                Flap.setPosition(0);
            }

            Intake.setPower(gamepad2.right_trigger-gamepad2.left_trigger);
            Transfer.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

            if (gamepad2.y) {
                Shooter.setPower(0.9);
            }
            else if (gamepad2.b) {
                Shooter.setPower(-0.5);
            }
            else if (gamepad2.x) {
                Shooter.setPower(0);
            }


        }
    }

    public double PIDControl(double reference, double state) {
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki);
        return output;
    }

}
