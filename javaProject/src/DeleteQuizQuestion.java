

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
 * Servlet implementation class DeleteQuizQuestion
 */
@WebServlet("/DeleteQuizQuestion")
public class DeleteQuizQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteQuizQuestion() {
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
		//check if the question was actually created by the user!
		String query = "select qid from question where qid = ? and iid = ?";
		String iid = (String) session.getAttribute("id");
		String quid = (String) request.getParameter("qid");
		String qzid = (String) request.getParameter("qzid");
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
		
		String query1 = "delete from quizquestion where qid = ? and qzid = ?";
        String json1 = DbHelper.executeUpdateJson(query1, new DbHelper.ParamType[] {DbHelper.ParamType.INT,DbHelper.ParamType.INT}, new String[] {quid,qzid});
      ;
    	
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
