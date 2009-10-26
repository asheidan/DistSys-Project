/*
 * GroupPanel.java
 */

package gui;
import gcom.Debug;
import gcom.interfaces.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import org.apache.log4j.Level;

/**
 *
 * @author emil
 */
public class GroupPanel extends javax.swing.JPanel implements ActionListener,gcom.interfaces.ViewChangeListener,gcom.interfaces.GComMessageListener {
	private GUIViewOther mainPanel;
	private Vector<MessageSender> senders = new Vector<MessageSender>();
	private String groupName;
	private JTabbedPane parent;
    /** Creates new form GroupPanel */
    public GroupPanel(GUIViewOther mainPanel, String groupName, JTabbedPane parent) {
		this.groupName = groupName;
		this.mainPanel = mainPanel;
		this.parent = parent;
        initComponents();
		textField.addActionListener(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        nodeList = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        textField = new javax.swing.JTextField();

        setName("Form"); // NOI18N

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerSize(0);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(1.0);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(208, 160));
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setLastDividerLocation(100);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(208, 140));
        jSplitPane1.setName("jSplitPane1"); // NOI18N
        jSplitPane1.setVerifyInputWhenFocusTarget(false);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(0, 23));
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(GroupPanel.class);
        nodeList.setFont(resourceMap.getFont("nodeList.font")); // NOI18N
        nodeList.setModel(new javax.swing.DefaultListModel());
        nodeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        nodeList.setName("nodeList"); // NOI18N
        jScrollPane2.setViewportView(nodeList);

        jSplitPane1.setRightComponent(jScrollPane2);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        textArea.setColumns(20);
        textArea.setEditable(false);
        textArea.setFont(nodeList.getFont());
        textArea.setRows(5);
        textArea.setName("textArea"); // NOI18N
        jScrollPane3.setViewportView(textArea);

        jSplitPane1.setLeftComponent(jScrollPane3);

        jSplitPane2.setLeftComponent(jSplitPane1);

        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        textField.setName("textField"); // NOI18N
        jSplitPane2.setRightComponent(textField);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JList nodeList;
    private javax.swing.JTextArea textArea;
    private javax.swing.JTextField textField;
    // End of variables declaration//GEN-END:variables

	@Override
	public void gotMember(Member member) {
		Debug.log("netbeansgui.GroupPanel",Debug.DEBUG,"Tab for " + groupName + " got member " + member);
		((DefaultListModel)nodeList.getModel()).addElement(member);
		//nodeList.repaint();
		append(String.format(">>> %s joined group (%s)",member.getName(),member.getID()));
	}

	@Override
	public void lostMember(Member member) {
		Debug.log("netbeansgui.GroupPanel",Debug.DEBUG,"Tab for " + groupName + " lost member " + member);
		((DefaultListModel)nodeList.getModel()).removeElement(member);
		append(String.format("<<< %s left group (%s)",member.getName(),member.getID()));
	}

	@Override
	public void messageReceived(Message message) {
		/* This allows for primitive formatting
		 * %0s - senders id
		 * %1s - senders chosen nick
		 * %2s - message
		 */
		append(String.format("<%s> %s",message.getSource().getName(),message.getMessage().toString()));
	}

	public void append(String line) {
		textArea.setText(
			textArea.getText() + mainPanel.getTimeStampFormatter().format(new Date()) +
			line + "\n");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String line = event.getActionCommand();
		textField.setText("");
		Debug.log(this, Debug.TRACE, "Got event in panel "+line);
		for(MessageSender sender : senders) {
			try {sender.sendMessage(groupName, line);} catch(IOException e) {}
		}
		//append(String.format("> %s",line));
	}

	public void addMessageSender(MessageSender sender) {
		senders.add(sender);
	}

	@Override
	public void lostGroup(String groupName) {
		Debug.log(this, Debug.TRACE, "Lost group: " + groupName);
		if(parent != null) parent.remove(this);
	}

	@Override
	public String toString() {
		return String.format("GroupPanel: %s", groupName);
	}
}
