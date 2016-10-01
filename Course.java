/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student_course;

import java.sql.Time;


/**
 *
 * @author Daniel
 */
public class Course {
    
    private int id;
    private String name;
    private Time startTime;
    
    //Default empty ctor
    public Course() {
        
    }
    
    public Course(int id, String name) { 
        this.id = id;
        this.name = name;
    }
    public Course(int id, String name, Time startTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
    }
    
    public Course(String name, Time startTime) {
        this.name = name;
        this.startTime = startTime;
    }
    //setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    
}
