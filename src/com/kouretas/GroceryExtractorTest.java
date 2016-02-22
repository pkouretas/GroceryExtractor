package com.kouretas;

import org.junit.Test;
import static org.junit.Assert.*;


public class GroceryExtractorTest {
    private static final String PRODUCTS_URL = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";
    private static final String EXPECTED_RESULT = "\n{\n" +
            "    \"results\":[\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Apricot Ripe & Ready x5\",\n" +
            "            \"size\":\"38.3kb\",\n" +
            "            \"unit_price\":3.5,\n" +
            "            \"description\":\"Apricots\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Avocado Ripe & Ready XL Loose 300g\",\n" +
            "            \"size\":\"38.7kb\",\n" +
            "            \"unit_price\":1.5,\n" +
            "            \"description\":\"Avocados\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Avocado, Ripe & Ready x2\",\n" +
            "            \"size\":\"43.4kb\",\n" +
            "            \"unit_price\":1.8,\n" +
            "            \"description\":\"Avocados\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Avocados, Ripe & Ready x4\",\n" +
            "            \"size\":\"38.7kb\",\n" +
            "            \"unit_price\":3.2,\n" +
            "            \"description\":\"Avocados\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Conference Pears, Ripe & Ready x4 (minimum)\",\n" +
            "            \"size\":\"38.5kb\",\n" +
            "            \"unit_price\":1.5,\n" +
            "            \"description\":\"Conference\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Golden Kiwi x4\",\n" +
            "            \"size\":\"38.6kb\",\n" +
            "            \"unit_price\":1.8,\n" +
            "            \"description\":\"Gold Kiwi\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"title\":\"Sainsbury's Kiwi Fruit, Ripe & Ready x4\",\n" +
            "            \"size\":\"39.0kb\",\n" +
            "            \"unit_price\":1.8,\n" +
            "            \"description\":\"Kiwi\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"total\":15.100000000000001\n" +
            "}" +
            "";
    @Test
    public void crawlGrocery(){
        try {
            String res = GroceryExtractor.crawlGroceryPage(PRODUCTS_URL);
            assertEquals(EXPECTED_RESULT, res);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}