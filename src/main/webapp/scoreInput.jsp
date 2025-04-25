<%@ page import="java.util.*, model.*, dao.*" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  List<Classes> classList = ClassDAO.getAllClasses();
%>

<%
  String editMode = request.getParameter("edit");
  boolean isEditing = "true".equals(editMode);
  String selectedClassId = request.getParameter("classId");
  
  List<Course> courseList = new ArrayList<>();
  List<User> studentList = new ArrayList<>();

  if (selectedClassId != null) {
    int classId = Integer.parseInt(selectedClassId);
    
    courseList = CourseDAO.getCoursesByClassId(classId);
    studentList = StudentDAO.getStudentsByClassId(classId);
  }
%>

<%
  String selectedCourseId = request.getParameter("courseId");
  Integer courseId = null;
  if (selectedCourseId != null && !selectedCourseId.isEmpty()) {
    try {
      courseId = Integer.parseInt(selectedCourseId);
    } catch (NumberFormatException e) {
      courseId = null;
    }
  }
%>


<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Score Management</title>
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
  <div class="container content-box">
    <h2 class="mb-4">Enter Scores</h2>
    <form method="get" action="">
      <div class="row mb-3">
        <div class="col-md-6">
          <label for="classDropdown" class="form-label">Select Class</label>
          <select class="form-select" name="classId" id="classDropdown" onchange="this.form.submit()">
            <option selected disabled>Choose a class</option>
            <% for (Classes c : classList) {
			     String selected = (selectedClassId != null && selectedClassId.equals(String.valueOf(c.getID()))) ? "selected" : "";
			%>
			  <option value="<%= c.getID() %>" <%= selected %>><%= c.getClassName() %></option>
			<% } %>
          </select>
        </div>
        <div class="col-md-6">
		  <label for="courseDropdown" class="form-label">Select Course</label>
		  <select class="form-select" name="courseId" id="courseDropdown" onchange="this.form.submit()" <%= selectedClassId == null ? "disabled" : "" %>>
			  <option disabled <%= (request.getParameter("courseId") == null) ? "selected" : "" %>>Choose a course</option>
			  <% for (Course course : courseList) {
			       String selected = (request.getParameter("courseId") != null && request.getParameter("courseId").equals(String.valueOf(course.getID()))) ? "selected" : "";
			  %>
			    <option value="<%= course.getID() %>" <%= selected %>><%= course.getCourseName() %></option>
			  <% } %>
			</select>
		</div>
      </div>
    </form>
	<% if (!studentList.isEmpty()) { %>
	  <form method="post" action="<%= request.getContextPath() %>/UpdateScoreServlet">
	    <input type="hidden" name="classId" value="<%= selectedClassId %>">
	    <input type="hidden" name="courseId" value="<%= selectedCourseId %>">
	    <% if (isEditing && courseId != null) { %>
		  <button type="submit" class="btn btn-success">Save Scores</button>
		<% } else if (courseId != null) { %>
		  <a href="?classId=<%= selectedClassId %>&courseId=<%= courseId %>&edit=true" class="btn btn-primary">Edit Scores</a>
		<% } %>
	    <table class="table table-bordered">
	      <thead>
	        <tr>
	          <th>Student ID</th>
	          <th>Student Name</th>
	          <th>Score</th>
	        </tr>
	      </thead>
	      <tbody>
	        <% if (courseId != null) { %>
			  <% for (User s : studentList) {
			       Score score = ScoreDAO.getScoreByStudentAndCourse(Integer.parseInt(s.getID()), courseId);
			  %>
			  <tr>
			    <td><%= s.getID() %></td>
			    <td><%= s.getFullName() %></td>
			    <td>
			      <% if (isEditing) { %>
			        <input type="number" step="0.1" name="score_<%= s.getID() %>" class="form-control" value="<%= score != null ? score.getScore() : "" %>" required>
			      <% } else { %>
			        <%= score != null ? score.getScore() : "N/A" %>
			      <% } %>
			    </td>
			  </tr>
			  <% } %>
			<% } else { %>
			  <tr>
			    <td colspan="3">Please select a course to view scores.</td>
			  </tr>
			<% } %> 
	      </tbody>
	    </table>
	  </form>
	<% } %>
  </div>

  
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
