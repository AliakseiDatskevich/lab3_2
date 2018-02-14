package edu.iis.mto.staticmock.reader;

import edu.iis.mto.staticmock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class) @PrepareForTest({NewsReaderFactory.class, ConfigurationLoader.class})
public class NewsLoaderTest {

    private NewsLoader newsLoader = new NewsLoader();
    private NewsReaderFactory mockedNewsReaderFactory;
    private ConfigurationLoader mockedConfigurationLoader;
    private Configuration mockedConfiguration;
    private NewsReader mockedNewsReader;
    private IncomingNews mockedIncomingNews;
    private IncomingInfo mockedIncomingInfo;
    private List<IncomingInfo> incomingInfos;

    @Before
    public void init() {
        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);
        mockedNewsReaderFactory = mock(NewsReaderFactory.class);
        mockedConfigurationLoader = mock(ConfigurationLoader.class);
        mockedConfiguration = mock(Configuration.class);
        mockedNewsReader = mock(NewsReader.class);
        mockedIncomingNews = mock(IncomingNews.class);
        mockedIncomingInfo = mock(IncomingInfo.class);
        incomingInfos = new ArrayList<>();

    }

    @Test
    public void loadNewsReturnOnePublishableNewsWithNoPublicElements() {
        int expectedElementsCount = 0;
        String readerType = "Sport";
        incomingInfos.add(mockedIncomingInfo);

        when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
        when(mockedConfigurationLoader.loadConfiguration()).thenReturn(mockedConfiguration);
        when(mockedConfiguration.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(mockedNewsReader);
        when(mockedNewsReader.read()).thenReturn(mockedIncomingNews);
        when(mockedIncomingNews.elems()).thenReturn(incomingInfos);
        when(mockedIncomingInfo.requiresSubsciption()).thenReturn(Boolean.TRUE);

        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");

        assertThat(publicContent.size(), is(expectedElementsCount));

    }

    @Test
    public void loadNewsReturnOnePublishableNewsWithTwoPublicElements() {
        int expectedElementsCount = 2;
        String readerType = "Sport";
        incomingInfos.add(mockedIncomingInfo);
        incomingInfos.add(mockedIncomingInfo);

        when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
        when(mockedConfigurationLoader.loadConfiguration()).thenReturn(mockedConfiguration);
        when(mockedConfiguration.getReaderType()).thenReturn(readerType);
        when(NewsReaderFactory.getReader(readerType)).thenReturn(mockedNewsReader);
        when(mockedNewsReader.read()).thenReturn(mockedIncomingNews);
        when(mockedIncomingNews.elems()).thenReturn(incomingInfos);
        when(mockedIncomingInfo.requiresSubsciption()).thenReturn(Boolean.FALSE);

        PublishableNews publishableNews = newsLoader.loadNews();
        List<String> publicContent = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");

        assertThat(publicContent.size(), is(expectedElementsCount));

    }
}
