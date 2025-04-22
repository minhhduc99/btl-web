<%@ page import="java.util.*, model.Course" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  List<Course> courseList = (List<Course>) request.getAttribute("courseList");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Course Management</title>
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
          <a class="nav-link" href="<%= request.getContextPath() %>/Classroom" aria-expanded="false">
            Classroom
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
	  <h2 class="mb-0">Course List</h2>
	  <div>
		  <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addCourseModal">
		    <i class="bi bi-person-plus"></i> Add Course
		  </button>
	  	<button id="deleteSelectedBtn" class="btn btn-danger me-2" disabled onclick="deleteSelectedCourses()">
	      <i class="bi bi-trash"></i> Delete Course
	    </button>
	  </div>
	</div>

    <% if (courseList != null && !courseList.isEmpty()) { %>
      <form id="deleteForm" action="DeleteCourse" method="post">
	      <table class="table table-bordered table-striped">
			  <thead class="table-dark">
			    <tr>
		    	  <th><input type="checkbox" id="selectAllCheckbox"></th>
			      <th>NO</th>
			      <th>Course Code</th>
			      <th>Course Name</th>
			      <th>Description</th>
			      <th>Credits</th>
			      <th>Actions</th>
			    </tr>
			  </thead>
			  <tbody>
			    <%
			      int index = 1;
			      for(Course c : courseList) {
			    %>
			      <tr>
			        <td><input type="checkbox" class="courseCheckbox" name="courseIds" value="<%= c.getID() %>"></td>
			        <td><%= index++ %></td>
			        <td><%= c.getCourseCode() %></td>
			        <td><%= c.getCourseName() %></td>
			        <td><%= c.getDescription() %></td>
			        <td><%= c.getCredits() %></td>
			        <td>
			          <button type="button" class="btn btn-warning btn-sm" onclick="openEditModal('<%= c.getID() %>', '<%= c.getCourseCode() %>', '<%= c.getCourseName() %>', '<%= c.getDescription() %>', '<%= c.getCredits() %>')">
			            <i class="bi bi-pencil"></i>
			          </button>
			        </td>
			      </tr>
			    <% } %>
			  </tbody>
			</table>
		</form>
    <% } else { %>
      <div class="alert alert-warning">No courses found.</div>
    <% } %>
  </div>
  
  <!-- Add Course Modal -->
	<div class="modal fade" id="addCourseModal" tabindex="-1" aria-labelledby="addCourseModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddCourse" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addCourseModalLabel">Add New Course</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label for="course_code" class="form-label">Course Code</label>
	            <input type="text" class="form-control" id="course_code" name="course_code" required>
	          </div>
	          <div class="mb-3">
	            <label for="course_name" class="form-label">Course Name</label>
	            <input type="text" class="form-control" id="course_name" name="course_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="description" class="form-label">Description</label>
	            <input type="text" class="form-control" id="description" name="description">
	          </div>
	          <div class="mb-3">
	            <label for="credits" class="form-label">Credits</label>
	            <input type="number" class="form-control" id="credits" name="credits" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-primary" onclick="switchToImportModal()">
				<i class="bi bi-upload"></i> Add CSV
			  </button>
	          <button type="submit" class="btn btn-success">Add Course</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<div class="modal fade" id="importCSVModal" tabindex="-1" aria-labelledby="importCSVModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddCourseCSV" method="post" enctype="multipart/form-data">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="importCSVModalLabel">Import Courses from CSV</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label for="csv_file" class="form-label">CSV File</label>
	            <input type="file" class="form-control" name="csv_file" id="csv_file" accept=".csv" required>
	            <small class="form-text text-muted">Format: course_code,course_name,description,credits</small>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Import</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<!-- Edit Course Modal -->
	<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="EditCourse" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="editModalLabel">Edit Course</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        <div class="modal-body">
	          <input type="hidden" id="edit-id" name="id">
	          <div class="mb-3">
	            <label for="edit-courseCode" class="form-label">Course Code</label>
	            <input type="text" class="form-control" id="edit-courseCode" name="course_code" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-courseName" class="form-label">Course Name</label>
	            <input type="text" class="form-control" id="edit-courseName" name="course_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-description" class="form-label">Description</label>
	            <input type="text" class="form-control" id="edit-description" name="description" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-credits" class="form-label">Credits</label>
	            <input type="number" class="form-control" id="edit-credits" name="credits" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Save changes</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<script>
	  function switchToImportModal() {
	    const addModalEl = document.getElementById('addCourseModal');
	    const importModalEl = document.getElementById('importCSVModal');
	
	    const addModalInstance = bootstrap.Modal.getInstance(addModalEl) || new bootstrap.Modal(addModalEl);
	    const importModalInstance = new bootstrap.Modal(importModalEl);
	
	    const onHidden = function () {
	      addModalEl.removeEventListener('hidden.bs.modal', onHidden);
	
	      document.body.classList.remove('modal-open');
	      const backdrop = document.querySelector('.modal-backdrop');
	      if (backdrop) backdrop.remove();
	
	      importModalInstance.show();
	    };
	
	    addModalEl.addEventListener('hidden.bs.modal', onHidden);
	
	    addModalInstance.hide();
	  }
	</script>
	
		
	
	<script>
	  function openEditModal(id, courseCode, courseName, description, credits) {
	    document.getElementById('edit-id').value = id;
	    document.getElementById('edit-courseCode').value = courseCode;
	    document.getElementById('edit-courseName').value = courseName;
	    document.getElementById('edit-description').value = description;
	    document.getElementById('edit-credits').value = credits;
	    var editModal = new bootstrap.Modal(document.getElementById('editModal'));
	    editModal.show();
	  }
	</script>
	
	<script>
	  const selectAllCheckbox = document.getElementById('selectAllCheckbox');
	  const courseCheckboxes = document.getElementsByClassName('courseCheckbox');
	  const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
	
	  // Get "Select All" event
	  selectAllCheckbox.addEventListener('change', function() {
	    for (let checkbox of courseCheckboxes) {
	      checkbox.checked = selectAllCheckbox.checked;
	    }
	    toggleDeleteButton();
	  });
	
	  // Get each checkbox event
	  for (let checkbox of courseCheckboxes) {
		  checkbox.addEventListener('change', function() {
		    if (!this.checked) {
		      selectAllCheckbox.checked = false;
		    } else {
		      const allChecked = Array.from(courseCheckboxes).every(cb => cb.checked);
		      selectAllCheckbox.checked = allChecked;
		    }
		    toggleDeleteButton();
		  });
		}

	
	  function toggleDeleteButton() {
	    const anyChecked = Array.from(courseCheckboxes).some(cb => cb.checked);
	    deleteSelectedBtn.disabled = !anyChecked;
	  }
	
	  // Delete Selected
	  function deleteSelectedCourses() {
	    if (confirm('Are you sure you want to delete the selected courses?')) {
	      document.getElementById('deleteForm').submit();
	    }
	  }
	</script>
  

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
