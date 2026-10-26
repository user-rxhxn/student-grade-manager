package studentgrademanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGradeManager extends JFrame {

    private JTextField nameField;
    private JTextField mark1Field;
    private JTextField mark2Field;
    private JTextField mark3Field;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JLabel summaryLabel;

    public StudentGradeManager() {
        setTitle("Student Grade Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
    }

    private void createInterface() {
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "Student Grade Management System",
                SwingConstants.CENTER
        );
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));

        add(heading, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(
                BorderFactory.createTitledBorder("Student Information")
        );

        nameField = new JTextField();
        mark1Field = new JTextField();
        mark2Field = new JTextField();
        mark3Field = new JTextField();

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Mark 1 (0-100):"));
        inputPanel.add(mark1Field);

        inputPanel.add(new JLabel("Mark 2 (0-100):"));
        inputPanel.add(mark2Field);

        inputPanel.add(new JLabel("Mark 3 (0-100):"));
        inputPanel.add(mark3Field);

        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear Fields");

        inputPanel.add(addButton);
        inputPanel.add(clearButton);

        String[] columns = {
            "Student Name", "Mark 1", "Mark 2",
            "Mark 3", "Average", "Grade"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        studentTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Student Results")
        );

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JButton removeButton = new JButton("Remove Selected Student");

        summaryLabel = new JLabel("Total Students: 0 | Class Average: 0.00");
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(summaryLabel, BorderLayout.CENTER);
        bottomPanel.add(removeButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        clearButton.addActionListener(e -> clearFields());
        removeButton.addActionListener(e -> removeStudent());
    }

    private void addStudent() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter the student's name."
            );
            return;
        }

        try {
            double mark1 = Double.parseDouble(mark1Field.getText().trim());
            double mark2 = Double.parseDouble(mark2Field.getText().trim());
            double mark3 = Double.parseDouble(mark3Field.getText().trim());

            if (!validMark(mark1) || !validMark(mark2)
                    || !validMark(mark3)) {
                JOptionPane.showMessageDialog(
                        this, "All marks must be between 0 and 100."
                );
                return;
            }

            double average = (mark1 + mark2 + mark3) / 3.0;
            String grade = calculateGrade(average);

            tableModel.addRow(new Object[]{
                name,
                mark1,
                mark2,
                mark3,
                String.format("%.2f", average),
                grade
            });

            updateSummary();
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter valid numbers for all three marks."
            );
        }
    }

    private boolean validMark(double mark) {
        return mark >= 0 && mark <= 100;
    }

    private String calculateGrade(double average) {
        if (average >= 80) {
            return "A";
        } else if (average >= 70) {
            return "B";
        } else if (average >= 60) {
            return "C";
        } else if (average >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    private void removeStudent() {
        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a student to remove."
            );
            return;
        }

        tableModel.removeRow(selectedRow);
        updateSummary();
    }

    private void clearFields() {
        nameField.setText("");
        mark1Field.setText("");
        mark2Field.setText("");
        mark3Field.setText("");
        nameField.requestFocusInWindow();
    }

    private void updateSummary() {
        int totalStudents = tableModel.getRowCount();
        double totalAverage = 0;

        for (int i = 0; i < totalStudents; i++) {
            totalAverage += Double.parseDouble(
                    tableModel.getValueAt(i, 4).toString()
            );
        }

        double classAverage = totalStudents > 0
                ? totalAverage / totalStudents
                : 0;

        summaryLabel.setText(String.format(
                "Total Students: %d | Class Average: %.2f",
                totalStudents, classAverage
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeManager app = new StudentGradeManager();
            app.setVisible(true);
        });
    }
}