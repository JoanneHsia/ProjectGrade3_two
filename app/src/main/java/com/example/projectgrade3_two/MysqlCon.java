package com.example.projectgrade3_two;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlCon {
    String mysql_ip = "192.168.137.1";// 電腦ip 自己cmd打ipconfig Ipv4位置
    int mysql_port = 3306; // Port 預設為 3306
    String db_name = "test"; //自己資料庫collection名字
    String url = "jdbc:mysql://"+mysql_ip+":"+mysql_port+"/"+db_name;

    String db_user = "test"; //'root'@'localhost'
    //ALTER USER 'test'@'LAPTOP-UF2JJMM9' IDENTIFIED WITH mysql_native_password BY 'A123456789a!';
    //GRANT ALL ON *.* TO 'test'@'LAPTOP-UF2JJMM9';
    //FLUSH PRIVILEGES;
    String db_password = "A123456789a!";

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB","1ok");
        }catch( ClassNotFoundException e) {
            Log.e("DB","1err");
            return;
        }

        // 連接資料庫
        try {
            Connection con = DriverManager.getConnection(url,db_user,db_password);
            Log.v("DB","2ok");
        }catch(SQLException e) {
            Log.e("DB","2err");
            Log.e("DB", e.toString());
        }
    }

    public String getData() {
        String data = "";
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM test";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next())
            {
                String id = rs.getString("user_id");
                String name = rs.getString("hos_id");
                data += id + "\n" + name;
            }
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
