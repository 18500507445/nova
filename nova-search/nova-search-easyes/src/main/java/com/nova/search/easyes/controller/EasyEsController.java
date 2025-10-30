package com.nova.search.easyes.controller;

import com.nova.common.utils.random.RandomUtils;
import com.nova.search.easyes.repository.Document;
import com.nova.search.easyes.repository.DocumentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
        List<Document> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            // 初始化-> 新增数据
            Document document = new Document();
            document.setId(i + "");
            document.setScore(RandomUtils.randomInt(1, 10));
            document.setTitle(RandomUtils.randomName());
            document.setContent(RandomUtils.randomAddress());
            list.add(document);
        }
        return documentMapper.insertBatch(list);
    }

    @GetMapping("/search")
    public List<Document> search() {
        // 查询出所有标题为老汉的文档列表
        LambdaEsQueryWrapper<Document> query = new LambdaEsQueryWrapper<>();
        query.between(Document::getScore, 1, 5);
        String source = documentMapper.getSource(query);
        log.info("source: {}", source);
        return documentMapper.selectList(query);
    }

}
