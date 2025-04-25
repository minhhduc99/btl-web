<%@ page import="java.util.*, model.*, dao.*" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  List<Schedule> scheduleList = (List<Schedule>) request.getAttribute("scheduleList");
  List<Course> courseList = CourseDAO.getAllCourses();
  List<Classes> classList = ClassDAO.getAllClasses();
%>

<%! 
    private String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            case 8: return "Sunday";
            default: return "Unknown";
        }
    }
%>


<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Schedule Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
  
  <style>
  	body {
      background: #f7f9fc;
    }
    .navbar-custom {
      background: linear-gradient(90deg, #4e54c8, #8f94fb);
    }
    .navbar-custom .navbar-brand,
    .navbar-custom .nav-link,
    .navbar-custom .navbar-text {
      color: white;
    }
    .welcome-box {
      margin-top: 60px;
      padding: 40px;
      background-color: white;
      box-shadow: 0 0 15px rgba(0,0,0,0.1);
      border-radius: 15px;
      text-align: center;
    }
    .welcome-box h1 {
      color: #4e54c8;
      font-weight: bold;
    }
  </style>
</head>
<body>
  <!-- NAVBAR -->
<nav class="navbar navbar-expand-lg navbar-custom shadow-sm">
  <div class="container">
    <a class="navbar-brand" href="<%= request.getContextPath() %>/">Student Management</a>
    <div class="collapse navbar-collapse justify-content-end">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Student" aria-expanded="false">
            Student
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Classes" aria-expanded="false">
            Class
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Course" aria-expanded="false">
            Course
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Schedule" aria-expanded="false">
            Schedule
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/Score/Input" aria-expanded="false">
          	Score
          </a>
        </li>
      </ul>
      <ul class="navbar-nav mb-2 mb-lg-0">
        <li class="nav-item me-3">
          <span class="navbar-text">
            <i class="bi bi-person-circle me-1"></i> Welcome, <%= session.getAttribute("full_name") != null ? session.getAttribute("full_name") : "User" %>
          </span>
        </li>
        <li class="nav-item">
          <a class="btn btn-light btn-sm" href="<%= request.getContextPath() %>/Logout"><i class="bi bi-box-arrow-right"></i> Logout</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

  <!-- CONTENT -->
  <div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
	  <h2 class="mb-0">Schedules List</h2>
	  <div>
		  <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addScheduleModal">
		    <i class="bi bi-calendar-plus"></i> Add Schedule
		  </button>
	  	<button id="deleteSelectedBtn" class="btn btn-danger me-2" disabled onclick="deleteSelectedSchedules()">
	      <i class="bi bi-trash"></i> Delete Schedule
	    </button>
	  </div>
	</div>

    <% 
	    if (scheduleList != null && !scheduleList.isEmpty()) { 
	  %>
	    <form id="deleteForm" action="DeleteSchedule" method="post">
	      <table class="table table-bordered table-striped">
	        <thead class="table-dark">
	          <tr>
	            <th><input type="checkbox" id="selectAllCheckbox"></th>
	            <th>NO</th>
	            <th>Class</th>
	            <th>Course</th>
	            <th>Teacher</th>
	            <th>Room</th>
	            <th>Day</th>
	            <th>Start</th>
	            <th>End</th>
	            <th>Actions</th>
	          </tr>
	        </thead>
	        <tbody>
	          <%
	            int index = 1;
	            for (Schedule sc : scheduleList) {
	          %>
	          <tr>
	            <td><input type="checkbox" class="scheduleCheckbox" name="scheduleIds" value="<%= sc.getId() %>"></td>
	            <td><%= index++ %></td>
	            <td><%= ClassDAO.getClassNameById(sc.getClassId()) %></td>
	            <td><%= CourseDAO.getCourseNameById(sc.getCourseId()) %></td>
	            <td><%= sc.getTeacherName() %></td>
	            <td><%= sc.getRoom() %></td>
	            <td><%= getDayName(sc.getDayOfWeek()) %></td>
	            <td><%= sc.getStartTime() %></td>
	            <td><%= sc.getEndTime() %></td>
	            <td>
	              <button type="button" class="btn btn-warning btn-sm"
					  onclick="openEditModal(
					  	'<%= sc.getId() %>', 
					  	'<%= sc.getClassId() %>',
					  	'<%= ClassDAO.getClassNameById(sc.getClassId()) %>',  
					  	'<%= sc.getCourseId() %>', 
					  	'<%= CourseDAO.getCourseNameById(sc.getCourseId()) %>', 
					  	'<%= sc.getTeacherName() %>', 
					  	'<%= sc.getRoom() %>', 
					  	'<%= sc.getDayOfWeek() %>', 
					  	'<%= sc.getStartTime() %>', 
					  	'<%= sc.getEndTime() %>')">
					  <i class="bi bi-pencil"></i>
					</button>
	            </td>
	          </tr>
	          <% } %>
	        </tbody>
	      </table>
	    </form>
	  <% } else { %>
	    <div class="alert alert-warning">No schedules found.</div>
	  <% } %>
	</div>
	
	<!-- Add Schedule Modal -->
	<div class="modal fade" id="addScheduleModal" tabindex="-1" aria-labelledby="addScheduleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddSchedule" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addScheduleModalLabel">Add New Schedule</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
          <div class="mb-3">
            <label for="classId" class="form-label">Class</label>
            <select class="form-select" id="classId" name="classId" required>
              <option value="">Select Class</option>
              <% 
                if (classList != null) {
                  for (Classes c : classList) {
              %>
                  <option value="<%= c.getID() %>"><%= c.getClassName() %></option>
              <% 
                  }
                }
              %>
            </select>
          </div>

          <div class="mb-3">
            <label for="courseId" class="form-label">Course</label>
            <select class="form-select" id="courseId" name="courseId" required>
              <option value="">Select Course</option>
              <% 
                if (courseList != null) {
                  for (Course course : courseList) {
              %>
                  <option value="<%= course.getID() %>"><%= course.getCourseName() %></option>
              <% 
                  }
                }
              %>
            </select>
          </div>

          <div class="mb-3">
            <label for="teacherName" class="form-label">Teacher</label>
            <input type="text" class="form-control" id="teacherName" name="teacherName" required>
          </div>

          <div class="mb-3">
            <label for="room" class="form-label">Room</label>
            <input type="text" class="form-control" id="room" name="room" required>
          </div>

          <div class="mb-3">
            <label for="dayOfWeek" class="form-label">Day of Week</label>
            <select class="form-select" id="dayOfWeek" name="dayOfWeek" required>
              <option value="">Select Day</option>
              <option value="2">Monday</option>
              <option value="3">Tuesday</option>
              <option value="4">Wednesday</option>
              <option value="5">Thursday</option>
              <option value="6">Friday</option>
              <option value="7">Saturday</option>
              <option value="8">Sunday</option>
            </select>
          </div>

          <div class="row">
            <div class="col-md-6 mb-3">
              <label for="startTime" class="form-label">Start Time</label>
              <input type="time" class="form-control" id="startTime" name="startTime" required>
            </div>
            <div class="col-md-6 mb-3">
              <label for="endTime" class="form-label">End Time</label>
              <input type="time" class="form-control" id="endTime" name="endTime" required>
            </div>
          </div>

        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-success">Add Schedule</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<!-- Edit Schedule Modal -->
	<div class="modal fade" id="editScheduleModal" tabindex="-1" aria-labelledby="editScheduleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form id="editScheduleForm" action="EditSchedule" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="editScheduleModalLabel">Edit Schedule</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	
	          <input type="hidden" id="editId" name="id">
	          <input type="hidden" id="editClassIdHidden" name="classId">
	          <input type="hidden" id="editCourseIdHidden" name="courseId">
	
	          <div class="mb-3">
	            <label for="editClassId" class="form-label">Class</label>
	            <input type="text" class="form-control" id="editClassId" disabled>
	          </div>
	
	          <div class="mb-3">
	            <label for="editCourseId" class="form-label">Course</label>
	            <input type="text" class="form-control" id="editCourseId" disabled>
	          </div>
	
	          <div class="mb-3">
	            <label for="editTeacherName" class="form-label">Teacher Name</label>
	            <input type="text" class="form-control" id="editTeacherName" name="teacherName" required>
	          </div>
	
	          <div class="mb-3">
	            <label for="editRoom" class="form-label">Room</label>
	            <input type="text" class="form-control" id="editRoom" name="room" required>
	          </div>
	
	          <div class="mb-3">
	            <label for="editDayOfWeek" class="form-label">Day of Week</label>
	            <select class="form-select" id="editDayOfWeek" name="dayOfWeek" required>
	              <option value="2">Monday</option>
	              <option value="3">Tuesday</option>
	              <option value="4">Wednesday</option>
	              <option value="5">Thursday</option>
	              <option value="6">Friday</option>
	              <option value="7">Saturday</option>
	              <option value="8">Sunday</option>
	            </select>
	          </div>
	
	          <div class="row">
	            <div class="col-md-6 mb-3">
	              <label for="editStartTime" class="form-label">Start Time</label>
	              <input type="time" class="form-control" id="editStartTime" name="startTime" required>
	            </div>
	            <div class="col-md-6 mb-3">
	              <label for="editEndTime" class="form-label">End Time</label>
	              <input type="time" class="form-control" id="editEndTime" name="endTime" required>
	            </div>
	          </div>
	
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Update Schedule</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<script>
	  const selectAllCheckbox = document.getElementById('selectAllCheckbox');
	  const scheduleCheckboxes = document.getElementsByClassName('scheduleCheckbox');
	  const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
	
	  // Get "Select All" event
	  selectAllCheckbox.addEventListener('change', function() {
	    for (let checkbox of scheduleCheckboxes) {
	      checkbox.checked = selectAllCheckbox.checked;
	    }
	    toggleDeleteButton();
	  });
	
	  // Get each checkbox event
	  for (let checkbox of scheduleCheckboxes) {
		  checkbox.addEventListener('change', function() {
		    if (!this.checked) {
		      selectAllCheckbox.checked = false;
		    } else {
		      const allChecked = Array.from(scheduleCheckboxes).every(cb => cb.checked);
		      selectAllCheckbox.checked = allChecked;
		    }
		    toggleDeleteButton();
		  });
		}

	
	  function toggleDeleteButton() {
	    const anyChecked = Array.from(scheduleCheckboxes).some(cb => cb.checked);
	    deleteSelectedBtn.disabled = !anyChecked;
	  }
	
	  // Delete Selected
	  function deleteSelectedSchedules() {
	    if (confirm('Are you sure you want to delete the selected schedules?')) {
	      document.getElementById('deleteForm').submit();
	    }
	  }
	</script>
	
	<script>
	  function openEditModal(id, classId, className, courseId, courseName, teacherName, room, dayOfWeek, startTime, endTime) {
	    document.getElementById('editId').value = id;
	    document.getElementById('editClassId').value = className;
	    document.getElementById('editClassIdHidden').value = classId;
	
	    document.getElementById('editCourseId').value = courseName;
	    document.getElementById('editCourseIdHidden').value = courseId;
	
	    document.getElementById('editTeacherName').value = teacherName;
	    document.getElementById('editRoom').value = room;
	    document.getElementById('editDayOfWeek').value = dayOfWeek;
	    document.getElementById('editStartTime').value = startTime;
	    document.getElementById('editEndTime').value = endTime;
	
	    var editModal = new bootstrap.Modal(document.getElementById('editScheduleModal'));
	    editModal.show();
	  }
	</script>
	
  

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
