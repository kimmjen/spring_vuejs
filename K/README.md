# kuzuro 

## mysql
```sql
create database study_board;
use study_board;
// 권한
grant all privileges on *.* to 'test02'@'localhost';
grant all privileges on study_board.* to 'test02'@'localhost';
flush privileges;

// 위에서 생성한 유저 권한 확인
commit;

select * from mysql.user;
```

## table생성
```
create table tbl_board (
	bno int not null auto_increment,
    title varchar(50) not null,
    content text not null,
    writer varchar(30) not null,
    regDate timestamp default now(),
    viewCnt int default 0,
    primary key(bno)
);

commit;
// 확인
select * from tbl_board;
```

## domain
```java
package com.kimmjen.domain;

import java.util.Date;

public class BoardVO {
	
	/*
	 * create table tbl_board ( bno int not null auto_increment, title varchar(50)
	 * not null, content text not null, writer varchar(30) not null, regDate
	 * timestamp default now(), viewCnt int default 0, primary key(bno) );
	 */
	private int bno;
	private String title;
	private String content;
	private String writer;
	private Date regDate;
	private int viewCnt;
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
}

```

## board/pom.xml
```java
<!-- mysql db -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>8.0.21</scope>
</dependency>
<!-- org.mybatis/mybatis -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.1</version>
</dependency>
<!-- mybatis-spring -->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.3.0</version>
</dependency>
<!-- spring-jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>${org.springframework-version}</version>
</dependency>
```

## root-context.xml
beans, context, jdbc, mybatis-spring선택

```
<!-- 디비 접근 -->
<bean class="org.springframework.jdbc.datasource.DriverManagerDataSource" id="dataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />
    <property name="url" value="jdbc:mysql://127.0.0.1:3306/stduy_board" />
    <property name="username" value="test02" />
    <property name="password" value="1111" />
</bean>

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="configLocation" value="classpath:/mybatis-config.xml" />
    <property name="mapperLocations" value="classpath:mappers/**/*Mapper.xml" />
</bean>

<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate" destroy-method="clearCache">
    <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

## mybatis-config.xml <- src/main/resources/mybatis-config.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration 
	PUBLIC "-//mybatis.org//DTD Config 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

</configuration>
```

## mappers 폴더생서 <- src/main/resources/mappers

## boardMapper.xml <- src/main/resources/mappers/boardMapper.xml
> mapper태그 안 namespace는 groupId.artifactId.mappers.study_board(이거는 DB이름)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC 
	"-//mybatis.org//DTD Mapper 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kimmjen.mappers.DB이름">

</mapper>
```

## 생성한 테이블에 임시 데이터 삽입
```sql
insert into tbl_board(title, content, writer)
	values('테스트 제목1', '테스트 내용', '작성자');
insert into tbl_board(title, content, writer)
	values('테스트 제목2', '테스트 내용', '작성자');
insert into tbl_board(title, content, writer)
	values('테스트 제목3', '테스트 내용', '작성자');
insert into tbl_board(title, content, writer)
	values('테스트 제목4', '테스트 내용', '작성자');
insert into tbl_board(title, content, writer)
	values('테스트 제목5', '테스트 내용', '작성자');
    
commit;

select * from tbl_board;
```

## list.jsp <- views/board/list.jsp
```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 목록</title>
</head>
<body>
	<table>
		<thead>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성일</th>
				<th>작성자</th>
				<th>조회수</th>
			</tr>
		</thead>
	</table>
</body>
</html>
```

## home.jsp에 게시물 목록 페이지 링크 만들기
```jsp
<p>
	<a hreef="./board/list">게시물목록</a>
</p>
```

이 과정 까지 진행하면 404발생

그이유는 board/list 컨트롤러가 존재하지 않기 때문에

## BoardController.class 만들기 <= com.지정.controller
```java
package com.kimmjen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList() {
		
	}
}
```

## server 웹 모듈에 들어가서 path를 수정
path: /지정 -> / 변환

## boardMapper.xml: 필요한 컬럼. 매퍼에 쿼리추가
```xml
	<!-- 게시물 목록 -->
	<select id="list" resultType="com.kimmjen.domain.BoardVO" >
		select
			bno, title, content, writer, regDate, viewCnt
		from tbl_board;
	</select>
```

## com.kimmjen + .dao, .service

## BoardDAO 인터페이스 생성
```java
package com.kimmjen.dao;

import java.util.List;

import com.kimmjen.domain.BoardVO;

public interface BoardDAO {
	
	public List<BoardVO> list() throws Exception;

}
```

## BoardDAOImpl.클래스 생성
```java
package com.kimmjen.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kimmjen.domain.BoardVO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	@Inject
	private SqlSession sql;
	
	private static String namesapce = "com.kimmjen.mappers.DB이름";

	// 게시물 목록
	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		
		return sql.selectList(namesapce + ".list");
	}
}
```

게시물, 즉 tbl_board 1행의 데이터의 형태는 BoardVO와 같다.
게시물목록은 tbl_board가 1행 이상 존재하는 것이므로, BoardVO를 리스트(List) 형태로 만들면 게시물 목록을 받아올 수 있다.

**여기서 boardMapper와 BoardDAOImpl**
의 namespace가 동일하여야 한다.(mappers.DB이름)

## service폴더에 BoardService(interface)와 BoardServiceImpl 생성
BoardService.interface
```java
package com.kimmjen.service;

import java.util.List;

import com.kimmjen.domain.BoardVO;

public interface BoardService {
	
	public List<BoardVO> list() throws Exception;
}

```

BoardServiceImpl.class
```java
package com.kimmjen.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.kimmjen.dao.BoardDAO;
import com.kimmjen.domain.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;

	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return dao.list();
	}
}
```

## BoardController.class
```java
package com.kimmjen.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kimmjen.domain.BoardVO;
import com.kimmjen.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	BoardService service;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {
		
		List<BoardVO> list = null;
		list = service.list();
		
		model.addAttribute("list", list);
		
	}	
}
```
`Model`은 컨트롤러(Controller)와 뷰(view)를 연결해주는 역할

## list.jsp
```jsp
</thead>
<tbody>
// jstl 반복문
    <c:forEach items="${list }" var="list">
        <tr>
            <td>${list.bno}</td>
            <td>${list.title}</td>
            <td>${list.regDate}</td>
            <td>${list.writer}</td>
            <td>${list.viewCnt}</td>
        </tr>
    </c:forEach>
</tbody>
```

실행하면 에러발생, 그 이유는 스프링이 BoardDAO와 BoardService를 못참음.

오류
```
타입 예외 보고

메시지 서블릿 [appServlet]을(를) 위한 Servlet.init() 호출이 예외를 발생시켰습니다.

설명 서버가, 해당 요청을 충족시키지 못하게 하는 예기치 않은 조건을 맞닥뜨렸습니다.

예외

javax.servlet.ServletException: 서블릿 [appServlet]을(를) 위한 Servlet.init() 호출이 예외를 발생시켰습니다.
	org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)
	org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:687)
	org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
	org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1726)
	org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	java.lang.Thread.run(Unknown Source)
근본 원인 (root cause)

org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'boardController': Unsatisfied dependency expressed through field 'service'; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.kimmjen.service.BoardService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@javax.inject.Inject()}
	org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:588)
	org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88)
	org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483)
	org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197)
	org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761)
	org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:866)
	org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:542)
	org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(FrameworkServlet.java:668)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:634)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:682)
	org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(FrameworkServlet.java:553)
	org.springframework.web.servlet.FrameworkServlet.initServletBean(FrameworkServlet.java:494)
	org.springframework.web.servlet.HttpServletBean.init(HttpServletBean.java:138)
	javax.servlet.GenericServlet.init(GenericServlet.java:158)
	org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)
	org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:687)
	org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
	org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1726)
	org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	java.lang.Thread.run(Unknown Source)
근본 원인 (root cause)

org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.kimmjen.service.BoardService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@javax.inject.Inject()}
	org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:1486)
	org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1104)
	org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066)
	org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:585)
	org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88)
	org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553)
	org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483)
	org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197)
	org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761)
	org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:866)
	org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:542)
	org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(FrameworkServlet.java:668)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:634)
	org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(FrameworkServlet.java:682)
	org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(FrameworkServlet.java:553)
	org.springframework.web.servlet.FrameworkServlet.initServletBean(FrameworkServlet.java:494)
	org.springframework.web.servlet.HttpServletBean.init(HttpServletBean.java:138)
	javax.servlet.GenericServlet.init(GenericServlet.java:158)
	org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)
	org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:687)
	org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
	org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1726)
	org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	java.lang.Thread.run(Unknown Source)
```

## root-context.xml에 추가
```xml
<context:component-scan base-package="com.kimmjen.domain" />
<context:component-scan base-package="com.kimmjen.dao" />
<context:component-scan base-package="com.kimmjen.service" />
```

위 코드를 입력하면 스프링이 패키지를 찾아서 사용함.

메인 페이지에서 목록 페이지 누르면 

## 오류발생
```
HTTP 상태 500 – 내부 서버 오류
타입 예외 보고

메시지 Request processing failed; nested exception is org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException:

설명 서버가, 해당 요청을 충족시키지 못하게 하는 예기치 않은 조건을 맞닥뜨렸습니다.

예외

org.springframework.web.util.NestedServletException: Request processing failed; nested exception is org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
### The error may exist in file [C:\ION_workspace\ION_KOSA\SPRING\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\K\WEB-INF\classes\mappers\boardMapper.xml]
### The error may involve com.kimmjen.mappers.board.list
### The error occurred while executing a query
### Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:982)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
근본 원인 (root cause)

