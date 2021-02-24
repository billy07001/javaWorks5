//賴鴻運 107403552 資管三B
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.protocol.Resultset;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JTextField;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.ImageIcon;

public class Main extends JFrame{

	private static JPanel contentPane;
	private static JTable table;
	  long firstClick=0;
	  long secondClick=0;
	  boolean click=false;

	
	//建立資料庫連線
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/member?serverTimezone=UTC";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	
	//預設基本SQL指令
	private static final String DEFAULT_QUERY = "SELECT name FROM people";
	private static resultsettablemodel tablemodel;
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	public Main() {
		try {
			tablemodel = new resultsettablemodel(DATABASE_URL, USERNAME, PASSWORD, DEFAULT_QUERY);
			int test = tablemodel.getColumnCount();
			}
		
		
		catch (SQLException sqlException) {
			JOptionPane.showMessageDialog(null, sqlException.getMessage(),"Database error", JOptionPane.ERROR_MESSAGE);
		    tablemodel.disconnectFromDatabase();
		    System.exit(1);
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 370, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel function = new JPanel();
		contentPane.add(function, BorderLayout.NORTH);
		GridBagLayout gbl_function = new GridBagLayout();
		gbl_function.columnWidths = new int[]{149, 46, 0};
		gbl_function.rowHeights = new int[]{0, 15, 0};
		gbl_function.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_function.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		function.setLayout(gbl_function);
		
		JLabel Contacts = new JLabel("Contacts");
		Contacts.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
		GridBagConstraints gbc_Contacts = new GridBagConstraints();
		gbc_Contacts.insets = new Insets(0, 0, 5, 5);
		gbc_Contacts.anchor = GridBagConstraints.NORTHWEST;
		gbc_Contacts.gridx = 0;
		gbc_Contacts.gridy = 0;
		function.add(Contacts, gbc_Contacts);
		
		JButton addnew = new JButton();
		addnew.setIcon(new ImageIcon("D:\\eclipse-workplace\\HW7_107403552\\src\\plus.png"));
		addnew.setBorder(null);
		addnew.setContentAreaFilled(false);
		
		GridBagConstraints gbc_addnew = new GridBagConstraints();
		gbc_addnew.insets = new Insets(0, 0, 5, 0);
		gbc_addnew.gridx = 1;
		gbc_addnew.gridy = 0;
		function.add(addnew, gbc_addnew);
		
		JTextArea SearchName = new JTextArea();
		GridBagConstraints gbc_SearchName = new GridBagConstraints();
		gbc_SearchName.insets = new Insets(0, 0, 0, 5);
		gbc_SearchName.fill = GridBagConstraints.BOTH;
		gbc_SearchName.gridx = 0;
		gbc_SearchName.gridy = 1;
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		SearchName.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		function.add(SearchName, gbc_SearchName);
		
		JButton Search = new JButton("search");
		GridBagConstraints gbc_Search = new GridBagConstraints();
		gbc_Search.gridx = 1;
		gbc_Search.gridy = 1;
		function.add(Search, gbc_Search);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		table = new JTable(tablemodel);
		panel.add(table);
		//新增滑鼠雙擊事件
		table.addMouseListener(new MouseListener()
        {
          public void mouseClicked(MouseEvent e)
          {
            int clickCount = e.getClickCount();
             if(click==false)
             {
               firstClick=new Date().getTime();
               click=true;
             }
             else if(click==true)
             {
               secondClick=new Date().getTime();
               click=false;
             }
             if (Math.abs((secondClick-firstClick)) < 200 && (secondClick-firstClick) > 30)
             {
                 try {
                	 
                	Point p = e.getPoint();
                	 
                    int row = table.rowAtPoint(p);
                    int column = table.columnAtPoint(p);
                    
                    String namerow = (String) tablemodel.getValueAt(row,column);
                    String phoneQuery = "SELECT phone FROM people WHERE name = '"+namerow+"';";
					String phonenum = (String) tablemodel.returnQuery(phoneQuery, row, column);
					
					String typeQuery = "SELECT type FROM people WHERE name = '"+namerow+"';";
					String type = (String) tablemodel.returnQuery(typeQuery, row, column);
	                 
	                String[] options = {"Update", "Delete"};
	                 
	                int x = JOptionPane.showOptionDialog(null, 
	                		                              type+" : "+phonenum,
	                		                              namerow,
	                		                              JOptionPane.DEFAULT_OPTION,
	                		                              JOptionPane.INFORMATION_MESSAGE, 
	                		                              null, 
	                		                              options, 
	                		                              null);
	                if (x == 0) {Update update = new Update(tablemodel,namerow);
	                                    update.setVisible(true);
	                            }
	                else if (x == 1) {
	                	String deleteQuery = "DELETE FROM people WHERE name = '"+namerow+"'";
	                	System.out.print(deleteQuery);
	                	tablemodel.setupdateQuery(deleteQuery);
	                	tablemodel.setQuery("SELECT name FROM people");
	                }
	                
				} catch (IllegalStateException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 
                  //雙擊事件
             }  
          }
          public void mousePressed(MouseEvent e)
          {
            if(click==false)
            {
              firstClick=new Date().getTime();
              click=true;
            }
            else if(click==true)
            {
              secondClick=new Date().getTime();
              click=false;
            }
            if (Math.abs((secondClick-firstClick)) < 200 && (secondClick-firstClick) > 30)
            {
            	try {
               	 
                	Point p = e.getPoint();
                	 
                    int row = table.rowAtPoint(p);
                    int column = table.columnAtPoint(p);
                    
                    String namerow = (String) tablemodel.getValueAt(row,column);
                    String phoneQuery = "SELECT phone FROM people WHERE name = '"+namerow+"';";
					String phonenum = (String) tablemodel.returnQuery(phoneQuery, row, column);
					
					String typeQuery = "SELECT type FROM people WHERE name = '"+namerow+"';";
					String type = (String) tablemodel.returnQuery(typeQuery, row, column);
	                 
	                String[] options = {"Update", "Delete"};
	                 
	                int x = JOptionPane.showOptionDialog(null, 
	                		                              type+" : "+phonenum,
	                		                              namerow,
	                		                              JOptionPane.DEFAULT_OPTION,
	                		                              JOptionPane.INFORMATION_MESSAGE, 
	                		                              null, 
	                		                              options, 
	                		                              null);
	                if (x == 0) {Update update = new Update(tablemodel,namerow);
	                                    update.setVisible(true);
	                            }
	                else if (x == 1) {
	                	String deleteQuery = "DELETE FROM people WHERE name = '"+namerow+"'";
	                	System.out.print(deleteQuery);
	                	tablemodel.setupdateQuery(deleteQuery);
	                	tablemodel.setQuery("SELECT name FROM people");
	                }
	                
				} catch (IllegalStateException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
              //雙擊事件
            }
          }

          public void mouseReleased(MouseEvent e)
          {
          }

          public void mouseEntered(MouseEvent e)
          {
          }

          public void mouseExited(MouseEvent e)
          {
          }
        });
		
		//新增按鈕actionlistener
		addnew.addActionListener(e -> {
			Insert insert = new Insert(tablemodel);
			insert.setVisible(true);
		});
		
		
		Search.addActionListener(e -> {
			String select = SearchName.getText();
			String selectquery = "SELECT name FROM people WHERE name LIKE '%"+select+"%';";
			try {
				tablemodel.setQuery(selectquery);
			} catch (IllegalStateException | SQLException e1) {
				e1.printStackTrace();
			}
			
		});
		
	}

}
