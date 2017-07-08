package com.lhpc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhpc.dao.BookedMapper;
import com.lhpc.dao.ExtractCashMapper;
import com.lhpc.dao.OrderMapper;
import com.lhpc.dao.StrokeMapper;
import com.lhpc.dao.UserMapper;
import com.lhpc.model.Booked;
import com.lhpc.model.ExtractCash;
import com.lhpc.model.Order;
import com.lhpc.model.Stroke;
import com.lhpc.model.User;
import com.lhpc.service.IPersonalService;
import com.lhpc.util.ConfigUtil;
import com.lhpc.util.GsonUtil;
import com.lhpc.util.OrderNumUtil;
import com.lhpc.util.ResponseCodeUtil;
import com.lhpc.util.SendSMSUtil;

@Service
@Transactional
public class PersonalServiceImpl implements IPersonalService {

	@Autowired
	private BookedMapper bookedMapper;
	@Autowired
	private StrokeMapper strokeMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ExtractCashMapper extractCashMapper;
	@Autowired
	private HttpSession session;

	@Override
	public ResponseEntity<String> agreedBook(String bookedId, String strokeId) {
		User user = (User) session.getAttribute("CURRENT_USER");
		Stroke stroke = strokeMapper.selectByPrimaryKey(Integer
				.parseInt(strokeId));
		Booked booked = bookedMapper.selectByPrimaryKey(Integer
				.parseInt(bookedId));
		if (stroke.getSeats() < booked.getBookedSeats()) {
			return GsonUtil.getJson(ResponseCodeUtil.SEAT_LACK,
					"座位数不足,请选择其他预定单!");
		}
		int seats = -booked.getBookedSeats();
		strokeMapper.update4Seats(Integer.parseInt(strokeId), seats);
		Order order = new Order();
		String orderNum = OrderNumUtil.getOrderNum(user);
		order.setBookedId(Integer.parseInt(strokeId));
		order.setOrderNum(orderNum);
		order.setCreateTime(new Date());
		orderMapper.insert(order);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", user.getUserName());
		map.put("carType", user.getCarType());
		map.put("count", strokeMapper.selectCount(stroke));
		map.put("upAddreess", booked.getUpAddress());
		map.put("downAddress", booked.getDownAddress());
		map.put("seats", booked.getBookedSeats());
		map.put("price", stroke.getPrice());
		map.put("orderNum", orderNum);
		return GsonUtil.getJson(ResponseCodeUtil.SUCCESS, "你已同意该预定!", map);
	}

	@Override
	public ResponseEntity<String> denialBook(String bookedId, String strokeId) {
		User user = (User) session.getAttribute("CURRENT_USER");
		Stroke stroke = strokeMapper.selectByPrimaryKey(Integer
				.parseInt(strokeId));
		Booked booked = bookedMapper.selectByPrimaryKey(Integer
				.parseInt(bookedId));
		int result = 0;
		if (booked != null && stroke != null) {
			booked.setUnbookedTime(new Date());
			booked.setUnbookedFlag(1);
			booked.setUnbookedId(stroke.getUserId());
			booked.setIsEnable(0);
			result = bookedMapper.updateByPrimaryKeySelective(booked);
		}
		if (result > 0) {
			User passenger = userMapper.selectByPrimaryKey(booked.getUserId());

			SendSMSUtil.sendSMS(passenger.getUserMobile(),
					ConfigUtil.DENIAL_BOOK, "#name#=" + user.getUserName());
			return GsonUtil.getJson(ResponseCodeUtil.SUCCESS, "拒绝成功!");
		} else {
			return GsonUtil.getJson(ResponseCodeUtil.SYSTEM_ERROR,
					"服务器繁忙,请稍后重试!");
		}
	}

	@Override
	public ResponseEntity<String> extractCash(String money,String openID) {
		User user = userMapper.selectByOpenID(openID);
		if (user.getWallet() < Double.parseDouble(money)) {
			return GsonUtil.getJson(ResponseCodeUtil.WALLET_EMPTY, "您的余额不足,不能提现!");
		}
		User u = new User();
		u.setUserId(user.getUserId());
		u.setWallet(user.getWallet() - Double.parseDouble(money));
		userMapper.updateWalletByUserId(u);
		ExtractCash extractCash = new ExtractCash();
		extractCash.setUserId(user.getUserId());
		extractCash.setExtractMoney(Double.parseDouble(money));
		extractCash.setCreateTime(new Date());
		extractCashMapper.insert(extractCash);
		// TODO 推送
		return GsonUtil.getJson(ResponseCodeUtil.SUCCESS, "提现成功!");
	}

}
