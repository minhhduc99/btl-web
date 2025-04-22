

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Schedule;

import java.io.IOException;

import dao.ScheduleDAO;

/**
 * Servlet implementation class AddScheduleServlet
 */
@WebServlet("/AddSchedule")
public class AddScheduleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddScheduleServlet() {
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
	  int classId = Integer.parseInt(request.getParameter("classId"));
      int courseId = Integer.parseInt(request.getParameter("courseId"));
      String teacherName = request.getParameter("teacherName");
      String room = request.getParameter("room");
      int dayOfWeek = Integer.parseInt(request.getParameter("dayOfWeek"));
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");

      Schedule schedule = new Schedule(classId, courseId, teacherName, room, dayOfWeek, startTime, endTime);
      ScheduleDAO.addSchedule(schedule);

      response.sendRedirect("Schedule");
	}

}
