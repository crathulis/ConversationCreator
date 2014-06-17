/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conversationcreator;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

/**
 *
 * @author craig.reese
 */
public class ConversationList {
    
    private ArrayList<Conversation> allConversations = new ArrayList();
    
    @XmlElementWrapper(name = "allConversations")
    @XmlElement(name = "conversation")
    public void setAllConversations(ArrayList<Conversation> allConversations) {
        this.allConversations = allConversations;
    }

    public ArrayList<Conversation> getAllConversations() {
        return allConversations;
    }
    
    
    
}
