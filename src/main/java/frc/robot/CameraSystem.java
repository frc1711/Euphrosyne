package frc.robot;

import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;

public class CameraSystem {
	
	private final UsbCamera[] usbCameras;
	private final MjpegServer server;
	
	public CameraSystem (int numCameras) {
		// Creates the array of usb camera video sources
		usbCameras = new UsbCamera[numCameras];
		for (int dev = 0; dev < numCameras; dev ++)
			usbCameras[dev] = new UsbCamera("UsbCamera["+dev+"]", dev);
		
		// Initializes the camera server
		server = new MjpegServer("MjpegServer", 1181);
		if (numCameras > 0) server.setSource(usbCameras[0]);
	}
	
	public void activateCameraNum (int num) {
		server.setSource(usbCameras[num]);
	}
	
}