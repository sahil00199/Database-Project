

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
 * Servlet implementation class StudentQuizzes
 */
@WebServlet("/StudentQuizzes")
public class StudentQuizzes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentQuizzes() {
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
		String secid = (String) request.getParameter("secid");
		if(!role.equals("student")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a student\"}");
			return;
		}
		if(secid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Section ID not passed as get parameter\"}");
			return;
		}
		int secid_int = Integer.parseInt(secid);
		String query =
				"select * "
				+ "from takes "
				+ "where sid = ? and secid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, secid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Student is not enrolled in the section"));
			return;
		}
		String query1 =  //TODO: verify query
				"select qzid, qzname "
				+ "from quiz "
				+ "where secid = ? and start <= now() "
				+ "order by start desc ;";
		String res1 = DbHelper.executeQueryJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT,}, 
				new Object[] {secid});
		
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
