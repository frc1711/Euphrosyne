package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSystem extends SubsystemBase {
	
	public CameraSystem () {
		// Initializes USB cameras
		for (CameraOption option : CameraOption.values())
			option.initCamera();
		
		// Initializes the camera server to the first given camera
		server = CameraServer.addSwitchedCamera("Camera");
		setCamera(CameraOption.values()[0]);
	}
	
	private VideoSink server;
	private UsbCamera currentCamera;
	
	public static enum CameraOption {
		CAMERA_1 (0),
		CAMERA_2 (1);
		
		private final int dev;
		private UsbCamera camera;
		
		private CameraOption (int dev) {
			this.dev = dev;
		}
		
		private void initCamera () {
			this.camera = new UsbCamera(this.name(), dev);
		}
	}
	
	// Keeps track of the current index so we can cycle through one at a time
	private int currentCameraOptionIndex;
	
	public void setCamera (CameraOption option) {
		if (currentCamera == option.camera) return;
		
		// Updates the camera options index
		currentCameraOptionIndex = getCameraOptionsIndex(option);
		
		// Updates the current camera
		currentCamera = option.camera;
		
		// Updates the server
		server.setSource(currentCamera);
	}
	
	private int getCameraOptionsIndex (CameraOption option) {
		CameraOption[] options = CameraOption.values();
		for (int i = 0; i < options.length; i ++)
			if (options[i] == option) return i;
		return 0;
	}
	
	public void nextCamera () {
		int nextIndex = putCameraOptionsIndexInBounds(currentCameraOptionIndex + 1);
		setCamera(CameraOption.values()[nextIndex]);
	}
	
	public void previousCamera () {
		int nextIndex = putCameraOptionsIndexInBounds(currentCameraOptionIndex - 1);
		setCamera(CameraOption.values()[nextIndex]);
	}
	
	private int putCameraOptionsIndexInBounds (int index) {
		if (index < 0) return CameraOption.values().length - 1;
		if (index >= CameraOption.values().length) return 0;
		return index;
	}
	
}