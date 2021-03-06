package bikeShare.servlet.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bikeShare.common.PageUtil;
import bikeShare.common.RespondTool;
import bikeShare.dao.AdminDaoImpl;
import bikeShare.dao.GoodDaoImpl;
import bikeShare.dao.ImagePathDaoimpl;
import bikeShare.dao.UserDaoImpl;
import bikeShare.model.Admin;
import bikeShare.model.Goods;
import bikeShare.model.User;

/**
 * Servlet implementation class AdminUserDaoImpl
 */
@WebServlet("/AdminUserServlet")
public class AdminUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminUserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String flag = request.getParameter("flag");

		AdminDaoImpl adi = new AdminDaoImpl();
		UserDaoImpl udi = new UserDaoImpl();
		if (flag.equals("adminLoginOut")) {
			HttpSession session = request.getSession();
			session.removeAttribute("admin");
			request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
			return;
		}
		if (flag.equals("confirmLogin")) {
			String dept = request.getParameter("dept");
			String adminname = request.getParameter("adminname");
			String password = request.getParameter("password");
			if (dept.equals("1")) {
				if (adi.queryByAdminNamePass(adminname, password, dept)) {
					Admin admin = adi.getAdmintBean(adminname);
					request.getSession().setAttribute("admin", admin);

					request.getRequestDispatcher("/WEB-INF/views/userManage.jsp").forward(request, response);
					return;
				} else {
					request.setAttribute("errorMsg", "Wrong Adminname or password!");
					request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
				}
			} else {

				if (adi.queryByAdminNamePass(adminname, password, dept)) {
					Admin admin = adi.getAdmintBean(adminname);
					request.getSession().setAttribute("admin", admin);

					request.getRequestDispatcher("/AdminGoodServlet?flag=goodshow").forward(request, response);
					return;
				} else {
					request.setAttribute("errorMsg", "Wrong username or password!");
					request.getRequestDispatcher("/WEB-INF/views/adminLogin.jsp").forward(request, response);
				}
			}
		}
		if (flag.equals("showjson")) {

			String pn = request.getParameter("page");
			int pageNo = 1;
			if (pn != null) {
				pageNo = Integer.parseInt(pn);
				System.out.println("pageNow" + pageNo);
			}

			PageUtil page = adi.getAdminUserByPage(pageNo, 5);
			page.setPageNo(pageNo);
			List<Admin> adminList = (List<Admin>) page.getData();
			JSONObject jsonObject = new JSONObject();
			if (null != adminList) {

				jsonObject.put("data", adminList);

			}

			String returnString = JSON.toJSONString(jsonObject);

			PrintWriter pw = null;
			try {
				response.setCharacterEncoding("UTF-8");
				pw = response.getWriter();
				pw.write(returnString);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
		if (flag.equals("deleteAdmin")) {

			String adminId = request.getParameter("adminId");

			if (adi.deleteAdmin(adminId) == 1) {
				RespondTool.getNewsString(request, response, "Delete success", 100);
			} else {
				RespondTool.getNewsString(request, response, "Delete fail", 200);
			}

		}
		if (flag.equals("ShowClient")) {
			List<User> ulist = udi.queryAllUser();
			request.setAttribute("ulist", ulist);
			request.getRequestDispatcher("/WEB-INF/views/ClientManage.jsp").forward(request, response);

		}
		if (flag.equals("deleteClient")) {

			String userid = request.getParameter("userid");
			String paras[] = { userid };
			if (udi.deleteUser(paras) == 1) {
				request.setAttribute("msg", "Delete Success");
				request.getRequestDispatcher("/AdminUserServlet?flag=ShowClient").forward(request, response);
			}

		}

		if (flag.equals("showAdmin")) {
			request.getRequestDispatcher("/WEB-INF/views/userManage.jsp").forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
