package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private NewsReader newsReader;

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        Configuration configuration = mock(Configuration.class);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader(anyString())).thenReturn(newsReader);

     @Test
        public void hasEmptyListForNoNews() {
        when(newsReader.read()).thenReturn(prepareIncomingNews());

        NewsLoader newsLoader = new NewsLoader();

        PublishableNews publishableNews = newsLoader.loadNews();

        List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
        List<String> subscribentContent = (List<String>) Whitebox.getInternalState(publishableNews, "subscribentContent");

        assertTrue(publicContent.isEmpty());
        assertTrue(subscribentContent.isEmpty());
    }

        @Test
        public void hasPublicContent() {
            when(newsReader.read()).thenReturn(prepareIncomingNews1());

            NewsLoader newsLoader = new NewsLoader();

            PublishableNews publishableNews = newsLoader.loadNews();

            List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
            List<String> subscribentContent = (List<String>) Whitebox.getInternalState(publishableNews, "subscribentContent");

            assertEquals(publicContent.size(), 2);
            assertTrue(subscribentContent.isEmpty());

            assertEquals(publicContent.get(0), "content");
            assertEquals(publicContent.get(1), "content1");
        }


        @Test
        public void hasPublicAndSubscribentContent() {
            when(newsReader.read()).thenReturn(prepareIncomingNews2());

            NewsLoader newsLoader = new NewsLoader();

            PublishableNews publishableNews = newsLoader.loadNews();

            List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
            List<String> subscribentContent = (List<String>) Whitebox.getInternalState(publishableNews, "subscribentContent");

            assertEquals(publicContent.size(), 1);
            assertEquals(subscribentContent.size(), 2);

            assertEquals(publicContent.get(0), "publicContent");
            assertEquals(subscribentContent.get(0), "content");
            assertEquals(subscribentContent.get(1), "content1");
        }

    private IncomingNews prepareIncomingNews() {
        IncomingNews news = new IncomingNews();
        return news;
    }

    private IncomingNews prepareIncomingNews1() {
        IncomingNews news = new IncomingNews();
        news.add(new IncomingInfo("content", SubsciptionType.NONE));
        news.add(new IncomingInfo("content1", SubsciptionType.NONE));
        return news;
    }

    private IncomingNews prepareIncomingNews2() {
        IncomingNews news = new IncomingNews();
        news.add(new IncomingInfo("content", SubsciptionType.A));
        news.add(new IncomingInfo("publicContent", SubsciptionType.NONE));
        news.add(new IncomingInfo("content1", SubsciptionType.C));
        return news;
    }
} 