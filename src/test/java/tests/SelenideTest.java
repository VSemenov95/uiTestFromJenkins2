package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

@DisplayName("Поиск issues через разную реализацию")
public class SelenideTest {
    private final static String REPOSITORY = "VSemenov95/homeWorkAllure";
    private static final int ISSUE = 1;

    @BeforeAll
    public static void preCondition() {
        Configuration.baseUrl = "https://github.com";
    }

    @BeforeEach
    public void setup() {
        SelenideLogger.addListener("allure", new AllureSelenide());
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
        step("Найти репозиторий" + REPOSITORY, () -> {
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
