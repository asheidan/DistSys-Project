/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DebugUI.java
 *
 * Created on Oct 26, 2009, 9:00:30 AM
 */

package gui;

import gcom.Debug;
import gcom.HashVectorClock;
import gcom.ReliableCommunicationModule;
import gcom.interfaces.DebugInterface;
import gcom.interfaces.GComMessageListener;
import gcom.interfaces.Message;
import gcom.interfaces.MessageOrderingModule;
import java.util.Vector;

/**
 *
 * @author emil
 */
public class DebugUI extends javax.swing.JFrame implements DebugInterface,Runnable {

	private MessageOrderingModule mom;
	private ReliableCommunicationModule rcom;
	private String groupName;
	private MockListModel<Message> holdBack = new MockListModel<Message>(new Vector<Message>());
	private MockListModel<Message> queue;
	private HashVectorClock clock;
	private boolean run = true;
	private Thread t;

	@Override
	public void addMessageListener(GComMessageListener listener) {
		mom.addMessageListener(listener);
	}

	@Override
	public HashVectorClock getClock() {
		return mom.getClock();
	}

	@Override
	public void tick() {
		mom.tick();
		clocksTextArea.setText(clock.prettyPrint());
	}
	
	@Override
	public void gotClock(HashVectorClock c) {}

	@Override
	public void attachDebugger(MessageOrderingModule mom) {
		Debug.log(this, Debug.DEBUG, groupName + ": Ordering module attached");
		this.mom = mom;
	}

	@Override
	public void attachDebugger(ReliableCommunicationModule rcom) {
		Debug.log(this, Debug.DEBUG, groupName + ": Reliable commod attached");
		this.rcom = rcom;
	}

	@Override
	public void receive(Message m) {
		if(rcom != null) {
			if(holdCheckBox.isSelected()) {
				holdBack.add(m);
			}
			else {
				rcom.actualReceive(m);
				queue.update();
			}
		}
	}

	
	@Override
	public void attachDebugger(HashVectorClock clock, Vector<Message> messages) {
		Debug.log(this, Debug.DEBUG, groupName + ": Message queue attached");
		queue = new MockListModel<Message>(messages);
		queueList.setModel(queue);
		this.clock = clock;
		clocksTextArea.setText(clock.prettyPrint());
	}

	@Override
	public void attachDebugger(DebugInterface debug) {
		// Another debugger?!
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public void queueMessage(Message m) {
		if(mom != null) {
			Debug.log(this, Debug.TRACE, groupName + ": Message!!");
			if(holdCheckBox.isSelected()) {
				holdBack.add(m);
			}
			else {
				mom.queueMessage(m);
				queue.update();
			}
		}
		else Debug.log(this, Debug.FATAL, groupName + ": using recieve without attached com");
	}

	@Override
	public void run() {
		while(run) {
			if(clock != null) clocksTextArea.setText(clock.prettyPrint());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				stop();
			}
		}
	}
	public void stop() {
		run = false;
	}

	/** Creates new form DebugUI */
    public DebugUI(String groupName) {
        initComponents();
		holdBackList.setModel(holdBack);
		this.groupName = groupName;
		setTitle("Debug: "+groupName);
		setVisible(true);
		t = new Thread(this);
		t.start();
    }
	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        holdBackList = new javax.swing.JList();
        moveUpButton = new javax.swing.JButton();
        holdbackLabel = new javax.swing.JLabel();
        moveDownButton = new javax.swing.JButton();
        holdCheckBox = new javax.swing.JCheckBox();
        dropButton = new javax.swing.JButton();
        releaseButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        queueList = new javax.swing.JList();
        queueLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        clocksTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        holdBackList.setFont(new java.awt.Font("Monospaced", 0, 12));
        holdBackList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        holdBackList.setToolTipText("");
        holdBackList.setName("holdBackList"); // NOI18N
        jScrollPane1.setViewportView(holdBackList);

        moveUpButton.setText("Move up");
        moveUpButton.setName("moveUpButton"); // NOI18N
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        holdbackLabel.setText("Hold buffer:");
        holdbackLabel.setName("holdbackLabel"); // NOI18N

        moveDownButton.setText("Move down");
        moveDownButton.setName("moveDownButton"); // NOI18N
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });

        holdCheckBox.setLabel("Hold messages");
        holdCheckBox.setName("holdCheckBox"); // NOI18N

        dropButton.setText("Drop");
        dropButton.setName("dropButton"); // NOI18N
        dropButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropButtonActionPerformed(evt);
            }
        });

        releaseButton.setText("Release");
        releaseButton.setName("releaseButton"); // NOI18N
        releaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        queueList.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        queueList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        queueList.setToolTipText("");
        queueList.setName("queueList"); // NOI18N
        jScrollPane2.setViewportView(queueList);

        queueLabel.setText("Message queue:");
        queueLabel.setName("queueLabel"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        clocksTextArea.setColumns(13);
        clocksTextArea.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        clocksTextArea.setRows(5);
        clocksTextArea.setText("no clock");
        clocksTextArea.setName("clocksTextArea"); // NOI18N
        jScrollPane3.setViewportView(clocksTextArea);

        jLabel1.setText("Clocks:");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(holdbackLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(holdCheckBox)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(releaseButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dropButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveUpButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(moveDownButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(queueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(moveUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(moveDownButton)
                .addGap(18, 18, 18)
                .addComponent(dropButton)
                .addGap(18, 18, 18)
                .addComponent(releaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(holdCheckBox)
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(holdbackLabel)
                    .addComponent(queueLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed
		int selectedIndex = holdBackList.getSelectedIndex();
		if(selectedIndex > 0) {
			holdBack.moveUp(selectedIndex);
			holdBackList.setSelectedIndex(selectedIndex-1);
		}
	}//GEN-LAST:event_moveUpButtonActionPerformed

	private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed
		int selectedIndex = holdBackList.getSelectedIndex();
		if(selectedIndex < holdBack.getSize() - 1) {
			holdBack.moveDown(selectedIndex);
			holdBackList.setSelectedIndex(selectedIndex+1);
		}
	}//GEN-LAST:event_moveDownButtonActionPerformed

	private void dropButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropButtonActionPerformed
		holdBack.drop(holdBackList.getSelectedIndex());
	}//GEN-LAST:event_dropButtonActionPerformed

	private void releaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseButtonActionPerformed
		if(mom != null && holdBack.getSize() > 0) {
			mom.queueMessage(holdBack.drop(0));
			queue.update();
		}
		else if(rcom != null && holdBack.getSize() > 0) {
			rcom.actualReceive(holdBack.drop(0));
			queue.update();
		}
	}//GEN-LAST:event_releaseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea clocksTextArea;
    private javax.swing.JButton dropButton;
    private javax.swing.JList holdBackList;
    private javax.swing.JCheckBox holdCheckBox;
    private javax.swing.JLabel holdbackLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JLabel queueLabel;
    private javax.swing.JList queueList;
    private javax.swing.JButton releaseButton;
    // End of variables declaration//GEN-END:variables

}
