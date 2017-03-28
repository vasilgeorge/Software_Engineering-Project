package net.java.sip.communicator.gui;

//import gov.nist.sip.proxy.Registration_Server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;

import net.java.sip.communicator.common.PropertiesDepot;
import net.java.sip.communicator.common.Utils;

/**
 * Sample registration splash screen
 */
public class RegistrationSplash extends JDialog
{
	String userName = null;
	char[] password = null;
	String email = null;
	String membership = null;
    JTextField userNameTextField = null;
    JPasswordField passwordTextField = null;
    JTextField emailTextField = null;
    JComboBox<String> membershipCombo = null;

	/**
	 * Resource bundle with default locale
	 */
	private ResourceBundle resources = null;
	
	/**
	 * Path to the image resources
	 */
	private String imagePath = null;
	
	/**
	 * Command string for a cancel action (e.g., a button).
	 * This string is never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	
	/**
	 * Command string for a help action (e.g., a button).
	 * This string is never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_HELP = "cmd.help" /*NOI18N*/;
	
	/**
	 * Command string for a login action (e.g., a button).
	 * This string is never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_REGISTER = "cmd.register" /*NOI18N*/;
	
	// Components we need to manipulate after creation
	private JButton registerButton = null;
	private JButton cancelButton = null;
	private JButton helpButton = null;

	/**
     * Creates new form AuthenticationSplash
     */
    public RegistrationSplash(Frame parent, boolean modal)
    {
        super(parent, modal);
        initResources();
        initComponents();
        pack();
        centerWindow();
    }

    public RegistrationSplash() {
		// TODO Auto-generated constructor stub
	}

	/**
     * Loads locale-specific resources: strings, images, etc
     */
    private void initResources()
    {
        Locale locale = Locale.getDefault();
        imagePath = ".";
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
        if (screen.contains(newLocation.x, newLocation.y,
                            this.getWidth(), this.getHeight())) {
            this.setLocation(newLocation);
        }
    } // centerWindow()
	

