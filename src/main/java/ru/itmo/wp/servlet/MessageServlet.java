package ru.itmo.wp.servlet;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageServlet extends HttpServlet {

    private ArrayList<MyPair> arrayList;

    @Override
    public void init() throws ServletException {
        super.init();
        arrayList = new ArrayList<>();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        HttpSession httpSession = request.getSession();
        /*if (httpSession.getAttribute("list") == null) {
            httpSession.setAttribute("list", new ArrayList<MyPair>());
        }*/
        if (uri.endsWith("/message/auth")) {
            String user = request.getParameter("user");
            if (user != null) {
                httpSession.setAttribute("user", user);
             } else {
                user = (String) httpSession.getAttribute("user");
            }
            String json = new Gson().toJson(user);
            response.getWriter().print(json);
            System.out.println(user);
            response.setContentType("application/json");
            response.getWriter().close();
        }
        if (uri.endsWith("/message/findAll")) {
            System.out.println(arrayList.size());
            //System.out.println(((ArrayList<MyPair>) httpSession.getAttribute("list")).size());
            String json = new Gson().toJson(arrayList);
            response.getWriter().print(json);
            System.out.println(json);
            response.setContentType("application/json");
            response.getWriter().close();
        }
        if (uri.endsWith("/message/add")) {
            String text = request.getParameter("text");
            if (text != null) {
                //httpSession.setAttribute((String) httpSession.getAttribute("user"), text);
                //TODO add to list;
                //System.out.println((String) httpSession.getAttribute("user"));
                arrayList.add(new MyPair((String) httpSession.getAttribute("user"), text));
                //((ArrayList<MyPair>) httpSession.getAttribute("list")).
                  //      add(new MyPair((String) httpSession.getAttribute("user"), text));
                String json = new Gson().toJson(text);
                response.getWriter().print(json);
                response.setContentType("application/json");
                response.getWriter().close();
            }
        }
        //response.addCookie();
    }
}
