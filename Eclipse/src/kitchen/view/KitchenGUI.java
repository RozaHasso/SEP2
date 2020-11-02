package kitchen.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domain.model.Item;
import domain.model.ItemList;
import domain.model.Order;
import domain.model.OrderList;
import kitchen.KitchenController;
import javax.swing.JTextArea;
import javax.swing.JComboBox;

public class KitchenGUI implements KitchenView {

   private KitchenController controller;

   private Item selectedItem;

   private OrderList activeOrders;
   private OrderList ordersHistory;
   private ItemList allItems;

   private JFrame frame;
   private JTable ordersTable;
   private JTable historyTable;
   private JTable itemsTable;
   private JTextField nameTextField;
   private JTextField ingredientsTextField;
   private JTextField allergensTextField;
   private JTextField priceTextField;
   private JButton updateButton, addButton, removeButton;
   private ButtonListener buttonListener;
   private ItemTableModel itemTableModel;
   private OrderTableModel historyTableModel;
   private OrderTableModel ordersTableModel;
   private JButton btnFinish;
   private JButton btnCancel;
   private JTextArea cancelNoteTextArea;
   private JLabel cancelNoteLabel;
   private JList<Item> historyItemsList;
   private JLabel customerNoteLabel;
   private JComboBox<String> categoryComboBox;
   
   public KitchenGUI() {
      
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {

      frame = new JFrame("Kitchen");
      frame.setBounds(100, 100, 1150, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.setVisible(true);

      JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

      tabbedPane.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent arg0) {
            if (tabbedPane.getSelectedIndex() == 1) {
               refreshOrderHistory();
               cancelNoteLabel.setText("");
               customerNoteLabel.setText("");
               DefaultListModel<Item> listModel = (DefaultListModel<Item>) historyItemsList.getModel();
               listModel.removeAllElements();
            }
         }
      });

      frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

      JPanel ordersTab = new JPanel();
      tabbedPane.addTab("Orders", null, ordersTab, "Active orders");
      ordersTab.setLayout(new BorderLayout(0, 0));

      JScrollPane ordersScrollPane = new JScrollPane();
      ordersTab.add(ordersScrollPane, BorderLayout.CENTER);

      JList<Item> orderItemsList = new JList<Item>(new DefaultListModel<Item>());

      JLabel noteLabel = new JLabel();

