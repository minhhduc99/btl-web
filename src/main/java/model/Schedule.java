package model;

public class Schedule {
    private int id;
    private int classId;
    private int courseId;
    private String teacherName;
    private String room;
    private int dayOfWeek;
    private String startTime;
    private String endTime;

    public Schedule() {}

    public Schedule(int classId, int courseId, String teacherName, String room, int dayOfWeek, String startTime, String endTime) {
        this.classId = classId;
        this.courseId = courseId;
        this.teacherName = teacherName;
        this.room = room;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public int getId() {
      return id;
    }
    
    public void setId(int id) {
      this.id = id;
    }

    public int getClassId() {
      return classId;
    }
    
    public void setClassId(int classId) {
      this.classId = classId;
    }
    
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getTeacherName() {
      return teacherName;
    }
  
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
    
    public int getDayOfWeek() {
      return dayOfWeek;
    }
  
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public String getStartTime() {
      return startTime;
    }
  
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
      return endTime;
    }
  
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
