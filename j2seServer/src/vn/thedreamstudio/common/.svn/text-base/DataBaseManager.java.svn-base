package vn.thedreamstudio.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;

public class DataBaseManager {

    private static Connection conn = null;
    private static BasicDataSource ds;
    static {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("");
        ds.setUrl("jdbc:mysql://localhost/mydatabase");
        ds.setInitialSize(4);
    }
    
    public static Connection getPoolingConnection() throws SQLException {
        return ds.getConnection();
    }
    
    
    public synchronized static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost/";
            String username = "root";
            String password = "";// mat khau cua ban
            String dbName = "mydatabase";// ten csdl ma ban can ket noi
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url + dbName, username, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
        }
        return null;
    }

//    static {
//        try {
//            String url = "jdbc:mysql://localhost/";
//            String username = "root";
//            String password = "";// mat khau cua ban
//            String dbName = "mydatabase";// ten csdl ma ban can ket noi
//            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection(url + dbName, username, password);
//        } catch (Exception ex) {
//            System.err.println("Không kết nối được với csdl");
//            ex.printStackTrace();
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                    System.out.println("Đóng kết nối");
//                } catch (Exception ex) {
//                }
//            }
//        }
//    }
    private DataBaseManager() {
    }

    public static void doConnect() {
    }

    public void Close() throws Exception {
        conn.close();
    }
//    preparedStatement.setString(1, "Test");
//			preparedStatement.setString(2, "TestEmail");
//			preparedStatement.setString(3, "TestWebpage");
//			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//			preparedStatement.setString(5, "TestSummary");
//			preparedStatement.setString(6, "TestComment");
//			preparedStatement.executeUpdate();
//
//			preparedStatement = connect
//					.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
//			resultSet = preparedStatement.executeQuery();
//			writeResultSet(resultSet);
//    preparedStatement = connect
//			.prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
//			preparedStatement.setString(1, "Test");
//			preparedStatement.executeUpdate();

}
