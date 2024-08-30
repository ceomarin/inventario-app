import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InventarioFunctionalTests {
    private static final String INVENTARIO_FRONTEND_URL = "http://localhost:3000";
    private static final String NEW_PRODUCT_NAME = "Oster coffee maker";
    private static final String NEW_PRODUCT_DESCRIPTION = "Cafetera de filtro";
    private static final String NEW_PRODUCT_STOCK = "15";
    private static final String NEW_PRODUCT_CREATED_OK_MSG = "Producto creado exitosamente";

    private static final String BUTTON_OPEN_MODAL_ID = "btn-open-modal";
    private static final String MODAL_PRODUCTS_ID = "modalProducts";
    private static final String FIELD_NOMBRE_ID = "nombre";
    private static final String FIELD_DESCRIPCION_ID = "descripcion";
    private static final String FIELD_STOCK_ID = "stock";
    private static final String BUTTON_SAVE_MODAL_ID = "btn-save-modal";
    private static final String CONFIRMATION_MESSAGE_XPATH = "/html/body/div[2]";
    private static final String CONFIRMATION_MESSAGE_TITLE_ID = "swal2-title";
    private static final String BUTTON_MODAL_OK_XPATH = "/html/body/div[2]/div/div[6]/button[1]";
    private static final String PRODUCTS_TABLE_LAST_ROW_XPATH = "//*[@id=\"root\"]/div/div[1]/div[2]/div/div/table/tbody/tr[last()]/td";

    private WebDriver driver;

    private WebDriverWait wait;

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    @Test
    void testAddNewProductOK() {
        driver.get(INVENTARIO_FRONTEND_URL);

        WebElement addProductButton = driver.findElement(By.id(BUTTON_OPEN_MODAL_ID));
        addProductButton.click();

        WebElement modal = driver.findElement(By.id(MODAL_PRODUCTS_ID));
        wait.until(ExpectedConditions.visibilityOf(modal));

        WebElement productNameField = driver.findElement(By.id(FIELD_NOMBRE_ID));
        productNameField.sendKeys(NEW_PRODUCT_NAME);

        WebElement descriptionField = driver.findElement(By.id(FIELD_DESCRIPCION_ID));
        descriptionField.sendKeys(NEW_PRODUCT_DESCRIPTION);

        WebElement stockField = driver.findElement(By.id(FIELD_STOCK_ID));
        stockField.sendKeys(NEW_PRODUCT_STOCK);

        WebElement saveButton = driver.findElement(By.id(BUTTON_SAVE_MODAL_ID));
        saveButton.click();

        WebElement confirmationMessage = driver.findElement(By.xpath(CONFIRMATION_MESSAGE_XPATH));
        wait.until(ExpectedConditions.visibilityOf(confirmationMessage));

        WebElement modalMessage = driver.findElement(By.id(CONFIRMATION_MESSAGE_TITLE_ID));
        wait.until(ExpectedConditions.visibilityOf(modalMessage));

        assertThat(modalMessage.getText()).isEqualTo(NEW_PRODUCT_CREATED_OK_MSG);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement modalOkButton = driver.findElement(By.xpath(BUTTON_MODAL_OK_XPATH));
        wait.until(ExpectedConditions.visibilityOf(modalOkButton));

        modalOkButton.click();

        List<String> productsTableLastRowItems = driver.findElements(By.xpath(PRODUCTS_TABLE_LAST_ROW_XPATH)).stream()
                .map(WebElement::getText)
                .toList();

        assertThat(productsTableLastRowItems).containsAll(List.of(NEW_PRODUCT_NAME, NEW_PRODUCT_DESCRIPTION, NEW_PRODUCT_STOCK));
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}