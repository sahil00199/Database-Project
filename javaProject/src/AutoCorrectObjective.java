

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * Servlet implementation class AutoCorrectObjective
 */
@WebServlet("/AutoCorrectObjective")
public class AutoCorrectObjective extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutoCorrectObjective() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null || session.getAttribute("role") == null) {
			response.sendRedirect("login.html");
		}
		
		String id = (String) session.getAttribute("id");
		String role = (String) session.getAttribute("role");
		String qzid= (String) request.getParameter("qzid");
		if(!role.equals("instructor")) {
			response.sendRedirect("illegalAccess.html");
		}
		if(qzid == null) {
			response.sendRedirect("illegalAccess.html");
		}
		
		String secid = "";
		String query1 = "select secid from quiz where qzid = ?";
		List<List<Object>> output1 = DbHelper.executeQueryList(query1, new DbHelper.ParamType[]
				{DbHelper.ParamType.INT}, new String[] {qzid});  
		secid = Integer.toString((int) output1.get(0).get(0));
		
		String query2 = "select qid from (quizquestion natural inner join question) where qzid = ? and isobjective=true;";
		List<List<Object> > output2 = DbHelper.executeQueryList(query2, new DbHelper.ParamType[]
				{DbHelper.ParamType.INT}, new String[] {qzid});
		Dictionary answers = new Hashtable();
		Dictionary maxmarks = new Hashtable();
		
		for (int i = 0  ; i < output2.size(); i ++)
		{
			String query3 = "select optnum from option where qid = ? and iscorrect=true order by optnum asc;";
			List<List<Object>> output3 = DbHelper.executeQueryList(query3, new DbHelper.ParamType[]
					{DbHelper.ParamType.INT}, new String[] {Integer.toString((int) output2.get(i).get(0))});
			String answer = "";
			for (int j = 0 ; j < output3.size(); j ++)
			{
				answer += Integer.toString((int) output3.get(j).get(0) - 1);
				answer += " ";
			}
			String query3a = "select maxmarks from quizquestion where qid = ? and qzid = ?;";
			List<List<Object>> output3a = DbHelper.executeQueryList(query3a, new DbHelper.ParamType[]
					{DbHelper.ParamType.INT, DbHelper.ParamType.INT}, new String[] {Integer.toString((int) output2.get(i).get(0)),
							qzid});
			answers.put((int) output2.get(i).get(0), answer);
//			System.out.println(Float.toString((float) output3a.get(0).get(0)));
			maxmarks.put((int) output2.get(i).get(0), Float.toString((float) output3a.get(0).get(0)) );
		}
		
		String query4 = "select sid from takes where secid = ?";
		List<List<Object>>output4 = DbHelper.executeQueryList(query4, new DbHelper.ParamType[]
				{DbHelper.ParamType.INT}, new String[] {secid});
		
		for (int i = 0 ; i < output4.size() ; i ++)
		{
			for (int j = 0 ; j < output2.size() ; j++)
			{
				String student = (String) output4.get(i).get(0);
				int questionNo = (int) output2.get(j).get(0);
				String question = Integer.toString((int) output2.get(j).get(0));
				String query5 = "select answer from response where sid = ? and qid = ?";
				List<List<Object>> output5 = DbHelper.executeQueryList(query5, new DbHelper.ParamType[]
						{DbHelper.ParamType.STRING, DbHelper.ParamType.INT}, new String[] {student, question});
				String studentAnswer = "";
				if (output5.size() == 1)
				{
					studentAnswer = (String) output5.get(0).get(0);
				}
				boolean updateSuccessful = true;
				if (answers.get(questionNo).equals(studentAnswer))
				{
					String query6 = "update response set marksobtained=? where sid = ? and qid = ? and qzid = ?;";
					updateSuccessful = DbHelper.executeUpdateBool(query6, new
							DbHelper.ParamType[] {
									DbHelper.ParamType.FLOAT,
									DbHelper.ParamType.STRING,
									DbHelper.ParamType.INT,
									DbHelper.ParamType.INT
							}, new String[] {
									(String) maxmarks.get(questionNo),
									student,
									question,
									qzid
							});
					
				}
				else if (output5.size() == 1)
				{
					String query6 = "update response set marksobtained=? where sid = ? and qid = ? and qzid = ?;";
					updateSuccessful = DbHelper.executeUpdateBool(query6, new
							DbHelper.ParamType[] {
									DbHelper.ParamType.FLOAT,
									DbHelper.ParamType.STRING,
									DbHelper.ParamType.INT,
									DbHelper.ParamType.INT
							}, new String[] {
									"0",
									student,
									question,
									qzid
							});
				}
				if (!updateSuccessful)
				{
					ObjectMapper mapper = new ObjectMapper();
			    	ObjectNode node = mapper.createObjectNode();
			    	node.put(DbHelper.STATUS_LABEL, false);
		           	response.getWriter().print(node.toString());
		        	return;
				}
			}
		}
		ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	node.put(DbHelper.STATUS_LABEL, true);
       	response.getWriter().print(node.toString());
		
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
