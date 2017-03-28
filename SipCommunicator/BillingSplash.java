package net.java.sip.communicator.gui;

//import gov.nist.sip.proxy.Billing_Server;
import java.io.*;
import java.net.*;

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
import javax.swing.JComboBox;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;

import net.java.sip.communicator.common.PropertiesDepot;
import net.java.sip.communicator.common.Utils;
import net.java.sip.communicator.sip.security.UserCredentials;

/**
 * Sample registration splash screen
 */
public class BillingSplash extends JDialog
{
	String userName = null;
	String membership = null;
    JComboBox<String> membershipCombo = null;

    /**
     * Resource bundle with default locale
     */
    private ResourceBundle resources = null;

    /**
     * Path to the image resources
     */
    private String imagepath = null;

	/**
	 * Command strings for a cancel,help,showTotalBill,changeMembership actions
	 * (e.g., a button).
	 * These strings are never presented to the user and should
	 * not be internationalized.
	 */
	private String CMD_CANCEL = "cmd.cancel" /*NOI18N*/;
	private String CMD_HELP = "cmd.help" /*NOI18N*/;
	private String CMD_SHOWBILL = "cmd.showBill" /*NOI18N*/;
	private String CMD_CONFIRM = "cmd.confirm" /*NOI18N*/;

	// Components we need to manipulate after creation
	private JButton confirmButton = null;
	private JButton showBillButton = null;
	private JButton helpButton = null;
	private JButton cancelButton = null;

	/**
     * Creates new form BillingSplash
     */
    public BillingSplash(Frame parent, boolean modal, UserCredentials cred)
    {
        super(parent, modal);
        userName = cred.getUserName();
        initResources();
        initComponents();
        pack();
        centerWindow();
    }

    public BillingSplash() {
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
	    String title = Utils.getProperty("net.java.sip.communicator.gui.BILLING_WIN_TITLE");

	    if(title == null)
	        title = "Billing";
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
	    getAccessibleContext().setAccessibleDescription("Billing Splash");
	    String authPromptLabelValue = Utils.getProperty("net.java.sip.communicator.gui.BILLING_PROMPT");

	    if(authPromptLabelValue  == null)
	        authPromptLabelValue  = "Now you can view your total bill or change your membership type.";

	    JLabel splashLabel = new JLabel(authPromptLabelValue );
	    splashLabel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
	    splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	    contents.add(splashLabel, BorderLayout.NORTH);

	    JPanel centerPane = new JPanel();
	    centerPane.setLayout(new GridBagLayout());

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

	    int gridy = 0;
	    GridBagConstraints c = new GridBagConstraints();
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

	    // Confirm Button
	    confirmButton = new JButton();
	    confirmButton.setText("Confirm");
	    confirmButton.setActionCommand(CMD_CONFIRM);
	    confirmButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(confirmButton);
	    // space
	    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

	    // ShowBill Button
	    showBillButton = new JButton();
	    showBillButton.setText("ShowBill");
	    showBillButton.setActionCommand(CMD_SHOWBILL);
	    showBillButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent event)
	        {
	            dialogDone(event);
	        }
	    });
	    buttonPanel.add(showBillButton);
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
	    getRootPane().setDefaultButton(confirmButton);
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
            confirmButton, showBillButton, cancelButton, helpButton
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
            setVisible(false);
            dispose();
        }
        else if (cmd.equals(CMD_HELP)) {
        	JOptionPane.showMessageDialog(null, "your help code here", "Help", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (cmd.equals(CMD_CONFIRM)) {
        	String membership = (String) membershipCombo.getSelectedItem();
        	//start
            String mess="102-"+userName+"-"+membership;
            BillingSplash bs=new BillingSplash();
            String test="";
            try {
				test=bs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            //end

			/*Billing_Server bServer = Billing_Server.getInstance();*/
            try {
				if(test.equals("Success")) {
					JOptionPane.showMessageDialog(
							null, "Successful change of membership.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(
							null, "Failed to change membership.", "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if (cmd.equals(CMD_SHOWBILL)) {
        	//start
            String mess="103-"+userName+"-"+"";
            BillingSplash bs=new BillingSplash();
            String test="0";
            try {
				test=bs.run(mess);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            JOptionPane.showMessageDialog(
					null, test, "Success.", JOptionPane.INFORMATION_MESSAGE);
            //end
			/*Billing_Server bServer = Billing_Server.getInstance();
            try {
            	//double i=bServer.getBill(userName);
            	int i=Integer.parseInt(test);
				if(i>=0) {
					JOptionPane.showMessageDialog(
							null, i, "Success.", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(
							null, "Failed to show of bill.", "Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
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
        BillingSplash dialog = new BillingSplash(frame, true,cred);
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
            if(  userName == null || userName.trim().length() == 0)
                return super.getFirstComponent(cont);
            else
            	return getFirstComponent(cont);
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
