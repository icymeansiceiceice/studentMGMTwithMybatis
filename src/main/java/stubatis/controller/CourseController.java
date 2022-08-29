package stubatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



import stubatis.bean.Course;
import stubatis.dto.CourseReq;
import stubatis.dto.CourseRes;

@Controller
public class CourseController {

	@Autowired
	@Qualifier("course")
	SqlSession coursedao;
	
	@GetMapping("/courseRegister")
	public ModelAndView courseRegister() {
		
		return new ModelAndView("BUD003","course",new Course());
	}
	
	
	@PostMapping("/CourseRegister")
	public String processingResgister(@ModelAttribute("course") Course course,HttpServletRequest request) {
		
		if(request.getParameter("id").equals("") || request.getParameter("name").equals("")) {
			request.setAttribute("error","fill all information");
			return "BUD003";
			}
		CourseReq req = new CourseReq();
		req.setId(course.getId());
		req.setName(course.getName());
		
		
		coursedao.insert("coursemap.insertCourse",req);
		
		int i = (Integer)coursedao.selectOne("coursemap.coureMaxid");
		i += 1;
		request.getSession().setAttribute("cid",i);
		request.getSession().setAttribute("course",coursedao.selectList("coursemap.getallcourse"));
		
		coursedao.commit();
		return "redirect:/courseRegister";
	}
	
	
	
	
	
	
	
	
	
	
}
