import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class SeleniumHomeworkTests {

    private static final Logger logger = LogManager.getLogger(SeleniumHomeworkTests.class);

    WebDriver driver;

    @AfterAll
    public void tearDown() {
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    @DisplayName("1. Headless режим: ввод текста в поле и проверка")
    public void testHeadlessInput() {
        logger.info("Запуск теста: Headless режим");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://otus.home.kartushin.su/training.html");

        WebElement inputField = driver.findElement(By.id("textInput"));
        String textToEnter = "ОТУС";
        inputField.sendKeys(textToEnter);

        String enteredText = inputField.getAttribute("value");
        assertEquals(textToEnter, enteredText, "Текст в поле не совпадает с введённым");

        logger.info("Тест 1 пройден: текст введён и проверен");
    }

    @Test
    @DisplayName("2. Режим киоска: открытие модального окна")
    public void testKioskModal() {
        logger.info("Запуск теста: Режим киоска");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--kiosk");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://otus.home.kartushin.su/training.html");

        WebElement modalButton = driver.findElement(By.id("openModalBtn"));
        modalButton.click();

        WebElement modal = driver.findElement(By.className("modal-content"));
        assertTrue(modal.isDisplayed(), "Модальное окно не открылось");

        logger.info("Тест 2 пройден: модальное окно открыто");
        driver.findElement(By.id("closeModal")).click();
    }

    @Test
    @DisplayName("3. Полноэкранный режим: заполнение формы")
    public void testFullscreenForm() {
        logger.info("Запуск теста: Полноэкранный режим");

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://otus.home.kartushin.su/training.html");

        String name = "фыв";
        String email = "asdf@sdfg.rt";

        WebElement nameInput = driver.findElement(By.id("name"));
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        nameInput.sendKeys(name);
        emailInput.sendKeys(email);
        submitButton.click();

        WebElement message = driver.findElement(By.id("messageBox"));
        String expectedMessage = "Форма отправлена с именем: " + name + " и email: " + email;
        assertTrue(message.getText().contains(expectedMessage), "Сообщение не совпадает с ожидаемым");

        logger.info("Тест 3 пройден: форма отправлена, сообщение проверено");
    }
}