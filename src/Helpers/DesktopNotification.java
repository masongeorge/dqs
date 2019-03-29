package Helpers;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class DesktopNotification {
    public static void displayTray(String user, int AssessmentCount) throws AWTException, MalformedURLException {
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("You have got new assessments!");
        tray.add(trayIcon);

        trayIcon.displayMessage(String.format("Welcome back, ", user), String.format("You have got %d new tests!", AssessmentCount), MessageType.INFO);
    }
}