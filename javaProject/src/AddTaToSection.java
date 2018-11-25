

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddTaToSection
 */
@WebServlet("/AddTaToSection")
public class AddTaToSection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTaToSection() {
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
		String secid = (String) request.getParameter("secid");
		String taid = (String) request.getParameter("taid");
		int recordsUpdated = -2;
		try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
        {
            conn.setAutoCommit(false);
            DbHelper.ParamType[]paramTypes = new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT};
			String[] params = new String[] {taid, secid};
            try(PreparedStatement stmt = conn.prepareStatement("insert into tasection values (?, ?)")) {
            	DbHelper.setParams(stmt, paramTypes, params);
            	recordsUpdated = stmt.executeUpdate();
            	conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally{
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
        	System.out.println(DbHelper.errorJson(e.getMessage().toString()));
        	response.getWriter().print(DbHelper.errorJson(e.getMessage().toString()));
            return;
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
