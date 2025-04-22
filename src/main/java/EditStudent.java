

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class EditStudent
 */
@WebServlet("/EditStudent")
public class EditStudent extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");

    int id = Integer.parseInt(request.getParameter("id"));
    String studentID = request.getParameter("student_id");
    String fullName = request.getParameter("full_name");
    String email = request.getParameter("email");

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579")) {
        String sql = "UPDATE students SET student_id = ?, full_name = ?, email = ? WHERE id = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, studentID);
        pst.setString(2, fullName);
        pst.setString(3, email);
        pst.setInt(4, id);
        pst.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }

    response.sendRedirect("Student");
  }
}

