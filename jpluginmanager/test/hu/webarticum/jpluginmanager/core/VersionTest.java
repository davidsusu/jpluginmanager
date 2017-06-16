package hu.webarticum.jpluginmanager.core;

import static org.junit.Assert.*;

import org.junit.Test;


public class VersionTest {

    @Test
    public void testParsing() {
        {
            Version version = new Version("2");
            assertEquals(2, version.getMajor());
            assertEquals(0, version.getMinor());
            assertEquals(0, version.getPatch());
            assertEquals("", version.getPre());
            assertEquals("", version.getBuild());
        }
        
        {
            Version version = new Version("1.4");
            assertEquals(1, version.getMajor());
            assertEquals(4, version.getMinor());
            assertEquals(0, version.getPatch());
            assertEquals("", version.getPre());
            assertEquals("", version.getBuild());
        }
        
        {
            Version version = new Version("2.1.3");
            assertEquals(2, version.getMajor());
            assertEquals(1, version.getMinor());
            assertEquals(3, version.getPatch());
            assertEquals("", version.getPre());
            assertEquals("", version.getBuild());
        }
        
        {
            Version version = new Version("4.3.5-rc1");
            assertEquals(4, version.getMajor());
            assertEquals(3, version.getMinor());
            assertEquals(5, version.getPatch());
            assertEquals("rc1", version.getPre());
            assertEquals("", version.getBuild());
        }
        
        {
            Version version = new Version("4.3.5+build123");
            assertEquals(4, version.getMajor());
            assertEquals(3, version.getMinor());
            assertEquals(5, version.getPatch());
            assertEquals("", version.getPre());
            assertEquals("build123", version.getBuild());
        }
        
        {
            Version version = new Version("4.3.5-rc2+build999");
            assertEquals(4, version.getMajor());
            assertEquals(3, version.getMinor());
            assertEquals(5, version.getPatch());
            assertEquals("rc2", version.getPre());
            assertEquals("build999", version.getBuild());
        }
    }

    @Test
    public void testSingleMatch() {
        {
            Version version = new Version("2.3.3");
            
            assertTrue(version.matches("2.3.3"));
            assertFalse(version.matches("2.3.2"));
            assertFalse(version.matches("4.2.6"));

            assertTrue(version.matches(">1.6.0"));
            assertFalse(version.matches(">=5.7.1"));
            assertTrue(version.matches("<3.2.1"));
            assertFalse(version.matches("<=1.0.1"));

            assertFalse(version.matches("2.3.3-*"));
        }
        
        {
            Version version = new Version("2.3.3-rc-1");

            assertTrue(version.matches(">1.6.0"));
            
            assertFalse(version.matches("1.6.*"));
            assertTrue(version.matches("2.*"));
            assertFalse(version.matches("2.1.*"));
            assertTrue(version.matches("2.3.*"));

            assertTrue(version.matches("2.3.3-*"));
            assertFalse(version.matches("2.3.3-x-*"));
            assertTrue(version.matches("2.3.3-rc-*"));
            assertTrue(version.matches("2.3.*-r*1"));

            assertFalse(version.matches("2.3.3-rc-1+*"));
        }
    }

    @Test
    public void testComplexMatch() {
        {
            Version version = new Version("2.3.3");

            assertTrue(version.matches(">2.3.0 <3.0.1"));
            assertFalse(version.matches(">=2.3.0 <=2.3.1"));
        }
    }

    @Test
    public void testMoreComplexMatch() {
        {
            Version version = new Version("2.3.3");

            assertTrue(version.matches(">2.3.0 <3.0.1 || >=2.3.0 <=2.3.1"));
            assertFalse(version.matches(">=2.3.0 <=2.3.1 || =4.5.6"));
        }
    }

}
