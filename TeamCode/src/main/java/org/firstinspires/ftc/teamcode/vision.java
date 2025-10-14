package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp
public class vision extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // adding an april tag processor
        AprilTagProcessor tagProcessor = new AprilTagProcessor.Builder()
                // draws where the axes are pointing
                .setDrawAxes(true)
                // helps further see where the camera thinks the tag is pointing
                .setDrawCubeProjection(true)
                // draws the id number on the tag
                .setDrawTagID(true)
                // draws the outline of the tag
                .setDrawTagOutline(true)
                .build();

        // adding a vision portal
        VisionPortal visionPortal = new VisionPortal.Builder()
                // adds vision processor and links it to tag processor
                .addProcessor(tagProcessor)
                // hardware maps the webcam
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .build();

        waitForStart();

        while (!isStopRequested() && opModeIsActive()) {
            // if the processor detects something
            if (!tagProcessor.getDetections().isEmpty()) {
                AprilTagDetection tag = tagProcessor.getDetections().get(0);
                // allows the data to be outputted on driver station
                telemetry.addData("x", tag.ftcPose.x);
                telemetry.addData("y", tag.ftcPose.y);
                telemetry.addData("z", tag.ftcPose.z);
                telemetry.addData("roll", tag.ftcPose.roll);
                telemetry.addData("pitch", tag.ftcPose.pitch);
                telemetry.addData("yaw", tag.ftcPose.yaw);

            }
            telemetry.update();
        }
    }
}
