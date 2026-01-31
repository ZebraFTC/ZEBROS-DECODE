package org.firstinspires.ftc.teamcode.mechanisms;

import android.util.Size;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class TestAprilTagCode {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    private Telemetry telemetry;

    // Control Gains - tune these for your robot's responsiveness
    final double SPEED_GAIN  =  0.02;   // Forward/back (reduced for stability)
    final double STRAFE_GAIN =  0.015;  // Left/right
    final double TURN_GAIN   =  0.01;   // Rotation

    // Deadbands - minimum error before correcting
    final double RANGE_DEADBAND = 2.0;   // cm
    final double STRAFE_DEADBAND = 1.0;  // degrees
    final double YAW_DEADBAND = 2.0;     // degrees

    // Power limits
    final double MAX_DRIVE = 0.5;
    final double MAX_STRAFE = 0.5;
    final double MAX_TURN = 0.3;

    public void init(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);
        visionPortal = builder.build();
    }

    public void update() {
        detectedTags = aprilTagProcessor.getDetections();
    }

    /**
     * Calculates powers for Mecanum drive to align to a specific tag.
     * Returns an array: [drive, strafe, turn]
     *
     * KEY FIX: All three axes correct simultaneously for smooth convergence
     */
    public double[] getAlignmentPowers(int targetId, double targetDistanceCm) {
        AprilTagDetection tag = getTagById(targetId);

        if (tag == null) return new double[]{0, 0, 0};

        // Calculate errors from target
        double rangeError = tag.ftcPose.range - targetDistanceCm;
        double yawError = tag.ftcPose.yaw;
        double strafeError = tag.ftcPose.bearing;

        // Apply deadbands FIRST (ignore tiny errors)
        if (Math.abs(rangeError) < RANGE_DEADBAND) rangeError = 0;
        if (Math.abs(strafeError) < STRAFE_DEADBAND) strafeError = 0;
        if (Math.abs(yawError) < YAW_DEADBAND) yawError = 0;

        // Calculate ALL corrections simultaneously (this is the key!)
        double drive = rangeError * SPEED_GAIN;
        double strafe = strafeError * STRAFE_GAIN;
        double turn = yawError * TURN_GAIN;

        // Clip to safe maximums
        drive = Range.clip(drive, -MAX_DRIVE, MAX_DRIVE);
        strafe = Range.clip(strafe, -MAX_STRAFE, MAX_STRAFE);
        turn = Range.clip(turn, -MAX_TURN, MAX_TURN);

        // Telemetry for debugging
        telemetry.addData("Range Error (cm)", "%.1f", rangeError);
        telemetry.addData("Bearing Error (deg)", "%.1f", strafeError);
        telemetry.addData("Yaw Error (deg)", "%.1f", yawError);
        telemetry.addData("Drive | Strafe | Turn", "%.2f | %.2f | %.2f", drive, strafe, turn);

        return new double[]{0, strafe, turn};
    }

    public AprilTagDetection getTagById(int id) {
        for (AprilTagDetection detection : detectedTags) {
            if (detection.id == id) return detection;
        }
        return null;
    }

    public void stop(){
        if (visionPortal != null) visionPortal.close();
    }
}