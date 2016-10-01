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

/**
 *
 * @author Daniel
 */
public class CourseDb {
    
    private String driver = "com.mysql.jdbc.Driver";
    private String connUrl = "jdbc:mysql://localhost/";
    private String database = "oakvillerec";
    private String user = "root";
    private String pass = "MySQL";
    //Defining constants
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String START_TIME = "startTime";
    private static final String TABLE_NAME = "course";
    private static final String STUDENTCOURSE_TABLE = "studentcourse";
    private static final String COURSE_ID = "courseId";
    private static final String STUDENT_ID = "studentId";
    //20 student limits constant
    private static final int LIMIT = 3;
   
    
    //add course into course table
    public int addCourse(Course course) throws Exception {  
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        
        String formatSql = "INSERT INTO %s (%s, %s) VALUES (?, ?)";
        String sql = String.format(formatSql, TABLE_NAME, NAME, START_TIME);
        
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, course.getName());
            //Using setTime to match SQL Time format
            ps.setTime(2, course.getStartTime());
            result = ps.executeUpdate();
        } catch(Exception ex) {
            throw(ex);
        } finally {
            //Releasing resources
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;  
    }
    
    public ArrayList<Course> getCoursesFromCourse() throws Exception {  
        //Select all the courses that are not in studentcourse and return it
        String formatSql = "SELECT %s, %s FROM %s WHERE "
                + "%s NOT IN (SELECT %s FROM %s)"; 
        String sql = String.format(formatSql, ID, NAME, 
                TABLE_NAME, ID, COURSE_ID, STUDENTCOURSE_TABLE);
       
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList<>();
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
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
        
    public ArrayList<Course> getCourses() throws Exception {  
        //Select all the courses that are not in studentcourse and return it
        String formatSql = "SELECT %s, %s FROM %s";
        String sql = String.format(formatSql, ID, NAME, TABLE_NAME);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Course> courses = new ArrayList<>();
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
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
    
    public ArrayList<Course> getCoursesBelowLimit() throws Exception {  
        //This will select courses that have less than 20 students from sc table
        String formatSql = "SELECT %s, %s FROM %s INNER JOIN %s"
                + " ON %s = %s GROUP BY %s HAVING COUNT(%s) < ?";
        String sql = String.format(formatSql, ID, NAME, STUDENTCOURSE_TABLE, 
                TABLE_NAME, COURSE_ID, ID, NAME, STUDENT_ID);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
       
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Course> getCourses = getCoursesFromCourse();
        // Concatenating two arraylists into one arraylist
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, LIMIT);
            rs = ps.executeQuery();

            //This will add the courses that have less than 20 students from
            //the studentcourse table
            while(rs.next()) {
               
                Course course = new Course(rs.getInt("id"), 
                       rs.getString("name"));
                courses.add(course);
                
            }
            //This will add the courses from getcourse into the courses array
            //We assume that all courses not in enrollment is under 20 students
            for (Course c : getCourses) {
                courses.add(c);
            }

        } catch(Exception ex) {
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps, rs);
        }
        return courses;
    }
    
    //Removes course if there are no enrolled students
    public int removeCourse(Course course) throws Exception {   
        
        String formatSql = "DELETE FROM %s WHERE %s = ?";
        String sql = String.format(formatSql, TABLE_NAME, ID);
        
        Connection conn = null;
        PreparedStatement ps = null;
        int result = 0;
        StudentDb studentdb = new StudentDb();
        try {
            conn = DBConnector.getConnection(driver, connUrl, database, user, pass);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, course.getId());
            result = ps.executeUpdate();
        } catch(Exception ex) {
            //Shows list of enrolled students with a message
            studentdb.getEnrolledStudentsMessage(course);
            throw(ex);
        } finally {
            DBConnector.closeJDBCObjects(conn, ps);
        }
        return result;
    }
}