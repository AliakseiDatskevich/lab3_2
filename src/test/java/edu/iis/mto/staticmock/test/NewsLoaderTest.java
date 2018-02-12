package edu.iis.mto.staticmock.test;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReader mockedNewsReader;
    private Configuration configuration;
    private ConfigurationLoader mockedConfigurationLoader;
    private IncomingNews incomingNews;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        mockedConfigurationLoader = mock(ConfigurationLoader.class);
        incomingNews = new IncomingNews();
        mockedNewsReader = mock(NewsReader.class);
        String readerTypeValue = "readerType";
        Whitebox.setInternalState(configuration, "readerType", readerTypeValue);
        when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(mockedNewsReader);
        when(mockedNewsReader.read()).thenReturn(incomingNews);
    }

}
