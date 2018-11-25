

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
 * Servlet implementation class AutoCompleteTopicCreateQuestion
 */
@WebServlet("/AutoCompleteTopicCreateQuestion")
public class AutoCompleteTopicCreateQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCompleteTopicCreateQuestion() {
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
		if (partial.equals("%")) {partial = "";}
		partial = partial + "%";
		String query = "select topicnm as label, topicnm as value from (select distinct topicname as topicnm from topic natural inner join teaches where iid = ? and topicname like ?) as temp;";
		String res = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING}, 
				new String[] {iid, partial});
		System.out.println(res);
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
