package com.ferry.tulen.datasources.alogritma;

import java.util.*;

public class SlopeOneWorkMan {

    Map<String, Map<String, Double>> diffs = new HashMap<>();
    Map<String, Map<String, Integer>> freqs = new HashMap<>();


//    public void update(Map<String, Map<String, Double>> data) {
//        for (Map.Entry<String, Map<String, Double>> userEntry : data.entrySet()) {
//            String user = userEntry.getKey();
//            Map<String, Double> userRatings = userEntry.getValue();
//
//            for (Map.Entry<String, Double> itemEntry1 : userRatings.entrySet()) {
//                String item1 = itemEntry1.getKey();
//                Double rating1 = itemEntry1.getValue();
//
//                for (Map.Entry<String, Double> itemEntry2 : userRatings.entrySet()) {
//                    if (itemEntry1 != itemEntry2) {
//                        String item2 = itemEntry2.getKey();
//                        Double rating2 = itemEntry2.getValue();
//
//                        Double diff = rating1 - rating2;
//
//                        diffs.computeIfAbsent(item1, k -> new HashMap<>()).put(item2, diffs.getOrDefault(item1, Collections.emptyMap()).getOrDefault(item2, 0.0) + diff);
//                        freqs.computeIfAbsent(item1, k -> new HashMap<>()).put(item2, freqs.getOrDefault(item1, Collections.emptyMap()).getOrDefault(item2, 0) + 1);
//                    }
//                }
//            }
//        }
//    }
// Update the diffs and freqs maps with the new data
public void update(Map<String, Map<String, Double>> data) {
    // Iterate over each user in the dataset
    for (Map.Entry<String, Map<String, Double>> userEntry : data.entrySet()) {
        String user = userEntry.getKey();
        Map<String, Double> userRatings = userEntry.getValue();

        // Iterate over each pair of items rated by the user
        for (Map.Entry<String, Double> itemEntry1 : userRatings.entrySet()) {
            String item1 = itemEntry1.getKey();
            Double rating1 = itemEntry1.getValue();

            for (Map.Entry<String, Double> itemEntry2 : userRatings.entrySet()) {
                if (itemEntry1 != itemEntry2) {
                    String item2 = itemEntry2.getKey();
                    Double rating2 = itemEntry2.getValue();

                    // Calculate the difference in ratings between the two items
                    Double diff = rating1 - rating2;

                    // Update the diffs and freqs maps with the new difference
                    diffs.computeIfAbsent(item1, k -> new HashMap<>()).put(item2, diffs.getOrDefault(item1, Collections.emptyMap()).getOrDefault(item2, 0.0) + diff);
                    freqs.computeIfAbsent(item1, k -> new HashMap<>()).put(item2, freqs.getOrDefault(item1, Collections.emptyMap()).getOrDefault(item2, 0) + 1);
                }
            }
        }
    }
}
//
//    public Map<String, Double> predict(Map<String, Double> userprefs) {
//        Map<String, Double> preds = new HashMap<>();
//
//        for (Map.Entry<String, Double> entry : userprefs.entrySet()) {
//            String item = entry.getKey();
//            Double rating = entry.getValue();
//
//            for (Map.Entry<String, Double> diffEntry : diffs.getOrDefault(item, Collections.emptyMap()).entrySet()) {
//                String diffItem = diffEntry.getKey();
//                Double diff = diffEntry.getValue();
//                Integer freq = freqs.getOrDefault(item, Collections.emptyMap()).getOrDefault(diffItem, 0);
//
//                preds.put(diffItem, preds.getOrDefault(diffItem, 0.0) + (diff + rating) * freq);
//            }
//        }
//
//        for (Map.Entry<String, Double> entry : preds.entrySet()) {
//            String item = entry.getKey();
//            Double sum = entry.getValue();
//
//            Integer freq = 0;
//            for (Map<String, Integer> freqMap : freqs.values()) {
//                freq += freqMap.getOrDefault(item, 0);
//            }
//
//            if (freq > 0) {
//                entry.setValue(sum / freq);
//            } else {
//                entry.setValue(0.0);
//            }
//        }
//
//        return preds;
//    }

    // Predict the ratings for the given user preferences
    public Map<String, Double> predict(Map<String, Double> userprefs) {
        Map<String, Double> preds = new HashMap<>();

        // Iterate over each item in the user preferences
        for (Map.Entry<String, Double> entry : userprefs.entrySet()) {
            String item = entry.getKey();
            Double rating = entry.getValue();

            // Iterate over each pair of items that the current item is different from
            for (Map.Entry<String, Double> diffEntry : diffs.getOrDefault(item, Collections.emptyMap()).entrySet()) {
                String diffItem = diffEntry.getKey();
                Double diff = diffEntry.getValue();
                Integer freq = freqs.getOrDefault(item, Collections.emptyMap()).getOrDefault(diffItem, 0);

                // Calculate the predicted rating for the difference item
                preds.put(diffItem, preds.getOrDefault(diffItem, 0.0) + (diff + rating) * freq);
            }
        }

        // Calculate the average predicted rating for each item
        for (Map.Entry<String, Double> entry : preds.entrySet()) {
            String item = entry.getKey();
            Double sum = entry.getValue();

            Integer freq = 0;
            for (Map<String, Integer> freqMap : freqs.values()) {
                freq += freqMap.getOrDefault(item, 0);
            }

            entry.setValue(sum / freq);
        }

        return preds;
    }
}
