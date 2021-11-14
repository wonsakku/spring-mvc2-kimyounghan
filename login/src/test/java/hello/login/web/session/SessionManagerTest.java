package hello.login.web.session;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.login.domain.member.Member;

class SessionManagerTest {
	
	
	SessionManager sessionManager = new SessionManager();
	
	@Test
	@DisplayName("세션 테스트")
	void sessionTest() {
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		// 세션 생성
		Member member = new Member();
		sessionManager.createSession(member, response);
		
		// 요청에 응답 쿠키 저장
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(response.getCookies());
		
		
		// 세션 조회
		Object result = sessionManager.getSession(request);
		assertThat(result).isEqualTo(member);
		
		// 세션 만료
		sessionManager.expire(request);
		Object expired = sessionManager.getSession(request);
		assertThat(expired).isNull();
	}

}


























