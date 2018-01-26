package lab3_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class})

public class Zad3_2NewsLoaderTest {

    private IncomingNews incomingNews;
    private IncomingInfo infoForAll;
    private IncomingInfo infoForSubscribers;
    private NewsLoader newsLoader;
    private NewsReader newsReader;
    private NewsReaderFactory newsReaderFactory;
    private String readerType;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);

        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        ConfigurationLoader configurationMockLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationMockLoader.loadConfiguration()).thenReturn(configuration);
        readerType = "typReadera";
        infoForAll = new IncomingInfo("publicContent", SubsciptionType.NONE);
        infoForSubscribers = new IncomingInfo("subscribentContent", SubsciptionType.B);
        newsReader = mock(NewsReader.class);
        incomingNews = new IncomingNews();

        newsReaderFactory = mock(NewsReaderFactory.class);

        newsLoader = new NewsLoader();
        Whitebox.setInternalState(configuration, "readerType", readerType);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationMockLoader);

        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        mockStatic(NewsReaderFactory.class);
        when(newsReader.read()).thenReturn(incomingNews);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
    }

    @Test
    public void oneInfoWithNoSubscripionNeededProperlyLoadedTest() {
        incomingNews.add(infoForAll);
        PublishableNews publishableNews = newsLoader.loadNews();
        ArrayList<String> newsList = Whitebox.getInternalState(publishableNews, "publicContent");
        assertEquals(1, newsList.size());
        assertTrue(newsList.contains(infoForAll.getContent()));
        assertFalse(newsList.contains(infoForSubscribers.getContent()));

    }

    @Test
    public void fiveInfoWithNoSubscripionNeededFiveLoadsTest() {
        for (int i = 0; i < 5; i++) {
            incomingNews.add(infoForAll);
        }
        PublishableNews publishableNews = newsLoader.loadNews();
        ArrayList<String> newsList = Whitebox.getInternalState(publishableNews, "publicContent");
        assertEquals(5, newsList.size());
    }

}
