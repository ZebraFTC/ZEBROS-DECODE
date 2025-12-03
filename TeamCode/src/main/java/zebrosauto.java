import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="zebrosauto", group="Autonomous")
public class zebrosauto extends LinearOpMode {

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Initialize hardware
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // Set motor directions if needed
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Autonomous actions
        if (opModeIsActive()) {
            // Drive forward for 2 seconds
            leftDrive.setPower(0.5);
            rightDrive.setPower(0.5);
            runtime.reset();
            while (opModeIsActive() && runtime.seconds() < 2.0) {
                telemetry.addData("Path", "Driving Forward: %4.1f S Elapsed", runtime.seconds());
                telemetry.update();
            }

            // Stop motors
            leftDrive.setPower(0);
            rightDrive.setPower(0);

            // Add more actions as needed (e.g., turn, grab, place)
        }
    }
}