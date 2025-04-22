

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Classroom;
import model.User;

import java.io.IOException;
import java.util.List;

import dao.ClassroomDAO;
import dao.StudentDAO;

/**
 * Servlet implementation class ViewClassroom
 */
@WebServlet("/ViewClassroom")
public class ViewClassroom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewClassroom() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	  String idStr = request.getParameter("id");
      if (idStr != null) {
          int classId = Integer.parseInt(idStr);
          Classroom classroom = null;
          try {
            classroom = ClassroomDAO.getClassroomById(classId);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          List<User> studentList = null;
          try {
            studentList = StudentDAO.getStudentsByClassroomId(classId);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          request.setAttribute("classroom", classroom);
          request.setAttribute("studentList", studentList);
          request.getRequestDispatcher("viewClassroom.jsp").forward(request, response);
      } else {
          response.sendRedirect("classroom.jsp?status=notfound");
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