org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
### The error may exist in file [C:\ION_workspace\ION_KOSA\SPRING\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\K\WEB-INF\classes\mappers\boardMapper.xml]
### The error may involve com.kimmjen.mappers.board.list
### The error occurred while executing a query
### Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	org.mybatis.spring.MyBatisExceptionTranslator.translateExceptionIfPossible(MyBatisExceptionTranslator.java:79)
	org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:447)
	com.sun.proxy.$Proxy10.selectList(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
근본 원인 (root cause)

org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
### The error may exist in file [C:\ION_workspace\ION_KOSA\SPRING\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\K\WEB-INF\classes\mappers\boardMapper.xml]
### The error may involve com.kimmjen.mappers.board.list
### The error occurred while executing a query
### Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	org.apache.ibatis.exceptions.ExceptionFactory.wrapException(ExceptionFactory.java:30)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:150)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:136)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:434)
	com.sun.proxy.$Proxy10.selectList(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
근본 원인 (root cause)

org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:80)
	org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:84)
	org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:70)
	org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336)
	org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:84)
	org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62)
	org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
	org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:136)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:434)
	com.sun.proxy.$Proxy10.selectList(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
근본 원인 (root cause)

java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:129)
	com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:97)
	com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:89)
	com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:63)
	com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:73)
	com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:76)
	com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:836)
	com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:456)
	com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:246)
	com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:197)
	java.sql.DriverManager.getConnection(Unknown Source)
	java.sql.DriverManager.getConnection(Unknown Source)
	org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriverManager(DriverManagerDataSource.java:153)
	org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriver(DriverManagerDataSource.java:144)
	org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnectionFromDriver(AbstractDriverBasedDataSource.java:196)
	org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnection(AbstractDriverBasedDataSource.java:159)
	org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:111)
	org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:77)
	org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:84)
	org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:70)
	org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336)
	org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:84)
	org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62)
	org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
	org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:136)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:434)
	com.sun.proxy.$Proxy10.selectList(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
근본 원인 (root cause)

com.mysql.cj.exceptions.InvalidConnectionAttributeException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	sun.reflect.NativeConstructorAccessorImpl.newInstance(Unknown Source)
	sun.reflect.DelegatingConstructorAccessorImpl.newInstance(Unknown Source)
	java.lang.reflect.Constructor.newInstance(Unknown Source)
	com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:61)
	com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:85)
	com.mysql.cj.util.TimeUtil.getCanonicalTimezone(TimeUtil.java:132)
	com.mysql.cj.protocol.a.NativeProtocol.configureTimezone(NativeProtocol.java:2120)
	com.mysql.cj.protocol.a.NativeProtocol.initServerSession(NativeProtocol.java:2143)
	com.mysql.cj.jdbc.ConnectionImpl.initializePropsFromServer(ConnectionImpl.java:1310)
	com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:967)
	com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:826)
	com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:456)
	com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:246)
	com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:197)
	java.sql.DriverManager.getConnection(Unknown Source)
	java.sql.DriverManager.getConnection(Unknown Source)
	org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriverManager(DriverManagerDataSource.java:153)
	org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriver(DriverManagerDataSource.java:144)
	org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnectionFromDriver(AbstractDriverBasedDataSource.java:196)
	org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnection(AbstractDriverBasedDataSource.java:159)
	org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:111)
	org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:77)
	org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:84)
	org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:70)
	org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336)
	org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:84)
	org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62)
	org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
	org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
	org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)
	org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:136)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:434)
	com.sun.proxy.$Proxy10.selectList(Unknown Source)
	org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	java.lang.reflect.Method.invoke(Unknown Source)
	org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
비고 근본 원인(root cause)의 풀 스택 트레이스를, 서버 로그들에서 확인할 수 있습니다.

Apache Tomcat/9.0.53
```
리스트를 불러올 수 없다는 이유.<br>

## mysql 오류
```
로 []의 컨텍스트 내의 서블릿 [appServlet]을(를) 위한 Servlet.service() 호출이, 근본 원인(root cause)과 함께, 예외 [Request processing failed; nested exception is org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.exceptions.PersistenceException: 
### Error querying database.  Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
### The error may exist in file [C:\ION_workspace\ION_KOSA\SPRING\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\K\WEB-INF\classes\mappers\boardMapper.xml]
### The error may involve com.kimmjen.mappers.board.list
### The error occurred while executing a query
### Cause: org.springframework.jdbc.CannotGetJdbcConnectionException: Could not get JDBC Connection; nested exception is java.sql.SQLException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.]을(를) 발생시켰습니다.
com.mysql.cj.exceptions.InvalidConnectionAttributeException: The server time zone value '���ѹα� ǥ�ؽ�' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the 'serverTimezone' configuration property) to use a more specifc time zone value if you want to utilize time zone support.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(Unknown Source)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(Unknown Source)
	at java.lang.reflect.Constructor.newInstance(Unknown Source)
	at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:61)
	at com.mysql.cj.exceptions.ExceptionFactory.createException(ExceptionFactory.java:85)
	at com.mysql.cj.util.TimeUtil.getCanonicalTimezone(TimeUtil.java:132)
	at com.mysql.cj.protocol.a.NativeProtocol.configureTimezone(NativeProtocol.java:2120)
	at com.mysql.cj.protocol.a.NativeProtocol.initServerSession(NativeProtocol.java:2143)
	at com.mysql.cj.jdbc.ConnectionImpl.initializePropsFromServer(ConnectionImpl.java:1310)
	at com.mysql.cj.jdbc.ConnectionImpl.connectOneTryOnly(ConnectionImpl.java:967)
	at com.mysql.cj.jdbc.ConnectionImpl.createNewIO(ConnectionImpl.java:826)
	at com.mysql.cj.jdbc.ConnectionImpl.<init>(ConnectionImpl.java:456)
	at com.mysql.cj.jdbc.ConnectionImpl.getInstance(ConnectionImpl.java:246)
	at com.mysql.cj.jdbc.NonRegisteringDriver.connect(NonRegisteringDriver.java:197)
	at java.sql.DriverManager.getConnection(Unknown Source)
	at java.sql.DriverManager.getConnection(Unknown Source)
	at org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriverManager(DriverManagerDataSource.java:153)
	at org.springframework.jdbc.datasource.DriverManagerDataSource.getConnectionFromDriver(DriverManagerDataSource.java:144)
	at org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnectionFromDriver(AbstractDriverBasedDataSource.java:196)
	at org.springframework.jdbc.datasource.AbstractDriverBasedDataSource.getConnection(AbstractDriverBasedDataSource.java:159)
	at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:111)
	at org.springframework.jdbc.datasource.DataSourceUtils.getConnection(DataSourceUtils.java:77)
	at org.mybatis.spring.transaction.SpringManagedTransaction.openConnection(SpringManagedTransaction.java:84)
	at org.mybatis.spring.transaction.SpringManagedTransaction.getConnection(SpringManagedTransaction.java:70)
	at org.apache.ibatis.executor.BaseExecutor.getConnection(BaseExecutor.java:336)
	at org.apache.ibatis.executor.SimpleExecutor.prepareStatement(SimpleExecutor.java:84)
	at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:62)
	at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
	at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
	at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:141)
	at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:136)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at org.mybatis.spring.SqlSessionTemplate$SqlSessionInterceptor.invoke(SqlSessionTemplate.java:434)
	at com.sun.proxy.$Proxy10.selectList(Unknown Source)
	at org.mybatis.spring.SqlSessionTemplate.selectList(SqlSessionTemplate.java:223)
	at com.kimmjen.dao.BoardDAOImpl.list(BoardDAOImpl.java:25)
	at com.kimmjen.service.BoardServiceImpl.list(BoardServiceImpl.java:21)
	at com.kimmjen.controller.BoardController.getList(BoardController.java:26)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
	at java.lang.reflect.Method.invoke(Unknown Source)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:963)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:897)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:687)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1726)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
	at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Unknown Source)
```
## root-context.xml
```
`DB이름`?serverTimezone=Asia/Seoul

<property name="url" value="jdbc:mysql://127.0.0.1:3306/study_board?serverTimezone=Asia/Seoul" />
```

## 게시물 작성
1. write.jsp 작성
```jsp
바디 안에
<form>
    <label>제목</label>
    <input tpye="text" name="title" /><br/>
    
    <label>작성자</label>
    <input tpye="text" name="writer" /><br/>
    
    <label>내용</label>
    <textarea cols="50" rows="5" name="content"></textarea><br/>
    
    <button tpye="submit">등록</button>
</form>
```

2. BoardController.class
```java
// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWrite() throws Exception {
		
	}
```

3. home.jsp 링크추가
```jsp
<p>
    ...
	<a href="./board/write">게시물 작성</a>
</p>
```

4. DB에 테스트 데이터 입력
```mysql
insert into tbl_board(title, content, writer)
	values('test title', 'test content', 'test writer');
    
commit;
select * from tbl_board;
```

> list를 불러올때는 resultType 사용했고, write할때는 parameterTpye을 사용하였다. 그 이유는 resultType은 데이터 쿼리를 실행한 뒤 결과가 있을 경우에 사용되고. parameterType은 데이터를 입력(삽입)할 때 사용된다.

5. boardMapper.xml에 insert문 입력
```xml
<!-- 게시물 작성 -->
<insert id="write" parameterType="com.kimmjen.domain.BoardVO">
    insert into tbl_board(title, content, writer)
    values(#{title}, #{writer}, #{content});
</insert>
```

6. BoardDAO.interface에 게시물 작성용 메서드 추가

boardDAO.java

```java
// 게시물 등록
	public void write(BoardVO vo) throws Exception;
```

6-1. BoardDAOImpl.class에
boardDAOImpl.java
```java
// 게시물 작성
@Override
public void write(BoardVO vo) throws Exception {
    // TODO Auto-generated method stub
    
    sql.insert(namesapce + ".write", vo);
    
}
```

7. 6.과 같이 BoardService와 BoardServiceImpl도 추가

boardService.java

```java
// 게시물 작성
package com.kimmjen.service;

import java.util.List;

import com.kimmjen.domain.BoardVO;

public interface BoardService {
	
	// 게시물 리스트
	public List<BoardVO> list() throws Exception;
	
	// 게시물 작성
	public void write(BoardVO vo) throws Exception;
	
}

```

boardServiceImpl.java
```java
package com.kimmjen.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.kimmjen.dao.BoardDAO;
import com.kimmjen.domain.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO dao;

	// 게시물 리스트
	@Override
	public List<BoardVO> list() throws Exception {
		// TODO Auto-generated method stub
		return dao.list();
	}
	
	// 게시물 작성
	@Override
	public void write(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.write(vo);
		
	}

}

