import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.sql.*;
public class CoursePanel extends JPanel {
    String createTable =
            "CREATE TABLE IF NOT EXISTS course " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " course_name TEXT not NULL, " +
                    " type TEXT not NULL, " +
                    " PRIMARY KEY (id))";
    static final String URL = "jdbc:mysql://localhost:3306/p2";
    static final String USER = "root";
    static final String PASS = "password";

    static Connection connection;
    static Statement statement;
    static Statement statement2;


    public CoursePanel() {
        statement = null;
        statement2 = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
            statement2 = connection.createStatement();

        } catch (Exception e) {
            System.out.println(e);
        }

        setLayout(null);
        setPreferredSize(new Dimension(900, 600));

        DefaultTableModel model = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Course Name");
        model.addColumn("Type");
        JTable table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 600, 400);
        add(pane);

        String s5 = "SELECT * FROM course WHERE id >=1;";
        try{
            ResultSet rs = statement.executeQuery(s5);
            while(rs != null && rs.next())
            {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("course_name"), rs.getString("type")});
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        JLabel label1 = new JLabel("Course Name");
        label1.setFont(new Font("Arial", Font.PLAIN, 24));
        label1.setBounds(20, 420, 160, 40);
        //label1.setVisible(false);
        add(label1);
        label1.setVisible(false);

        JLabel label2 = new JLabel("Type");

        label2.setFont(new Font("Arial", Font.PLAIN, 24));
        label2.setBounds(20, 480, 160, 40);
        //label2.setVisible(false);
        add(label2);
        label2.setVisible(false);

        JTextField courseName = new JTextField();
        courseName.setFont(new Font("Arial", Font.PLAIN, 24));
        courseName.setBounds(180, 420, 280, 40);
        //firstName.setVisible(false);
        add(courseName);
        courseName.setVisible(false);

        ButtonGroup group = new ButtonGroup();

        JRadioButton academic = new JRadioButton("Academic");
        academic.setFont(new Font("Arial", Font.PLAIN, 15));
        academic.setBounds(100, 475, 100, 50);
        academic.setVisible(false);
        add(academic);
        group.add(academic);

        JRadioButton KAP = new JRadioButton("KAP");
        KAP.setFont(new Font("Arial", Font.PLAIN, 15));
        KAP.setBounds(200, 475, 75, 50);
        add(KAP);
        KAP.setVisible(false);
        group.add(KAP);

        JRadioButton AP = new JRadioButton("AP");
        AP.setFont(new Font("Arial", Font.PLAIN, 15));
        AP.setBounds(275, 475, 100, 50);
        add(AP);
        AP.setVisible(false);
        group.add(AP);

        JButton remove = new JButton("Remove Course");
        remove.setFont(new Font("Arial", Font.BOLD, 14));
        remove.setBounds(700, 500, 180, 40);
        add(remove);
        remove.setVisible(false);


        JButton edit = new JButton("Edit Course");
        edit.setFont(new Font("Arial", Font.BOLD, 14));
        edit.setBounds(480, 500, 180, 40);
        add(edit);
        edit.setVisible(false);

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


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    edit.setVisible(true);
                    remove.setVisible(true);

                }
            }
        });



        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 24));
        addButton.setBounds(480, 420, 160, 40);
        addButton.setVisible(false);
        //addButton.setVisible(false);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 24));
        cancelButton.setBounds(480, 480, 160, 40);
        //cancelButton.setVisible(false);
        add(cancelButton);
        cancelButton.setVisible(false);

        add(addButton);
        JButton addEntryButton = new JButton("Add new Course");
        addEntryButton.setFont(new Font("Arial", Font.BOLD, 14));
        addEntryButton.setBounds(700, 420, 180, 40);
        add(addEntryButton);
        addEntryButton.setVisible(true);
        addEntryButton.addActionListener(e -> {
            table.clearSelection();
            edit.setVisible(false);
            remove.setVisible(false);
            courseName.setVisible(true);
            label1.setVisible(true);
            label2.setVisible(true);
            addButton.setVisible(true);
            cancelButton.setVisible(true);
            addEntryButton.setVisible(false);
            academic.setVisible(true);
            KAP.setVisible(true);
            AP.setVisible(true);
        });

        addButton.addActionListener(e -> {

            String first = courseName.getText();
            String last = "";
            if(academic.isSelected())
                last = academic.getText();
            else if(KAP.isSelected())
                last = KAP.getText();
            else
                last = AP.getText();
            if(!first.equals("") && !last.equals(""))
            {
                String s1 = "INSERT INTO course (course_name, type) VALUES ('" + first + "', '" + last + "');";
                try {
                    String s2 = "SELECT * FROM course WHERE id = (SELECT max(id) FROM course);";
                    statement.executeUpdate(s1);
                    ResultSet rs = statement.executeQuery(s2);
                    while(rs != null && rs.next())
                        model.addRow(new Object[]{rs.getInt("id"), rs.getString("course_name"), rs.getString("type")});
                    model.fireTableDataChanged();
                    courseName.setText("");
                    group.clearSelection();
                    courseName.setVisible(false);
                    label1.setVisible(false);
                    label2.setVisible(false);
                    academic.setVisible(false);
                    KAP.setVisible(false);
                    AP.setVisible(false);
                    addButton.setVisible(false);
                    cancelButton.setVisible(false);
                    addEntryButton.setVisible(true);

                } catch (Exception ex) {}
            }

        });
        cancelButton.addActionListener(e -> {
            courseName.setText("");
            group.clearSelection();
            courseName.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            academic.setVisible(false);
            KAP.setVisible(false);
            AP.setVisible(false);
            addButton.setVisible(false);
            cancelButton.setVisible(false);
            addEntryButton.setVisible(true);
        });

        edit.addActionListener(e -> {
            addEntryButton.setVisible(false);
            confirmEdit.setVisible(true);
            cancelEdit.setVisible(true);
            remove.setVisible(false);
            addEntryButton.setVisible(false);
            label1.setVisible(true);
            label2.setVisible(true);
            academic.setVisible(true);
            KAP.setVisible(true);
            AP.setVisible(true);
            courseName.setVisible(true);
            courseName.setText((String) table.getValueAt(table.getSelectedRow(),1));
            String type = (String) table.getValueAt(table.getSelectedRow(),2);
            if(type.equals(academic.getText()))
                academic.setSelected(true);
            else if(type.equals(KAP.getText()))
                KAP.setSelected(true);
            else
                AP.setSelected(true);
            edit.setVisible(false);
        });
        confirmEdit.addActionListener(e -> {
            if(!courseName.getText().equals(""))
            {
                editSchedule((String) table.getValueAt(table.getSelectedRow(), 1), courseName.getText());
                table.setValueAt(courseName.getText(), table.getSelectedRow(), 1);
                String type;
                if(academic.isSelected())
                    type = academic.getText();
                else if(KAP.isSelected())
                    type  = KAP.getText();
                else
                    type = AP.getText();

                table.setValueAt(type, table.getSelectedRow(),2);

                try {
                    ResultSet rs = statement.executeQuery("SELECT * FROM section WHERE id >=1;");
                    while(rs != null  && rs.next())
                    {
                        int courseID = rs.getInt("course_id");
                        if(courseID == (Integer) table.getValueAt(table.getSelectedRow(),0))
                        {
                            statement.execute("UPDATE section SET course_name = '" + courseName.getText()  + "' WHERE id=" + courseID);
                            break;
                        }
                    }
                    statement.execute("UPDATE course SET course_name = '" + courseName.getText()  + "' WHERE id=" + table.getValueAt(table.getSelectedRow(),0));
                    statement.execute("UPDATE course SET type = '" + type  + "' WHERE id=" + table.getValueAt(table.getSelectedRow(),0));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                confirmEdit.setVisible(false);
                cancelEdit.setVisible(false);
                table.clearSelection();
                label1.setVisible(false);
                label2.setVisible(false);
                courseName.setText("");
                courseName.setVisible(false);
                addEntryButton.setVisible(true);
                remove.setVisible(true);
                group.clearSelection();
                academic.setVisible(false);
                KAP.setVisible(false);
                AP.setVisible(false);
            }
        });

        cancelEdit.addActionListener(e -> {
            cancelEdit.setVisible(false);
            confirmEdit.setVisible(false);
            table.clearSelection();
            addEntryButton.setVisible(true);
            remove.setVisible(true);
            courseName.setText("");
            courseName.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            group.clearSelection();
            academic.setVisible(false);
            KAP.setVisible(false);
            AP.setVisible(false);
            remove.setVisible(false);

        });

        remove.addActionListener(e -> {
            removeInSchedule((String) table.getValueAt(table.getSelectedRow(),1));
            try {
                ResultSet rs = statement2.executeQuery("SELECT * FROM section WHERE id >=1;");
                while(rs != null && rs.next())
                {
                    int courseId = rs.getInt("course_id");
                    if(courseId == (Integer) table.getValueAt(table.getSelectedRow(),0))
                    {
                        statement2.execute("DELETE FROM section WHERE id=" + courseId);
                        break;
                    }
                }

                statement2.execute("DELETE FROM course WHERE id=" + table.getValueAt(table.getSelectedRow(), 0));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            model.removeRow(table.getSelectedRow());
            remove.setVisible(false);
            edit.setVisible(false);

        });

    }
    public static void editSchedule(String courseName, String newCourseName)
    {
        try
        {
            ResultSet s = statement2.executeQuery("Select * from student where id >=1;");
            String replacement = "";
            while(s != null && s.next())
            {
                String schedule = s.getString("section");
                StringTokenizer st = new StringTokenizer(schedule);
                int cnt = 0;
                while(st.hasMoreTokens())
                {
                    String token = st.nextToken();
                    String[] l = token.split(",");

                    if(l[1].equals(courseName))
                    {
                        //edit + add to replacement
                        String[] ar = token.split(",");
                        if(cnt == 0)
                        {
                            replacement+= ar[0] + "," + newCourseName + " ";
                        }
                        else
                        {
                            replacement+=" " + ar[0] + "," + newCourseName;
                        }

                    }
                    else
                    {
                        if(cnt ==0) {
                            replacement+= token + " ";
                        }
                        else
                        {
                            replacement+=" " + token;
                        }
                    }
                    cnt++;
                }
                statement.execute("UPDATE student SET section = '" + replacement  + "' WHERE id=" + s.getInt("id"));

            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    public static void removeInSchedule(String courseName)
    {
        try
        {
            ResultSet s = statement2.executeQuery("Select * from student where id >=1;");
            String replacement = "";
            while(s != null && s.next())
            {
                String schedule = s.getString("section");
                StringTokenizer st = new StringTokenizer(schedule);
                while(st.hasMoreTokens())
                {
                    String token = st.nextToken();
                    String[] l = token.split(",");

                    if(!l[1].equals(courseName))
                    {
                        if(replacement.equals(""))
                        {
                            replacement+=token + " ";
                        }
                        else
                        {
                            replacement+= " " + token;
                        }

                    }
                }
                statement.execute("UPDATE student SET section = '" + replacement  + "' WHERE id=" + s.getInt("id"));

            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

}