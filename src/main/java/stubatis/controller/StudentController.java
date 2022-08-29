package stubatis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;



import stubatis.bean.Student;
import stubatis.dao.Studentdao;
import stubatis.dao.Studentdao.student;
import stubatis.dto.StudentReq;
import stubatis.dto.StudentRes;


@Controller
public class StudentController {

	@Autowired
	Studentdao student;
	
	@Autowired
	@Qualifier("session")
	SqlSession session;
	

	@GetMapping("/registerStudent")
	public ModelAndView registerStudent() {
		return new ModelAndView("STU001","student",new Student());
	}
	
	@PostMapping("/StudentRegister")
	public String processingRegister(@ModelAttribute("student") Student stu,HttpServletRequest request) {
		
		
		
		if(stu.getName().equals("") || stu.getDob().equals("") || stu.getGender().equals("") ||
				stu.getPhone().equals("") || stu.getEducation().equals("") || stu.getAttend().length == 0) {
			request.setAttribute("error","fill all the information !! ");
			return "STU001";
			
		}
		
		
		StudentReq req = new StudentReq();
		
		req.setId(stu.getId());
		req.setName(stu.getName());
		req.setDob(stu.getDob());
		req.setGender(stu.getGender());
		req.setPhone(stu.getPhone());
		req.setEducation(stu.getEducation());
		String [] b = stu.getAttend();
		req.setAttend(b);

		
		student.insertStudent(req);
		
		for(String z: req.getAttend()) {
		student.insertcoursedetail(req.getId(),z);
		}
		int i = student.studentMaxID();
		i += 1;
		request.getSession().setAttribute("stuno",i);
		request.getSession().setAttribute("student",student.showStudent());	  
		return "redirect:/registerStudent";
	}
	
	@GetMapping("/showuser")
	public ModelAndView showUser(HttpServletRequest request) {
		request.getSession().setAttribute("student",student.showStudent());	
		return new ModelAndView("STU003","stu",new Student());
	}
	
	@PostMapping("/StudentSearch")
	public String searchstudent(@ModelAttribute("stu") Student stu,HttpServletRequest request) {
		
		StudentReq req = new StudentReq();
		req.setId(stu.getId());
		req.setName(stu.getName());
		req.setCourse(stu.getCourse());
		
		if(student.specificStudent(req) == null) {
			return "redirect:/showuser??notfound='not found!!'";
		}
		request.getSession().setAttribute("student",student.specificStudent(req));
		
	
		System.out.println(">>>"+req.getId());
		
		return "STU003";	
	}
	
	@GetMapping("/StudentUpdate/{id}")
	public ModelAndView update(@PathVariable("id") String id,HttpServletRequest request) {
		StudentReq req = new StudentReq();
		req.setId(id);
		
		request.setAttribute("stu", student.getonestudent(req));
		List<StudentRes> a = student.getonestudent(req);
		
		
		
		return new ModelAndView("STU002","stud",new Student());
	}
	
	@PostMapping("/StudentUpdate")
	public String updateProcessing(@ModelAttribute("stud") Student stu , HttpServletRequest request) {

		if(stu.getName().equals("") || stu.getDob().equals("") || stu.getGender().equals("") ||
				stu.getPhone().equals("") || stu.getEducation().equals("") || stu.getAttend().length == 0) {
			request.setAttribute("error","fill all the information !! ");
			StudentReq req = new StudentReq();
			req.setId(stu.getId());
			
			request.setAttribute("stu", student.specificStudent(req));
			
			
			return "STU002";
			
		}
		
		StudentReq req = new StudentReq();
		
		req.setId(stu.getId());
		req.setName(stu.getName());
		req.setDob(stu.getDob());
		req.setGender(stu.getGender());
		req.setPhone(stu.getPhone());
		req.setEducation(stu.getEducation());
		String [] b = stu.getAttend();
		req.setAttend(b);
	
		student.deletecoursedetail(req);
		student.updateStudent(req);
		for(String z : req.getAttend()) {
		student.insertcoursedetail(req.getId(),z );
		}
		
		request.getSession().setAttribute("student",student.showStudent());	
		return "redirect:/showuser";
	}
	
	
	
	@GetMapping("/StudentDelete/{id}")
	public String deleteStudent(@PathVariable("id") String id,HttpServletRequest request) {
		
		StudentReq req = new StudentReq();
		req.setId(id);
		student.deletecoursedetail(req);
		student.deleteStudent(req);
		
		request.getSession().setAttribute("student",session.getMapper(Studentdao.class).showStudent());	
		return "redirect:/showuser";
	}
	
	
	
	
	
}
