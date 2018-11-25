import java.io.IOException;
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
 * Servlet implementation class CreateSection
 */
@WebServlet("/CreateSection")
public class CreateSection extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateSection() {
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
		String courseID = (String)request.getParameter("courseID");
        String year = (String)request.getParameter("year");
		String semester = (String)request.getParameter("semester");
		int recordsUpdated;
		try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
        {
            conn.setAutoCommit(false);
            DbHelper.ParamType[]paramTypes = new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING};
			String[] params = new String[] {courseID, year, semester};
            try(PreparedStatement stmt = conn.prepareStatement("insert into section(courseid, year, semester) values (?, ?, ?)")) {
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
        	response.getWriter().print(DbHelper.errorJson(e.getMessage().toString()));
            return;
        }
    	boolean status = recordsUpdated == 0 ? false : true;
    	ObjectMapper mapper = new ObjectMapper();
    	ObjectNode node = mapper.createObjectNode();
    	if (recordsUpdated == 0)
        {
    		node.put(DbHelper.STATUS_LABEL, status);
           	response.getWriter().print(node.toString());
        	return;
        }
        
        ResultSet rs = null;
        String secid;
        try (Connection conn = DriverManager.getConnection(Config.url, Config.user, Config.password))
        {
            conn.setAutoCommit(false);
            DbHelper.ParamType[]paramTypes = new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
						DbHelper.ParamType.INT,
						DbHelper.ParamType.STRING};
			String[] params = new String[] {courseID, year, semester};
            try(PreparedStatement stmt = conn.prepareStatement("select secid from section where courseid = ? and year = ? and semester = ?")) {
            	DbHelper.setParams(stmt, paramTypes, params);
            	rs = stmt.executeQuery();
            	int maxx = 1000000; // will not work is we create more than 100000 sections
            	if (!rs.next())
            	{
            		node.put(DbHelper.STATUS_LABEL, false);
            		response.getWriter().print(node.toString());
            		return;
            	}
            	secid = rs.getString(1);
            	maxx = Integer.parseInt(secid);
            	while (rs.next())
            	{
            		int current = Integer.parseInt(rs.getString(1));
            		System.out.println(current);
            		if(current > maxx)
            		{
            			maxx = current;
            		}
            	}
            	secid = Integer.toString(maxx);
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
        	response.getWriter().print(DbHelper.errorJson(e.getMessage().toString()));
            return;
        }
        //System.out.println(secid);
        
        String finalQuery = "insert into teaches values(?, ?)";
        String finalJson = DbHelper.executeUpdateJson(finalQuery, new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
        		DbHelper.ParamType.INT}, new String[] {instructorID, secid});
    	
        System.out.println(instructorID);
        System.out.println(secid);
        System.out.println(finalJson);

        response.getWriter().print(finalJson);
    	return;
		// String year = (String)request.getParameter("year");
		// String semester = (String)request.getParameter("semester");
		// String query = "insert into section(courseid, year, semester) values (?, ?, ?)";
		// String originalJson = DbHelper.executeUpdateJson(query, 
		// 		new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
		// 				DbHelper.ParamType.INT,
		// 				DbHelper.ParamType.STRING},
		// 		new String[] {courseID, year, semester});
		// query = "select secid from section where courseid = ? and year = ? and semester = ?";
		// String json = DbHelper.executeQueryJson(query, 
		// 		new DbHelper.ParamType[] {DbHelper.ParamType.STRING,
		// 				DbHelper.ParamType.INT,
		// 				DbHelper.ParamType.STRING},
		// 		new String[] {courseID, year, semester});
		// 		System.out.println(json);
		// 		try
		// 		{
		// 			JSONObject jsonObj = new JSONObject(json);
		// 		}
		// 		catch(Exception e)
		// 		{
		// 			System.out.println("Json error");
		// 		//	throw e;
		// 		}
//		if (json["status"] == false)
//		{
//			response.getWriter().print(json);
//			return;
//		}
		//{"data":[{"secid":5}],"status":true}
		//response.getWriter().print(originalJson);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}