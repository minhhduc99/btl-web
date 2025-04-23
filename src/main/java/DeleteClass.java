

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class DeleteClass
 */
@WebServlet("/DeleteClass")
public class DeleteClass extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] ids = request.getParameterValues("classIds");

    if (ids == null || ids.length == 0) {
        response.sendRedirect("Classes");
        return;
    }

    List<Integer> deletableIds = new ArrayList<>();
    List<String> undeletableClasses = new ArrayList<>();

    try (Connection conn = DBUtil.getConnection()) {

      for (String idStr : ids) {
          int id = Integer.parseInt(idStr);

          String checkSql = "SELECT " +
                            "(SELECT COUNT(*) FROM student_classes WHERE class_id = ?) AS student_count, " +
                            "(SELECT COUNT(*) FROM schedules WHERE class_id = ?) AS schedule_count";

          try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
              checkStmt.setInt(1, id);
              checkStmt.setInt(2, id);
              ResultSet rs = checkStmt.executeQuery();

              boolean hasStudents = false;
              boolean hasSchedules = false;

              if (rs.next()) {
                  hasStudents = rs.getInt("student_count") > 0;
                  hasSchedules = rs.getInt("schedule_count") > 0;
              }

              String reason = "";
              if (hasStudents && hasSchedules) {
                  reason = "has students and study schedules.";
              } else if (hasStudents) {
                  reason = "has students.";
              } else if (hasSchedules) {
                  reason = "has study schedules.";
              }

              if (!reason.isEmpty()) {
                  String nameSql = "SELECT class_name FROM classes WHERE id = ?";
                  try (PreparedStatement nameStmt = conn.prepareStatement(nameSql)) {
                      nameStmt.setInt(1, id);
                      ResultSet nameRs = nameStmt.executeQuery();
                      if (nameRs.next()) {
                          undeletableClasses.add(nameRs.getString("class_name") + " - " + reason);
                      }
                  }
              } else {
                  deletableIds.add(id);
              }
          }
      }

      if (!deletableIds.isEmpty()) {
          StringBuilder sql = new StringBuilder("DELETE FROM classes WHERE id IN (");
          for (int i = 0; i < deletableIds.size(); i++) {
              sql.append("?");
              if (i < deletableIds.size() - 1) {
                  sql.append(",");
              }
          }
          sql.append(")");

          try (PreparedStatement deleteStmt = conn.prepareStatement(sql.toString())) {
              for (int i = 0; i < deletableIds.size(); i++) {
                  deleteStmt.setInt(i + 1, deletableIds.get(i));
              }
              deleteStmt.executeUpdate();
          }
      }

    } catch (Exception e) {
        e.printStackTrace();
    }

    if (!undeletableClasses.isEmpty()) {
        HttpSession session = request.getSession();
        session.setAttribute("undeletableClasses", undeletableClasses);
    }

    response.sendRedirect("Classes");
  }
}

