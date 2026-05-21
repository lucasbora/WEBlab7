<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${topicTitle} - Forum</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        function confirmDelete() {
            return confirm("Are you sure you want to delete this post?");
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Topic: ${topicTitle}</h2>
            <span><a href="topics">Back to Topics</a> | <a href="logout">Logout</a></span>
        </div>

        <div class="posts">
            <c:forEach var="post" items="${posts}">
                <div class="post-card">
                    <div class="post-header">
                        <strong>${post.username}</strong> <span class="date">${post.createdAt}</span>
                    </div>
                    <div class="post-content">
                        ${post.content}
                    </div>
                    <c:if test="${post.userId == sessionScope.user_id}">
                        <form action="posts" method="post" onsubmit="return confirmDelete();" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="post_id" value="${post.id}">
                            <input type="hidden" name="topic_id" value="${topicId}">
                            <button type="submit" class="btn-delete">Delete</button>
                        </form>
                    </c:if>
                </div>
            </c:forEach>
        </div>

        <div class="reply-section">
            <h3>Add a Reply</h3>
            <form action="posts" method="post">
                <input type="hidden" name="topic_id" value="${topicId}">
                <div class="form-group">
                    <textarea name="content" rows="4" required></textarea>
                </div>
                <button type="submit">Post Reply</button>
            </form>
        </div>
    </div>
</body>
</html>