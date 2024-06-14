package student_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Student extends JFrame implements ActionListener{

    // 定义组件
    JPanel jp_1, jp_2, jp_3, jp_4, jp_5, jp_6 = null;                        // 姓名、学号
    JLabel jlb_1, jlb_2, jlb_3, jlb_4 = null;                     // 姓名、学号
    JButton jb_1, jb_2, jb_3, jb_4 = null;                           // 单击按钮（查成绩和选课）
    JTable jtable_1, jtable_2 = null;                                      // 表格
    DefaultTableModel model_1, model_2 = null;
    JScrollPane jsp_1, jsp_2 = null;                                    // 滚动条


    // 存储学生的学号和各科的成绩
    String Sno; // 学号
    String math; // 数学分数
    String c; // C语言分数
    String java; // java分数
    String english; // 英语分数

    String math_room; // 数学考场
    String c_room; // C语言考场
    String java_room; // java考场
    String english_room; //英语考场


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


    // 主函数，单独运行时有作用（用于测试）
    public static void main(String[] args)
    {
        Student stu = new Student("小木同学","123456789");
    }



    // 构造函数（参数为登录学生的姓名和学号）
    Student(String name,String sno){
        Sno = sno;                     // 得到登录学生学号（用于后面查询成绩）

        // 连接数据库（加载驱动、建立连接）
        try {
            Class.forName(driverName);
            ct = DriverManager.getConnection(dbURL, userName, userPwd);
//            ct = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建组件
        // 面板
        jp_1 = new JPanel();
        jp_2 = new JPanel();
        jp_3 = new JPanel();
        jp_4 = new JPanel();
        jp_5 = new JPanel();
        jp_6 = new JPanel();

        // 标签
        jlb_1 = new JLabel("姓名：");
        jlb_2 = new JLabel(name);
        jlb_3 = new JLabel("学号：");
        jlb_4 = new JLabel(sno);

        jlb_1.setFont(new java.awt.Font("Dialog", 1, 30));
        jlb_2.setFont(new java.awt.Font("Dialog", 1, 25));
        jlb_3.setFont(new java.awt.Font("Dialog", 1, 30));
        jlb_4.setFont(new java.awt.Font("Dialog", 1, 25));

        // 单击按钮
        jb_1 = new JButton("考场查询");
        jb_2 = new JButton("成绩查询");
        jb_3 = new JButton("退出系统");
        jb_4 = new JButton("修改密码");
        jb_1.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_2.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_3.setFont(new java.awt.Font("Dialog", 1, 20));
        jb_4.setFont(new java.awt.Font("Dialog", 1, 20));

        // 设置动作监听
        jb_1.addActionListener(this);
        jb_2.addActionListener(this);
        jb_3.addActionListener(this);
        jb_4.addActionListener(this);

        // 设置表格
        String[] colnames = {"高数成绩", "C语言成绩", "Java成绩", "英语成绩" };
        model_2 = new DefaultTableModel(colnames,1);
        jtable_2 = new JTable(model_2);
        jsp_2 = new JScrollPane(jtable_2);

        String[] colnames_1 = {"高数考场", "C语言考场", "Java考场", "英语考场"};
        model_1 = new DefaultTableModel(colnames_1, 1);
        jtable_1 = new JTable(model_1);
        jsp_1 = new JScrollPane(jtable_1);

        // 加入面板
        jp_1.add(jlb_1);
        jp_1.add(jlb_2);

        jp_2.add(jlb_3);
        jp_2.add(jlb_4);

        jp_3.add(jb_1);
        jp_3.add(jsp_1);

        jp_4.add(jb_2);
        jp_4.add(jsp_2);

        jp_5.add(jb_4);
        jp_5.add(jb_3);

        // 将面板加入到窗口
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
        this.setTitle("某大学学生页面");

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


    // 动作事件监听函数
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "考场查询"){
            try{
                ps = ct.prepareStatement("select * from Student where 学号 =? ");	// 将编译好的SQL语句赋值给ps
                ps.setString(1, Sno);			// 给?赋值，用setString将设置SQL语句按照学号Sno查询
                rs = ps.executeQuery();	        // ResultSet结果集,将对应学号的每一行信息放在ResultSet中

                // 获取对应学号的学生考场信息
                while(rs.next()){
                    math_room = rs.getString(10);
                    c_room = rs.getString(11);
                    java_room = rs.getString(12);
                    english_room = rs.getString(13);
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
            // 将考场信息加入表格
            jtable_1.setValueAt(math_room, 0, 0);
            jtable_1.setValueAt(c_room, 0, 1);
            jtable_1.setValueAt(java_room, 0, 2);
            jtable_1.setValueAt(english_room, 0, 3);
        }else if(e.getActionCommand() == "成绩查询"){
            try{
                ps = ct.prepareStatement("select * from Student where 学号 =? ");
                ps.setString(1, Sno);
                rs = ps.executeQuery();
                // 获取对应学号的学生成绩
                while(rs.next()){
                    math = rs.getString(6);
                    c = rs.getString(7);
                    java = rs.getString(8);
                    english = rs.getString(9);
                }
            }catch (Exception e1) {
                e1.printStackTrace();
            }
            // 将考试成绩加入表格
            jtable_2.setValueAt(math, 0, 0);
            jtable_2.setValueAt(c, 0, 1);
            jtable_2.setValueAt(java, 0, 2);
            jtable_2.setValueAt(english, 0, 3);
        }else if(e.getActionCommand() == "修改密码"){
            ChangePassword cp = new ChangePassword(Sno);  // 学号为Sno的学生修改密码，弹出修改密码界面
        }else if(e.getActionCommand() == "退出系统"){
            System.exit(0);        // 结束程序，所有窗口关闭
        }
    }
}
//————————————————
//
//        版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//        原文链接：https://blog.csdn.net/Mu_yongheng/article/details/107224452
