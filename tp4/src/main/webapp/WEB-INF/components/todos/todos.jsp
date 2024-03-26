<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Todos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <meta http-equiv="refresh" content="5">
</head>
<body>

<h2>Liste des todos</h2>
<p>Il y a actuellement ${requestScope.model.size()} todos(s) :</p>
<ul>
    <c:forEach items="${requestScope.model}" var="todo">
        <li>${todo} : <strong><a href="${pageContext.request.contextPath}/todos/${todo}">${applicationScope.todoDao.findByHash(todo).title}</a></strong></li>
    </c:forEach>
</ul>
</body>
</html>
