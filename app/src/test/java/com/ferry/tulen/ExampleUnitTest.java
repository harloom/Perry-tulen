package com.ferry.tulen;

import org.junit.Test;

import static org.junit.Assert.*;

import com.ferry.tulen.datasources.alogritma.SlopeOneWorkMan;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 * @noinspection ALL
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public  void testSlopeOne(){
//        SlopeOneWorkMan s = new SlopeOneWorkMan();
//        s.update(Collections.singletonMap("alice", Collections.singletonMap("squid", 1.0)));
//        s.update(Collections.singletonMap("bob", Collections.singletonMap("squid", 1.0)));
//        s.update(Collections.singletonMap("alice", Collections.singletonMap("cuttlefish", 4.0)));
//        s.update(Collections.singletonMap("bob", Collections.singletonMap("octopus", 3.0)));
//
//        Map<String, Double> predictions = s.predict(Collections.singletonMap("alice", Collections.singletonMap("squid", 1.0)));
//        System.out.println(predictions);

        SlopeOneWorkMan slopeOneWorkMan = new SlopeOneWorkMan();

        // Create a sample dataset
        Map<String, Map<String, Double>> data = new HashMap<>();
        data.put("user1", new HashMap<>());
        data.get("user1").put("item1", 5.0);
        data.get("user1").put("item2", 3.0);
        data.get("user1").put("item3", 4.0);



        data.put("user2", new HashMap<>());
        data.get("user2").put("item1", 4.0);
        data.get("user2").put("item2", 5.0);
        data.get("user2").put("item3", 3.0);

        data.put("user3", new HashMap<>());
        data.get("user3").put("item1", 3.0);
        data.get("user3").put("item2", 4.0);
        data.get("user3").put("item3", 5.0);

        // Update the SlopeOneWorkMan with the dataset
        slopeOneWorkMan.update(data);

        // Create a sample user preferences
        Map<String, Double> userprefs = new HashMap<>();
        userprefs.put("item1", 4.0);
        userprefs.put("item3", 5.0);

        // Predict ratings for the user
        Map<String, Double> preds = slopeOneWorkMan.predict(userprefs);

        // Print the predicted ratings
        for (Map.Entry<String, Double> entry : preds.entrySet()) {
            System.out.println("Predicted rating for item " + entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test
    public  void testSlopeOne2(){
        Map<String, Map<String, Double>> ratings = new HashMap<>();

// Add some ratings data
        ratings.put("user1", Map.of("item1", 5.0, "item2", 3.0, "item3", 4.0));
        ratings.put("user2", Map.of("item1", 4.0, "item2", 2.0, "item3", 3.0));

// Create a SlopeOneWorkMan object
        SlopeOneWorkMan s1 = new SlopeOneWorkMan();

// Update the SlopeOneWorkMan object with the ratings data
        s1.update(ratings);

// Predict the ratings for a user
        Map<String, Double> userprefs = Map.of("item1", 4.0, "item2", 2.0);
        Map<String, Double> preds = s1.predict(userprefs);

// Print the predicted ratings
        for (Map.Entry<String, Double> entry : preds.entrySet()) {
            System.out.println("test 2 Predicted rating for " + entry.getKey() + ": " + entry.getValue());
        }
    }
}