      ordersTableModel = new OrderTableModel(activeOrders);
      ordersTable = new JTable(ordersTableModel);
      ordersTable.setSurrendersFocusOnKeystroke(true);
      ordersScrollPane.setViewportView(ordersTable);
      ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      ordersTable.getTableHeader().setReorderingAllowed(false);
      ordersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
               int index = ordersTable.convertRowIndexToModel(ordersTable.getSelectedRow());
               if (index == -1) {
                  cancelNoteTextArea.setText("");
                  cancelNoteTextArea.setEnabled(false);
                  btnFinish.setEnabled(false);
                  btnCancel.setEnabled(false);
               }
               else {
                  DefaultListModel<Item> listModel = (DefaultListModel<Item>) orderItemsList.getModel();
                  listModel.removeAllElements();

                  for (Item i : activeOrders.get(index).getItems())
                     listModel.addElement(i);

                  noteLabel.setText(activeOrders.get(index).getCustomerNote());

                  cancelNoteTextArea.setEnabled(true);
                  btnFinish.setEnabled(true);
                  btnCancel.setEnabled(true);
               }
            }
         }
      });

      JPanel ordersInfoPanel = new JPanel();
      ordersTab.add(ordersInfoPanel, BorderLayout.EAST);
      ordersInfoPanel.setLayout(new BoxLayout(ordersInfoPanel, BoxLayout.PAGE_AXIS));

      JPanel orderControlPanel = new JPanel();
      ordersInfoPanel.add(orderControlPanel);
      orderControlPanel.setLayout(new BorderLayout(0, 0));

      JPanel cancelNoteFieldPanel = new JPanel();
      orderControlPanel.add(cancelNoteFieldPanel, BorderLayout.CENTER);
      cancelNoteFieldPanel.setLayout(new BorderLayout(0, 0));
      orderControlPanel.setBorder(null);

      cancelNoteTextArea = new JTextArea();
      cancelNoteTextArea.setBorder(new LineBorder(Color.GRAY));
      cancelNoteTextArea.setEnabled(false);
      cancelNoteFieldPanel.add(cancelNoteTextArea);
      cancelNoteFieldPanel.setBorder(new TitledBorder(null, "Cancel note", TitledBorder.LEADING, TitledBorder.TOP, null, null));

      JPanel buttonsPanel = new JPanel();
      orderControlPanel.add(buttonsPanel, BorderLayout.SOUTH);

      buttonListener = new ButtonListener();

      btnFinish = new JButton("Finish");
      buttonsPanel.add(btnFinish);

      btnCancel = new JButton("Cancel");
      buttonsPanel.add(btnCancel);
      btnCancel.addActionListener(buttonListener);
      btnFinish.addActionListener(buttonListener);
      btnCancel.setEnabled(false);
      btnFinish.setEnabled(false);

      JPanel orderItemsPanel = new JPanel();
      ordersInfoPanel.add(orderItemsPanel);
      orderItemsPanel.setBorder(new TitledBorder("Items"));
      orderItemsPanel.setLayout(new BorderLayout(0, 0));

      JScrollPane orderItemsScrollPane = new JScrollPane(orderItemsList);
      orderItemsPanel.add(orderItemsScrollPane);

      JPanel notePanel = new JPanel();
      notePanel.setBorder(new TitledBorder(null, "Customer note", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      ordersInfoPanel.add(notePanel);

      notePanel.add(noteLabel);

      JPanel historyPanel = new JPanel();
      tabbedPane.addTab("History", null, historyPanel, null);
      historyPanel.setLayout(new BorderLayout(0, 0));

      JPanel historyInfoPanel = new JPanel();
      historyPanel.add(historyInfoPanel, BorderLayout.EAST);
      historyInfoPanel.setLayout(new BoxLayout(historyInfoPanel, BoxLayout.PAGE_AXIS));

      JPanel cancelNotePanel = new JPanel();
      historyInfoPanel.add(cancelNotePanel);
      cancelNotePanel.setBorder(new TitledBorder(null, "Cancel note", TitledBorder.LEADING, TitledBorder.TOP, null, null));

      cancelNoteLabel = new JLabel();
      cancelNotePanel.add(cancelNoteLabel);

      JPanel historyItemsPanel = new JPanel();
      historyItemsPanel.setBorder(new TitledBorder(null, "Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      historyInfoPanel.add(historyItemsPanel);
      historyItemsPanel.setLayout(new BorderLayout(0, 0));

      historyItemsList = new JList<Item>(new DefaultListModel<Item>());

      JScrollPane historyItemsScrollPane = new JScrollPane(historyItemsList);

      historyItemsPanel.add(historyItemsScrollPane);

      JPanel customerNotePanel = new JPanel();
      customerNotePanel.setBorder(new TitledBorder(null, "Customer note", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      historyInfoPanel.add(customerNotePanel);

      customerNoteLabel = new JLabel();
      customerNotePanel.add(customerNoteLabel);

      JScrollPane historyScrollPane = new JScrollPane();
      historyPanel.add(historyScrollPane, BorderLayout.CENTER);

      historyTableModel = new OrderTableModel(ordersHistory);
      historyTable = new JTable(historyTableModel);
      historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      historyTable.getTableHeader().setReorderingAllowed(false);
      historyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
               int index = historyTable.convertRowIndexToModel(historyTable.getSelectedRow());
               if (index != -1) {
                  DefaultListModel<Item> listModel = (DefaultListModel<Item>) historyItemsList.getModel();
                  listModel.removeAllElements();
                  for (Item i : ordersHistory.get(index).getItems())
                     listModel.addElement(i);

                  customerNoteLabel.setText(ordersHistory.get(index).getCustomerNote());
                  cancelNoteLabel.setText(ordersHistory.get(index).getCancelNote());
               }
            }
         }
      });

      historyScrollPane.setViewportView(historyTable);

      JPanel itemsTab = new JPanel();
      tabbedPane.addTab("Items", null, itemsTab, null);
      itemsTab.setLayout(new BorderLayout(0, 0));

      itemTableModel = new ItemTableModel(allItems);
      itemsTable = new JTable(itemTableModel);
      itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      itemsTable.getTableHeader().setReorderingAllowed(false);
      itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            int index = itemsTable.convertRowIndexToModel(itemsTable.getSelectedRow());
            if (index != -1) {
               updateButton.setEnabled(true);
               removeButton.setEnabled(true);
               selectedItem = allItems.get(index);
               nameTextField.setText(selectedItem.getName());
               ingredientsTextField.setText(selectedItem.getIngredients());
               allergensTextField.setText(selectedItem.getAllergens());
               categoryComboBox.setSelectedItem(selectedItem.getCategory());
               priceTextField.setText(Double.toString(selectedItem.getPrice()));
            }
            else {
               updateButton.setEnabled(false);
               removeButton.setEnabled(false);
            }
         }
      });

      JScrollPane itemScrollPane = new JScrollPane(itemsTable);
      itemsTab.add(itemScrollPane);

      JPanel itemEditPanel = new JPanel();
      itemEditPanel.setBorder(new TitledBorder(null, "Edit menu items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
      itemsTab.add(itemEditPanel, BorderLayout.NORTH);

      JLabel nameLabel = new JLabel("Name:");
      itemEditPanel.add(nameLabel);

      nameTextField = new JTextField();
      itemEditPanel.add(nameTextField);
      nameTextField.setColumns(10);

      JLabel ingredientsLabel = new JLabel("Ingredients:");
      itemEditPanel.add(ingredientsLabel);

      ingredientsTextField = new JTextField();
      itemEditPanel.add(ingredientsTextField);
      ingredientsTextField.setColumns(10);

      JLabel allergensLabel = new JLabel("Allergens:");
      itemEditPanel.add(allergensLabel);

      allergensTextField = new JTextField();
      itemEditPanel.add(allergensTextField);
      allergensTextField.setColumns(10);

      JLabel categoryLabel = new JLabel("Category:");
      itemEditPanel.add(categoryLabel);

      itemEditPanel.add(categoryComboBox);

      JLabel priceLabel = new JLabel("Price:");
      itemEditPanel.add(priceLabel);

      priceTextField = new JTextField();
      itemEditPanel.add(priceTextField);
      priceTextField.setColumns(10);

      updateButton = new JButton("Update");
      itemEditPanel.add(updateButton);
      updateButton.addActionListener(buttonListener);
      updateButton.setEnabled(false);

      addButton = new JButton("Add");
      itemEditPanel.add(addButton);
      addButton.addActionListener(buttonListener);

      removeButton = new JButton("Remove");
      itemEditPanel.add(removeButton);
      removeButton.addActionListener(buttonListener);
      removeButton.setEnabled(false);

   }

   private class ButtonListener implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == addButton) {
            Item item = new Item(nameTextField.getText(), ingredientsTextField.getText(), allergensTextField.getText(), (String) categoryComboBox.getSelectedItem(),
                  Double.parseDouble(priceTextField.getText()));
            controller.addItem(item);
            refreshMenu();
         }

         if (e.getSource() == updateButton) {
            Item newItem = new Item(selectedItem.getId(), nameTextField.getText(), ingredientsTextField.getText(), allergensTextField.getText(), (String) categoryComboBox.getSelectedItem(),
                  Double.parseDouble(priceTextField.getText()));
            controller.editItem(selectedItem, newItem);
            refreshMenu();
         }

         if (e.getSource() == removeButton) {
            controller.removeItem(selectedItem);
            refreshMenu();
         }

         if (e.getSource() == btnFinish) {
            if (ordersTable.getSelectedRow() != -1) {
               Order selectedOrder = activeOrders.get(ordersTable.convertRowIndexToModel(ordersTable.getSelectedRow()));
               selectedOrder.setStatus("finished");
               controller.finishOrder(selectedOrder);
               ordersHistory.add(selectedOrder);
               refreshPendingOrders();
            }
         }

         if (e.getSource() == btnCancel) {
            if (ordersTable.getSelectedRow() != -1) {
               if (cancelNoteTextArea.getText().equals("")) {
                  JOptionPane.showMessageDialog(frame, "Please enter cancel note!", "Cancel note missing", JOptionPane.WARNING_MESSAGE);
               }
               else {
                  Order selectedOrder = activeOrders.get(ordersTable.convertRowIndexToModel(ordersTable.getSelectedRow()));
                  selectedOrder.setCancelNote(cancelNoteTextArea.getText());
                  selectedOrder.setStatus("cancelled");
                  ordersHistory.add(selectedOrder);
                  controller.cancelOrder(selectedOrder);
                  refreshPendingOrders();
               }
            }
         }

      }

   }

   @Override
   public void refreshMenu() {
      itemTableModel.fireTableDataChanged();
   }

   @Override
   public void refreshOrderHistory() {
      historyTableModel.fireTableDataChanged();
      historyTable.repaint();

   }

   @Override
   public void refreshPendingOrders() {
      ordersTableModel.fireTableDataChanged();
      ordersTable.repaint();
   }

   @Override
   public void start(KitchenController controller) throws SQLException {
      this.controller = controller;
      this.allItems = controller.getAllItems();
      this.activeOrders = controller.getActiveOrders();
      this.ordersHistory = controller.getOrderHistory();
      this.categoryComboBox = new JComboBox<String>(controller.getCategories());
      initialize();
   }

   @Override
   public void printError(String e) {
      System.out.println(e);
   }
}
