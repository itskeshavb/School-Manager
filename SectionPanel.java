import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.sql.*;
public class SectionPanel extends JPanel {
    String createTable =
            "CREATE TABLE IF NOT EXISTS section " +
                    "(id INTEGER not NULL AUTO_INCREMENT, " +
                    " course_id INTEGER not NULL, " +
                    " teacher_id INTEGER not NULL, " +
                    " course_name TEXT not NULL, " +
                    " teacher_name TEXT not NULL, " +
                    " PRIMARY KEY (id))";
    static final String URL = "jdbc:mysql://localhost:3306/p2";
    static final String USER = "root";
    static final String PASS = "password";

    static Connection connection;
    static Statement statement;
    static Statement statement2;
    public SectionPanel() {
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

        DefaultTableModel dtm = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        dtm.addColumn("Student ID");
        dtm.addColumn("First Name");
        dtm.addColumn("Last Name");
        JTable sectionsTaught = new JTable(dtm);

        JScrollPane sp = new JScrollPane(sectionsTaught);
        sp.setVisible(false);
        sp.setBounds(700, 0, 200, 400);
        add(sp);

        JButton addStudentToRoster = new JButton("Add Student");
        addStudentToRoster.setFont(new Font("Arial", Font.BOLD, 14));
        addStudentToRoster.setBounds(480, 500, 180, 40);
        add(addStudentToRoster);
        addStudentToRoster.setVisible(false);

        JButton removeStudentFromRoster = new JButton("Remove Student");
        removeStudentFromRoster.setFont(new Font("Arial", Font.BOLD, 14));
        removeStudentFromRoster.setBounds(480, 420, 180, 40);
        add(removeStudentFromRoster);
        removeStudentFromRoster.setVisible(false);

        JLabel student = new JLabel("Students");
        student.setFont(new Font("Arial", Font.PLAIN, 24));
        student.setBounds(20, 420, 160, 40);
        add(student);
        student.setVisible(false);

        ArrayList<String> addStudents = new ArrayList<>();
        String s10 = "SELECT * FROM student WHERE id >=1;";

        try{
            ResultSet rs = statement.executeQuery(s10);
            while(rs != null && rs.next())
            {
                addStudents.add("" + rs.getInt("id") + " " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String[] ar = new String[addStudents.size()];
        for(int i  = 0; i < ar.length; i++)
        {
            ar[i] = addStudents.get(i);
        }
        JComboBox<String> cb3 = new JComboBox<>(ar);
        cb3.setBounds(20, 460, 150, 50);
        cb3.setVisible(false);
        add(cb3);

        JButton cancelStudent = new JButton("Cancel");
        cancelStudent.setFont(new Font("Arial", Font.BOLD, 14));
        cancelStudent.setBounds(480, 420, 180, 40);
        add(cancelStudent);
        cancelStudent.setVisible(false);

        JButton confirmStudent = new JButton("Confirm");
        confirmStudent.setFont(new Font("Arial", Font.BOLD, 14));
        confirmStudent.setBounds(480, 500, 180, 40);
        add(confirmStudent);
        confirmStudent.setVisible(false);

        DefaultTableModel model = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Course ID");
        model.addColumn("Teacher ID");
        model.addColumn("Course");
        model.addColumn("Teacher");
        JTable table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 0, 600, 400);
        add(pane);

        String s5 = "SELECT * FROM section WHERE id >=1;";
        try{
            ResultSet rs = statement.executeQuery(s5);
            while(rs != null && rs.next())
            {
                model.addRow(new Object[]{rs.getInt("id"), rs.getInt("course_id"), rs.getString("teacher_id"), rs.getString("course_name"), rs.getString("teacher_name")});
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        JLabel label1 = new JLabel("Course");
        label1.setFont(new Font("Arial", Font.PLAIN, 24));
        label1.setBounds(20, 420, 160, 40);

        add(label1);
        label1.setVisible(false);

        JLabel label2 = new JLabel("Teacher");
        label2.setFont(new Font("Arial", Font.PLAIN, 24));
        label2.setBounds(240, 420, 160, 40);

        add(label2);
        label2.setVisible(false);

        //import courses

        ArrayList<String> courses = new ArrayList<>();
        String s2 = "SELECT * FROM course WHERE id >=1;";

        try{
            ResultSet rs = statement.executeQuery(s2);
            while(rs != null && rs.next())
            {
                courses.add("" + rs.getInt("id") + "-" + rs.getString("course_name"));
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String[] cr = new String[courses.size()];
        for(int i = 0; i < cr.length; i++)
        {
            cr[i] = courses.get(i);
        }
        JComboBox<String> cb1 = new JComboBox<>(cr);
        cb1.setBounds(20, 460, 150, 50);
        cb1.setVisible(false);
        add(cb1);


        //import teachers
        ArrayList<String> teachers = new ArrayList<>();
        String s3 = "SELECT * FROM teacher WHERE id >=1;";
        try{
            ResultSet rs = statement.executeQuery(s3);
            while(rs != null && rs.next())
            {
                teachers.add("" + rs.getInt("id")  + "-" + rs.getString("last_name") + ", " + rs.getString("first_name"));
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        String[] tc = new String[teachers.size()];
        for(int i = 0; i < tc.length; i++)
        {
            tc[i] = teachers.get(i);
        }


        JComboBox<String> cb2 = new JComboBox<>(tc);
        cb2.setBounds(240, 460, 150, 50);
        cb2.setVisible(false);
        add(cb2);



        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Arial", Font.BOLD, 24));
        addButton.setBounds(480, 420, 160, 40);
        addButton.setVisible(false);


        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 24));
        cancelButton.setBounds(480, 480, 160, 40);
        add(cancelButton);
        cancelButton.setVisible(false);

        add(addButton);
        JButton addEntryButton = new JButton("Add new Section");
        addEntryButton.setFont(new Font("Arial", Font.BOLD, 14));
        addEntryButton.setBounds(700, 420, 180, 40);
        add(addEntryButton);
        addEntryButton.setVisible(true);
        addEntryButton.addActionListener(e -> {
            table.clearSelection();
            sp.setVisible(false);
            label1.setVisible(true);
            label2.setVisible(true);
            addButton.setVisible(true);
            cancelButton.setVisible(true);
            addEntryButton.setVisible(false);
            cb1.setVisible(true);
            cb2.setVisible(true);
        });

        addButton.addActionListener(e -> {
            if(cb1.getSelectedIndex() != -1 && cb2.getSelectedIndex() != -1 && cb1.getSelectedItem() != null && cb2.getSelectedItem() != null)
            {
                String f = (String) cb1.getSelectedItem();
                String[] ar1 = f.split("-");
                String first = ar1[1];
                int courseID = Integer.parseInt(ar1[0]);
                String l = (String) cb2.getSelectedItem();
                String[] ar2 = l.split("-");
                String last = ar2[1];
                int teacherID = Integer.parseInt(ar2[0]);
                String s1 = "INSERT INTO section (course_id, teacher_id, course_name, teacher_name) VALUES ('" + courseID + "', '" + teacherID + "', '"+ first + "', '" + last + "')";
                try {
                    String s4 = "SELECT * FROM section WHERE id = (SELECT max(id) FROM section);";
                    statement.executeUpdate(s1);

                    ResultSet rs = statement.executeQuery(s4);
                    while(rs != null && rs.next())
                        model.addRow(new Object[]{rs.getInt("id"), rs.getString("course_id"), rs.getString("teacher_id"), rs.getString("course_name"), rs.getString("teacher_name")});
                    model.fireTableDataChanged();
                    cb1.setVisible(false);
                    cb2.setVisible(false);
                    label1.setVisible(false);
                    label2.setVisible(false);

                    addButton.setVisible(false);
                    cancelButton.setVisible(false);
                    addEntryButton.setVisible(true);

                } catch (Exception ex) {}
            }


        });
        cancelButton.addActionListener(e -> {
            cb1.setVisible(false);
            label1.setVisible(false);
            label2.setVisible(false);
            cb2.setVisible(false);
            addButton.setVisible(false);
            cancelButton.setVisible(false);
            addEntryButton.setVisible(true);
        });

        addStudentToRoster.addActionListener(e -> {
            addStudentToRoster.setVisible(false);
            removeStudentFromRoster.setVisible(false);
            student.setVisible(true);
            cb3.setVisible(true);
            addEntryButton.setVisible(false);
            confirmStudent.setVisible(true);
            cancelStudent.setVisible(true);
        });
        confirmStudent.addActionListener(e -> {
            String s = (String) cb3.getSelectedItem();
            if(s != null)
            {
                StringTokenizer st = new StringTokenizer(s);
                int id = Integer.parseInt(st.nextToken());
                dtm.addRow(new Object[]{id, st.nextToken(), st.nextToken()});
                cb3.setVisible(false);
                student.setVisible(false);
                confirmStudent.setVisible(false);
                cancelStudent.setVisible(false);
                addStudentToRoster.setVisible(true);
                removeStudentFromRoster.setVisible(true);
                String original = "";
                String val = "";
                try {
                    ResultSet rs = statement.executeQuery("SELECT * FROM student WHERE id=" +id);
                    while(rs!=null&&rs.next())
                        original = rs.getString("section");
                    if(original.equals(""))
                        val = table.getValueAt(table.getSelectedRow(),0) + "," + table.getValueAt(table.getSelectedRow(),3) + " ";
                    else
                        val = " " + table.getValueAt(table.getSelectedRow(),0) + "," + table.getValueAt(table.getSelectedRow(),3);
                    statement.execute("UPDATE student SET section = '" + original + val  + "' WHERE id=" + id);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        removeStudentFromRoster.addActionListener(e -> {
            dtm.removeRow(sectionsTaught.getSelectedRow());
            sectionsTaught.clearSelection();
        });

        cancelStudent.addActionListener(e -> {
            cb3.setVisible(false);
            student.setVisible(false);
            cancelStudent.setVisible(false);
            confirmStudent.setVisible(false);
            addEntryButton.setVisible(true);
            table.clearSelection();
            sp.setVisible(false);
        });

        JButton removeSection = new JButton("Remove Section");
        removeSection.setFont(new Font("Arial", Font.BOLD, 14));
        removeSection.setBounds(700, 420, 180, 40);
        add(removeSection);
        removeSection.setVisible(false);
        removeSection.addActionListener(e -> {
            removeInSchedule((Integer) table.getValueAt(table.getSelectedRow(),0));
            try {
                statement.execute("DELETE FROM section WHERE id=" + table.getValueAt(table.getSelectedRow(),0));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            model.removeRow(table.getSelectedRow());
            sp.setVisible(false);
            addStudentToRoster.setVisible(false);
            removeStudentFromRoster.setVisible(false);
        });


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    sp.setVisible(true);
                    removeSection.setVisible(true);
                    addStudentToRoster.setVisible(true);
                    removeStudentFromRoster.setVisible(true);
                    addEntryButton.setVisible(false);
                    String s5 = "SELECT * FROM student WHERE id >=1;";
                    String tempSchedule = "";
                    dtm.getDataVector().removeAllElements();

                    try{
                        ResultSet rs = statement.executeQuery(s5);
                        while(rs != null && rs.next())
                        {
                            tempSchedule = rs.getString("section");
                            StringTokenizer st = new StringTokenizer(tempSchedule);
                            while(st.hasMoreTokens())
                            {
                                String[] ar = st.nextToken().split(",");
                                int sectionID = Integer.parseInt(ar[0]);
                                if((Integer) table.getValueAt(table.getSelectedRow(),0) == sectionID)
                                {
                                    dtm.addRow(new Object[]{rs.getInt("id"),rs.getString("first_name"),rs.getString("last_name")});
                                }
                            }
                        }
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });

    }
    public static void removeInSchedule(int sectionID)
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

                    if(Integer.parseInt(l[0]) == sectionID)
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