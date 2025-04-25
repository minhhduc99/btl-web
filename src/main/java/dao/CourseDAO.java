package dao;

import model.Course;
import model.User;

import java.sql.*;
import java.util.*;
import utils.DBUtil;


public class CourseDAO {

    public static List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Course c = new Course();
                c.setID(rs.getInt("id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseName(rs.getString("course_name"));
                c.setDescription(rs.getString("description"));
                c.setCredits(rs.getInt("credits"));
                list.add(c);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return list;
    }
    
    public static String getCourseNameById(int id) throws Exception {
      // TODO Auto-generated method stub
      String course_name = null;
      String sql = "SELECT * FROM courses WHERE id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) {

          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();

          if (rs.next()) {
            course_name = rs.getString("course_name");
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

      return course_name;
    }

    public static List<Course> getCoursesByClassId(int classId) throws Exception {
      List<Course> courses = new ArrayList<>();
      Connection conn = DBUtil.getConnection();
      try {
          String sql = "SELECT c.id, c.course_code, c.course_name FROM courses c " +
                       "JOIN schedules s ON c.id = s.course_id " +
                       "WHERE s.class_id = ?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setInt(1, classId);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
              Course c = new Course();
              c.setID(rs.getInt("id"));
              c.setCourseCode(rs.getString("course_code"));
              c.setCourseName(rs.getString("course_name"));
              courses.add(c);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return courses;
  }

    public static List<Course> getCoursesByStudentId(String studentId) throws Exception {
      List<Course> courseList = new ArrayList<>();
 
      try (Connection conn = DBUtil.getConnection()) {
          String classSql = "SELECT class_id FROM student_classes WHERE student_id = ?";
          PreparedStatement classPst = conn.prepareStatement(classSql);
          classPst.setString(1, studentId);
          ResultSet classRs = classPst.executeQuery();
          
          while (classRs.next()) {
              int classId = classRs.getInt("class_id");
              
              String courseSql = "SELECT c.id, c.course_name " +
                                 "FROM courses c " +
                                 "JOIN schedules s ON c.id = s.course_id " +
                                 "WHERE s.class_id = ?";
              PreparedStatement coursePst = conn.prepareStatement(courseSql);
              coursePst.setInt(1, classId);
              ResultSet courseRs = coursePst.executeQuery();
              
              while (courseRs.next()) {
                  Course course = new Course();
                  course.setID(courseRs.getInt("id"));
                  course.setCourseName(courseRs.getString("course_name"));
                  courseList.add(course);
              }
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      
      return courseList;
  }

}