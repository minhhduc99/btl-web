import dao.ClassroomDAO;
import dao.StudentDAO;

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
      String classroomIdStr = request.getParameter("classroomId");

      if (selectedStudentIds == null || classroomIdStr == null) {
          request.setAttribute("error", "Missing parameters.");
          response.sendRedirect("classroom.jsp?status=failed");
          return;
      }

      try {
          int classroomId = Integer.parseInt(classroomIdStr);
          List<String> studentIds = Arrays.asList(selectedStudentIds);

          ClassroomDAO.assignStudentsToClassroom(studentIds, classroomId);

          response.sendRedirect(request.getContextPath() + "/ViewClassroom?id=" + classroomId);
      } catch (Exception e) {
          e.printStackTrace();
          response.sendRedirect("classroom.jsp?status=failed");
      }
	}

}
