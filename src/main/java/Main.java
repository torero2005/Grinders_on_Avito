import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class Main {

    public static void main(String[] args) throws Exception {

            String avitoUrl = "https://www.avito.ru";
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(avitoUrl);

            //Ожидание появленя и заполянение инпут поля поиска
            WebElement webelement = new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='app']/div/div[3]/div/div[1]/div/div[3]/div[1]/div[2]/div[1]/div/div/div/label[1]/input")));
            webelement.sendKeys("Grinders");

            //Клик на кнопку найти
            driver.findElement(By.xpath("//*[@id='app']/div/div[3]/div/div[1]/div/div[3]/div[1]/div[2]/div[2]/button/span")).click();

            //Ожидание загрузки селекта и выбор сортировки по дате
            webelement = new WebDriverWait(driver, Duration.ofSeconds(10)).
                until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='app']/div/div[3]/div/div[2]/div[3]/div[3]/div[1]/div[2]/label/div/select")));
            Select select = new Select(webelement);
            select.selectByValue("104");

            //В pageSource поместить HTML страницы
            String pageSource = driver.getPageSource();

            //Парсинг страницы
            Document document = Jsoup.parse(pageSource, "avito.ru");

            //Заголовки всех товаров со страницы в itemsNames
            Elements itemsNames = document.select(".styles-module-root-QmppR").select("[itemprop=name]");

            //Ссылки на товары в itemsLinks
            Elements itemsLinks = document.select(".iva-item-title-py3i_").select("[data-marker=item-title]");

            //Даты/время размещения товаров в itemsDates
            Elements itemsDates = document.select("[data-marker=item-date]");

            //Цены товаров в itemsPrices
            Elements itemsPrices = document.select(".styles-module-root-LIAav").select("span");

            //Вывод названия товара, ссылки на товар, сстоимости товара и времени публикации товара ЕСЛИ во времени публикации содержатся минуты или секунды
            int counter = 0;
            for (Element elem : itemsLinks) {
                if (itemsDates.get(counter).text().contains("минут") || itemsDates.get(counter).text().contains("секунд")
                                                                  || itemsDates.get(counter).text().contains("только что")) {
                    System.out.println(elem.text());
                    System.out.println("http://avito.ru/" + elem.attr("href"));
                    System.out.println(itemsPrices.get(counter).text());
                    System.out.println(itemsDates.get(counter).text());
                    System.out.println("----------------------------------------------------------");
                    counter++;
                } else {
                    if (counter == 0) {
                        System.out.println("Новых объявлений нет");
                    }
                    break;
                }
            }

            driver.close();
    }

}