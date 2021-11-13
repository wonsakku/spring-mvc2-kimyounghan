package hello.itemservice.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

	@Autowired
	MessageSource ms;
	
	@Test
	void helloMessage() {
		String result = ms.getMessage("hello", null, null);
		assertEquals(result, "안녕");
		assertThat(result).isEqualTo("안녕");
	}
	
	@Test
	@DisplayName("메세지 코드가 없을 때")
	void notFoundMessageCode() {
		assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
		.isInstanceOf(NoSuchMessageException.class);
	}
	
	@Test
	@DisplayName("기본 메세지 주기")
	void notFoundDefaultMessageCode() {
		String result = ms.getMessage("no_code", null, "기본 메세지", null);
		assertThat(result).isEqualTo("기본 메세지");
	}
	
	
	@Test
	@DisplayName("파라미터 넘기기")
	void argumentMessage() {
		String result = ms.getMessage("hello.name", new Object[] {"Spring"}, null);
		assertThat(result).isEqualTo("안녕 Spring");
	}
	
	@Test
	@DisplayName("국제화 테스트 - KOREA")
	void defaultLang() {
		assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
		assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
	}
	
	@Test
	@DisplayName("국제화 테스트 - ENGLISH")
	void enLang() {
		assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}

}



















