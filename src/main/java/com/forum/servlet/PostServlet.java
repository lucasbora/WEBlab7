package com.forum.servlet;

import com.forum.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int topicId = Integer.parseInt(request.getParameter("topic_id"));
        
        try (Connection conn = DBUtil.getConnection()) {
            // Get topic title
            PreparedStatement psTopic = conn.prepareStatement("SELECT title FROM topics WHERE id = ?");
            psTopic.setInt(1, topicId);
            ResultSet rsTopic = psTopic.executeQuery();
            if (rsTopic.next()) {
                request.setAttribute("topicTitle", rsTopic.getString("title"));
            }
            
            // Get posts
            List<Map<String, Object>> posts = new ArrayList<>();
            PreparedStatement psPosts = conn.prepareStatement("SELECT p.id, p.content, u.username, p.user_id, p.created_at FROM posts p JOIN users u ON p.user_id = u.id WHERE p.topic_id = ? ORDER BY p.created_at ASC");
            psPosts.setInt(1, topicId);
            ResultSet rsPosts = psPosts.executeQuery();
            while (rsPosts.next()) {
                Map<String, Object> post = new HashMap<>();
                post.put("id", rsPosts.getInt("id"));
                post.put("content", rsPosts.getString("content"));
                post.put("username", rsPosts.getString("username"));
                post.put("user_id", rsPosts.getInt("user_id"));
                post.put("created_at", rsPosts.getString("created_at"));
                posts.add(post);
            }
            request.setAttribute("posts", posts);
            request.setAttribute("topicId", topicId);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        request.getRequestDispatcher("view_topic.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int userId = (int) request.getSession().getAttribute("user_id");
        
        if ("delete".equals(action)) {
            int postId = Integer.parseInt(request.getParameter("post_id"));
            int topicId = Integer.parseInt(request.getParameter("topic_id"));
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM posts WHERE id = ? AND user_id = ?")) {
                ps.setInt(1, postId);
                ps.setInt(2, userId);
                ps.executeUpdate();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            response.sendRedirect("posts?topic_id=" + topicId);
        } else {
            int topicId = Integer.parseInt(request.getParameter("topic_id"));
            String content = request.getParameter("content");
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO posts (content, topic_id, user_id) VALUES (?, ?, ?)")) {
                ps.setString(1, content);
                ps.setInt(2, topicId);
                ps.setInt(3, userId);
                ps.executeUpdate();
            } catch (Exception e) {
                throw new ServletException(e);
            }
            response.sendRedirect("posts?topic_id=" + topicId);
        }
    }
}