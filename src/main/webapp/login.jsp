<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: linear-gradient(135deg, #f8f9fa, #e0e0e0);
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
    .login-form {
      background-color: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.1);
      width: 100%;
      max-width: 500px;
    }
    .form-title {
      text-align: center;
      margin-bottom: 10px;
    }
    .error-message {
      text-align: center;
      color: red;
      font-size: 1rem;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
  <div class="login-form">
    <h2 class="form-title">Welcome back</h2>

    <%
      String status = (String) request.getAttribute("status");
      String warn = request.getParameter("status");
      if ("failed".equals(status)) {
    %>
      <div class="error-message">Invalid email or password. Please try again.</div>
    <% } else if ("failed".equals(warn)) { %>
      <div class="error-message">You must log in to access the dashboard.</div>
    <% } %>

    <form method="post" action="Login">
      <div class="mb-3">
        <label for="email" class="form-label">Email address</label>
        <input type="text" class="form-control" name="email" id="email" required>
      </div>
      <div class="mb-4">
        <label for="password" class="form-label">Password</label>
        <input type="password" class="form-control" name="password" id="password" required>
      </div>
      <div class="d-grid mb-3">
        <button type="submit" class="btn btn-success btn-lg">Login</button>
      </div>
      <div class="text-center">
        Don’t have an account? <a href="register.jsp">Register here</a>
      </div>
    </form>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
