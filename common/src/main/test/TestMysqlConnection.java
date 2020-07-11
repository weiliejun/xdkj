import java.sql.*;

public class TestMysqlConnection {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mbiger?useSSL=false&useTimezone=true&serverTimezone=GMT%2B8"
                    , "mbiger", "mbiger");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from USER_INFO");

            while (rs.next()) {
                System.out.println(" user_name:" + rs.getString("USER_NAME") + " mobile:"
                        + rs.getString("MOBILE"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
