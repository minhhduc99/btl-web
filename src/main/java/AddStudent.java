

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
      String sql = "INSERT INTO students (student_id, full_name, email) VALUES (?, ?, ?)";

      try {
          Class.forName("com.mysql.cj.jdbc.Driver");
          
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
          PreparedStatement pst = con.prepareStatement(sql);
          pst.setString(1, studentID);
          pst.setString(2, fullName);
          pst.setString(3, email);
          pst.executeUpdate();
      } catch (Exception e) {
          e.printStackTrace();
      }
      response.sendRedirect("Student");
    }
}

