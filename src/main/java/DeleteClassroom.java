

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
 * Servlet implementation class DeleteClassroom
 */
@WebServlet("/DeleteClassroom")
public class DeleteClassroom extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] ids = request.getParameterValues("classroomIds");

    if (ids == null || ids.length == 0) {
        response.sendRedirect("Classroom");
        return;
    }

    List<Integer> deletableIds = new ArrayList<>();
    List<String> undeletableClassrooms = new ArrayList<>();
    
    try (Connection conn = DBUtil.getConnection()) {

      for (String idStr : ids) {
          int id = Integer.parseInt(idStr);

          // Check if there are students in this classroom
          String checkSql = "SELECT COUNT(*) FROM student_classes WHERE classroom_id = ?";
          try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
              checkStmt.setInt(1, id);
              ResultSet rs = checkStmt.executeQuery();
              if (rs.next() && rs.getInt(1) > 0) {
                  // This classroom still has students => cannot delete
                  String nameSql = "SELECT class_name FROM classrooms WHERE id = ?";
                  try (PreparedStatement nameStmt = conn.prepareStatement(nameSql)) {
                      nameStmt.setInt(1, id);
                      ResultSet nameRs = nameStmt.executeQuery();
                      if (nameRs.next()) {
                          undeletableClassrooms.add(nameRs.getString("class_name"));
                      }
                  }
              } else {
                  // No students, safe to delete
                  deletableIds.add(id);
              }
          }
      }

      // Now delete classrooms that have no students
        if (!deletableIds.isEmpty()) {
            StringBuilder sql = new StringBuilder("DELETE FROM classrooms WHERE id IN (");
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
    
    if (!undeletableClassrooms.isEmpty()) {
        HttpSession session = request.getSession();
        session.setAttribute("undeletableClassrooms", undeletableClassrooms);
    }
    
    response.sendRedirect("Classroom");
  }
}

