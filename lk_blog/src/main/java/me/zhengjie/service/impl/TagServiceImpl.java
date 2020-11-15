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

import me.zhengjie.domain.Tag;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.TagRepository;
import me.zhengjie.service.TagService;
import me.zhengjie.service.dto.TagDto;
import me.zhengjie.service.dto.TagQueryCriteria;
import me.zhengjie.service.mapstruct.TagMapper;
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
* @author LK
* @date 2020-11-15
**/
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Map<String,Object> queryAll(TagQueryCriteria criteria, Pageable pageable){
        Page<Tag> page = tagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(tagMapper::toDto));
    }

    @Override
    public List<TagDto> queryAll(TagQueryCriteria criteria){
        return tagMapper.toDto(tagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TagDto findById(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseGet(Tag::new);
        ValidationUtil.isNull(tag.getTagId(),"Tag","tagId",tagId);
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TagDto create(Tag resources) {
        return tagMapper.toDto(tagRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Tag resources) {
        Tag tag = tagRepository.findById(resources.getTagId()).orElseGet(Tag::new);
        ValidationUtil.isNull( tag.getTagId(),"Tag","id",resources.getTagId());
        tag.copy(resources);
        tagRepository.save(tag);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long tagId : ids) {
            tagRepository.deleteById(tagId);
        }
    }

    @Override
    public void download(List<TagDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TagDto tag : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("内容", tag.getContent());
            map.put("color", tag.getColor());
            map.put("创建日期", tag.getCreateTime());
            map.put("更新时间", tag.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}