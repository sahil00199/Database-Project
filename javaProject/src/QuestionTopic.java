

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
 * Servlet implementation class QuestionTopic
 */
@WebServlet("/QuestionTopic")
public class QuestionTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionTopic() {
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
		String qid = (String) request.getParameter("qid");
		if(!role.equals("instructor")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a instructor\"}");
			return;
		}
		if(qid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Question ID not passed as get parameter\"}");
			return;
		}
		String query =
				"select * \n"
				+ "from instructor natural join question \n"
				+ "where iid = ? and qid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, qid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Instructor is not the author of this question"));
			return;
		}
		String query1 =  //TODO: verify query
				"select topicname "
				+ "from question natural join questiontopic natural join topic "
				+ "where qid = ? ;";
		String res1 = DbHelper.executeQueryJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT,}, 
				new Object[] {qid});
		
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
