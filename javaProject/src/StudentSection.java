

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StudentSection
 */
@WebServlet("/StudentSection")
public class StudentSection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentSection() {
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
		System.out.println(role);
		System.out.println(secid);;

		if(!role.equals("student")) {
			System.out.println("hello2");
			response.sendRedirect("illegalAccess.html");
		}
		if(secid == null) {
			System.out.println("hello1");
			response.sendRedirect("illegalAccess.html");
		}
		System.out.println("hello");
//		String html = "<html><head><title>Section</title>" + 
//				"    <script src=\"jquery-3.3.1.js\"> </script>" + 
//				"    <script src=\"jquery.dataTables.min.js\"></script>" + 
//				"    <script src=\"jquery-ui.min.js\"></script>" + 
//
//				"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
//				"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 
//
//				"    <script> var secid = " + secid + " </script>" +
//				"	 <script src=\"student_section.js\"></script>" +
//				"</head>" + 
//				"<body>"
//				+ "<h1 id = \"heading\"></h1>" + 
//				"    <div id=\"content\">" +
//				"	 </div> "
//				+ "</body>"
//				+ "</html>" ;
//		response.setContentType("text/html");
//		response.getWriter().print(html);
		RequestDispatcher view = request.getRequestDispatcher("studentSection.jsp");
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
