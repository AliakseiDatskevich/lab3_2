package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Justyna on 30.01.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    @Test
    public void hasEmptyListForNoNews() {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        Configuration configuration = mock(Configuration.class);
        when(configurationLoader.loadConfiguration()).thenReturn(configuration);

        NewsReader newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader(anyString())).thenReturn(newsReader);

        when(newsReader.read()).thenReturn(prepareIncomingNews());

        NewsLoader newsLoader = new NewsLoader();

        PublishableNews publishableNews = newsLoader.loadNews();

        List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
        List<String> subscribentContent = (List<String>) Whitebox.getInternalState(publishableNews, "subscribentContent");

        assertTrue(publicContent.isEmpty());
        assertTrue(subscribentContent.isEmpty());
    }

    private IncomingNews prepareIncomingNews() {
        IncomingNews news = new IncomingNews();
        return news;
    }
}