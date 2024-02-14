package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardRequestTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendRequestCardSuccess() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Петров Алексей");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79271232457");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement actualTextElement = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        String actualText = actualTextElement.getText().trim();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        assertTrue(actualTextElement.isDisplayed());
        assertEquals(expectedText, actualText);

    }

    @Test
    public void displayErrorIfIncorrectName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Petrov Petr");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79271232457");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement actualTextElement = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        String actualText = actualTextElement.getText().trim();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        assertTrue(actualTextElement.isDisplayed());
        assertEquals(expectedText, actualText);
    }
}
