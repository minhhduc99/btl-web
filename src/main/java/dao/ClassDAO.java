package dao;

import model.Classes;

import java.sql.*;
import java.util.*;
import utils.DBUtil;


public class ClassDAO {

    public static List<Classes> getAllClasses() {
        List<Classes> list = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Classes c = new Classes();
                c.setID(rs.getInt("id"));
                c.setClassName(rs.getString("class_name"));
                c.setClassPeriod(rs.getString("class_period"));
                c.setTeacherName(rs.getString("teacher_name"));
                list.add(c);
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return list;
    }
    
    public static void assignStudentsToClass(List<String> studentIds, int classId) {
      String insertSQL = "INSERT INTO student_classes (student_id, class_id) VALUES (?, ?) "
                       + "ON DUPLICATE KEY UPDATE class_id = class_id";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

          for (String studentId : studentIds) {
              stmt.setString(1, studentId);
              stmt.setInt(2, classId);
              stmt.addBatch();
          }

          stmt.executeBatch();

      } catch (Exception e) {
          e.printStackTrace();
      }
  }
    
    public static Classes getClassById(int id) throws Exception {
      Classes cls = null;
      String sql = "SELECT * FROM classes WHERE id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) {

          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();

          if (rs.next()) {
            cls = new Classes();
            cls.setID(rs.getInt("id"));
            cls.setClassName(rs.getString("class_name"));
            cls.setClassPeriod(rs.getString("class_period"));
            cls.setTeacherName(rs.getString("teacher_name"));
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

      return cls;
  }
    
    public static boolean removeStudentFromClass(int classId, int studentId) throws Exception {
      String sql = "DELETE FROM student_classes WHERE class_id = ? AND student_id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, classId);
        stmt.setInt(2, studentId);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
        
      } catch (SQLException e) {
        e.printStackTrace();
        return false;
      }
    }

    public static String getClassNameById(int id) throws Exception {
      // TODO Auto-generated method stub
      String class_name = null;
      String sql = "SELECT * FROM classes WHERE id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) {

          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();

          if (rs.next()) {
            class_name = rs.getString("class_name");
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

      return class_name;
    }
    
    public static Classes getClassByStudentId(String studentId) {
      Classes result = null;
      try {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT c.id, c.class_name " +
                     "FROM classes c " +
                     "JOIN student_classes sc ON c.id = sc.class_id " +
                     "WHERE sc.student_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            result = new Classes();
            result.setID(rs.getInt("id"));
            result.setClassName(rs.getString("class_name"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
  }

}