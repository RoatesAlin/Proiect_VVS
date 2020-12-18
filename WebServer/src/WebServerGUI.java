import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebServerGUI {
    private JButton startButton;
    private JPanel mainPanel;
    private JButton stopButton;
    private JButton maintenanceButton;
    private JTextField port;
    private JTextField root;
    private JTextField maintenance;
    private JButton changeRoot;
    private JButton changeMaintenance;
    private JTextField status;

    public WebServerGUI() {

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "WebPage Started");
            }
        } );

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "WebPage Stopped");
            }
        } );

        maintenanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "WebPage Maintenance");
            }
        } );

        changeRoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Changing WebServer Root");
            }
        } );

        changeMaintenance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Changing WebServer Maintenance");
            }
        } );

        JTextField port = new JTextField("WebServer listening on port:");
        port.setBorder(null);

        JTextField root = new JTextField("WebServer root directory:");
        port.setBorder(null);

        JTextField maintenance = new JTextField("WebServer maintenance directory:");
        port.setBorder(null);

        JTextField status = new JTextField("WebServer status:");
        port.setBorder(null);
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("WebServerGUI");
        frame.setContentPane(new WebServerGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
