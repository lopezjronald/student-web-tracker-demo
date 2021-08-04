package com.steadfast.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "TestServlet", value = "/TestServlet")
public class TestServlet extends HttpServlet {

    // Define datasource/connection ppol for Resource Injection
    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Step 1: set up the printWriter
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        // Step 2: Get a connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            connection = dataSource.getConnection();
            // Step 3: Create a SQL Statements
            String sql = "SELECT * FROM student";
            statement = connection.createStatement();

            // Step 4: Execute SQL query
            resultSet = statement.executeQuery(sql);

            // Step 5: Process the result set
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                out.println(firstName + " " + lastName);
                out.println(email);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
