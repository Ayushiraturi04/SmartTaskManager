import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskManager extends JFrame {
    public TaskManager() {
        setTitle("TASK MANAGER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);

        JLabel usernameLabel = new JLabel("ENTER USERNAME");
        JLabel passwordLabel = new JLabel("ENTER PASSWORD");

        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (login(username, password)) {
                    openTaskManager(username);
                    JOptionPane.showMessageDialog(TaskManager.this, "Login Successful");
                } else {
                    JOptionPane.showMessageDialog(TaskManager.this, "Invalid username or password. Please try again.");
                }
            }
        });

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);
        loginPanel.add(new JLabel());

        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.CENTER);
    }

    private boolean login(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private void openTaskManager(String username) {
        TaskManagerFrame TM = new TaskManagerFrame(username);
        TM.setVisible(true);
        dispose();
    }

    private class TaskManagerFrame extends JFrame {
        private ArrayList<Task> tasks;
        private JTable taskTable;
        private DefaultTableModel tableModel;
        private String username;

        public TaskManagerFrame(String username) {
            this.username = username;

            setTitle("TASK MANAGER");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 500);

            tasks = new ArrayList<>();

            tableModel = new DefaultTableModel(new Object[]{"TITLE", "DESCRIPTION", "PRIORITY", "DUE DATE"}, 0);
            taskTable = new JTable(tableModel);

            JButton addButton = new JButton("ADD");
            JButton editButton = new JButton("EDIT");
            JButton deleteButton = new JButton("DELETE");

            addButton.setIcon(resizeIcon(new ImageIcon("icons8-add-50.png"), 20, 20));
            editButton.setIcon(resizeIcon(new ImageIcon("icons8-edit-50.png"), 20, 20));
            deleteButton.setIcon(resizeIcon(new ImageIcon("icons8-delete-50.png"), 20, 20));

            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addTask();
                }
            });

            editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editTask();
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteTask();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            JScrollPane scrollPane = new JScrollPane(taskTable);
            setLayout(new BorderLayout());
            add(scrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);

            loadTasksFromFile();
        }

        private void loadTasksFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader(username + ".txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] taskData = line.split(",");
                    String title = taskData[0];
                    String description = taskData[1];
                    String priority = taskData[2];
                    String dueDate = taskData[3];
                    tasks.add(new Task(title, description, priority, dueDate));
                }
            } catch (IOException e) {
                // Handle file reading errors
            }

            tasks.sort(new Comparator<Task>() {
                public int compare(Task task1, Task task2) {
                    int priority1 = getPriorityValue(task1.priority);
                    int priority2 = getPriorityValue(task2.priority);
                    return Integer.compare(priority1, priority2);
                }

                private int getPriorityValue(String priority) {
                    switch (priority) {
                        case "high":
                            return 0;
                        case "medium":
                            return 1;
                        case "low":
                            return 2;
                        default:
                            return 3;
                    }
                }
            });

            for (Task task : tasks) {
                tableModel.addRow(new Object[]{task.title, task.description, task.priority, task.dueDate});
            }
        }

        private void addTask() {
            String title = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the task title");
            String description = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the task description");
            String[] priorityOptions = {"high", "medium", "low"};
            String priority = (String) JOptionPane.showInputDialog(
                    TaskManagerFrame.this,
                    "Select the task Priority: ",
                    "Priority",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    priorityOptions,
                    "medium"
            );
            String dueDate;
            while (true) {
                dueDate = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the task due date (yyyy-MM-dd)");
                if (isValidDateFormat(dueDate)) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(TaskManagerFrame.this, "Invalid date format. Please enter the date in yyyy-MM-dd format.");
                }
            }
            Task task1 = new Task(title, description, priority, dueDate);
            tasks.add(task1);
            tableModel.addRow(new Object[]{task1.title, task1.description, task1.priority, task1.dueDate});
            saveTasksToFile();
        }

        private boolean isValidDateFormat(String date) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }

        private void editTask() {
            int selectedRow = taskTable.getSelectedRow();

            if (selectedRow != -1) {
                Task tsk = tasks.get(selectedRow);

                String title = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the new task title", tsk.title);
                String description = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the new task description", tsk.description);
                String[] priorityOptions = {"high", "medium", "low"};
                String priority = (String) JOptionPane.showInputDialog(
                        TaskManagerFrame.this,
                        "Select new task Priority",
                        "Priority",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        priorityOptions,
                        tsk.priority
                );
                String dueDate;
                while (true) {
                    dueDate = JOptionPane.showInputDialog(TaskManagerFrame.this, "Enter the new task due date (yyyy-MM-dd)");
                    if (isValidDateFormat(dueDate)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(TaskManagerFrame.this, "Invalid date format. Please enter the date in yyyy-MM-dd format.");
                    }
                }
                tsk.title = title;
                tsk.description = description;
                tsk.priority = priority;
                tsk.dueDate = dueDate;
                tableModel.setValueAt(tsk.title, selectedRow, 0);
                tableModel.setValueAt(tsk.description, selectedRow, 1);
                tableModel.setValueAt(tsk.priority, selectedRow, 2);
                tableModel.setValueAt(tsk.dueDate, selectedRow, 3);
                saveTasksToFile();
            }
        }

        private void deleteTask() {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                tasks.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                saveTasksToFile();
            }
        }

        private void saveTasksToFile() {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(username + ".txt")))) {
                for (Task task : tasks) {
                    writer.println(task.title + "," + task.description + "," + task.priority + "," + task.dueDate);
                }
            } catch (IOException e) {
                // Handle file writing errors
            }
        }
    }

    private static class Task {
        private String title;
        private String description;
        private String priority;
        private String dueDate;

        public Task(String title, String description, String priority, String dueDate) {
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.dueDate = dueDate;
        }
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.setVisible(true);
    }
}
