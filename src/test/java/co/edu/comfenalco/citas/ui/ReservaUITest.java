package co.edu.comfenalco.citas.ui;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.comfenalco.citas.repositorio.CitaRepository;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("ui")
class ReservaUITest {

    @LocalServerPort
    int puerto;

    @Autowired
    CitaRepository repo;

    WebDriver driver;

    @BeforeEach
    void abrirNavegador() {
        repo.deleteAll();
        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(opciones);
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void completarFormulario(String profesional, String fecha, String paciente) {
        driver.findElement(By.id("profesional")).clear();
        driver.findElement(By.id("profesional")).sendKeys(profesional);
        ((JavascriptExecutor) driver).executeScript(
                "const el=document.getElementById('fechaHora'); el.value=arguments[0]; "
                        + "el.dispatchEvent(new Event('change', {bubbles:true}));", fecha);
        driver.findElement(By.id("pacienteId")).clear();
        driver.findElement(By.id("pacienteId")).sendKeys(paciente);
        driver.findElement(By.id("btnReservar")).click();
    }

    private void esperarMensaje(String texto) {
        boolean encontrado = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("mensaje"), texto));
        assertThat(encontrado).isTrue();
    }

    @Test
    void debeConfirmarLaReservaAlPaciente() {
        driver.get("http://localhost:" + puerto + "/citas.html");
        completarFormulario("Dra. Gómez", "2030-11-05T09:00", "paciente-010");
        esperarMensaje("Cita reservada");
    }

    @Test
    void debeRechazarDobleReserva() {
        driver.get("http://localhost:" + puerto + "/citas.html");
        completarFormulario("Dr. López", "2030-11-06T10:00", "paciente-011");
        esperarMensaje("Cita reservada");
        completarFormulario("Dr. López", "2030-11-06T10:00", "paciente-012");
        esperarMensaje("Horario no disponible");
    }
}
