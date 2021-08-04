package com.steadfast.web.jdbc;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StudentControllerServlet", value = "/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

    private StudentDbUtil studentDbUtil;

    @Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();

        // create our student db util ... and pass in the connection pool / datasource
        try {
            studentDbUtil = new StudentDbUtil(dataSource);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            // read the "command" parameter
            String command = request.getParameter("command");

            // if the command is missing, then default to listing students
            if (command == null) {
                command = "LIST";
            }

            // route to the appropriate method
            switch (command) {
                case "LIST":
                    listStudents(request, response);
                    break;
                case "ADD":
                    addStudent(request, response);
                    break;
                case "LOAD":
                    loadStudent(request, response);
                    break;
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                default:
                    listStudents(request, response);
            }

            // list the students ... in MVC fashion
            listStudents(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read the student info from form data
        String id = request.getParameter("studentId");

        // delete student from the database
        studentDbUtil.deleteStudent(id);

        // send back to "list students" page
        listStudents(request, response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read teh student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student student = new Student(id, firstName, lastName, email);

        // perform update on database
        studentDbUtil.updateStudent(student);

        // send them back to the "list students" page
        listStudents(request, response);
    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{

        // read student id from data
        String studentId = request.getParameter("studentId");

        // get student from database (db util)
        Student student = studentDbUtil.getStudent(studentId);

        // place student in the request attribute
        request.setAttribute("student", student);

        // send to jsp page: update-student-form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);

    }

    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // read student info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("firstName");
        String email = request.getParameter("email");

        // create a new student object
        Student student = new Student(firstName, lastName, email);

        // add the student to the database
        studentDbUtil.addStudent(student);

        // send back to main page (the student list)
        listStudents(request, response);

    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // get students from db util
        List<Student> students = studentDbUtil.getStudents();

        // add students to the request
        request.setAttribute("students", students);

        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
