package com.nova.search.easyes.controller;

import com.nova.search.easyes.repository.Document;
import com.nova.search.easyes.repository.DocumentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: wzh
 * @description: Controller
 * @date: 2025/03/28 16:10
 */
@Slf4j(topic = "EasyEsController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/api")
public class EasyEsController {

    private final DocumentMapper documentMapper;

    @GetMapping("/createIndex")
    public Boolean createIndex() {
        // 初始化-> 创建索引
        return documentMapper.createIndex();
    }

    @GetMapping("/insert")
    public Integer insert() {
        // 初始化-> 新增数据
        Document document = new Document();
        document.setId("1");
        document.setTitle("老汉");
        document.setContent("推*技术过硬");
        return documentMapper.insert(document);
    }

    @GetMapping("/search")
    public List<Document> search() {
        // 查询出所有标题为老汉的文档列表
        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(Document::getTitle, "老汉")
                .and(i -> i.eq(Document::getId, 1).eq(Document::getContent, "推车"))
                .or()
                .and(i -> i.eq(Document::getId, 2).eq(Document::getContent, "推土"));
        String source = documentMapper.getSource(wrapper);
        log.info("source: {}", source);
        return documentMapper.selectList(wrapper);
    }
}