```

8. BoardController에도 post 메서드 추가

```java
// 게시물 작성
@RequestMapping(value = "/write", method = RequestMethod.POST)
public String postWrite(BoardVO vo) throws Exception {
    
    service.write(vo);
    
    return "redirect:/board/list";
}
```

## 게시물 조회

1. write.jsp 를 복사하여 view.jsp로 붙여넣기

view.jsp

```
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 조회</title>
</head>
<body>
	<form method="post">
		<label>제목</label>
		<input tpye="text" name="title" /><br/>
		
		<label>작성자</label>
		<input tpye="text" name="writer" /><br/>
		
		<label>내용</label>
		<textarea cols="50" rows="5" name="content"></textarea><br/>
		
	</form>
</body>
</html>
```

2. boardcontroller에 조회용 겟메서드 작성

boardcontroller.java

```java
// 게시물 조회
@RequestMapping(value = "/view", method = RequestMethod.GET)
public void getView() throws Exception {

}
```

게시물을 구분하는 방법은 여러가지가 있지만, 보편적으로 게시물에 고유번호(또는 문자)를 이용해서 구분.

## list.jsp 제목태그 수정

```jsp
<td>${list.title}</td>

<td>
    <a href="/board/view?bno=${list.bno}">${list.title}</a>
</td>
```

## DB에서 먼저 테스트
```sql
select
	bno, title, content, writer, regDate, viewCnt
from
	tbl_board
where
	bno = 1;
```

## boardMapper.xml 게시물조회 쿼리 작성
```xml
select
	bno, title, content, writer, regDate, viewCnt
from
	tbl_board
where
	bno = 1;
```

## 게시물조회관련 DAO, DAOImle, Service, ServiceImpl 사용
boardDAO.interface
```java
// 게시물 조회
	public BoardVO view(int bno) throws Exception;
```

boardDAOImpl.class
```java
// 게시물 조회
	@Override
	public BoardVO view(int bno) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectOne(namesapce + ".view", bno);
	}
```

boardService.interface
```java
// 게시물 조회
public BoardVO view(int bno) throws Exception;
```

boardServiceImple.class
```java
// 게시물 조회
@Override
public BoardVO view(int bno) throws Exception {
    // TODO Auto-generated method stub
    return dao.view(bno);
}
```

boardController.java
```java
// 게시물 조회
@RequestMapping(value = "/view", method = RequestMethod.GET)
public void getView() throws Exception {
    
    service.view(bno);
}

// 게시물 조회
@RequestMapping(value = "/view", method = RequestMethod.GET)
public void getView(@RequestParam("bno") int bno, Model model) throws Exception {
    
    BoardVO vo = service.view(vo);
    
    model.addAttribute("view", vo);

}
```
## view.jsp
```jsp
<form method="post">
    <label>제목</label>
    <input tpye="text" name="title" value="${view.title }"/><br/>
    
    <label>작성자</label>
    <input tpye="text" name="writer" value="${view.writer }"/><br/>
    
    <label>내용</label>
    <textarea cols="50" rows="5" name="content">${view.content }</textarea><br/>
    
    <div>
        <a href="/board/modify?bno=${view.bno }">게시물 수정</a>
    </div>
    
</form>

or

<label>제목</label>
${view.title}<br />

<label>작성자</label>
${view.writer}<br />

<label>내용</label><br />
${view.content}<br />
```

## 수정하기

modify.jsp

```java
<form method="post">
    <label>제목</label>
    <input tpye="text" name="title" /><br/>
    
    <label>작성자</label>
    <input tpye="text" name="writer" /><br/>
    
    <label>내용</label>
    <textarea cols="50" rows="5" name="content"></textarea><br/>
    
    <button tpye="submit">완료</button>
</form>
```

boardController.java<br>
수정용 겟 메서드 추가

```java
// 게시물 수정
@RequestMapping(value = "/modify", method = RequestMethod.GET)
public void getModify() throws Exception {
    
}
```

view.jsp
```jsp
<div>
    <a href="/board/modify?bno=${view.bno }">게시물 수정</a>
</div>
```

게시물 수정을 눌렀으나 해당 내용이 존재하기 않기 때문에,
게시물 조회에서 사용된 메서드 이용한다.

boardController.java

```java
// 게시물 수정
@RequestMapping(value = "/modify", method = RequestMethod.GET)
public void getModify(@RequestParam("bno") int bno, Model model) throws Exception {
    
    BoardVO vo = service.view(bno);
    
    model.addAttribute("view", vo);
}
```

modify.jsp
```jsp
<form method="post">
    <label>제목</label>
    <input tpye="text" name="title" value="${view.title }" /><br/>
    
    <label>작성자</label>
    <input tpye="text" name="writer" value="${view.writer }" /><br/>
    
    <label>내용</label>
    <textarea cols="50" rows="5" name="content">${view.content}</textarea><br/>
    
    <button tpye="submit">완료</button>
</form>
```

수정 sql
```sql
update tbl_board
	set
		title = "수정된 테스트 제목1",
        content = "수정된 테스트 내용",
        writer = "수정된 작성자"
	where bno = 1;
    
select * from tbl_board;
```

수정용 쿼리를 boardMapper.xml에 추가
```xml
<!-- 게시물 수정 -->
<update id="modify" parameterType="com.kimmjen.domain.BoardVO">
    update tbl_board
        set
            title = #{title},
            content = #{content},
            writer = #{writer}
        where bno = #{bno};
</update>
```

dao, service, impl

boardDAO.interface
```java
// 게시물 수정
	public void modify(BoardVO vo) throws Exception;
```

boardDAOImple.class
```java
// 게시물 수정
@Override
public void modify(BoardVO vo) throws Exception {
    // TODO Auto-generated method stub
    sql.update(namesapce + ".modify", vo);
    
}
```

boardService.interface
```java
// 게시물 수정
public void modify(BoardVO vo) throws Exception;
```

boardSerivceImpl.class
```java
// 게시물 수정
@Override
public void modify(BoardVO vo) throws Exception {
    // TODO Auto-generated method stub
    dao.modify(vo);
}
```

boardcontroller.class
```java
// 게시물 수정
@RequestMapping(value = "/modify", method = RequestMethod.POST)
public String postModify(BoardVO vo) throws Exception {
    
    service.modify(vo);
    
    return "redirect:/board/view?bno=" + vo.getBno();
}

```

## 메뉴 include 

views에 include라는 폴더생성 후 nav.jsp 파일생성<br>
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<ul>
	<li>
		<a href="/board/list">글 목록</a>
	</li>
	
		<li>
		<a href="/board/write">글 작성</a>
	</li>
</ul>
```

list, write, view, modify의 body 안에
```jsp
<div id="nav">
	<%@ include file="../include/nav.jsp" %>
</div>
```

날짜 포맷 변경하는 방식 누락<br>

list.jsp
```jsp
상단에
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%-- <td>${list.regDate}</td> --%>

변경
<td>
    <fmt:formatDate value="${list.regDate }" patten="yyyy-MM-dd" />
</td>
```

## 삭제 구현
view.jsp
```jsp
<a href="/board/delete?bno=${view.bno}">게시물 삭제</a>
```

DB에 확인하기<br>
```sql
delete 
	from tbl_board
where bno = 2;

select * from tbl_board;
```

boradMapper.xml 에 추가
```xml
<!-- 게시물 삭제 -->
<delete id="delete" parameterType="int">
    delete
        from tbl_board
    where bno = #{bno};
</delete>
```

dao, service + impl 에도 삭제 메서드 코드 추가

boardDAO
```java
// 게시물 삭제
public void delete(int bno) throws Exception;
```

boardDAOImpl
```java
// 게시물 삭제
@Override
public void delete(int bno) throws Exception {
    // TODO Auto-generated method stub
    sql.delete(namesapce + ".delete", bno);
}
```

boardService
```java
// 게시물 삭제
public void delete(int bno) throws Exception;
```

boardServiceImpl
```java
// 게시물 삭제
@Override
public void delete(int bno) throws Exception {
    // TODO Auto-generated method stub
    dao.delete(bno);
    
}
```

boardController.class
```java
// 게시물 삭제
@RequestMapping(value = "/delete", method = RequestMethod.GET)
public String getDelete(@RequestParam("bno") int bno) throws Exception {
    
    service.delete(bno);
    
    return "redirect:/board/list";
}
```

## 페이징 구현 1-1
현재 임시데이터이며, 페이징 구현 처리하기에는 데이터수가 너무 적기 때문에 데이터를 복사하여 DB에 데이터넣기
```sql
insert into tbl_board(title, content, writer)
select title, content, writer from tbl_board;
```

limit 10을 적용하여 아래에서부터 10개 데이터 보여주기
```sql
select
	bno, title, content, writer, regDate, viewCnt
from tbl_board
order by bno desc
	limit 10;
```

limit을 이용하여 
```txt
첫 페이지는 0번째부터 10개 이므로 (0, 10)
두번째 페이지는 11번째부터 10개 이므로 (10, 10)
세번째 페이지는 21번째부터 10개이므로 (20, 10)

select count(bno) from tbl_board;
- 게시물의 촛 갯수 구하는 쿼리
```
> 페이지 처리 조건
> 1. 한 페이지에 출려될 게시물 갯수(10)개
> 2. 페이징에 출력할 번호를 계산하기 위한 게시물의 총 갯수

boardMapper.xml
```xml
<!-- 게시물 총 갯수 -->
<select id="count" resultType="int">
	select count(bno) from tbl_board;
</select>
```

Mapper에 쿼리를 추가하였으면, DAO, Service, +Impl에도 코드 추가.

boardDAO.Interface
```java
// 게시물 총 갯수
public int count() throws Exception;
```
BoardDAOImpl.class
```java
// 게시물 총 갯수
@Override
public int count() throws Exception {
	// TODO Auto-generated method stub
	return sql.selectOne(namesapce + ".count");
}
```
boardService.Interface
```java
// 게시물 총 갯수
public int count() throws Exception;
```
boardServiceImpl.class
```java
// 게시물 총 갯수
@Override
public int count() throws Exception {
	// TODO Auto-generated method stub
	return dao.count();
}
```

boardController.class
```java
// 게시물 총 갯수(목록) + 페이징 추가 
@RequestMapping(value = "/listPage", method = RequestMethod.GET)
public void getListPage(Model model) throws Exception {
	
	List<BoardVO> list = null;
	list = service.list();
	model.addAttribute("lsit", list);
}
```

listPage는 게시물 목록꺼에서 가져와서 value 값은 listPage 메서드로 지정.

