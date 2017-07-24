package derbytest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.Date;

public class DerbyTest {

    private static String dbURL = "jdbc:derby://localhost:1527/myDB;create=true;user=me;password=mine";
    private static String tableName = "test";

    private static Connection conn = null;
    private static Statement stmt = null;

    public static void main(String[] args) {
        createConnection();
        insertConfigs(5);
        selectConfig();
        shutdown();
    }

    public static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public static void insertConfigs(int cnt) {
        try {
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder();
            sb.append("insert into ");
            sb.append(tableName);
            sb.append(" values (");
            Date dt = new Date();
            sb.append(dt.getTime());
            for (int i = 1; i < 401; i++) {
                sb.append(", 'tes-column-");
                sb.append(i);
                sb.append("'");
            }
            sb.append(")");
            stmt.execute(sb.toString());
            stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace(System.out);
        }
    }
    
    public static void selectConfig() {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace(System.out);
        }        
    }
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
}
