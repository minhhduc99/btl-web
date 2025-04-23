

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class AddCourse
 */
@WebServlet("/AddCourse")
public class AddCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCourse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
	  String courseCode = request.getParameter("course_code");
      String courseName = request.getParameter("course_name");
      String description = request.getParameter("description");
      int credits = Integer.parseInt(request.getParameter("credits"));

      Connection con = null;
      String sql = "INSERT INTO courses (course_code, course_name, description, credits) VALUES (?, ?, ?, ?)";
      
      String checkSQL = "SELECT * FROM courses WHERE course_code=? OR course_name=?";
      
      PreparedStatement pst = null;
      PreparedStatement checkPst = null;
      ResultSet rs = null;

      try {
        con = DBUtil.getConnection();
        checkPst = con.prepareStatement(checkSQL);
        checkPst.setString(1, courseCode);
        checkPst.setString(2, courseName);
        rs = checkPst.executeQuery();

        if (rs.next()) {
            // If exists, raise error
            response.sendRedirect("Course?add_error=info_already_exists");
        } else {
            // If not exist, add student
            pst = con.prepareStatement(sql);
            pst.setString(1, courseCode);
            pst.setString(2, courseName);
            pst.setString(3, description);
            pst.setInt(4, credits);
            pst.executeUpdate();
            response.sendRedirect("Course");
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (checkPst != null) checkPst.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
      }
	}

}
