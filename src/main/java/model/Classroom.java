package model;

public class Classroom {
  private int id;
  private String class_name;
  private String class_period;
  private String teacher_name;

  // Getters and Setters
  public Classroom() {}

  public Classroom(String class_name, String class_period, String teacher_name) {
      this.class_name = class_name;
      this.class_period = class_period;
      this.teacher_name = teacher_name;
  }
  
  public int getID() {
    return id;
  }
  
  public void setID(int id) {
    this.id = id;
  }

  public String getClassName() {
    return class_name;
  }
  
  public void setClassName(String class_name) {
    this.class_name = class_name;
  }
  
  public void setClassPeriod(String class_period) {
    this.class_period = class_period;
  }
  
  public String getClassPeriod() {
      return class_period;
  }

  public void setTeacherName(String teacher_name) {
      this.teacher_name = teacher_name;
  }
  
  public String getTeacherName() {
    return teacher_name;
}

}
