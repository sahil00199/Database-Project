

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
 * Servlet implementation class StudentCorrectAnswer
 */
@WebServlet("/StudentCorrectAnswer")
public class StudentCorrectAnswer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentCorrectAnswer() {
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
		if(!role.equals("student")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a student\"}");
			return;
		}
		if(qid == null) {
			response.getWriter().print(DbHelper.errorJson("Question ID not passed as get parameter"));
			return;
		}
//		String query =  "select * "
//				+ "from (student natural join takes) natural join quiz "
//				+ "where sid = ? and qzid = ? ;";
//		List<List<Object>> res = DbHelper.executeQueryList(query, 
//				new DbHelper.ParamType[] { 
//						DbHelper.ParamType.STRING,
//						DbHelper.ParamType.INT}, 
//				new Object[] {id, qzid});
//		
//		if(res.isEmpty()) {
//			response.getWriter().print(DbHelper.errorJson("Student is not authorised for the quiz"));
//			return;
//		}
		String query1 =  //TODO: verify query
				"select  opt "
				+ "from question natural join option "
				+ "where qid = ? and iscorrect;";
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


