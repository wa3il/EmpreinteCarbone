<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Todo</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<c:set var="todo" value="${requestScope.model}" scope="request"/>

<h1>Todo ${todo.hash}</h1>
<ul>
    <li>title : ${todo.title}</li>
    <c:if test="${requestScope.authorizedUser}">
    <li>
        assignee:
        <ul>
                <a href="${pageContext.request.contextPath}/users/${todo.assignee}">${applicationScope.userDao.findOne(todo.assignee).getName()}</a>
        </ul>
    </li>
    </c:if>
    <c:if test="${todo.completed != null && todo.completed == true }">
            <li>status : ${"&#x2611;"}</li>
    </c:if>
    <c:if test="${todo.completed != null && todo.completed == false}">
            <li>status : ${"&#x2610;"}</li>
    </c:if>
</ul>
</body>
</html>
