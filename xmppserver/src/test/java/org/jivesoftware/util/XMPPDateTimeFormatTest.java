/*
 * Copyright (C) 2017-2025 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.util;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class XMPPDateTimeFormatTest {
    private final String TEST_DATE = "2013-01-25T18:07:22.768Z";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final XMPPDateTimeFormat xmppDateTimeFormat = new XMPPDateTimeFormat();
    
    @Test
    public void failTest() {
        Date parsedDate = null;
        try {
            parsedDate = xmppDateTimeFormat.parseString(TEST_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getTimeZone("utc"));
        String date = df.format(parsedDate);
        assertEquals(date, "2013-01-25T18:07:22.768+0000");
    }

    @Test
    public void testNull() throws Exception
    {
        // Setup fixture
        final String testValue = null;

        // Execute system under test
        final Date result = xmppDateTimeFormat.parseString(testValue);

        // Verify results
        assertNull(result);
    }

    @Test
    public void testEmpty() throws Exception
    {
        // Setup fixture
        final String testValue = "";

        // Execute system under test
        final Date result = xmppDateTimeFormat.parseString(testValue);

        // Verify results
        assertNull(result);
    }

    @Test
    public void testExceptionContainsOffendingValue() throws Exception
    {
        // Setup fixture
        final String testValue = "This is not a valid date value";

        // Execute system under test
        try {
            xmppDateTimeFormat.parseString(testValue);

            // Verify results
            fail("An exception should have been thrown (but was not).");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains(testValue));
        }
    }

    @Test
    public void testFormatNoSecondFractions() throws Exception
    {
        // Setup fixture
        final String testValue = "2015-03-19T22:54:15+00:00"; // Thu, 19 Mar 2015 22:54:15 GMT

        // Execute system under test
        final Date result = xmppDateTimeFormat.parseString(testValue);

        // Verify results
        long expected = 1426805655000L; // Epoch value of Thu, 19 Mar 2015 22:54:15 GMT
        assertEquals( expected, result.getTime() );
    }

    @Test
    public void testFormatThreeSecondFractions() throws Exception
    {
        // Setup fixture
        final String testValue = "2015-03-19T22:54:15.841+00:00"; // Thu, 19 Mar 2015 22:54:15.841 GMT

        // Execute system under test
        final Date result = xmppDateTimeFormat.parseString(testValue);

        // Verify results
        long expected = 1426805655841L; // Epoch value of Thu, 19 Mar 2015 22:54:15.841 GMT
        assertEquals( expected, result.getTime() );
    }

    @Test
    public void testFormatManySecondFractions() throws Exception
    {
        // Setup fixture
        final String testValue = "2015-03-19T22:54:15.841473+00:00"; // Thu, 19 Mar 2015 22:54:15.841473 GMT

        // Execute system under test
        final Date result = xmppDateTimeFormat.parseString(testValue);

        // Verify results
        long expected = 1426805655841L; // Epoch value of Thu, 19 Mar 2015 22:54:15:841 GMT
        assertEquals( expected, result.getTime() );
    }

    @Test
    public void testFormatDate() throws Exception
    {
        // Setup fixture
        final Date testValue = new Date(1426805655841L); // Epoch value of Thu, 19 Mar 2015 22:54:15:841 GMT

        // Execute system under test
        final String result = XMPPDateTimeFormat.format(testValue);

        // Verify results
        assertEquals("2015-03-19T22:54:15.841Z", result);
    }

    @Test
    public void testFormatDateNull() throws Exception
    {
        // Setup fixture
        final Date testValue = null;

        // Execute system under test & verify results
        assertThrows(NullPointerException.class, () -> XMPPDateTimeFormat.format(testValue));
    }

    @Test
    public void testFormatInstant() throws Exception
    {
        // Setup fixture
        final Instant testValue = Instant.ofEpochMilli(1426805655841L); // Epoch value of Thu, 19 Mar 2015 22:54:15:841 GMT

        // Execute system under test
        final String result = XMPPDateTimeFormat.format(testValue);

        // Verify results
        assertEquals("2015-03-19T22:54:15.841Z", result);
    }

    @Test
    public void testFormatInstantNull() throws Exception
    {
        // Setup fixture
        final Instant testValue = null;

        // Execute system under test & verify results
        assertThrows(NullPointerException.class, () -> XMPPDateTimeFormat.format(testValue));
    }
}
