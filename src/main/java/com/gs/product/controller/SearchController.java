package com.gs.product.controller;

import com.gs.product.search.IndexDocument;
import com.gs.product.search.LuceneInMemoryExample;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SearchController {

    private final LuceneInMemoryExample luceneInMemoryExample = new LuceneInMemoryExample();

    @PutMapping("/index")
    public void getAllProducts(@RequestBody IndexDocument indexDocument) throws IOException {
        luceneInMemoryExample.updateDocument(indexDocument.getId(), indexDocument.getContent());
    }
    @GetMapping("/search")
    public List<String> search(@RequestParam("query") String query) throws Exception {
        return luceneInMemoryExample.searchIndex(query);
    }
}
