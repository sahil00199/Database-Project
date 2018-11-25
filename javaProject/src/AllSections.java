import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AllSections
 */
@WebServlet("/AllSections")
public class AllSections extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllSections() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.getAttribute("id") == null) { //not logged in
			response.sendRedirect("LoginServlet");
		}
		//String instructorID = (String) session.getAttribute("id");
		String instructorID = (String) session.getAttribute("id");
		//String role = (String) session.getAttribute("role");
		String query = 
				"select c.courseID, coursename, s.secid, year, semester from section as s, course as c, teaches as t where iID = ? and s.secid = t.secid and s.courseid = c.courseid;";
		String res = DbHelper.executeQueryJson(query, 
				new DbHelper.ParamType[] {DbHelper.ParamType.STRING}, 
				new String[] {instructorID});
		
		PrintWriter out = response.getWriter();
		out.print(res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * For testing other methods in this class.
	 */
	public static void main(String[] args) throws ServletException, IOException {
		new AllSections().doGet(null, null);
	}

}
