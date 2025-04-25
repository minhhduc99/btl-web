<%@ page import="java.util.*, model.Classes, model.User, dao.StudentDAO, dao.ClassDAO" %>
<%
  String email = (String) session.getAttribute("email");
  if (email == null) {
    session.setAttribute("status", "You must be logged in to access this page");
    response.sendRedirect("login.jsp?status=failed");
    return;
  }

  List<Classes> classList = ClassDAO.getAllClasses();
  List<User> studentList = StudentDAO.getStudentsWithoutClass();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Class Management</title>
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
	  <h2 class="mb-0">Class List</h2>
	  <div>
	  	<button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addClassModal">
		    <i class="bi bi-person-plus"></i> Add class
		  </button>
	  	<button id="deleteSelectedBtn" class="btn btn-danger me-2" disabled onclick="deleteSelectedClasses()">
	      <i class="bi bi-trash"></i> Delete Class
	    </button>
	  </div>
	</div>

	<% String errorMessage = request.getParameter("add_error"); %>
	<% if (errorMessage != null) { 
	  	if (errorMessage.equals("class_name_already_exists")) {
	%>
	    <div style="color: red;">Class name already exists.</div>
	<% } } %>

	<%
	  List<String> undeletableClasses = (List<String>) session.getAttribute("undeletableClasses");
	if (undeletableClasses != null) {
	  %>
	      <div class="alert alert-danger">
	          <strong>Cannot delete the below classes:</strong>
	          <ul>
	          <% for (String message : undeletableClasses) { %>
	              <li><%= message %></li>
	          <% } %>
	          </ul>
	      </div>
	  <%
	      session.removeAttribute("undeletableClasses");
	  }
	%>

    <% if (classList != null && !classList.isEmpty()) { %>
    	<form id="deleteForm" action="DeleteClass" method="post">
	      <table class="table table-bordered table-striped">
			  <thead class="table-dark">
			    <tr>
			      <th><input type="checkbox" id="selectAllCheckboxClass"></th>
			      <th>NO</th>
			      <th>Class Name</th>
			      <th>Class Period</th>
			      <th>Teacher</th>
			      <th>Actions</th>
			    </tr>
			  </thead>
			  <tbody>
			    <%
			      int index = 1;
			      for(Classes cls : classList) {
			    %>
			      <tr>
			        <td><input type="checkbox" class="classCheckbox" name="classIds" value="<%= cls.getID() %>"></td>
			        <td><%= index++ %></td>
			        <td><%= cls.getClassName() %></td>
			        <td><%= cls.getClassPeriod() %></td>
			        <td><%= cls.getTeacherName() %></td>
			        <td>
			        	<a href="ViewClass?id=<%= cls.getID() %>" class="btn btn-info btn-sm">
						  <i class="bi bi-eye"></i>
						</a>
			        	<button type="button" class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#assignStudentModal" data-class-id="<%= cls.getID() %>">
						  <i class="bi bi-person-plus"></i>
						</button>
			          <button type="button" class="btn btn-warning btn-sm" onclick="openEditModal('<%= cls.getID() %>', '<%= cls.getClassName() %>', '<%= cls.getClassPeriod() %>', '<%= cls.getTeacherName() %>')">
			            <i class="bi bi-pencil"></i>
			          </button>
			        </td>
			      </tr>
			    <% } %>
			  </tbody>
			</table>
		</form>
    <% } else { %>
      <div class="alert alert-warning">No classes available.</div>
    <% } %>
  </div>
  
  <!-- Add Class Modal -->
	<div class="modal fade" id="addClassModal" tabindex="-1" aria-labelledby="addClassModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="<%= request.getContextPath() %>/AddClass" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="addClassModalLabel">Add New Class</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label for="class_name" class="form-label">Class Name</label>
	            <input type="text" class="form-control" id="class_name" name="class_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="class_period" class="form-label">Class Period</label>
	            <input type="text" class="form-control" id="class_period" name="class_period" required>
	          </div>
	          <div class="mb-3">
	            <label for="teacher" class="form-label">Teacher</label>
	            <input type="text" class="form-control" id="teacher" name="teacher_name" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-success">Add Class</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<!-- Update Class Modal -->
	<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <form action="UpdateClass" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="editModalLabel">Edit Class</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	        <div class="modal-body">
	          <input type="hidden" id="edit-id" name="id">
	          <div class="mb-3">
	            <label for="edit-className" class="form-label">Class Name</label>
	            <input type="text" class="form-control" id="edit-className" name="class_name" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-classPeriod" class="form-label">Class Period</label>
	            <input type="text" class="form-control" id="edit-classPeriod" name="class_period" required>
	          </div>
	          <div class="mb-3">
	            <label for="edit-teacherName" class="form-label">Teacher</label>
	            <input type="text" class="form-control" id="edit-teacherName" name="teacher_name" required>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">Save changes</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	<!-- ASSIGN STUDENT MODAL -->
	<div class="modal fade" id="assignStudentModal" tabindex="-1" aria-labelledby="assignStudentModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg">
	    <form action="<%= request.getContextPath() %>/AssignStudentToClass" method="post">
	      <div class="modal-content">
	        <div class="modal-header">
	          <h5 class="modal-title" id="assignStudentModalLabel">Assign Students to Class</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
	        </div>
	
	        <div class="modal-body">
	          <!-- Filter -->
	          <div class="mb-3">
	            <input type="text" class="form-control" id="filterInput" placeholder="Search by Student ID">
	          </div>
	
	          <!-- Student List Table -->
	          <table class="table table-bordered table-striped">
	            <thead class="table-light">
	              <tr>
	                <th><input type="checkbox" id="selectAllCheckbox"></th>
	                <th>Student ID</th>
	                <th>Full Name</th>
	                <th>Email</th>
	              </tr>
	            </thead>
	            <tbody id="studentTableBody">
	              <% for (User u : studentList) { %>
	                <tr>
	                  <td><input type="checkbox" class="student-checkbox" name="selectedStudents" value="<%= u.getID() %>"></td>
	                  <td class="student-id"><%= u.getStudentID() %></td>
	                  <td><%= u.getFullName() %></td>
	                  <td><%= u.getEmail() %></td>
	                </tr>
	              <% } %>
	            </tbody>
	          </table>
	        </div>
	
	        <div class="modal-footer">
	          <input type="hidden" name="classId" value="">
	          <button type="submit" class="btn btn-primary">Assign Selected</button>
	          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
	
	
	<script>
	  function openEditModal(id, className, classPeriod, teacherName) {
	    document.getElementById('edit-id').value = id;
	    document.getElementById('edit-className').value = className;
	    document.getElementById('edit-classPeriod').value = classPeriod;
	    document.getElementById('edit-teacherName').value = teacherName;
	    var editModal = new bootstrap.Modal(document.getElementById('editModal'));
	    editModal.show();
	  }
	</script>
	
  	<script>
	  // Filter student by ID
	  document.getElementById('filterInput').addEventListener('keyup', function () {
	    const filterValue = this.value.toLowerCase();
	    const rows = document.querySelectorAll('#studentTableBody tr');
	    rows.forEach(row => {
	      const studentID = row.querySelector('.student-id').textContent.toLowerCase();
	      row.style.display = studentID.includes(filterValue) ? '' : 'none';
	    });
	  });
	
	  const selectAllCheckboxInModal = document.getElementById('selectAllCheckbox');
	  selectAllCheckboxInModal.addEventListener('change', function () {
	    const isChecked = this.checked;
	    const rows = document.querySelectorAll('#studentTableBody tr');
	    rows.forEach(row => {
	      if (row.style.display !== 'none') {
	        const checkbox = row.querySelector('input.student-checkbox');
	        checkbox.checked = isChecked;
	      }
	    });
	  });

	  const studentCheckboxesInModal = document.querySelectorAll('#studentTableBody input.student-checkbox');
	  studentCheckboxesInModal.forEach(cb => {
	    cb.addEventListener('change', function() {
	      const visibleCheckboxes = Array.from(document.querySelectorAll('#studentTableBody tr'))
	        .filter(row => row.style.display !== 'none')
	        .map(row => row.querySelector('input.student-checkbox'));
	      
	      const allChecked = visibleCheckboxes.every(cb => cb.checked);
	      selectAllCheckboxInModal.checked = allChecked;
	    });
	  });
	</script>
	
	<script>
	  const assignModal = document.getElementById('assignStudentModal');
	  assignModal.addEventListener('show.bs.modal', function (event) {
	    const button = event.relatedTarget;
	    const classId = button.getAttribute('data-class-id');
	    const inputHidden = assignModal.querySelector('input[name="classId"]');
	    inputHidden.value = classId;
	  });
	</script>
		
	<script>
	  const selectAllCheckboxClass = document.getElementById('selectAllCheckboxClass');
	  const classCheckboxes = document.getElementsByClassName('classCheckbox');
	  const deleteSelectedBtn = document.getElementById('deleteSelectedBtn');
	
	  // Get "Select All" event
	  selectAllCheckboxClass.addEventListener('change', function() {
	    for (let checkbox of classCheckboxes) {
	      checkbox.checked = selectAllCheckboxClass.checked;
	    }
	    toggleDeleteButton();
	  });
	
	  // Get each checkbox event
	  for (let checkbox of classCheckboxes) {
		  checkbox.addEventListener('change', function() {
		    if (!this.checked) {
		      selectAllCheckboxClass.checked = false;
		    } else {
		      const allChecked = Array.from(classCheckboxes).every(cb => cb.checked);
		      selectAllCheckboxClass.checked = allChecked;
		    }
		    toggleDeleteButton();
		  });
		}

	
	  function toggleDeleteButton() {
	    const anyChecked = Array.from(classCheckboxes).some(cb => cb.checked);
	    deleteSelectedBtn.disabled = !anyChecked;
	  }
	
	  // Delete Selected
	  function deleteSelectedClasses() {
	    if (confirm('Are you sure you want to delete the selected classes?')) {
	      document.getElementById('deleteForm').submit();
	    }
	  }
	</script>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
