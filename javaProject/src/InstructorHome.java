

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InstructorHome
 */
@WebServlet("/InstructorHome")
public class InstructorHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstructorHome() {
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
			System.out.println("User not logged in");
			return;
		}
		
		String id = (String) session.getAttribute("id");
		String role = (String) session.getAttribute("role");
		if(!role.equals("instructor")) {
			response.sendRedirect("illegalAccess.html");
		}
		else {
//			String html = "<html><head><title>Instructor Home</title>" + 
//					"    <script src=\"jquery-3.3.1.js\"> </script>" + 
//					"<!-- Latest compiled and minified CSS -->\n" + 
//					"    <script src=\"jquery.dataTables.min.js\"></script>" + 
//					"    <script src=\"jquery-ui.min.js\"></script>" + 
//	
//					"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
//					"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 
//					
//					"	 <script src=\"instructor_home.js\"></script>" +
//					"</head>" + 
//					"<body>"
//					+ "<h1>Instructor Home</h1>" + 
//					"    <div id=\"content\">" +
//					"	 </div> "+
//					"<a href = \"CreateQuestion\"> Add a new question </a><br><br>"+
//					"<a href = \"InstructorDB\"> View all questions </a><br><br>"+
//					"<a href = \"LogoutServlet\"> Logout </a>" 
//					+ "</body>"
//					+ "</html>" ;
//			response.setContentType("text/html");
//			response.getWriter().print(html);
			RequestDispatcher view = request.getRequestDispatcher("/instructorHome.html");
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
