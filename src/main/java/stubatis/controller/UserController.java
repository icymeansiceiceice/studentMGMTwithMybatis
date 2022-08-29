package stubatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



import stubatis.bean.User;
import stubatis.dao.LoginLogoutdao;
import stubatis.dao.Userdao;
import stubatis.dto.UserReq;
import stubatis.dto.UserRes;

//@MappedTypes(User.class)
@Controller
public class UserController {

	
	@Autowired
	@Qualifier("session")
	SqlSession session;
	
	@Autowired
	LoginLogoutdao logindao;
	
	@GetMapping("/URegister")
	public ModelAndView register(HttpServletRequest request) {
		List<UserRes> list = session.getMapper(Userdao.class).showUser();
		request.setAttribute("user",list);
		return new ModelAndView("USR001","user",new User());		
	}
	
	
	@PostMapping("/UserReigster")
	public String proessingRegister(@ModelAttribute("user") User user,HttpServletRequest request) {
		
		if(user.getName().equals("") || user.getEmail().equals("") || user.getConfirm().equals("") ||
				user.getPassword().equals("") || user.getRole().equals("")) {
			request.setAttribute("error","fill all the requirement");
			return "USR001";
			
		}
	
		if(!user.getPassword().equals(user.getConfirm())) {
			request.setAttribute("error","doesnt match with password!!");
			return "USR001";
		}
		
		UserReq req= new UserReq();
		
		req.setName(user.getName());
		req.setEmail(user.getEmail());
		req.setPassword(user.getPassword());
		req.setRole(user.getRole());
		
		//session.getMapper(Userdao.class).insertUser(req);
		session.getMapper(Userdao.class).insertUser(req);
		
		
		request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
		
		session.commit();
		return "redirect:/UserShow";
	}
	
	
	@GetMapping("/UserShow")
	public ModelAndView searchUser(HttpServletRequest request) {
		request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
		return new ModelAndView("USR003","user", new User());
	}
	
	@PostMapping("/processingUsearch")
	public String processingSearch(@ModelAttribute("user") User user,HttpServletRequest request) {
		UserReq req = new UserReq();
		req.setId(user.getId());
		req.setName(user.getName());
		
		if(session.getMapper(Userdao.class).specificUser(req)==null) {
			return "redirect:/UserShow?notfound='not found!!'";
		 }
		request.getSession().setAttribute("user", session.getMapper(Userdao.class).specificUser(req));
		
		return "USR003";
	}
	
	@GetMapping("/updateUser/{id}")
	public ModelAndView updateUser(@PathVariable("id")String id,HttpServletRequest request) {

		UserReq req = new UserReq();
		req.setId(id);
		request.setAttribute("us",session.getMapper(Userdao.class).specificUser(req));
				
		return new ModelAndView("USR002","user", new User());
	}
	
	@PostMapping("/UserUpdate")
	public String processingUpdate(@ModelAttribute("user")User user,HttpServletRequest request) {
		
		if(user.getName().equals("") || user.getEmail().equals("") || user.getConfirm().equals("") ||
				user.getPassword().equals("") || user.getRole().equals("")) {
			request.setAttribute("error","fill all the requirement");
			return "USR002";
			
		}
	
		if(!user.getPassword().equals(user.getConfirm())) {
			request.setAttribute("error","doesnt match with password!!");
			return "USR002";
		}
		
		
		UserReq req= new UserReq();
		req.setId(user.getId());
		req.setName(user.getName());
		req.setEmail(user.getEmail());
		req.setPassword(user.getPassword());
		req.setRole(user.getRole());
		
		session.getMapper(Userdao.class).updateUser(req);
		
		UserRes res=  (UserRes) request.getSession().getAttribute("login");
		
		if(req.getId().equals(res.getId())) {
			request.getSession().setAttribute("login",logindao.login(req));
		}
		request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
		
		session.commit();
		return "redirect:/UserShow";
	}
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id") String id,HttpServletRequest request) {
		 UserRes res  =(UserRes) request.getSession().getAttribute("login");
		 UserReq req1 = new UserReq();
		 req1.setId(id);
		 if(res.getId().equals(id)) {
			 request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
				return "redirect:/UserShow?notfoun='u cannot delete'"; 
		 }
		session.getMapper(Userdao.class).deleteUser(req1);
		request.getSession().setAttribute("user",session.getMapper(Userdao.class).showUser());
		session.commit();
		session.close();
		return "redirect:/UserShow";
	}
	
	
	
	
	
	
	
}
