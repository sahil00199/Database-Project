

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
		System.out.println(secid);
		String query  = "select sid, qzname, sum(marksobtained) \n" + 
				"from (select sid, qzname, (case when marksobtained = -1 or marksobtained is null \n" + 
				"then 0 else marksobtained end) \n" + 
				"from (select takes.sid, quiz.qzname, quiz.qzid from takes, quiz \n" + 
				"where takes.secid = ? and quiz.secid = ?) as foo natural left outer join response)\n" + 
				"as bar group by qzname, sid order by sid, qzname asc;";
		List<List<Object>> output = DbHelper.executeQueryList(query, new DbHelper.ParamType[] 
				{DbHelper.ParamType.INT,DbHelper.ParamType.INT }, new String[] {secid, secid});
		
		String metaQuery = "select qzname from quiz where secid = ? order by qzname asc;";
		List<List<Object>> metaData = DbHelper.executeQueryList(metaQuery, new DbHelper.ParamType[] 
				{DbHelper.ParamType.INT}, new String[] {secid});
		
		int numQuizzes = metaData.size();
		
		//System.out.println(");)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))0");
		String theString = "";
		
		theString += "<div class=\"limiter\">\n" + 
				"		<div class=\"container-table100\">\n" + 
				"			<div class=\"wrap-table100\">\n" + 
				"				<div class=\"table100 ver1 m-b-110\">\n" + 
				"					<div class=\"table100-head\">";
		theString += "<table>\n";
			theString += "<tr class=\"row100 head\">\n";
				theString += "<th class=\"cell100 column1\">Student Name</th>\n";
				for (int i = 0 ; i < metaData.size(); i ++)
				{
					theString += "<th class=\"cell100 column2\">" + (String) metaData.get(i).get(0) + "</th>\n";
				}
			theString += "</tr>\n";
			theString += "</thead>\n" + 
					"						</table>\n" + 
					"					</div>";
			theString += "<div class=\"table100-body js-pscroll\">\n" + 
					"						<table>\n" + 
					"							<tbody>";
			for (int i = 0 ; i < output.size() / numQuizzes ; i ++)
			{
				theString += "<tr class=\"row100 body\">\n";
				theString += "<td class=\"cell100 column1\">" + (String) output.get(numQuizzes*i).get(0) + "</td>\n";
				for (int j = 0 ; j < numQuizzes ; j ++)
				{
					System.out.println(numQuizzes*i + j);
					theString += "<td class=\"cell100 column2\">" + Float.toString((float) output.get(numQuizzes*i + j).get(2)) + "</td>\n";
				}
				theString += "</tr>\n";
			}
			theString += "</tbody>\n" + 
				"						</table>\n" + 
				"					</div>\n" + 
				"				</div></div></div>";

		
		
		//System.out.println(theString);
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
