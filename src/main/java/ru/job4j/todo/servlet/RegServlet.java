package ru.job4j.todo.servlet;

import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Store store = Store.getInstance();
        String email = req.getParameter("email");
        if (Objects.nonNull(store.findUserByEmail(email))) {
            String errorMessage = "Пользователь с таким email уже зарегистрирован в системе, используйте другой email";
            req.setAttribute("error", errorMessage);
            doGet(req, resp);
        } else {
            User user = new User(0, email, req.getParameter("password"));
            store.save(user);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/items.do");
        }
    }
}
