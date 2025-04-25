

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Classes;
import model.Course;
import model.Score;
import model.User;

import java.io.IOException;
import java.util.List;

import dao.ClassDAO;
import dao.CourseDAO;
import dao.ScoreDAO;
import dao.StudentDAO;

/**
 * Servlet implementation class ViewStudent
 */
@WebServlet("/ViewStudent")
public class ViewStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewStudent() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	  String studentId = request.getParameter("id");

      if (studentId != null) {
          try {
              User student = StudentDAO.getAllStudents().stream()
                      .filter(s -> s.getID().equals(studentId))
                      .findFirst()
                      .orElse(null);
              
              if (student != null) {
                  Classes classOfStudent = ClassDAO.getClassByStudentId(studentId);

                  List<Course> courseList = CourseDAO.getCoursesByStudentId(studentId);

                  List<Score> scoreList = ScoreDAO.getScoresByStudentId(Integer.parseInt(studentId));

                  request.setAttribute("student", student);
                  request.setAttribute("classOfStudent", classOfStudent);
                  request.setAttribute("courseList", courseList);
                  request.setAttribute("scoreList", scoreList);

                  RequestDispatcher dispatcher = request.getRequestDispatcher("viewStudent.jsp");
                  dispatcher.forward(request, response);
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
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
