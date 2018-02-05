package staticmock.test;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.PublishableNews;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NewsReaderFactory.class, PublishableNews.class})
public class NewsLoaderTest {

}
