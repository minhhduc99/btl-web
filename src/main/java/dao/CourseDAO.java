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
}