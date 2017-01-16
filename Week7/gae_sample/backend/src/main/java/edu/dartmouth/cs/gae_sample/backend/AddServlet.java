package edu.dartmouth.cs.gae_sample.backend;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.gae_sample.backend.data.Contact;
import edu.dartmouth.cs.gae_sample.backend.data.ContactDatastore;

public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		String name = req.getParameter("name");
		String addr = req.getParameter("addr");
		String phone = req.getParameter("phone");

		if (name == null || name.equals("")) {
			req.setAttribute("_retStr", "invalid input");
			getServletContext().getRequestDispatcher("/query_result.jsp")
					.forward(req, resp);
			return;
		}

		Contact contact = new Contact(name, addr, phone);
		boolean ret = ContactDatastore.add(contact);
		if (ret) {
			req.setAttribute("_retStr", "Add contact " + name + " succ");

			ArrayList<Contact> result = new ArrayList<Contact>();
			result.add(contact);
			req.setAttribute("result", result);
		} else {
			req.setAttribute("_retStr", name + " exists");
		}

		getServletContext().getRequestDispatcher("/query_result.jsp").forward(
				req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doGet(req, resp);
	}

}
