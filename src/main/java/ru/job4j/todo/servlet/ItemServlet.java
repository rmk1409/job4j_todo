package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.Store;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class ItemServlet extends HttpServlet {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, h:mm:ss a");
    private static final Gson GSON = new GsonBuilder().create();
    private static final Store store = new Store();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(store.findAll());
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        OutputStream output = resp.getOutputStream();

        String id = req.getParameter("id");
        String description = req.getParameter("description");
        String done = req.getParameter("done");
        String created = req.getParameter("created");

        String json;
        if (Objects.isNull(id)) {
            json = GSON.toJson(store.saveOrUpdate(new Item(description)));
        } else {
            int intId = Integer.parseInt(id);
            Timestamp tCreated = null;
            try {
                tCreated = new Timestamp(dateFormat.parse(created).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            boolean bDone = Boolean.parseBoolean(done);
            json = GSON.toJson(store.saveOrUpdate(new Item(intId, description, tCreated, bDone)));
        }
        output.write(json.getBytes(StandardCharsets.UTF_8));

        output.flush();
        output.close();
    }
}
