

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StudentHome
 */
@WebServlet("/StudentHome")
public class StudentHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentHome() {
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
		if(!role.equals("student")) {
			response.sendRedirect("illegalAccess.html");
		}
		else {
//			String html = "<html><head><title>Student Home</title>" + 
//					"    <script src=\"jquery-3.3.1.js\"> </script>" + 
//					"    <script src=\"jquery.dataTables.min.js\"></script>" + 
//					"    <script src=\"jquery-ui.min.js\"></script>" + 
//	
//					"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
//					"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 
//					
//					"	 <script src=\"student_home.js\"></script>" +
//					"</head>" + 
//					"<body>"
//					+ "<h1>Student Home</h1>" + 
//					"    <div id=\"content\">" +
//					"	 </div> "
//					+ "</body>"
//					+ "</html>" ;
//			response.setContentType("text/html");
//			response.getWriter().print(html);
			RequestDispatcher view = request.getRequestDispatcher("/studentHome.html");
	        view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
