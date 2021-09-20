package ru.job4j.todo.servlet;

import ru.job4j.todo.persistence.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ItemPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", Store.getInstance().findAllCategories());
        req.getRequestDispatcher("items.jsp").forward(req, resp);
    }
}