	private void initComponents()
	{
	    Container contents = getContentPane();
	    contents.setLayout(new BorderLayout());
	
	    String title = Utils.getProperty("net.java.sip.communicator.gui.AUTH_WIN_TITLE");
	
	    if(title == null)
	        title = "Register Form";
	
	    setTitle(title);
	    setResizable(false);
	    addWindowListener(new WindowAdapter()
	    {
	        public void windowClosing(WindowEvent event)
	        {
	            dialogDone(CMD_CANCEL);
	        }
	    });
	    
	    // Accessibility -- all frames, dialogs, and applets should have a description
	    getAccessibleContext().setAccessibleDescription("Registration Splash");
	
	    String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.REGISTRATION_PROMPT");
	
	    if(authPromptLabelValue  == null)
	        authPromptLabelValue  = "Let us know more about you! Enter username,password,e-mail and membership.";
	
	    JLabel splashLabel = new JLabel(authPromptLabelValue );
	    splashLabel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
	    splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    contents.add(splashLabel, BorderLayout.NORTH);
	
	    JPanel centerPane = new JPanel();
	    centerPane.setLayout(new GridBagLayout());
	
	    // Manipulate userName
	    userNameTextField = new JTextField(); // needed below
	    
	    JLabel userNameLabel = new JLabel();
	    userNameLabel.setDisplayedMnemonic('U');
	    // setLabelFor() allows the mnemonic to work
	    userNameLabel.setLabelFor(userNameTextField);
	
	    String userNameLabelValue = Utils.getProperty("net.java.sip.communicator.gui.USER_NAME_LABEL");
	
	    if(userNameLabelValue == null)
	        userNameLabelValue = "Username";
	
	    int gridy = 0;
	
	    userNameLabel.setText(userNameLabelValue);
	    GridBagConstraints c = new GridBagConstraints();
	    c.gridx=0;
	    c.gridy=gridy;
	    c.anchor=GridBagConstraints.WEST;
	    c.insets=new Insets(12,12,0,0);
	    centerPane.add(userNameLabel, c);
	
	    // userName text
	    c = new GridBagConstraints();
	    c.gridx=1;
	    c.gridy=gridy++;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    c.weightx=1.0;
	    c.insets=new Insets(12,7,0,11);
	    centerPane.add(userNameTextField, c);
	
	    // Manipulate password
	    passwordTextField = new JPasswordField(); //needed below

	    JLabel passwordLabel = new JLabel();
	    passwordLabel.setDisplayedMnemonic('P');
	    passwordLabel.setLabelFor(passwordTextField);
	    
	    String pLabelValue = PropertiesDepot.getProperty("net.java.sip.communicator.gui.PASSWORD_LABEL");
	    if(pLabelValue == null)
	        pLabelValue = "Password";
	    passwordLabel.setText(pLabelValue);
	    
	    c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = gridy;
	    c.anchor = GridBagConstraints.WEST;
	    c.insets = new Insets(11, 12, 0, 0);
	
	    centerPane.add(passwordLabel, c);
	
	    // password text
	    passwordTextField.setEchoChar('\u2022'); /* echoes password characters */
	    c = new GridBagConstraints();
	    c.gridx = 1;
	    c.gridy = gridy++;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1.0;
	    c.insets = new Insets(12, 7, 0, 11);
	    centerPane.add(passwordTextField, c);
	    
	    // Manipulate email
	    emailTextField = new JTextField(); //needed below
	    
	    JLabel emailLabel = new JLabel();
	    emailLabel.setDisplayedMnemonic('E');
	    emailLabel.setLabelFor(emailTextField);
	    String eLabelValue = PropertiesDepot.getProperty("net.java.sip.communicator.gui.EMAIL_LABEL");
	    if(eLabelValue == null)
	        eLabelValue = "e-mail";
	    emailLabel.setText(eLabelValue);
	    
	    c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = gridy;
	    c.anchor = GridBagConstraints.WEST;
	    c.insets = new Insets(11, 12, 0, 0);
	    centerPane.add(emailLabel, c);
	    
	    // email text
	    c = new GridBagConstraints();
	    c.gridx = 1;
	    c.gridy = gridy++;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1.0;
	    c.insets = new Insets(12, 7, 0, 11);
	    centerPane.add(emailTextField, c);
	    
	    // Manipulate membership type
	    membershipCombo = new JComboBox<String>();
	    membershipCombo.addItem("Premium");
	    membershipCombo.addItem("Standard");
	    
	    // membership label
	    JLabel membershipLabel = new JLabel();
	    membershipLabel.setDisplayedMnemonic('M');
	    membershipLabel.setLabelFor(membershipLabel);
	    String mLabelValue = PropertiesDepot.getProperty("net.java.sip.communicator.gui.MEMBERSHIP_LABEL");
	    if(mLabelValue == null)
	        mLabelValue = "Membership Type";
	    membershipLabel.setText(mLabelValue);
	    
	    c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = gridy + 1;
	    c.anchor = GridBagConstraints.WEST;
	    c.insets = new Insets(12, 12, 0, 0);
	
	    centerPane.add(membershipCombo, c);
	
	    // membership Combobox
	    c = new GridBagConstraints();
	    c.gridx = 0;
	    c.gridy = gridy++;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1.0;
	    c.insets = new Insets(12, 7, 0, 11);
	    centerPane.add(membershipLabel, c);

	    // Buttons along bottom of window
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
	    
	    // Register Button
	    registerButton = new JButton();
	    registerButton.setText("Register");
	    registerButton.setActionCommand(CMD_REGISTER);
	    registerButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(registerButton);
	
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
	    getRootPane().setDefaultButton(registerButton);
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
            registerButton, cancelButton, helpButton 
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
            password = null;
            email = null;
            membership = null;
            setVisible(false);
            dispose();
        }
        else if (cmd.equals(CMD_HELP)) {
        	JOptionPane.showMessageDialog(null, "your help code here", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(CMD_REGISTER)) {
            userName = userNameTextField.getText();
            password = passwordTextField.getPassword();
            String pswd= new String (password);
            email = emailTextField.getText();
            membership =membershipCombo.getSelectedItem().toString();
            //start
            String mess="101-"+userName+"-"+pswd+"-"+email+"-"+membership;   
            RegistrationSplash rs=new RegistrationSplash();
            try {
            	rs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //end
			/*Registration_Server Server = Registration_Server.getInstance();
			int i;
            try {
				 i= Server.register_user(userName, pswd, email, membership);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
           /* if (i==200)		{
				JOptionPane.showMessageDialog(
						null, "You were successfully signed up.", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(
						null, "Username already exists.", "Failed", JOptionPane.INFORMATION_MESSAGE);
			}*/
        }
        setVisible(false);
        dispose();
    } // dialogDone()*
   
    /**
     * This main() is provided for debugging purposes, to display a
     * sample dialog.
     */
    public static void main(String args[])
    {
        JFrame frame = new JFrame()
        {
            public Dimension getPreferredSize()
            {
                return new Dimension(200, 100);
            }
        };
        frame.setTitle("Debugging frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);

        RegistrationSplash dialog = new RegistrationSplash(frame, true);
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
    } // main()

    private class FocusTraversalPol extends LayoutFocusTraversalPolicy
    {
        public Component getDefaultComponent(Container cont)
        {
            if(  userNameTextField.getText() == null
               ||userNameTextField.getText().trim().length() == 0)
                return super.getFirstComponent(cont);
            else
                return passwordTextField;
        }
    }
    public void run(String mess) throws Exception{
		Socket sock=new Socket("192.168.1.71",444);
		PrintStream ps=new PrintStream(sock.getOutputStream());
		ps.println(mess);
		
		InputStreamReader ir=new InputStreamReader(sock.getInputStream());
		BufferedReader br=new BufferedReader(ir);
		String message=br.readLine();
		System.out.println(message);
	}
} // class LoginSplash