package com.artisan.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.artisan.dao.UserDao;
import com.artisan.model.User;
import com.artisan.util.DbUtil;
import com.artisan.util.StringUtil;

public class LoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DbUtil dbUtil = new DbUtil();
	private UserDao userDao = new UserDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		if(StringUtil.isEmpty(userName)||StringUtil.isEmpty(password)){
			request.setAttribute("error_msg", "用户名或密码不能为空！");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		User user = new User(userName, password);
		Connection con = null;
		try {
			con = dbUtil.getCon();
			User currentUser = userDao.login(con, user);
			if(currentUser == null){
				request.setAttribute("error_msg", "用户名或密码错误！");
				//服务器端跳转
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}else{
				//登录成功，写入session
				HttpSession session = request.getSession();
				session.setAttribute("currentUser", currentUser);
				//客户端跳转
				response.sendRedirect("main.jsp");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
