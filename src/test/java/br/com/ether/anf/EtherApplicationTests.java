package br.com.ether.anf;

import br.com.ether.utilities.SeleniumUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class EtherApplicationTests {
private final SeleniumUtility seleniumUtility;
	@Test
	void contextLoads() {
		WebDriver driver = seleniumUtility.getDriver(true, false,"");
	driver.get("http://www.youtube.com");
	}

}
