

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.ClassroomDAO;

/**
 * Servlet implementation class RemoveStudentFromClass
 */
@WebServlet("/RemoveStudentFromClass")
public class RemoveStudentFromClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveStudentFromClass() {
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
	    String[] studentIds = request.getParameterValues("studentIds");
	    int classroomId = Integer.parseInt(request.getParameter("classroomId"));

	    boolean success = false;
      
	    if (studentIds != null) {
	        for (String idStr : studentIds) {
	            int studentId = Integer.parseInt(idStr);
	            boolean removed = false;
              try {
                removed = ClassroomDAO.removeStudentFromClass(classroomId, studentId);
              } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
	            if (!removed) {
	                success = false;
	            }
	        }
	    } else {
	        success = false;
	    }

	    if (success) {
	        request.setAttribute("message", "Student(s) removed successfully");
	    } else {
	        request.setAttribute("error", "Failed to remove student(s)");
	    }

	    response.sendRedirect("ViewClassroom?id=" + classroomId);
	}

}
