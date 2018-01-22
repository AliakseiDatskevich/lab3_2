package edu.iis.mto.staticmock;

import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        Configuration configuration = new Configuration();
        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        mockStatic(NewsReaderFactory.class);
        NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
        NewsReader newsReader = mock(NewsReader.class);
        IncomingNews incomingNews = new IncomingNews();
        IncomingInfo infoA = new IncomingInfo("infoA", SubsciptionType.A);
        IncomingInfo infoB = new IncomingInfo("infoB", SubsciptionType.B);
        IncomingInfo infoC = new IncomingInfo("infoC", SubsciptionType.C);
        IncomingInfo infoNone = new IncomingInfo("infoNone", SubsciptionType.NONE);
        incomingNews.add(infoA);
        incomingNews.add(infoB);
        incomingNews.add(infoC);
        incomingNews.add(infoNone);
        String readerType = "test";
        when(newsReader.read()).thenReturn(incomingNews);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
    }

    @Test
    public void testLoadNews() {}
}
