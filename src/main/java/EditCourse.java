

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Servlet implementation class EditCourse
 */
@WebServlet("/EditCourse")
public class EditCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditCourse() {
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
		int id = Integer.parseInt(request.getParameter("id"));
	    String courseCode = request.getParameter("course_code");
	    String courseName = request.getParameter("course_name");
	    String description = request.getParameter("description");
	    int credits = Integer.parseInt(request.getParameter("credits"));

	    try (Connection conn = DBUtil.getConnection()) {
	        String sql = "UPDATE courses SET course_code = ?, course_name = ?, description = ?, credits = ? WHERE id = ?";
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setString(1, courseCode);
	        pst.setString(2, courseName);
	        pst.setString(3, description);
	        pst.setInt(4, credits);
	        pst.setInt(5, id);
	        pst.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    response.sendRedirect("Course");
	}

}
