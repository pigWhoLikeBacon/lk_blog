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
package me.zhengjie.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Article;
import me.zhengjie.service.ArticleService;
import me.zhengjie.service.dto.ArticleQueryCriteria;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author lk
* @date 2020-11-26
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "文章管理")
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('article:list')")
    public void download(HttpServletResponse response, ArticleQueryCriteria criteria) throws IOException {
        articleService.download(articleService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询文章")
    @ApiOperation("查询文章")
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<Object> query(ArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(articleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增文章")
    @ApiOperation("新增文章")
    @PreAuthorize("@el.check('article:add')")
    @CacheEvict(value = "article", allEntries = true)
    public ResponseEntity<Object> create(@Validated @RequestBody Article resources){
        return new ResponseEntity<>(articleService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改文章")
    @ApiOperation("修改文章")
    @PreAuthorize("@el.check('article:edit')")
    @CacheEvict(value = "article", allEntries = true)
    public ResponseEntity<Object> update(@Validated @RequestBody Article resources){
        articleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文章")
    @ApiOperation("删除文章")
    @PreAuthorize("@el.check('article:del')")
    @DeleteMapping
    @CacheEvict(value = "article", allEntries = true)
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        articleService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("查询可展示文章")
    @ApiOperation("查询可展示文章")
    @GetMapping(value = "/show")
    @Cacheable(value = "article", key = "'queryShow' + #criteria + '_' + #pageable")
    public ResponseEntity<Object> queryShow(ArticleQueryCriteria criteria, Pageable pageable){
        criteria.setIsShow(true);
        return new ResponseEntity<>(articleService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("查询归档")
    @ApiOperation("查询归档")
    @GetMapping(value = "/file")
    @Cacheable(value = "article", key = "'queryFile'")
    public ResponseEntity<Object> queryFile(){
        return new ResponseEntity<>(articleService.queryFile(),HttpStatus.OK);
    }
}