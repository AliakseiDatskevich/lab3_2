package staticmock.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
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
@PrepareForTest({NewsReaderFactory.class, PublishableNews.class, ConfigurationLoader.class})
public class NewsLoaderTest {

    private PublishableNews publishableNews = null;
    private PublishableNews publishableNewsMock = mock(PublishableNews.class);
    private NewsLoader newsLoader = new NewsLoader();
    private ConfigurationLoader configurationLoaderMock = mock(ConfigurationLoader.class);
    private Configuration configurationMock = mock(Configuration.class);
    private NewsReader newsReaderMock = mock(NewsReader.class);
    private IncomingNews incomingNewsMock = mock(IncomingNews.class);
    private IncomingInfo incomingInfoMock = mock(IncomingInfo.class);
    private List<IncomingInfo> listOfIncomingInfoMocks = new ArrayList();

    @Before
    public void setUp() {
        publishableNews = PublishableNews.create();
        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(PublishableNews.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);
    }

    @After
    public void tearDown() {
        listOfIncomingInfoMocks.clear();
    }

    @Test
    public void loadNewsShouldReturnOnePublishableNewsWithOnePublicElement() {
        int expectedOutput = 1;
        String readerType = "File";
        String content = "Test";

        listOfIncomingInfoMocks.add(incomingInfoMock);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNews);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);
        when(incomingInfoMock.requiresSubsciption()).thenReturn(false);
        when(incomingInfoMock.getContent()).thenReturn(content);

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> actualOutput = Whitebox.getInternalState(actualNews, "publicContent");

        assertThat(actualOutput.size(), is(expectedOutput));
    }

    @Test
    public void loadNewsShouldReturnOnePublishableNewsWithNoPublicElements() {
        int expectedOutput = 0;
        String readerType = "File";

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNews);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> actualOutput = Whitebox.getInternalState(actualNews, "publicContent");

        assertThat(actualOutput.size(), is(expectedOutput));
    }

    @Test
    public void loadNewsShouldReturnOnePublishableNewsWithTwoPublicElements() {
        int expectedOutput = 2;
        String readerType = "File";
        String content = "Test";

        listOfIncomingInfoMocks.add(incomingInfoMock);
        listOfIncomingInfoMocks.add(incomingInfoMock);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNews);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);
        when(incomingInfoMock.requiresSubsciption()).thenReturn(false);
        when(incomingInfoMock.getContent()).thenReturn(content);

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> actualOutput = Whitebox.getInternalState(actualNews, "publicContent");

        assertThat(actualOutput.size(), is(expectedOutput));
    }

    @Test
    public void addSubscriptionMethodShouldBeCalledOnce() {
        int expectedTimes = 1;
        String readerType = "File";
        String content = "Test";

        listOfIncomingInfoMocks.add(incomingInfoMock);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNewsMock);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);
        when(incomingInfoMock.requiresSubsciption()).thenReturn(true);
        when(incomingInfoMock.getContent()).thenReturn(content);
        when(incomingInfoMock.getSubscriptionType()).thenReturn(SubsciptionType.A);

        newsLoader.loadNews();

        verify(publishableNewsMock, times(expectedTimes)).addForSubscription(content, SubsciptionType.A);
    }

    @Test
    public void addSubscriptionMethodShouldBeCalledTwice() {
        int expectedTimes = 2;
        String readerType = "File";
        String content = "Test";

        listOfIncomingInfoMocks.add(incomingInfoMock);
        listOfIncomingInfoMocks.add(incomingInfoMock);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNewsMock);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);
        when(incomingInfoMock.requiresSubsciption()).thenReturn(true);
        when(incomingInfoMock.getContent()).thenReturn(content);
        when(incomingInfoMock.getSubscriptionType()).thenReturn(SubsciptionType.A);

        newsLoader.loadNews();

        verify(publishableNewsMock, times(expectedTimes)).addForSubscription(content, SubsciptionType.A);
    }

    @Test
    public void addSubscriptionMethodShouldNotBeCalled() {
        int expectedTimes = 0;
        String readerType = "File";
        String content = "Test";

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(configurationMock.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNewsMock);
        when(PublishableNews.create()).thenReturn(publishableNewsMock);
        when(incomingNewsMock.elems()).thenReturn(listOfIncomingInfoMocks);

        newsLoader.loadNews();

        verify(publishableNewsMock, times(expectedTimes)).addForSubscription(content, SubsciptionType.A);
    }

}
