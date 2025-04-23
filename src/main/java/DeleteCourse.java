

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
	        StringBuilder checkSql = new StringBuilder("SELECT COUNT(*) FROM schedules WHERE course_id IN (");
	        for (int i = 0; i < ids.length; i++) {
	            checkSql.append("?");
	            if (i < ids.length - 1) {
	                checkSql.append(",");
	            }
	        }
	        checkSql.append(")");

	        PreparedStatement checkPst = conn.prepareStatement(checkSql.toString());
	        for (int i = 0; i < ids.length; i++) {
	            checkPst.setInt(i + 1, Integer.parseInt(ids[i]));
	        }

	        var rs = checkPst.executeQuery();
	        int count = 0;
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }

	        if (count > 0) {
	            response.sendRedirect("Course?error=course_in_use");
	            return;
	        }

	        StringBuilder deleteSql = new StringBuilder("DELETE FROM courses WHERE id IN (");
	        for (int i = 0; i < ids.length; i++) {
	            deleteSql.append("?");
	            if (i < ids.length - 1) {
	                deleteSql.append(",");
	            }
	        }
	        deleteSql.append(")");

	        PreparedStatement deletePst = conn.prepareStatement(deleteSql.toString());
	        for (int i = 0; i < ids.length; i++) {
	            deletePst.setInt(i + 1, Integer.parseInt(ids[i]));
	        }

	        deletePst.executeUpdate();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    response.sendRedirect("Course");
	}

}
