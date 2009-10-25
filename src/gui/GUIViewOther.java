/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GUIView.java
 *
 * Created on Oct 25, 2009, 4:13:30 PM
 */

package gui;

import gcom.interfaces.*;
import gcom.Debug;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author emil
 */
public class GUIViewOther extends javax.swing.JFrame {
	private DateFormat timeStampFormatter = new SimpleDateFormat("[HH:mm:ss] ");
	private GCom gcom = new gcom.GCom();
	//private boolean connectedToRegistry = false;
    /** Creates new form GUIView */
    public GUIViewOther() {
        initComponents();
		setJMenuBar(menuBar);
    }

	public GUIViewOther(int port) {
		this();
		rmiPortField.setText(String.valueOf(port));
		connectRMIRegistry();
	}

	public GUIViewOther(String address, int port) {
		this();
		rmiAddressField.setText(address);
		rmiPortField.setText(String.valueOf(port));
		connectRMIRegistry();
	}

	public DateFormat getTimeStampFormatter() {
		return timeStampFormatter;
	}

	public void appendLog(String s) {
		jTextArea1.setText(
				jTextArea1.getText() + timeStampFormatter.format(new Date()) +
				s + "\n");
	}

	private boolean isConnected() {
		return statusConnectedLabel.isEnabled();
	}

	public void showErrorDialog(java.awt.Component parent, String msg) {
		JOptionPane.showMessageDialog(parent, msg,
				"Error!",JOptionPane.ERROR_MESSAGE);
	}

	@Action
	public void connectRMIRegistry() {
		int port = Integer.valueOf(rmiPortField.getText());
		String host = rmiAddressField.getText();
		try {
			appendLog(String.format("Connecting to registry %s:%d",
					host, port));
			gcom.connectToRegistry(host, port);
			hideRMIConnectDialog();
			statusConnectedLabel.setEnabled(true);
			statusConnectedLabel.setToolTipText(String.format("Connected to %s:%d", host,port));
		}
		catch(NumberFormatException e) {
			showErrorDialog(rmiPortField,String.format("Error: %s doesn't appear to be a valid number",rmiPortField.getText()));
			rmiPortField.requestFocusInWindow();
		}
		catch(ConnectException ex) {
			showErrorDialog(connectRMIDialog, String.format("Connection refused to %s:%d",host,port));
			Debug.log(this,Debug.ERROR, null, ex);
		}
		catch (IOException ex) {
			showErrorDialog(connectRMIDialog,
					String.format("Error while connecting to %s:%d",host,port));
			Debug.log(this,Debug.ERROR, null, ex);
		}
	}

	@Action
	public void showRMIConnectDialog() {
		connectRMIDialog.setSize(316, 137);
		//connectRMIDialog.pack();
		rmiAddressField.requestFocusInWindow();
		connectRMIDialog.setVisible(true);
	}
	@Action
	public void hideRMIConnectDialog() {
		connectRMIDialog.setVisible(false);
		this.requestFocus();
	}

	@Action
	public void showJoinDialog() {
		try {
			groupList.setListData(gcom.listReferences());
			joinGroupDialog.setSize(318,244);
			joinGroupDialog.setVisible(true);
		}
		catch(RemoteException e) {
			showErrorDialog(joinGroupDialog, "Couldn't list references in registry.");
			Debug.log(this,Debug.ERROR, null, e);
		}
	}
	@Action
	public void hideJoinDialog() {
		joinGroupDialog.setVisible(false);
		this.requestFocus();
	}

	@Action
	public void joinGroup() {
		String nickname = joinNickField.getText();
		String groupName = (String)groupList.getSelectedValue();
		if( nickname.equals("") ) {
			showErrorDialog(joinGroupDialog,
					"Error: You must supply a nickname.");
			joinNickField.requestFocusInWindow();
		}
		else if(groupName == null) {
			showErrorDialog(joinGroupDialog, "No group was chosen.");
			groupList.requestFocusInWindow();
		}
		else{
			appendLog(String.format("Joining group: %s", groupName));
			try {
				gcom.joinGroup(groupName, nickname);
				addGroup(groupName);
				hideJoinDialog();
			} catch (NotBoundException ex) {
				showErrorDialog(joinGroupDialog, "No such group was found: " + groupName);
			} catch (IOException ex) {
				Debug.log(this,Debug.WARN, null, ex);
			} catch (IllegalStateException ex) {
				Debug.log(this,Debug.WARN, null, ex);
			}
		}
	}

