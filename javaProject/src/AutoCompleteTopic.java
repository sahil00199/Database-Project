

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
 * Servlet implementation class AutoCompleteTopic
 */
@WebServlet("/AutoCompleteTopic")
public class AutoCompleteTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteTopic() {
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
		String partial = (String) request.getParameter("partial");
		String qzid = (String) request.getParameter("qzid");
		if (partial.equals("%")) {partial = "";}
		partial = partial + "%";
		//System.out.println(qzid);
		String initialQuery = " select courseid from quiz natural join section where qzid = ?;";
		List<List<Object> > returnValue = DbHelper.executeQueryList(initialQuery, new DbHelper.ParamType[] {DbHelper.ParamType.INT},
				new String[] {qzid});
		String courseid = (String) returnValue.get(0).get(0);
		//System.out.println(courseid);
		String query = "select topicname as label, topicname as value "
				+ "from topic "
				+ "where courseid = ? and topicname like ?";
//		System.out.println(courseid);
//		System.out.println(partial);
		String res = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING}, 
				new String[] {courseid, partial});
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
