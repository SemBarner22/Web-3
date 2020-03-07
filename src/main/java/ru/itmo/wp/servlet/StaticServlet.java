package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] uris = uri.split("\\+");
        boolean isFirst = true;
        OutputStream outputStream = response.getOutputStream();
        for (String singleUri : uris) {
            if (singleUri != null) {
                // TODO change to relative path inside unexploded webapp
               // File file = new File("/home/sem/Desktop/FirstServlet/src/main/webapp/static" + "/" + singleUri);
                File file = new File(getServletContext().getRealPath("."),
                        Paths.get("../../src/main/webapp/static", singleUri).toString());
                if (!file.exists()) {
                    file = new File(getServletContext().getRealPath("/static" + "/" + singleUri));
                }
                if (file.isFile()) {
                    if (isFirst) {
                        isFirst = false;
                        response.setContentType(getContentTypeFromName(singleUri));
                    }
                    Files.copy(file.toPath(), outputStream);
                    outputStream.flush();
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
