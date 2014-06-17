/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversationcreator;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author craig.reese
 */
@XmlType(propOrder = {"id", "prerequisites", "text", "pointer", "actions"})
@XmlRootElement(namespace = "Conversation")

/*
     <id>a25</id>
     <prereq>str>=25,DEX >45 && end >2,sneak > 22</prereq>
     <text> blah blah blah</text>
     <pointer>a26,a27</pointer>
     <actions>First Aid + 2,factionevil - 5</actions>
    */

public class Dialogue {

    private String id;
    private String text;
    private String[] prerequisites;
    private String[] pointers;
    private String[] actions;  //is this correct?
    //TODO: add attribute for character portrait

    public String[] getPrerequisites() {
        return prerequisites;
    }
    
    public Dialogue(String id, String[] prerequisites,  String text, String[] pointers, String[] actions)
    {
        this.id = id;
        this.text = text;
        this. prerequisites = prerequisites;
        this.pointers = pointers;
        this.actions = actions;
    }

    public void setPrerequisites(String[] prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String[] getActions() {
        return actions;
    }

    public void setActions(String[] actions) {
        this.actions = actions;
    }
    

    
    public Dialogue(String id, String text, String[] pointer) {
        this.id = id;
        this.text = text;
        this.pointers = pointer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getPointer() {
        return pointers;
    }

    public void setPointer(String[] pointer) {
        this.pointers = pointer;
    }

    Dialogue() {

    }

}
