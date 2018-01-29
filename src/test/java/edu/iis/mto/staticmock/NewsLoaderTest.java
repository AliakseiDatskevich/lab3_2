package edu.iis.mto.staticmock;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class })

public class NewsLoaderTest {

	private NewsLoader newsLoader;
	private NewsReader mockedNewsReader;
	private Configuration configuration;
	private ConfigurationLoader mockedConfigurationLoader;
	private IncomingNews incomingNews;

	@Before
	public void setUp() {
		newsLoader = new NewsLoader();
		configuration = new Configuration();
		mockStatic(ConfigurationLoader.class);
		mockStatic(NewsReaderFactory.class);
		mockedConfigurationLoader = mock(ConfigurationLoader.class);
		incomingNews = new IncomingNews();
		mockedNewsReader = mock(NewsReader.class);
		String readerTypeValue = "readerType";
		Whitebox.setInternalState(configuration, "readerType", readerTypeValue);
		when(ConfigurationLoader.getInstance()).thenReturn(mockedConfigurationLoader);
		when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);
		when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(mockedNewsReader);
		when(mockedNewsReader.read()).thenReturn(incomingNews);
	}

	@Test
	public void readMethodShouldBeCalledOnceWhenNewsIsLoaded() {
		newsLoader.loadNews();
		verify(mockedNewsReader, times(1)).read();
	}

	@Test
	public void newsForSubscriptionTypeAIsAddedCorrectly() {
		PublishableNews publishableNews = PublishableNews.create();
		publishableNews.addForSubscription("Subscription Type A", SubsciptionType.A);
		List<String> newsList = Whitebox.getInternalState(publishableNews, "subscribentContent");
		assertThat(newsList.size(), Matchers.is(1));
	}

}
