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
                <c:if test="${sessionScope.username == 'admin'}">
                    <th>Actions</th>
                </c:if>
            </tr>
            <c:forEach var="topic" items="${topics}">
                <tr>
                    <td><a href="posts?topic_id=${topic.id}">${topic.title}</a></td>
                    <td>${topic.username}</td>
                    <td>${topic.createdAt}</td>
                    <c:if test="${sessionScope.username == 'admin'}">
                        <td>
                            <form action="topics" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this topic and all its comments?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="topic_id" value="${topic.id}">
                                <button type="submit" class="btn btn-delete" style="margin-top: 0; padding: 4px 8px; font-size: 0.85em;">Delete</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>