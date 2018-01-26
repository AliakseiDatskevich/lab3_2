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
    private IncomingInfo infoForSubscribersB;
    private IncomingInfo infoForSubscribersC;
    private NewsLoader newsLoader;
    private NewsReader newsReader;
    private String readerType;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        infoForAll = new IncomingInfo("publicContent", SubsciptionType.NONE);
        infoForSubscribersB = new IncomingInfo("subscribentContent", SubsciptionType.B);
        infoForSubscribersC = new IncomingInfo("subscribentContent", SubsciptionType.C);
        newsReader = mock(NewsReader.class);
        incomingNews = new IncomingNews();
        newsLoader = new NewsLoader();
        Whitebox.setInternalState(configuration, "readerType", readerType);
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
        assertFalse(newsList.contains(infoForSubscribersB.getContent()));
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

    @Test
    public void oneInfoWithSubscripionNeededProperlyLoadedTest() {
        incomingNews.add(infoForSubscribersB);
        PublishableNews publishableNews = newsLoader.loadNews();
        ArrayList<String> newsList = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertEquals(1, newsList.size());
        assertFalse(newsList.contains(infoForAll.getContent()));
        assertTrue(newsList.contains(infoForSubscribersB.getContent()));
    }

    @Test
    public void tenInfoWithSubscripionNeededTenLoadsTest() {
        for (int i = 0; i < 10; i++) {
            incomingNews.add(infoForSubscribersC);
        }
        PublishableNews publishableNews = newsLoader.loadNews();
        ArrayList<String> newsList = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertEquals(10, newsList.size());
    }

    @Test
    public void twoInfoWithDifferentSubscripionTypeNeededTwoLoadsTest() {

        incomingNews.add(infoForSubscribersC);
        incomingNews.add(infoForSubscribersB);
        PublishableNews publishableNews = newsLoader.loadNews();
        ArrayList<String> newsList = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertEquals(2, newsList.size());
    }

}
