package so.xunta.response;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.json.JSONObject;

import so.xunta.author.evaluation.SearchAuthor_Evaluation;

public class AuthorEvaluationResponse extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取网站名，作者名
		request.setCharacterEncoding("utf-8");
		String author=request.getParameter("author");
		String site=request.getParameter("site");
		response.setContentType("application/x-json"); 
		//返回　json格式数据
		try {
			JSONObject jsonObj=SearchAuthor_Evaluation.searchAuthor(author, site);
			response.getWriter().write(jsonObj.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
