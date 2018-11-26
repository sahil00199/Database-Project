

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
 * Servlet implementation class QuizQuesResponseI
 */
@WebServlet("/QuizQuesResponseI")
public class QuizQuesResponseI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizQuesResponseI() {
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
			response.getWriter().print("{\"status\": false, \"message\": \"Invalid session\"}");
			return;
		}
		
		String id = (String) session.getAttribute("id");
		String role = (String) session.getAttribute("role");
		String quizid = (String) request.getParameter("qzid");
		String iter = (String) request.getParameter("i");
		String qid = (String) request.getParameter("qid");
		if(!role.equals("TA")) {
			response.getWriter().print("{\"status\": false, \"message\": \"Unauthorised user\"}");
			return;
		}
		if(quizid == null || iter == null || qid ==null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Parameters not passed correctly\"}");
			return;
		}
		String query =
				"select * "
				+ "from tasection natural join section natural join quiz "
				+ "where taid = ? and qzid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, quizid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Unauthorised access"));
			return;
		}
		
		
		String query1 =
				"select * "
				+ "from response "
				+ "where qid= ? and qzid = ? "
				+ "order by sid "
				+ " LIMIT 1 OFFSET ? ;";
		String res1 = DbHelper.executeQueryJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT,}, 
				new Object[] {qid, quizid, iter});
		if(res1.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Reached end of the list"));
			return;
		}
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
