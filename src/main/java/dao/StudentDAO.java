package dao;

import java.sql.*;
import java.util.*;
import model.User;
import utils.DBUtil;

public class StudentDAO {
    public static List<User> getAllStudents() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM students");
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setID(rs.getString("id"));
                u.setStudentID(rs.getString("student_id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<User> getStudentsByClassId(int classId) throws Exception {
      List<User> students = new ArrayList<>();
      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(
             "SELECT u.* FROM students u JOIN student_classes sc ON u.id = sc.student_id WHERE sc.class_id = ?")) {
          ps.setInt(1, classId);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
              User u = new User();
              u.setID(rs.getString("id"));
              u.setStudentID(rs.getString("student_id"));
              u.setFullName(rs.getString("full_name"));
              u.setEmail(rs.getString("email"));
              students.add(u);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return students;
  }
    
    public static List<User> getStudentsWithoutClass() {
      List<User> list = new ArrayList<>();
      try {
          Connection conn = DBUtil.getConnection();
          String sql = "SELECT * FROM students WHERE id NOT IN (SELECT student_id FROM student_classes)";
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
              User u = new User();
              u.setID(rs.getString("id"));
              u.setStudentID(rs.getString("student_id"));
              u.setFullName(rs.getString("full_name"));
              u.setEmail(rs.getString("email"));
              list.add(u);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return list;
  }


}
