package com.gs.product.search;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class LuceneJsonExample {

    private final Directory memoryIndex;
    private final Analyzer analyzer;

    public LuceneJsonExample() {
        memoryIndex = new RAMDirectory();
        analyzer = new StandardAnalyzer();
    }

    public void addJsonDocument(String jsonString) throws IOException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        Document document = new Document();

        for (String key : jsonObject.keySet()) {
            String value = jsonObject.get(key).getAsString();
            document.add(new TextField(key, value, Field.Store.YES));
        }

        try (IndexWriter writer = new IndexWriter(memoryIndex, new IndexWriterConfig(analyzer))) {
            writer.addDocument(document);
        }
    }

    public void updateJsonDocument(String jsonString) throws IOException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        String id = jsonObject.get("id").getAsString();

        Document document = new Document();
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.get(key).getAsString();
            document.add(new TextField(key, value, Field.Store.YES));
        }

        try (IndexWriter writer = new IndexWriter(memoryIndex, new IndexWriterConfig(analyzer))) {
            writer.updateDocument(new Term("id", id), document);
        }
    }

    public void searchIndex(String searchString, String[] fieldsToSearch) throws Exception {
        try (DirectoryReader reader = DirectoryReader.open(memoryIndex)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser queryParser = new MultiFieldQueryParser(fieldsToSearch, analyzer);
            Query query = queryParser.parse(searchString);

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
                System.out.println("Document ID: " + doc.get("id"));

                for (String field : fieldsToSearch) {
                    String text = doc.get(field);
                    if (text != null) {
                        String[] fragments = highlighter.getBestFragments(analyzer, field, text, 2);
                        System.out.println(field + " Highlighted: " + String.join("...", fragments));
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        LuceneJsonExample luceneExample = new LuceneJsonExample();

        // Example JSON documents
        String json1 = "{\"id\": \"1\", \"title\": \"Introduction to Lucene\", \"content\": \"Lucene is a powerful search library written in Java.\"}";
        String json2 = "{\"id\": \"2\", \"title\": \"Getting Started with Lucene\", \"content\": \"Lucene supports full-text indexing and searching.\"}";

        // Add JSON documents to index
        luceneExample.addJsonDocument(json1);
        luceneExample.addJsonDocument(json2);

        // Define fields to search
        String[] fields = {"title", "content"};

        // Search and highlight
        System.out.println("Initial Search Results:");
        luceneExample.searchIndex("Lucene", fields);

        // Update document
        String updatedJson1 = "{\"id\": \"1\", \"title\": \"Introduction to Lucene\", \"content\": \"Lucene is a powerful and flexible search library written in Java.\"}";
        luceneExample.updateJsonDocument(updatedJson1);

        // Search and highlight after update
        System.out.println("\nSearch Results after Update:");
        luceneExample.searchIndex("Lucene", fields);
    }
}

