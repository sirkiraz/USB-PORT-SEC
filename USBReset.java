import java.io.*;
import java.util.*;

public class USBReset {

    private static final String USB_DEVICE_PATH = "/media/usb0";
    private static final String USB_RESET_COMMAND = "sudo usbreset /dev/bus/usb/001/001";

    public static void main(String[] args) {
        // Create a File object for the root directory of the USB drive
        File root = new File(USB_DEVICE_PATH);

        // Set up a Timer to check for changes to the root directory every 1000 milliseconds (1 second)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                // Check if the root directory exists
                if (root.exists()) {
                    // If the root directory exists, check if it was added or removed since the last check
                    File[] files = root.listFiles();
                    if (files == null || files.length == 0) {
                        // If the root directory is empty, it was probably removed
                        System.out.println("USB device removed");
                    } else {
                        // If the root directory is not empty, it was probably added
                        System.out.println("USB device inserted");
                        // Reset the USB device
                        resetUSBDevice();
                    }
                } else {
                    // If the root directory does not exist, it was probably removed
                    System.out.println("USB device removed");
                }
            }
        }, 0, 1000);
    }

    private static void resetUSBDevice() {
        try {
            Process process = Runtime.getRuntime().exec(USB_RESET_COMMAND);
            process.waitFor();

            // Check the exit value of the process to see if the command was successful
            int exitValue = process.exitValue();
            if (exitValue == 0) {
                System.out.println("USB device reset successful");
            } else {
                System.out.println("USB device reset failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
