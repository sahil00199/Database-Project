import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.json.*;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Servlet implementation class CreateQuiz
 */
@WebServlet("/CreateQuiz")
public class CreateQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuiz() {
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
		String instructorID = (String) session.getAttribute("id");
		String  qzname = (String) request.getParameter("coursen");
		String starttime = (String)request.getParameter("starttime");
        String duration = (String)request.getParameter("duration");
		String maxmarks = (String)request.getParameter("maxmarks");
		String weightage = (String)request.getParameter("weightage");
		String secid = (String) request.getParameter("secid");
		if(qzname == null || starttime == null || duration == null || maxmarks == null || weightage == null || secid  == null) {
			response.getWriter().print("{\"status\": false, \"message\": \"Null value passed as request parameter\"}");
			return;
		}
//		System.out.println("reached here");
		String query =  //TODO: verify query
				"insert into quiz(qzname, secid, start,  duration) values (?, ?, CAST(? AS TIMESTAMP),  CAST(? AS INTERVAL))";
		String res = DbHelper.executeUpdateJson(query, 
				new DbHelper.ParamType[] {
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING,
						DbHelper.ParamType.STRING}, 
				new Object[] {qzname, secid, starttime, duration});
		
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