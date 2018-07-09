package de.dani09.http;

import org.junit.Test;

/**
 * Created by dani909 on 09.07.18.
 */
public class JavaTest {
    @Test
    public void responseCodeValid() {
        int s = Http.get("http://httpbin.org/ip")
                .execute()
                .getResponseCode();

        System.out.println(s);
        assert (s == 200);
    }
}
