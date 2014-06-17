/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversationcreator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author craig.reese
 */
public class ConversationCreatorWindow extends javax.swing.JFrame {

    ArrayList<Conversation> allConversations;
    Conversation currentConversation = null;
    Dialogue currentDialogue = null;
    Boolean needsToBeWritten = false;
    File xmlFile = null;
    ConversationList list = new ConversationList();
    JFrame treeView;
    JPanel infoPanel;
    ArrayList<Dialogue> alreadyDrawn;
    ArrayList<NewPanel> panelList;
    TreeClass newtree;

    public ConversationCreatorWindow() {
        initComponents();
/*
        treeView = new JFrame();
        treeView.setSize(1024, 768);
        treeView.setVisible(true);
        treeView.setLayout(null);
        infoPanel = new JPanel();
        infoPanel.setSize(1024, 1500);
        infoPanel.setLocation(0, 0);
        infoPanel.setLayout(null);
        //treeView.add(infoPanel);
        Graphics g;
        panelList = new ArrayList();
        //figure out how many jpanels we need.
        /*
         JButton button = new JButton();
         button.setText("start");
         button.setSize(80,25);
         button.setLocation(0, 0);
         infoPanel.add(button);
         infoPanel.addMouseListener(new MouseListener(){

         @Override
         public void mouseClicked(MouseEvent me) {
         System.out.println(me.getX() + " " + me.getY());
         }

         @Override
         public void mousePressed(MouseEvent me) {
         // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         @Override
         public void mouseReleased(MouseEvent me) {
         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         @Override
         public void mouseEntered(MouseEvent me) {
         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         @Override
         public void mouseExited(MouseEvent me) {
         //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }
        
         });
         */
        // SetXMLFile();
        //infoPanel.validate();
        allConversations = GetAllConversations();
        ArrayList list = new ArrayList();
        for (Conversation con : allConversations) {
            list.add(con.getConversationName());

        }

        this.ConversationList.setListData(list.toArray());

        ConversationList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                    int index = lse.getFirstIndex();
                    System.out.println(ConversationList.getSelectedValue().toString());
                    PopulateDialogueList(ConversationList.getSelectedValue().toString());

                }
            }
        });

        DialogueList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                    int index = lse.getFirstIndex();
                    System.out.println(ConversationList.getSelectedValue().toString());
                    PopulateDialogueQuestionaire(DialogueList.getSelectedValue().toString());

                }
            }

        });

    }

    private void RefreshDialogueList() {

        ArrayList dialogueList = new ArrayList();

        for (Dialogue dialogue : currentConversation.getDialogueList()) {
            dialogueList.add(dialogue.getId());
        }
        this.DialogueList.setListData(dialogueList.toArray());
    }

    private void RefreshConversations() {
        ArrayList list = new ArrayList();
        for (Conversation con : allConversations) {
            list.add(con.getConversationName());

        }

        this.ConversationList.setListData(list.toArray());
    }

    public void PopulateDialogueQuestionaire(String dialogueID) {
        currentDialogue = null;
        for (Dialogue d : currentConversation.getDialogueList()) {
            if (d.getId().equals(dialogueID)) {
                currentDialogue = d;
                break;
            }
        }
        txtID.setText(currentDialogue.getId());
        txtText.setText(currentDialogue.getText());

        txtPrerequisites.setText("");

        for (String pre : currentDialogue.getPrerequisites()) {
            if (!pre.equals("")) {
                if (!txtPrerequisites.getText().equals("")) {
                    txtPrerequisites.setText(txtPrerequisites.getText() + ", " + pre);
                } else {
                    txtPrerequisites.setText(pre);
                }

            }
        }

        txtPointers.setText("");

        for (String pre : currentDialogue.getPointer()) {
            if (!pre.equals("")) {
                if (!txtPointers.getText().equals("")) {
                    txtPointers.setText(txtPointers.getText() + ", " + pre);
                } else {
                    txtPointers.setText(pre);
                }

            }
        }

        txtActions.setText("");

        for (String pre : currentDialogue.getActions()) {
            if (!pre.equals("")) {
                if (!txtActions.getText().equals("")) {
                    txtActions.setText(txtActions.getText() + ", " + pre);
                } else {
                    txtActions.setText(pre);
                }

            }
        }

    }

    private void PopulateDialogueList(String conversationName) {

        for (Conversation con : allConversations) {
            if (con.getConversationName().equals(conversationName)) {
                currentConversation = con;
                
        newtree = new TreeClass(con,this);
                break;
            }
        }

        ArrayList dialogueList = new ArrayList();

        for (Dialogue dialogue : currentConversation.getDialogueList()) {
            dialogueList.add(dialogue.getId());
        }
        this.DialogueList.setListData(dialogueList.toArray());
        //RefreshTree();

    }

    

    private ArrayList<Conversation> GetAllConversations() {
        ArrayList<Conversation> conversationList = null;
        try {

            // create JAXB context and initializing Marshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(ConversationList.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // specify the location and name of xml file to be read
            //File XMLfile = new File("./Arena1_conversation.xml");
            // this will create Java object - country from the XML file
            //Country countryIndia = (Country) jaxbUnmarshaller.unmarshal(XMLfile);
            ConversationList list = null;

            while (list == null) {
                SetXMLFile();
                try {
                    list = (ConversationList) jaxbUnmarshaller.unmarshal(xmlFile);
                } catch (javax.xml.bind.UnmarshalException ex) {
                    JOptionPane.showMessageDialog(null, "The file you have chosen is not in the correct format.  Please find the conversation file and try again.");

                }
            }

            //System.out.println("Conversation Name: " + countryIndia.getCountryName());
            //System.out.println("Country Population: " + countryIndia.getCountryPopulation());
            //ArrayList<state> listOfStates = countryIndia.getListOfStates();
            conversationList = list.getAllConversations();

            /*
             int i = 0;
             for (State state : listOfStates) {
             i++;
             System.out.println("State:"+i + ' ' + state.getStateName());
             }*/
        } catch (JAXBException e) {
            // some exception occured
            e.printStackTrace();
        }

        return conversationList;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ConversationList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        DialogueList = new javax.swing.JList();
        btnAddConversation = new javax.swing.JButton();
        btnDeleteConversation = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAddDialogue = new javax.swing.JButton();
        btnDeleteDialogue = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        txtPrerequisites = new javax.swing.JTextField();
        txtPointers = new javax.swing.JTextField();
        txtActions = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtText = new javax.swing.JTextArea();
        btnUpdateDialogue = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        ConversationList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        ConversationList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ConversationList.setName("ConversationList"); // NOI18N
        jScrollPane1.setViewportView(ConversationList);

        DialogueList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        DialogueList.setName("ConversationList"); // NOI18N
        jScrollPane2.setViewportView(DialogueList);

        btnAddConversation.setText("New Conversation");
        btnAddConversation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddConversationActionPerformed(evt);
            }
        });

        btnDeleteConversation.setText("Delete Conversation");
        btnDeleteConversation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteConversationActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Conversations");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Dialogues");

        btnAddDialogue.setText("Add Dialogue");
        btnAddDialogue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDialogueActionPerformed(evt);
            }
        });

        btnDeleteDialogue.setText("Delete Dialogue");
        btnDeleteDialogue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDialogueActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("ID:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Text:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Prerequisites:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Pointers:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Actions:");

        txtID.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtPrerequisites.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtPointers.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtActions.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("-Prerequisites, pointers and actions are all\ncomma seperated.");
        jScrollPane3.setViewportView(jTextArea1);

        txtText.setColumns(20);
        txtText.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtText.setLineWrap(true);
        txtText.setRows(5);
        txtText.setWrapStyleWord(true);
        jScrollPane4.setViewportView(txtText);

        btnUpdateDialogue.setText("Update Dialogue");
        btnUpdateDialogue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDialogueActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("*");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("*");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("*");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Write Changes to XML File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnAddConversation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDeleteConversation, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDeleteDialogue)
                                .addGap(159, 159, 159)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel2)))
                                .addGap(110, 110, 110)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)))
                                .addGap(18, 18, 18)
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(227, 227, 227))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(17, 17, 17)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel6))
                                        .addComponent(jLabel7))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtPointers)
                                            .addComponent(txtActions)
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnAddDialogue, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnUpdateDialogue, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(17, 17, 17)))
                                        .addComponent(txtPrerequisites, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAddConversation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnDeleteConversation)
                            .addComponent(btnDeleteDialogue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton1)
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel9))
                                .addGap(50, 50, 50))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPrerequisites, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPointers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddDialogue)
                            .addComponent(btnUpdateDialogue))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateDialogueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDialogueActionPerformed
        //TODO: add in code to check if file needs to be re-written

        if (txtID.getText().equals("") || txtText.getText().equals("") || txtPointers.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Missing a required field, please check required fields and try again");
            return;
        }

        currentDialogue.setActions(txtActions.getText().split(","));
        currentDialogue.setId(txtID.getText());
        currentDialogue.setPointer(txtPointers.getText().split(","));
        currentDialogue.setPrerequisites(txtPrerequisites.getText().split(","));
        currentDialogue.setText(txtText.getText());

        //JOptionPane.showMessageDialog(null, "Dialogue successfully updated! Remember to Re-Write the xml file before you close the program!");
        this.newtree.RefreshTree();
    }//GEN-LAST:event_btnUpdateDialogueActionPerformed

    private void btnAddDialogueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDialogueActionPerformed
        Dialogue d = new Dialogue();

        if (txtID.getText().equals("") || txtText.getText().equals("") || txtPointers.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Missing a required field, please check required fields and try again");
            return;
        }

        boolean isUnique = true;
        for (Dialogue dia : currentConversation.getDialogueList()) {
            if (dia.getId().equals(txtID.getText())) {
                isUnique = false;
            }
        }

        if (isUnique == false) {
            JOptionPane.showMessageDialog(null, "Dialogue ID must be unique, please fix this and try to add again");
            return;
        }

        d.setActions(txtActions.getText().split(","));
        d.setId(txtID.getText());
        d.setPointer(txtPointers.getText().split(","));
        d.setPrerequisites(txtPrerequisites.getText().split(","));
        d.setText(txtText.getText());

        currentConversation.AddDialogue(d);
        RefreshDialogueList();

        //JOptionPane.showMessageDialog(null, "Dialogue successfully added! Remember to Re-Write the xml file before you close the program!");
        this.newtree.RefreshTree();
    }//GEN-LAST:event_btnAddDialogueActionPerformed

    private void btnAddConversationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddConversationActionPerformed
        Conversation con = new Conversation();
        String name = "";

        Boolean isUnique = false;
        while (isUnique == false) {
            isUnique = true;
            name = JOptionPane.showInputDialog("What is the name of the new conversation?");
            while (name.equals("")) {
                name = JOptionPane.showInputDialog("Conversation name can not be blank, please try again.");
            }

            for (Conversation conver : allConversations) {
                if (conver.getConversationName().equals(name)) {
                    isUnique = false;
                }
            }

        }
        con.setConversationName(name);
        //we have a conversation name
        allConversations.add(con);
        RefreshConversations();
    }//GEN-LAST:event_btnAddConversationActionPerformed

    private void btnDeleteConversationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteConversationActionPerformed
        if (ConversationList.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(null, "Please select a conversation first");
            return;
        }

        if (JOptionPane.showConfirmDialog(null, "Are you SURE you want to delete this conversation?????", "Are you sure??", JOptionPane.YES_NO_OPTION) == (JOptionPane.YES_OPTION)) {
            for (Conversation con : allConversations) {
                if (con.getConversationName().equals(ConversationList.getSelectedValue().toString())) {
                    allConversations.remove(con);
                    JOptionPane.showMessageDialog(null, ConversationList.getSelectedValue().toString() + " has been deleted.  Make sure to re-write the xml before quitting.");
                    RefreshConversations();
                    return;
                }
            }
        }
    }//GEN-LAST:event_btnDeleteConversationActionPerformed

    private void btnDeleteDialogueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDialogueActionPerformed
        if (DialogueList.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(null, "Please select a dialogue first");
            return;
        }

        if (JOptionPane.showConfirmDialog(null, "Are you SURE you want to delete this dialogue?????", "Are you sure??", JOptionPane.YES_NO_OPTION) == (JOptionPane.YES_OPTION)) {
            for (Dialogue d : currentConversation.getDialogueList()) {
                if (d.getId().equals(DialogueList.getSelectedValue().toString())) {
                    currentConversation.getDialogueList().remove(d);
                    JOptionPane.showMessageDialog(null, DialogueList.getSelectedValue().toString() + " has been deleted.  Make sure to re-write the xml before quitting.");
                    RefreshDialogueList();
                    return;
                }
            }
        }
    }//GEN-LAST:event_btnDeleteDialogueActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you SURE you want to write your changes to the XML file?  This can't be undone!", "Are you sure??", JOptionPane.YES_NO_OPTION) == (JOptionPane.YES_OPTION)) {
            try {
                JAXBContext context = null;
                try {
                    context = JAXBContext.newInstance(ConversationList.class);
                } catch (JAXBException ex) {
                    Logger.getLogger(ConversationCreatorWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                Marshaller m = null;
                try {
                    m = context.createMarshaller();
                } catch (JAXBException ex) {
                    Logger.getLogger(ConversationCreatorWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                try {
                    // Write to System.out
                    //m.marshal(skillSet, System.out);
                    // Write to File
                    list.setAllConversations(allConversations);
                    m.marshal(list, xmlFile);
                } catch (JAXBException ex) {
                    Logger.getLogger(ConversationCreatorWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (PropertyException ex) {
                Logger.getLogger(ConversationCreatorWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void SetXMLFile() {
        /*
         final JFileChooser fc = new JFileChooser();
         fc.setDialogTitle("Please choose the conversation XML file");
         FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
         fc.setFileFilter(xmlfilter);
         int returnVal = fc.showOpenDialog(this);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
         xmlFile = fc.getSelectedFile();
         }
         */
        xmlFile = new File("C:\\Users\\craig.reese\\Documents\\Git\\SVUGame\\Arena1_conversation.xml");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConversationCreatorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConversationCreatorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConversationCreatorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConversationCreatorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConversationCreatorWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ConversationList;
    private javax.swing.JList DialogueList;
    private javax.swing.JButton btnAddConversation;
    private javax.swing.JButton btnAddDialogue;
    private javax.swing.JButton btnDeleteConversation;
    private javax.swing.JButton btnDeleteDialogue;
    private javax.swing.JButton btnUpdateDialogue;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField txtActions;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPointers;
    private javax.swing.JTextField txtPrerequisites;
    private javax.swing.JTextArea txtText;
    // End of variables declaration//GEN-END:variables
}
