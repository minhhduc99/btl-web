

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Schedule;

import java.io.IOException;

import dao.ScheduleDAO;

/**
 * Servlet implementation class EditSchedule
 */
@WebServlet("/EditSchedule")
public class EditSchedule extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditSchedule() {
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
	  int id = Integer.parseInt(request.getParameter("id"));
//      int classId = Integer.parseInt(request.getParameter("classId"));
//      int courseId = Integer.parseInt(request.getParameter("courseId"));
      String teacherName = request.getParameter("teacherName");
      String room = request.getParameter("room");
      int dayOfWeek = Integer.parseInt(request.getParameter("dayOfWeek"));
      String startTime = request.getParameter("startTime");
      String endTime = request.getParameter("endTime");

      Schedule schedule = null;
      try {
        schedule = ScheduleDAO.getScheduleById(id);
        schedule.setTeacherName(teacherName);
        schedule.setRoom(room);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      ScheduleDAO.updateSchedule(schedule);

      response.sendRedirect("Schedule");
	}

}
