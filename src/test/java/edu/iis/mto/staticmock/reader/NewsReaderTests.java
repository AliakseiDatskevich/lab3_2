package edu.iis.mto.staticmock.reader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Matchers.*;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;
import edu.iis.mto.staticmock.SubsciptionType;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class })

public class NewsReaderTests {

	private NewsReader newsReader;
	private ConfigurationLoader configurationLoader;

	@Before
	public void setUp() {
		mockStatic(ConfigurationLoader.class);
		mockStatic(NewsReaderFactory.class);
		mockStatic(PublishableNews.class);

		configurationLoader = mock(ConfigurationLoader.class);
		Configuration configuration = mock(Configuration.class);
		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		doReturn(configuration).when(configurationLoader).loadConfiguration();
		doReturn("WS").when(configuration).getReaderType();

		newsReader = mock(NewsReader.class);
		when(NewsReaderFactory.getReader(anyString())).thenReturn(newsReader);
	}

	@Test
	public void testEmptyListOfNews() {
		IncomingNews news = new IncomingNews();
		doReturn(news).when(newsReader).read();
		when(PublishableNews.create()).thenReturn(mock(PublishableNews.class));

		PublishableNews publishableNews = new NewsLoader().loadNews();

		Mockito.verify(publishableNews, Mockito.times(0)).addForSubscription(anyString(), any(SubsciptionType.class));
		Mockito.verify(publishableNews, Mockito.times(0)).addPublicInfo(anyString());
	}

	@Test
	public void testSubscriptionContent() {
		IncomingNews news = new IncomingNews();
		news.add(new IncomingInfo("sub", SubsciptionType.C));
		doReturn(news).when(newsReader).read();
		when(PublishableNews.create()).thenReturn(mock(PublishableNews.class));

		PublishableNews publishableNews = new NewsLoader().loadNews();

		Mockito.verify(publishableNews, Mockito.times(1)).addForSubscription(eq("sub"), eq(SubsciptionType.C));
		Mockito.verify(publishableNews, Mockito.times(0)).addPublicInfo(anyString());
	}
}
