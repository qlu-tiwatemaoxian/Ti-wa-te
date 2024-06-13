package student_system;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class ChangeGrade extends JFrame implements ActionListener{

    // 组件
    JPanel jp_1, jp_2 = null;
    JLabel jlb = null;
    JTextField jtf = null; // 输入修改的成绩
    JButton jb_1, jb_2, jb_3 = null; // 修改和退出修改按钮

    // 存储修改成绩学生学号和课程
    String Sno;
    String subject;

    // 数据库驱动
    static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=jsp;trustServerCertificate=true";
//    static String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=jsp";

    // 数据库用户名和密码
    static String userName = "sa";
    static String userPwd = "123456";

    // 设置与数据库连接的对象、编译后的sql语句、查询的结果集
    static Connection ct = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 主函数（实际运行时不需要）
    public static void main(String[] args[]){
        ChangeGrade cg = new ChangeGrade("123456789", "英语");
    }

    // 清除文本框中的内容
    public void clear(){
        jtf.setText("");
    }

    // 构造函数
    public ChangeGrade(String sno, String sub){
        // 学生学号以及科目
        Sno = sno;
        subject = sub;

        // 连接数据库（加载驱动、建立连接）
        try {
            Class.forName(driverName);
            ct = DriverManager.getConnection(dbURL, userName, userPwd);
//            ct = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 面板
        jp_1 = new JPanel();
        jp_2 = new JPanel();

        // 标签
        jlb = new JLabel("请输入修改后成绩");
        jlb.setFont(new java.awt.Font("Dialog", 1, 20));

        // 文本框
        jtf = new JTextField(10);

        // 单击按钮
        jb_1 = new JButton("确认修改");
        jb_2 = new JButton("重置");
        jb_3 = new JButton("退出修改");
        jb_1.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_2.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_3.setFont(new java.awt.Font("Dialog", 1, 20));

        // 添加监听
        jb_1.addActionListener(this);
        jb_2.addActionListener(this);
        jb_3.addActionListener(this);

        // 将组件加入面板
        jp_1.add(jlb);
        jp_1.add(jtf);

        jp_2.add(jb_1);
        jp_2.add(jb_2);
        jp_2.add(jb_3);

        // 将面板加入窗口
        this.add(jp_1);
        this.add(jp_2);

        // 设置格网分布
        this.setLayout(new GridLayout(2,1));

        // 给窗口添加标题
        this.setTitle("修改学生成绩");

        // 设置窗口大小
        this.setSize(500, 300);

        // 设置窗口的起始位置
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)

        // 设置当关闭窗口时，程序也结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示窗口
        this.setVisible(true);
        this.setResizable(true);
    }

    // 动作监听函数
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "确认修改"){
            change_grade();
        }else if(e.getActionCommand() == "重置"){
            clear();
        }else if(e.getActionCommand() == "退出修改"){
            dispose();
        }
    }

    public void change_grade(){
        String new_grade = String.valueOf(jtf.getText());
        // 当输入成绩，将字符串转换为数字
        double num_grade = 0;
        if(!new_grade.isEmpty())
            num_grade = Double.valueOf(new_grade);

        if(new_grade.isEmpty()){
            JOptionPane.showMessageDialog(null, "修改成绩为空\n请重新输入！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(num_grade > 100 || num_grade < 0){
            JOptionPane.showMessageDialog(null, "分数必须在0~100之间\n请重新输入！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else {
            try{
                String sql;
                if(subject.equals("高数")) {
                    sql = "update Student set 高数=? where 学号=?";
                    ps = ct.prepareStatement(sql);
                }else if(subject.equals("C语言")) {
                    sql = "update Student set C语言 = ? where 学号=?";
                    ps = ct.prepareStatement(sql);
                }else if(subject.equals("Java")) {
                    sql = "update Student set java=? where 学号=?";
                    ps = ct.prepareStatement(sql);
                } else if(subject.equals("英语")){
                    sql = "update Student set 英语=? where 学号=?";
                    ps = ct.prepareStatement(sql);
                }
                ps.setString(1, new_grade);
                ps.setString(2, Sno);
                int count=ps.executeUpdate();//执行sql语句
                ct.close();
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "修改成功", "提示信息", JOptionPane.WARNING_MESSAGE);
            dispose(); // 修改成功后关闭此界面
        }
    }
}

//————————————————
//
//        版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//        原文链接：https://blog.csdn.net/Mu_yongheng/article/details/107224452
