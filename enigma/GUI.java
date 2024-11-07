package enigma;

import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GUI {
    private JFrame frame;

    private JTextField text;

    private JTextField conf;

    private JTextField setting;

    private JButton encrypt;

    private JTextArea displayArea;


    /** The refference of the main class
     *
     */
    private Main _main;
    public GUI(Main main) {
        frame = new JFrame();
        JPanel panel = new JPanel();
        _main = main;


        //label of the  text
        JLabel conflabel = new JLabel("Enter the configuration file bellow, use Default if it is empty");
        Dimension labelD = new Dimension(200,50);
        JLabel pblabel = new JLabel("Enter the setting of the machine. Example: * B Beta I II III AAAA (TD) (KC) (JZ)");
        JLabel postEncrypt = new JLabel("Encrypted/Decrypted Message");
        JLabel textFrame = new JLabel("Please enter your message in CAPITALIZED ENGLISH");

        //Set up the configuration
        conf = new JTextField("demo/default.conf");
        setting = new JFormattedTextField("* B Beta I II III ABCD (OD) (FA)");


        // Add selection to encryption permutation
        text = new JTextField("HELLO WORLD");

        // Set up the encryption button
        encrypt = new JButton("Begin Encryption");
        encrypt.setPreferredSize(labelD);




        //Set up the display area for the message
        displayArea = new JTextArea("Encrypted/Decrypted Message will be Shown");
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        String outputString = "";
        ActionListener encryptionProcessClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                String config = conf.getText();
                Scanner configuration;

                if (config.length() == 0) {
                    //setting the default configuration
                    config = "demo/default.conf";
                    try {
                        configuration = new Scanner(new File(config));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException("The file does not exist");
                    }
                } else {
                    try {
                        configuration = new Scanner(new File(config));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException("The file does not exist");
                    }
                }


                //setting up the input to fit in the data structure in main
                Scanner input;
                String inputsetup = setting.getText() + "\n" + text.getText();
                input = new Scanner(inputsetup);
                String outputString = _main.guiProcess(configuration, input);
                displayArea.setText(outputString);

            }

        };
        encrypt.addActionListener(encryptionProcessClick);




        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Enigma");
        frame.pack();
        frame.setSize(400, 300);

        Point center = new Point(780, 400);
        frame.setLocation(center);






        // Set layout and add components
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(conflabel);
        panel.add(conf);
        panel.add(pblabel);
        panel.add(setting);
        panel.add(textFrame);
        panel.add(text);
        panel.add(encrypt);
        panel.add(postEncrypt);
        panel.add(scrollPane);





    }

    // Method to create and show the GUI
    public void createAndShowGUI() {
        frame.setVisible(true);  // This keeps the window visible
    }


    /**
     * Returns the configuration of engima machine
     * @return
     */
    public String getConf() {
        return conf.getText();
    }

    /**
     * returns the plug board ocnfiguration of the machine
     * @return
     */


    /** Returns the message input of th emachine.
     *
     * @return
     */
    public String getMsg() {
        return text.getText();
    }


}
