package elisa.generator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

/**
 * @author Adam Goscicki
 */
public class AnalyzerTest {
    private Analyzer analyzer = new Analyzer();

    @Before
    public void testParse() throws Exception {
        File file = new File(getClass().getResource("/testfile").toURI());
        analyzer.parse(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWord() throws Exception {
        Map.Entry<String, Integer> map = analyzer.getWord("ala");
        Assert.assertEquals("ala", map.getKey());
        Assert.assertTrue(map.getValue() == 1);

        map = analyzer.getWord("ma");
        Assert.assertEquals("ma", map.getKey());
        Assert.assertTrue(map.getValue() == 2);

        analyzer.getWord("blabla");
    }

    @Test
    public void testGetUniqueWordCount() throws Exception {
        Assert.assertTrue(analyzer.getUniqueWordCount() == 19);
    }

    @Test
    public void testIsEmpty() throws Exception {
        Assert.assertFalse(analyzer.isEmpty());

        Analyzer empty = new Analyzer();
        Assert.assertTrue(empty.isEmpty());
    }
}