	@Action
	public void toggleToolbar() {
		// TODO: Set default property for this (save state between starts)
		if(toolBar.isVisible()) {
			toolBar.setVisible(false);
		}
		else {
			toolBar.setVisible(true);
		}
	}

	@Action
	public void showCreateGroupDialog() {
		//createGroupDialog.setSize(323,143);
		createGroupDialog.pack();
		createGroupDialog.setVisible(true);
	}
	@Action
	public void hideCreateGroupDialog() {
		createGroupDialog.setVisible(false);
		this.requestFocus();
	}

	private GCom.TYPE_MESSAGEORDERING getOrderingType() {
		return GCom.TYPE_MESSAGEORDERING.valueOf(
				orderType.getSelectedItem().toString()
			);
	}

	private GCom.TYPE_COMMUNICATION getCommunicationType() {
		String cast = castType.getSelectedItem().toString();
		if(cast.equals("Basic")) {
			return GCom.TYPE_COMMUNICATION.BASIC_UNRELIABLE_MULTICAST;
		}
		if(cast.equals("Reliable")) {
			return GCom.TYPE_COMMUNICATION.BASIC_RELIABLE_MULTICAST;
		}
		return null;
	}

	private GCom.TYPE_GROUP getGroupType() {
		String type = groupType.getSelectedItem().toString();
		if(type.equals("Dynamic")) {
			return GCom.TYPE_GROUP.DYNAMIC;
		}
		if(type.equals("Static")) {
			return GCom.TYPE_GROUP.STATIC;
		}
		return null;
	}

	@Action
	public void createGroup() {
		String nickName = createNickField.getText();
		String groupName = groupNameField.getText();
		if( nickName.equals("") ) {
			showErrorDialog(createGroupDialog,
					"Error: You must supply a nickname.");
			createNickField.requestFocusInWindow();
		}
		else if(groupName.equals("")) {
			showErrorDialog(createGroupDialog, "No group name was given.");
			groupNameField.requestFocusInWindow();
		}
		else{
			appendLog(String.format("Creating group: %s", groupName));
			try {
				GroupDefinition def = new gcom.GroupDefinition(groupName,getCommunicationType(),getGroupType(), getOrderingType());
				gcom.createGroup(def, nickName);
				Debug.log(this,Debug.DEBUG,"Created " + def);
				hideCreateGroupDialog();
				addGroup(groupName);
			}
			catch (AlreadyBoundException ex) {
				Debug.log(this, Debug.WARN, "Trying to create already existing group.");
				showErrorDialog(createGroupDialog, "That group already exists");
			}
			catch (RemoteException ex) {
				// TODO: Make a proper error message
				Debug.log(this,Debug.ERROR, null, ex);
				showErrorDialog(createGroupDialog,"Got a remote exception");
			}
			catch (IOException ex) {
				Debug.log(this,Debug.ERROR, null, ex);
				showErrorDialog(createGroupDialog, "Error!");
			}
		}
	}

