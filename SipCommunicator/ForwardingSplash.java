package net.java.sip.communicator.gui;

//import gov.nist.sip.proxy.Forwarding_Server;
import java.io.*;
import java.net.*;
import net.java.sip.communicator.sip.security.UserCredentials;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;

import net.java.sip.communicator.common.PropertiesDepot;
import net.java.sip.communicator.common.Utils;
import net.java.sip.communicator.sip.security.UserCredentials;

/**
 * Sample registration splash screen
 */
public class ForwardingSplash extends JDialog
{
	String userName = null;
	String targetUserName = null;
	JTextField targetUserNameTextField = null;
    
    /**
     * Resource bundle with default locale
     */
    private ResourceBundle resources = null;

    /**
     * Path to the image resources
     */
    private String imagepath = null;
    
	/**
	 * Command strings for a cancel,help,forward,unforward actions 
	 * (e.g., a button).
	 * These strings are never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_HELP = "cmd.help" /*NOI18N*/;
	private String CMD_FORWARD = "cmd.forward" /*NOI18N*/;
	private String CMD_UNFORWARD = "cmd.unforward" /*NOI18N*/;
	
	// Components we need to manipulate after creation
	private JButton forwardToButton = null;
	private JButton unForwardFromButton = null;
	private JButton helpButton = null;
	private JButton cancelButton = null;
	/**
     * Creates new form AuthenticationSplash
     */
    public ForwardingSplash(Frame parent, boolean modal,UserCredentials cred)
    {
        super(parent, modal);
        userName = cred.getUserName();
        initResources();
        initComponents();
        pack();
        centerWindow();
    }

    public ForwardingSplash() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Loads locale-specific resources: strings, images, etc
     */
    private void initResources()
    {
        Locale locale = Locale.getDefault();
        imagepath = ".";
    }

    /**
     * Centers the window on the screen.
     */
    private void centerWindow()
    {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Point center = new Point((int) screen.getCenterX(), (int) screen.getCenterY());
        Point newLocation = new Point(center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
        if (screen.contains(newLocation.x, newLocation.y, this.getWidth(), this.getHeight())) {
            this.setLocation(newLocation);
        }
    } // centerWindow()
	
	private void initComponents()
	{
	    Container contents = getContentPane();
	    contents.setLayout(new BorderLayout());
	
	    String title = Utils.getProperty("net.java.sip.communicator.gui.FORWARDING_WIN_TITLE");
	
	    if(title == null)
	        title = "Forwarding";
	    setTitle(title);
	    setResizable(true);
	    
	    addWindowListener(new WindowAdapter()
	    {
	        public void windowClosing(WindowEvent event)
	        {
	            dialogDone(CMD_CANCEL);
	        }
	    });
	    
	    // Accessibility -- all frames, dialogs, and applets should have a description
	    getAccessibleContext().setAccessibleDescription("Forwarding Splash");
	    String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.FORWARDING_PROMPT");
	
	    if(authPromptLabelValue  == null)
	        authPromptLabelValue  = "Please enter the username you wish to forward/unforward your calls to/from.";
	
	    JLabel splashLabel = new JLabel(authPromptLabelValue );
	    splashLabel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
	    splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    contents.add(splashLabel, BorderLayout.NORTH);
	
	    JPanel centerPane = new JPanel();
	    centerPane.setLayout(new GridBagLayout());
	    
	    
		//System.out.println(userName);
		 
	    // Manipulate targetUserName
	    targetUserNameTextField = new JTextField(); 
	    JLabel  targetUserNameLabel = new JLabel();
	    targetUserNameLabel.setDisplayedMnemonic('T');
	    // setLabelFor() allows the mnemonic to work
	    targetUserNameLabel.setLabelFor(targetUserNameTextField);
	
	    String targetUserNameLabelValue = Utils.getProperty("net.java.sip.communicator.gui.TARGET_USER_NAME_LABEL");
	    if(targetUserNameLabelValue == null)
	        targetUserNameLabelValue = "Username:";
	
	    int gridy = 0;
	
	    targetUserNameLabel.setText(targetUserNameLabelValue);
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx=0;
	    c.gridy=gridy;
	    c.anchor=GridBagConstraints.WEST;
	    c.insets=new Insets(12,12,0,0);
	    centerPane.add(targetUserNameLabel, c);
	
	    // targetUserName text
	    c = new GridBagConstraints();
	    c.gridx=1;
	    c.gridy=gridy++;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    c.weightx=1.0;
	    c.insets=new Insets(12,7,0,11);
	    centerPane.add(targetUserNameTextField, c);
	         
	    // Buttons along bottom of window
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
	    
	    // Forward Button
	    forwardToButton = new JButton();
	    forwardToButton.setText("Forward");
	    forwardToButton.setActionCommand(CMD_FORWARD);
	    forwardToButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(forwardToButton);
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	    
	    // UnForward Button
	    unForwardFromButton = new JButton();
	    unForwardFromButton.setText("UnForward");
	    unForwardFromButton.setActionCommand(CMD_UNFORWARD);
	    unForwardFromButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(unForwardFromButton);
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	    	    
	    // Cancel Button
	    cancelButton = new JButton();
	    cancelButton.setText("Cancel");
	    cancelButton.setActionCommand(CMD_CANCEL);
	    cancelButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(cancelButton);
	
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));   
	    
