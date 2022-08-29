package stubatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import com.mysql.cj.x.protobuf.MysqlxCrud.Column;
import com.mysql.cj.xdevapi.SelectStatement;

import stubatis.dto.StudentReq;
import stubatis.dto.StudentRes;

@Mapper
public interface Studentdao  {
	
	
	@InsertProvider(type = student.class,method = "insertstudent")
	@Options(useGeneratedKeys = true, keyColumn = "id",keyProperty = "id")
	public int insertStudent(StudentReq req); 
	
	@InsertProvider(type = student.class,method = "insertintodetail")
	public int insertcoursedetail(String id , String course); 

	@UpdateProvider(type = student.class,method = "updateStudent")
	public int updateStudent(StudentReq req); 
	
	@DeleteProvider(type = student.class,method = "deletecoursedetail")
	public int deletecoursedetail(StudentReq req);
	
	@DeleteProvider(type = student.class,method = "deleteStudent")
	public int deleteStudent(StudentReq req);
	
	
	@SelectProvider(type = student.class,method = "showstudent")
	@Results( value = {
			@Result(column = "id" ,property = "id",jdbcType = JdbcType.INTEGER),
			@Result(column = "name" ,property = "name",jdbcType = JdbcType.VARCHAR),
			@Result(column = "education" ,property = "education",jdbcType = JdbcType.VARCHAR),
			@Result(column = "dob" ,property = "dob",jdbcType = JdbcType.VARCHAR),
			@Result(column = "phone" ,property = "phone",jdbcType = JdbcType.VARCHAR),
			@Result(column = "gender" ,property = "gender",jdbcType = JdbcType.VARCHAR),
			@Result(column = "course" , property = "course" ,jdbcType = JdbcType.VARCHAR)
		})
	List<StudentRes> showStudent();
	
	@SelectProvider(type = student.class,method = "studentmaxid")
	@Results(value = {
			@Result(column = "maxid",property = "id")
	})
	int studentMaxID();
	
	
	@SelectProvider(type = student.class,method = "specificstudent")
	@Results( value = {
			@Result(column = "id" ,property = "id",jdbcType = JdbcType.INTEGER),
			@Result(column = "name" ,property = "name",jdbcType = JdbcType.VARCHAR),
			@Result(column = "education" ,property = "education",jdbcType = JdbcType.VARCHAR),
			@Result(column = "dob" ,property = "dob",jdbcType = JdbcType.VARCHAR),
			@Result(column = "phone" ,property = "phone",jdbcType = JdbcType.VARCHAR),
			@Result(column = "gender" ,property = "gender",jdbcType = JdbcType.VARCHAR),
			@Result(column = "course" , property = "course" ,jdbcType = JdbcType.VARCHAR)
		})
	List<StudentRes> specificStudent(StudentReq req);
	
	@SelectProvider(type = student.class,method = "getoneStudent")
	@Results( value = {
			@Result(column = "id" ,property = "id",jdbcType = JdbcType.INTEGER),
			@Result(column = "name" ,property = "name",jdbcType = JdbcType.VARCHAR),
			@Result(column = "education" ,property = "education",jdbcType = JdbcType.VARCHAR),
			@Result(column = "dob" ,property = "dob",jdbcType = JdbcType.VARCHAR),
			@Result(column = "phone" ,property = "phone",jdbcType = JdbcType.VARCHAR),
			@Result(column = "gender" ,property = "gender",jdbcType = JdbcType.VARCHAR),
			@Result(column = "course" , property = "course" ,jdbcType = JdbcType.VARCHAR)
		})
	List<StudentRes> getonestudent(StudentReq req);
	
	
	
	public class student{
		
		
		public String showstudent() {
			return new SQL() {{
				SELECT("group_concat(course.name)as course,student.*");
				FROM("student");
				JOIN("coursedetail on student.id = coursedetail.studentid");
				JOIN("course on course.id = coursedetail.courseid");
				GROUP_BY("student.id");
				}}.toString();
		}
		
		public String getoneStudent(StudentReq req) {
			return new SQL() {{
				SELECT("*");
				FROM("student");
				WHERE("id = #{id}");
			}
			}.toString();
			
		}
		
		
		
		
		public String specificstudent(StudentReq req) {
			return new SQL() {{
				SELECT("group_concat(c.name)as course,s.*");
				FROM("student s");
				JOIN("coursedetail cd on s.id = cd.studentid");
				JOIN("course c on c.id = cd.courseid");
				if(req.getId()!=null || req.getName()!=null || req.getCourse()!=null) {	
					WHERE("s.id = #{id} or s.name = #{id} or c.name = #{course}");
					//GROUP_BY("s.name");
				}
				if(!req.getId().equals("") && !req.getName().equals("") && !req.getCourse().equals("")) {
					WHERE("s.id = #{id} and c.name = #{course} and s.name = #{id} ");
					//GROUP_BY("s.name");
				}
				GROUP_BY("s.name");
				}}.toString();
		}
		
		public String insertstudent(StudentReq req) {
			return new SQL() {{
				
				INSERT_INTO("student");
				VALUES("id","#{id}");
				VALUES("name","#{name}");
				VALUES("dob","#{dob}" );
				VALUES("phone","#{phone}" );
				VALUES("gender","#{gender}" );
				VALUES("education","#{education}");
						
			}}.toString();
		}
		
		public String insertintodetail(String id , String course) {
			
				return new SQL() {{
					INSERT_INTO("coursedetail");
					INTO_COLUMNS("studentid","courseid");
					INTO_VALUES(id,course);
					}}.toString();
	}
		
		public String studentmaxid() {
			return new SQL() {{
				SELECT("max(id) as maxid");
				FROM("student");
			}}.toString();
		}
		
		
	
	public String updateStudent(StudentReq req) {
		return new SQL() {{
			UPDATE("student");
			SET("name = #{name} , dob = #{dob} , gender = #{gender} , phone = #{phone} , education = #{education}");
			WHERE("id = #{id}");
		}}.toString();
	}
	
	public String deletecoursedetail(StudentReq req) {
		return new SQL() {{
			DELETE_FROM("coursedetail");
			WHERE("studentid = #{id}");
			
		}}.toString();
		
	}
	
	
	public String deleteStudent(StudentReq req) {
		return new SQL() {{
			DELETE_FROM("student");
			WHERE("id = #{id}");
			
		}}.toString();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
