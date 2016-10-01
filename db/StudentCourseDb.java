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
public class StudentCourseDb {
    
    private String driver = "com.mysql.jdbc.Driver";
    private String connUrl = "jdbc:mysql://localhost/";
    private String database = "oakvillerec";
    private String user = "root";
    private String pass = "MySQL";
    
    //Defining constants
    private static final String TABLE_NAME = "studentcourse";
    private static final String STUDENT_ID = "studentId";
    private static final String COURSE_ID = "courseId";
    private static final String STUDENTID = "student.id";
    private static final String COURSEID = "course.id";
    private static final String COURSE_TABLE = "course";
    private static final String STUDENT_TABLE = "student";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final int LIMIT = 20;
        
    public int enrollStudentCourse(Student student, Course course) throws Exception { 
        
        String formatSql = "INSERT INTO %s (%s, %s) VALUES (?, ?)";
        String sql = String.format(formatSql, TABLE_NAME, 
                STUDENT_ID, COURSE_ID);
       
        //This query retrieves the count of total students in a course
        String formatString = "SELECT COUNT(%s) FROM %s WHERE %s = ?";
        String limit = String.format(formatString, STUDENT_ID, 
                TABLE_NAME, COURSE_ID);
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;
        //row set in order to get the courses below the 20 student limit
        int row;
 
        try {           
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            pstmt = conn.prepareStatement(limit);
            ps.setInt(1, student.getId());
            ps.setInt(2, course.getId());
            pstmt.setInt(1, course.getId());
            //Execute query to obtain the amount of rows
            rs = pstmt.executeQuery();
            rs.next();
            row = rs.getInt(1);
            //Denies user from adding students above the 20 limit in a course
            if(row < LIMIT) {   
                result = ps.executeUpdate();
            } else {  
                System.out.println(course.getName() 
                        + " course reached the maximum capacity of " 
                        + LIMIT 
                        + " students");
            }
        } catch(Exception ex) {  
            //If student is in course already then run descriptive error message
            System.out.println(student.getFirstName() 
                    + " "
                    + student.getLastName()
                    + " is already in the "
                    + course.getName()
                    + " course!");
            throw(ex);
        } finally {  
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
        
    public ArrayList<Course> getEnrolledStudentCourses(Student student) throws Exception {
        
        String formatSql = "SELECT %s, %s FROM %s INNER JOIN %s ON %s = %s WHERE %s = ?";
        String sql = String.format(formatSql, ID, NAME, TABLE_NAME, 
                COURSE_TABLE, COURSE_ID, ID, STUDENT_ID);
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList();
        
        try {  
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getId());
            rs = ps.executeQuery();
            
            while(rs.next()) {
                Course course = new Course(rs.getInt("id"),
                        rs.getString("name"));
                courses.add(course);
            }
        } catch(Exception ex) {  
            throw(ex);      
        } finally {   
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        return courses;
    }
    
    public int unEnrollStudentCourse(Student student, Course course) throws Exception { 
        
        String formatSql = "DELETE FROM %s WHERE %s = ? AND %s = ?";
        String sql = String.format(formatSql, TABLE_NAME, 
                STUDENT_ID, COURSE_ID);
        
        Connection conn = null;
        PreparedStatement ps = null;
        //initialize result
        int result = 0;
        
        try {   
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getId());
            ps.setInt(2, course.getId());
            result = ps.executeUpdate();   
        } catch(Exception ex) {       
            throw(ex);       
        } finally {   
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
    //Unenrolls student from all courses then removes with removeStudent method
    public int unEnrollStudentFromCourse(Student student) throws Exception {   
        
        String formatSql = "DELETE FROM %s WHERE %s = ?";
        String sql = String.format(formatSql, TABLE_NAME, 
                STUDENT_ID);
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
    //return students list based on typed course name
    public ArrayList<Student> getStudentsFromCourse(String courseName) throws Exception {  
        
        String formatSql = "SELECT * FROM %s INNER JOIN %s ON %s = %s "
                + "INNER JOIN %s ON %s = %s "
                + "WHERE %s = ?";
        String sql = String.format(formatSql, TABLE_NAME, 
                STUDENT_TABLE, STUDENT_ID, STUDENTID, 
                COURSE_TABLE, COURSE_ID, COURSEID, NAME);
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Student> students = new ArrayList();
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setString(1, courseName);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                Student student = new Student(
                        rs.getInt("id"), 
                        rs.getString("firstName"), 
                        rs.getString("lastName"));
                students.add(student);    
            }
            //If there are no students in the course
            if(students.isEmpty()) {
                System.out.println("There are no students in " + courseName);
            }  
        } catch(Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        return students;        
    }
}
