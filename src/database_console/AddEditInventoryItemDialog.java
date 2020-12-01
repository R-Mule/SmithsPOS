package database_console;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**

 @author R-Mule
 */
public class AddEditInventoryItemDialog extends JDialog {

    MainFrame mf;

    private boolean isEditMode;
    private Item editingItem;
    private JComboBox createdCodeDropDown;
    private JTextField mutIdField;
    private JTextField upcField;
    private JTextField nameField;
    private JTextField costField;
    private JTextField priceField;
    private JRadioButton isTaxedButton;

    public AddEditInventoryItemDialog(MainFrame mf) {

        ArrayList<JLabel> labels = new ArrayList<>();
        ArrayList<Component> components = new ArrayList<>();

        this.mf = mf;
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.setLocation(700, 300);
        JPanel contentPanel = new JPanel();
        this.setSize(400, 250);
        this.add(mainPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
        contentPanel.setLayout(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
        mainPanel.add(contentPanel, c);

        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(buttonPanel, c);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                EditInventoryItemDialogCancelClicked();
            }
        });

        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isEditMode)
                {
                    updateEditInventoryItemDialogOkClicked();
                }
                else
                {
                    addEditInventoryItemDialogOkClicked();
                }
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        JLabel mutIdFirst3Label = new JLabel("Mutual ID First 3:");
        createdCodeDropDown = new JComboBox();
        createdCodeDropDown.addItem(UserCreatedCodes.CAN);
        createdCodeDropDown.addItem(UserCreatedCodes.FRI);
        createdCodeDropDown.addItem(UserCreatedCodes.OTC);
        createdCodeDropDown.addItem(UserCreatedCodes.POP);
        JLabel mutIdLabel = new JLabel("Mutual ID Last 3 (###):");
        mutIdField = new JTextField();
        JLabel upcLabel = new JLabel("UPC:");
        upcField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel costLabel = new JLabel("Cost: $");
        costField = new JTextField();
        JLabel priceLabel = new JLabel("Price: $");
        priceLabel.setAlignmentX(SwingConstants.RIGHT);
        priceField = new JTextField();
        JLabel taxableLabel = new JLabel("Taxable:");
        isTaxedButton = new JRadioButton();

        labels.add(mutIdFirst3Label);
        labels.add(mutIdLabel);
        labels.add(upcLabel);
        labels.add(nameLabel);
        labels.add(costLabel);
        labels.add(priceLabel);
        labels.add(taxableLabel);

        components.add(createdCodeDropDown);
        components.add(mutIdField);
        components.add(upcField);
        components.add(nameField);
        components.add(costField);
        components.add(priceField);
        components.add(isTaxedButton);

        isTaxedButton.setText("No");

        c.ipadx = 0;
        c.gridx = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        for (int i = 0; i < labels.size(); i++)
        {
            c.gridy = i;
            c.insets = new Insets(5, 5, 0, 5);
            contentPanel.add(labels.get(i), c);
        }

        c.ipadx = 150;
        c.weightx = 1;
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        for (int i = 0; i < components.size(); i++)
        {
            c.gridy = i;
            contentPanel.add(components.get(i), c);
        }

        isTaxedButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    isTaxedButton.setText("Yes");
                }
                else
                {
                    isTaxedButton.setText("No");
                }
            }
        });

        //Init Dialog
    }

    public void openEditInventoryItemDialog(Item item) {
        editingItem = item;
        setTitle("Edit Inventory Item");
        isEditMode = true;
        String code = item.mutID.substring(0, 3);
        if (code.contentEquals(UserCreatedCodes.CAN.name()))
        {
            createdCodeDropDown.setSelectedItem(UserCreatedCodes.CAN);
        }
        else if (code.contentEquals(UserCreatedCodes.FRI.name()))
        {
            createdCodeDropDown.setSelectedItem(UserCreatedCodes.FRI);
        }
        else if (code.contentEquals(UserCreatedCodes.POP.name()))
        {
            createdCodeDropDown.setSelectedItem(UserCreatedCodes.POP);
        }
        else if (code.contentEquals(UserCreatedCodes.OTC.name()))
        {
            createdCodeDropDown.setSelectedItem(UserCreatedCodes.OTC);
        }
        mutIdField.setText(item.mutID.substring(3, 6));
        upcField.setText(item.itemUPC);
        nameField.setText(item.itemName);
        costField.setText(Double.toString(item.itemCost));
        priceField.setText(Double.toString(item.itemPrice));
        isTaxedButton.setSelected(item.isTaxable);

        setVisible(true);
    }

    public void openAddInventoryItemDialog() {
        setTitle("Add Inventory Item");

        createdCodeDropDown.setSelectedItem(UserCreatedCodes.CAN);

        mutIdField.setText("");
        upcField.setText("");
        nameField.setText("");
        costField.setText("");
        priceField.setText("");
        isTaxedButton.setSelected(true);
        isEditMode = false;
        setVisible(true);

    }

    public void EditInventoryItemDialogCancelClicked() {
        setVisible(false);
    }

    public void addEditInventoryItemDialogOkClicked() {

        String mutId = mutIdField.getText();
        if (mutId == null || mutId.isEmpty() || !mutId.matches("[0-9][0-9][0-9]"))
        {
            mf.showErrorMessage("Mutual ID cannot be blank. Must be 3 number value.");
            return;
        }
        UserCreatedCodes code = ((UserCreatedCodes) createdCodeDropDown.getSelectedItem());
        mutId = code.name() + mutId;

        if (Database.doesItemExistByID(mutId))
        {
            mf.showErrorMessage("Mutual ID already exists.");
            return;
        }

        String upc = upcField.getText();
        if (upc == null || upc.isEmpty())
        {
            mf.showErrorMessage("UPC cannot be blank.");
            return;
        }

        if (Database.doesItemExistByUPC(upc))
        {
            mf.showErrorMessage("UPC already exists.");
            return;
        }

        String name = nameField.getText();

        if (name == null || name.isEmpty())
        {
            mf.showErrorMessage("Name cannot be blank.");
            return;
        }

        if (!mf.validateDouble(costField.getText()))
        {
            mf.showErrorMessage("Cost must be a number.");
            return;
        }

        if (!mf.validateDouble(priceField.getText()))
        {
            mf.showErrorMessage("Price must be a number.");
            return;
        }

        double cost = Double.parseDouble(costField.getText());
        double price = Double.parseDouble(priceField.getText());
        if (cost > price)
        {
            mf.showErrorMessage("Cost must be less than price.");
            return;
        }
        upc = upc.replaceAll("'", "");
        name = name.replaceAll("'", "");
        Object[] message2 =
        {
            "Are you sure?\nName: " + name, "ID: " + mutId, "UPC: " + upc, "Cost: $ " + cost, "Price: $ " + price, "Category: " + code.getValue(), "Is Taxed:  " + (isTaxedButton.isSelected() ? "Yes" : "No")
        };
        JFrame frame = new JFrame();
        int option2 = JOptionPane.showConfirmDialog(frame, message2, "Add Item Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION)
        {
            Database.addItem(mutId, upc, name, price, cost, isTaxedButton.isSelected(), code.getValue());
            setVisible(false);
        }

    }

    public void updateEditInventoryItemDialogOkClicked() {

        String mutId = mutIdField.getText();
        if (mutId == null || mutId.isEmpty() || !mutId.matches("[0-9][0-9][0-9]"))
        {
            mf.showErrorMessage("Mutual ID cannot be blank. Must be 3 number value.");
            return;
        }
        UserCreatedCodes code = ((UserCreatedCodes) createdCodeDropDown.getSelectedItem());
        mutId = code.name() + mutId;

        if (Database.doesItemExistByIDButNotPID(mutId, editingItem.uniqueId))
        {
            mf.showErrorMessage("Mutual ID already exists.");
            return;
        }

        String upc = upcField.getText();
        if (upc == null || upc.isEmpty())
        {
            mf.showErrorMessage("UPC cannot be blank.");
            return;
        }

        if (Database.doesItemExistByUPCButNotPID(upc, editingItem.uniqueId))
        {
            mf.showErrorMessage("UPC already exists.");
            return;
        }

        String name = nameField.getText();

        if (name == null || name.isEmpty())
        {
            mf.showErrorMessage("Name cannot be blank.");
            return;
        }

        if (!mf.validateDouble(costField.getText()))
        {
            mf.showErrorMessage("Cost must be a number.");
            return;
        }

        if (!mf.validateDouble(priceField.getText()))
        {
            mf.showErrorMessage("Price must be a number.");
            return;
        }

        double cost = Double.parseDouble(costField.getText());
        double price = Double.parseDouble(priceField.getText());
        if (cost > price)
        {
            mf.showErrorMessage("Cost must be less than price.");
            return;
        }
        upc = upc.replaceAll("'", "");
        name = name.replaceAll("'", "");
        
                Object[] message2 =
        {
            "Are you sure?\nName: " + name, "ID: " + mutId, "UPC: " + upc, "Cost: $ " + cost, "Price: $ " + price, "Category: " + code.getValue(), "Is Taxed:  " + (isTaxedButton.isSelected() ? "Yes" : "No")
        };
        JFrame frame = new JFrame();
        int option2 = JOptionPane.showConfirmDialog(frame, message2, "Update Item Menu", JOptionPane.OK_CANCEL_OPTION);
        if (option2 == JOptionPane.OK_OPTION)
        {
            Database.updateMutualInventory(mutId, upc, name, price, cost, isTaxedButton.isSelected(), code.getValue());
            setVisible(false);
        }
        
    }

}