	private void addGroup(String name) {
		GroupPanel panel = new GroupPanel(this,name);
		gcom.addViewChangeListener(name, panel);
		gcom.addMessageListener(name, panel);
		panel.addMessageSender((MessageSender)gcom);
		tabbedPane.addTab(name,panel);
		tabbedPane.setSelectedComponent(panel);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        toolBar = new javax.swing.JToolBar();
        showRMIDialogButton = new javax.swing.JButton();
        showCreateGroupDialogToolbarButton = new javax.swing.JButton();
        showJoinDialogToolbarButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        connectRMIMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        groupMenu = new javax.swing.JMenu();
        joinGroupMenuItem = new javax.swing.JMenuItem();
        createGroupMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        leaveGroupMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        viewToolBarMenuItem = new javax.swing.JCheckBoxMenuItem();
        connectRMIDialog = new javax.swing.JDialog();
        connectToRMIButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        rmiAddressField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        rmiPortField = new javax.swing.JTextField();
        cancelConnectRMIDialogButton = new javax.swing.JButton();
        joinGroupDialog = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        joinNickField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        groupList = new javax.swing.JList();
        joinGroupButton = new javax.swing.JButton();
        cancelJoinGroupDialogButton = new javax.swing.JButton();
        createGroupDialog = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        groupNameField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        createNickField = new javax.swing.JTextField();
        createGroupButton = new javax.swing.JButton();
        cancelCreateGroupDialogButton = new javax.swing.JButton();
        groupType = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        castType = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        orderType = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        tabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusConnectedLabel = new javax.swing.JLabel();

        toolBar.setBorderPainted(false);
        toolBar.setName("toolBar"); // NOI18N

        showRMIDialogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/icons/network-wireless.png"))); // NOI18N
        showRMIDialogButton.setText("Connect");
        showRMIDialogButton.setToolTipText("Connect to a Registry");
        showRMIDialogButton.setBorderPainted(false);
        showRMIDialogButton.setFocusable(false);
        showRMIDialogButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showRMIDialogButton.setName("showRMIDialogButton"); // NOI18N
        showRMIDialogButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(showRMIDialogButton);

        showCreateGroupDialogToolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/icons/network-server.png"))); // NOI18N
        showCreateGroupDialogToolbarButton.setText("Create");
        showCreateGroupDialogToolbarButton.setToolTipText("Create a new group");
        showCreateGroupDialogToolbarButton.setBorderPainted(false);
        showCreateGroupDialogToolbarButton.setFocusable(false);
        showCreateGroupDialogToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showCreateGroupDialogToolbarButton.setName("showCreateGroupDialogToolbarButton"); // NOI18N
        showCreateGroupDialogToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(showCreateGroupDialogToolbarButton);

        showJoinDialogToolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/icons/network-workgroup.png"))); // NOI18N
        showJoinDialogToolbarButton.setText("Join");
        showJoinDialogToolbarButton.setToolTipText("Join a group");
        showJoinDialogToolbarButton.setBorderPainted(false);
        showJoinDialogToolbarButton.setFocusable(false);
        showJoinDialogToolbarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        showJoinDialogToolbarButton.setName("showJoinDialogToolbarButton"); // NOI18N
        showJoinDialogToolbarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(showJoinDialogToolbarButton);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText("File");
        fileMenu.setName("fileMenu"); // NOI18N

        connectRMIMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        connectRMIMenuItem.setText("Connect to registry");
        connectRMIMenuItem.setName("connectRMIMenuItem"); // NOI18N
        connectRMIMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectRMIMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(connectRMIMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Quit");
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        groupMenu.setText("Group");
        groupMenu.setName("groupMenu"); // NOI18N

        joinGroupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        joinGroupMenuItem.setText("Join group");
        joinGroupMenuItem.setName("joinGroupMenuItem"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, statusConnectedLabel, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), joinGroupMenuItem, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        joinGroupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinGroupMenuItemActionPerformed(evt);
            }
        });
        groupMenu.add(joinGroupMenuItem);

        createGroupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        createGroupMenuItem.setText("Create group");
        createGroupMenuItem.setName("createGroupMenuItem"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, statusConnectedLabel, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), createGroupMenuItem, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        createGroupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGroupMenuItemActionPerformed(evt);
            }
        });
        groupMenu.add(createGroupMenuItem);

        jSeparator5.setName("jSeparator5"); // NOI18N
        groupMenu.add(jSeparator5);

        leaveGroupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        leaveGroupMenuItem.setText("Leave Group");
        leaveGroupMenuItem.setName("leaveGroupMenuItem"); // NOI18N
        leaveGroupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveGroupMenuItemActionPerformed(evt);
            }
        });
        groupMenu.add(leaveGroupMenuItem);

        menuBar.add(groupMenu);

        viewMenu.setText("View");
        viewMenu.setName("viewMenu"); // NOI18N

        viewToolBarMenuItem.setText("View Toolbar");
        viewToolBarMenuItem.setName("viewToolBarMenuItem"); // NOI18N
        viewMenu.add(viewToolBarMenuItem);

        menuBar.add(viewMenu);

        connectRMIDialog.setTitle("Connect to Registry");
        connectRMIDialog.setAlwaysOnTop(true);
        connectRMIDialog.setName("connectRMIDialog"); // NOI18N
        connectRMIDialog.setResizable(false);
        connectRMIDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                connectRMIDialogKeyPressed(evt);
            }
        });

        connectToRMIButton.setText("Connect");
        connectToRMIButton.setName("connectToRMIButton"); // NOI18N
        connectToRMIButton.setSelected(true);
        connectToRMIButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToRMIButtonActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Registry address:");
        jLabel1.setName("jLabel1"); // NOI18N

        rmiAddressField.setToolTipText("The address for a public accessable RMI-registry"); // NOI18N
        rmiAddressField.setName("rmiAddressField"); // NOI18N
        rmiAddressField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rmiAddressFieldKeyPressed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Registry port:");
        jLabel2.setName("jLabel2"); // NOI18N

        rmiPortField.setToolTipText("The port which the registry is listening on. The standard is 1099."); // NOI18N
        rmiPortField.setName("rmiPortField"); // NOI18N
        rmiPortField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rmiPortFieldKeyPressed(evt);
            }
        });

        cancelConnectRMIDialogButton.setText("Cancel");
        cancelConnectRMIDialogButton.setName("cancelConnectRMIDialogButton"); // NOI18N
        cancelConnectRMIDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelConnectRMIDialogButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout connectRMIDialogLayout = new javax.swing.GroupLayout(connectRMIDialog.getContentPane());
        connectRMIDialog.getContentPane().setLayout(connectRMIDialogLayout);
        connectRMIDialogLayout.setHorizontalGroup(
            connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectRMIDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(connectRMIDialogLayout.createSequentialGroup()
                        .addComponent(cancelConnectRMIDialogButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectToRMIButton))
                    .addGroup(connectRMIDialogLayout.createSequentialGroup()
                        .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rmiPortField)
                            .addComponent(rmiAddressField, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))))
                .addContainerGap())
        );
        connectRMIDialogLayout.setVerticalGroup(
            connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectRMIDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rmiAddressField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(rmiPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(connectRMIDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectToRMIButton)
                    .addComponent(cancelConnectRMIDialogButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        joinGroupDialog.setTitle("Join Group");
        joinGroupDialog.setAlwaysOnTop(true);
        joinGroupDialog.setName("joinGroupDialog"); // NOI18N
        joinGroupDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                joinGroupDialogKeyPressed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nickname:");
        jLabel3.setName("jLabel3"); // NOI18N

        joinNickField.setName("joinNickField"); // NOI18N
        joinNickField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                joinNickFieldKeyPressed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Groups:");
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        groupList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        groupList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        groupList.setName("groupList"); // NOI18N
        groupList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                groupListKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(groupList);

        joinGroupButton.setText("Join");
        joinGroupButton.setName("joinGroupButton"); // NOI18N
        joinGroupButton.setSelected(true);
        joinGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinGroupButtonActionPerformed(evt);
            }
        });

        cancelJoinGroupDialogButton.setText("Cancel");
        cancelJoinGroupDialogButton.setName("cancelJoinGroupDialogButton"); // NOI18N
        cancelJoinGroupDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelJoinGroupDialogButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout joinGroupDialogLayout = new javax.swing.GroupLayout(joinGroupDialog.getContentPane());
        joinGroupDialog.getContentPane().setLayout(joinGroupDialogLayout);
        joinGroupDialogLayout.setHorizontalGroup(
            joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinGroupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, joinGroupDialogLayout.createSequentialGroup()
                        .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                            .addComponent(joinNickField, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, joinGroupDialogLayout.createSequentialGroup()
                        .addComponent(cancelJoinGroupDialogButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(joinGroupButton)))
                .addContainerGap())
        );
        joinGroupDialogLayout.setVerticalGroup(
            joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinGroupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(joinNickField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(joinGroupDialogLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(joinGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(joinGroupButton)
                            .addComponent(cancelJoinGroupDialogButton)))
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        createGroupDialog.setName("createGroupDialog"); // NOI18N
        createGroupDialog.setResizable(false);
        createGroupDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                createGroupDialogKeyPressed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Group name:");
        jLabel5.setName("jLabel5"); // NOI18N

        groupNameField.setName("groupNameField"); // NOI18N
        groupNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                groupNameFieldKeyPressed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nickname:");
        jLabel6.setName("jLabel6"); // NOI18N

        createNickField.setName("createNickField"); // NOI18N
        createNickField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                createNickFieldKeyPressed(evt);
            }
        });

        createGroupButton.setText("Create");
        createGroupButton.setName("createGroupButton"); // NOI18N
        createGroupButton.setSelected(true);
        createGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGroupButtonActionPerformed(evt);
            }
        });

        cancelCreateGroupDialogButton.setText("Cancel");
        cancelCreateGroupDialogButton.setName("cancelCreateGroupDialogButton"); // NOI18N
        cancelCreateGroupDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelCreateGroupDialogButtonActionPerformed(evt);
            }
        });

        groupType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dynamic", "Static" }));
        groupType.setName("groupType"); // NOI18N
        groupType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                groupTypeKeyPressed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Group type:");
        jLabel7.setName("jLabel7"); // NOI18N

        castType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Basic", "Reliable" }));
        castType.setName("castType"); // NOI18N
        castType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                castTypeKeyPressed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Multicast:");
        jLabel8.setName("jLabel8"); // NOI18N

        orderType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NONORDERED", "FIFO", "CAUSAL", "TOTAL", "CAUSALTOTAL" }));
        orderType.setName("orderType"); // NOI18N
        orderType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                orderTypeKeyPressed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ordering:");
        jLabel9.setName("jLabel9"); // NOI18N

        jSeparator2.setName("jSeparator2"); // NOI18N

        javax.swing.GroupLayout createGroupDialogLayout = new javax.swing.GroupLayout(createGroupDialog.getContentPane());
        createGroupDialog.getContentPane().setLayout(createGroupDialogLayout);
        createGroupDialogLayout.setHorizontalGroup(
            createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createGroupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addGroup(createGroupDialogLayout.createSequentialGroup()
                        .addComponent(cancelCreateGroupDialogButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createGroupButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, createGroupDialogLayout.createSequentialGroup()
                        .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(createNickField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(castType, 0, 193, Short.MAX_VALUE)
                            .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(groupType, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(groupNameField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                            .addComponent(orderType, 0, 193, Short.MAX_VALUE))))
                .addContainerGap())
        );
        createGroupDialogLayout.setVerticalGroup(
            createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createGroupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(groupNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(createNickField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(castType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(createGroupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createGroupButton)
                    .addComponent(cancelCreateGroupDialogButton))
                .addContainerGap())
        );

        jSeparator3.setName("jSeparator3"); // NOI18N

        jSeparator4.setName("jSeparator4"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabbedPane.setName("tabbedPane"); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        tabbedPane.addTab("Status", jScrollPane1);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(548, 20));

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusConnectedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusConnectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/icons/emblem-default.png"))); // NOI18N
        statusConnectedLabel.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/resources/icons/emblem-important.png"))); // NOI18N
        statusConnectedLabel.setEnabled(false);
        statusConnectedLabel.setName("statusConnectedLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(558, Short.MAX_VALUE)
                .addComponent(statusConnectedLabel)
                .addGap(14, 14, 14))
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(284, Short.MAX_VALUE)))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusConnectedLabel)
                .addContainerGap())
            .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(statusPanelLayout.createSequentialGroup()
                    .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(433, Short.MAX_VALUE)
                    .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addGap(22, 22, 22)))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void connectRMIDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_connectRMIDialogKeyPressed
		if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
			connectRMIRegistry();
		}
		else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			hideRMIConnectDialog();
		}
}//GEN-LAST:event_connectRMIDialogKeyPressed

	private void joinGroupDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_joinGroupDialogKeyPressed
		if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
			joinGroup();
		}
		else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			hideJoinDialog();
		}
	}//GEN-LAST:event_joinGroupDialogKeyPressed

	private void createGroupDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_createGroupDialogKeyPressed
		if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
			createGroup();
		}
		else if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			hideCreateGroupDialog();
		}
	}//GEN-LAST:event_createGroupDialogKeyPressed

	private void connectRMIMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectRMIMenuItemActionPerformed
		showRMIConnectDialog();
	}//GEN-LAST:event_connectRMIMenuItemActionPerformed

	private void joinGroupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinGroupMenuItemActionPerformed
		showJoinDialog();
	}//GEN-LAST:event_joinGroupMenuItemActionPerformed

	private void createGroupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGroupMenuItemActionPerformed
		showCreateGroupDialog();
	}//GEN-LAST:event_createGroupMenuItemActionPerformed

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	}//GEN-LAST:event_exitMenuItemActionPerformed

	private void rmiAddressFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rmiAddressFieldKeyPressed
		connectRMIDialogKeyPressed(evt);
	}//GEN-LAST:event_rmiAddressFieldKeyPressed

	private void connectToRMIButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToRMIButtonActionPerformed
		connectRMIRegistry();
	}//GEN-LAST:event_connectToRMIButtonActionPerformed

	private void cancelConnectRMIDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelConnectRMIDialogButtonActionPerformed
		hideRMIConnectDialog();
	}//GEN-LAST:event_cancelConnectRMIDialogButtonActionPerformed

	private void rmiPortFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rmiPortFieldKeyPressed
		connectRMIDialogKeyPressed(evt);
	}//GEN-LAST:event_rmiPortFieldKeyPressed

	private void cancelJoinGroupDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelJoinGroupDialogButtonActionPerformed
		hideJoinDialog();
	}//GEN-LAST:event_cancelJoinGroupDialogButtonActionPerformed

	private void joinGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinGroupButtonActionPerformed
		joinGroup();
	}//GEN-LAST:event_joinGroupButtonActionPerformed

	private void joinNickFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_joinNickFieldKeyPressed
		joinGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_joinNickFieldKeyPressed

	private void groupListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_groupListKeyPressed
		joinGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_groupListKeyPressed

	private void createGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGroupButtonActionPerformed
		createGroup();
	}//GEN-LAST:event_createGroupButtonActionPerformed

	private void cancelCreateGroupDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelCreateGroupDialogButtonActionPerformed
		hideCreateGroupDialog();
	}//GEN-LAST:event_cancelCreateGroupDialogButtonActionPerformed

	private void groupNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_groupNameFieldKeyPressed
		createGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_groupNameFieldKeyPressed

	private void createNickFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_createNickFieldKeyPressed
		createGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_createNickFieldKeyPressed

	private void groupTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_groupTypeKeyPressed
		createGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_groupTypeKeyPressed

	private void castTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_castTypeKeyPressed
		createGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_castTypeKeyPressed

	private void orderTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderTypeKeyPressed
		createGroupDialogKeyPressed(evt);
	}//GEN-LAST:event_orderTypeKeyPressed

	private void leaveGroupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveGroupMenuItemActionPerformed
		// TODO Move this to separate method
		int selected = tabbedPane.getSelectedIndex();
		String groupName = tabbedPane.getTitleAt(selected);
		try {
			gcom.disconnect(groupName);
		} catch (IOException ex) {
			Debug.log(this,Debug.WARN, null, ex);
		}
		if(selected > 0) {
			tabbedPane.remove(selected);
		}
	}//GEN-LAST:event_leaveGroupMenuItemActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        /*java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {*/
		switch(args.length) {
			case 2:
				Debug.log("Startup", Debug.DEBUG, String.format("Autoconnecting to %s:%s\n",args[0],args[1]));
				new GUIViewOther(args[0],new Integer(args[1])).setVisible(true);
				break;
			case 1:
				Debug.log("Startup", Debug.DEBUG, "Autoconnecting to port " + args[0]);
				new GUIViewOther(new Integer(args[0])).setVisible(true);
				break;
			default:
				Debug.log("Startup", Debug.DEBUG, "No autoconnect");
				new GUIViewOther().setVisible(true);
		}
            /*}
        });*/
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelConnectRMIDialogButton;
    private javax.swing.JButton cancelCreateGroupDialogButton;
    private javax.swing.JButton cancelJoinGroupDialogButton;
    private javax.swing.JComboBox castType;
    private javax.swing.JDialog connectRMIDialog;
    private javax.swing.JMenuItem connectRMIMenuItem;
    private javax.swing.JButton connectToRMIButton;
    private javax.swing.JButton createGroupButton;
    private javax.swing.JDialog createGroupDialog;
    private javax.swing.JMenuItem createGroupMenuItem;
    private javax.swing.JTextField createNickField;
    private javax.swing.JList groupList;
    private javax.swing.JMenu groupMenu;
    private javax.swing.JTextField groupNameField;
    private javax.swing.JComboBox groupType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton joinGroupButton;
    private javax.swing.JDialog joinGroupDialog;
    private javax.swing.JMenuItem joinGroupMenuItem;
    private javax.swing.JTextField joinNickField;
    private javax.swing.JMenuItem leaveGroupMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JComboBox orderType;
    private javax.swing.JTextField rmiAddressField;
    private javax.swing.JTextField rmiPortField;
    private javax.swing.JButton showCreateGroupDialogToolbarButton;
    private javax.swing.JButton showJoinDialogToolbarButton;
    private javax.swing.JButton showRMIDialogButton;
    private javax.swing.JLabel statusConnectedLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JCheckBoxMenuItem viewToolBarMenuItem;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

}
