package student_system;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class ChangePassword extends JFrame implements ActionListener{

    // 定义组件
    JPanel jp_1, jp_2, jp_3, jp_4 = null;
    JLabel jlb_1, jlb_2, jlb_3 = null;
    JTextField jtf = null;  // 用来输入原密码
    JPasswordField jpf_1, jpf_2 = null; // 用来输入新密码和确认密码
    JButton jb_1, jb_2, jb_3 = null;

    String Sno; // 存储学生的学号
    String old_password; // 存储旧密码

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

    // 主函数（实际运行时不需要）
    public static void main(String[] args[]){
        ChangePassword cp = new ChangePassword("muyongheng");
    }

    // 清除文本框内容
    public void clear(){
        jtf.setText("");
        jpf_1.setText("");
        jpf_2.setText("");
    }

    public ChangePassword(String sno){
        Sno = sno;          // 赋值学号

        // 连接数据库（加载驱动、建立连接）
        try {
            Class.forName(driverName);
            ct = DriverManager.getConnection(dbURL, userName, userPwd);
//            ct = DriverManager.getConnection(dbURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jp_1 = new JPanel();
        jp_2 = new JPanel();
        jp_3 = new JPanel();
        jp_4 = new JPanel();

        jlb_1 = new JLabel("原密码");
        jlb_2 = new JLabel("新密码");
        jlb_3 = new JLabel("确认密码");

        jtf = new JTextField(10);
        jpf_1 = new JPasswordField(10);
        jpf_2 = new JPasswordField(10);

        jb_1 = new JButton("确认修改");
        jb_2 = new JButton("退出修改");
        jb_3 = new JButton("重置");

        // 添加监听
        jb_1.addActionListener(this);
        jb_2.addActionListener(this);
        jb_3.addActionListener(this);

        jp_1.add(jlb_1);
        jp_1.add(jtf);

        jp_2.add(jlb_2);
        jp_2.add(jpf_1);

        jp_3.add(jlb_3);
        jp_3.add(jpf_2);

        jp_4.add(jb_1);
        jp_4.add(jb_2);
        jp_4.add(jb_3);


        this.add(jp_1);
        this.add(jp_2);
        this.add(jp_3);
        this.add(jp_4);

        // 设置布局管理器（格网布局）
        this.setLayout(new GridLayout(4,1));
        // 给窗口添加标题
        this.setTitle("修改密码");

        // 设置窗口大小
        this.setSize(500, 300);

        // 设置窗口的起始位置
        //this.setLocation(null);
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
            try {
                ps = ct.prepareStatement("select * from Student where 学号=? ");
                ps.setString(1, Sno);

                rs=ps.executeQuery();
                //取出对应学号学生信息
                while(rs.next()){
                    old_password = rs.getString(3);      //将学生的旧密码取出，用于判断原密码是否正确
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            change_password();
        }else if(e.getActionCommand() == "退出修改"){
            dispose();
        }else if(e.getActionCommand() == "重置"){
            clear();
        }
    }



    public void change_password(){
        // 获得新密码和确认密码
        String new_password_1 = String.valueOf(jpf_1.getPassword());
        String new_password_2 = String.valueOf(jpf_2.getPassword());

        if(old_password.equals(jtf.getText()) && new_password_1.equals(new_password_2) && !new_password_1.isEmpty() && !new_password_2.isEmpty()){
            try{
                String sql="update Student set 密码=? where 学号=? ";//生成一条mysql语句
                ps = ct.prepareStatement(sql);
                ps.setString(1, new_password_1);
                ps.setString(2, Sno);
                int count=ps.executeUpdate();//执行sql语句
                ct.close();
            }catch (SQLException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "修改成功", "提示信息", JOptionPane.WARNING_MESSAGE);
            dispose();
        }else if(jtf.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "请输入原密码！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(!old_password.equals(jtf.getText()))
        {
            JOptionPane.showMessageDialog(null, "原密码错误\n请重新输入！", "提示信息", JOptionPane.ERROR_MESSAGE);
            clear();
        }else if(new_password_1.isEmpty() || new_password_2.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "新密码或者确认密码为空\n请重新输入！", "提示信息", JOptionPane.WARNING_MESSAGE);
        }else if(!new_password_1.equals(new_password_2))
        {
            JOptionPane.showMessageDialog(null, "确认密码与新密码不符\n请重新输入！", "提示信息", JOptionPane.ERROR_MESSAGE);
        }
    }
}
//————————————————
//
//        版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
//
//        原文链接：https://blog.csdn.net/Mu_yongheng/article/details/107224452
