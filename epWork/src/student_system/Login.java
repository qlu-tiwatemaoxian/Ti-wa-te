package student_system;

import javax.swing.*;// 引入swing工具包进行GUI的设计

import java.awt.*;					             // 引入awt工具包
import java.awt.event.ActionEvent;              // 动作事件
import java.awt.event.ActionListener;           // 动作监听器
import java.awt.event.*;

import java.sql.*;                              // 数据库包

public class Login extends JFrame implements ActionListener{

    // 定义组件
    JPanel jp_1, jp_2, jp_3, jp_4 = null;       // 设置4个面板
    JLabel jlb_1, jlb_2, jlb_3 = null;          // 设置3个标签（用户、密码、身份）
    JTextField jtf = null;	                    // 设置1个普通文本框，用来输入用户账号
    JPasswordField jpf = null;                  // 设置一个密码文本框，用来输入密码
    JRadioButton jrb_1, jrb_2 = null;           // 设置两个按钮，用来选择身份（学生或者老师）
    ButtonGroup bg = null;				        // 添加一个按钮组（只能选择其中的一个）
    JButton jb_1, jb_2, jb_3 = null;            // 设置三个单击按钮（登录、重置、退出）


    // 设定用户名和密码
    String user_name;
    String password;

    // 学生姓名和学号
    String stu_name;
    String sno;

    // 老师姓名和教工号
    String tea_name;
    String tno;

    //192.168.83.92
    // 数据库驱动
    static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=jsp;trustServerCertificate=true";

    // 数据库用户名和密码
    static String userName="sa";
    static String userPwd="123456";


