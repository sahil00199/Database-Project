

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
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		if(role.equals("student")) {
			String query = "select * from student where sID = ?";
			List<List<Object>> res = DbHelper.executeQueryList(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, 
					new Object[] {id});
			
			String dbPass = res.isEmpty()? null : (String)res.get(0).get(0);
			if(dbPass != null) {
				if(dbPass.equals(password)) {
					HttpSession session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("role", role);
					response.getWriter().print("{\"status\": true}");
				}
				else {
					response.getWriter().print("{\"status\": false, \"message\": \"Password incorrect\"}");
				}
				
			}
			else {
				response.getWriter().print("{\"status\": false, \"message\": \"Student with the entered ID is not registered\"}");
			}
		}
		else if(role.equals("instructor")) {
			String query = "select * from instructor where iID = ?";
			List<List<Object>> res = DbHelper.executeQueryList(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, 
					new Object[] {id});
			
			String dbPass = res.isEmpty()? null : (String)res.get(0).get(0);
			if(dbPass != null) {
				if(dbPass.equals(password)) {
					HttpSession session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("role", role);
					response.getWriter().print("{\"status\": true}");
				}
				else {
					response.getWriter().print("{\"status\": false, \"message\": \"Password incorrect\"}");
				}
				
			}
			else {
				response.getWriter().print("{\"status\": false, \"message\": \"Instructor with the entered ID is not registered\"}");
			}
		}
		else {
			String query = "select * from TA where taID = ?";
			List<List<Object>> res = DbHelper.executeQueryList(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, 
					new Object[] {id});
			
			String dbPass = res.isEmpty()? null : (String)res.get(0).get(0);
			if(dbPass != null) {
				if(dbPass.equals(password)) {
					HttpSession session = request.getSession();
					session.setAttribute("id", id);
					session.setAttribute("role", role);
					response.getWriter().print("{\"status\": true}");
				}
				else {
					response.getWriter().print("{\"status\": false, \"message\": \"Password incorrect\"}");
				}
				
			}
			else {
				response.getWriter().print("{\"status\": false, \"message\": \"TA with the entered ID is not registered\"}");
			}
		}
		
	}

}
