package dao;

import java.sql.*;
import java.util.*;

import model.Score;
import utils.DBUtil;

public class ScoreDAO {
    public static void insert(Score score) throws Exception {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "INSERT INTO scores (student_id, course_id, score) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, score.getStudentId());
            ps.setInt(2, score.getCourseId());
            ps.setDouble(3, score.getScore());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(Score score) throws Exception {
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "UPDATE scores SET score = ? WHERE student_id = ? AND course_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, score.getScore());
            ps.setInt(2, score.getStudentId());
            ps.setInt(3, score.getCourseId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Score getScoreByStudentAndCourse(int studentId, int courseId) throws Exception {
        Connection conn = DBUtil.getConnection();
        Score score = null;
        try {
            String sql = "SELECT * FROM scores WHERE student_id = ? AND course_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                score = new Score(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),
                    rs.getDouble("score")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }

    public static List<Score> getScoresByStudentId(int studentId) throws Exception {
        List<Score> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            String sql = "SELECT * FROM scores WHERE student_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Score score = new Score(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),
                    rs.getDouble("score")
                );
                list.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static List<Score> getScoresByClass(int classId) throws Exception {
      List<Score> list = new ArrayList<>();
      Connection conn = DBUtil.getConnection();
      try {
          String sql = "SELECT s.id, s.student_id, s.course_id, s.score " +
                       "FROM scores s " +
                       "JOIN students st ON s.student_id = st.id " +
                       "WHERE st.class_id = ?";
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setInt(1, classId);
          ResultSet rs = ps.executeQuery();
          while (rs.next()) {
              Score score = new Score(
                  rs.getInt("id"),
                  rs.getInt("student_id"),
                  rs.getInt("course_id"),
                  rs.getDouble("score")
              );
              list.add(score);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return list;
  }

    public static void updateOrInsertScore(int studentId, int courseId, int classId, float score) {
    try (Connection conn = DBUtil.getConnection()) {
        String checkSql = "SELECT * FROM scores WHERE student_id = ? AND course_id = ? AND class_id = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, studentId);
        checkStmt.setInt(2, courseId);
        checkStmt.setInt(3, classId);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            String updateSql = "UPDATE scores SET score = ? WHERE student_id = ? AND course_id = ? AND class_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setFloat(1, score);
            updateStmt.setInt(2, studentId);
            updateStmt.setInt(3, courseId);
            updateStmt.setInt(4, classId);
            updateStmt.executeUpdate();
        } else {
            String insertSql = "INSERT INTO scores (student_id, course_id, class_id, score) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, studentId);
            insertStmt.setInt(2, courseId);
            insertStmt.setInt(3, classId);
            insertStmt.setFloat(4, score);
            insertStmt.executeUpdate();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
