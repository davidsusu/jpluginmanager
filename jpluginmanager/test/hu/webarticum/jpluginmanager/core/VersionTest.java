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
            Version version = new Version("4.3.5-rc.1");
            assertEquals(4, version.getMajor());
            assertEquals(3, version.getMinor());
            assertEquals(5, version.getPatch());
            assertEquals("rc.1", version.getPre());
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
            Version version = new Version("4.3.5-rc.2+build999");
            assertEquals(4, version.getMajor());
            assertEquals(3, version.getMinor());
            assertEquals(5, version.getPatch());
            assertEquals("rc.2", version.getPre());
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
            
            assertFalse(version.matches("2.3.3+*"));
            assertFalse(version.matches("2.3.3+build1234"));
        }
        
        {
            Version version = new Version("2.3.3-rc.1");

            assertFalse(version.matches("2.3.1"));
            assertFalse(version.matches("2.3.3"));

            assertTrue(version.matches(">1.6.0"));
            
            assertFalse(version.matches("1.6.*"));
            assertFalse(version.matches("2.*"));
            assertFalse(version.matches("2.1.*"));
            assertFalse(version.matches("2.3.*"));

            assertTrue(version.matches("2.3.3-*"));
            assertFalse(version.matches("2.3.3-x-*"));
            assertTrue(version.matches("2.3.3-rc.*"));
            assertTrue(version.matches("2.3.*-r*1"));

            assertFalse(version.matches("2.3.3-rc.1+*"));

            assertFalse(version.matches("2.3.3+*"));
            assertFalse(version.matches("2.3.3+build1234"));
            assertFalse(version.matches("2.3.3-rc.1+*"));
            assertFalse(version.matches("2.3.3-rc.1+build1234"));
        }

        {
            Version version = new Version("2.3.3-rc.1+build1234");
            
            assertFalse(version.matches("2.3.1"));
            assertFalse(version.matches("2.3.3"));

            assertTrue(version.matches(">1.6.0"));
            
            assertFalse(version.matches("1.6.*"));
            assertFalse(version.matches("2.*"));
            assertFalse(version.matches("2.1.*"));
            assertFalse(version.matches("2.3.*"));

            assertTrue(version.matches("2.3.3-*"));
            assertFalse(version.matches("2.3.3-x.*"));
            assertTrue(version.matches("2.3.3-rc.*"));
            assertTrue(version.matches("2.3.*-r*1"));

            assertTrue(version.matches("2.3.3-rc.1+*"));

            assertFalse(version.matches("2.3.3+*"));
            assertFalse(version.matches("2.3.3+build1234"));
            assertTrue(version.matches("2.3.3-rc.1+*"));
            assertFalse(version.matches("2.3.3-rc.1+build0123"));
            assertTrue(version.matches("2.3.3-rc.1+build1234"));
        }

        {
            Version version = new Version("2.3.3+build1234");
            
            assertFalse(version.matches("1.6.0"));
            assertTrue(version.matches("2.3.3"));

            assertTrue(version.matches(">=1.6.0"));
            
            assertFalse(version.matches("2.3.3-rc.*"));
            assertFalse(version.matches("2.3.3-rc.*+*"));

            assertTrue(version.matches("2.3.3+*"));
            assertFalse(version.matches("2.3.3+build0123"));
            assertTrue(version.matches("2.3.3+build1234"));
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
    
    @Test
    public void testComparison() {
        assertTrue(new Version("2.3.4").compareTo(new Version("2.3.4")) == 0);
        assertTrue(new Version("2.3.4").compareTo(new Version("1.4.0")) > 0);
        assertTrue(new Version("0.3.2").compareTo(new Version("1.0.3")) < 0);

        assertTrue(new Version("1.3.3-rc.2").compareTo(new Version("1.4.6-rc.1")) < 0);
        assertTrue(new Version("1.3.3-rc.1").compareTo(new Version("1.3.3")) < 0);
        assertTrue(new Version("1.3.3-rc.1").compareTo(new Version("1.3.3-rc.x")) < 0);
        assertTrue(new Version("1.3.3-rc.1").compareTo(new Version("1.3.3-rc.2")) < 0);
        assertTrue(new Version("1.3.3-aplha.1").compareTo(new Version("1.3.3-rc.1")) < 0);
        assertTrue(new Version("1.3.3-aplha.1").compareTo(new Version("1.3.3-aplha.1")) == 0);

        assertTrue(new Version("1.3.3+build1234").compareTo(new Version("1.3.3")) == 0);
        assertTrue(new Version("1.3.3+build1234").compareTo(new Version("1.3.3+build01234")) == 0);
        assertTrue(new Version("1.3.3-rc.2").compareTo(new Version("1.4.6+build0123")) < 0);
        assertTrue(new Version("1.3.3-aplha.1+build1234").compareTo(new Version("1.3.3-aplha.1")) == 0);
        assertTrue(new Version("1.3.3-aplha.1+build1234").compareTo(new Version("1.3.3-aplha.1+build0123")) == 0);
        assertTrue(new Version("1.3.3-aplha.1+build1234").compareTo(new Version("1.3.3-aplha.1+build1234")) == 0);
        assertTrue(new Version("1.3.3-aplha.1+build1234").compareTo(new Version("1.3.3-aplha.2+build1234")) < 0);
    }

}
