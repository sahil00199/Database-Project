

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
 * Servlet implementation class CreateObjQuestion
 */
@WebServlet("/CreateObjQuestion")
public class CreateObjQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateObjQuestion() {
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
		String question = (String) request.getParameter("question");
		String iid = (String) session.getAttribute("id");
		
		String query = "insert into question(iid, problem, isobjective) values (?, ?, true)";
		boolean madeQuestion =  DbHelper.executeUpdateBool(query, new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
        		DbHelper.ParamType.STRING}, new String[] {iid, question});
		
		if (!madeQuestion)
		{
			ObjectMapper mapper = new ObjectMapper();
	    	ObjectNode node = mapper.createObjectNode();
	    	node.put(DbHelper.STATUS_LABEL, false);
    		response.getWriter().print(node.toString());
    		return;
		}
		
		String query2 = "select qid from question where iid = ? and problem = ? and isobjective = true";
		List<List<Object> > quidList = DbHelper.executeQueryList(query2, new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
				DbHelper.ParamType.STRING}, new String[] {iid, question});
		
		if (quidList.size() != 1 || quidList.get(0).size() != 1)
		{
			ObjectMapper mapper = new ObjectMapper();
	    	ObjectNode node = mapper.createObjectNode();
	    	node.put(DbHelper.STATUS_LABEL, false);
	    	node.put("message", "You have already created this question");
    		response.getWriter().print(node.toString());
    		System.out.println(node.toString());
    		return;
		}
		
		String qid = Integer.toString((int) quidList.get(0).get(0));
		String query3 = "insert into option values(?, ?, ?, ?)";
		boolean status;
		
		int length = Integer.parseInt((String) request.getParameter("length"));
		for (int i = 0 ; i < length ; i ++)
		{
			String param1 = "answer" + Integer.toString(i);
			String param2 = "isCorrect" + Integer.toString(i);
			String option = request.getParameter(param1);
			String isCorrect = request.getParameter(param2);
			if (isCorrect.equals("1"))
			{
				isCorrect = "true";
			}
			else 
			{
				isCorrect = "false";
			}
			status = DbHelper.executeUpdateBool(query3, new DbHelper.ParamType[] {DbHelper.ParamType.INT,
                DbHelper.ParamType.BOOL,
                DbHelper.ParamType.INT,
                DbHelper.ParamType.STRING}, new String [] {qid, isCorrect, Integer.toString(i), option});
			if (!status)
			{
				ObjectMapper mapper = new ObjectMapper();
		    	ObjectNode node = mapper.createObjectNode();
		    	node.put(DbHelper.STATUS_LABEL, false);
		    	response.getWriter().print(node.toString());
//		    	System.out.println(Integer.toString(i));
//	    		System.out.println(qid);
//	    		System.out.println(i);
//	    		System.out.println(param1);
//	    		System.out.println(param2);
//	    		System.out.println(option);
//	    		System.out.println(isCorrect);
	    		return;
			}
		}
		
		System.out.println("==============================================================================+++++");
		System.out.println((String) request.getParameter("tLength"));
		int tLength = Integer.parseInt((String) request.getParameter("tLength"));
		String query4 = "insert into questiontopic(topicid, qid) select distinct topicid, ? as qid from topic, teaches "
				+ "where iid = ? and topicname = ?;";
		for (int i = 0 ; i < tLength ; i ++)
		{
			String param1 = "topic" + Integer.toString(i);
			//String param2 = "isCorrect" + Integer.toString(i);
			//String option = request.getParameter(param1);
			//String isCorrect = request.getParameter(param2);
			String topicname = request.getParameter(param1);
			System.out.println(param1);
			System.out.println(topicname);
			
			status = DbHelper.executeUpdateBool(query4, new DbHelper.ParamType[] {DbHelper.ParamType.INT,
                DbHelper.ParamType.STRING,
                DbHelper.ParamType.STRING}, new String [] {qid, iid, topicname});
			if (!status)
			{
				ObjectMapper mapper = new ObjectMapper();
		    	ObjectNode node = mapper.createObjectNode();
		    	node.put(DbHelper.STATUS_LABEL, false);
		    	response.getWriter().print(node.toString());
//		    	System.out.println(Integer.toStrinsg(i));
//	    		System.out.println(qid);
//	    		System.out.println(i);
//	    		System.out.println(param1);
//	    		System.out.println(param2);
//	    		System.out.println(option);
//	    		System.out.println(isCorrect);
	    		return;
			}
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put(DbHelper.STATUS_LABEL, true);
    	response.getWriter().print(node.toString());
		System.out.println(node.toString());
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
