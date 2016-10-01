/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_course;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import student_course.db.CourseDb;
import student_course.db.StudentCourseDb;
import student_course.db.StudentDb;

/**
 * I wish you luck in trying to break my program Professor!:)
 * 
 * @author Daniel
 */
public class Student_Course {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        menu();    
    }
    
    private static void menu() {
        //menu system
        String menu = ("***************MENU**************\n"
            + "Press 1 to add a Course: \n"
            + "Press 2 to add a student: \n"
            + "Press 3 to enroll a Student: \n"
            + "Press 4 to un-enroll a Student from a Course: \n"
            + "Press 5 to remove a course: \n"
            + "Press 6 to remove a student: \n"
            + "Press 7 to list students given a course name: \n"
            + "Press 0 to exit \n"    
            + "*********************************** \n"
            + "Enter your choice: ");
        
        Scanner input = new Scanner(System.in);
        System.out.println(menu);
        String selection = input.nextLine().trim();
        while(!selection.equals("0")) {
            switch(selection) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    addStudent();
                    break;
                case "3":
                    enrollStudentCourses();
                    break;
                case "4":
                    unEnrollStudentCourse();
                    break;
                case "5":
                    removeCourse();
                    break;
                case "6":
                    removeStudent();
                    break;
                case "7":
                    listStudentsFromCourse();
                    break;    
                default:
                    System.out.println("Wrong input, try again!");
                    break;
            }
            //repeats menu with selection
            System.out.println(menu);
            selection = input.nextLine().trim();
        }   
    }
    //This validates that the input is not empty
    private static String getName() {    
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a course name:");
        String name = input.nextLine().trim();
        
        while(name.equals("")) {
            System.out.println("Please enter a course name:");
            name = input.nextLine().trim();
        }
        return name;
    }
    //This validates that the input is not empty
    private static String getStartTime() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a start time:");
        String startTime = input.nextLine().trim();
        
        while(startTime.equals("")) {
            System.out.println("Please enter a start time:");
            startTime = input.nextLine().trim();
        }
        return startTime;
    }
            
    private static void addCourse() {
        
        int result;
        CourseDb coursedb = new CourseDb();
        String name = getName();
        String startTime = getStartTime();

        try {
            //Convert string to time object
            Time time = Time.valueOf(startTime);
            Course course = new Course(name, time);
            result = coursedb.addCourse(course);
            if(result > 0) {
                System.out.println("\nCourse: " 
                        + name 
                        + " was added for "
                        + time);
            } else {
                System.out.println("\nCourse: " 
                        + name 
                        + " was not added for "
                        + time);
            }
        } catch(Exception ex) {
           System.out.println("\nTime must be in hh:mm:ss format!\n"
                   + ex.toString());
        }        
    }
    //This validates that the input is not empty
    private static String getFirstName() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a first name:");
        String firstName = input.nextLine().trim();
        //validate first name
        while(firstName.equals("")) {   
            System.out.println("Please enter a first name:");
            firstName = input.nextLine().trim();
        }
        return firstName;
    }
    //This validates that the input is not empty
    private static String getLastName() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter a last name:");
        String lastName = input.nextLine().trim();
        while(lastName.equals("")) {    
            System.out.println("Please enter a last name:");
            lastName = input.nextLine().trim();
        }
        return lastName;
    }
    //validate empty input
    private static String getAge() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter an age:");
        String age = input.nextLine().trim();
        
        while(age.equals("")) {  
            System.out.println("Please enter an age:");
            age = input.nextLine().trim();
        }
        return age;
    }
    
    private static void addStudent() {
        
        int result;
        int age;
        StudentDb studentdb = new StudentDb();
        String firstName = getFirstName();
        String lastName = getLastName();
        String vAge = getAge();
        
        try {
            //Provide handling for mismatch input and array out of bound
            age = Integer.parseInt(vAge);
            Student student = new Student(firstName, lastName, age);
            result = studentdb.addStudent(student);
            if(result > 0) {  
                System.out.println("\nStudent: " 
                        + firstName 
                        + " " 
                        + lastName 
                        + ", age " 
                        + age
                        + " was added");
            } else {
                System.out.println("\nStudent: " 
                        + firstName
                        + " "
                        + lastName
                        + ", age "
                        + age
                        + " was not added");
            }
        } catch(Exception ex) {
            System.out.println("Please enter a numeric age\n" 
                    + ex.toString());
        }     
    }
    
    private static ArrayList<Student> listStudents() {
        
        StudentDb studentdb = new StudentDb();
        ArrayList<Student> students = new ArrayList<>();
        
        try {
            students = studentdb.getStudents();
            for (int i = 0; i < students.size(); i++) {
                String format = "%d: %s %s";
                String output = String.format(format, 
                        i,
                        students.get(i).getFirstName(),
                        students.get(i).getLastName());
                System.out.println(output);
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return students; 
    }
    
    private static ArrayList<Course> listCourses() {
        
        CourseDb coursedb = new CourseDb();
        ArrayList<Course> courses = new ArrayList<>();
        
        try {
          courses = coursedb.getCoursesBelowLimit();
          
          for (int i = 0; i < courses.size(); i++) {
              String format = "%d: %s";
              String output = String.format(format, 
                      i, 
                      courses.get(i).getName());
              System.out.println(output);
          }
        } catch(Exception ex) { 
            System.out.println(ex.toString());
        }
        return courses;
    }
   
    private static void enrollStudentCourses() {
        
        System.out.println("Enroll a Student in a Course:");
        int result;
        int studentPosition;
        int coursePosition;
        String studentPos;
        StudentCourseDb studentcoursedb = new StudentCourseDb();
        ArrayList<Student> students;
        ArrayList<Course> courses;
        
        try {
            students = listStudents();
            studentPos = getStudent();
            studentPosition =  Integer.parseInt(studentPos); 
            Student student = students.get(studentPosition);
            
            courses = listCourses();
            String coursePos = getCourse();
            coursePosition = Integer.parseInt(coursePos);
            Course course = courses.get(coursePosition);
            result = studentcoursedb.enrollStudentCourse(student, course);
            
            if(result > 0) {
                System.out.println(student.getFirstName() 
                        + " " 
                        + student.getLastName()
                        + " has successfully enrolled into "
                        + course.getName());
            } else {    
                System.out.println(student.getFirstName() 
                        + " "
                        + student.getLastName()
                        + " did not enroll into "
                        + course.getName());
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    private static String getStudent() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Choose student by id: ");
        String position = input.nextLine().trim();
        
        while(position.equals("")) {
            listStudents();
            System.out.println("ENTER A VALID NUMBER:");
            position = input.nextLine().trim();
        }
        return position;
    }
    //validates empty inut and return index of chosen course
    private static String getCourse() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Choose course by id: ");
        String position = input.nextLine().trim();
        
        while(position.equals("")) {
            listCourses();
            System.out.println("ENTER A VALID NUMBER:");
            position = input.nextLine().trim();   
        }
        return position;
    }
    
    //Retrieves the enrolled courses of chosen student
    private static ArrayList<Course> listEnrolledStudentCourses(Student student) {
        
        StudentCourseDb studentcoursedb = new StudentCourseDb();
        ArrayList<Course> courses = new ArrayList<>();
        
        try {
            //This grabs array of courses from studentcoursedb
            courses = studentcoursedb.getEnrolledStudentCourses(student);
            //formats the string with a pseudo index and displays course list
            for (int i = 0; i < courses.size(); i++) {
                String format = "%d: %s";
                String output = String.format(format,
                        i,
                        courses.get(i).getName());
                System.out.println(output);
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return courses;
    }
    
    //validates for empty input and returns index of chosen student
    private static String getEnrolledStudent() {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a student by id: ");
        String position = input.nextLine().trim();
        
        while(position.equals("")) {
            listStudents();
            System.out.println("ENTER A VALID NUMBER:");
            position = input.nextLine().trim();
        }
        return position;  
    }
    //validates for empty input and returns index of chosen course
    private static String getEnrolledCourse(Student student) {
        
        Scanner input = new Scanner(System.in);
        System.out.println("Choose a course by id: ");
        String position = input.nextLine().trim();
        
        while(position.equals("")) {
            listEnrolledStudentCourses(student);
            System.out.println("ENTER A VALID NUMBER:");
            position = input.nextLine().trim();
        } 
        return position;
    }
    
    private static void unEnrollStudentCourse() {
        
        System.out.println("Unenroll student from a course:");
        StudentCourseDb studentcoursedb = new StudentCourseDb();
        int result;
        int studentPosition;
        int coursePosition;
        String studentPos;
        ArrayList<Course> courses;
        ArrayList<Student> students;
        
        try {
            //Make a call to retrieve list of student 
            students = listStudents();
            //Method call to put validated input and get position of student
            studentPos = getEnrolledStudent();
            studentPosition = Integer.parseInt(studentPos);
            Student student = students.get(studentPosition);
            
            //Passes in one student to get his/her enrolled courses
            courses = listEnrolledStudentCourses(student);
            //Passes in chosen student to get position in list
            String coursePos = getEnrolledCourse(student);
            //Parses string position to int position
            coursePosition = Integer.parseInt(coursePos);
            //Grabs the chosen course by it's position in ArrayList
            Course course = courses.get(coursePosition);
            result = studentcoursedb.unEnrollStudentCourse(student, course);
            
            if(result > 0) {  
                System.out.println(student.getFirstName() 
                        + " " 
                        + student.getLastName()
                        + " has successfully unenrolled from "
                        + course.getName());
            } else {
                System.out.println(student.getFirstName() 
                        + " "
                        + student.getLastName()
                        + " did not unenroll from "
                        + course.getName());
            }        
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }   
    }
    
    private static void removeCourse() {
        
        System.out.println("Removing a course");
        CourseDb coursedb = new CourseDb();
        ArrayList<Course> courses;
        String coursePos;
        int coursePosition;
        int result;
        
        try {
            courses = listCourses();
            coursePos = getCourse();
            coursePosition = Integer.parseInt(coursePos);
            Course course = courses.get(coursePosition);
            result = coursedb.removeCourse(course);
            
            if(result > 0) {
                System.out.println(course.getName() 
                        + " was removed");
            } 
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    private static void removeStudent() {
        
        System.out.println("Removing a student");
        StudentDb studentdb = new StudentDb();
        StudentCourseDb studentcoursedb = new StudentCourseDb();
        ArrayList<Student> students;
        String studentPos;
        int studentPosition;
        int result;
        
        try {   
            students = listStudents();
            studentPos = getCourse();
            studentPosition = Integer.parseInt(studentPos);
            Student student = students.get(studentPosition);
            studentcoursedb.unEnrollStudentFromCourse(student);
            result = studentdb.removeStudent(student);
            if(result > 0) {
                System.out.println(student.getFirstName() 
                        + " " 
                        + student.getLastName()
                        + " was removed");
            } else {
                System.out.println(student.getFirstName() 
                        + " " 
                        + student.getLastName()
                        + " was not removed");
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
    private static String getCoursesByName() {
        
        Scanner input = new Scanner(System.in);
        listAllCourses();
        System.out.println("Choose course by name: ");
        String courseName = input.nextLine().trim();
        
        while(courseName.equals("")) {
            listAllCourses();
            System.out.println("ENTER A VALID NAME:");
            courseName = input.nextLine().trim();
        }
        return courseName;
    }
    
    private static ArrayList<Course> listAllCourses() {
        
        CourseDb coursedb = new CourseDb();
        ArrayList<Course> courses = new ArrayList<>();
        
        try {
          courses = coursedb.getCourses();
          
          for (int i = 0; i < courses.size(); i++) {
              String format = "%d: %s";
              String output = String.format(format, 
                      i, 
                      courses.get(i).getName());
              System.out.println(output);
          }
        } catch(Exception ex) { 
            System.out.println(ex.toString());
        }
        return courses;
    }
    
    private static void listStudentsFromCourse() {
        
        System.out.println("List students from course name");
        ArrayList<Student> students;
        StudentCourseDb studentcoursedb = new StudentCourseDb();
        String courseName;
        
        try {
            courseName = getCoursesByName();
            students = studentcoursedb.getStudentsFromCourse(courseName);
            for (int i = 0; i < students.size(); i++) {
                String format = "%d: %s %s";
                String output = String.format(format,
                        i,
                        students.get(i).getFirstName(), 
                        students.get(i).getLastName());
                System.out.println(output);
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }   
}
