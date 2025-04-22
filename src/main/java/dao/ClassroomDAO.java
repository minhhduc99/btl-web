package dao;

import model.Classroom;
import model.User;

import java.sql.*;
import java.util.*;
import utils.DBUtil;


public class ClassroomDAO {

    public static List<Classroom> getAllClassrooms() {
        List<Classroom> list = new ArrayList<>();
        String sql = "SELECT * FROM classrooms";
        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Classroom c = new Classroom();
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
    
    public static void assignStudentsToClassroom(List<String> studentIds, int classroomId) {
      String insertSQL = "INSERT INTO student_classes (student_id, classroom_id) VALUES (?, ?) "
                       + "ON DUPLICATE KEY UPDATE classroom_id = classroom_id";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

          for (String studentId : studentIds) {
              stmt.setString(1, studentId);
              stmt.setInt(2, classroomId);
              stmt.addBatch();
          }

          stmt.executeBatch();

      } catch (Exception e) {
          e.printStackTrace();
      }
  }
    
    public static Classroom getClassroomById(int id) throws Exception {
      Classroom classroom = null;
      String sql = "SELECT * FROM classrooms WHERE id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) {

          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();

          if (rs.next()) {
              classroom = new Classroom();
              classroom.setID(rs.getInt("id"));
              classroom.setClassName(rs.getString("class_name"));
              classroom.setClassPeriod(rs.getString("class_period"));
              classroom.setTeacherName(rs.getString("teacher_name"));
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

      return classroom;
  }
    
    public static boolean removeStudentFromClass(int classroomId, int studentId) throws Exception {
      String sql = "DELETE FROM student_classes WHERE classroom_id = ? AND student_id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, classroomId);
        stmt.setInt(2, studentId);

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
        
      } catch (SQLException e) {
        e.printStackTrace();
        return false;
      }
    }

    public static String getClassroomNameById(int id) throws Exception {
      // TODO Auto-generated method stub
      String classroom_name = null;
      String sql = "SELECT * FROM classrooms WHERE id = ?";

      try (Connection conn = DBUtil.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql)) {

          ps.setInt(1, id);
          ResultSet rs = ps.executeQuery();

          if (rs.next()) {
            classroom_name = rs.getString("class_name");
          }

      } catch (SQLException e) {
          e.printStackTrace();
      }

      return classroom_name;
    }
}