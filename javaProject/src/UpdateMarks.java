

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
 * Servlet implementation class UpdateMarks
 */
@WebServlet("/UpdateMarks")
public class UpdateMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateMarks() {
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
		String sid = (String) request.getParameter("sid");
		String qid = (String) request.getParameter("qid");
		String qzid = (String) request.getParameter("qzid");
		String marks = (String) request.getParameter("marks");
		if(!role.equals("instructor")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a instructor\"}");
			return;
		}
		if(qid == null || sid == null || qzid == null || marks == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"All parameters not passed\"}");
			return;
		}
		String query =
				"select * \n"
				+ "from instructor natural join teaches natural join quiz \n"
				+ "where iid = ? and qzid = ? ;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT}, 
				new Object[] {id, qzid});
		
		if(res.isEmpty()) {
			response.getWriter().print(DbHelper.errorJson("Instructor is not authorised"));
			return;
		}
		String query1 =  //TODO: verify query
				"update response "
				+ "set marksObtained = ? "
				+ "where sid = ? and qid = ? and qzid = ? ";
		String res1 = DbHelper.executeUpdateJson(query1, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.FLOAT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT,}, 
				new Object[] {marks, sid, qid, qzid});
		
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
