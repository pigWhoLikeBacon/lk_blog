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
package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.zhengjie.annotation.Query;

/**
* @website https://el-admin.vip
* @author lk
* @date 2020-11-26
**/
@Data
public class ArticleQueryCriteria{

    @Query
    private Long id;

    @Query
    private Boolean isShow;

    @Query(blurry = "title,introduce,content")
    private String blurry;

    @Query(propName = "id", type = Query.Type.IN, joinName = "tags")
    private Set<Long> tagIds = new HashSet<>();

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}