boardMapper.xml: 게시물 10개씩 출력하겠다는 쿼리추가
```xml
<!-- 게시물 목록과 페이징 -->
<select id="listPage" parameterType="hashMap" resultType="com.kimmjen.domain.BoardVO">
	select
		bno, title, content, writer, regDate, viewCnt
	from tbl_board
	order by bno desc
		limit #{displayPost}, ${postNum};
</select>
```

mapper를 추가하였기 때문에 DAO, Service, + Impl에도 코드 추가

boardDAO.interface
```java
// 게시물 목록과 페이징
public List<BoardVO> listPage(int displayPost, int postNum) throws Exception;
```
boardDAOImpl.class
```java
// 게시물 목록과 페이징
@Override
public List<BoardVO> listPage(int displayPost, int postNum) throws Exception {
	// TODO Auto-generated method stub
	HashMap<String, Integer> data = new HashMap<String, Integer>();
	
	data.put("displayPost", displayPost);
	data.put("postNum", postNum);
	
	return sql.selectList(namesapce + ".listPage", data);
}
```
boardService.interface
```java
// 게시물 목록과 페이징
public List<BoardVO> listPage(int displayPost, int postNum) throws Exception;
```
boardServiceImpl.class
```java
// 게시물 목록과 페이징
@Override
public List<BoardVO> listPage(int displayPost, int postNum) throws Exception {
	// TODO Auto-generated method stub
	return dao.listPage(displayPost, postNum);
}
```

boardController.class
```java
// 게시물 총 갯수(목록) + 페이징 추가 
@RequestMapping(value = "/listPage", method = RequestMethod.GET)
public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
	
	// 게시물 총 갯수
	int count = service.count();
	
	// 한 페이지에 출력할 게시물 갯수
	int postNum = 10;
	
	// 하단 페이징 번호 ([ 게시물 총 갯수 % 한 페이지에 출력할 갯수] 의 올림)
	int pageNum = (int)Math.ceil((double) count / postNum);
	
	// 출력할 게시물
	int displayPost = (num - 1) * postNum;
	
	List<BoardVO> list = null;
	list = service.listPage(displayPost, postNum);
	model.addAttribute("list", list);
	model.addAttribute("pageNum", pageNum);
}
```
매개변수로 num는 페이지 번호.
1. 게시물의 총 갯수를 구하고
2. 한 페이지당 출력할 게시물 갯수 10개
3. 하단에 표시할 페이징 번호의 갯수(소수점 올림)
4. 현재 페이지를 기준으로 10개의 데이터를 출력

listPage.jsp를 list.jsp를 복사 붙여넣기

listPage.jsp
```jsp
...
	</table>
	<div>
		<c:forEach begin="1" end="${pageNum }" var="num">
			<span>
				<a href="/board/listPage?num=${num }">${num }</a>
			</span>
		</c:forEach>
	</div>
</body>
</html>
```

데이터가 만개이상일 때 목록페이지를 보면, 만개데이터가 보인다.<br>
이것을 특정 갯수만큼의 페이징번호를 표시하거나, 중간을 생략하는 방법을 사용해야함.<br>
이 작업은 이후에

nav.jsp
```jsp
<li><a href="/board/listPage">글 목록(페이징)</a></li>
```

## 페이지 구현 1-2
- 첫 번째 방법: 나열

- 두 번째 방법: 페이지 번호 앞뒤로 특정 갯수 출력

우선은 첫 번째 방법

boardController.class
```java
// 출력할 게시물
int displayPost = (num - 1) * postNum;

//  첫번째방법------------------------------------------------------
// 한번에 표시할 페이징 번호의 갯수
int pageNum_cnt = 10;

// 표시되는 페이지 번호 중 마지막 번호
int endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);

// 표시되는 페이지 번호 중 첫번째 번호
int startPageNum = endPageNum - (pageNum_cnt - 1);

//  첫번째방법------------------------------------------------------
```

소수점 올림처리(ceil)<br>
마지막 페이지 번호를 구하는 공식

```
마지막 페이지 번호 = ((올림)(현재 페이지 번호 / 한번에 표시할 페이지 번호의 갯수)) * 한 번에 표시할 페이지 번호의 갯수
```

시작 페이지 구하는 공식
```
시작 페이지 = 마지막 페이지 번호 - 한번에 표시할 페이지 번호의 갯수 + 1
```

BoardController.class에 마지막 번호 재계산 코드 입력
```java
// 마지막 번호 재계산
int endPageNum_tmp = (int)(Math.ceil((double) count / (double) pageNum_cnt));

if (endPageNum > endPageNum_tmp) {
	endPageNum = endPageNum_tmp;
}
```

그리고 페이지 간격을 넘어가는 이전과 다음 링크 표시<br>
뷰에 출력하기 위해서

Boardcontroller.class 전체코드
```java
package com.kimmjen.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kimmjen.domain.BoardVO;
import com.kimmjen.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	BoardService service;
	
	// 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {
		
		List<BoardVO> list = null;
		list = service.list();
		
		model.addAttribute("list", list);
		
	}
	
	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWrite() throws Exception {
		
	}
	
	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String postWrite(BoardVO vo) throws Exception {
		
		service.write(vo);
		
		return "redirect:/board/list";
	}
	
	// 게시물 조회
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void getView(@RequestParam("bno") int bno, Model model) throws Exception {
		
		BoardVO vo = service.view(bno);
		
//		service.view(bno);
		model.addAttribute("view",vo);
	}
	
	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception {
		
		BoardVO vo = service.view(bno);
		
		model.addAttribute("view", vo);
	}
	
	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postModify(BoardVO vo) throws Exception {
		
		service.modify(vo);
		
		return "redirect:/board/view?bno=" + vo.getBno();
	}
	
	// 게시물 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(@RequestParam("bno") int bno) throws Exception {
		
		service.delete(bno);
		
		return "redirect:/board/list";
	}
	
	// 게시물 총 갯수(목록) + 페이징 추가 
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
		
		// 게시물 총 갯수
		int count = service.count();
		
		// 한 페이지에 출력할 게시물 갯수
		int postNum = 10;
		
		// 하단 페이징 번호 ([ 게시물 총 갯수 % 한 페이지에 출력할 갯수] 의 올림)
		int pageNum = (int)Math.ceil((double) count / postNum);
		
		// 출력할 게시물
		int displayPost = (num - 1) * postNum;
		
		//  첫번째방법------------------------------------------------------
		// 한번에 표시할 페이징 번호의 갯수
		int pageNum_cnt = 10;
		
		// 표시되는 페이지 번호 중 마지막 번호
		int endPageNum = (int)(Math.ceil((double)num / (double)pageNum_cnt) * pageNum_cnt);
		
		// 표시되는 페이지 번호 중 첫번째 번호
		int startPageNum = endPageNum - (pageNum_cnt - 1);
		
		// 마지막 번호 재계산
		int endPageNum_tmp = (int)(Math.ceil((double) count / (double) pageNum_cnt));
		
		if (endPageNum > endPageNum_tmp) {
			endPageNum = endPageNum_tmp;
		}
		
		boolean prev = startPageNum == 1 ? false:true;
		boolean next = endPageNum * pageNum_cnt >= count ? false:true;
		
		//  첫번째방법------------------------------------------------------
		
		
		List<BoardVO> list = null;
		list = service.listPage(displayPost, postNum);
		model.addAttribute("list", list);
		model.addAttribute("pageNum", pageNum);
		
		// 시작 및 끝 번호
		model.addAttribute("startPageNum", startPageNum);
		model.addAttribute("endPageNum", endPageNum);
		
		// 이전 및 다음
		model.addAttribute("prev", prev);
		model.addAttribute("next", next);
	}
	
}
```

listPage.jsp
```jsp
</table>
<div>
	<c:if test="${prev }">
		<span>[ <a href="/board/listPage?num=${startPageNum - 1 }">이전</a> ]</span>
	</c:if>
	<c:forEach begin="${startPageNum }" end="${endPageNum }" var="num">
		<span>
			<a href="/board/listPage?num=${num }">${num }</a>
		</span>
	</c:forEach>
	
	<c:if test="${next }">
		<span>[ <a href="/board/listPage?num=${endPageNum - 1 }">다음</a> ]</span>
	</c:if>
	
	<%-- <c:forEach begin="1" end="${pageNum }" var="num">
		<span>
			<a href="/board/listPage?num=${num }">${num }</a>
		</span>
	</c:forEach> --%>
</div>
```

boardController.class
```java
// 이전 및 다음
model.addAttribute("prev", prev);
model.addAttribute("next", next);

// 현재 페이지
model.addAttribute("select", num);
```

listPage.jsp
```jsp
<c:forEach begin="${startPageNum }" end="${endPageNum }" var="num">
	<span>
		<c:if test="${select != num }">
			<a href="/board/listPage?num=${num }">${num }</a>
		</c:if>
		
		<c:if test="${select == num }">
			<b>${num }</b>
		</c:if>
		<%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
	</span>
</c:forEach>
```

반복문 중간에 조건을 넣어서, select의 값이 num 과 다를 경우 링크를 그대로 출력하고 select의 값이 num 과 같은 경우 링크가 아닌 굵은 글자로 출력

## 페이지 구현 1-3

복잡하고 난잡하니 깔끔하게 만들기<br>

com.kimmjen.domain 패키지에 Page.class 만들기
```java
package com.kimmjen.domain;

public class Page {
	
	// 현재 페이지 번호
	private int num;
	
	// 게시물 총 갯수
	private int count;
	
	// 한 페이지에 출력할 게시물 갯수
	private int postNum = 10;
	
	// 하단 페이징 번호 ([ 게시물 총 갯수 % 한 페이지에 출력할 갯수 ]의 올림)
	private int pageNum;
	
	// 출력할 게시물
	private int displayPost;
	
	// 한번에 표시할 페이징 번호의 갯수
	private int pageNumCnt = 10;
	
	// 표시되는 페이지 번호 중 마지막 번호
	private int endPageNum;
	
	// 표시되는 페이지 번호 중 첫 번째 번호
	private int startPageNum;
	
	// 다음 /이전 표시 여부
	private boolean prev;
	private boolean next;
	

	public void setNum(int num) {
		this.num = num;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}
	public int getPostNum() {
		return postNum;
	}
	public int getPageNum() {
		return pageNum;
	}
	public int getDisplayPost() {
		return displayPost;
	}
	public int getPageNumCnt() {
		return pageNumCnt;
	}
	public int getEndPageNum() {
		return endPageNum;
	}
	public int getStartPageNum() {
		return startPageNum;
	}
	public boolean getPrev() {
		return prev;
	}
	public boolean getNext() {
		return next;
	}
}
```
getter와 setter 설정을 하는데, setter는 2개 현재페이지 num과 게시물 총 갯수 count만 

