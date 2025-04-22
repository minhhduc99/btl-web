package dao;

import model.Classroom;
import model.Schedule;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public static List<Schedule> getAllSchedules() {
        List<Schedule> list = new ArrayList<>();
        String sql = "SELECT sc.id, cl.id AS classId, cs.id AS courseId, sc.teacher_name, sc.room, sc.day_of_week, sc.start_time, sc.end_time " +
                     "FROM schedules sc " +
                     "JOIN classrooms cl ON sc.class_id = cl.id " +
                     "JOIN courses cs ON sc.course_id = cs.id ";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Schedule s = new Schedule();
                s.setId(rs.getInt("id"));
                s.setClassId(rs.getInt("classId"));
                s.setCourseId(rs.getInt("courseId"));
                s.setTeacherName(rs.getString("teacher_name"));
                s.setRoom(rs.getString("room"));
                s.setDayOfWeek(rs.getInt("day_of_week"));
                s.setStartTime(rs.getString("start_time"));
                s.setEndTime(rs.getString("end_time"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void addSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedules (class_id, course_id, teacher_name, room, day_of_week, start_time, end_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, schedule.getClassId());
            stmt.setInt(2, schedule.getCourseId());
            stmt.setString(3, schedule.getTeacherName());
            stmt.setString(4, schedule.getRoom());
            stmt.setInt(5, schedule.getDayOfWeek());
            stmt.setString(6, schedule.getStartTime());
            stmt.setString(7, schedule.getEndTime());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSchedule(int id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSchedule(Schedule s) {
      // TODO Auto-generated method stub
      try (Connection conn = DBUtil.getConnection();
          PreparedStatement stmt = conn.prepareStatement("UPDATE schedules SET class_id=?, course_id=?, teacher_name=?, room=?, day_of_week=?, start_time=?, end_time=? WHERE id=?")) {
         stmt.setInt(1, s.getClassId());
         stmt.setInt(2, s.getCourseId());
         stmt.setString(3, s.getTeacherName());
         stmt.setString(4, s.getRoom());
         stmt.setInt(5, s.getDayOfWeek());
         stmt.setString(6, s.getStartTime());
         stmt.setString(7, s.getEndTime());
         stmt.setInt(8, s.getId());
         stmt.executeUpdate();
     } catch (Exception e) {
         e.printStackTrace();
     }
    }
    
  public static Schedule getScheduleById(int id) throws Exception {
    Schedule schedule = null;
    String sql = "SELECT * FROM schedules WHERE id = ?";

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            schedule = new Schedule();
            schedule.setId(rs.getInt("id"));
            schedule.setClassId(rs.getInt("class_id"));
            schedule.setCourseId(rs.getInt("course_id"));
            schedule.setTeacherName(rs.getString("teacher_name"));
            schedule.setRoom(rs.getString("room"));
            schedule.setDayOfWeek(rs.getInt("day_of_week"));
            schedule.setStartTime(rs.getString("start_time"));
            schedule.setEndTime(rs.getString("end_time"));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return schedule;
  }
}
