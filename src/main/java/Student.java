

import jakarta.servlet.RequestDispatcher;
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

import model.User;

/**
 * Servlet implementation class StudentList
 */
@WebServlet("/Student")
public class Student extends HttpServlet {
    /**
   * 
   */
  private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> studentList = new ArrayList<>();
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
          
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_student_management", "dbadmin", "Abc@13579");
            String sql = "SELECT * FROM students";
            PreparedStatement pst = con.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setID(rs.getString("id"));
                user.setStudentID(rs.getString("student_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                studentList.add(user);
            }
            request.setAttribute("studentList", studentList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("student.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

