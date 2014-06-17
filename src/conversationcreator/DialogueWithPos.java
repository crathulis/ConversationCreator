/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conversationcreator;

import java.awt.Dimension;

/**
 *
 * @author craig.reese
 */
public class DialogueWithPos {
    Dialogue d;
    Dimension dim;
    
    public DialogueWithPos (Dialogue d, Dimension dim)
    {
        this.d = d;
        this.dim = dim;
    }

    public Dialogue getD() {
        return d;
    }

    public void setD(Dialogue d) {
        this.d = d;
    }

    public Dimension getDim() {
        return dim;
    }

    public void setDim(Dimension dim) {
        this.dim = dim;
    }
    
    
    
}
