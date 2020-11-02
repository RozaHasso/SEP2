package table.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domain.model.Item;
import domain.model.ItemList;
import table.TableController;
import table.TableSingleton;

public class TableGUI implements TableView, Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = 240511784205600903L;

   private TableController controller;

   private JFrame frame;

   private DefaultListModel<Object> menuListModel;
   private DefaultListModel<Object> orderListModel;
   private JList<Object> menuList;

   private JButton addButton;
   private JButton removeButton;
   private JButton finishButton;
   
   private JPanel customerNotePanel;
   private JTextArea noteTextArea;
   private JPanel mainPanel;

   private Item selectedMenuItem;
   private int selectedOrderItem;

   private JList<Object> orderList;

   private ButtonListener buttonListener;
   
   private JLabel totalLabel;
   
   private int tableNumber;
   private double orderTotal = 0;

   private String[] categories;
   private ItemList menu;
  
   
   public TableGUI() {
      
   }

    /**
    * Initialize the contents of the frame.
    */
   private void initialize() {

      ItemListCellRenderer listRenderer = new ItemListCellRenderer();
      buttonListener = new ButtonListener();
      Font big = new Font("Tahoma", Font.PLAIN, 20);
      
      tableNumber = TableSingleton.getInstance().getTableNo();
      frame = new JFrame("Table " + tableNumber);
      frame.setVisible(true);
      frame.setBounds(50, 50, 1050, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      menuListModel = new DefaultListModel<>();

      orderListModel = new DefaultListModel<>();
      frame.getContentPane().setLayout(new BorderLayout(0, 0));

      mainPanel = new JPanel();
      frame.getContentPane().add(mainPanel);
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

      JPanel menuPanel = new JPanel();
      mainPanel.add(menuPanel);
      menuPanel.setBorder(new TitledBorder(null, "Menu", TitledBorder.CENTER, TitledBorder.TOP, big, null));
      menuPanel.setLayout(new BorderLayout(0, 0));

      JScrollPane menuScrollPane = new JScrollPane();
      menuScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
      menuPanel.add(menuScrollPane);
      menuList = new JList<Object>(menuListModel);
      menuScrollPane.setViewportView(menuList);

      // Don't allow to select category
      menuList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (menuList.getSelectedValue() instanceof String) {
               int previousIndex = menuList.getSelectedIndex();
               menuList.setSelectedIndex(++previousIndex);
            }
            else
               selectedMenuItem = (Item) menuList.getSelectedValue();

            if (menuList.getSelectedIndex() != -1)
               addButton.setEnabled(true);
            else
               addButton.setEnabled(false);
         }
      });

      menuList.setCellRenderer(listRenderer);

      JPanel middlePanel = new JPanel();
      mainPanel.add(middlePanel);
      middlePanel.setBorder(new EmptyBorder(20, 20, 5, 20));
      middlePanel.setLayout(new GridLayout(4, 1, 0, 50));

      totalLabel = new JLabel("<html><center><font size=+1>Order total:</font><br><font size=+2 color=#00454A>0.00</font></center></html>");
      totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
      middlePanel.add(totalLabel);

      addButton = new JButton("Add");
      addButton.setFont(big);
      addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      middlePanel.add(addButton);
      addButton.addActionListener(buttonListener);
      addButton.setEnabled(false);

      removeButton = new JButton("Remove");
      removeButton.setFont(big);
      removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      middlePanel.add(removeButton);
      removeButton.addActionListener(buttonListener);
      removeButton.setEnabled(false);

      finishButton = new JButton("Finish");
      finishButton.setFont(big);
      middlePanel.add(finishButton);
      finishButton.setEnabled(false);

      JPanel orderPanel = new JPanel();
      mainPanel.add(orderPanel);
      orderPanel.setBorder(new TitledBorder(null, "Your order", TitledBorder.CENTER, TitledBorder.TOP, big, null));
      orderPanel.setLayout(new BorderLayout(0, 0));

      JScrollPane orderScrollPane = new JScrollPane();
      orderScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 0));
      orderPanel.add(orderScrollPane);

      orderList = new JList<Object>();
      orderScrollPane.setViewportView(orderList);
      orderList.setModel(orderListModel);
      orderList.setCellRenderer(listRenderer);

      customerNotePanel = new JPanel();
      customerNotePanel.setBorder(new TitledBorder(null, "Notes", TitledBorder.CENTER, TitledBorder.TOP, big, null));
      orderPanel.add(customerNotePanel, BorderLayout.SOUTH);
      customerNotePanel.setLayout(new BorderLayout(0, 0));

      noteTextArea = new JTextArea(3, 0);
      noteTextArea.setFont(big);
      noteTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

      customerNotePanel.add(noteTextArea);
      orderList.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            selectedOrderItem = orderList.getSelectedIndex();

            if (orderList.getSelectedIndex() != -1)
               removeButton.setEnabled(true);
            else
               removeButton.setEnabled(false);
         }
      });
      finishButton.addActionListener(buttonListener);
   }

   private class ButtonListener implements ActionListener, Serializable {
      /**
       * 
       */
      private static final long serialVersionUID = -3272414871224930485L;

      @Override
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == addButton && selectedMenuItem != null) {
            orderListModel.addElement(selectedMenuItem);
            controller.addItem(selectedMenuItem);
            finishButton.setEnabled(true);
            orderList.clearSelection();
            menuList.clearSelection();
            updateTotal();
         }
         if (e.getSource() == removeButton && selectedOrderItem != -1) {
            int tempIndex = selectedOrderItem;
            orderListModel.removeElementAt(selectedOrderItem);
            controller.removeItem(tempIndex);
            orderList.clearSelection();
            updateTotal();
            if (orderListModel.isEmpty())
               finishButton.setEnabled(false);
         }
         if (e.getSource() == finishButton && !orderListModel.isEmpty()) {
            controller.sendOrder(noteTextArea.getText());
            finishButton.setEnabled(false);
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
            menuList.setEnabled(false);
            noteTextArea.setEnabled(false);
            JOptionPane.showMessageDialog(frame, "<html><font size=+1>Your order has been sent to the kitchen<br>You will get notified...</font></html>", "Please wait",
                  JOptionPane.INFORMATION_MESSAGE);
         }

      }
   }

   @Override
   public void loadMenu() {
      menuListModel.removeAllElements();
      for (String category : categories) {
         menuListModel.addElement(category);
         for (Item item : menu.getItemsByCategory(category)) {
            if (item != null)
               menuListModel.addElement(item);
         }
      }
   }

   @Override
   public void finishOrder(String cancelNote) {
      orderListModel.removeAllElements();
      noteTextArea.setText("");
      addButton.setEnabled(true);
      menuList.setEnabled(true);
      noteTextArea.setEnabled(true);
      Thread t = new Thread(new Runnable() {
         public void run() {
            if (cancelNote == null)
               JOptionPane.showMessageDialog(frame, "<html><font size=+1>Your order is finished!<br>\\O/&nbsp;\\o/&nbsp;\\O/</font></html>", "Enjoy", JOptionPane.INFORMATION_MESSAGE);
            else
               JOptionPane.showMessageDialog(frame, "<html><font size=+1>Your order has been cancelled :(<br>Reason:" + cancelNote + "</font></html>", "Sorry", JOptionPane.ERROR_MESSAGE);
         }
      });
      t.start();
      loadMenu();
   }

   @Override
   public void updateTotal() {
      orderTotal = 0;
      for (int i = 0; i < orderListModel.size(); i++) {
         Item item = (Item) orderListModel.getElementAt(i);
         orderTotal += item.getPrice();
      }
      totalLabel.setText("<html><center><font size=+1>Order total:</font><br><font size=+2 color=#00454A>" + String.format("%.2f", orderTotal) + "</font></center></html>");
   }

   @Override
   public void start(TableController controller) {
      this.controller = controller;
      initialize();
      this.tableNumber = TableSingleton.getInstance().getTableNo();
      this.categories = controller.refreshCategories();
      this.menu = controller.refreshMenu();
      loadMenu();
   }

   @Override
   public void printError(String e) {
      System.out.println(e);
   }
}
