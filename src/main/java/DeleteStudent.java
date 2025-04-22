

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
 * Servlet implementation class DeleteStudent
 */
@WebServlet("/DeleteStudent")
public class DeleteStudent extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

//  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] ids = request.getParameterValues("studentIds");

    if (ids == null || ids.length == 0) {
        response.sendRedirect("Student");
        return;
    }

    List<Integer> deletableIds = new ArrayList<>();
    List<String> undeletableStudents = new ArrayList<>();
    
    try (Connection conn = DBUtil.getConnection()) {

      for (String idStr : ids) {
          int id = Integer.parseInt(idStr);
  
          // Check if student in class or not
          String checkSql = "SELECT COUNT(*) FROM student_classes WHERE student_id = ?";
          try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
              checkStmt.setInt(1, id);
              ResultSet rs = checkStmt.executeQuery();
              if (rs.next() && rs.getInt(1) > 0) {
                  String nameSql = "SELECT full_name FROM students WHERE id = ?";
                  try (PreparedStatement nameStmt = conn.prepareStatement(nameSql)) {
                      nameStmt.setInt(1, id);
                      ResultSet nameRs = nameStmt.executeQuery();
                      if (nameRs.next()) {
                          undeletableStudents.add(nameRs.getString("full_name"));
                      }
                  }
              } else {
                  deletableIds.add(id);
              }
          }
      }

        if (!deletableIds.isEmpty()) {
            StringBuilder sql = new StringBuilder("DELETE FROM students WHERE id IN (");
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
    
    if (!undeletableStudents.isEmpty()) {
        HttpSession session = request.getSession();
        session.setAttribute("undeletableStudents", undeletableStudents);
    }
    
    response.sendRedirect("Student");
  }
}

