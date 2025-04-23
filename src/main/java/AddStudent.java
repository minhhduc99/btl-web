

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class AddStudent
 */
@WebServlet("/AddStudent")
public class AddStudent extends HttpServlet {
    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      String studentID = request.getParameter("student_id");
      String fullName = request.getParameter("full_name");
      String email = request.getParameter("email");

      Connection con = null;
      PreparedStatement pst = null;
      PreparedStatement checkPst = null;
      ResultSet rs = null;

      String insertSQL = "INSERT INTO students (student_id, full_name, email) VALUES (?, ?, ?)";
      String checkSQL = "SELECT * FROM students WHERE student_id = ? OR email = ?";

      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          con = DBUtil.getConnection();

          // Check if student_id or email already exists
          checkPst = con.prepareStatement(checkSQL);
          checkPst.setString(1, studentID);
          checkPst.setString(2, email);
          rs = checkPst.executeQuery();

          if (rs.next()) {
              // If exists, raise error
              response.sendRedirect("Student?error=info_already_exists");
          } else {
              // If not exist, add student
              pst = con.prepareStatement(insertSQL);
              pst.setString(1, studentID);
              pst.setString(2, fullName);
              pst.setString(3, email);
              pst.executeUpdate();
              response.sendRedirect("Student");
          }

      } catch (Exception e) {
          e.printStackTrace();
          throw new ServletException(e);
      } finally {
          try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (checkPst != null) checkPst.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
          try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
      }
    }
}

