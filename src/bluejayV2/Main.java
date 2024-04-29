package bluejayV2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import bluejayDB.EmployeeDatabase;

// Represents the splash screen with a background image and progress bar


// Main application logic with splash screen and progress update
public class Main {

    public static GUI frame;
    public static EmployeeDatabase DB;
    public static Employee emp;

    public static void main(String[] args) {

        SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);

        // Simulate background work
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                splashScreen.setStatus("Connecting to Database...");
                splashScreen.updateProgress(20); // Simulate progress

                Thread.sleep(2000); // Simulate delay

                try {
					DB = new EmployeeDatabase();
					
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                splashScreen.setStatus("Setting up GUI...");
                splashScreen.updateProgress(60); // Simulate progress

                frame = new GUI("Login", new LoginPanel(DB), 1200, 700, true, false);
                frame.isDark(false);

                Thread.sleep(2000); // Simulate delay
                splashScreen.updateProgress(100); // Complete progress

                SwingUtilities.invokeLater(() -> {
    				FlatAnimatedLafChange.showSnapshot();
                    splashScreen.setVisible(false);
                    frame.setVisible(true);

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        });

    }
}