여기에 데이터 계산하는 메서드 추가, 게시물 총 갯수를 알고난 시점부터 계산이 가능하니까, setCount에서 메서드를 호출

```jsp
public void setCount(int count) {
	this.count = count;
	dataCalc();

	...

	public boolean getNext() {
		return next;
	}

	private void dataCalc() {
		
		// 마지막 번호
		endPageNum = (int)(Math.ceil((double) num / (double) pageNumCnt) * pageNumCnt);
		
		// 시작 번호
		startPageNum = endPageNum - (pageNumCnt - 1);
		
		// 마지막 번호 재계산
		int endPageNum_tmp = (int)(Math.ceil((double) count / (double) pageNumCnt));
		
		if (endPageNum > endPageNum_tmp) {
			endPageNum = endPageNum_tmp;
		}
		
		prev = startPageNum == 1 ? false:true;
		next = endPageNum * pageNumCnt >= count ? false:true;
		
		displayPost = (num - 1) * postNum;
	}
}
```

그리고 BoardController.class는 아래와 같이 주석처리하고 Page클래스를 넣어준다.

```java
package com.kimmjen.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kimmjen.domain.BoardVO;
import com.kimmjen.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	BoardService service;
	
	// 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {
		
		List<BoardVO> list = null;
		list = service.list();
		
		model.addAttribute("list", list);
		
	}
	
	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWrite() throws Exception {
		
	}
	
	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String postWrite(BoardVO vo) throws Exception {
		
		service.write(vo);
		
		return "redirect:/board/list";
	}
	
	// 게시물 조회
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void getView(@RequestParam("bno") int bno, Model model) throws Exception {
		
		BoardVO vo = service.view(bno);
		
//		service.view(bno);
		model.addAttribute("view",vo);
	}
	
	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception {
		
		BoardVO vo = service.view(bno);
		
		model.addAttribute("view", vo);
	}
	
	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postModify(BoardVO vo) throws Exception {
		
		service.modify(vo);
		
		return "redirect:/board/view?bno=" + vo.getBno();
	}
	
	// 게시물 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(@RequestParam("bno") int bno) throws Exception {
		
		service.delete(bno);
		
		return "redirect:/board/list";
	}
	
	// 게시물 총 갯수(목록) + 페이징 추가 
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
		
		Page page = new Page();

		page.setNum(num);
		page.setCount(service.count());
		
		List<BoardVO> list = null;
		list = service.listPage(page.getDisplayPost(), page.getPostNum());
		
		model.addAttribute("list", list);
		model.addAttribute("pageNum", page.getPageNum());
		
		model.addAttribute("startPageNum", page.getStartPageNum());
		model.addAttribute("endPageNum", page.getEndPageNum());
		
		model.addAttribute("prev", page.getPrev());
		model.addAttribute("next", page.getNext());
		
		model.addAttribute("select", num);
		/*
		 * // 게시물 총 갯수 int count = service.count();
		 * 
		 * // 한 페이지에 출력할 게시물 갯수 int postNum = 10;
		 * 
		 * // 하단 페이징 번호 ([ 게시물 총 갯수 % 한 페이지에 출력할 갯수] 의 올림) int pageNum =
		 * (int)Math.ceil((double) count / postNum);
		 * 
		 * // 출력할 게시물 int displayPost = (num - 1) * postNum;
		 * 
		 * // 첫번째방법------------------------------------------------------ // 한번에 표시할 페이징
		 * 번호의 갯수 int pageNum_cnt = 10;
		 * 
		 * // 표시되는 페이지 번호 중 마지막 번호 int endPageNum = (int)(Math.ceil((double)num /
		 * (double)pageNum_cnt) * pageNum_cnt);
		 * 
		 * // 표시되는 페이지 번호 중 첫번째 번호 int startPageNum = endPageNum - (pageNum_cnt - 1);
		 * 
		 * // 마지막 번호 재계산 int endPageNum_tmp = (int)(Math.ceil((double) count / (double)
		 * pageNum_cnt));
		 * 
		 * if (endPageNum > endPageNum_tmp) { endPageNum = endPageNum_tmp; }
		 * 
		 * boolean prev = startPageNum == 1 ? false:true; boolean next = endPageNum *
		 * pageNum_cnt >= count ? false:true;
		 * 
		 * // 첫번째방법------------------------------------------------------
		 * 
		 * 
		 * List<BoardVO> list = null; list = service.listPage(displayPost, postNum);
		 * model.addAttribute("list", list); model.addAttribute("pageNum", pageNum);
		 * 
		 * // 시작 및 끝 번호 model.addAttribute("startPageNum", startPageNum);
		 * model.addAttribute("endPageNum", endPageNum);
		 * 
		 * // 이전 및 다음 model.addAttribute("prev", prev); model.addAttribute("next",
		 * next);
		 * 
		 * // 현재 페이지 model.addAttribute("select", num);
		 */
	}
	
}

```

Page에 현재 페이지인 num, 게시물 총 갯수인 service.count()를 넣어주면 클래스 내부에서 계산<br>
이렇게 계산된 데이터는 page.getDisplayPost()처럼 호출하여 사용

page.jsp
```jsp
// -----------------페이지 데이터------------------------
model.addAttribute("pageNum", page.getPageNum());

model.addAttribute("startPageNum", page.getStartPageNum());
model.addAttribute("endPageNum", page.getEndPageNum());

model.addAttribute("prev", page.getPrev());
model.addAttribute("next", page.getNext());
// -----------------페이지 데이터------------------------		


이 부분을

model.addAttribute("page", page);
```

page의 데이터 전부를 뷰로 전달

listPage.jsp
```jsp
<c:if test="${prev }">
	<span>[ <a href="/board/listPage?num=${startPageNum - 1 }">이전</a> ]</span>
</c:if>
<c:forEach begin="${startPageNum }" end="${endPageNum }" var="num">
	<span>
		<c:if test="${select != num }">
			<a href="/board/listPage?num=${num }">${num }</a>
		</c:if>
		
		<c:if test="${select == num }">
			<b>${num }</b>
		</c:if>
		<%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
	</span>
</c:forEach>

<c:if test="${next }">
	<span>[ <a href="/board/listPage?num=${endPageNum - 1 }">다음</a> ]</span>
</c:if>

이 부분을

<c:if test="${page.prev }">
	<span>[ <a href="/board/listPage?num=${page.startPageNum - 1 }">이전</a>
		]
	</span>
</c:if>
<c:forEach begin="${page.startPageNum }" end="${page.endPageNum }" var="num">
	<span> <c:if test="${select != num }">
			<a href="/board/listPage?num=${num }">${num }</a>
		</c:if> <c:if test="${select == num }">
			<b>${num }</b>
		</c:if> <%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
	</span>
</c:forEach>

<c:if test="${page.next }">
	<span>[ <a href="/board/listPage?num=${page.endPageNum - 1 }">다음</a>
		]
	</span>
</c:if>
```

## 검색 구현 1-1
listPage -> listPageSearch 복사 붙여넣기<br>
/board/listPage -> /board/listPageSearch
```jsp
<c:if test="${page.prev }">
	<span>[ <a href="/board/listPage?num=${page.startPageNum - 1 }">이전</a>
		]
	</span>
</c:if>
<c:forEach begin="${page.startPageNum }" end="${page.endPageNum }" var="num">
	<span> <c:if test="${select != num }">
			<a href="/board/listPage?num=${num }">${num }</a>
		</c:if> <c:if test="${select == num }">
			<b>${num }</b>
		</c:if> <%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
	</span>
</c:forEach>

<c:if test="${page.next }">
	<span>[ <a href="/board/listPage?num=${page.endPageNum - 1 }">다음</a>
		]
	</span>
</c:if>

----------------------------------

<c:if test="${page.prev }">
	<span>[ <a href="/board/listPageSearch?num=${page.startPageNum - 1 }">이전</a>
		]
	</span>
</c:if>
<c:forEach begin="${page.startPageNum }" end="${page.endPageNum }" var="num">
	<span> <c:if test="${select != num }">
			<a href="/board/listPageSearch?num=${num }">${num }</a>
		</c:if> <c:if test="${select == num }">
			<b>${num }</b>
		</c:if> <%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
	</span>
</c:forEach>

<c:if test="${page.next }">
	<span>[ <a href="/board/listPageSearch?num=${page.endPageNum - 1 }">다음</a>
		]
	</span>
</c:if>
```

boardController.class
```java
// 게시물 목록 + 페이징 추가 + 검색
@RequestMapping(value = "/listPageSearch", method = RequestMethod.GET)
public void getListPageSearch(Model model, @RequestParam("num") int num) throws Exception {
	
	Page page = new Page();
	
	page.setNum(num);
	page.setCount(service.count());
	
	List<BoardVO> list = null;
	list = service.listPage(page.getDisplayPost(), page.getPostNum());
	
	model.addAttribute("list", list);
	model.addAttribute("page", page);
	model.addAttribute("select", num);
}
```

nav.jsp
```jsp
<li><a href="/board/listPageSearch?num=1">글 목록(페이징 + 검색)</a></li>
```

listPageSearch.jsp
```jsp
</c:if>
		
<div>
	<select name="searchType">
		<option value="title">제목</option>
		<option value="content"내용></option>
		<option value="title_content">제목 + 내용</option>
		<option value="writer">작성자</option>
	</select>
	
	<input type="text" name="keyword">
	
	<button type="button">검색</button>
</div>
```

DB에서 데이터를 가져오도록 쿼리를 작성
```sql
select
	bno, title, content, writer, regDate, viewCnt
from tbl_board
where title = "test title";

위 쿼리는 title이 일치할때만 가져오게 된다.
따라서

select
	bno, title, content, writer, regDate, viewCnt
from tbl_board
where title like '%test%';

like '%검색어%';  %가 앞이나 뒤에 다른 문자열이 있을 수 있다는 의미


select
	bno, title, content, writer, regDate, viewCnt
from tbl_board
where title like '%test%'
or content like '%수정된%';

제목 또는 내용
```

