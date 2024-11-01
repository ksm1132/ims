package com.ims.inventorymgmtsys.ui;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderUiTest {

    @Value("${local.server.port}")
    int randomPort;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
//        WebDriverManager.chromedriver().setup();
//        WebDriver driver = new ChromeDriver();
//        Configuration.baseUrl = "http://localhost:" + randomPort;
//        Configuration.browser = "chrome";
//        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\20240909\\chromedriver-win64\\chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", "/home/mks/IdeaProjects/inventoryms/chromedriver-linux64/chromedriver");

//        System.setProperty("webdriver.gecko.driver", "/home/mks/fireFoxDriver/geckodriver*");


    }

    private WebDriver webDriver;

    @Test
    @Sql("/OrderUiTest.sql")
    @Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void test_注文() {
        open("/catalog/list");
        $("div.register a.newaccount").click();
        $("input[name=userName").setValue("mkasa");
        $("input[name=password").setValue("abcd");
        $("input[name=emailAddress").setValue("test@gmail.com");
        $("input[value='Sign Up']").click();
        $("div.login a").click();
        $("input[name=username").setValue("mkasa");
        $("input[name=password").setValue("abcd");
        $("input[value=Login]").click();
        $("a[href*=productId]").should(visible).click();
        $("input[id=quantity]").setValue("5");
        $("input[value=カートに追加]").click();
        $("input[value=注文する]").click();
        $("input[name=name]").setValue("テスト太郎");
        $("input[name=address]").setValue("六本木");
        $("input[name=phone]").setValue("08012234567");
        $("input[name=emailAddress]").setValue("test@gmail.com");
        $("input[name=paymentMethod]").selectRadio("CREDIT_CARD");
        SelenideElement employeeSelect = $("#employee");

        // "佐藤" のオプションを選択
        employeeSelect.selectOptionContainingText("佐藤");

        $("input[value=注文内容を確認]").click();
        $("input[value=注文]").click();

        SelenideElement backLink = $(".back-link a");
        backLink.shouldBe(visible).click();

        executeJavaScript("openNav();");
        $("#mySidebar").shouldBe(Condition.visible);
        $("a[href='/order/orderlist']").click();
        $("h1").shouldHave(Condition.text("注文履歴"));

        SelenideElement firstRow = $$(".container table tbody tr").first();
        String orderId = firstRow.$("td").text();

        Map<String, Object> reservationMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE orderid = ?", orderId);
        Assertions.assertThat(reservationMap.get("customer_name")).isEqualTo("テスト太郎");

    }


}
