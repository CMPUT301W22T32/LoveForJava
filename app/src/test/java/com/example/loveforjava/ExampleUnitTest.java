package com.example.loveforjava;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    /*@Test
    public void createPlayerTest() {
        APIMain APIServer = new APIMain();
        APIServer.createPlayer("jasper", "jleng1@ualberta.ca", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> res) {
                assertEquals(true, res.get("success"));
                String id  =(String) res.get("user_id");
                APIServer.getPlayerInfo(id, new ResponseCallback() {
                    @Override
                    public void onResponse(Map<String, Object> response) {
                        assertEquals(true, response.get("success"));
                    }
                });
            }
        });
    }*/

    /*@Test
    public void getPlayerInfoTest() {
        APIMain APIServer = new APIMain();
        //Map<String, Object>  res = APIServer.createPlayer("jasper", "jleng1@ualberta.ca");
        //assertEquals(true, res.get("success"));
        //String id  =(String) res.get("user_id");
        Log.i("test", "here");
        APIServer.getPlayerInfo("3ou2WaY8MwpJbeesN0UO", new ResponseCallback() {
            @Override
            public void onResponse(Map<String, Object> response) {
                Log.i("test", response+"");
                assertEquals(true, response.get("success"));
            }
        });

    }*/
}