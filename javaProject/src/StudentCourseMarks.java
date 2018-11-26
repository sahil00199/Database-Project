

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class StudentCourseMarks
 */
@WebServlet("/StudentCourseMarks")
public class StudentCourseMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentCourseMarks() {
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
		String sid = (String) request.getParameter("secid");
//		if(!role.equals("student")) {
//			response.getWriter().print("{\"status\": false, \"message\": \"User is not a student\"}");
//			return;
//		}
		String query = "with q1 as (select qzid as quizid from quiz where secid = ?), q2  as (select "
				+ "(select (case when sum(marksobtained) is null then 0 else sum(marksobtained) end) as s from response where sid = ?  and qzid = quizid and marksobtained > -1) as m,"
				+ " (select (case when sum(maxmarks) is null then 0 else sum(maxmarks) end) as s from quizquestion where qzid = quizid ) as mm,"
				+ "(select weightage from quiz where qzid=quizid) as w, quizid from q1) select sum(m*100*w/mm) as s from q2 where mm>0 ;";
		String res = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING}, 
				new String[] {sid, id});
		PrintWriter out = response.getWriter();
		out.print(res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
