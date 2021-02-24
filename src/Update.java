import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Update extends JFrame {

	private JPanel contentPane;
	private JTextField nametext;
	private JTextField phonetext;
	
	private String type;
	

	public Update(resultsettablemodel tablemodel,String name) {

		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{424, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		
		JLabel namelabel = new JLabel("Name:");
		panel_1.add(namelabel);
		
		nametext = new JTextField();
		panel_1.add(nametext);
		nametext.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		contentPane.add(panel_2, gbc_panel_2);
		
		JLabel typelabel = new JLabel("Type:");
		panel_2.add(typelabel);
		
		JComboBox typecomboBox = new JComboBox();
		typecomboBox.addItem("home");
		typecomboBox.addItem("cell");
		typecomboBox.addItem("company");
		typecomboBox.addActionListener(
		         new ActionListener()
		         {
		            @Override
		            public void actionPerformed(ActionEvent e) 
		            {
		               switch (typecomboBox.getSelectedIndex()) {
		            	  
		               case 0 :
		            	   type = "home";
		            	   break;
		               case 1 :
		            	   type = "cell";
		            	   break;
		               case 2 :
		            	   type = "company";
		            	   break;
		               }
		            } 
		         }
		      );
		panel_2.add(typecomboBox);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		contentPane.add(panel_3, gbc_panel_3);
		
		JLabel phonelabel = new JLabel("Phone:");
		panel_3.add(phonelabel);
		
		phonetext = new JTextField();
		panel_3.add(phonetext);
		phonetext.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 6;
		contentPane.add(panel, gbc_panel);
		
		JButton complete = new JButton("\u5B8C\u6210");
		panel.add(complete);
		
		complete.addActionListener(        
	            new ActionListener() 
	            {
	               public void actionPerformed(ActionEvent event)
	               {
	                  // perform a new query
	                  try 
	                  {
	                	 String updatequery =
	                	"UPDATE people SET name='"+nametext.getText()+"',type='"+type+"',phone='"+phonetext.getText()+"' WHERE name = '"+name+"';\r\n";	
	                	 
	                     tablemodel.setupdateQuery(updatequery);
	                     tablemodel.setQuery("SELECT name FROM people");
	                  }
	                  catch (SQLException sqlException) 
	                  {
	                     JOptionPane.showMessageDialog(null, 
	                        sqlException.getMessage(), "Database error", 
	                        JOptionPane.ERROR_MESSAGE);               
	                  } 
	               } 
	            }         
	         );
	}

}

