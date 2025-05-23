

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Classes;

/**
 * Servlet implementation class Classes
 */
@WebServlet("/Classes")
public class ClassServlet extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    List<Classes> classList = new ArrayList<>();

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579")) {
      String sql = "SELECT * FROM classes";
      PreparedStatement pst = conn.prepareStatement(sql);
      ResultSet rs = pst.executeQuery();

      while (rs.next()) {
        Classes cls = new Classes();
        cls.setID(rs.getInt("id"));
        cls.setClassName(rs.getString("class_name"));
        cls.setClassPeriod(rs.getString("class_period"));
        cls.setTeacherName(rs.getString("teacher_name"));
        classList.add(cls);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    request.setAttribute("classList", classList);
    request.getRequestDispatcher("classes.jsp").forward(request, response);
  }
}

