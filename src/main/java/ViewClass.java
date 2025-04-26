

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Classes;
import model.Course;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import dao.ClassDAO;
import dao.CourseDAO;
import dao.ScoreDAO;
import dao.StudentDAO;

/**
 * Servlet implementation class ViewClass
 */
@WebServlet("/ViewClass")
public class ViewClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	  String idStr = request.getParameter("id");
	  
      if (idStr != null) {
          int classId = Integer.parseInt(idStr);
          Classes cls = null;
          try {
            cls = ClassDAO.getClassById(classId);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          List<User> studentList = null;
          List<Course> courses = null;
          Map<String, Double> scoreMap = null;
          
          try {
            studentList = StudentDAO.getStudentsByClassId(classId);
            courses = CourseDAO.getCoursesByClassId(classId);
            scoreMap = ScoreDAO.getScoresByClass(classId);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          request.setAttribute("class", cls);
          request.setAttribute("studentList", studentList);
          request.setAttribute("courses", courses);
          request.setAttribute("scoreMap", scoreMap);
          request.getRequestDispatcher("viewClass.jsp").forward(request, response);
      } else {
          response.sendRedirect("class.jsp?status=notfound");
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
