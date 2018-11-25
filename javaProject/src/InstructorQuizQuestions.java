

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InstructorQuizQuestions
 */
@WebServlet("/InstructorQuizQuestions")
public class InstructorQuizQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstructorQuizQuestions() {
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
		String quizid = (String) request.getParameter("qzid");
		if(!role.equals("instructor")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a instructor\"}");
			return;
		}
		if(quizid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
//		int quizid_int = Integer.parseInt(quizid);
		String query =
				"select * "
				+ "from (instructor natural join teaches) natural join quiz "
				+ "where iid = ? and qzid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, quizid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("You are not a instructor of this course"));
			return;
		}
		String query1 =  //TODO: verify query
				"select qID, problem, isObjective, maxmarks "
				+ "from (quiz natural join quizQuestion) natural join question "
				+ "where qzid = ?;";
		String res1 = DbHelper.executeQueryJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT}, 
				new Object[] {quizid});
		
		PrintWriter out = response.getWriter();
		out.print(res1);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
