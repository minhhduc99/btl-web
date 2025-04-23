import dao.ClassDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * SStudentimplementation class AssignStudentToClass
 */
@WebServlet("/AssignStudentToClass")
public class AssignStudentToClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignStudentToClass() {
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
	  String[] selectedStudentIds = request.getParameterValues("selectedStudents");
      String classIdStr = request.getParameter("classId");

      if (selectedStudentIds == null || classIdStr == null) {
          request.setAttribute("error", "Missing parameters.");
          response.sendRedirect("classes.jsp?status=failed");
          return;
      }

      try {
          int classId = Integer.parseInt(classIdStr);
          List<String> studentIds = Arrays.asList(selectedStudentIds);

          ClassDAO.assignStudentsToClass(studentIds, classId);

          response.sendRedirect(request.getContextPath() + "/ViewClass?id=" + classId);
      } catch (Exception e) {
          e.printStackTrace();
          response.sendRedirect("classes.jsp?status=failed");
      }
	}

}
