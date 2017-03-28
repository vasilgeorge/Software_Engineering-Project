package net.java.sip.communicator.gui;

//import gov.nist.sip.proxy.Blocking_Server;
import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;

import net.java.sip.communicator.common.PropertiesDepot;
import net.java.sip.communicator.common.Utils;
import net.java.sip.communicator.sip.security.UserCredentials;

/**
 * Sample registration splash screen
 */
public class BlockingSplash extends JDialog
{
	String userName = null;
	String blockUserName = null;
	JTextField blockUserNameTextField = null;
    
    /**
     * Resource bundle with default locale
     */
    private ResourceBundle resources = null;

    /**
     * Path to the image resources
     */
    private String imagepath = "/net.java.sip.communicator/resource/Block_user_pic.png";
    
	/**
	 * Command strings for a cancel,help,block,unblock and show_blocked actions 
	 * (e.g., a button).
	 * These strings are never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_HELP = "cmd.help" /*NOI18N*/;
	private String CMD_BLOCK = "cmd.block" /*NOI18N*/;
	private String CMD_UNBLOCK = "cmd.unblock" /*NOI18N*/;
	private String CMD_SHOW_BLOCKED = "cmd.show_blocked" /*NOI18N*/;
	
	// Components we need to manipulate after creation
	private JButton blockButton = null;
	private JButton unblockButton = null;
	private JButton showBlockedButton = null;
	private JButton helpButton = null;
	private JButton cancelButton = null;

	/**
     * Creates new form AuthenticationSplash
     */
    public BlockingSplash(Frame parent, boolean modal,UserCredentials cred)
    {
        super(parent, modal);
        userName = cred.getUserName();
        initResources();
        initComponents();
        pack();
        centerWindow();
    }

    public BlockingSplash() {
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
        Rectangle screen = new Rectangle(
            Toolkit.getDefaultToolkit().getScreenSize());
        Point center = new Point(
            (int) screen.getCenterX(), (int) screen.getCenterY());
        Point newLocation = new Point(
            center.x - this.getWidth() / 2, center.y - this.getHeight() / 2);
        if (screen.contains(newLocation.x, newLocation.y, this.getWidth(), this.getHeight())) {
            this.setLocation(newLocation);
        }
    } // centerWindow()
	
    
	private void initComponents()
	{
	    Container contents = getContentPane();
	    contents.setLayout(new BorderLayout());
	
	    String title = Utils.getProperty("net.java.sip.communicator.gui.BLOCKING_WIN_TITLE");
	
	    if(title == null)
	        title = "Blocking";
	    
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
	    getAccessibleContext().setAccessibleDescription("Blocking Splash");
	
	    String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.BLOCKING_PROMPT");
	
	    if(authPromptLabelValue  == null)
	        authPromptLabelValue  = "Please enter the username of the user you wish to block/unblock.";
	
	    JLabel splashLabel = new JLabel(authPromptLabelValue );
	    splashLabel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
	    splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    contents.add(splashLabel, BorderLayout.NORTH);
	
	    JPanel centerPane = new JPanel();
	    centerPane.setLayout(new GridBagLayout());
	    

		//System.out.println(userName);
		 
	    // Manipulate blockTargetUserName
	    blockUserNameTextField = new JTextField(); 
	    JLabel  blockUserNameLabel = new JLabel();
	    blockUserNameLabel.setDisplayedMnemonic('B');
	    // setLabelFor() allows the mnemonic to work
	    blockUserNameLabel.setLabelFor(blockUserNameTextField);
	
	    String blockUserNameLabelValue = Utils.getProperty("net.java.sip.communicator.gui.BLOCK_USER_NAME_LABEL");
	
	    if(blockUserNameLabelValue == null)
	        blockUserNameLabelValue = "Username to be blocked:";
	
	    int gridy = 0;
	
	    blockUserNameLabel.setText(blockUserNameLabelValue);
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx=0;
	    c.gridy=gridy;
	    c.anchor=GridBagConstraints.WEST;
	    c.insets=new Insets(12,12,0,0);
	    centerPane.add(blockUserNameLabel, c);
	
	    // blockUserName text
	    c = new GridBagConstraints();
	    c.gridx=1;
	    c.gridy=gridy++;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    c.weightx=1.0;
	    c.insets=new Insets(12,7,0,11);
	    centerPane.add(blockUserNameTextField, c);
	         
	    // Buttons along bottom of window
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
	    
	    // Block Button
	    blockButton = new JButton();
	    blockButton.setText("Block");
	    blockButton.setActionCommand(CMD_BLOCK);
	    blockButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(blockButton);
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	    
	    // Unblock Button
	    unblockButton = new JButton();
	    unblockButton.setText("Unblock");
	    unblockButton.setActionCommand(CMD_UNBLOCK);
	    unblockButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(unblockButton);
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	    
	    // ShowBlocked Button
	    showBlockedButton = new JButton();
	    showBlockedButton.setText("Show blocked users");
	    showBlockedButton.setActionCommand(CMD_SHOW_BLOCKED);
	    showBlockedButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(showBlockedButton);	
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
	    getRootPane().setDefaultButton(blockButton);
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
            blockButton, unblockButton, showBlockedButton, cancelButton, helpButton 
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
            blockUserName = null;
            setVisible(false);
            dispose();
        }
        else if (cmd.equals(CMD_HELP)) {
        	JOptionPane.showMessageDialog(null, "your help code here", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(CMD_BLOCK)) {
            blockUserName = blockUserNameTextField.getText();
            //start
            String mess="104-"+userName+"-"+blockUserName;   
            BlockingSplash bs=new BlockingSplash();
            String test="";
            try {
				test=bs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //end
			/*Blocking_Server bServer = Blocking_Server.getInstance();*/
			try {
				if( test.equals("400")) {
					JOptionPane.showMessageDialog(
							null, "User successfully blocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(
							null, "BlockUsername does not exist.", "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
        else if (cmd.equals(CMD_SHOW_BLOCKED)){
        	
        	//start
            String mess="106-"+userName+"-"+"";   
            BlockingSplash bs=new BlockingSplash();
            String test="";
            try {
				test=bs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//end
        	JOptionPane.showMessageDialog(
					null, test, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(CMD_UNBLOCK)) {
        	blockUserName = blockUserNameTextField.getText();
            //start
            String mess="105-"+userName+"-"+blockUserName;   
            BlockingSplash bs=new BlockingSplash();
            String test="";
            try {
				test=bs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//end
			if (test.equals("400")){
					JOptionPane.showMessageDialog(
							null, "User successfully unblocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
					JOptionPane.showMessageDialog(
							null, "unblockUsername does not exist.", "Failed", JOptionPane.INFORMATION_MESSAGE);
			}
			
        }
        //setVisible(false);
        //dispose();
    } // dialogDone()*
   
    /**
     * This main() is provided for debugging purposes, to display a
     * sample dialog.
     */
   /* public static void main(String args[])
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
        BlockingSplash dialog = new BlockingSplash(frame, true);
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
            if(  blockUserNameTextField.getText() == null || blockUserNameTextField.getText().trim().length() == 0)
                return super.getFirstComponent(cont);
            else
                return blockUserNameTextField;
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
} // class BlockingSplash