

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
 * Servlet implementation class DeleteCourse
 */
@WebServlet("/DeleteCourse")
public class DeleteCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCourse() {
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
	  String[] ids = request.getParameterValues("courseIds");

      if (ids == null || ids.length == 0) {
          response.sendRedirect("Course");
          return;
      }

      try (Connection conn = DBUtil.getConnection()) {

          StringBuilder sql = new StringBuilder("DELETE FROM courses WHERE id IN (");
          for (int i = 0; i < ids.length; i++) {
              sql.append("?");
              if (i < ids.length - 1) {
                  sql.append(",");
              }
          }
          sql.append(")");

          PreparedStatement pst = conn.prepareStatement(sql.toString());

          for (int i = 0; i < ids.length; i++) {
              pst.setInt(i + 1, Integer.parseInt(ids[i]));
          }

          pst.executeUpdate();
      } catch (Exception e) {
          e.printStackTrace();
      }

      response.sendRedirect("Course");
	}

}
