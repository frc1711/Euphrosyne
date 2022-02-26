package frc.robot.commands;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class CameraSystem extends CommandBase {
	
	public CameraSystem () {}
	
	private MjpegServer server;
	private UsbCamera currentCamera;
	
	private SendableChooser<CameraOption> cameraChooser;
	
	private static enum CameraOption {
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
	
	@Override
	public void initialize () {
		// Initializes USB cameras
		for (CameraOption option : CameraOption.values())
			option.initCamera();
		
		// Initializes the camera server to the first given camera
		server = CameraServer.addSwitchedCamera("MjpegServer");
		setCamera(CameraOption.values()[0]);
		
		// Initializes the SendableChooser to select cameras
		cameraChooser = new SendableChooser<CameraOption>();
		for (CameraOption option : CameraOption.values())
			cameraChooser.addOption(option.name(), option);
		cameraChooser.setDefaultOption(CameraOption.values()[0].name(), CameraOption.values()[0]);
	}
	
	@Override
	public void execute () {
		setCamera(cameraChooser.getSelected());
	}
	
	@Override
	public boolean isFinished () {
		return false;
	}
	
	private void setCamera (CameraOption option) {
		if (currentCamera == option.camera) return;
		currentCamera = option.camera;
		server.setSource(currentCamera);
	}
	
}