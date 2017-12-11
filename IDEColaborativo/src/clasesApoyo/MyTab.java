/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clasesApoyo;

import javafx.scene.control.Tab;

/**
 *
 * @author Alonso Lora
 * @author Raymundo Pérez
 */
public class MyTab extends Tab{
    
    private MyTreeItem treeItem;
    
    /**
     * Constructor sobre cargado de Tab
     * @param nombreTab 
     */
    public MyTab(String nombreTab){
        super(nombreTab);
        
    }

    /**
     * Regresa el MyTreeItem
     * @return MyTreeItem donde esta un archivo
     */
    public MyTreeItem getTreeItem() {
        return treeItem;
    }

    /**
     * Da valor al MyTreeItem
     * @param treeItem  MyTreeItem donde esta un archivo
     */
    public void setTreeItem(MyTreeItem treeItem) {
        this.treeItem = treeItem;
    }
}
