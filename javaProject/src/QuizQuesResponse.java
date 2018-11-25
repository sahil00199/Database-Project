

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
 * Servlet implementation class QuizQuesResponse
 */
@WebServlet("/QuizQuesResponse")
public class QuizQuesResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizQuesResponse() {
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
		String qid = (String) request.getParameter("qid");
		String sid = "";
		if(quizid == null || qid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
		if(role.equals("student")) {
			sid = id;
		}
		else if(role.equals("instructor")){
			sid = (String) request.getParameter("sid");
			String query =
					"select * "
					+ "from teaches natural join section natural join quiz "
					+ "where iid = ? and qzid = ? ;";
			List<List<Object>> res = DbHelper.executeQueryList(query, 
					new DbHelper.ParamType[] { 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT}, 
					new Object[] {id, quizid});
			
			if(res.isEmpty()) {
				response.getWriter().print(DbHelper.errorJson("Unauthorised access"));
				return;
			}
		}
		else if(role.equals("TA")) {
			
		}
		else {
			response.getWriter().print("{\"status\": false, \"message\": \"Unauthorised access\"}");
			return;
		}
		
		
		String query1 =
				"select * "
				+ "from response "
				+ "where sid = ? and qid= ? and qzid = ? ;";
		String res1 = DbHelper.executeQueryJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT}, 
				new Object[] {sid, qid, quizid});
		
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
