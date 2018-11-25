

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateQuestion
 */
@WebServlet("/CreateQuestion")
public class CreateQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuestion() {
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
//					"    <script src=\"jquery.dataTables.min.js\"></script>" + 
//					"    <script src=\"jquery-ui.min.js\"></script>" + 
//	
//					"    <link rel=\"stylesheet\" href=\"jquery-ui.css\" />" + 
//					"    <link rel=\"stylesheet\" href=\"jquery.dataTables.min.css\"/>" + 
//					
//					"	 <script src=\"create_question.js\"></script>" +
//					"</head>" + 
//					"<body>\n" + 
//					"<button type=\"button\" onclick=\"goBack()\">Back to Home</button><br>\n" + 
//					"	<h1>Create a New Question</h1>\n" + 
//					"	\n" + 
//					"	<p id=\"error\" style=\"color:red\"></p>\n" + 
//					"	<form>\n" + 
//					"		\n" + 
//					"		Question<br> <input type=\"text\" name=\"question\" id=\"question\" maxlength=\"2000\" required> <br>\n" +  
//					"		<p id=\"currtopics\">Current Topics:</p>\n" + 
//					"		<p id=\"addingTopics\"> Add a new Topic <br> <input type=\"text\" name=\"newtop\" id=\"newtop\" onchange=addTopic() maxlength=\"1000\"> \n<br><br>" +  
//					"		<!-- <input type=\"radio\" name=\"role\" value=\"TA\"> Teaching assistant <br> <br> -->\n" +
//					"		<input type=\"radio\" name=\"role\" value=\"objective\" onclick=\"makeO()\" checked> Objective <br>\n" + 
//					"		<input type=\"radio\" name=\"role\" value=\"subjective\" onclick=\"makeS()\"> Subjective <br>\n" + 
//					"		<p id=\"adding\"> Add a new option <br> <input type=\"text\" name=\"newop\" id=\"newop\" maxlength=\"1000\"> <br><br><button type=\"button\" onclick=\"addOption()\"> Add Options</button></p>\n" + 
//					"		<!-- <input type=\"radio\" name=\"role\" value=\"TA\"> Teaching assistant <br> <br> -->\n" + 
//					"		<p id=\"options\">Current Options:</p>\n" + 
//					"		<button type=\"button\" onclick=\"validateForm()\">Create Question</button>\n" + 
//					"	</form>\n" + 
//					"</body>"
//					+ "</html>" ;
//			response.setContentType("text/html");
//			response.getWriter().print(html);
			RequestDispatcher view = request.getRequestDispatcher("/contact.html");
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
