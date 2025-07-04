package org.traincontrol.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.traincontrol.marklin.MarklinAccessory;
import org.traincontrol.marklin.MarklinLayoutComponent;
import org.traincontrol.marklin.MarklinRoute;

/**
 * This class represents a right-click menu on the track editor
 * @author Adam
 */
final class LayoutEditorRightclickMenu extends JPopupMenu
{        
    public LayoutEditorRightclickMenu(LayoutEditor edit, TrainControlUI ui, LayoutLabel label, MarklinLayoutComponent component)
    {        
        JMenuItem menuItem;
        
        // Show the name of the component
        if (component != null)
        {
            JMenuItem titleItem = new JMenuItem(component.getUserFriendlyTypeName());
            titleItem.setEnabled(false);
            titleItem.setFont(titleItem.getFont().deriveFont(Font.BOLD));
            add(titleItem);
            addSeparator();
        }
        
        JMenu pasteSubMenu = new JMenu("Paste"); // Create the submenu

        JMenuItem pasteMenuItem = new JMenuItem("Tile");
        pasteMenuItem.addActionListener(event -> 
        {
            try
            {
                edit.executeTool(label, null);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        pasteMenuItem.setToolTipText("Control+V");

        pasteSubMenu.add(pasteMenuItem);

        JMenuItem pasteColumnMenuItem = new JMenuItem("Entire Column");
        pasteColumnMenuItem.addActionListener(event -> 
        {
            try
            {
                edit.executeTool(label, LayoutEditor.bulk.COL);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        pasteColumnMenuItem.setToolTipText("Shift+C");
        
        if (edit.addBoxHighlighted())
        {
            pasteColumnMenuItem.setEnabled(false);
        }

        pasteSubMenu.add(pasteColumnMenuItem);

        JMenuItem pasteRowMenuItem = new JMenuItem("Entire Row");
        pasteRowMenuItem.addActionListener(event -> 
        {
            try
            {
                edit.executeTool(label, LayoutEditor.bulk.ROW);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        pasteRowMenuItem.setToolTipText("Shift+R");

        if (edit.addBoxHighlighted())
        {
            pasteRowMenuItem.setEnabled(false);
        }
        
        if (!edit.hasToolFlag()) 
        {
            pasteSubMenu.setEnabled(false);
        }

        pasteSubMenu.add(pasteRowMenuItem); 

        add(pasteSubMenu); // Add the submenu to the parent menu
        
        menuItem = new JMenuItem("Undo");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.undo();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        if (!edit.canUndo()) menuItem.setEnabled(false);
        menuItem.setToolTipText("Control+Z");
        
        add(menuItem);
        
        menuItem = new JMenuItem("Redo");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.redo();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        if (!edit.canRedo()) menuItem.setEnabled(false);
        menuItem.setToolTipText("Control+Y");
        
        add(menuItem);
        
        if (component != null)
        {
            addSeparator();
            
            menuItem = new JMenuItem("Cut");
            menuItem.addActionListener(event -> 
            {
                try
                {
                    edit.initCopy(label, null, true);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            });
            menuItem.setToolTipText("Control+X");

            add(menuItem);

            menuItem = new JMenuItem("Copy");
            menuItem.addActionListener(event -> 
            {
                try
                {
                    edit.initCopy(label, null, false);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            });
            menuItem.setToolTipText("Control+C");

            add(menuItem);
            
            // Text can't be rotated
            if (!component.isText()
                // These elements are symmetrical
                && component.getNumOrientations() > 1
            )
            {
                menuItem = new JMenuItem("Rotate");
                menuItem.addActionListener(event -> 
                {
                    try
                    {
                        edit.rotate(label);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                });
                menuItem.setToolTipText("Control+R");
            }

            add(menuItem);
                        
            if (component.isClickable())
            {
                addSeparator();
                
                String protocol = "";
    
                if (component.getProtocol() != null)
                {
                    protocol = MarklinAccessory.getProtocolStringForName(component.getProtocol().toString());
                }
                
                String addressLabel = "Address"; // component.getUserFriendlyTypeName() + " Address";
                
                if (component.isLink())
                {
                    addressLabel = "Linked Page";
                }
                else if (component.isRoute())
                {
                    addressLabel = "Route ID";
                }
                else if (component.isFeedback())
                {
                    addressLabel = "Feedback Address";
                }
                
                menuItem = new JMenuItem("Edit " + addressLabel + " (" + component.getLogicalAddress() + protocol + ")");
                menuItem.addActionListener(event -> 
                {
                    try
                    {
                        edit.editAddress(label);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                });
                menuItem.setToolTipText("Control+A");

                add(menuItem);
                
                // Shortcut to edit routes
                if (component.isRoute())
                {
                    // Get the route by address, otherwise it will not change as we edit
                    MarklinRoute route = ui.getModel().getRoute(component.getAddress());
                    
                    if (route != null)
                    {         
                        menuItem = new JMenuItem("Open in Route Editor...");
                        menuItem.addActionListener(event -> 
                        {
                            try
                            {
                                javax.swing.SwingUtilities.invokeLater(new Thread(() -> 
                                {
                                    ui.editRoute(route.getName());
                                }));
                            }
                            catch (Exception e)
                            {
                                JOptionPane.showMessageDialog(this, e.getMessage());
                            }
                        });
                        
                        menuItem.setToolTipText("Shortcut to edit: " + route.getName());
                        add(menuItem);
                    }
                }
            }  

            addSeparator();

            menuItem = new JMenuItem("Edit Text Label");
            menuItem.addActionListener(event -> 
            {
                try
                {
                    edit.editText(label);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            });
            menuItem.setToolTipText("Control+T");
            add(menuItem);
            
            if (ui.getModel().getAutoLayout() != null && !ui.getModel().getAutoLayout().getPoints().isEmpty())
            {     
                menuItem = new JMenuItem("Place Autonomy Station Label");
                menuItem.addActionListener(event -> 
                {
                    try
                    {
                        edit.editTextWithDropdown(label);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                });
                menuItem.setToolTipText("Control+S");
                add(menuItem);
            }

            addSeparator();
            
            menuItem = new JMenuItem("Delete");
            menuItem.addActionListener(event -> 
            {
                try
                {
                    edit.delete(label);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            });
            
            menuItem.setToolTipText("Delete");
            add(menuItem);
        }
        
        addSeparator();
        
        JMenu diagramSubmenu = new JMenu("Diagram"); // Create the submenu
             
        menuItem = new JMenuItem("Increase Size (" + edit.getMarklinLayout().getSx() + " x " + edit.getMarklinLayout().getSy() + ")");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.addRowsAndColumns(1, 1);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        menuItem.setToolTipText("Control+I");
        diagramSubmenu.add(menuItem);
        
        diagramSubmenu.addSeparator();
        
        menuItem = new JMenuItem("Shift Right");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.shiftRight();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        menuItem.setToolTipText("Shifts the entire diagram right from the highlighted column");
        diagramSubmenu.add(menuItem);
                
        menuItem = new JMenuItem("Shift Down");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.shiftDown();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        menuItem.setToolTipText("Shifts the entire diagram down from the highlighted row");
        diagramSubmenu.add(menuItem);
        
                menuItem = new JMenuItem("Shift Left");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.shiftLeft();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        menuItem.setToolTipText("Shifts the entire diagram left from the highlighted column");
        diagramSubmenu.add(menuItem);
        
        menuItem = new JMenuItem("Shift Up");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.shiftUp();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        menuItem.setToolTipText("Shifts the entire diagram up from the highlighted row");
        diagramSubmenu.add(menuItem);
        
        diagramSubmenu.addSeparator();
        
        menuItem = new JMenuItem("Clear Diagram");
        menuItem.addActionListener(event -> 
        {
            try
            {
                edit.clear();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        });
        
        diagramSubmenu.add(menuItem);
        
        add(diagramSubmenu);
    }
}
   