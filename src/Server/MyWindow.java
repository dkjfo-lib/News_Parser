package Server;

import Parsers.ParsersTimer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Date;

public class MyWindow extends JFrame {
    public static  MyWindow instance;

    public static void main(String[] args) {
        MyWindow window = new MyWindow();
    }

    private final static String[] buttonText = new String[]{
            "Start Timer", "Stop Timer"
    };

    private JTextArea messageLog;
    private ServerSocket serverSocket;
    private final ParsersTimer parsersTimer;

    private MyWindow() {
        instance = this;
        parsersTimer = new ParsersTimer(this);

        JPanel mainPanel = getMainPanel();
        setContentPane(mainPanel);
        messageLog = new JTextArea();
        messageLog.setColumns(20);
        messageLog.setLineWrap(true);
        messageLog.setRows(5);
        messageLog.setWrapStyleWord(true);
        messageLog.setEditable(false);
        JScrollPane jScrollPane1 = new JScrollPane(messageLog);
        messageLog.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, Color.black));
        mainPanel.add(jScrollPane1, BorderLayout.CENTER);

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(2, 1));
        lowPanel.add(getLowServerPanel());
        lowPanel.add(getLowTimerPanel());
        mainPanel.add(lowPanel, BorderLayout.SOUTH);
//        imageComponent = getImageFrame();
//        mainPanel.add(imageComponent, BorderLayout.CENTER);
        //mainPanel.add(getParametersPanel(), BorderLayout.SOUTH);

        setMiscSettings();
    }

    void setMiscSettings() {
        setTitle("News Aggregator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
    }

    JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        return mainPanel;
    }

    JComponent getLowTimerPanel() {
        JLabel label = new JLabel();
        label.setText("Timer status : OFF");


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton startButton = new JButton();
        startButton.setText("Start Timer");
        JButton stopButton = new JButton();
        stopButton.setText("Stop Timer");
        stopButton.setEnabled(false);
        startButton.addActionListener((e) -> {
            Thread timerThread = new Thread(parsersTimer);
            timerThread.setDaemon(true);
            timerThread.start();
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            label.setText("Timer status : ON");
        });
        stopButton.addActionListener((e) -> {
            parsersTimer.run = false;
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            label.setText("Timer status : OFF");
        });
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(1, 2));
        editPanel.add(new Label("Timer delay in minutes : "));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(label, BorderLayout.CENTER);

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(2, 1));
        lowPanel.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, Color.black));
        lowPanel.add(labelPanel);
        lowPanel.add(buttonPanel);
        return lowPanel;
    }

    JComponent getLowServerPanel() {
        JLabel label = new JLabel();
        label.setText("Server status : OFF");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton startButton = new JButton();
        startButton.setText("Start Server");
        JButton stopButton = new JButton();
        stopButton.setText("Stop Server");
        stopButton.setEnabled(false);
        startButton.addActionListener((e) -> {
            writeLog("Server start");
            try {
                serverSocket = new ServerSocket();
                Thread serverThread = new Thread(new Server(serverSocket, this));
                serverThread.setDaemon(true);
                serverThread.start();
                label.setText("Server status : ON");
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            } catch (Exception e1) {
                writeLog("Server closed");
                label.setText("Server status : OFF");
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
        stopButton.addActionListener((e) -> {
            try {
                serverSocket.close();
            } catch (IOException e1) {
            }
            writeLog("Server closed");
            label.setText("Server status : OFF");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.add(label, BorderLayout.CENTER);

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new GridLayout(2, 1));
        lowPanel.setBorder(BorderFactory.createMatteBorder(
                1, 1, 1, 1, Color.black));
        lowPanel.add(labelPanel);
        lowPanel.add(buttonPanel);
        return lowPanel;
    }

    public void writeLog(String message) {
        messageLog.setText(" " + new Date() + " : " + message + "\n" + messageLog.getText());
    }
}
