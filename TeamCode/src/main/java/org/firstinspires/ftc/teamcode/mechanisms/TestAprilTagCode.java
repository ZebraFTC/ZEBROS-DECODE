package org.firstinspires.ftc.teamcode.mechanisms;

import android.util.Size;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range; // Added for clipping power
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

    // Control Gains: Adjust these to make alignment smoother or faster
    final double SPEED_GAIN  =  0.05;   // Forward speed control
    final double STRAFE_GAIN =  0.015;  // Side-to-side control
    final double TURN_GAIN   =  0.01;   // Rotation control

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
     */
    public double[] getAlignmentPowers(int targetId, double targetDistanceCm) {
        AprilTagDetection tag = getTagById(targetId);

        if (tag == null) return new double[]{0, 0, 0};

        // Determine errors
        double rangeError = (tag.ftcPose.range - targetDistanceCm);
        double headingError = tag.ftcPose.bearing;
        double yawError = (tag.ftcPose.yaw);
        if (targetId == 24) {
            yawError = (tag.ftcPose.yaw - 20);
        } else if (targetId == 20) {
            yawError = (tag.ftcPose.yaw + 5);
        }


        // Calculate drive, strafe and yaw for mecanum
        double drive  = Range.clip(rangeError * SPEED_GAIN, -0.3, 0.3);
        double turn   = Range.clip(headingError * TURN_GAIN, -0.25, 0.25);
        double strafe = Range.clip(-yawError * STRAFE_GAIN, -0.25, 0.25);

        return new double[]{drive, strafe, turn};
    }

    // ... (Your existing getTagById, displayTelemetry, and stop methods remain the same)
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