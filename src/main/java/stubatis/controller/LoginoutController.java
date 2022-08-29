package stubatis.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import stubatis.bean.User;
import stubatis.dao.LoginLogoutdao;
import stubatis.dao.Studentdao;
import stubatis.dao.Userdao;
import stubatis.dto.CourseRes;
import stubatis.dto.StudentRes;
import stubatis.dto.UserReq;
import stubatis.dto.UserRes;



@Controller
public class LoginoutController {

	@Autowired
	LoginLogoutdao logindao;
	
	@Autowired
	Studentdao student;
	
	@Autowired
	@Qualifier("session")
	SqlSession session;
	
	@Autowired
	@Qualifier("course")
	SqlSession course;
	
	
	
	@GetMapping("/")
	public ModelAndView welcome() {
		return new ModelAndView("LGN001","user",new User());	
	}
	
	@PostMapping("/Login")
	public String login(@ModelAttribute("user") User user,HttpServletRequest request) {
		UserReq req = new UserReq();
		req.setId(user.getId());
		req.setPassword(user.getPassword());	  
		
	  
		if(logindao.login(req) == null) {
			return "redirect:/?error='not found!!'";
		}
	
		 if((Integer)course.selectOne("coursemap.coureMaxid")!=0) {
				int i = (Integer)course.selectOne("coursemap.coureMaxid");
				i+= 1;
				
				request.getSession().setAttribute("cid",i);
			} 
		 
		 
		 if(student.studentMaxID()!=0) {
				int i = student.studentMaxID();
				i+= 1;
				
				request.getSession().setAttribute("stuno",i);
			}
		
		 List<CourseRes> a = course.selectList("coursemap.getallcourse");
			a.forEach(n -> System.out.println(n));
		
		request.getSession().setAttribute("course",course.selectList("coursemap.getallcourse"));
		request.getSession().setAttribute("login",logindao.login(req));
		request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
		request.getSession().setAttribute("student",student.showStudent());	
		return "MNU001";
	}
	
	
	
	@GetMapping("/Logout")
	public String logout(HttpServletRequest request) {
		request.getSession(false);
		request.getSession().invalidate();
		session.close();
		
		return "redirect:/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
