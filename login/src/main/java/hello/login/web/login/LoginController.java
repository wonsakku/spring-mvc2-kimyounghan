package hello.login.web.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
	private final SessionManager sessionManager;
	
	@GetMapping("/login")
	public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
		return "login/loginForm";
	}
	

//	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		if(bindingResult.hasErrors()) {
			return "login/loginForm";
		}
		
		Member loginMember =loginService.login(form.getLoginId(), form.getPassword());
		
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		// 쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
		Cookie idCookie = new Cookie("memberId", loginMember.getId() + "");
		response.addCookie(idCookie);
		
		return "redirect:/";
	}
	
	
//	@PostMapping("/login")
	public String login2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		if(bindingResult.hasErrors()) {
			return "login/loginForm";
		}
		
		Member loginMember =loginService.login(form.getLoginId(), form.getPassword());
		
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		// 쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
		sessionManager.createSession(loginMember, response);
		
		return "redirect:/";
	}
	
	
	@PostMapping("/login")
	public String login3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
		if(bindingResult.hasErrors()) {
			return "login/loginForm";
		}
		
		Member loginMember =loginService.login(form.getLoginId(), form.getPassword());
		
		if(loginMember == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		// 로그인 성공 처리
		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
		
		return "redirect:/";
	}
	
	
	
//	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		expireCookie(response, "memberId");
		return "redirect:/";
	}
	
//	@PostMapping("/logout")
	public String logout2(HttpServletRequest request, HttpServletResponse response) {
		sessionManager.expire(request);
		return "redirect:/";
	}

	
	@PostMapping("/logout")
	public String logout3(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return "redirect:/";
	}
	
	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	
	
}



















