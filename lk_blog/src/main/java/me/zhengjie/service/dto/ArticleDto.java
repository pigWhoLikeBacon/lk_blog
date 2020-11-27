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
import me.zhengjie.domain.Tag;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Collection;

/**
* @website https://el-admin.vip
* @description /
* @author lk
* @date 2020-11-26
**/
@Data
public class ArticleDto implements Serializable {

    /** ID */
    private Long id;

    private Collection<TagDto> tags;

    /** 文章封面 */
    private String cover;

    /** 简介 */
    private String introduce;

    /** 题目 */
    private String title;

    /** 内容 */
    private String content;

    /** 浏览量 */
    private Long views;

    /** 是否展示 */
    private Boolean isShow;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}