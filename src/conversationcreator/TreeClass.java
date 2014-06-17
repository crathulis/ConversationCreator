/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversationcreator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.sound.sampled.Line;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author craig.reese
 */
public class TreeClass extends JPanel {

    Conversation currentConversation = null;
    JFrame treeView;
    ArrayList<NewPanel> panelList;
    ArrayList<Dialogue> alreadyDrawn;
    Dimension start = new Dimension();
    Dimension end = new Dimension();
    JPanel testPanel;
    ArrayList<LineHolder> lineArray = new ArrayList();
    ConversationCreatorWindow window;

    /*
     @Override
     public void paintComponent(Graphics g) {
     super.paintComponent(g);
     g.drawLine(start.width, start.height, end.width, end.height);
     }
     */
    /*
     @Override
     public void paint(Graphics g)
     {
        
     super.paint(g);
     
     }
     */
    @Override
    public void paintComponent(Graphics g) {
    // Let UI Delegate paint first, which 
        // includes background filling since 
        // this component is opaque.

        super.paintComponent(g);
        for (LineHolder line : lineArray) {
            g.drawLine(line.getStartx(), line.getStarty(), line.getEndx(), line.getEndy());
            System.out.println("drawing a line: startx: " + line.getStartx() + " starty: " + line.getStarty() + " endx: " + line.getEndx() + " endy: " + line.getEndy());
        }
        // g.drawLine(800, 0,700, 106);
        System.out.println("paintcomponent is painting");
    //redSquare.paintSquare(g);
    }

    private TreeClass() {
       // BufferedImage bim = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_RGB);
        // Graphics b = bim.getGraphics();
        // b.drawLine(0, 0, 1000, 1000);
    }
    
    private void Init()
    {
        
        //RefreshTree();
        
    }

    public TreeClass(Conversation currentConversation, ConversationCreatorWindow w) {
        window = w;
        this.currentConversation = currentConversation;
        treeView = new JFrame();
        treeView.setSize(1024, 768);
        treeView.setLayout(null);
        treeView.setLocation(0, 0);
        treeView.setVisible(true);
        testPanel = this;
        testPanel.setSize(treeView.getSize());
        testPanel.setVisible(true);
        testPanel.setLocation(0, 0);
        testPanel.setOpaque(false);
        //testPanel.setBackground(Color.yellow);
        treeView.add(testPanel);

        panelList = new ArrayList();

        treeView.getContentPane().repaint();
        treeView.validate();
        RefreshTree(currentConversation);
        //Init();
       // treeView.repaint();
        //this.repaint();

    }

    public void RefreshTree(Conversation currentConversation) {
        this.currentConversation = currentConversation;
        //this method will populate our tree structure to our new JFrame
        //we will start in the middle then go out from there
        //we will always have a starter so lets add that manually
        //Init();
        treeView.getContentPane().removeAll();
        
        treeView.add(testPanel);
        //treeView.getContentPane().repaint();
        treeView.revalidate();
        alreadyDrawn = new ArrayList();
        panelList = new ArrayList();
        lineArray = new ArrayList();

        Dialogue d = currentConversation.GetDialogue("start");
        if (d == null) {
            return;  //there is no start, cant do anything else
        }
        JPanel panel = new JPanel();
        panel.setSize(130, treeView.getSize().height);
        panel.setLocation(0, 0);
        panel.setBackground(Color.cyan);
        treeView.add(panel);
        NewPanel newpanel = new NewPanel();
        newpanel.setPanel(panel);
        newpanel.setPosition(0);
        panelList.add(newpanel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton button = new JButton();
        button.setSize(100, 25);
        button.setText(d.getId());
        button.setToolTipText(d.getText());
        NewPanel tempPanel = GetPanel(0);
        tempPanel.panel.add(Box.createVerticalGlue());
        tempPanel.panel.add(button);
        tempPanel.panel.add(Box.createVerticalGlue());
        RePaintPanels();
        //now lets go through its children and make all their buttons.
        //every child should get an equal portion of the screen based on how many children there are.
        //we will need a list of already drawn nodes which we check against to make sure it's not a duplicate
        //i think we'll need a recurring method that calls itself until it reaches the end or it's linked back to nodes
        DrawNodes(130, treeView.getSize().height, currentConversation.GetAllLinkingDialogues(d.getId()), new Dimension(0, (int) (treeView.getSize().height / 2)));
        DrawLines();
        //infoPanel.add(start);
        //infoPanel.revalidate();
        //infoPanel.repaint();
    }
    /*
     @Override
     public void paint(Graphics g)
     {
     treeView.paint(g);
     g.drawLine(0, 0, 100, 100);
     }
     */

    private NewPanel GetPanel(int x) {
        for (NewPanel panel : panelList) {
            if (panel.position == x) {
                return panel;
            }
        }
        return null;
    }

    private void RePaintPanels() {
        for (NewPanel newpanel : panelList) {
            JPanel temp = newpanel.panel;
            temp.revalidate();
            temp.repaint();
            testPanel.repaint();
        }
    }

    private void DrawNodes(int x, int ySize, ArrayList<Dialogue> nodes, Dimension parentNodeDim) {
        boolean foundPanel = false;
        for (NewPanel newpanel : panelList) {
            if (newpanel.position == x) {
                foundPanel = true;
            }
        }

        if (foundPanel == false) {
            //create a panel
            JPanel panel = new JPanel();
            panel.setSize(130, treeView.getSize().height);
            panel.setLocation(x, 0);
            panel.add(Box.createVerticalGlue());
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.cyan);
            treeView.add(panel);
            NewPanel newpanel = new NewPanel();
            newpanel.setPanel(panel);
            newpanel.setPosition(x);
            panelList.add(newpanel);
        }

        //first thing is we draw all of this nodes for this level
        //TODO: Fix spacing.  We know the distance between two nodes.
        //we need to pass the information of where the current node is, and the space that the new nodes can use.
        ArrayList<DialogueWithPos> tempNotFound = new ArrayList();
        int steps = 0;
        int i = 1;
        for (Dialogue node : nodes) {
            if (node != null) {
                int startingPosition = parentNodeDim.height - (ySize / 2);
                steps = ySize / (nodes.size() + 1);
                //at this point we know how much space should be inbetween each button, but now we need to find our starter space

                boolean found = false;

                final JButton button = new JButton();
                button.setSize(100, 25);
                button.setText(node.getId());
                button.setToolTipText(node.getText());
                button.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        window.PopulateDialogueQuestionaire(button.getText());
                    }
                    
                });

