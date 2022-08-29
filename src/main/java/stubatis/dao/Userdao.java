package stubatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import stubatis.dto.UserReq;
import stubatis.dto.UserRes;


public interface Userdao {

	
	@Insert("insert into user (name,email,password,role) values(#{name},#{email},#{password},#{role});")
	void insertUser(UserReq req);
	
	
	@Update("update user set name=#{name} , email = #{email} , password = #{password}, role = #{role} where id = #{id};")
	void updateUser(UserReq req);
	
	@Delete("delete from user where id= #{id};")
	void deleteUser(UserReq req);
	
	@Select("select * from user;")
	@Results(value = {
			@Result(property = "id" , column = "id"),
			@Result(property = "name" , column = "name"),
			@Result(property = "email" , column = "email"),
			@Result(property = "role" , column = "role")
			})
	List<UserRes>showUser();
	
	@Select("<script>"
			+ "select * from user"
			+ "<if test =' id != null or name != null '>where id = #{id} or name = #{name}</if>"
			+ "</script>")
	@Results(value = {
			@Result(property = "id" , column = "id"),
			@Result(property = "name" , column = "name"),
			@Result(property = "email" , column = "email"),
			@Result(property = "role" , column = "role")
			})
	List<UserRes> specificUser(UserReq req);
	
	
	
}
