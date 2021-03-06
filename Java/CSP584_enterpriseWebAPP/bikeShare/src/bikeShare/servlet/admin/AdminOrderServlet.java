package bikeShare.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bikeShare.common.PageUtil;
import bikeShare.dao.ActivityDaoImpl;
import bikeShare.dao.CategoryDaoImpl;
import bikeShare.dao.GoodDaoImpl;
import bikeShare.dao.ImagePathDaoimpl;
import bikeShare.dao.OrderDaoImpl;
import bikeShare.dao.UserDaoImpl;
import bikeShare.model.Activity;
import bikeShare.model.Category;
import bikeShare.model.Goods;
import bikeShare.model.ImagePath;
import bikeShare.model.Order;
import bikeShare.model.User;

/**
 * Servlet implementation class AdminOrderServlet
 */
@WebServlet("/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminOrderServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ImagePathDaoimpl ipdi = new ImagePathDaoimpl();
		GoodDaoImpl gdi = new GoodDaoImpl();
		OrderDaoImpl odi = new OrderDaoImpl();
		ActivityDaoImpl activityDaoImpl = new ActivityDaoImpl();
		CategoryDaoImpl cdi = new CategoryDaoImpl();
		UserDaoImpl udi = new UserDaoImpl();

		String flag = request.getParameter("flag");
		if (flag.equals("orderSend")) {
			String pn = request.getParameter("pn");
			int pageNo = 1;
			if (pn != null) {
				pageNo = Integer.parseInt(pn);
				System.out.println("pageNow" + pageNo);
			}

			String sql = "select * from bike_sharing.indent where isSend=?";
			String paras[] = { "0" };
			List<Order> orderList = odi.findOrderListByStatue(paras, sql);
			for (Integer i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				User user = udi.queryUserByPramryKey(order.getUserid() + "");
				order.setUser(user);
				Goods goods = gdi.queryGoodByPrimaryKey(order.getGoodid() + "");
				ImagePath imagePathList = ipdi.queryByImageId(goods.getGoodsid() + "");
				goods.setImgepath(imagePathList.getPath());
				Activity activity = activityDaoImpl.queryActivityByPramaryKey(goods.getActivityid() + "");
				goods.setActivity(activity);
				Category category = cdi.queryCategoryByPramryKey(goods.getCategoryId() + "");
				goods.setCategory(category);
				order.setGood(goods);

				orderList.set(i, order);
			}
			PageUtil pageInfo = new PageUtil(2, orderList.size());
			pageInfo.setData(orderList);
			pageInfo.setPageNo(pageNo);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher("/WEB-INF/views/adminAllOrder.jsp").forward(request, response);

		}
		if (flag.equals("sendGoods")) {
			String orderid = request.getParameter("orderid");
			String paras[] = { "1", "0", "0", orderid };
			odi.SetOrderALlStatue(paras);

			request.getRequestDispatcher("AdminOrderServlet?flag=orderSend").forward(request, response);

		}
		if (flag.equals("orderReceive")) {
			String pn = request.getParameter("pn");
			int pageNo = 1;
			if (pn != null) {
				pageNo = Integer.parseInt(pn);
				System.out.println("pageNow" + pageNo);
			}

			String sql = "select * from bike_sharing.indent where isSend=? and isReceive=?";
			String paras[] = { "1", "0" };
			List<Order> orderList = odi.findOrderListByStatue(paras, sql);
			for (Integer i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				User user = udi.queryUserByPramryKey(order.getUserid() + "");
				order.setUser(user);
				Goods goods = gdi.queryGoodByPrimaryKey(order.getGoodid() + "");
				ImagePath imagePathList = ipdi.queryByImageId(goods.getGoodsid() + "");
				goods.setImgepath(imagePathList.getPath());
				Activity activity = activityDaoImpl.queryActivityByPramaryKey(goods.getActivityid() + "");

				goods.setActivity(activity);
				Category category = cdi.queryCategoryByPramryKey(goods.getCategoryId() + "");
				goods.setCategory(category);
				order.setGood(goods);
				orderList.set(i, order);
			}
			PageUtil pageInfo = new PageUtil(2, orderList.size());
			pageInfo.setData(orderList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher("/WEB-INF/views/adminOrderReceive.jsp").forward(request, response);

		}
		if (flag.equals("Ordercomplete")) {
			String pn = request.getParameter("pn");
			int pageNo = 1;
			if (pn != null) {
				pageNo = Integer.parseInt(pn);
				System.out.println("pageNow" + pageNo);
			}

			String sql = "select * from indent where isSend=? and isReceive=? and isComplete=?";
			String paras[] = { "1", "1", "1" };
			List<Order> orderList = odi.findOrderListByStatue(paras, sql);
			for (Integer i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				User user = udi.queryUserByPramryKey(order.getUserid() + "");
				order.setUser(user);
				Goods goods = gdi.queryGoodByPrimaryKey(order.getGoodid() + "");
				ImagePath imagePathList = ipdi.queryByImageId(goods.getGoodsid() + "");
				goods.setImgepath(imagePathList.getPath());
				Activity activity = activityDaoImpl.queryActivityByPramaryKey(goods.getActivityid() + "");
				goods.setActivity(activity);
				Category category = cdi.queryCategoryByPramryKey(goods.getCategoryId() + "");
				goods.setCategory(category);
				order.setGood(goods);
				orderList.set(i, order);
			}
			PageUtil pageInfo = new PageUtil(2, orderList.size());
			pageInfo.setData(orderList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher("/WEB-INF/views/adminOrderComplete.jsp").forward(request, response);

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
