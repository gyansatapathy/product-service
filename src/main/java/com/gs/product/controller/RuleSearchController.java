package com.gs.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rules")
public class RuleSearchController {

    private final Directory memoryIndex;
    private final Analyzer analyzer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuleSearchController() {
        memoryIndex = new RAMDirectory();
        analyzer = new StandardAnalyzer();
    }

    @PostMapping("/add")
    public String addRule(@RequestBody String jsonString) throws IOException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        Document document = new Document();

        // Store the entire JSON string
        document.add(new StoredField("json", jsonString));

        // Index individual fields
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.get(key).getAsString();
            document.add(new TextField(key, value, Field.Store.YES));
        }

        try (IndexWriter writer = new IndexWriter(memoryIndex, new IndexWriterConfig(analyzer))) {
            writer.addDocument(new Term(jsonObject.get("id").getAsString()),document);
        }

        return "Rule added successfully!";
    }

    @PostMapping("/update")
    public String updateRule(@RequestBody String jsonString) throws IOException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        Document document = new Document();

        // Store the entire JSON string
        document.add(new StoredField("json", jsonString));

        // Index individual fields
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.get(key).getAsString();
            document.add(new TextField(key, value, Field.Store.YES));
        }

        try (IndexWriter writer = new IndexWriter(memoryIndex, new IndexWriterConfig(analyzer))) {
            writer.updateDocument(new Term("id", id), document);
        }

        return "Rule updated successfully!";
    }

    @GetMapping("/search")
    public List<Object> searchRules(@RequestParam String queryStr, @RequestParam String[] fields) throws Exception {
        List<JsonObject> results = new ArrayList<>();

        try (DirectoryReader reader = DirectoryReader.open(memoryIndex)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
            Query query = queryParser.parse(queryStr);

            TopDocs topDocs = searcher.search(query, 10);

            // Highlighter setup
            QueryScorer queryScorer = new QueryScorer(query);
            Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
            Highlighter highlighter = new Highlighter(formatter, queryScorer);
            Fragmenter fragmenter = new SimpleFragmenter(100);
            highlighter.setTextFragmenter(fragmenter);

            // Highlighting search results
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);

                // Retrieve the stored JSON
                String json = doc.get("json");
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                JsonObject highlightedObject = new JsonObject();

                for (String field : fields) {
                    String text = doc.get(field);
                    if (text != null) {
                        String[] fragments = highlighter.getBestFragments(analyzer, field, text, 2);
                        if(fragments.length > 0) {
                            highlightedObject.addProperty(field, String.join("...", fragments));
                        } else {
                            highlightedObject.addProperty(field, jsonObject.get(field).getAsString());
                        }
                    }
                }

                results.add(highlightedObject);
            }
        }
       return results.stream().map(value -> {
            try {
                return objectMapper.readValue(value.toString(), Object.class);
            } catch (JsonProcessingException e) {
                System.err.println(e);
                return null;
            }
        }).collect(Collectors.toList());
    }
}
