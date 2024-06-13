package student_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Teacher extends JFrame implements ActionListener{

    // 定义组件
    JPanel jp_1, jp_2, jp_3, jp_4, jp_5 = null;
    JLabel jlb_1, jlb_2, jlb_3, jlb_4, jlb_5 = null;
    JButton jb_1, jb_2, jb_3, jb_4 = null;
    JTextField jtf;
    JTable jtable_1, jtable_2 = null;
    DefaultTableModel model_1, model_2 = null;
    JScrollPane jsp_1, jsp_2 = null;

    String tea_no; // 存储教师教工号
    String tea_name; // 教师姓名
    String sexual;  // 教师性别
    String age; // 年龄
    String salary; // 工资
    String position; // 职称
    String subject; // 科目

    String sno; // 学生学号
    String stu_name; //学生姓名
    String grade; // 分数

    // 数据库驱动
    static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=jsp;trustServerCertificate=true";
//    static String dbURL = "jdbc:sqlserver://localhost:1433;DatabaseName=jsp";

    // 数据库用户名和密码
    static String userName = "sa";
    static String userPwd = "123456";

    // 设置与数据库连接的对象、sql语句、查询的结果集
    static Connection ct = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public static void main(String args[]){
        Teacher tea = new Teacher("小木同学", "123456789");
    }

    public Teacher(String name, String tno){
        tea_no = tno;  // 记录教工号

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
        jp_3 = new JPanel();
        jp_4 = new JPanel();
        jp_5 = new JPanel();

        // 标签
        jlb_1 = new JLabel("姓名：");
        jlb_2 = new JLabel(name);
        jlb_3 = new JLabel("教工号：");
        jlb_4 = new JLabel(tno);
        jlb_5 = new JLabel("请输入学号");

        jlb_1.setFont(new java.awt.Font("Dialog", 1, 30));
        jlb_2.setFont(new java.awt.Font("Dialog", 1, 25));
        jlb_3.setFont(new java.awt.Font("Dialog", 1, 30));
        jlb_4.setFont(new java.awt.Font("Dialog", 1, 25));
        jlb_5.setFont(new java.awt.Font("Dialog", 1, 20));

        // 单击按钮
        jb_1 = new JButton("个人信息");
        jb_2 = new JButton("查询成绩");
        jb_3 = new JButton("修改成绩");
        jb_4 = new JButton("退出系统");
        jb_1.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_2.setFont(new java.awt.Font("Dialog", 1, 15));
        jb_3.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_4.setFont(new java.awt.Font("Dialog", 1, 20));

        // 添加监听器
        jb_1.addActionListener(this);
        jb_2.addActionListener(this);
        jb_3.addActionListener(this);
        jb_4.addActionListener(this);

        // 文本框
        jtf = new JTextField(10);

        // 设置表格
        String[] colnames_1 = {"姓名", "性别", "年龄","工资", "职称", "所教科目"};
        model_1 = new DefaultTableModel(colnames_1, 1);
        jtable_1 = new JTable(model_1);
        jsp_1 = new JScrollPane(jtable_1);

        String[] colnames_2 = {"姓名", "学号", "科目", "成绩"};
        model_2 = new DefaultTableModel(colnames_2, 1);
        jtable_2 = new JTable(model_2);
        jsp_2 = new JScrollPane(jtable_2);

        // 加入面板
        jp_1.add(jlb_1);
        jp_1.add(jlb_2);

        jp_2.add(jlb_3);
        jp_2.add(jlb_4);

        jp_3.add(jb_1);
        jp_3.add(jsp_1);

        jp_4.add(jlb_5);
        jp_4.add(jtf);
        jp_4.add(jb_2);
        jp_4.add(jsp_2);

        jp_5.add(jb_3);
        jp_5.add(jb_4);

        // 将面板加入窗口
        this.add(jp_1);
        this.add(jp_2);
        this.add(jp_3);
        this.add(jp_4);
        this.add(jp_5);

        // 设置布局管理器（网格布局和流式布局）
        this.setLayout(new GridLayout(5, 1));
        jp_1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp_2.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 设置窗口标题
        this.setTitle("某大学教师页面");

        // 设置窗口大小
        this.setSize(500, 500);

        // 设置窗口的起始位置
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)

        // 设置当关闭窗口时，程序也结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示窗口
        this.setVisible(true);
        this.setResizable(true);
    }

    // 动作响应函数
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "个人信息"){
            try{
                ps = ct.prepareStatement("select * from Teacher where 教工号 =? ");
                ps.setString(1, tea_no);

                rs = ps.executeQuery();
                // 获取对应教工号的教师信息
                while(rs.next()){
                    tea_name = rs.getString(5);
                    sexual = rs.getString(6);
                    age = rs.getString(9);
                    salary = rs.getString(7);
                    position = rs.getString(8);
                    subject = rs.getString(10);
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
            // 将获取到的个人信息放入表格
            jtable_1.setValueAt(tea_name, 0, 0);
            jtable_1.setValueAt(sexual, 0, 1);
            jtable_1.setValueAt(age, 0, 2);
            jtable_1.setValueAt(salary, 0, 3);
            jtable_1.setValueAt(position, 0, 4);
            jtable_1.setValueAt(subject, 0, 5);
        }else if(e.getActionCommand() == "查询成绩"){   // 想查询哪个学生的成绩（输入其学号）
            sno = jtf.getText();
            try{
                ps = ct.prepareStatement("select * from Student where 学号 =? ");
                ps.setString(1, sno);

                rs = ps.executeQuery();
                // 获取对应学号的学生成绩（根据此教师所教的科目）
                while(rs.next()){
                    stu_name = rs.getString(5);
                    if(subject.equals("高数"))
                        grade = rs.getString(6);
                    else if(subject.equals("C语言"))
                        grade = rs.getString(7);
                    else if(subject.equals("Java"))
                        grade = rs.getString(8);
                    else if(subject.equals("英语"))
                        grade = rs.getString(9);
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
            // 将学生信息和成绩放入表格
            jtable_2.setValueAt(stu_name, 0, 0);
            jtable_2.setValueAt(sno, 0, 1);
            jtable_2.setValueAt(subject, 0, 2);
            jtable_2.setValueAt(grade, 0, 3);
        }else if(e.getActionCommand() == "修改成绩"){
            ChangeGrade cg = new ChangeGrade(sno, subject);   // 弹出修改成绩的界面
        }else if(e.getActionCommand() == "退出系统"){
            System.exit(0);
        }
    }
}
//————————————————
//
//        版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//        原文链接：https://blog.csdn.net/Mu_yongheng/article/details/107224452
