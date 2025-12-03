package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanism.AprilTagWebcamVs;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class AprilTagWebcamExample extends OpMode {
    AprilTagWebcamVs aprilTagWebcam = new AprilTagWebcamVs();

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }
    @Override
    public void loop() {
        // update the vision portal
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificID(2);
        aprilTagWebcam.displayDetectionTelemetry(id20);

    }
}
