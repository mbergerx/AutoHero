package WebTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


    public class AutohearTester {

        public static void main(String[] args)  throws Exception{
            Document doc = Jsoup
                    .connect("https://www.autohero.com/de/search/?sort=PRICE_DESC&yearMin=2015").get();

            Elements results = doc.select("div.item___T1IPF");

            if(!checkMinYear2015Filter(results)){
                System.err.println("Some results are before 2015");
            }
            if(!checkDescPriceSort(results)){
                System.err.println("Sort is not correct");
            }

        }

        private static boolean checkDescPriceSort(Elements results) {
            if(results == null || results.isEmpty()) {
                return true;
            }

            Integer lastPrice = null;
            for(Element result:results){
                String text = result.select("div.totalPrice___3yfNv").text();
                String priceClean = text.replaceAll("[^0-9]", "");
                int price = Integer.parseInt(priceClean);
                if(lastPrice == null){
                    lastPrice = price;
                }else{
                    if(lastPrice >= price){
                        lastPrice = price;
                    }else{
                        return false;
                    }
                }
            }
            return true;

        }

        private static boolean checkMinYear2015Filter(Elements results) {
            if(results == null || results.isEmpty()){
                return true;
            }
            for(Element result:results){
                String text = result.select("div > a > ul > li").get(0).text();
                String[] parts = text.split("/");
                String yearStr = parts[parts.length - 1];
                if(Integer.parseInt(yearStr) < 2015){
                    return false;
                }
            }
            return true;
        }
    }

