

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AutoCompleteQuestion
 */
@WebServlet("/AutoCompleteQuestion")
public class AutoCompleteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null){
			response.sendRedirect("LoginServlet");
			return;
		}
		if (!session.getAttribute("role").equals("instructor")){
			response.sendRedirect("illegalAccess.html");
			return;
		}
		String iid = (String) session.getAttribute("id");
		String partial = (String) request.getParameter("partial");
		String topicname = (String) request.getParameter("topic");
		String quizid = (String) request.getParameter("quizid");
		String objective = (String) request.getParameter("objective");
		String subjective = (String) request.getParameter("subjective");
		System.out.println(topicname);
		System.out.println(objective);
		System.out.println(subjective);
		System.out.println(partial);
		int isObj = Integer.parseInt(objective);
		int isSubj = Integer.parseInt(subjective);
		partial = partial + "%";
		String query = "";
		if (isObj == 1 && isSubj == 0 && !topicname.equals(""))
		{
			query = "select problem as label, qid as value from (question natural inner join questiontopic "
					+ "natural inner join (select topicid from topic natural join (select "
					+ "courseid from quiz natural inner join section where qzid = ?) as temp "
					+ "where topicname like ?) as temp2) where"
					+ " problem like ? and isObjective=true and iid = ?;";
		}
		else if (isObj == 0 && isSubj == 1 && !topicname.equals(""))
		{
			query = "select problem as label, qid as value from (question natural inner join questiontopic "
					+ "natural inner join (select topicid from topic natural join (select "
					+ "courseid from quiz natural inner join section where qzid = ?) as temp "
					+ "where topicname like ?) as temp2) where "
					+ "problem like ? and isObjective=false and iid = ?;";
		}
		else if (isObj == 1 && isSubj == 1 && !topicname.equals(""))
		{
			query = "select problem as label, qid as value from (question natural inner join questiontopic "
					+ "natural inner join (select topicid from topic natural join (select "
					+ "courseid from quiz natural inner join section where qzid = ?) as temp "
					+ "where topicname like ?) as temp2) where "
					+ "problem like ? and iid = ?;";
		}
		else if (isObj == 1 && isSubj == 0 && topicname.equals(""))
		{
			query = "select problem as label, qid as value from (question where "
					+ "problem like ? and isObjective=true and iid = ?;";
		}
		else if (isObj == 0 && isSubj == 1 && topicname.equals(""))
		{
			query = "select problem as label, qid as value from question where "
					+ "problem like ? and isObjective=false and iid = ?;";
		}
		else if (isObj == 1 && isSubj == 1 && topicname.equals(""))
		{
			query = "select problem as label, qid as value from question where "
					+ "	problem like ? and iid = ?";
		}
		else
		{
			response.getWriter().print("\"data\":[],\"status\":true");
			return;
		}
		String res = "";
		if (!topicname.equals(""))
		{
			res = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {
							DbHelper.ParamType.INT,
//							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new String[] {quizid, topicname, partial, iid});
		}
		else
		{
			res = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new String[] {partial, iid});
		}
//		System.out.println(res);
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