검색어 구현을 위해 boardMapper.xml에 쿼리 추가

```xml
<!-- 게시물 목록과 페이징 -->
<select id="listPage" parameterType="hashMap" resultType="com.kimmjen.domain.BoardVO">
	select
		bno, title, content, writer, regDate, viewCnt
	from tbl_board
	order by bno desc
		limit #{displayPost}, ${postNum};
</select>

----복사 붙여넣기 수정 ListPageSearch

<!-- 게시물 목록과 페이징 + 검색 -->
<select id="listPageSearch" parameterType="hashMap" resultType="com.kimmjen.domain.BoardVO">
	select
		bno, title, content, writer, regDate, viewCnt
	from tbl_board
	
	where title like '%#{keyword}%'
	
	order by bno desc
		limit #{displayPost}, ${postNum};

하지만 '%#{keyword}%' 이렇게 사용하면 '%'keyword'%' 데이터가 %키워드% 이렇게 되기 때문에
문제를 해결하기 위해 concat을 이용한다.

<!-- 게시물 목록과 페이징 + 검색 -->
<select id="listPageSearch" parameterType="hashMap" resultType="com.kimmjen.domain.BoardVO">
	select
		bno, title, content, writer, regDate, viewCnt
	from tbl_board
	
	where title like concat('%', #{keyword}, '%')
	
	where content like concat('%', #{keyword}, '%')
	
	where title like concat('%', #{keyword}, '%')
		or content like concat('%', #{keyword}, '%')
		
	where writer like concat('%', #{keyword}, '%')
	
	order by bno desc
		limit #{displayPost}, ${postNum};
</select>

모든 경우의 조건문을 사용하지만 이대로 는 사용 불가하기 때문에 매퍼에서 조건문을 사용

searchType의 값에 따라 제목, 내용, 제목+내용, 작성자로 구분

<if test='searchType.equals("title")'>
	where title like concat('%', #{keyword}, '%')
</if>
<if test='searchType.equals("content")'>
	where content like concat('%', #{keyword}, '%')
</if>
<if test='searchType.equals("title_content")'>
	where title like concat('%', #{keyword}, '%')
		or content like concat('%', #{keyword}, '%')
</if>
<if test='searchType.equals("writer")'>
	where writer like concat('%', #{keyword}, '%')
</if>
```

하지만 searchType과 keyword를 잡아오는 곳은 DAO, Service + Impl

boardDAO.interface
```java
// 게시물 목록과 페이징 + 검색
public List<BoardVO> listPageSearch(int displayPost, int postNum, String searchType, String keyword) throws Exception;
```
boardDAOImpl.class
```java
// 게시물 목록과 페이징 + 검색
@Override
public List<BoardVO> listPageSearch(int displayPost, int postNum, String searchType, String keyword)
		throws Exception {
	// TODO Auto-generated method stub
	
	HashMap<String, Object> data = new HashMap<String, Object>();
	
	data.put("displayPost", displayPost);
	data.put("postNum", postNum);
	
	data.put("searchType", searchType);
	data.put("keyword", keyword);
	
	return sql.selectList(namesapce + ".listPageSearch", data);
}
```
boardService.interface
```java
// 게시물 목록과 페이징 + 검색
public List<BoardVO> listPageSearch(int displayPost, int postNum, String searchType, String keyword) throws Exception;
```
boardServiceImpl.class
```java
// 게시물 목록과 페이징 + 검색
@Override
public List<BoardVO> listPageSearch(int displayPost, int postNum, String searchType, String keyword)
		throws Exception {
	// TODO Auto-generated method stub
	return dao.listPageSearch(displayPost, postNum, searchType, keyword);
}
```

BoardController.class<br>


```java
// 게시물 목록 + 페이징 추가 + 검색
@RequestMapping(value = "/listPageSearch", method = RequestMethod.GET)
public void getListPageSearch(
		Model model, 
		@RequestParam("num") int num,
		@RequestParam("searchType") String searchType,
		@RequestParam("keyword") String keyword
		) throws Exception {
	
	Page page = new Page();
	
	page.setNum(num);
	page.setCount(service.count());
	
	List<BoardVO> list = null;
//		list = service.listPage(page.getDisplayPost(), page.getPostNum());
	list = service.listPageSearch(page.getDisplayPost(), page.getPostNum(), searchType, keyword);
	
	model.addAttribute("list", list);
	model.addAttribute("page", page);
	model.addAttribute("select", num);
}

매개변수에 추가, URL를 통해 searchType과 keyword를 받아낼수 있다.
그리고 기존 list 주석처리후 service.listPageSearch로 바꾼다.
하지만 @RequestParam에 searchType과 keyword는 데이터는 컨트롤러에서는 받지만 URL에서는 못받기 때문에 아래와 같이 수정한다.

// 게시물 목록 + 페이징 추가 + 검색
@RequestMapping(value = "/listPageSearch", method = RequestMethod.GET)
public void getListPageSearch(
		Model model, 
		@RequestParam("num") int num,
		@RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
		@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
		) throws Exception {
	
	Page page = new Page();
	
	page.setNum(num);
	page.setCount(service.count());
	
	List<BoardVO> list = null;
	// list = service.listPage(page.getDisplayPost(), page.getPostNum());
	list = service.listPageSearch(page.getDisplayPost(), page.getPostNum(), searchType, keyword);
	
	model.addAttribute("list", list);
	model.addAttribute("page", page);
	model.addAttribute("select", num);
}
```

- value는 받고자할 데이터의 키
- required는 해당 데이터의 필수여부
- defaultvalue는 만약 데이터가 들어오지 않았을 경우 대신할 기본 값

하지만 정상적인 작동이 아니라 매번 URL에 &searchType=title&keyword=테스트 입력하기 힘들 기 때문에<br>
button 태그에 id=searchBtn추가하고 script도 추가해줍니다.

listPageSearch.jsp
```jsp
	<div>
		<select name="searchType">
			<option value="title">제목</option>
			<option value="content"내용></option>
			<option value="title_content">제목 + 내용</option>
			<option value="writer">작성자</option>
		</select>
		
		<input type="text" name="keyword">
		
		<button type="button" id="searchBtn">검색</button>
	</div>
	
</div>
<script>
	document.getElementById("searchBtn").onclick = function() {
		
		let searchType = document.getElementsByName("searchType")[0].value;
		let keyword = document.getElementsByName("keyword")[0].value;
		
		console.log(searchType);
		console.log(keyword);
	}
</script>
```
- id가 searchBtn인 html 엘리먼트에 클릭 이벤트가 발생하면, function() {} 내부의 코드가 실행
- name이 searchType인 html엘리먼트 중 첫 번째([0])의 값을 변수(let) searchType 에 저장
- name이 keyword html 엘리먼트 중 첫번째 ([0])의 값을 변수(let) keyword에 저장

첫 번째([0])을 사용하는 이유는, html 엘리먼트에 사용되는 name속성은 2개 이상의 복수로 사용할 수 있기 때문에 document.getElementsByName()로 데이터를 가져오려면 배열로 가져오기 때문에 가장 첫 번째인 0번째 데이터를 가져오는 것

위에 console.log값을 개발자도구로 확인 후 제대로 작동하면
listPageSearch.jsp
```jsp
document.getElementById("searchBtn").onclick = function() {
			
	let searchType = document.getElementsByName("searchType")[0].value;
	let keyword = document.getElementsByName("keyword")[0].value;
	
	location.href = "/board/listPageSearch?num=1" + "&searchType=" + searchType + "&keyword=" + keyword;
	
	// console.log(searchType);
	// console.log(keyword);
}

이렇게 수정
```

- location.href = [URL] 해당 URL로 이동하는 기능
- searchType은 선택한 검색 타입
- keyword는 검색어가 들어가므로, '작성자'를 선택하고 '123'을 입력했다면 이동될 실제 URL은

`/board/listPageSearch?num=1&searchType=writer&keyword=123` 이렇게 나올 것이다.

## 검색 구현 1-2
1. 페이지 번호를 클릭하여 페이지를 이동하면, 검색조건과 검색어가 없어짐
	: 검색조건과 검색어를 유지할 수 잇는 코드가 없기 때문
2. 검색겨과와 무고나하게 페이징이 생성됨
	: 페이징 기능에서 전체 게시물 갯수를 가져올 때 조건을 두지 않아서

boardController.class에 있는 page.setCount(service.count());가 페이징을 만들때 게시물의 갯수를 구하는 메서드

```
select count(bno) from tbl_board;

아무런 조건없이 게시물 전체 갯수를가져옴

select count(bno) from tbl_board
where title like concat('%', '1', '%');

조건을 넣어주면, 조건에 맞는 게시물만 가져오므로 갯수가 달라짐.
```

boardMapper.xml
```xml
<!-- 게시물 총 갯수 + 검색 적용 -->
<select id="searchCount" parameterType="hashMap" resultType="int">
	select count(bno) from tbl_board
	
	<if test='searchType.equals("title")'>
		where title like concat('%', #{keyword}, '%')
	</if>
	<if test='searchType.equals("content")'>
		where content like concat('%', #{keyword}, '%')
	</if>
	<if test='searchType.equals("title_content")'>
		where title like concat('%', #{keyword}, '%')
			or content like concat('%', #{keyword}, '%')
	</if>
	<if test='searchType.equals("writer")'>
		where writer like concat('%', #{keyword}, '%')
	</if>
	
</select>
```

조건을 추가하면서, searchType과 keyword를 받아야하므로 위에 파라미터 타입(HashMap)을 추가

BoardDAO.interface
```java
// 게시물 총 갯수 + 검색적용
public int searchCount(String searchType, String keyword) throws Exception;
```
BoardDAOImpl.class
```java
// 게시물 총 갯수 + 검색적용
@Override
public int searchCount(String searchType, String keyword) throws Exception {
	// TODO Auto-generated method stub
	HashMap<String, Obejct> data = new HashMap<String, Obejct>();
	
	data.put("searchType", searchType);
	data.put("keyword", keyword);
	
	return sql.selectOne(namesapce + ".searchCount", data);
}
```
BoardService.interface
```java
// 게시물 총 갯수 + 검색적용
public int searchCount(String searchType, String keyword) throws Exception;
```
BoardServiceImpl.class
```java
// 게시물 총 갯수 + 검색적용
@Override
public int searchCount(String searchType, String keyword) throws Exception {
	// TODO Auto-generated method stub
	return dao.searchCount(searchType, keyword);
}
```

