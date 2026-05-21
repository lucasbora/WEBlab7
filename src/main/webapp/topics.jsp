<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Topics - Forum</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Forum Topics</h2>
            <span>Welcome, ${sessionScope.username}! | <a href="logout">Logout</a></span>
        </div>
        
        <div class="actions">
            <a href="new_topic.jsp" class="btn">Create New Topic</a>
        </div>

        <table>
            <tr>
                <th>Title</th>
                <th>Author</th>
                <th>Created At</th>
            </tr>
            <c:forEach var="topic" items="${topics}">
                <tr>
                    <td><a href="posts?topic_id=${topic.id}">${topic.title}</a></td>
                    <td>${topic.username}</td>
                    <td>${topic.createdAt}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>