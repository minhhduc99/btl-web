

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import dao.ScoreDAO;

/**
 * Servlet implementation class UpdateScoreServlet
 */
@WebServlet("/UpdateScoreServlet")
public class UpdateScoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateScoreServlet() {
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
	  String classId = request.getParameter("classId");
      String courseId = request.getParameter("courseId");

      if (classId == null || courseId == null) {
          response.sendRedirect("scoreInput.jsp?error=Missing class or course");
          return;
      }

      int classIdInt = Integer.parseInt(classId);
      int courseIdInt = Integer.parseInt(courseId);

      Enumeration<String> parameterNames = request.getParameterNames();
      while (parameterNames.hasMoreElements()) {
          String paramName = parameterNames.nextElement();

          if (paramName.startsWith("score_")) {
              String studentIdStr = paramName.substring(6);
              String scoreValue = request.getParameter(paramName);

              try {
                  int studentId = Integer.parseInt(studentIdStr);
                  float score = Float.parseFloat(scoreValue);

                  ScoreDAO.updateOrInsertScore(studentId, courseIdInt, classIdInt, score);

              } catch (NumberFormatException e) {
                  e.printStackTrace();
              }
          }
      }

      response.sendRedirect("scoreInput.jsp?classId=" + classId + "&courseId=" + courseId + "&edit=false&status=success");
	}

}
