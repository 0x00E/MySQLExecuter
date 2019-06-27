package com.github.qianniancc.mysqlexecuter;

import javax.swing.UIManager;

import com.github.qianniancc.mysqlexecuter.frame.MySQLExecuter;

public class Launch {
	
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
		MySQLExecuter.getInstance();
		
		
	}

}