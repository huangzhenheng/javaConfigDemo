package com.hzh.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.hzh.pojo.User;

//@RestController  @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用
@Controller
public class HomeController {

	@RequestMapping(value = "/home2", method = RequestMethod.GET)
	public String home2() {
		return "home2";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/58", method = RequestMethod.GET)
	public String login58() {
		return "58/index";
	} 
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String login( RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		user.setName("Rose");
		modelAndView.addObject("name", "黄振衡");
		modelAndView.addObject("user", user);

		List<User> users = Lists.newArrayList();
		for (int i = 0; i < 5; i++) {
			User u = new User();
			u.setName("姓名" + i);
			users.add(u);
		}

		modelAndView.addObject("userList", users);
		return "redirect:/home";
	}
	
	 /**
     * 若登录成功去home页
     */
    @RequestMapping("/home")
    public String home() {
        return "home";
    }

	
	
	@ResponseBody
	@RequestMapping("/p.png")
	public String getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ConfigurableCaptchaService service = new ConfigurableCaptchaService();
		service.setColorFactory(new SingleColorFactory(new Color(26, 69, 226)));
		service.setFilterFactory(new CurvesRippleFilterFactory(service.getColorFactory()));

		RandomWordFactory wordFactory = new RandomWordFactory();
		wordFactory.setMinLength(4);
		wordFactory.setMaxLength(4);
		service.setWordFactory(wordFactory);

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();
		String captcha = EncoderHelper.getChallangeAndWriteImage(service, "png", outputStream);

//		HttpSession session = request.getSession();
//		session.setAttribute("captcha", captcha);

		outputStream.flush();
		outputStream.close();

		return "success";
	}

	@RequestMapping("/captcha")
	public String getCaptcha(HttpServletRequest request, String hzh) throws IOException {
		System.err.println("hzh:" + hzh);

		HttpSession session = request.getSession();
		String capcha = (String) session.getAttribute("captcha");
		System.err.println(capcha);
		return "redirect:/hello";
	}
}
