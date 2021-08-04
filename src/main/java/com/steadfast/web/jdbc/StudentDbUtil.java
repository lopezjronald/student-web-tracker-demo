package com.steadfast.web.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDbUtil {

    private final DataSource dataSource;

    public StudentDbUtil(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Student> getStudents() throws Exception {
        List<Student> students = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // get a connection
            connection = dataSource.getConnection();

            // create the SQL statement
            String sql = "SELECT * FROM student ORDER BY last_name";
            statement = connection.createStatement();

            // execute query
            resultSet = statement.executeQuery(sql);

            // process result set
            while (resultSet.next()) {

                // retrieve data from result set row
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                // create new student object
                Student student = new Student(id, firstName, lastName, email);

                // add it to the list of students
                students.add(student);

            }
            return students;

        } finally {

            // close JDBC objects
            close(connection, statement, resultSet);
        }

    }

    private void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student student) throws Exception {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // get db connection
            connection = dataSource.getConnection();

            // create sql for insert
            String sql = "INSERT INTO student (first_name, last_name, email) " +
                    "VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);

            // set the param values for the student
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getEmail());

            // execute sql insert
            statement.execute();

        } finally {
            // clean up JDBC objects
            close(connection, statement, null);
        }


    }

    public Student getStudent(String studentId) throws Exception {
        Student student = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id;

        try {
            // convert student id to an int
            id = Integer.parseInt(studentId);

            // get connection to database
            connection = dataSource.getConnection();

            // create sql to get selected student
            String sql = "SELECT * FROM STUDENT WHERE ID=?";

            // create prepared statement
            statement = connection.prepareStatement(sql);

            // set params
            statement.setInt(1, id);

            // execute statement
            resultSet = statement.executeQuery();

            // retrieve data from result set row
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                // use the student Id during construction
                student = new Student(id, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student ID: " + id);
            }

            return student;

        } finally {
            close(connection, statement, resultSet);
        }

    }

    public void updateStudent(Student student) throws Exception {

        Connection connection = null;
        PreparedStatement statement = null;

        try {

            // get db connection
            connection = dataSource.getConnection();

            // create SQL update statement
            String sql = "UPDATE student SET first_name=?, last_name=?, email=? WHERE id=?";

            // prepare statement
            statement = connection.prepareStatement(sql);

            // set params
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getEmail());
            statement.setInt(4, student.getId());

            // execute SQL statement
            statement.execute();

        } finally {
            close(connection, statement, null);
        }


    }

    public void deleteStudent(String id) throws Exception{
        Connection connection = null;
        PreparedStatement statement = null;
        try {

            // convert student id to int
            int studentId = Integer.parseInt(id);

            // get connection to database
            connection = dataSource.getConnection();

            // create the sql to delete student
            String sql = "DELETE FROM student WHERE id=?";

            // prepare statement
            statement = connection.prepareStatement(sql);

            // set parameters
            statement.setInt(1, studentId);

            // execute sql statement
            statement.execute();

        } finally {
            close(connection, statement, null);
        }
    }
}