BoardController.class 에서 중간 page
```java
page.setNum(num);
// page.setCount(service.count());
page.setCount(service.searchCount(searchType, keyword));

// 검색조건, 검색어 유지
model.addAttribute("searchType", searchType);
model.addAttribute("keyword", keyword);

```

listPageSearch.jsp
```jsp
</c:if>

<div>
	<select name="searchType">
		<option value="title">
		<c:if test="${searchType eq 'title' }">selected</c:if>
		제목
		</option>
		<option value="content"내용>
		<c:if test="${searchType eq 'content' }">selected</c:if>
		내용
		</option>
		<option value="title_content">
		<c:if test="${searchType eq 'title_content' }">selected</c:if>
		제목 + 내용
		</option>
		<option value="writer">
		<c:if test="${searchType eq 'writer' }">selected</c:if>
		작성자
		</option>
	</select>
	
	<input type="text" name="keyword" value="${page.keyword }">
	
	<button type="button" id="searchBtn">검색</button>
</div>
```

`jstl문법 중 if문`
- <c:if test="${page.searchType eq 'title'}">selected</c:if>
	: test내부에는 조건이 들어가고, 이 조건이 참인 경우 <c:if></c:if> 사이에 있는 문자인 selected를 출력, 거짓인 경우 아무것도 출력하지 않는다.
- ${page.searchType eq 'title'}
	: page.searchType와 문자열 title가 같은지(equals) 확인, jstl에서는 .equals(); 대신 eq로 짧게 사용

검색 조건을 제목으로 한 경우, 컨트롤러로 전송되는 searchType의 값은 title이며,<br>
이 값이 다시 뷰(jsp)로 전송되면, searchType의 값과 같은 값을 가진 option 태그에 selected가 생겨서 선택된 상태<<br>

이렇게 까지 했지만 첫번째 페이지만 적용되는 검색어 사이트.
<br>
다시 수정

Page.class 에 변수와 메서드 추가.
```java
// 검색 타입과 검색어
private String searchTypekeyword;

public void setSearchTypeKeyword(String searchType, String keyword) {
	
	if (searchType.equals("") || keyword.equals("")) {
		searchTypekeyword = "";
	} else {
		searchTypekeyword = "&searchType=" + searchType + "&keyword=" + keyword;
	}
}

public String getSearchTypeKeyword() {
	return searchTypekeyword;
}

=> setSearchTypeKeyword(); 메서드를 이용해, 검색조건(searchType)과 검색어(keyword)가 공란("")이 아니라면, url 뒤에 들어갈 `&searchTpye=[검색조건]&keyword=[검색어1]`
```

page.class에 setSearchTypeKeyword 메서드를 호출하며, 필요한 매개변수인 searchType, keyword 추가.
```java
// 검색타입과 검색어
page.setSearchTypeKeyword(searchType, keyword);
```

listPageSearch.jsp 에서 prev와next 쪽에  ${page.searchTypeKeyword} 추가
```java
<div>
	<c:if test="${page.prev }">
		<span>[ <a href="/board/listPageSearch?num=${page.startPageNum - 1 }${page.searchTypeKeyword}">이전</a>
			]
		</span>
	</c:if>
	<c:forEach begin="${page.startPageNum }" end="${page.endPageNum }" var="num">
		<span> <c:if test="${select != num }">
				<a href="/board/listPageSearch?num=${num }${page.searchTypeKeyword}">${num }</a>
			</c:if> <c:if test="${select == num }">
				<b>${num }</b>
			</c:if> <%-- <a href="/board/listPage?num=${num }">${num }</a> --%>
		</span>
	</c:forEach>

	<c:if test="${page.next }">
		<span>[ <a href="/board/listPageSearch?num=${page.endPageNum + 1 }${page.searchTypeKeyword}">다음</a>
			]
		</span>
	</c:if>
```

But,<br>

BoardController.class에서 setSearchTypeKeyword 메서드에 검색조건과 검색어가 사용되는데,<br>
모델에다가 또 검색조건과 검색어를 넣을 필요가 없다. 그래서 수정

BoardController.class
```java
// 게시물 목록 + 페이징 추가 + 검색
@RequestMapping(value = "/listPageSearch", method = RequestMethod.GET)
public void getListPageSearch(
		Model model, 
		@RequestParam("num") int num,
		@RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType,
		@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
		) throws Exception {
	
	Page page = new Page();
	
	page.setNum(num);
//		page.setCount(service.count());
//		page.setCount(service.searchCount(searchType, keyword));
	
	// 검색타입과 검색어
	page.setSearchTypeKeyword(searchType, keyword);
	
	List<BoardVO> list = null;
	// list = service.listPage(page.getDisplayPost(), page.getPostNum());
	list = service.listPageSearch(page.getDisplayPost(), page.getPostNum(), searchType, keyword);
	
	model.addAttribute("list", list);
	model.addAttribute("page", page);
	model.addAttribute("select", num);
	
	// 검색조건, 검색어 유지
//		model.addAttribute("searchType", searchType);
//		model.addAttribute("keyword", keyword);
}


searchType, keyword 주석처리.
```

Page.class
```java
// 검색 타입과 검색어
//	private String searchTypekeyword;
//	
//	public void setSearchTypeKeyword(String searchType, String keyword) {
//		
//		if (searchType.equals("") || keyword.equals("")) {
//			searchTypekeyword = "";
//		} else {
//			searchTypekeyword = "&searchType=" + searchType + "&keyword=" + keyword;
//		}
//	}
//	
//	public String getSearchTypeKeyword() {
//		return searchTypekeyword;
//	}

-----------------------

// 검색어 수정
public String getSearchTypeKeyword() {
	
	if (searchType.equals("") || keyword.equals("")) {
		return "";
	} else {
		return "&searchType=" + searchType + "&keyword=" + keyword;
	}
}
private String searchType;
private String keyword;

public String getSearchType() {
	return searchType;
}
public void setSearchType(String searchType) {
	this.searchType = searchType;
}
public String getKeyword() {
	return keyword;
}
public void setKeyword(String keyword) {
	this.keyword = keyword;
}
```

searchType과 keyword는 평범하게 입력 받는 것이고, `.setSearchTypeKeyword()`는 입력받은 searchType과 keyword가 공란("")이라면 공란을 그대로 반환하고, 공란("")이 아닌 경우 url에 추가될 문자열을 반환.
<br>

BoardController.class에서 <br>
page.setSearchTypeKeyword(searchType, keyword);<br>
, model.addAttribute("searchType", searchType);<br>
, model.addAttribute("keyword", keyword); 는 주석처리<br>
```java
Page page = new Page();
		
page.setNum(num);
//		page.setCount(service.count());
//		page.setCount(service.searchCount(searchType, keyword));

// 검색타입과 검색어
//		page.setSearchTypeKeyword(searchType, keyword);
page.setSearchType(searchType);
page.setKeyword(keyword);
```

listPageSearch.jsp에서 searchType과 keyword는 page에서 불러올 수 있으므로, 앞에 `.page`만 추가
```jsp
<div>
	<select name="searchType">
		<option value="title">
		<c:if test="${searchType eq 'title' }">selected</c:if>
		제목
		</option>
		<option value="content"내용>
		<c:if test="${searchType eq 'content' }">selected</c:if>
		내용
		</option>
		<option value="title_content">
		<c:if test="${searchType eq 'title_content' }">selected</c:if>
		제목 + 내용
		</option>
		<option value="writer">
		<c:if test="${searchType eq 'writer' }">selected</c:if>
		작성자
		</option>
	</select>
	
	<input type="text" name="keyword" value="${page.keyword }">
	
	<button type="button" id="searchBtn">검색</button>
</div>

-------------------------------

<div>
	<select name="searchType">
		<option value="title">
		<c:if test="${page.searchType eq 'title' }">selected</c:if>
		제목
		</option>
		<option value="content"내용>
		<c:if test="${page.searchType eq 'content' }">selected</c:if>
		내용
		</option>
		<option value="title_content">
		<c:if test="${page.searchType eq 'title_content' }">selected</c:if>
		제목 + 내용
		</option>
		<option value="writer">
		<c:if test="${page.searchType eq 'writer' }">selected</c:if>
		작성자
		</option>
	</select>
	
	<input type="text" name="keyword" value="${page.keyword }">
	
	<button type="button" id="searchBtn">검색</button>
</div>
```

## 댓글 기본 설정 및 조회 구현
board폴더에 view.jsp 
````java
<!-- 댓글시작 -->
	<hr />
	<ul>
		<li>
		<div>
			<p>첫번째 댓글 작성자</p>
			<p>첫번째 댓글</p>
		</div>
	</li>
	<li>
		<div>
			<p>두번째 댓글 작성자</p>
			<p>두번째 댓글</p>
		</div>
	</li>
	<li>
		<div>
			<p>세번째 댓글 작성자</p>
			<p>세번째 댓글</p>
		</div>
	</li>
	</ul>
	<div>
		<p>
			<label>댓글 작성자</label> <input type="text">
		</p>
		<p>
			<textarea rows="5" cols="5-"></textarea>
		</p>
		<p>
			<button tpye"button">댓글 작성</button>
		</p>
	</div>
	<!-- 댓글 끝 -->
```

댓글 테이블 만들기

```java
create table tbl_reply (
	rno int not null auto_increment,
    bno int not null,
    writer varchar(30) not null,
    content text not null,
    regDate timestamp not null default now(),
    primary key(rno, bno),
    foreign key(bno) references tbl_board(bno)
);

commit;

