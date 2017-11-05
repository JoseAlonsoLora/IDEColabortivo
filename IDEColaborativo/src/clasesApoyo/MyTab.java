/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import javafx.scene.control.Tab;

/**
 *
 * @author alonso
 */
public class MyTab extends Tab{
    
    private MyTreeItem treeItem;
    
    public MyTab(String nombreTab){
        super(nombreTab);
        
    }

    public MyTreeItem getTreeItem() {
        return treeItem;
    }

    public void setTreeItem(MyTreeItem treeItem) {
        this.treeItem = treeItem;
    }
    
    

    
    
    
}
