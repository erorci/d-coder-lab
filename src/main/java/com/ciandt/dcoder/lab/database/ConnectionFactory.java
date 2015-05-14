package com.ciandt.dcoder.lab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:sqlserver://209.208.78.46;user=dcoders;password=Suporte123;database=db_card");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
