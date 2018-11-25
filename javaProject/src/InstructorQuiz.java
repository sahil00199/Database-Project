

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InstructorQuiz
 */
@WebServlet("/InstructorQuiz")
public class InstructorQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstructorQuiz() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null || session.getAttribute("role") == null) {
			response.sendRedirect("login.html");
		}
		
		String id = (String) session.getAttribute("id");
		String role = (String) session.getAttribute("role");
		String qzid= (String) request.getParameter("qzid");
		if(!role.equals("instructor")) {
			response.sendRedirect("illegalAccess.html");
//			String html = "<html><head><title>Error</title>" +
//					"</head>" + 
//					"<body>" +
//					"    <div id=\"content\">" +
//					"	 User is not an instructor</div> "
//					+ "</body>"
//					+ "</html>" ;
//			response.setContentType("text/html");
//			response.getWriter().print(html);
//			return;
		}
		if(qzid == null) {
			response.sendRedirect("illegalAccess.html");
//			String html = "<html><head><title>Error</title>" +
//					"</head>" + 
//					"<body>" +
//					"    <div id=\"content\">" +
//					"	 Section ID not passed as get parameter</div> "
//					+ "</body>"
//					+ "</html>" ;
//			response.setContentType("text/html");
//			response.getWriter().print(html);
//			return;
		}
		String html = "<html><head><title>Quiz</title>" + 
				"    <script src=\"jquery-3.3.1.js\"> </script>" + 
				"    <script src=\"jquery.dataTables.min.js\"></script>" + 
				"    <script src=\"jquery-ui.min.js\"></script>" + 

				"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
				"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 

				"    <script> var qzid = " + qzid + " </script>" +
				"	 <script src=\"instructor_quiz.js\"></script>" +
				"</head>" + 
				"<body>"
				+ "<h1 id = \"heading\"></h1>" + 
				"    <div id=\"content\">" +
				"	 </div> "
				+ "</body>"
				+ "</html>" ;
		response.setContentType("text/html");
		response.getWriter().print(html);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
