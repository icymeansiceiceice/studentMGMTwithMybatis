package stubatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.Alias;

import stubatis.bean.User;
import stubatis.dto.UserReq;
import stubatis.dto.UserRes;



@Mapper
public interface LoginLogoutdao  {

	@Select("select * from user where id = #{id} and password = #{password};")
	@Results(value = {
	@Result(property = "id" , column = "id"),
	@Result(property = "name" , column = "name"),
	@Result(property = "email" , column = "email"),
	@Result(property = "role" , column = "role")
	}) 
	UserRes login(UserReq req);
	
	
}
