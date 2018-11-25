

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class TASections
 */
@WebServlet("/TASections")
public class TASections extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TASections() {
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
		if(!role.equals("TA")) {
			response.getWriter().print("{\"status\": false, \"message\": \"User is not a TA\"}");
		}
		else {
			String query = 
					"select courseID, secID, coursename, year, semester "
					+ "from (( course natural join section) natural join tasection) natural join ta "
					+ "where taid = ?;";
			String res = DbHelper.executeQueryJson(query, 
					new DbHelper.ParamType[] {
							DbHelper.ParamType.STRING,}, 
					new String[] {id});
			
			PrintWriter out = response.getWriter();
			out.print(res);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
