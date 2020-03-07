package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class CaptchaFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();

        if ((!"GET".equalsIgnoreCase(request.getMethod()) ||
                "Captcha-true".equalsIgnoreCase((String) httpSession.getAttribute("isCaptcha"))
                //|| "/favicon.ico".equals(request.getRequestURI()))
                        )
                &&
                        !"/captcha/response".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if ("/captcha/response".equals(request.getRequestURI())) {
                System.out.println(request.getParameter("captcha"));
                System.out.println(httpSession.getAttribute("number"));
                if (request.getParameter("captcha").equals(httpSession.getAttribute("number"))) {
                    httpSession.setAttribute("isCaptcha", "Captcha-true");
                    System.out.println("captcha is true");
                } else
                    System.out.println("captcha is false");
                System.out.println("from captcha: " + request.getParameter("captcha"));
                System.out.println("number " + (httpSession.getAttribute("number")));
                if ("Captcha-true".equalsIgnoreCase((String) httpSession.getAttribute("isCaptcha"))) {
                    System.out.println(httpSession.getAttribute("awaited-uri"));
                    try {
                        System.out.println(request.getRequestURL());
                        response.sendRedirect((String) httpSession.getAttribute("awaited-uri"));
                        //response.sendRedirect("/messages.html");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    System.out.println("done!");
                    //httpSession.removeAttribute("awaited-uri");
                } else {
                    try {
                        System.out.println("again...");
                        httpSession.setAttribute("isCaptcha", "");
                        sendCaptcha(response, httpSession);
                        System.out.println("...again...");
                        //chain.doFilter(request, response);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            } else {
                httpSession.setAttribute("isCaptcha", "");
                httpSession.setAttribute("awaited-uri", request.getRequestURI());
                sendCaptcha(response, httpSession);
                System.out.println(httpSession.getAttribute("awaited-uri"));
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("AAA");
        }
    }

    private void sendCaptcha(HttpServletResponse response, HttpSession httpSession) throws IOException {
        //if (httpSession.getAttribute("awaited-uri") == null) {
        //    httpSession.setAttribute("awaited-uri", request.getRequestURI());
        //}
        System.out.println(response);
        try {
            int number = (int) (Math.random() * 900) + 100;
            httpSession.setAttribute("number", String.valueOf(number));
            System.out.println("number set " + httpSession.getAttribute("number"));
            PrintWriter printWriter = response.getWriter();
            String s2 = Base64.getEncoder().encodeToString(ImageUtils.toPng(number + ""));
            byte[] s1Byte = Files.readAllBytes(Paths.get(getServletContext().getRealPath("/static/captcha.html")));
            System.out.println(s1Byte.length);
            String s1 = new String(s1Byte, StandardCharsets.UTF_8);
            response.setContentType("text/html");
            printWriter.write(String.format(s1, s2));
            printWriter.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
