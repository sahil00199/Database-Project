

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;


/**
 * Servlet implementation class DeleteQuestion
 */
@WebServlet("/DeleteQuestion")
public class DeleteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteQuestion() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		//check if the question was actually created by the user!
		String query = "select qid from question where qid = ? and iid = ?";
		String iid = (String) session.getAttribute("id");
		String quid = (String) request.getParameter("qid");
		List<List<Object> > status = DbHelper.executeQueryList(query, new DbHelper.ParamType[] {DbHelper.ParamType.INT,
				DbHelper.ParamType.STRING}, new String[] {quid, iid});
		if (status.size() == 0)
		{
			ObjectMapper mapper = new ObjectMapper();
	    	ObjectNode node = mapper.createObjectNode();
	    	node.put(DbHelper.STATUS_LABEL, false);
           	response.getWriter().print(node.toString());
        	return;
		}
		
		String query1 = "delete from question where qid = ?";
		String query2 = "delete from option where qid = ?";
        String json1 = DbHelper.executeUpdateJson(query1, new DbHelper.ParamType[] {DbHelper.ParamType.INT}, new String[] {quid});
        String json2 = DbHelper.executeUpdateJson(query2, new DbHelper.ParamType[] {DbHelper.ParamType.INT}, new String[] {quid});
    	
        response.getWriter().print(json1);
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
