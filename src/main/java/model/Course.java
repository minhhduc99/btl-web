package model;

public class Course {
    private int id;
    private String courseCode;
    private String courseName;
    private String description;
    private int credits;

    public Course() {}

    public Course(String courseCode, String courseName, String description, int credits) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
    }
    
    public int getID() {
      return id;
    }
    
    public void setID(int id) {
      this.id = id;
    }

    public String getCourseCode() {
      return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
      this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCredits() {
      return credits;
    }
  
    public void setCredits(int credits) {
        this.credits = credits;
    }
}
