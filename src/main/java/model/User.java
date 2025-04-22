package model;

public class User {
    private String id;
    private String studentID;
    private String fullName;
    private String email;

    public User() {}

    public User(String studentID, String fullName, String email) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.email = email;
    }
    
    public String getID() {
      return id;
    }
    
    public void setID(String id) {
      this.id = id;
    }

    public String getStudentID() {
      return studentID;
    }
    
    public void setStudentID(String studentID) {
      this.studentID = studentID;
    }
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
