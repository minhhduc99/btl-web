

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

      try {
          con = DBUtil.getConnection();
          PreparedStatement pst = con.prepareStatement(sql);
          pst.setString(1, courseCode);
          pst.setString(2, courseName);
          pst.setString(3, description);
          pst.setInt(4, credits);
          pst.executeUpdate();
      } catch (Exception e) {
          e.printStackTrace();
      }
      response.sendRedirect("Course");
	}

}