select * from tbl_reply;
```

댓글을 저장하기 위한 테이블인 tbl_reply를 생성

- rno: 댓글의 고유 번호
- bno: 댓글이 작성된 게시물의 번호

댓글 테이블은 게시물 테이블에 종속된 하위 테이블이므로, 상위 테이블의 pk(Primary Key) 컬럼인 bno를 fk(foreign key)로 설정

`SQL` insert, update, delete
```sql
// 댓글 테이블 생성
create table tbl_reply (
	rno int not null auto_increment,
    bno int not null,
    writer varchar(30) not null,
    content text not null,
    regDate timestamp not null default now(),
    primary key(rno, bno),
    foreign key(bno) references tbl_board(bno)
);

commit;

// 확인
select * from tbl_reply;

select * from tbl_board
order by bno desc;

-- insert문
insert into tbl_reply(bno, writer, content, regDate)
	value(444, '댓글 작성자', '댓글 내용', sysdate());

-- 수정문
update tbl_reply set
	writer = '댓글 작성자_수정',
    content = '댓글 내용_수정'
where rno = 1
	and bno = 444;
    
-- delete
delete from tbl_reply
where rno = 1
	and bno = 444;

select * from tbl_reply;

// insert
insert into tbl_reply(bno, writer, content, regDate)
	value(444, '댓글 작성자', '댓글 내용', sysdate());
    
// 댓글 확인
select
	rno, bno, writer, content, regDate
from tbl_reply
	where bno = 444;
```

ReplyVO 클래스생성.

```java
package com.kimmjen.domain;

import java.util.Date;

public class ReplyVO {
	
	/*
	 * create table tbl_reply ( rno int not null auto_increment, bno int not null,
	 * writer varchar(30) not null, content text not null, regDate timestamp not
	 * null default now(), primary key(rno, bno), foreign key(bno) references
	 * tbl_board(bno) );
	 */
	private int rno;
	private int bno;
	private String writer;
	private String content;
	private Date regDate;
	
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public int getBno() {
		return bno;
	}
	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
}
```

댓글 조회는 게시물 번호(bno)만 이용하여 조회하되 결과는 ReplyVO의 형태로 반환되므로, 파라미터타입은 정수형(int)이며 리절트 타입은 ReplyVO입니다.
<br>

댓글 작성/수정/삭제는 게시물 번호(bno)와 댓글 번호(rno)가 모두 필요하며, 추가적으로 작성자(wrtier), 댓글내용(content), 작성 날짜(regDate)가 필요하며, 반환되는 데이터는 없으므로 파라미터타입은 ReplyVO이고 리절트타입은 없다.
<br>

replyMapper.xml 생성
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kimmjen.mappers.reply">

	<!-- 댓글 조회 -->
	<select id="replyList" parameterType="int" resultType="com.kimmjen.domain.ReplyVO">
		select
			rno, bno, writer, content, regDate
		from tbl_reply
			where bno = #{bno};
	</select>
	
	<!-- 댓글 작성 -->
	<select id="replyWrite" parameterType="com.kimmjen.domain.ReplyVO">
		insert into tbl_reply(bno, writer, content, regDate)
			value(${bno}, #{writer}, #{content}, #{regDate});
	</select>
	
	<!-- 댓글 수정 -->
	<select id="replyModify" parameterType="com.kimmjen.domain.ReplyVO">
		update tbl_reply set
			writer = #{writer},
			content = #{content}
		where rno = #{rno}
			and bno = #{bno};
	</select>
	
	<!-- 댓글 삭제 -->
	<delete id="replyDelete" parameterType="com.kimmjen.domain.ReplyVO">
		delete from tbl_reply
		where rno = #{rno}
			and bno = #{bno};
	</delete>

</mapper>
````

그리고 매퍼에 접속할 DAO(Date Obejct Access)를 생성<br>

com.board.dao 패키지에 interface인 ReplyDAO생성

ReplyDAO.interface
```java
package com.kimmjen.dao;

import java.util.List;

import com.kimmjen.domain.ReplyVO;

public interface ReplyDAO {

	// 댓글 조회
	public List<ReplyVO> list(int bno) throws Exception;
	
	// 댓글 입력
	public void write(ReplyVO vo) throws Exception;
	
	// 댓글 수정
	public void modify(ReplyVO vo) throws Exception;
	
	// 댓글 삭제
	public void delete(ReplyVO vo) throws Exception;
}

```

ReplyDAOImpl.class
```java
package com.kimmjen.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import com.kimmjen.domain.ReplyVO;

@Repository
public class ReplyDAOImpl implements ReplyDAO {
	
	@Inject
	private SqlSession sql;
	
	private static String namespace = "com.kimmjen.mappers.reply";

	// 댓글 조회
	@Override
	public List<ReplyVO> list(int bno) throws Exception {
		// TODO Auto-generated method stub
		return sql.selectList(namespace + ".replyList", bno);
	}

	// 댓글 작성
	@Override
	public void write(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.insert(namespace + ".replyWrite", vo);
		
		
	}
	
	// 댓글 수정
	@Override
	public void modify(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.update(namespace + ".replyModify", vo);
		
	}

	// 댓글 삭제
	@Override
	public void delete(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		sql.delete(namespace + ".replyDelete", vo);
		
	}

}

```

ReplyService.interface
```java
package com.kimmjen.service;

import java.util.List;

import com.kimmjen.domain.ReplyVO;

public interface ReplyService {

	// 댓글 조회
	public List<ReplyVO> list(int bno) throws Exception;
	
	// 댓글 입력
	public void write(ReplyVO vo) throws Exception;
	
	// 댓글 수정
	public void modify(ReplyVO vo) throws Exception;
	
	// 댓글 삭제
	public void delete(ReplyVO vo) throws Exception;
}

```
ReplyServiceImpl.class
```java
package com.kimmjen.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.kimmjen.dao.ReplyDAO;
import com.kimmjen.domain.ReplyVO;


@Service
public class ReplyServiceImpl implements ReplyService {

	@Inject
	private ReplyDAO dao;
	
	// 댓글 조회
	@Override
	public List<ReplyVO> list(int bno) throws Exception {
		// TODO Auto-generated method stub
		return dao.list(bno);
	}

	// 댓글 작성
	@Override
	public void write(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.write(vo);
	}

	// 댓글 수정
	@Override
	public void modify(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.modify(vo);
	}

	// 댓글 삭제
	@Override
	public void delete(ReplyVO vo) throws Exception {
		// TODO Auto-generated method stub
		dao.delete(vo);
	}
}
```

ReplyController 클래스 생성
```java

```

BoardController.class 에 추가 해주기
<br>
이유는 게시물을 읽어오면서 댓글도 같이 읽어오는 방식으로 진행하기 때문에

```java
@Inject
private ReplyService replyService;

...

// 게시물 조회
@RequestMapping(value = "/view", method = RequestMethod.GET)
public void getView(@RequestParam("bno") int bno, Model model) throws Exception {
	
	BoardVO vo = service.view(bno);
	
//		service.view(bno);
	model.addAttribute("view",vo);
	
	// 댓글 조회
	List<ReplyVO> reply = null;
	reply = replyService.list(bno);
	model.addAttribute("reply", reply);
}
```


view.jsp 수정 DB에서 값 받아오기
```jsp
<li>
	<div>
		<p>첫번째 댓글 작성자</p>
		<p>첫번째 댓글</p>
	</div>
</li>
<li>
	<div>
		<p>두번째 댓글 작성자</p>
		<p>두번째 댓글</p>
	</div>
</li>
<li>
	<div>
		<p>세번째 댓글 작성자</p>
		<p>세번째 댓글</p>
	</div>
</li>

...수정

<c:forEach items="${reply }" var="reply">
<li>
	<div>
		<p>${reply.writer } / ${reply.regDate }</p>
		<p>${reply.content }</p>
	</div>
</li>
</c:forEach>
```

반복문을 이용해서 값 받아오기<br>

그리고 날짜 표기 바꾸기

view.sjp
```jsp
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
...

<div>
<p>${reply.writer } / <fmt:formatDate value="${reply.regDate }" pattern="yyyy-MM-dd"/> </p>
<p>${reply.content }</p>
</div>
```

## 댓글 작성 구현
댓글 구성

- rno: 댓글 고유 번호 -> 테이블을 생성할 때 설정한 auto_increment 값을 가지며 자동으로 증가되는 값
- bno: 게시물 고유번호 -> 댓글을 작성할 때 조회중인 게시물의 고유번호 값
- writer: 댓글 작성자 -> 직접작성
- content: 댓글 내용 -> 직접작성
- regDate: 댓글 작성 날짜 -> 쿼리문을 이용하여 넣어주는 값

댓글 작성시 필요한 데이터

- bno: 게시물 고유번호
- writer: 댓글 작성자
- content: 댓글 내용

view.jsp 수정
```jsp
<form method="post" action="/reply/writer">
	<p>
		<label>댓글 작성자</label> <input type="text" name="writer">
	</p>
	<p>
		<textarea rows="5" cols="50" name="content"></textarea>
	</p>
	<p>
		<button type="submit">댓글 작성</button>
	</p>
</form>
	<!-- <p>
		<label>댓글 작성자</label> <input type="text">
	</p>
	<p>
		<textarea rows="5" cols="5-"></textarea>
	</p>
	<p>
		<buttontpye"button">댓글 작성</button>
	</p> -->
</div>
```
게시물 작성구현과 같은 post방식의 form이지만, action에 /reply/write가 있다.<br>
form 내부의 데이터를 post형식으로 보내되, /reply/write의 경로로 보낸다는 걸 추측

게시물 작성 구현에 action을 사용하지 않은 이유는 현재의 url로 데이터를 보내기 때문.

```jsp
<p>
	+ <input tpye="hidden" name="bno" value="${view.bno }">  추가
	<button type="submit">댓글 작성</button>
</p>
```

input추가, hidden은 실제로 값은 담고있지만, 화면에 표시하지 않겠다.<br>

ReaplyController.class

```java
// 댓글 작성
@RequestMapping(value = "/write", method = RequestMethod.POST)
public String postWrite(ReplyVO vo) throws Exception {
	replyService.write(vo);
	
	return "redirect:/board/view?bno=" + vo.getBno();
}
```
댓글 데이터 순서
- 1. 게시물 조회 페이지에서 댓글 작성 후 등록
- 2. /reply/write 에 해당되는 컨트롤러 데이터 전달
- 3. service -> dao -> mapper를 통해 db에 데이터 등록
- redirect:/board/view?bno= 로 인해 1번에서 조회 중이던 게시물로 이동

