package demoqa.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import demoqa.models.AddBookModel;
import demoqa.models.LoginResponseModel;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static demoqa.tests.TestData.credentials;

public class DeleteOneBookTest extends BaseTest {

    @Test
    void deleteBook() {
        LoginResponseModel loginResponse = authorizationApi.login(credentials);

        step("Авторизоваться в профиле с пустой корзиной", () ->
                bookApi.deleteAllBooks(loginResponse));

        step("Добавить книгу в профиль", () ->
                bookApi.addBook(loginResponse, new AddBookModel()));

        step("Удалить книгу из профиля", () ->
                bookApi.deleteBook(loginResponse, "9781491904244"));

        step("Открыть UI и убедиться, что книга отсутствует", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
            getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));
            open("/profile");
            $("[href='profile?book=9781491904244']").shouldNot(exist);
        });
    }
}
