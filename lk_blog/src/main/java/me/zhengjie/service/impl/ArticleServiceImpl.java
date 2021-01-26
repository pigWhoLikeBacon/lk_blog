/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.service.impl;

import me.zhengjie.domain.Article;
import me.zhengjie.domain.Tag;
import me.zhengjie.repository.TagRepository;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.ArticleRepository;
import me.zhengjie.service.ArticleService;
import me.zhengjie.service.dto.ArticleDto;
import me.zhengjie.service.dto.ArticleQueryCriteria;
import me.zhengjie.service.mapstruct.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author lk
* @date 2020-11-26
**/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public Map<String,Object> queryAll(ArticleQueryCriteria criteria, Pageable pageable){
        Page<Article> page = articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(articleMapper::toDto));
    }

    @Override
    public List<ArticleDto> queryAll(ArticleQueryCriteria criteria){
        return articleMapper.toDto(articleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ArticleDto findById(Long id) {
        Article article = articleRepository.findById(id).orElseGet(Article::new);
        ValidationUtil.isNull(article.getId(),"Article","id",id);
        return articleMapper.toDto(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleDto create(Article resources) {
        return articleMapper.toDto(articleRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Article resources) {
        Article article = articleRepository.findById(resources.getId()).orElseGet(Article::new);
        ValidationUtil.isNull( article.getId(),"Article","id",resources.getId());
        article.copy(resources);
        articleRepository.save(article);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            articleRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ArticleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleDto article : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文章封面", article.getCover());
            map.put("简介", article.getIntroduce());
            map.put("题目", article.getTitle());
            map.put("内容", article.getContent());
            map.put("浏览量", article.getViews());
            map.put("是否展示", article.getIsShow());
            map.put("创建日期", article.getCreateTime());
            map.put("更新时间", article.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}