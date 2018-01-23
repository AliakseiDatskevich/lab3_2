package edu.iis.mto.staticmock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReader newsReader;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        Configuration configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        String readerType = "test";
        Whitebox.setInternalState(configuration, "readerType", readerType);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        IncomingNews incomingNews = new IncomingNews();
        IncomingInfo infoA = new IncomingInfo("infoA", SubsciptionType.A);
        IncomingInfo infoB = new IncomingInfo("infoB", SubsciptionType.B);
        IncomingInfo infoC = new IncomingInfo("infoC", SubsciptionType.C);
        IncomingInfo infoNone = new IncomingInfo("infoNone", SubsciptionType.NONE);
        incomingNews.add(infoA);
        incomingNews.add(infoB);
        incomingNews.add(infoC);
        incomingNews.add(infoNone);
        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);
        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
    }

    @Test
    public void testSizePublicContentReturnOne() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicContent = Whitebox.getInternalState(publishableNews, "publicContent");
        assertThat(publicContent.size(), is(1));
    }

    @Test
    public void testSizeSubscribentContentReturnThree() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> subscribentContent = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertThat(subscribentContent.size(), is(3));
    }

    @Test
    public void testPublicContentReturnOneElement() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicContent = Whitebox.getInternalState(publishableNews, "publicContent");
        assertThat(publicContent.toArray(new String[1]), is(new String[] {"infoNone"}));
    }

    @Test
    public void testSubscribentContentReturnThreeElement() {
        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> subscribentContent = Whitebox.getInternalState(publishableNews, "subscribentContent");
        assertThat(subscribentContent.toArray(new String[3]), is(new String[] {"infoA", "infoB", "infoC"}));
    }

    @Test
    public void testReadNewsReaderCallOnce() {
        newsLoader.loadNews();
        Mockito.verify(newsReader, Mockito.times(1)).read();
    }
}
