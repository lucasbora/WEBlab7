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
        HttpSession session = request.getSession();
        Integer userIdObj = (Integer) session.getAttribute("user_id");
        String username = (String) session.getAttribute("username");

        if (userIdObj == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            // Secure server-side check
            if (!"admin".equals(username)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only administrators can delete topics.");
                return;
            }

            String topicIdStr = request.getParameter("topic_id");
            if (topicIdStr != null) {
                int topicId = Integer.parseInt(topicIdStr);
                try (Connection conn = DBUtil.getConnection()) {
                    conn.setAutoCommit(false);
                    try {
                        // 1. Delete all posts belonging to this topic first
                        try (PreparedStatement psPosts = conn.prepareStatement("DELETE FROM posts WHERE topic_id = ?")) {
                            psPosts.setInt(1, topicId);
                            psPosts.executeUpdate();
                        }
                        // 2. Delete the topic itself
                        try (PreparedStatement psTopic = conn.prepareStatement("DELETE FROM topics WHERE id = ?")) {
                            psTopic.setInt(1, topicId);
                            psTopic.executeUpdate();
                        }
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                    }
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
            response.sendRedirect("topics");
        } else {
            // Normal creation logic
            String title = request.getParameter("title");
            if (title == null || title.trim().isEmpty()) {
                response.sendRedirect("topics");
                return;
            }
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO topics (title, user_id) VALUES (?, ?)")) {
                ps.setString(1, title);
                ps.setInt(2, userIdObj);
                ps.executeUpdate();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            response.sendRedirect("topics");
        }
    }
}