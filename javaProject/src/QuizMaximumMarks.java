

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class QuizMaximumMarks
 */
@WebServlet("/QuizMaximumMarks")
public class QuizMaximumMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizMaximumMarks() {
        super();
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String ID = (String) session.getAttribute("id");
		String  qzid = (String) request.getParameter("qzid");
		if(qzid == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Null value passed as request parameter\"}");
			return;
		}
		String query =  //TODO: verify query
				"select sum(maxmarks) as s from quizquestion where qzid = ? ";
		String res = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT,
						}, 
				new Object[] {qzid});
		
		PrintWriter out = response.getWriter();
		out.print(res);
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
