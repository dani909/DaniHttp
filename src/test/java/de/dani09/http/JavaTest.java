package de.dani09.http;

import org.junit.Test;

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
