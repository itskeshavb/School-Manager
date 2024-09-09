import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import java.sql.*;
public class Main {

    static final String URL = "jdbc:mysql://localhost:3306/p2";
    static final String USER = "root";
    static final String PASS = "password";

    static Connection connection;
    static Statement statement;

    public static void main(String[] args) {
        TeacherPanel teacherPanel = new TeacherPanel();
        StudentPanel studentPanel = new StudentPanel();
        CoursePanel coursePanel = new CoursePanel();


        statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
            statement = connection.createStatement();
            statement.execute("USE p2");
            statement.executeUpdate(teacherPanel.createTable);
            statement.executeUpdate(studentPanel.createTable);
            statement.executeUpdate(coursePanel.createTable);

        } catch (Exception e) {
            System.out.println(e);
        }
        SectionPanel sectionPanel = new SectionPanel();
        try{
            statement.executeUpdate(sectionPanel.createTable);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("School");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();

        Insets insets = frame.getInsets();
        int width = 900 + insets.left + insets.right;
        int height = 600 + insets.top + insets.bottom;
        frame.setPreferredSize(new Dimension(width, height));
        frame.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem exportMenu = new JMenuItem("Export Data");
        fileMenu.add(exportMenu);

        JMenuItem importMenu = new JMenuItem("Import Data");
        fileMenu.add(importMenu);

        JMenuItem purgeMenu = new JMenuItem("Purge Data");
        purgeMenu.addActionListener(e -> {
            try {
                statement.executeUpdate("DROP TABLE IF EXISTS teacher;");
                statement.executeUpdate("DROP TABLE IF EXISTS student;");
                statement.executeUpdate("DROP TABLE IF EXISTS course;");
                statement.executeUpdate("DROP TABLE IF EXISTS section;");
            } catch (Exception ex) {}
            System.exit(0);
        });
        fileMenu.add(purgeMenu);

        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(e ->
        {
            System.exit(0);
        });

        fileMenu.add(exitMenu);

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        JMenuItem teacherView = new JMenuItem("Teacher");
        teacherView.addActionListener(e -> {
            frame.setContentPane(new TeacherPanel());
            frame.pack();
        });
        viewMenu.add(teacherView);

        JMenuItem studentView = new JMenuItem("Student");
        studentView.addActionListener(e -> {
            frame.setContentPane(new StudentPanel());
            frame.pack();
        });
        viewMenu.add(studentView);

        JMenuItem courseView = new JMenuItem("Course");
        courseView.addActionListener(e -> {
            frame.setContentPane(coursePanel);
            frame.pack();
        });
        viewMenu.add(courseView);

        JMenuItem sectionView = new JMenuItem("Section");
        sectionView.addActionListener(e -> {
            frame.setContentPane(new SectionPanel());
            frame.pack();

        });
        viewMenu.add(sectionView);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenu = new JMenuItem("About");
        aboutMenu.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Version 1. Creator: Keshav Bhargava"));
        helpMenu.add(aboutMenu);
        menuBar.add(helpMenu);

        frame.pack();
        frame.setVisible(true);


    }
}