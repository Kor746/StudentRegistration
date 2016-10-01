/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_course.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import student_course.Course;
import student_course.Student;

/**
 *
 * @author Daniel
 */
public class StudentDb {
    private String driver = "com.mysql.jdbc.Driver";
    private String connUrl = "jdbc:mysql://localhost/";
    private String database = "oakvillerec";
    private String user = "root";
    private String pass = "MySQL";
    
    private static final String STUDENTCOURSE_TABLE = "studentcourse";
    private static final String COURSE_ID = "courseId";
    private static final String STUDENT_ID = "studentId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final String ID = "id";
    private static final String TABLE_NAME = "student";
    
    
    public int addStudent(Student student) throws Exception { 
        
        String formatSql = "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)";
        String sql = String.format(formatSql, TABLE_NAME, FIRST_NAME, LAST_NAME, AGE);
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getAge());
            
            result = ps.executeUpdate();
        } catch(Exception ex) {
            throw(ex);   
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
    
    public ArrayList<Student> getStudents() throws Exception {
        
        String formatSql = "SELECT * FROM %s";
        String sql = String.format(formatSql, TABLE_NAME);
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Student> students = new ArrayList<>();
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                Student student = new Student(rs.getInt("id"), 
                        rs.getString("firstName"), 
                        rs.getString("lastName"));
                students.add(student);
            }
        } catch(Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        return students;
    }
     
    //This method shows an error message if students are enrolled in
    //course.removeCourse method
    public void getEnrolledStudentsMessage(Course course) throws Exception {    
     
        ArrayList<Student> students = new ArrayList<>();
        //Error message when trying to remove course
        System.out.println("\nError: Students are still enrolled!"
                + "\nThe following student(s) are still enrolled in " 
                + course.getName()
                + ":");
        //This returns the list of enrolled students in the course
        students = getEnrolledStudents(course);
        for (Student s : students) {
            System.out.println(s.getFirstName() 
                    + " " 
                    + s.getLastName());
        }
    }
    
    //This method returns the list of enrolled student in the course back to
    //getEnrolledStudentsMessage method
    public ArrayList<Student> getEnrolledStudents(Course course) throws Exception {
        
        String formatSql = "SELECT * FROM %s INNER JOIN %s ON %s = %s WHERE %s = ?";
        String sql = String.format(formatSql, STUDENTCOURSE_TABLE, 
                TABLE_NAME, STUDENT_ID, ID, 
                COURSE_ID);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Student> students = new ArrayList();
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, course.getId());
            rs = ps.executeQuery();
            while(rs.next()) {
                Student student = new Student(
                        rs.getInt("id"), 
                        rs.getString("firstName"), 
                        rs.getString("lastName"));
                students.add(student);
            }   
        } catch(Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        return students;
    }
    //Removes student from table
    public int removeStudent(Student student) throws Exception { 
        
        String formatSql = "DELETE FROM %s WHERE %s = ?";
        String sql = String.format(formatSql, TABLE_NAME, ID);
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getId());
            result = ps.executeUpdate();
        } catch(Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
}
