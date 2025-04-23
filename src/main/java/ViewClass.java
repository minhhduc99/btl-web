

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Classes;
import model.User;

import java.io.IOException;
import java.util.List;

import dao.ClassDAO;
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
          try {
            studentList = StudentDAO.getStudentsByClassId(classId);
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          request.setAttribute("class", cls);
          request.setAttribute("studentList", studentList);
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
