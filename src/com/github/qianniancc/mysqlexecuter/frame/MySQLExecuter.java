package com.github.qianniancc.mysqlexecuter.frame;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class MySQLExecuter extends JFrame implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private Connection conn;
	private JScrollPane jsp;
	private JTextArea textArea;
	private static MySQLExecuter instance;

	public MySQLExecuter() {
		try {
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "数据库连接失败：\n"+e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return;	
		}
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setTitle("SQLRunner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 450);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		textArea = new JTextArea();
		textArea.setFont(new Font("微软雅黑", Font.BOLD, 14));
		textArea.setBounds(39, 82, 1024, 768);
		setVisible(true);
			
	}

	@Override
	public void actionPerformed(ActionEvent arg0){
		String sql=textArea.getText();
		if(!sql.toLowerCase().startsWith("select")){
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				int updateLineCount=(ps.executeUpdate());
				JOptionPane.showMessageDialog(this, "执行成功，影响行数："+updateLineCount, "提示", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "SQL语句发生错误：\n"+e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			}
			
			return;
		}
		
		try {
			Vector<String> columns=new Vector<>();
			Vector<Vector<String>> values=new Vector<>();
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs=null;
			try {
				rs = ps.executeQuery();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "SQL语句发生错误：\n"+e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			 ResultSetMetaData rsmd=rs.getMetaData();
			 
			 for(int i=1;i<=rsmd.getColumnCount();i++){
				 columns.add(rsmd.getColumnName(i)+"("+
						 rsmd.getColumnTypeName(i)+")"+
						 "\t");
				 System.out.print(rsmd.getColumnName(i)+"("+
						 rsmd.getColumnTypeName(i)+")"+
						 "\t");
			 }
			 System.out.println();
			 
			 while(rs.next()){
				 Vector<String> tmp2=new Vector<String>();
				 for(int i=1;i<=rsmd.getColumnCount();i++){
					 System.out.print(rs.getString(i)+"\t");
					 tmp2.add(rs.getString(i));
				 }
				 values.add(tmp2);
				 System.out.println();
			 }
			 
			 try {
				contentPane.remove(jsp);
			} catch (Exception e1) {
			}			 
			 table = new JTable(values,columns); 
			 
			jsp=new JScrollPane(table);
			
			contentPane.add(jsp,BorderLayout.SOUTH);
			contentPane.updateUI();
			contentPane.repaint();
			contentPane.validate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
	}

	public static MySQLExecuter getInstance() {
		if(instance==null)instance=new MySQLExecuter();
		return instance;
	}
}
