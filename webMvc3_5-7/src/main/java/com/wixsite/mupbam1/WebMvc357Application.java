package com.wixsite.mupbam1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebMvc357Application {

	public static void main(String[] args) {
		SpringApplication.run(WebMvc357Application.class, args);
		String url = "jdbc:postgresql://localhost:5432/photo_album";
		String user = "postgres";
		String password = "bestuser";
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connection successfull!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
