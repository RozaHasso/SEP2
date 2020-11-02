package table.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import domain.model.Item;

public class ItemListCellRenderer extends JPanel implements ListCellRenderer<Object>{

   /**
    * 
    */
   private static final long serialVersionUID = 7796902189335380744L;
   private JLabel label = new JLabel();
   private JLabel price = new JLabel();

   public ItemListCellRenderer() {
      setLayout(new BorderLayout());
      add(label, BorderLayout.CENTER);
      add(price, BorderLayout.EAST);
      price.setHorizontalAlignment(SwingConstants.RIGHT);
      setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
   }

   @Override
   public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      String html = "<html>";
      if (value instanceof Item) {
         Item item = (Item) value;
         
         int indentSize = 6;
         String indent = "";
         for (int i = 0; i < indentSize; i++)
            indent += "&nbsp;";

         html += indent;
         html += "<font color=#003545 size=+2>" + item.getName() + "</font>";
         html += "<br>"+indent;
         html += "<font color=#00454A>" + item.getIngredients() + "</font>";
         html += "<br>"+indent;
         html += "<font color=#ED6363>" + item.getAllergens() + "</font>";

         price.setText("<html>" + "<font color=#3C6562 size=+2>" + String.format("%.2f", item.getPrice()) + "&nbsp;</font>" + "</html>");
      }
      else {
         String category = (String) value;
         html += "<font color=#203541 size=+3>" + category + ":</font>";
         price.setText("");
      }

      html += "</html>";
      label.setText(html);

      if (isSelected) {
         setBackground(list.getSelectionBackground());
      }
      else {
         setBackground(list.getBackground());
      }
      return this;
   }
}
