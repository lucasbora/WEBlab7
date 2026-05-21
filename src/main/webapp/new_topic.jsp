<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Topic - Forum</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Create New Topic</h2>
            <span><a href="topics">Back to Topics</a> | <a href="logout">Logout</a></span>
        </div>

        <form action="topics" method="post">
            <div class="form-group">
                <label>Topic Title:</label>
                <input type="text" name="title" required>
            </div>
            <button type="submit">Create</button>
        </form>
    </div>
</body>
</html>