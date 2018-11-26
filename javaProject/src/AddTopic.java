

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class AddTopic
 */
@WebServlet("/AddTopic")
public class AddTopic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null)//not logged in!
		{
			response.sendRedirect("login.html");
			return;
		}
		if (!session.getAttribute("role").equals("instructor"))
		{
			response.sendRedirect("illegalAccess.html");
			return;
		}
		String iid = (String) session.getAttribute("id");
		String secid = (String) request.getParameter("secid");
		String topic = (String) request.getParameter("topic");
		//check if the question was actually created by the user!
		String query2 = "select * from topic natural join section where secid = ? and topicname = ?";
		List<List<Object>> l1 = DbHelper.executeQueryList(query2, new DbHelper.ParamType[] {DbHelper.ParamType.INT,
				DbHelper.ParamType.STRING}, new String[] {secid, topic});
//		System.out.println(l1.size());
		if (l1.size() > 0) {
			response.getWriter().print("{\"status\":false, \"message\":\"Topic already present\"}");
			return;
		}
		String query = "insert into topic(courseid, topicname)  select courseid, ? from section where secid = ?;";
//		System.out.println("not here");
		String res1 = DbHelper.executeUpdateJson(query, new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
				DbHelper.ParamType.INT}, new String[] {topic, secid});
//		System.out.println("##" + res1);
		response.getWriter().print(res1);
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
