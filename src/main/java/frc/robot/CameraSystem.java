package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;

public class CameraSystem {
	
	private final UsbCamera[] usbCameras;
	private final MjpegServer server;
	
	private UsbCamera currentCamera;
	
	public CameraSystem (int numCameras) {
		// Creates the array of usb camera video sources
		usbCameras = new UsbCamera[numCameras];
		for (int dev = 0; dev < numCameras; dev ++)
			usbCameras[dev] = new UsbCamera("UsbCamera["+dev+"]", dev);
		
		// Initializes the camera server
		server = CameraServer.addSwitchedCamera("MjpegServer");
		if (numCameras > 0) activateCameraNum(0);
	}
	
	public void activateCameraNum (int num) {
		if (currentCamera == usbCameras[num]) return;
		currentCamera = usbCameras[num];
		server.setSource(currentCamera);
	}
	
}