package edu.iis.mto.staticmock.test;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
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

public class NewsLoaderTest {

    private NewsLoader newsLoader;
    private NewsReader mockedNewsReader;
    private Configuration configuration;
    private ConfigurationLoader mockedConfigurationLoader;
    private IncomingNews mockedIncomingNews;
    private PublishableNews publishableNews;
    private IncomingInfo mockedIncomingInfo;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        publishableNews = spy(PublishableNews.create());

        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        mockStatic(PublishableNews.class);

        mockedConfigurationLoader = mock(ConfigurationLoader.class);
        mockedIncomingNews = mock(IncomingNews.class);
        mockedNewsReader = mock(NewsReader.class);
        mockedIncomingInfo = mock(IncomingInfo.class);

        String readerTypeValue = "readerType";

        Whitebox.setInternalState(configuration, "readerType", readerTypeValue);

        when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(mockedNewsReader);
        when(mockedNewsReader.read()).thenReturn(mockedIncomingNews);
        when(PublishableNews.create()).thenReturn(publishableNews);
    }

    @Test
    public void isNewsReaderReadMethodCalledOnce() {
        newsLoader.loadNews();
        verify(mockedNewsReader, times(1)).read();
    }

    @Test
    public void isNewsLoaderLoadNewsMethodReturningPublishableNewsWithOnePublicElement() {
        List<IncomingInfo> incomingInfoList = new ArrayList<>();
        incomingInfoList.add(mockedIncomingInfo);

        when(mockedIncomingNews.elems()).thenReturn(incomingInfoList);
        when(mockedIncomingInfo.requiresSubsciption()).thenReturn(false);
        when(mockedIncomingInfo.getContent()).thenReturn("TEST CONTENT");

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> publicContent = Whitebox.getInternalState(actualNews, "publicContent");
        assertThat(publicContent.size(), Matchers.is(1));
    }

    @Test
    public void isNewsLoaderLoadNewsMethodReturningPublishableNewsWithOneSubscribedElement() {
        List<IncomingInfo> incomingInfoList = new ArrayList<>();
        incomingInfoList.add(mockedIncomingInfo);

        when(mockedIncomingNews.elems()).thenReturn(incomingInfoList);
        when(mockedIncomingInfo.requiresSubsciption()).thenReturn(true);
        when(mockedIncomingInfo.getContent()).thenReturn("TEST CONTENT");
        when(mockedIncomingInfo.getSubscriptionType()).thenReturn(SubsciptionType.A);

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> subscribentContent = Whitebox.getInternalState(actualNews, "subscribentContent");
        assertThat(subscribentContent.size(), Matchers.is(1));
    }

    @Test
    public void isNewsLoaderLoadNewsMethodReturningPublishableNewsWithOnePublicAndOneSubscribedElements() {
        List<IncomingInfo> incomingInfoList = new ArrayList<>();
        incomingInfoList.add(new IncomingInfo("TEST CONTENT", SubsciptionType.NONE));
        incomingInfoList.add(new IncomingInfo("TEST CONTENT", SubsciptionType.A));

        when(mockedIncomingNews.elems()).thenReturn(incomingInfoList);

        PublishableNews actualNews = newsLoader.loadNews();
        List<String> publicContent = Whitebox.getInternalState(actualNews, "publicContent");
        List<String> subscribentContent = Whitebox.getInternalState(actualNews, "subscribentContent");

        assertThat(publicContent.size(), Matchers.is(1));
        assertThat(subscribentContent.size(), Matchers.is(1));
    }

    @Test
    public void howManyTimesAddPublicInfoMethodIsCalled() {
        List<IncomingInfo> incomingInfoList = new ArrayList<>();
        incomingInfoList.add(mockedIncomingInfo);
        incomingInfoList.add(mockedIncomingInfo);
        incomingInfoList.add(mockedIncomingInfo);

        when(mockedIncomingNews.elems()).thenReturn(incomingInfoList);

        newsLoader.loadNews();
        verify(publishableNews, times(incomingInfoList.size())).addPublicInfo(any(String.class));
    }
}
