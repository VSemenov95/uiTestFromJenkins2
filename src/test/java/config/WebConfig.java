package config;

import org.aeonbits.owner.Config;

@Config.Sources({

        "classpath:${env}.properties", "classpath:local.properties"})

public interface WebConfig extends Config {
    String BROWSER = "CHROME";
    String SCREEN = "1920x1080";
    String BASE_URL = "https://github.com";
    String TIMEOUT = "5000";

    @Key("browserName")
    @DefaultValue(BROWSER)
    String getBrowserName();

    @Key("browserVersion")
    String getBrowserVersion();

    @Key("browserSize")
    @DefaultValue(SCREEN)
    String getBrowserSize();

    @Key("baseUrl")
    @DefaultValue(BASE_URL)
    String getBaseUrl();

    @Key("remoteUrl")
    String getRemoteURL();

    @Key("timeout")
    @DefaultValue(TIMEOUT)
    long getTimeout();
}