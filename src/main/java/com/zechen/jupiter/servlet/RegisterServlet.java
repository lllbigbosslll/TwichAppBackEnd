package com.zechen.jupiter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zechen.jupiter.db.MySQLConnection;
import com.zechen.jupiter.db.MySQLException;
import com.zechen.jupiter.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = ServletUtil.readRequestBody(User.class, request);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isUserAdded = false;
        MySQLConnection connection = null;
        try {
            connection = new MySQLConnection();
            user.setPassword(ServletUtil.encryptPassword(user.getUserId(), user.getPassword()));
            isUserAdded = connection.addUser(user);
        } catch (MySQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        if (!isUserAdded) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}