	    // Help Button
	    helpButton = new JButton();
	    helpButton.setMnemonic('H');
	    helpButton.setText("Help");
	    helpButton.setActionCommand(CMD_HELP);
	    helpButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(helpButton);
	    
	    c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = 5;
	    c.gridwidth = 2;
	    c.insets = new Insets(11, 12, 11, 11);
	
	    centerPane.add(buttonPanel, c);
	
	    contents.add(centerPane, BorderLayout.CENTER);
	    getRootPane().setDefaultButton(forwardToButton);
	    equalizeButtonSizes();
	
	    setFocusTraversalPolicy(new FocusTraversalPol());
	} // initComponents()
	
	/**
     * Sets the buttons along the bottom of the dialog to be the
     * same size. This is done dynamically by setting each button's
     * preferred and maximum sizes after the buttons are created.
     * This way, the layout automatically adjusts to the locale-
     * specific strings.
     */
    private void equalizeButtonSizes()
    {
        JButton[] buttons = new JButton[] {
            forwardToButton, unForwardFromButton, cancelButton, helpButton 
        };

        String[] labels = new String[buttons.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = buttons[i].getText();
        }

        // Get the largest width and height
        int i = 0;
        Dimension maxSize = new Dimension(0, 0);
        Rectangle2D textBounds = null;
        Dimension textSize = null;
        FontMetrics metrics = buttons[0].getFontMetrics(buttons[0].getFont());
        Graphics g = getGraphics();
        for (i = 0; i < labels.length; ++i) {
            textBounds = metrics.getStringBounds(labels[i], g);
            maxSize.width =
                Math.max(maxSize.width, (int) textBounds.getWidth());
            maxSize.height =
                Math.max(maxSize.height, (int) textBounds.getHeight());
        }

        Insets insets =
            buttons[0].getBorder().getBorderInsets(buttons[0]);
        maxSize.width += insets.left + insets.right;
        maxSize.height += insets.top + insets.bottom;

        // reset preferred and maximum size since BoxLayout takes both
        // into account
        for (i = 0; i < buttons.length; ++i) {
            buttons[i].setPreferredSize( (Dimension) maxSize.clone());
            buttons[i].setMaximumSize( (Dimension) maxSize.clone());
        }
    } // equalizeButtonSizes()
	
    /**
     * The user has selected an option. Here we close and dispose the dialog.
     * If actionCommand is an ActionEvent, getCommandString() is called,
     * otherwise toString() is used to get the action command.
     *
     * @param actionCommand may be null
     */
    private void dialogDone(Object actionCommand)
    {
        String cmd = null;
        if (actionCommand != null) {
            if (actionCommand instanceof ActionEvent) {
                cmd = ( (ActionEvent) actionCommand).getActionCommand();
            }
            else {
                cmd = actionCommand.toString();
            }
        }
        if (cmd == null) {
            // do nothing
        }
        else if (cmd.equals(CMD_CANCEL)) {
            userName = null;
            targetUserName = null;
            setVisible(false);
            dispose();
        }
        else if (cmd.equals(CMD_HELP)) {
        	JOptionPane.showMessageDialog(null, "your help code here", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(CMD_FORWARD)) {
            targetUserName = targetUserNameTextField.getText();
           //start
            String mess="107-"+userName+"-"+targetUserName;   
            ForwardingSplash fs=new ForwardingSplash();
            String test="";
            try {
				test=fs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //end
            try {
				if(test.equals("Success")) {
					JOptionPane.showMessageDialog(
							null, "Forwarding done.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(
							null, "Username does not exist.", "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
        else if (cmd.equals(CMD_UNFORWARD)) {
            targetUserName = targetUserNameTextField.getText();
            //start
            String mess="108-"+userName+"-"+"";   
            ForwardingSplash fs=new ForwardingSplash();
            String test="";
            try {
				test=fs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //end
            try {
				if(test.equals("Success")) {
					JOptionPane.showMessageDialog(
							null, "Unforward done.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(
							null, "Username does not exist." , "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
        //setVisible(false);
        //dispose();
    } // dialogDone()*
   
    /**
     * This main() is provided for debugging purposes, to display a
     * sample dialog.
     */
    /*public static void main(String args[])
    {
        JFrame frame = new JFrame()
        {
            public Dimension getPreferredSize()
            {
                return new Dimension(200, 100);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);
        ForwardingSplash dialog = new ForwardingSplash(frame, true);
        dialog.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent event)
            {
                System.exit(0);
            }

            public void windowClosed(WindowEvent event)
            {
                System.exit(0);
            }
        });
        dialog.pack();
        dialog.setVisible(true);
    } // main()*/

    private class FocusTraversalPol extends LayoutFocusTraversalPolicy
    {
        public Component getDefaultComponent(Container cont)
        {
            if(  targetUserNameTextField.getText() == null || targetUserNameTextField.getText().trim().length() == 0)
                return super.getFirstComponent(cont);
            else
                return targetUserNameTextField;
        }
    }
    
    public String run(String mess) throws Exception{
		Socket sock=new Socket("192.168.1.71",444);
		PrintStream ps=new PrintStream(sock.getOutputStream());
		ps.println(mess);
		
		InputStreamReader ir=new InputStreamReader(sock.getInputStream());
		BufferedReader br=new BufferedReader(ir);
		String message=br.readLine();
		System.out.println(message);
		return message;
	}
} // class ForwardingSplash