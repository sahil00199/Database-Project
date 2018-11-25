

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
 * Servlet implementation class StudentQuizQuestions
 */
@WebServlet("/StudentQuizQuestions")
public class StudentQuizQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentQuizQuestions() {
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
		if(!role.equals("student")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a student\"}");
			return;
		}
		if(quizid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
		String query =
				"select * "
				+ "from (student natural join takes) natural join quiz "
				+ "where sid = ? and qzid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, quizid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Student not enrolled in the section of this quiz"));
			return;
		}
		String query1 =
				"select qID, problem, isObjective,maxMarks "
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
