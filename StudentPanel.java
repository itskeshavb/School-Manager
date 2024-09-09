import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.sql.*;
public class StudentPanel extends JPanel {
    String createTable =
            "CREATE TABLE IF NOT EXISTS student " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " first_name TEXT, " +
                    " last_name TEXT, " +
                    " section TEXT, "    +
                    " PRIMARY KEY (id))";
    static final String URL = "jdbc:mysql://localhost:3306/p2";
    static final String USER = "root";
    static final String PASS = "password";

    static Connection connection;
    static Statement statement;

    public StudentPanel() {
        statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }

        setLayout(null);
        setPreferredSize(new Dimension(900, 600));

        DefaultTableModel dtm = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtm.addColumn("Section ID");
        dtm.addColumn("Course");
        JTable sectionsTaught = new JTable(dtm);


        JScrollPane sp = new JScrollPane(sectionsTaught);
        sp.setVisible(false);
        sp.setBounds(700, 0, 200, 400);
        add(sp);


        DefaultTableModel model = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Last Name");
        model.addColumn("First Name");
        JTable table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    sp.setVisible(true);
                }
            }
        });
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 600, 400);
        add(pane);

        String s5 = "SELECT * FROM student WHERE id >=1;";
        try{
            ResultSet rs = statement.executeQuery(s5);
            while(rs != null && rs.next())
            {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("last_name"), rs.getString("first_name")});
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        JLabel label1 = new JLabel("First Name");
        label1.setFont(new Font("Arial", Font.PLAIN, 24));
        label1.setBounds(20, 420, 160, 40);
        //label1.setVisible(false);
        add(label1);
        label1.setVisible(false);

        JLabel label2 = new JLabel("Last Name");
        label2.setFont(new Font("Arial", Font.PLAIN, 24));
        label2.setBounds(20, 480, 160, 40);
        //label2.setVisible(false);
        add(label2);
        label2.setVisible(false);

        JTextField firstName = new JTextField();
        firstName.setFont(new Font("Arial", Font.PLAIN, 24));
        firstName.setBounds(180, 420, 280, 40);
        //firstName.setVisible(false);
        add(firstName);
        firstName.setVisible(false);

        JTextField lastName = new JTextField();
        lastName.setFont(new Font("Arial", Font.PLAIN, 24));
        lastName.setBounds(180, 480, 280, 40);
        //lastName.setVisible(false);
        add(lastName);
        lastName.setVisible(false);


        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 24));
        addButton.setBounds(480, 420, 160, 40);
        addButton.setVisible(false);
        //addButton.setVisible(false);

        JButton remove = new JButton("Remove student");
        remove.setFont(new Font("Arial", Font.BOLD, 14));
        remove.setBounds(700, 500, 180, 40);
        add(remove);
        remove.setVisible(false);

        JButton confirmEdit = new JButton("Confirm Edit");
        confirmEdit.setFont(new Font("Arial", Font.BOLD, 14));
        confirmEdit.setBounds(700, 420, 180, 40);
        add(confirmEdit);
        confirmEdit.setVisible(false);

        JButton cancelEdit = new JButton("Cancel Edit");
        cancelEdit.setFont(new Font("Arial", Font.BOLD, 14));
        cancelEdit.setBounds(700, 500, 180, 40);
        add(cancelEdit);
        cancelEdit.setVisible(false);

        JButton edit = new JButton("Edit Student");
        edit.setFont(new Font("Arial", Font.BOLD, 14));
        edit.setBounds(480, 500, 180, 40);
        add(edit);
        edit.setVisible(false);



        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    edit.setVisible(true);
                    remove.setVisible(true);
                    sp.setVisible(true);
                    String schedule = "";
                    try {
                        ResultSet rs = statement.executeQuery("SELECT * FROM student WHERE id=" +table.getValueAt(table.getSelectedRow(),0));
                        while(rs!=null&&rs.next())
                            schedule = rs.getString("section");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    StringTokenizer st = new StringTokenizer(schedule);
                    dtm.getDataVector().removeAllElements();
                    while(st.hasMoreTokens())
                    {
                        String[] temp = st.nextToken().split(",");
                        dtm.addRow(new Object[]{temp[0], temp[1]});
                    }
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 24));
        cancelButton.setBounds(480, 480, 160, 40);
        //cancelButton.setVisible(false);
        add(cancelButton);
        cancelButton.setVisible(false);

        add(addButton);
        JButton addEntryButton = new JButton("Add new student");
        addEntryButton.setFont(new Font("Arial", Font.BOLD, 14));
        addEntryButton.setBounds(700, 420, 180, 40);
        add(addEntryButton);
        addEntryButton.setVisible(true);
        addEntryButton.addActionListener(e -> {
            sp.setVisible(false);
            table.clearSelection();
            edit.setVisible(false);
            remove.setVisible(false);
            firstName.setVisible(true);
            lastName.setVisible(true);
            label1.setVisible(true);
            label2.setVisible(true);
            addButton.setVisible(true);
            cancelButton.setVisible(true);
            addEntryButton.setVisible(false);
        });

        addButton.addActionListener(e -> {
            String first = firstName.getText();
            String last = lastName.getText();
            String schedule = "";
            String s1 = "INSERT INTO student (first_name, last_name, section) VALUES ('" + first + "', '" + last + "','"+ schedule + "')";
            try {
                String s2 = "SELECT * FROM student WHERE id = (SELECT max(id) FROM student);";
                statement.executeUpdate(s1);
                ResultSet rs = statement.executeQuery(s2);
                while(rs != null && rs.next())
                {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("last_name"), rs.getString("first_name")});
                }
                model.fireTableDataChanged();
                firstName.setText("");
                lastName.setText("");
                firstName.setVisible(false);
                label1.setVisible(false);
                label2.setVisible(false);
                lastName.setVisible(false);
                addButton.setVisible(false);
                cancelButton.setVisible(false);
                addEntryButton.setVisible(true);

            } catch (Exception ex) {}
        });
        cancelButton.addActionListener(e -> {
            firstName.setText("");
            lastName.setText("");
            firstName.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            lastName.setVisible(false);
            addButton.setVisible(false);
            cancelButton.setVisible(false);
            addEntryButton.setVisible(true);
        });

        edit.addActionListener(e -> {
            edit.setVisible(false);
            confirmEdit.setVisible(true);
            cancelEdit.setVisible(true);
            remove.setVisible(false);
            addEntryButton.setVisible(false);
            label1.setVisible(true);
            label2.setVisible(true);
            firstName.setVisible(true);
            lastName.setVisible(true);
            firstName.setText((String) table.getValueAt(table.getSelectedRow(), 2));
            lastName.setText((String) table.getValueAt(table.getSelectedRow(), 1));
        });

        cancelEdit.addActionListener(e -> {
            addEntryButton.setVisible(true);
            cancelEdit.setVisible(false);
            confirmEdit.setVisible(false);
            table.clearSelection();
            edit.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            firstName.setText("");
            lastName.setText("");
            firstName.setVisible(false);
            sp.setVisible(false);
            lastName.setVisible(false);
        });

        confirmEdit.addActionListener(e -> {
            if(!firstName.getText().equals("") && !lastName.getText().equals(""))
            {
                table.setValueAt(firstName.getText(), table.getSelectedRow(), 2);
                table.setValueAt(lastName.getText(), table.getSelectedRow(),1);
                try {
                    statement.execute("UPDATE student SET first_name = '" + firstName.getText()  + "' WHERE id=" + table.getValueAt(table.getSelectedRow(),0));
                    statement.execute("UPDATE student SET last_name = '" + lastName.getText()  + "' WHERE id=" + table.getValueAt(table.getSelectedRow(),0));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            confirmEdit.setVisible(false);
            cancelEdit.setVisible(false);
            table.clearSelection();
            label1.setVisible(false);
            label2.setVisible(false);
            firstName.setText("");
            lastName.setText("");
            lastName.setVisible(false);
            firstName.setVisible(false);
            sp.setVisible(false);
            addEntryButton.setVisible(true);
        });

        remove.addActionListener(e -> {

            try {
                statement.execute("DELETE FROM student WHERE id=" + table.getValueAt(table.getSelectedRow(), 0));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            model.removeRow(table.getSelectedRow());
            remove.setVisible(false);
            sp.setVisible(false);
            edit.setVisible(false);
        });


    }
}