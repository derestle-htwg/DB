import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class SQLAccess {

	Connection conn = null;
	
	public SQLAccess() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			String url = "jdbc:oracle:thin:@oracle12c.in.htwg-konstanz.de:1521:ora12c"; // String f�r DB-Connection
			conn = DriverManager.getConnection(url, "dbsys37", "dbsys37"); 						// Verbindung erstellen

			conn.setTransactionIsolation(conn.TRANSACTION_SERIALIZABLE); 				// Transaction Isolations-Level setzen
			conn.setAutoCommit(false);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void commit()
	{
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollback()
	{
		try {
			conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execute(String SQLStatement)
	{
		try {
		Statement stmt = conn.createStatement();
		
			stmt.execute(SQLStatement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet getData(String SQLStatement)
	{
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(SQLStatement);
			return stmt.getResultSet();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
