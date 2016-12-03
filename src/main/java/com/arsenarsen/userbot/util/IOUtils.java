package com.arsenarsen.userbot.util;

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
        if(!url.contains("&"))
            return ret;
        url = url.substring(0, url.indexOf('?'));
        for(String s : url.split("&")){
            String[] params = s.split("=", 2);
            ret.put(params[0], URLDecoder.decode(params[1], "UTF-8"));
        }
        return ret;
    }

    public static String getIcon(Document doc) throws URISyntaxException {
        String meta;
        try {
            meta = doc.head().select("link[href~=.*\\.ico]").first().attr("abs:href");
        } catch (NullPointerException ignored){
            String uri = new URI(doc.location()).getHost();
            return uri.endsWith("/") ? uri + "favicon.ico" : uri + "/favicon.ico";
        }
        return meta;
    }
}
