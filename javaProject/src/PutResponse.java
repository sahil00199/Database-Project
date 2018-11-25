

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
 * Servlet implementation class PutResponse
 */
@WebServlet("/PutResponse")
public class PutResponse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PutResponse() {
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
		String qzid = (String) request.getParameter("qzid");
		String qid = (String) request.getParameter("qid");
		String ans = (String) request.getParameter("answer");
		if(!role.equals("student")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a student\"}");
			return;
		}
		if(qzid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Quiz ID not passed as get parameter\"}");
			return;
		}
		int qz_int = Integer.parseInt(qzid);
		String query =
				"select * "
				+ "from response "
				+ "where sid = ? and qzid = ?  and qid = ?;";
		List<List<Object>> res = DbHelper.executeQueryList(query, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT}, 
				new Object[] {id, qzid, qid});
		
		if(res.isEmpty()) {
			String query1 =  //TODO: verify query
					"insert into response(sid,qid,qzid,answer) values "
					+ "(?,?,?,?) ";
			String res1 = DbHelper.executeUpdateJson(query1, 
					new DbHelper.ParamType[] { 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.INT,
							DbHelper.ParamType.STRING}, 
					new Object[] {id, qid, qzid, ans});
			PrintWriter out = response.getWriter();
			out.print(res1);
			return;
		}
		System.out.println("Hereeee");
		String query1 =  //TODO: verify query
				"update response "
				+ "set answer=? where "
				+ "sid = ? and qid = ? and "
				+ "qzid = ?;";
		String res1 = DbHelper.executeUpdateJson(query1, 
				new DbHelper.ParamType[] { 
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT}, 
				new Object[] {ans, id, qid, qzid});
		PrintWriter out = response.getWriter();
		out.print(res1);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
