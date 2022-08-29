package stubatis.config;



import java.io.IOException;
import java.io.Reader;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import stubatis.bean.User;
import stubatis.dao.LoginLogoutdao;
import stubatis.dao.Userdao;
import stubatis.dto.UserRes;



@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"stubatis"})
@MapperScan(basePackages = {"stubatis"})
public class StuDispatcher implements WebMvcConfigurer {

	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		
		
		return viewResolver;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/reach/**").addResourceLocations("/extra/");
	}
	
	
	public DataSource source() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:mysql://localhost:3306/studentregister");
		ds.setUsername("root");
		ds.setPassword("Mybirthday2312");
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		
		return ds;
	}
	
	@Bean
	public JdbcTemplate template () {
		return new JdbcTemplate(source());
	}
	
	@Bean
	public SqlSessionFactory sessionfactorybean () throws Exception {
		SqlSessionFactoryBean session = new SqlSessionFactoryBean();
		session.setDataSource(source());
	
		return session.getObject();	
	}
	
	@Bean(name="session")
	public SqlSession  sqlsession(){
		TransactionFactory tf = new JdbcTransactionFactory();		
		Environment ev = new Environment("env",tf,source());
		org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration(ev);
		conf.addMappers("stubatis.dao");
		SqlSessionFactory sf = new DefaultSqlSessionFactory(conf);		
		SqlSession ss = sf.openSession();
		
		return ss;
	}
	

	@Bean(name="course")
	public SqlSession coursesql() throws IOException {
		Reader read = Resources.getResourceAsReader("configuration.xml");
	
		SqlSessionFactory fact = new SqlSessionFactoryBuilder().build(read);
		SqlSession session = fact.openSession();
		return session;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Bean(name="userdao")
//	public Userdao  logindao() {
//		TransactionFactory tf = new JdbcTransactionFactory();
//		Environment ev = new Environment("env", tf, source());
//		org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration(ev);
//		SqlSessionFactory sf = new DefaultSqlSessionFactory(conf);
//		SqlSession sqlsession = sf.openSession();
//	    sqlsession.getConfiguration().addMapper(Userdao.class);
//	    Userdao dao =  sqlsession.getMapper(Userdao.class);
//	    return dao;			
//	}
	
	
	
	
	
	
}
