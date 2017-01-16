package edu.dartmouth.cs.appengineserver.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppEngServletDemo extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7224390555085474606L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        String str = req.getParameter("user_input");
        
        PrintWriter writer = resp.getWriter();
        writer.write("<html>\n");
        writer.write("<b>");
        writer.write("doGet(): " + str + ", Time: " + Calendar.getInstance().getTime().toString());
        writer.write("</b>");
        writer.write("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        String str = req.getParameter("user_input");
        
        PrintWriter writer = resp.getWriter();
        writer.write("<html>\n");
        writer.write("<b>");
        writer.write("doPost(): " + str + ", Time: " + Calendar.getInstance().getTime().toString());
        writer.write("</b>");
        writer.write("</html>");
    }
}
