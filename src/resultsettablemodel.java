// Fig. 24.25: ResultSetTableModel.java
// A TableModel that supplies ResultSet data to a JTable.
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

// ResultSet rows and columns are counted from 1 and JTable 
// rows and columns are counted from 0. When processing 
// ResultSet rows or columns for use in a JTable, it is 
// necessary to add 1 to the row or column number to manipulate
// the appropriate ResultSet column (i.e., JTable column 0 is 
// ResultSet column 1 and JTable row 0 is ResultSet row 1).
public class resultsettablemodel extends AbstractTableModel 
{
   private Connection connection;
   private Statement statement;
   private ResultSet resultSet1;
   private ResultSetMetaData metaData1;
   private ResultSetMetaData metaData2;
   private int numberOfRows;

   // keep track of database connection status
   private boolean connectedToDatabase = false;
   
   // constructor initializes resultSet and obtains its meta data object;
   // determines number of rows
   public resultsettablemodel(String url, String username,
      String password, String query) throws SQLException
   {         
      // connect to database
      connection = DriverManager.getConnection(url, username, password);

      // create Statement to query database
      statement = connection.createStatement(
         ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY);

      // update database connection status
      connectedToDatabase = true;

      // set query and execute it
      setQuery(query);
   } 

   // get class that represents column type
   public Class getColumnClass(int column) throws IllegalStateException
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine Java class of column
      try 
      {
         String className = metaData1.getColumnClassName(column + 1);
         
         // return Class object that represents className
         return Class.forName(className);
      }
      catch (Exception exception) 
      {
         exception.printStackTrace();
      } 
      
      return Object.class; // if problems occur above, assume type Object
   } 

   // get number of columns in ResultSet
   public int getColumnCount() throws IllegalStateException
   {   
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine number of columns
      try 
      {
         return metaData1.getColumnCount(); 
         
      }
      catch (SQLException sqlException) 
      {
         sqlException.printStackTrace();
      } 
      
      return 0; // if problems occur above, return 0 for number of columns
   } 

   // get name of a particular column in ResultSet
   public String getColumnName(int column) throws IllegalStateException
   {    
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // determine column name
      try 
      {
         return metaData1.getColumnName(column + 1);  
      } 
      catch (SQLException sqlException) 
      {
         sqlException.printStackTrace();
      } 
      
      return ""; // if problems, return empty string for column name
   } 

   // return number of rows in ResultSet
   public int getRowCount() throws IllegalStateException
   {      
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");
 
      return numberOfRows;
   }

   // obtain value in particular row and column
   public Object getValueAt(int row, int column) 
      throws IllegalStateException
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // obtain a value at specified ResultSet row and column
      try 
      {
         resultSet1.absolute(row + 1);
         return resultSet1.getObject(column + 1);
      }
      catch (SQLException sqlException) 
      {
         sqlException.printStackTrace();
      } 
      
      return ""; // if problems, return empty string object
   } 
   
   // set new database query string
   public void setQuery(String query) 
      throws SQLException, IllegalStateException 
   {
      // ensure database connection is available
      if (!connectedToDatabase) 
         throw new IllegalStateException("Not Connected to Database");

      // specify query and execute it
      resultSet1 = statement.executeQuery(query);

      // obtain meta data for ResultSet
      metaData1 = resultSet1.getMetaData();

      // determine number of rows in ResultSet
      resultSet1.last(); // move to last row
      numberOfRows = resultSet1.getRow(); // get row number      
      
      // notify JTable that model has changed
      fireTableStructureChanged();
   } 
   
   
   // set new database query string
   public void setupdateQuery(String query) 
      throws SQLException, IllegalStateException 
   {
	      // ensure database connection is available
	      if (!connectedToDatabase) 
	         throw new IllegalStateException("Not Connected to Database");

          Statement statement2 = connection.createStatement(
        	         ResultSet.TYPE_SCROLL_INSENSITIVE,
        	         ResultSet.CONCUR_READ_ONLY);
	      int resultSet2 = statement2.executeUpdate(query); 

	      if(resultSet2 != -1) {
	      fireTableStructureChanged();
	      }
   } 
   
   //to search in database and return result
   public Object returnQuery(String query,int row,int column) 
		      throws SQLException, IllegalStateException 
		   {
		      // ensure database connection is available
		      if (!connectedToDatabase) 
		         throw new IllegalStateException("Not Connected to Database");

		      Statement statement3 = connection.createStatement(
	        	         ResultSet.TYPE_SCROLL_INSENSITIVE,
	        	         ResultSet.CONCUR_READ_ONLY);

		      ResultSet resultSet3 = statement3.executeQuery(query);
		      resultSet3.absolute(row + 1);
		      resultSet3.last(); // move to last row
		      return resultSet3.getObject(column + 1);
		   }

   // close Statement and Connection               
   public void disconnectFromDatabase()            
   {              
      if (connectedToDatabase)                  
      {
         // close Statement and Connection            
         try                                          
         {                                            
            resultSet1.close();  
            statement.close();                        
            connection.close();                       
         }                                  
         catch (SQLException sqlException)          
         {                                            
            sqlException.printStackTrace();           
         }                              
         finally  // update database connection status
         {                                            
            connectedToDatabase = false;              
         }                             
      } 
   }         
} // end class ResultSetTableModel

