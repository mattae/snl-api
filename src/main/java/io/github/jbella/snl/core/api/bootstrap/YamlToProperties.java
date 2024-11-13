package io.github.jbella.snl.core.api.bootstrap;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

class YamlToProperties {

    record Pair(String key, String value) {
    }

    static final Yaml yaml = new Yaml();
    TreeMap<String, Map<String, Object>> config;

    @SuppressWarnings({"raw_types", "unchecked"})
    public YamlToProperties(String content) {
        this.config = (TreeMap<String, Map<String, Object>>) yaml.loadAs(content, TreeMap.class);
    }

    @SuppressWarnings({"raw_types", "unchecked"})
    public YamlToProperties(InputStream inputStream) {
        this.config = (TreeMap<String, Map<String, Object>>) yaml.loadAs(inputStream, TreeMap.class);
    }

    @Override
    public String toString() {
        return toProperties(config);
    }

    public Map<String, Object> asProperties() {
        return toPropertyMap(config);
    }

    private static String toProperties(final TreeMap<String, Map<String, Object>> config) {
        StringBuilder sb = new StringBuilder();
        for (final String key : config.keySet()) {
            sb.append(toString(key, config.get(key)));
        }
        return sb.toString();
    }

    private static Map<String, Object> toPropertyMap(final TreeMap<String, Map<String, Object>> config) {
        return config.keySet().stream()
                .map(it -> toString(it, config.get(it)))
                .filter(it -> !it.isEmpty())
                .flatMap(it -> {
                    String[] lines = it.split("\n");
                    return Arrays.stream(lines)
                            .map(line -> {
                                String[] propertyParts = line.split("=");
                                if (propertyParts.length == 2) {
                                    return new Pair(propertyParts[0], propertyParts[1]);
                                }
                                return null;
                            });
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    @SuppressWarnings("unchecked")
    private static String toString(final String key, final Object o) {
        StringBuilder sb = new StringBuilder();
        if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            for (final String mapKey : map.keySet()) {
                if (map.get(mapKey) instanceof Map) {
                    sb.append(toString(String.format("%s.%s", key, mapKey), map.get(mapKey)));
                } else if (map.get(mapKey) instanceof List) {
                    List<String> list = (List<String>) map.get(mapKey);
                    sb.append(String.format("%s.%s=%s%n", key, mapKey, String.join(",", list)));
                } else {
                    sb.append(String.format("%s.%s=%s%n", key, mapKey,
                            (null == map.get(mapKey)) ? null : map.get(mapKey).toString()));
                }
            }

        } else {
            sb.append(String.format("%s=%s%n", key, o));
        }
        return sb.toString();
    }
}
