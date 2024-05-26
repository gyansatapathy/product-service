package com.gs.product.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LuceneInMemoryExample {

    private final Directory memoryIndex;
    private final Analyzer analyzer;
    private final ReentrantLock writeLock;

    public LuceneInMemoryExample() {
        memoryIndex = new RAMDirectory();
        analyzer = new StandardAnalyzer();
        writeLock = new ReentrantLock();
    }

    public void addDocument(String id, String content) throws IOException {
        writeLock.lock();
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig)) {
                Document document = new Document();
                document.add(new StringField("id", id, Field.Store.YES));
                document.add(new TextField("content", content, Field.Store.YES));
                writer.addDocument(document);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void updateDocument(String id, String newContent) throws IOException {
        writeLock.lock();
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(memoryIndex, indexWriterConfig)) {
                Document document = new Document();
                document.add(new StringField("id", id, Field.Store.YES));
                document.add(new TextField("content", newContent, Field.Store.YES));
                writer.updateDocument(new Term("id", id), document);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public List<String> searchIndex(String searchString) throws Exception {
        List<String> results = new ArrayList<>();
        try (DirectoryReader directoryReader = DirectoryReader.open(memoryIndex)) {
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            QueryParser queryParser = new QueryParser("content", analyzer);
            Query query = queryParser.parse(searchString);
            TopDocs topDocs = indexSearcher.search(query, 10);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("Document ID: " + document.get("id"));
                results.add(document.get("content"));
            }
        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        LuceneInMemoryExample luceneExample = new LuceneInMemoryExample();
        luceneExample.addDocument("1", "Lucene is a powerful search library written in Java.");
        luceneExample.addDocument("2", "Lucene supports full-text indexing and searching.");

        System.out.println("Initial Search Results:");
        luceneExample.searchIndex("Lucene");

        luceneExample.updateDocument("1", "Lucene is an open-source project available from the Apache Software Foundation.");

        System.out.println("\nSearch Results after Update:");
        luceneExample.searchIndex("Lucene");
    }
}
