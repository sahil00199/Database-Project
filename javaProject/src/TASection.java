

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TASection
 */
@WebServlet("/TASection")
public class TASection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TASection() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null || session.getAttribute("role") == null) {
			response.sendRedirect("login.html");
		}
		
		String id = (String) session.getAttribute("id");
		String role = (String) session.getAttribute("role");
		String secid = (String) request.getParameter("secid");
		if(!role.equals("TA")) {
			String html = "<html><head><title>Error</title>" +
					"</head>" + 
					"<body>" +
					"    <div id=\"content\">" +
					"	 User is not a TA</div> "
					+ "</body>"
					+ "</html>" ;
			response.setContentType("text/html");
			response.getWriter().print(html);
			return;
		}
		if(secid == null) {
			String html = "<html><head><title>Error</title>" +
					"</head>" + 
					"<body>" +
					"    <div id=\"content\">" +
					"	 Section ID not passed as get parameter</div> "
					+ "</body>"
					+ "</html>" ;
			response.setContentType("text/html");
			response.getWriter().print(html);
			return;
		}
		RequestDispatcher view = request.getRequestDispatcher("/TASection.jsp");
        view.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
