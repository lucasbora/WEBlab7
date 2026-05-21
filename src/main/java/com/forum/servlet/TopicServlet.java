package com.forum.servlet;

import com.forum.model.Topic;
import com.forum.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/topics")
public class TopicServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Topic> topics = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT t.id, t.title, u.username, t.created_at FROM topics t JOIN users u ON t.user_id = u.id ORDER BY t.created_at DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getInt("id"));
                topic.setTitle(rs.getString("title"));
                topic.setUsername(rs.getString("username"));
                topic.setCreatedAt(rs.getTimestamp("created_at"));
                topics.add(topic);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.setAttribute("topics", topics);
        request.getRequestDispatcher("topics.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        int userId = (int) request.getSession().getAttribute("user_id");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO topics (title, user_id) VALUES (?, ?)")) {
            ps.setString(1, title);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ServletException(e);
        }
        response.sendRedirect("topics");
    }
}