import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.*;
import java.util.ArrayList;

public class task extends JFrame{
    task()
    {
        setTitle("TASK MANAGER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);

        JLabel usernameLabel = new JLabel("ENTER USERNAME");
        JLabel passwordLabel = new JLabel("ENTER PASSWORD");

        JTextField usernameField= new JTextField(10);
        JPasswordField passwordField=new JPasswordField(10);

        JButton Login_Button=new JButton("LOGIN");
        Login_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                String username=usernameField.getText();
                String password=new String(passwordField.getPassword());

                if(login(username,password))
                {
                    openTaskManeger(username);
                }
                else
                {
                    JOptionPane.showMessageDialog(task.this,"Invalid user name or password. Please try again.");

                }
            }
        });

        JPanel loginPanel=new JPanel();
        loginPanel.setLayout(new GridLayout(4,2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(Login_Button);
        loginPanel.add(new JLabel());

        setLayout(new BorderLayout());
        add(loginPanel,BorderLayout.CENTER);
    }
    private boolean login(String username,String password)
    {
        return (username.equals("admin") && password.equals("admin"));
    }
    private void openTaskManeger(String username)
    {
        TaskManagerFrame Tasks=new TaskManagerFrame(username);
        task.setVisible(true);
        dispose();
    }
    private class TaskManagerFrame extends JFrame{
        //private ArrayList<Task>tasks;
        private JTable taskTable;
        private DefaultTableModel tableModel;
        private String username;
        
        TaskManagerFrame(String username)
        {
            this.username=username;
    
            setTitle("TASK MANAGER");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500,400);
    
            //tasks=new ArrayList<>();
    
            
        }
    };
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new task();
            }
        });
    }
        
}

