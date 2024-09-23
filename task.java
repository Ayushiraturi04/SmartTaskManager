import java.awt.*;
import java.awt.event.*;

public package task_manager;

class task extends Frame{
    private Label labelcount;
    private Button btncount;
    private TextField tfcount;
    private int count=0;

    task()
    {
        setLayout(new FlowLayout());

        labelcount=new Label("Counter");
        add(labelcount);
        tfcount=new TextField(count+" ",10);
        tfcount.setEditable(false);
        add(tfcount);
        btncount=new Button("Count");
        add(btncount);
        btncount.addActionListener(new btncountListener());

        setTitle("counter");
        setSize(300,300);
        setVisible(true);
    }
    public static void Main(String args[])
    {
        task ts=new task();
    }
    private Class btnCountListner implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ++count;
            tfcount.setText(count+"");
        }
    }
    
    
    
}