package tests;
import com.codeborne.selenide.Selenide;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
public class RemoteWebDriverFake extends RemoteWebDriver{
    private Selenide    selenide;
}
