

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ShowTaQues
 */
@WebServlet("/ShowTaQues")
public class ShowTaQues extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowTaQues() {
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
		String qid= (String) request.getParameter("qid");
//		String taid= (String) request.getParameter("taid");
//		String secid = "";
		if(!role.equals("instructor")) {
			response.sendRedirect("illegalAccess.html");
		}
		if(qzid == null || qid == null) {
			response.sendRedirect("illegalAccess.html");
		}
		String query2 = "select distinct taid as id, name from checked natural join ta where qid = ? and qzid = ?;";
		String res1 = DbHelper.executeQueryJson(query2, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.INT,
						DbHelper.ParamType.INT}, 
				new Object[] {qid, qzid});
//		System.out.println(secid);
		PrintWriter out = response.getWriter();
		out.print(res1);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