    // 数据库连接、sql语句、结果集等对象
    static Connection ct = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    public static void main(String[] args){
        // 创建登录界面
        Login login = new Login();
        // 进行数据库的连接
        try {
            Class.forName(driverName);
            ct = DriverManager.getConnection(dbURL,userName,userPwd);
            System.out.println("连接成功");
//            ct=DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 构造函数
    public Login()
    {
        // 创建组件

        // 面板（JPanel）
        jp_1 = new JPanel();
        jp_2 = new JPanel();
        jp_3 = new JPanel();
        jp_4 = new JPanel();

        // 标签（JLabel）
        jlb_1 = new JLabel("用户名");
        jlb_2 = new JLabel("密   码");
        jlb_3 = new JLabel("身   份");

        // 普通文本框（JTextField）
        jtf = new JTextField(10);              // 用于输入用户名

        // 密码文本框（JPasswordField）
        jpf = new JPasswordField(10);          // 用于输入登录密码

        // 单选按钮（JRadioButton）
        jrb_1 = new JRadioButton("教师");      // 用于选择身份
        jrb_2 = new JRadioButton("学生");

        //按钮组（ButtonGroup）只能选择其中一个
        bg = new ButtonGroup();
        bg.add(jrb_1);
        bg.add(jrb_2);
        jrb_2.setSelected(true);               // 默认为学生

        //单击按钮（JButton）
        jb_1 = new JButton("登录");
        jb_2 = new JButton("重置");
        jb_3 = new JButton("退出");
        // 设置监听
        jb_1.addActionListener(this);
        jb_2.addActionListener(this);
        jb_3.addActionListener(this);


        // 将个组件加入到面板
        jp_1.add(jlb_1);
        jp_1.add(jtf);

        jp_2.add(jlb_2);
        jp_2.add(jpf);

        jp_3.add(jlb_3);
        jp_3.add(jrb_1);
        jp_3.add(jrb_2);

        jp_4.add(jb_1);
        jp_4.add(jb_2);
        jp_4.add(jb_3);

        // 将面板加入到框架
        this.add(jp_1);
        this.add(jp_2);
        this.add(jp_3);
        this.add(jp_4);


        // 设置布局管理器（格网布局）
        this.setLayout(new GridLayout(4,1));

        // 给窗口添加标题
        this.setTitle("齐鲁工业大学学生管理系统");

        // 设置窗口大小
        this.setSize(500, 300);

        // 设置窗口的起始位置
        this.setLocationRelativeTo(null);       //在屏幕中间显示(居中显示)

        // 设置当关闭窗口时，程序也结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示窗口
        this.setVisible(true);
        this.setResizable(true);
    }


    // 将两个文本框的内容清空，点击重置按钮时调用
    public void clear(){
        jtf.setText("");
        jpf.setText("");
    }

    // 动作事件监听函数
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "登录"){
            // 选中教师登录
            if(jrb_1.isSelected()){
                try {
                    ps = ct.prepareStatement("select * from Teacher where 身份=? and 用户名=?");
                    // 给?赋值（按照身份为教师、用户名为输入的用户名查询）
                    ps.setString(1, "教师");
                    ps.setString(2, jtf.getText());

                    // ResultSet结果集
                    rs = ps.executeQuery();
                    if(rs.next()){
                        //将教师的用户名、密码、姓名、教工号从数据库取出
                        user_name = rs.getString(2);
                        password = rs.getString(3);
                        tea_name = rs.getString(5);
                        tno = rs.getString(4);
                        tea_login();
                    }else {
                        JOptionPane.showMessageDialog(null, "没有此用户或用户名为空！\n请重新输入", "提示消息", JOptionPane.WARNING_MESSAGE);
                    }
                }catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }else if(jrb_2.isSelected()) {  // 学生登录
                try {
                    ps = ct.prepareStatement("select * from Student where 身份=? and 用户名=?");
                    //给?赋值（按照身份为学生、用户名为输入的用户名查询）
                    ps.setString(1, "学生");
                    ps.setString(2, jtf.getText());

                    //ResultSet结果集
                    rs=ps.executeQuery();
                    if(rs.next()){
                        //将学生的用户名、密码、姓名、学号取出
                        user_name = rs.getString(2);
                        password = rs.getString(3);
                        stu_name = rs.getString(5);
                        sno = rs.getString(4);
                        stu_login();
                    }else {
                        JOptionPane.showMessageDialog(null, "没有此用户或用户名为空！\n请重新输入", "提示消息", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }else if(e.getActionCommand() == "重置"){
            clear();	        // 清除文本框里的内容
        }else if(e.getActionCommand() == "退出"){
            System.exit(0);		// 结束程序，关闭所有窗口
        }
    }


    // 学生登陆判断方法
    public void stu_login(){
        if(user_name.equals(jtf.getText()) && password.equals(String.valueOf(jpf.getPassword()))){
            JOptionPane.showMessageDialog(null, "登陆成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
            clear();
            dispose();			// 关闭当前登录界面
            Student stu = new Student(stu_name, sno); // 创建学生界面并显示
        }else if(jtf.getText().isEmpty() && String.valueOf(jpf.getPassword()).isEmpty()){
            JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示消息", JOptionPane.WARNING_MESSAGE);
        }else if(jtf.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "请输入用户名！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(String.valueOf(jpf.getPassword()).isEmpty()){
            JOptionPane.showMessageDialog(null, "请输入密码！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "用户名或者密码错误！\n请重新输入", "提示信息", JOptionPane.ERROR_MESSAGE);
            clear();
        }
    }


    // 教师登陆判断方法
    public void tea_login() {
        if(user_name.equals(jtf.getText()) && password.equals(String.valueOf(jpf.getPassword()))) {
            JOptionPane.showMessageDialog(null, "登录成功", "提示信息", JOptionPane.WARNING_MESSAGE);
            clear();
            dispose();
            Teacher tea = new Teacher(tea_name, tno);
        }else if(jtf.getText().isEmpty() && String.valueOf(jpf.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入用户名和密码！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(jtf.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入用户名！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(String.valueOf(jpf.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入密码！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(null, "用户名或密码错误！\n请重新输入", "提示信息", JOptionPane.ERROR_MESSAGE);
            clear();
        }
    }
}
