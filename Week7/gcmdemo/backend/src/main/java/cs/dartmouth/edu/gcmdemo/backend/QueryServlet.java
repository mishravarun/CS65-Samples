package cs.dartmouth.edu.gcmdemo.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs.dartmouth.edu.gcmdemo.backend.data.Contact;
import cs.dartmouth.edu.gcmdemo.backend.data.ContactDatastore;

public class QueryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String name = req.getParameter("name");
		ArrayList<Contact> result = ContactDatastore.query(name);
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.write("<html>\n" +
				"<head>\n" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
				"<title>Query Result</title>\n" +
				"</head>\n" +
				"<body>\n");
		String retStr = (String) req.getAttribute("_retStr");
		if (retStr != null) {
			out.write(retStr+"<br>");

		}
		out.write("<center>\n" +
				"\t\t<b>Query Result</b>\n" +
				"\t\t<form name=\"input\" action=\"/query.do\" method=\"get\">\n" +
				"\t\t\tName: <input type=\"text\" name=\"name\"> <input type=\"submit\"\n" +
				"\t\t\t\tvalue=\"OK\">\n" +
				"\t\t</form>\n" +
				"\t</center>");


		if (result != null) {
			for (Contact contact : result) {
				out.write("Name:" + contact.mName + "&nbsp; Address:" + contact.mAddress + "&nbsp;" +
						"PhoneNumber:" + contact.mPhoneNumber + "&nbsp; &nbsp;&nbsp; <input type=\"button\" onclick=\"location.href='/delete.do?name="+contact.mName+"'\" value=\"Delete\"><br>");

			}
		}
		out.write("</b> Add new contact:\n" +
				"\t<br>\n" +
				"\t<form name=\"input\" action=\"/add.do\" method=\"post\">\n" +
				"\t\tName: <input type=\"text\" name=\"name\"> Address: <input\n" +
				"\t\t\ttype=\"text\" name=\"addr\"> Phone: <input type=\"text\"\n" +
				"\t\t\tname=\"phone\"> <input type=\"submit\" value=\"Add\">\n" +
				"\t</form>\n" +
				"\t---------------------------------------------------------------------\n" +
				"\t<br>\n" +
				"\t<form name=\"input\" action=\"/update.do\" method=\"post\">\n" +
				"\t\tName: <input type=\"text\" name=\"name\">\n" +
				"\t\tAddress: <input type=\"text\" name=\"addr\">\n" +
				"\t\tPhone: <input type=\"text\" name=\"phone\">\n" +
				"\t\t<input type=\"submit\" value=\"Update\">\n" +
				"\t</form>\n" +
				"</body>\n" +
				"</html>");

	}


	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}
}
