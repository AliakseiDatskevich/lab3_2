package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Lukasz on 2018-01-30.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private NewsLoader newsLoader = new NewsLoader();
    private Configuration configuration = new Configuration();
    private IncomingNews incomingNews = new IncomingNews();
    private PublishableNews publishableNews = PublishableNews.create();
    @Mock
    private NewsReader newsReaderMock;
    @Mock
    private ConfigurationLoader mockedConfigurationLoader;


    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        String readerTypeValue = "readerTypeValue";
        Whitebox.setInternalState(configuration, "readerTypeValue", readerTypeValue);
        when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNews);
    }

    @Test
    public void loadNewsInvokeReadMethodOnlyOnce() {
        newsLoader.loadNews();
        verify(newsReaderMock, times(1)).read();
    }

    @Test
    public void properlyAddSubscription() {
        publishableNews.addForSubscription("", SubsciptionType.A);
        List<String> news = Whitebox.getInternalState(publishableNews, "");
        assertThat(news.size(), Matchers.is(1));
    }

}
