package com.kouretas;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroceryExtractor {

    private static final String PRODUCTS_URL = "http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html";

    private static final String PRODUCT_LIST_SELECTOR = "ul[class=productLister listView]> li > div[class=product ]";
    private static final String PATH_TO_A = "div.productInner > div.productInfoWrapper > div.productInfo > h3 > a";
    private static final String PATH_TO_PRICE = "div.productInner > div.addToTrolleytabBox  p.pricePerUnit";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+\\.\\d+|\\.\\d+|\\d+");
    private static final String PATH_TO_DESCRIPTION = "productcontent > htmlcontent > div.productText";// > p:eq(0)";

    public static void main(String[] args) {
        try {
            String results = crawlGroceryPage(PRODUCTS_URL);
            System.out.println(results);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static String crawlGroceryPage(String url) throws Exception{
        String html = getHTML(url);
        Document doc = Jsoup.parse(html);
        Elements liProducts = doc.select(PRODUCT_LIST_SELECTOR);
        JsonArrayBuilder builder = Json.createArrayBuilder();
        double totalPrice = 0.0;
        for (Element product : liProducts){

            // Extract TITLE
            Elements a = product.select(PATH_TO_A);
            String title = a.text();

            // Extract PRICE
            double priceDouble = extractPrice(product);
            totalPrice += priceDouble;

            // Extract LINK & Follow link
            String link = a.attr("href");
            String sizeKb = getPageSize(link);

            Document innerDom = Jsoup.parse(getHTML(link));
            Element description = innerDom.select(PATH_TO_DESCRIPTION).first().select("p").first();
            String descriptionText = description.text();

            JsonObject obj = createProductObject(title, sizeKb, priceDouble, descriptionText);
            builder.add(obj);
        }

        JsonObject result = createResultObject(builder.build(), totalPrice);
        return prettifyJson(result);
    }

    private static String prettifyJson(JsonObject jObj){
        StringWriter sw = new StringWriter();

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);

        jsonWriter.writeObject(jObj);
        jsonWriter.close();
        return sw.toString();
    }

    private static JsonObject createResultObject(JsonArray array, double totalPrice) {
        return Json.createObjectBuilder()
                .add("results", array)
                .add("total", totalPrice)
                .build();
    }

    private static double extractPrice(Element product) {
        Elements price = product.select(PATH_TO_PRICE);
        Matcher m = NUMBER_PATTERN.matcher(price.text());
        double priceDouble = 0.0;
        if (m.find()) {
            priceDouble = Double.parseDouble(m.group());
        }
        return priceDouble;
    }

    public static JsonObject createProductObject(String title, String sizeKb, double priceDouble, String description) {
        return Json.createObjectBuilder()
                .add("title", title)
                .add("size", sizeKb)
                .add("unit_price", priceDouble)
                .add("description", description)
                .build();
    }

    public static String getPageSize(String url) throws Exception{
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        int bytes = conn.getContentLength();

        String kilobytes = String.format("%.1f", bytes/1024.0);
        return kilobytes + "kb";
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
