

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;
import utils.DBUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class CourseServlet
 */
@WebServlet("/Course")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	  List<Course> courseList = new ArrayList<>();
      Connection con = null;
      try {
          con = DBUtil.getConnection();
          String sql = "SELECT * FROM courses";
          PreparedStatement pst = con.prepareStatement(sql);

          ResultSet rs = pst.executeQuery();
          
          while (rs.next()) {
              Course course = new Course();
              course.setID(rs.getInt("id"));
              course.setCourseCode(rs.getString("course_code"));
              course.setCourseName(rs.getString("course_name"));
              course.setDescription(rs.getString("description"));
              course.setCredits(rs.getInt("credits"));
              courseList.add(course);
          }
          request.setAttribute("courseList", courseList);
          RequestDispatcher dispatcher = request.getRequestDispatcher("course.jsp");
          dispatcher.forward(request, response);

      } catch (Exception e) {
          e.printStackTrace();
      }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
