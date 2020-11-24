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
* @author lk
* @date 2020-11-24
**/
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository TagRepository;
    private final TagMapper TagMapper;

    @Override
    public Map<String,Object> queryAll(TagQueryCriteria criteria, Pageable pageable){
        Page<Tag> page = TagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(TagMapper::toDto));
    }

    @Override
    public List<TagDto> queryAll(TagQueryCriteria criteria){
        return TagMapper.toDto(TagRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TagDto findById(Long id) {
        Tag Tag = TagRepository.findById(id).orElseGet(Tag::new);
        ValidationUtil.isNull(Tag.getId(),"Tag","id",id);
        return TagMapper.toDto(Tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TagDto create(Tag resources) {
        return TagMapper.toDto(TagRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Tag resources) {
        Tag Tag = TagRepository.findById(resources.getId()).orElseGet(Tag::new);
        ValidationUtil.isNull( Tag.getId(),"Tag","id",resources.getId());
        Tag.copy(resources);
        TagRepository.save(Tag);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            TagRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TagDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TagDto Tag : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("内容", Tag.getContent());
            map.put("color", Tag.getColor());
            map.put("创建日期", Tag.getCreateTime());
            map.put("更新时间", Tag.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}