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
 * Servlet implementation class Signup
 */
@WebServlet("/SignupServlet")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String role = request.getParameter("role");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		if(role.equals("student")) {
			String query = "insert into student values(?, ?, ?, ?)";
			String json = DbHelper.executeUpdateJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {id, name, email, password});
			
			response.getWriter().print(json);
		}
		else if(role.equals("instructor")) {
			String query = "insert into instructor values(?, ?, ?, ?)";
			String json = DbHelper.executeUpdateJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {id, name, email, password});
			
			response.getWriter().print(json);
		}
		else {
			String query = "insert into TA values(?, ?, ?, ?)";
			String json = DbHelper.executeUpdateJson(query, 
					new DbHelper.ParamType[] {DbHelper.ParamType.STRING, 
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING,
							DbHelper.ParamType.STRING}, 
					new Object[] {id, name, email, password});
			
			response.getWriter().print(json);
		}
		
	}

}
