

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InsertQuizQuestion
 */
@WebServlet("/InsertQuizQuestion")
public class InsertQuizQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertQuizQuestion() {
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
		String quizid = (String) request.getParameter("quizid");
		String qid = (String) request.getParameter("qid");
		String maxMarks = (String) request.getParameter("maxmarks");
		if(!role.equals("instructor")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not an instructor\"}");
			return;
		}
		if(quizid == null ) {
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
		if (qid == null)
		{
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
		
		String query = "insert into quizquestion values(?, ?, ?)";
		
		String json = DbHelper.executeUpdateJson(query, new DbHelper.ParamType[] {
				DbHelper.ParamType.INT, DbHelper.ParamType.INT, DbHelper.ParamType.INT
		}, new Object[] {qid, quizid, maxMarks});
		PrintWriter out = response.getWriter();
		out.print(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
