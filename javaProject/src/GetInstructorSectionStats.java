

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
 * Servlet implementation class GetInstructorSectionStats
 */
@WebServlet("/GetInstructorSectionStats")
public class GetInstructorSectionStats extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInstructorSectionStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null)//not logged in
		{
			response.sendRedirect("login.html");
			return;
		}
		if (!session.getAttribute("role").equals("instructor"))
		{
			response.sendRedirect("illegalAccess.html");
		}
		String secid = (String) request.getParameter("secid");
		String query  = "select sid, qzname, sum(marksobtained) from (select sid, qzname, (case when "
				+ "marksobtained = -1 or marksobtained is null then 0 else marksobtained end) from "
				+ "(select takes.sid, quiz.qzname from takes, quiz where takes.secid = ? and "
				+ "quiz.secid = ?) as foo natural left outer join response) as bar group by "
				+ "qzname, sid order by sid, qzname asc;";
		List<List<Object>> output = DbHelper.executeQueryList(query, new DbHelper.ParamType[] 
				{DbHelper.ParamType.INT,DbHelper.ParamType.INT }, new String[] {secid, secid});
		
		String metaQuery = "select qzname from quiz where secid = ? order by qzname asc;";
		List<List<Object>> metaData = DbHelper.executeQueryList(metaQuery, new DbHelper.ParamType[] 
				{DbHelper.ParamType.INT}, new String[] {secid});
		
		String theString = "";
		
		System.out.println(theString);
		PrintWriter out = response.getWriter();
		out.print(theString);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
