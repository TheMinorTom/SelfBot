package com.arsenarsen.userbot.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class IOUtils {
    public static void delete(File toRecurse) throws IOException {
        Files.walk(toRecurse.toPath(), FileVisitOption.FOLLOW_LINKS)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static Map<String, String> parse(String url) throws UnsupportedEncodingException {
        HashMap<String, String> ret = new HashMap<>();
        if(!url.contains("?"))
            return ret;
        url = url.substring(url.indexOf('?') + 1);
        String[] params = url.split("&");
        for(String s : params){
            String[] pair = s.split("=", 2);
            ret.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
        }
        return ret;
    }

    public static String getIcon(String url) throws URISyntaxException, IOException {
        String meta;
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 " + System.currentTimeMillis()).get();
            meta = doc.head().select("link[href~=.*\\.ico]").first().attr("abs:href");
        } catch (NullPointerException ignored){
            String uri = new URI(url).getHost();
            return uri.endsWith("/") ? uri + "favicon.ico" : uri + "/favicon.ico";
        }
        return meta;
    }
}