                for (Dialogue drawnNode : alreadyDrawn) {
                    if (drawnNode.getId().equals(node.getId())) {
                        button.setBackground(Color.magenta);
                        found = true;
                    }
                }
                for(String s : node.getPointer())
                {
                    if(s.equals("end")){
                        button.setBackground(Color.red);
                    }
                }
                //if(nodes.size() ==1)
                // {
                //button.setLocation(x, parentNodeDim.height);
                NewPanel tempPanel = GetPanel(x);
                //check to see if the node is already in the panel
                boolean foundDuplicate = false;
                for (int j = 0; j < tempPanel.panel.getComponentCount(); j++) {
                    if ((Object) tempPanel.panel.getComponent(j) instanceof Box.Filler) {

                    } else {
                        JButton childButton = (JButton) tempPanel.panel.getComponent(j);
                        if(childButton.getText().equals(button.getText()))
                        {
                            foundDuplicate = true;
                        }
                    }
                }
                if(foundDuplicate != true)
                {
                    tempPanel.panel.add(button);
                    tempPanel.panel.add(Box.createVerticalGlue());
                    RePaintPanels();
                }
                
               // }
                // else
                // {

                //     button.setLocation(x, startingPosition + (i*steps));
                // }
                System.out.println("new button: " + button.getText() + " at x: " + button.getLocation().x + " y: " + button.getLocation().y);

                if (found != true) {
                    alreadyDrawn.add(node);
                    DialogueWithPos pos = new DialogueWithPos(node, new Dimension(x, startingPosition + (i * steps)));
                    tempNotFound.add(pos);
                    //DrawNodes(x+100,ySize/2,currentConversation.GetAllLinkingDialogues(node.getId()));
                }
                //infoPanel.add(button);
                //infoPanel.revalidate();
                //infoPanel.repaint();
                treeView.revalidate();
                treeView.repaint();
                System.out.println("i: " + i + " total nodes: " + nodes.size());
                i++;

            }
        }

        int j = 1;
        for (DialogueWithPos d : tempNotFound) {

            DrawNodes(d.dim.width + 130, steps, currentConversation.GetAllLinkingDialogues(d.d.getId()), new Dimension(d.dim.width, d.dim.height));

            //  }
        }
    }

    private void DrawLines() {
        for (NewPanel panel : panelList) {
            int componentcount = panel.panel.getComponentCount();
            for (int i = 0; i < componentcount; i++) {

                if ((Object) panel.panel.getComponent(i) instanceof Box.Filler) {

                } else {
                    JButton button = (JButton) panel.panel.getComponent(i);
                    ArrayList<Dialogue> childrenList = currentConversation.GetAllLinkingDialogues(button.getText());
                    NewPanel childrenPanel = GetPanel(panel.position + 130);
                    for (Dialogue d : childrenList) {
                        if (childrenPanel != null) {
                            for (int j = 0; j < childrenPanel.panel.getComponentCount(); j++) {
                                if ((Object) childrenPanel.panel.getComponent(j) instanceof Box.Filler) {

                                } else {

                                    JButton childButton = (JButton) childrenPanel.panel.getComponent(j);
                                    if (childButton != null && d != null) {
                                        if (childButton.getText().equals(d.getId())) {
                                            LineHolder line = new LineHolder(button.getX() + panel.position +button.getSize().width, button.getY()+button.getSize().height/2, childButton.getX() + panel.position + 130, childButton.getY()+childButton.getSize().height/2);
                                            lineArray.add(line);

                                            System.out.println("trying to paint line");
                                            repaint();
                                            testPanel.repaint();
                                            testPanel.validate();
                                            repaint();
                                            RePaintPanels();
                                            //this.repaint();
                                            //treeView.repaint();
                                            //infoPanel.repaint();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

    }

}
