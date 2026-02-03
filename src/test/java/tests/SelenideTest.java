package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.WebConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import helpers.Attach;

import java.util.Map;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

@DisplayName("Поиск issues через разную реализацию")
public class SelenideTest {
    private final static String REPOSITORY = "VSemenov95/homeWorkAllure";
    private static final int ISSUE = 1;
    private static final WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

    @BeforeAll
    public static void preCondition() {

        Configuration.browser = webConfig.getBrowserName();
        Configuration.browserSize = webConfig.getBrowserSize();
        Configuration.browserVersion = webConfig.getBrowserVersion();
        Configuration.remote = webConfig.getRemoteURL();
        Configuration.baseUrl = webConfig.getBaseUrl();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of("enableVNC", true, "enableVideo", true));
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    public void setup() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }


    @Test
    @DisplayName("Поиск issues по номеру. Реализация: Чистый Selenide с Listener")
    public void searchIssuesFromGitRepositoryTest() {
        open("");
        $(".search-input").click();
        $("#query-builder-test").sendKeys(REPOSITORY);
        $("#query-builder-test").submit();
        $(linkText(REPOSITORY)).click();
        $("#issues-tab").click();
        $(withText("#" + ISSUE)).should(Condition.exist);
    }

    @Test
    @DisplayName("Поиск issues по номеру. Реализация: Лямбда шаги через step")
    public void searchIssuesFromGitRepositoryWithlambdaTest() {
        step("Открыть главную страницу", () -> {
            open("");
        });
        step("Найти репозиторий " + REPOSITORY, () -> {
            $(".search-input").click();
            $("#query-builder-test").sendKeys(REPOSITORY);
            $("#query-builder-test").submit();
        });
        step("Перейти в найденный репозиторий" + REPOSITORY, () -> {
            $(linkText(REPOSITORY)).click();
        });
        step("Открыть табу Issues", () -> {
            $("#issues-tab").click();
        });
        step("Проверить наличие #" + ISSUE + "Issue", () -> {
            $(withText("#" + ISSUE)).should(Condition.exist);
        });
    }

    @Test
    @DisplayName("Поиск issues по номеру. Реализация: Шаги с аннотацией @Step")
    public void searchIssuesFromGitRepositoryWithStepAnnotationTest() {
        WebSteps steps = new WebSteps();
        steps.openMainPage();
        steps.searchForRepository(REPOSITORY);
        steps.clickOnRepositoryLink(REPOSITORY);
        steps.openIssuesTab();
        steps.shouldSeeIssueWithNumber(ISSUE);
    }
}
